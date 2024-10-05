package data_structures;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * An abstract class representing a binary tree. Implements the Collection interface.
 *
 * @param <T> the type of elements in the tree
 */
public abstract class BinaryTree<T> implements Collection<T> {

    /**
     * Protected class representing a vertex (node) in the binary tree.
     */
    protected class Vertex implements BinaryTreeVertex<T> {

        /** The element stored in the vertex. */
        protected T element;
        
        /** The father (parent) of the vertex. */
        protected Vertex father;
        
        /** The left child of the vertex. */
        protected Vertex left;
        
        /** The right child of the vertex. */
        protected Vertex right;

        /**
         * Constructor that initializes the vertex with the given element.
         *
         * @param element the element to be stored in the vertex
         */
        protected Vertex(T element) {
            this.element = element;
        }

        /**
         * Checks if the vertex has a father (parent).
         *
         * @return true if the vertex has a father, false otherwise
         */
        @Override public boolean hasFather() {
            return father != null;
        }

        /**
         * Checks if the vertex has a left child.
         *
         * @return true if the vertex has a left child, false otherwise
         */
        @Override public boolean hasLeft() {
            return left != null;
        }

        /**
         * Checks if the vertex has a right child.
         *
         * @return true if the vertex has a right child, false otherwise
         */
        @Override public boolean hasRight() {
            return right != null;
        }

        /**
         * Returns the father (parent) of the vertex.
         *
         * @return the father of the vertex
         * @throws NoSuchElementException if the vertex has no father
         */
        @Override public BinaryTreeVertex<T> father() {
            if (hasFather()) 
                return father;
            else throw new NoSuchElementException("There's no father.\n");
        }

        /**
         * Returns the left child of the vertex.
         *
         * @return the left child of the vertex
         * @throws NoSuchElementException if the vertex has no left child
         */
        @Override public BinaryTreeVertex<T> left() {
            if (hasLeft()) 
                return left;
            else throw new NoSuchElementException("There's no left.\n");
        }

        /**
         * Returns the right child of the vertex.
         *
         * @return the right child of the vertex
         * @throws NoSuchElementException if the vertex has no right child
         */
        @Override public BinaryTreeVertex<T> right() {
            if (hasRight())
                return right;
            else throw new NoSuchElementException("There's no right.\n");
        }

        /**
         * Returns the height of the subtree rooted at this vertex.
         *
         * @return the height of the subtree
         */
        @Override public int height() {
            return height(this);
        }

        /**
         * Recursively calculates the height of the subtree rooted at the given vertex.
         *
         * @param v the vertex whose subtree height is to be calculated
         * @return the height of the subtree, or -1 if the vertex is null
         */
        private int height(Vertex v) {
            if (v == null) 
                return -1;
            else {
                int leftHeight = height(v.left);
                int rightHeight = height(v.right);
                return Math.max(leftHeight, rightHeight) + 1;
            }
        }

        /**
         * Returns the depth of this vertex in the tree.
         *
         * @return the depth of the vertex
         */
        @Override public int depth() {
            return depth(this);
        }

        /**
         * Recursively calculates the depth of the given vertex in the tree.
         *
         * @param v the vertex whose depth is to be calculated
         * @return the depth of the vertex
         */
        private int depth(Vertex v) {
            return !v.hasFather() ? 0 : 1 + depth(v.father);
        }

        /**
         * Returns the element stored in the vertex.
         *
         * @return the element stored in the vertex
         */
        @Override public T get() {
            return element;
        }

        /**
         * Compares this vertex to the specified object for equality.
         *
         * @param object the object to be compared for equality with this vertex
         * @return true if the specified object is equal to this vertex
         */
        @Override public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            
            @SuppressWarnings("unchecked")
            Vertex vertex = (Vertex) object;

            if (!this.element.equals(vertex.element))
                return false;

            Queue<Vertex> queue1 = new LinkedList<>();
            Queue<Vertex> queue2 = new LinkedList<>();

            queue1.offer(this);
            queue2.offer(vertex);

            while (!queue1.isEmpty() && !queue2.isEmpty()) {
                Vertex v1 = queue1.poll();
                Vertex v2 = queue2.poll();

                if (!v1.element.equals(v2.element))
                    return false;
            
                if (v1.hasLeft() != v2.hasLeft() || v1.hasRight() != v2.hasRight()) 
                    return false;

                if (v1.hasLeft()) {
                    queue1.offer(v1.left);
                    queue2.offer(v2.left);
                }

                if (v1.hasRight()) {
                    queue1.offer(v1.right);
                    queue2.offer(v2.right);
                }
            }

            return true;
        }

        /**
         * Returns a string representation of the vertex.
         *
         * @return a string representation of the vertex
         */
        @Override public String toString() {
            return String.format("%s", element);
        }
    }

    /** The root of the binary tree. */
    protected Vertex root;
    
    /** The number of elements in the tree. */
    protected int elements;

    /**
     * Default constructor that creates an empty binary tree.
     */
    public BinaryTree() {}

    /**
     * Constructor that creates a binary tree from the elements of the given collection.
     *
     * @param collection the collection of elements to be added to the tree
     */
    public BinaryTree(Collection<T> collection) {
        for (T element : collection)
            add(element);
    }

    /**
     * Creates a new vertex with the given element.
     *
     * @param element the element to be stored in the new vertex
     * @return the newly created vertex
     */
    protected Vertex newVertex(T element) {
        return new Vertex(element);
    }

    /**
     * Returns the height of the tree.
     *
     * @return the height of the tree, or -1 if the tree is empty
     */
    public int height() {
        return isEmpty() ? -1 : root.height();
    }

    /**
     * Returns the number of elements in the tree.
     *
     * @return the number of elements in the tree
     */
    @Override public int getElements() {
        return elements;
    }

    /**
     * Checks if the tree contains the specified element.
     *
     * @param element the element to be checked for containment
     * @return true if the tree contains the specified element, false otherwise
     */
    @Override public boolean contains(T element) {
        if (element == null)
            return false;

        Queue<Vertex> queue = new LinkedList<>();

        if (root != null)
            queue.offer(root);

        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (v.element.equals(element))
                return true;
            else {
                if (v.hasLeft())
                    queue.offer(v.left);

                if (v.hasRight())
                    queue.offer(v.right);
            }
        }

        return false;
    }

    /**
     * Searches for vertices containing the specified element.
     *
     * @param element the element to be searched for
     * @return a list of vertices containing the specified element
     */
    public LinkedList<BinaryTreeVertex<T>> search(T element) {
        if (element == null)
            return null;

        LinkedList<BinaryTreeVertex<T>> list = new LinkedList<>();
        Queue<Vertex> queue = new LinkedList<>();

        if (root != null)
            queue.offer(root);

        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (v.element.equals(element))
                list.add(v);
            else {
                if (v.hasLeft())
                    queue.offer(v.left);

                if (v.hasRight())
                    queue.offer(v.right);
            }
        }

        return list;
    }

    /**
     * Returns the root vertex of the tree.
     *
     * @return the root vertex of the tree
     * @throws NoSuchElementException if the tree is empty
     */
    public BinaryTreeVertex<T> root() {
        if (isEmpty()) 
            throw new NoSuchElementException("The tree is empty. There's no root.\n");

        return root;
    }

    /**
     * Checks if the tree is empty.
     *
     * @return true if the tree is empty, false otherwise
     */
    @Override public boolean isEmpty() {
        return root == null;
    }

    /**
     * Clears the tree, removing all elements.
     */
    @Override public void clear() {
        root = null;
        elements = 0;
    }

    /**
     * Compares this tree to the specified object for equality.
     *
     * @param object the object to be compared for equality with this tree
     * @return true if the specified object is equal to this tree
     */
    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;

        @SuppressWarnings("unchecked")
        BinaryTree<T> tree = (BinaryTree<T>) object;

        if (this.elements == tree.getElements()) {
            Vertex root1 = root;
            Vertex root2 = (Vertex) tree.root();
            return root1.equals(root2);
        } else 
            return false;
    }

    /**
     * Casts a BinaryTreeVertex to a Vertex.
     *
     * @param vertex the vertex to be cast
     * @return the casted Vertex
     */
    protected Vertex vertex(BinaryTreeVertex<T> vertex) {
        return (Vertex) vertex;
    }
}
