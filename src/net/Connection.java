package net;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

/**
 * Clase pública Connection. Representará a la conexión existente entre el servidor y 
 * el cliente. 
 * 
 * Una conexión usa sockets para conectarse a un servidor; usa un lector y 
 * un escritor dinámico para intercambiar mensajes con el servidor; nos dice si está 
 * activa, y tiene una lista de escuchas que serán avisados sobre cualquier evento 
 * que ocurra en la conexión.
 */
public class Connection {

    /** El lector dinámico de la conexión. */
    private BufferedReader in;
    /** El escritor dinámico de la conexión. */
    private BufferedWriter out;
    /** El socket de la conexión. */
    private Socket socket;
    /** Nos dice si la conexión está activa. */
    private boolean isActive;
    /** La lista de escuchas de la conexión. */
    private LinkedList<ConnectionListener> listeners;
    
    /**
     * Constructor único. Para construír una conexión, sólo se necesita un sócket que 
     * establezca una conexión. 
     * @param socket el socket.
     * @throws IOException en caso de que ocurra un error al leer o mandar mensajes, 
     *          se emitirá una excepción de entrada/salida.
     */
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.listeners = new LinkedList<>();
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        isActive = true;
    }
    
    /**
     * Permite que la conexión lea cualquier mensaje que haya recibido. Lo procesa 
     * y procede a notificar a todos sus escuchas sobre el mensaje recibido.
     */
    public void receiveMessages() {
        try {
            String read;
            while((read = in.readLine()) != null) {
                for (ConnectionListener listener : listeners) 
                    listener.receivedMessage(this, read);
            }
            isActive = false;
        } catch (SocketException se) {

        } catch (IOException ioe) {
            if (isActive) 
                for (ConnectionListener listener : listeners) 
                    listener.receivedMessage(this, "INVALID");
                System.out.printf("It's been tried to send an invalid message.\n");
            ioe.printStackTrace();
        } 
        for (ConnectionListener listener : listeners) 
            listener.receivedMessage(this, "DISCONNECT");
    }

    /**
     * Envía un mensaje determinado al otro lado de la conexión.
     * @param message el mensaje a enviar.
     * @throws IOException en caso de que ocurra un error al tratar de enviar un 
     *          mensaje, se emitirá una excepción de entrada/salida.
     */
    public void sendMessage(String message) throws IOException {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            throw new IOException("It's been ocurred an error during the send of the " +
                                    "message \"" + message + "\"");
        }
    }

    /**
     * Desconecta a la conexión de donde sea que esté conectada.
     */
    public void disconect() {
        isActive = false;
        try {
            socket.close();
            System.out.println("Connection disconnected.\n");
        } catch (IOException ioe) {}
    }

    /**
     * Nos dice si la conexión está activa.
     * @return <code>true</code> si la conexión está activa, 
     *          <code>false</code> en otro caso.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Permite agregar a un escucha de la conexión a la lista de escuchas de la 
     * conexión.
     * @param listener el escucha de la conexión a agregar.
     */
    public void addListener(ConnectionListener listener) {
        listeners.add(listener);
    }
}
