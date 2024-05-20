package net;

import java.util.LinkedList;

import data_structures.BinaryTreeVertex;
import data_structures.RedBlackTree;

public class SearchByEditorial implements SearchStrategy {

    private RedBlackTree<Book> tree;
    private Database db;

    public SearchByEditorial(Database db) {
        this.db = db;
    }
    
    public LinkedList<Book> search(String property) {
        tree = db.getEditorialsTree();
        Book book = new Book(null, null, null, property, null);
        LinkedList<BinaryTreeVertex<Book>> list = tree.search(book);
        LinkedList<Book> books = new LinkedList<>();
        for (BinaryTreeVertex<Book> v : list) {
            books.add(v.get());
        }
        return books;
    }
}