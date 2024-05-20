package net;

import java.util.LinkedList;

public interface SearchStrategy {
    
    public LinkedList<Book> search(String property);
}
