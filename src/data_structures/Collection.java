package data_structures;

/**
 * Interface representing a generic collection of elements.
 *
 * @param <T> the type of elements in the collection
 */
public interface Collection<T> extends Iterable<T> {

    /**
     * Adds an element to the collection.
     *
     * @param element the element to be added
     */
    public void add(T element);

    /**
     * Removes an element from the collection.
     *
     * @param element the element to be removed
     */
    public void remove(T element);

    /**
     * Checks if the collection contains the specified element.
     *
     * @param element the element to be checked for containment
     * @return true if the collection contains the element, false otherwise
     */
    public boolean contains(T element);

    /**
     * Checks if the collection is empty.
     *
     * @return true if the collection is empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the number of elements in the collection.
     *
     * @return the number of elements in the collection
     */
    public int getElements();

    /**
     * Clears all elements from the collection.
     */
    public void clear();
}
