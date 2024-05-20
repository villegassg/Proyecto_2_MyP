package net;

public class IdGenerator {
    private volatile static IdGenerator idGenerator;
    private Book book;
    private static int bookIdCounter = 1;

    private IdGenerator(Book book) {
        this.book = book;
    }

    public static IdGenerator getIdGenerator(Book book) {
        if (idGenerator == null) {
            synchronized(IdGenerator.class) {
                if (idGenerator == null) {
                    idGenerator = new IdGenerator(book);
                }
            }
        }

        return idGenerator;
    }

    public void generateUniqueId() {
        if (idGenerator == null) {
            System.out.println("You have to initialize the idGenerator first.\n");
            return;
        } else {
            book.setId(bookIdCounter++);
        }
    }
}
