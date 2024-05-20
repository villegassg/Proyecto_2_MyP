package net;

import java.util.LinkedList;

import data_structures.BinaryTreeVertex;
import data_structures.RedBlackTree;

public class SearchByCategory implements SearchStrategy {

    private RedBlackTree<Book> tree;
    private Database db;

    public SearchByCategory(Database db) {
        this.db = db;
    }
    
    public LinkedList<Book> search(String property) {
        tree = db.getCategoriesTree();
        Book book = new Book(null, null, property, null, null);
        LinkedList<BinaryTreeVertex<Book>> list = tree.search(book);
        LinkedList<Book> books = new LinkedList<>();
        for (BinaryTreeVertex<Book> v : list) {
            books.add(v.get());
        }
        return books;
    }
}
