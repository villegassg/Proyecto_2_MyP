package net;

@FunctionalInterface
public interface ServerListener {

    /**
     * Permite a los escuchas procesar un mensaje. 
     * @param format el mensaje a procesar.
     * @param arguments los argumentos del mensaje.
     */
    public void processMessage(String format, Object ... arguments);   
}
