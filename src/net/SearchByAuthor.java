package net;

import data_structures.RedBlackTree;

public class SearchByAuthor implements SearchStrategy {

    private RedBlackTree<Book> tree;
    private Database db;

    public SearchByAuthor(Database db) {
        this.db = db;
    }
    
    public Book search(String property) {
        tree = db.getAuthorsTree();
        Book book = new Book(null, property, null, null, null);
        return tree.search(book).get();
    }
}
