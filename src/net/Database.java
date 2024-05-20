package net;

import java.util.LinkedList;

import data_structures.RedBlackTree;

public class Database {
    
    private RedBlackTree<Book> byName;
    private RedBlackTree<Book> byAuthor;
    private RedBlackTree<Book> byCategory;
    private RedBlackTree<Book> byEditorial;
    private LinkedList<RedBlackTree<Book>> trees;

    public Database() {

        trees = new LinkedList<>();
        byName = new RedBlackTree<>(BookComparators.BY_NAME);
        byAuthor = new RedBlackTree<>(BookComparators.BY_AUTHOR);
        byCategory = new RedBlackTree<>(BookComparators.BY_CATEGORY);
        byEditorial = new RedBlackTree<>(BookComparators.BY_EDITORIAL);

        trees.add(byName);
        trees.add(byAuthor);
        trees.add(byCategory);
        trees.add(byEditorial);
    }

    public void save(Book book) {
        for (RedBlackTree<Book> tree : trees) {
            tree.add(book);
        }
    }

    public void saveAll(Book... books) {
        for (Book book: books) 
            save(book);
    }

    public void delete(Book book) {
        for (RedBlackTree<Book> tree : trees) {
            tree.remove(book);
        }
    }

    public RedBlackTree<Book> getNamesTree() {
        return byName;
    } 

    public RedBlackTree<Book> getAuthorsTree() {
        return byAuthor;
    }

    public RedBlackTree<Book> getCategoriesTree() {
        return byCategory;
    }

    public RedBlackTree<Book> getEditorialsTree() {
        return byEditorial;
    }
}
