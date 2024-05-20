package net;

import data_structures.RedBlackTree;

public class SearchByCategory implements SearchStrategy {

    private RedBlackTree<Book> tree;
    private Database db;

    public SearchByCategory(Database db) {
        this.db = db;
    }
    
    public Book search(String property) {
        tree = db.getCategoriesTree();
        Book book = new Book(null, null, property, null, null);
        return tree.search(book).get();
    }
}
