package net;

@FunctionalInterface
public interface ConnectionListener {
    
    /**
     * Permitirá procesar el mensaje recivido de la conexión.
     * @param connection la conexión.
     * @param message el mensaje recibido.
     */
    public void receivedMessage(Connection connection, String message);
}
