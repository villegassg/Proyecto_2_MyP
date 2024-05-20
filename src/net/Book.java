package net;

public class Book implements Comparable<Book> {

    private class IdGenerator {
        private static int bookIdCounter = 1;

        private static int generateUniqueId() {
            return bookIdCounter++;
        }
    }

    private int id;
    private String name;
    private String author;
    private String category;
    private String editorial;
    private String pathToPortrait;

    public Book(String name, String author, 
            String category, String editorial, String path) {
        id = IdGenerator.generateUniqueId();
        this.name = name;
        this.author = author;
        this.category = category;
        this.editorial = editorial;
        this.pathToPortrait = path;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getPath() {
        return pathToPortrait;
    }

    public void setPathToPortrait(String path) {
        this.pathToPortrait = path;
    }

    @Override
    public int compareTo(Book book) {
        return Integer.compare(this.id, book.id);
    }
}
