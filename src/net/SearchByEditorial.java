package net;

import data_structures.RedBlackTree;

public class SearchByEditorial implements SearchStrategy {

    private RedBlackTree<Book> tree;
    private Database db;

    public SearchByEditorial(Database db) {
        this.db = db;
    }
    
    public Book search(String property) {
        tree = db.getEditorialsTree();
        Book book = new Book(null, null, null, property, null);
        return tree.search(book).get();
    }
}