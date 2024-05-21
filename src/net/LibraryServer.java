package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;

import library.User;
import library.UserProxy;

public class LibraryServer {


    private static final String SEARCH = "SEARCHBY";
    private static final String REQUEST = "REQUEST";

    private static final String CONNECT = "CONNECT";
    private static final String INVALID = "INVALID";
    private static final String BOOK = "BOOK";
    private static final String FINISHRESULTS = "FINISHRESULTS";
    private static final String REQUESTAPROVED = "REQUESTAPROVED";
    private static final String REQUESTDENIED = "REQUESTDENIED";

    private Database database;
    private ServerSocket serverSocket;
    private int port;
    private LinkedList<Connection> connections;
    private boolean isRunning;
    private LinkedList<ServerListener> listeners;
    private SearchStrategy searcher;
    private LinkedList<UserProxy> users;

    public LibraryServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        connections = new LinkedList<>();
        listeners = new LinkedList<>();
        isRunning = true;
        database = createDatabase();
        users = new LinkedList<>();
        UserProxy cesar = new UserProxy(new User("Cesar Villegas", "proyecto1", 320108226));
        UserProxy diego = new UserProxy(new User("Diego Martinez", "proyecto1", 320318429));
        UserProxy beto = new UserProxy(new User("Luis Suarez", "proyecto1", 320222337));
        users.add(cesar);
        users.add(diego);
        users.add(beto);
    }

    public void serve() {
        writeMessage("Listening from the port: %s.\n", port);
        while (isRunning) {
            try {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket);
                writeMessage("Connection received from: %s.\n", 
                                socket.getInetAddress().getCanonicalHostName());
                port = socket.getPort();
                connection.addListener((c, m) -> receivedMessage(c, m));
                synchronized(connections) {
                    connections.add(connection);
                }
                new Thread(() -> connection.receiveMessages()).start();
            } catch (IOException ioe) {
                writeMessage("Error while accepting a new connection.\n");
            }
        }
    }

    /**
     * Agrega un escucha a la lista de escuchas.
     * @param listener el escucha a agregar.
     */
    public void addListener(ServerListener listener) {
        listeners.add(listener);
    }

    /**
     * Limpia la lista de escuchas.
     */
    public void clearListeners() {
        listeners.clear();
    }

    private void receivedMessage(Connection connection, String message) {
        if (connection.isActive()) {
            if (message.startsWith(SEARCH)) {
                search(connection, message);
                writeMessage("Search function asked by port %d", port);
            } else if (message.startsWith(REQUEST)) {
                request(connection, message);
            }
        }
    }

    private void search(Connection connection, String message) {
        String [] msg = message.split("_");
        String method = msg[1];
        String property = msg[2];
        LinkedList<Book> results = new LinkedList<>();
        switch (method) {
            case "NAME" :
                searcher = new SearchByName(database);
                results = searcher.search(property);
                break;
            case "AUTHOR" :
                searcher = new SearchByAuthor(database);
                results = searcher.search(property);
                break;
            case "CATEGORY" :
                searcher = new SearchByCategory(database);
                results = searcher.search(property);
                break;
            case "EDITORIAL" :
                searcher = new SearchByEditorial(database);
                results = searcher.search(property);
                break;
            default : 
                try {
                    connection.sendMessage(INVALID);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
        }
        Iterator<Book> iterator = results.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            try {
                connection.sendMessage(BOOK + "_" + book.toStringWithTabs());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            connection.sendMessage(FINISHRESULTS);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void request(Connection connection, String message) {
        String[] m = message.split("_");
        String name = m[1] + " " + m[2];
        long accountNumber = Long.parseLong(m[3]);
        String book = m[4];
        boolean found = false;
        writeMessage("Request for the book %s by port %d", book, port);
        for (UserProxy user : users) {
            if (user.getName().equals(name) && 
                user.getAccountNumber() == accountNumber) {
                found = true;
                break;
            }
        }

        if (found) {
            try {
                connection.sendMessage(REQUESTAPROVED + book);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            try {
                connection.sendMessage(REQUESTDENIED);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Permite a una conexión dada desconectarse del servidor de manera elegante.
     * @param connection la conexión que será desconectada.
     */
    private void toDisconnect(Connection connection) {
        writeMessage("Disconnect request by port %d", port);
        disconnect(connection);
    }
    
    /**
     * Maneja un error producido en una conexión y la desconecta.
     * @param connection la conexión a desconectar.
     * @param message el mensaje a mostrar en la pantalla del servidor.
     */
    private void error(Connection connection, String message) {
        writeMessage(message);
        writeMessage("Disconnecting the connection from the port %d: Invalid message", 
                        port);
        disconnect(connection);
    }

    /**
     * Desconecta a una conexión.
     * @param connection la conexión a desconectar.
     */
    private void disconnect(Connection connection) {
        synchronized(connections) {
            connections.remove(connection);
        }
        connection.disconect();
        writeMessage("The connection %d has been disconnected.\n", port);
    }

    /**
     * Permite al servidor del cliente ejecutar la acción de conectarse al menú inicial de la 
     * conexión.
     * @param connection la conexión.
     */
    private void connect(Connection connection) {
        try {
            connection.sendMessage(CONNECT);
        } catch (IOException ioe) {}
    }

    /**
     * Imprime un mensaje en la pantalla del servidor.
     * @param format el formato del mensaje.
     * @param arguments los argumentos del mensaje.
     */
    private void writeMessage(String format, Object ... arguments) {
        for (ServerListener listener : listeners) 
            listener.processMessage(format, arguments);
    }

    public Database createDatabase() {
        Database db = new Database(); 

        Book book1 = new Book("El Señor de los Anillos", "J.R.R. Tolkien", 
            "Fantasía", "Minotauro", "resources/portraits/el_señor_de_los_anillos.jpg");
        Book book2 = new Book("Cien años de soledad", "Gabriel García Márquez", 
            "Realismo mágico", "Diana", "resources/portraits/cien_años_de_soledad.jpg");
        Book book3 = new Book("Harry Potter y la piedra filosofal", "J.K. Rowling", 
            "Fantasía", "Salamandra", "resources/portraits/harry_potter_y_la_piedra_filosofal.jpg");
        Book book4 = new Book("1984", "George Orwell", 
            "Ciencia ficción", "Debolsillo", "resources/portraits/1984.jpg");

        db.saveAll(book1, book2, book3, book4);

        return db;
    }
}
