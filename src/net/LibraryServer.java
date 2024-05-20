package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class LibraryServer {

    private static final String CONNECT = "CONNECT";

    private Database database;
    private ServerSocket serverSocket;
    private int port;
    private LinkedList<Connection> connections;
    private boolean isRunning;
    private LinkedList<ServerListener> listeners;
    //private LinkedList<ClientProxy> clients;

    public LibraryServer(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        connections = new LinkedList<>();
        listeners = new LinkedList<>();
        isRunning = true;
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
            "Fantasía", "Minotauro", "src/resources/portraits/el_señor_de_los_anillos.jpg");
        Book book2 = new Book("Cien años de soledad", "Gabriel García Márquez", 
            "Realismo mágico", "Diana", "src/resources/portraits/cien_años_de_soledad.jpg");
        Book book3 = new Book("Harry Potter y la piedra filosofal", "J.K. Rowling", 
            "Fantasía", "Salamandra", "src/resources/portraits/harry_potter_y_la_piedra_filosofal.jpg");
        Book book4 = new Book("1984", "George Orwell", 
            "Ciencia ficción", "Debolsillo", "src/resources/portraits/1984.jpg");

        db.saveAll(book1, book2, book3, book4);

        return db;
    }
}
