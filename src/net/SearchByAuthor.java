package net;

import java.util.LinkedList;

import data_structures.BinaryTreeVertex;
import data_structures.RedBlackTree;

public class SearchByAuthor implements SearchStrategy {

    private RedBlackTree<Book> tree;
    private Database db;

    public SearchByAuthor(Database db) {
        this.db = db;
    }
    
    public LinkedList<Book> search(String property) {
        tree = db.getAuthorsTree();
        Book book = new Book(null, property, null, null, null);
        LinkedList<BinaryTreeVertex<Book>> list = tree.search(book);
        LinkedList<Book> books = new LinkedList<>();
        for (BinaryTreeVertex<Book> v : list) {
            books.add(v.get());
        }
        return books;
    }
}
