package data_structures;

public interface BinaryTreeVertex<T> {
    
    public boolean hasFather();

    public boolean hasLeft();

    public boolean hasRight();

    public BinaryTreeVertex<T> father();

    public BinaryTreeVertex<T> left();

    public BinaryTreeVertex<T> right();

    public int height();

    public int depth();

    public T get();
}
