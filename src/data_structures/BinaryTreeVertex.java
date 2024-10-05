package data_structures;

/**
 * Interface representing a vertex in a binary tree.
 *
 * @param <T> the type of element held in the vertex
 */
public interface BinaryTreeVertex<T> {
    
    /**
     * Checks if the vertex has a father (parent).
     *
     * @return true if the vertex has a father, false otherwise
     */
    public boolean hasFather();

    /**
     * Checks if the vertex has a left child.
     *
     * @return true if the vertex has a left child, false otherwise
     */
    public boolean hasLeft();

    /**
     * Checks if the vertex has a right child.
     *
     * @return true if the vertex has a right child, false otherwise
     */
    public boolean hasRight();

    /**
     * Returns the father (parent) of the vertex.
     *
     * @return the father of the vertex
     */
    public BinaryTreeVertex<T> father();

    /**
     * Returns the left child of the vertex.
     *
     * @return the left child of the vertex
     */
    public BinaryTreeVertex<T> left();

    /**
     * Returns the right child of the vertex.
     *
     * @return the right child of the vertex
     */
    public BinaryTreeVertex<T> right();

    /**
     * Calculates and returns the height of the vertex.
     *
     * @return the height of the vertex
     */
    public int height();

    /**
     * Calculates and returns the depth of the vertex.
     *
     * @return the depth of the vertex
     */
    public int depth();

    /**
     * Returns the element held in the vertex.
     *
     * @return the element held in the vertex
     */
    public T get();
}
