package library;

public class User implements UserInterface {
    private String name;
    private int password;
    private long accountNumber;
    private int numberOfRequests;

    public User(String name, String password, long accountNumber) {
        this.name = name;
        this.password = hash(password);
        this.accountNumber = accountNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPassword() {
        return password;
    }

    @Override
    public long getAccountNumber() {
        return accountNumber;
    }

    @Override
    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    @Override
    public void setNumberOfRequests(int number) {
        numberOfRequests = number;
    }

    /**
     * Función de dispersión para contraseñas.
     * @param str la contraseña.
     * @return un entero positivo que es la dispersión de la contraseña.
     */
    private int hash(String str) {
        int hash = 5381;
        
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        
        return hash & 0x7FFFFFFF; // To make sure the result is a positive integer.
    }
}
