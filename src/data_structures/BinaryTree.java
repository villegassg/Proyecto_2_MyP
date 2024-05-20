package data_structures;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public abstract class BinaryTree<T> implements Collection<T> {
    
    protected class Vertex implements BinaryTreeVertex<T> {

        protected T element;
        protected Vertex father;
        protected Vertex left;
        protected Vertex right;

        protected Vertex(T element) {
            this.element = element;
        }

        @Override public boolean hasFather() {
            return father != null;
        }

        @Override public boolean hasLeft() {
            return left != null;
        }

        @Override public boolean hasRight() {
            return right != null;
        }

        @Override public BinaryTreeVertex<T> father() {
            if (hasFather()) 
                return father();
            else throw new NoSuchElementException("There's no father.\n");
        }

        @Override public BinaryTreeVertex<T> left() {
            if (hasLeft()) 
                return left;
            else throw new NoSuchElementException("There's no left.\n");
        }

        @Override public BinaryTreeVertex<T> right() {
            if (hasRight())
                return right;
            else throw new NoSuchElementException("There's no right.\n");
        }

        @Override public int height() {
            return height(this);
        }

        private int height(Vertex v) {
            if (v == null) 
                return -1;
            else {
                int leftHeight = height(v.left);
                int rightHeight = height(v.right);
                return Math.max(leftHeight, rightHeight) + 1;
            }
        }

        @Override public int depth() {
            return depth(this);
        }

        private int depth(Vertex v) {
            return !v.hasFather() ? 0 : 1 + depth(v.father);
        }

        @Override public T get() {
            return element;
        }

        @Override public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            
            @SuppressWarnings("unchecked")
            Vertex vertex = (Vertex)object;

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

        @Override public String toString() {
            return String.format("%s", element);
        }
    }

    protected Vertex root;
    protected int elements;

    public BinaryTree() {}

    public BinaryTree(Collection<T> collection) {
        for (T element : collection)
            add(element);
    }

    protected Vertex newVertex(T element) {
        return new Vertex(element);
    }

    public int height() {
        return isEmpty() ? -1 : root.height();
    }

    @Override public int getElements() {
        return elements;
    }

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

    public BinaryTreeVertex<T> root() {
        if (isEmpty()) 
            throw new NoSuchElementException("The tree is empty. There's no root.\n");

        return root;
    }

    @Override public boolean isEmpty() {
        return root != null;
    }

    @Override public void clear() {
        root = null;
        elements = 0;
    }

    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;

        @SuppressWarnings("unchecked")
        BinaryTree<T> tree = (BinaryTree<T>)object;

        if (this.elements == tree.getElements()) {
            Vertex root1 = root;
            Vertex root2 = (Vertex)tree.root();
            return root1.equals(root2);
        } else 
            return false;
    }

    protected Vertex vertex(BinaryTreeVertex<T> vertex) {
        return (Vertex)vertex;
    }
}
