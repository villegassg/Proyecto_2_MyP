package data_structures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * A binary tree that maintains its elements in order.
 *
 * @param <T> the type of elements in the tree, which must be comparable
 */
public class OrderedBinaryTree<T extends Comparable<T>> extends BinaryTree<T> {

    /**
     * An iterator for the binary tree that iterates over the elements in order.
     */
    private class TreeIterator implements Iterator<T> {

        /** A stack to help with the in-order traversal. */
        private Stack<Vertex> stack;

        /**
         * Constructs a new TreeIterator.
         */
        private TreeIterator() {
            stack = new Stack<>();
            Vertex v = root;
            while (v != null) {
                stack.push(v);
                v = v.left;
            }
        }

        /**
         * Returns true if there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if there are no more elements
         */
        @Override public T next() {
            if (hasNext()) {
                Vertex v = stack.pop();
                T element = v.element;
                if (v.hasRight()) {
                    v = v.right;
                    while (v != null) {
                        stack.push(v);
                        v = v.left;
                    }
                }
                return element;
            } else {
                throw new NoSuchElementException("There's not a next element.\n");
            }
        }
    }

    /** The last added vertex in the tree. */
    protected Vertex lastAdded;
    
    /** The comparator used to compare elements in the tree. */
    protected Comparator<T> comparator;

    /**
     * Constructs an empty OrderedBinaryTree.
     */
    public OrderedBinaryTree() {
        super();
        comparator = null;
    }

    /**
     * Constructs an OrderedBinaryTree with elements from the specified collection.
     *
     * @param collection the collection of elements to add to the tree
     */
    public OrderedBinaryTree(Collection<T> collection) {
        super(collection);
        comparator = null;
    }

    /**
     * Constructs an OrderedBinaryTree with the specified comparator.
     *
     * @param comparator the comparator to use for ordering the elements
     */
    public OrderedBinaryTree(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    /**
     * Constructs an OrderedBinaryTree with the specified comparator and elements from the specified collection.
     *
     * @param comparator the comparator to use for ordering the elements
     * @param collection the collection of elements to add to the tree
     */
    public OrderedBinaryTree(Comparator<T> comparator, Collection<T> collection) {
        super(collection);
        this.comparator = comparator;
    }

    /**
     * Adds an element to the tree.
     *
     * @param element the element to add
     * @throws IllegalArgumentException if the element is null
     */
    @Override public void add(T element) {
        if (element == null)
            throw new IllegalArgumentException("You cannot add a null element.\n");

        Vertex v = newVertex(element);
        elements++;
        lastAdded = v;
        if (root == null)
            root = v;
        else
            addOrdered(root, v);
    }

    /**
     * Adds a vertex to the tree in the correct order.
     *
     * @param v the root vertex to start the insertion from
     * @param w the vertex to be added
     */
    private void addOrdered(Vertex v, Vertex w) {
        if (v == null)
            return;

        int comparison = (comparator == null) ? 
            w.element.compareTo(v.element) : 
            comparator.compare(w.element, v.element); 
        
        if (comparison <= 0) {
            if (v.left == null) {
                v.left = w;
                w.father = v;
            } else {
                addOrdered(v.left, w);
            }
        } else {
            if (v.right == null) {
                v.right = w;
                w.father = v;
            } else {
                addOrdered(v.right, w);
            }
        }
    }

    /**
     * Removes an element from the tree.
     *
     * @param element the element to remove
     */
    @Override public void remove(T element) {
        LinkedList<BinaryTreeVertex<T>> list = search(element);
        for (BinaryTreeVertex<T> vertex : list) {
            Vertex v = vertex(vertex);
            if (v == null)
                return;
            else {
                elements--;
                if (v == root && isLeaf(v))
                    root = null;
                else
                    removeAuxiliary(v);
            }
        }
    }

    /**
     * Removes a vertex from the tree, handling all necessary adjustments.
     *
     * @param v the vertex to remove
     */
    private void removeAuxiliary(Vertex v) {
        if (isLeaf(v)) {
            if (v.father.left == v)
                v.father.left = null;
            else
                v.father.right = null;
        } else {
            if (v.hasLeft() != v.hasRight())
                removeVertex(v);
            else
                removeAuxiliary(exchangeRemovable(v));
        }
    }

    /**
     * Checks if a vertex is a leaf (has no children).
     *
     * @param v the vertex to check
     * @return true if the vertex is a leaf, false otherwise
     */
    private boolean isLeaf(Vertex v) {
        return v.left == null && v.right == null;
    }

    /**
     * Exchanges the elements of two vertices.
     *
     * @param v1 the first vertex
     * @param v2 the second vertex
     * @return the second vertex
     */
    private Vertex exchange(Vertex v1, Vertex v2) {
        Vertex aux = v1;
        v1.element = v2.element;
        v2.element = aux.element;
        return v2;
    }

    /**
     * Finds the maximum element in the subtree rooted at the given vertex.
     *
     * @param v the root of the subtree
     * @return the vertex with the maximum element in the subtree
     */
    private Vertex maximumInSubtree(Vertex v) {
        return !v.hasRight() ? v : maximumInSubtree(v.right);
    }

    /**
     * Exchanges the removable vertex with the maximum vertex in its left subtree.
     *
     * @param vertex the vertex to exchange
     * @return the exchanged vertex
     */
    protected Vertex exchangeRemovable(Vertex vertex) {
        Vertex v = maximumInSubtree(vertex.left);
        return vertex = exchange(vertex, v);
    }

    /**
     * Removes a vertex that has at most one child.
     *
     * @param vertex the vertex to remove
     */
    protected void removeVertex(Vertex vertex) {
        Vertex son = vertex.left != null ? vertex.left : vertex.right;
        if (vertex.father == null) 
            root = son;
        else if (vertex.father.left == vertex)
            vertex.father.left = son;
        else
            vertex.father.right = son;
        if (son != null) son.father = vertex.father;
    }

    /**
     * Searches for vertices containing the specified element.
     *
     * @param element the element to search for
     * @return a list of vertices containing the specified element
     */
    @Override public LinkedList<BinaryTreeVertex<T>> search(T element) {
        LinkedList<BinaryTreeVertex<T>> results = new LinkedList<>();
        return searchVertex(root, element, results);
    }

    /**
     * Recursively searches for vertices containing the specified element.
     *
     * @param v the current vertex
     * @param element the element to search for
     * @param results the list of results found so far
     * @return a list of vertices containing the specified element
     */
    private LinkedList<BinaryTreeVertex<T>> searchVertex(Vertex v, T element, LinkedList<BinaryTreeVertex<T>> results) {
        if (v == null)
            return results;
        else {
            int comparison = (comparator == null) ?
                v.element.compareTo(element) :
                comparator.compare(v.element, element);

            if (comparison == 0) {
                results.add(v);
                return searchVertex(v.left, element, results);
            } else {
                return (comparison > 0) ?
                    searchVertex(v.left, element, results) :
                    searchVertex(v.right, element, results);
            }
        }
    }

    /**
     * Returns the last added vertex in the tree.
     *
     * @return the last added vertex
     */
    public BinaryTreeVertex<T> getLastAdded() {
        return lastAdded;
    }

    /**
     * Performs a right rotation on the given vertex.
     *
     * @param vertex the vertex to rotate
     */
    public void turnRight(BinaryTreeVertex<T> vertex) {
        if (isEmpty() || vertex == null) 
            return;

        Vertex q = vertex(vertex);
        if (!vertex.hasLeft())
            return;

        Vertex p = q.left;
        Vertex s = p.right;
        Vertex t = q.right;
        Vertex u = null;
        boolean isRight = false;
        if (q != root) 
            u = q.father;
        if (u != null && u.right == q) 
            isRight = true;
        p.right = q;
        q.father = p;
        q.left = s;
        q.right = t;
        if (s != null)
            s.father = q;
        if (t != null)
            t.father = q;
        if (u != null) {
            p.father = u;
            if (isRight)
                u.right = p;
            else
                u.left = p;
        } else {
            p.father = null;
            root = p;
        }
    }

    /**
     * Performs a left rotation on the given vertex.
     *
     * @param vertex the vertex to rotate
     */
    public void turnLeft(BinaryTreeVertex<T> vertex) {
        if (isEmpty() || vertex == null)
            return;

        Vertex p = vertex(vertex);
        if (!p.hasRight())
            return;

        Vertex q = p.right;
        Vertex s = q.left;
        Vertex t = p.left;
        Vertex u = p.father;
        p.right = s;
        if (s != null) 
            s.father = p;
        p.left = t;
        p.father = q;
        q.left = p;
        q.father = u;
        if (t != null) 
            t.father = p;
        if (u != null)
            if (u.left == p) 
                u.left = q;
            else 
                u.right = q;
        else 
            root = q;
    }

    /**
     * Returns an iterator over the elements in the tree.
     *
     * @return an iterator over the elements in the tree
     */
    @Override public Iterator<T> iterator() {
        return new TreeIterator();
    }
}
