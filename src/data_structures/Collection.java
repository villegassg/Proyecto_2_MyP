package data_structures;

public interface Collection<T> extends Iterable<T> {
    
    public void add(T element);

    public void remove(T element);

    public boolean contains(T element);

    public boolean isEmpty();

    public int getElements();

    public void clear();
}
