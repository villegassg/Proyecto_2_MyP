package net;

import java.util.Comparator;

public class BookComparators {
    public static final Comparator<Book> BY_NAME = Comparator.comparing(Book::getName);
    public static final Comparator<Book> BY_AUTHOR = Comparator.comparing(Book::getAuthor);
    public static final Comparator<Book> BY_CATEGORY = Comparator.comparing(Book::getCategory);
    public static final Comparator<Book> BY_EDITORIAL = Comparator.comparing(Book::getEditorial);
}
