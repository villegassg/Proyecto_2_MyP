package library;

import java.util.LinkedList;
import net.Book;

public class UserProxy {
    private User user;
    private String name;
    private int password;
    private long accountNumber;
    private int numberOfRequests;
    private LinkedList<Book> askedBooks;

    public UserProxy(User user) {
        this.user = user;
        name = user.getName();
        password = user.getPassword();
        accountNumber = user.getAccountNumber();
        numberOfRequests = user.getNumberOfRequests();
        askedBooks = new LinkedList<>();
    }

    public void setNumberOfRequests(int number) {
        numberOfRequests = number;
        user.setNumberOfRequests(number);
    }

    public String getName() {
        return name;
    }

    public int getPassword() {
        return password;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public int getNumberOfRequests() {
        return numberOfRequests;
    }

    public void addToAskedBooks(Book book) {
        askedBooks.add(book);
    }

    public void removeFromAskedBooks(Book book) {
        askedBooks.remove(book);
    }

    public LinkedList<Book> getAskedBooks() {
        return askedBooks;
    }

    public void clearAskedBooks() {
        askedBooks.clear();
    }

    @Override public String toString() {
        String n = String.format("Name: %s\n", name);
        String p = String.format("Password: %d\n", password);
        String an = String.format("Account Number: %d\n", accountNumber);
        String nr = String.format("Number of Requests: %s\n", numberOfRequests);
        return n + p + an + nr;
    }

    public String toStringWithTabs() {
        String n = String.format("Name: %s\t", name);
        String p = String.format("Password: %d\t", password);
        String an = String.format("Account Number: %d\t", accountNumber);
        String nr = String.format("Number of Requests: %s\t", numberOfRequests);
        return n + p + an + nr;
    }

    @Override public boolean equals(Object object) {
        if (!(object instanceof UserProxy))
            return false;

        UserProxy proxy = (UserProxy)object;

        return this.name.equals(proxy.getName()) &&
                this.password == proxy.getPassword() &&
                this.accountNumber == proxy.getAccountNumber() &&
                this.numberOfRequests == proxy.getNumberOfRequests();
    }
}
