package net;

public class Book implements Comparable<Book> {

    private int id;
    private String name;
    private String author;
    private String category;
    private String editorial;
    private String pathToPortrait;

    public Book(String name, String author, 
            String category, String editorial, String path) {
        IdGenerator idg = IdGenerator.getIdGenerator(this);
        idg.generateUniqueId();
        this.name = name;
        this.author = author;
        this.category = category;
        this.editorial = editorial;
        this.pathToPortrait = path;
    }

    public Book(int id, String name, String author, 
            String category, String editorial, String path) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.category = category;
        this.editorial = editorial;
        this.pathToPortrait = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        String ID = String.format("Id: %d\n", id);
        String NAME = String.format("Name: %s\n", name);
        String AUTHOR = String.format("Author: %s\n", author);
        String CATEGORY = String.format("Category: %s\n", category);
        String EDITORIAL = String.format("Editorial: %s\n", editorial);

        return ID + NAME + AUTHOR + CATEGORY + EDITORIAL;
    }
    
    public String toStringWithTabs() {
        String ID = String.format("Id: %d\t", id);
        String NAME = String.format("Name: %s\t", name);
        String AUTHOR = String.format("Author: %s\t", author);
        String CATEGORY = String.format("Category: %s\t", category);
        String EDITORIAL = String.format("Editorial: %s\t", editorial);
        String PATH = String.format("Path: %s\t", pathToPortrait);

        return ID + NAME + AUTHOR + CATEGORY + EDITORIAL + PATH;
    }
}
