package data_structures;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;

public class OrderedBinaryTree<T extends Comparable<T>> extends BinaryTree<T> {

    private class TreeIterator implements Iterator<T> {

        private Stack<Vertex> stack;

        private TreeIterator() {
            stack = new Stack<>();
            Vertex v = root;
            while (v != null) {
                stack.push(v);
                v = v.left;
            }
        }

        @Override public boolean hasNext() {
            return !stack.isEmpty();
        }

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
            } else 
                throw new NoSuchElementException("There's not a next element.\n");
        }
    }

    protected Vertex lastAdded;
    protected Comparator<T> comparator;

    public OrderedBinaryTree() {
        super();
        comparator = null;
    }

    public OrderedBinaryTree(Collection<T> collection) {
        super(collection);
        comparator = null;
    }

    public OrderedBinaryTree(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    public OrderedBinaryTree(Comparator<T> comparator, Collection<T> collection) {
        super(collection);
        this.comparator = comparator;
    }

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

    private void addOrdered(Vertex v, Vertex w) {
        if (v == null) 
            return;

        int comparison = (comparator == null) ? 
            w.element.compareTo(v.element) : 
            comparator.compare(w.element, v.element); 
        
        if (comparison <= 0)
            if (v.left == null) {
                v.left = w;
                w.father = v;
            } else 
                addOrdered(v.left, w);
        else 
            if (v.right == null) {
                v.right = w;
                w.father = v;
            } else 
                addOrdered(v.right, w);
    }

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

    private void removeAuxiliary(Vertex v) {
        if (isLeaf(v)) 
            if (v.father.left == v)
                v.father.left = null;
            else 
                v.father.right = null;
        else 
            if (v.hasLeft() != v.hasRight())
                removeVertex(v);
            else 
                removeAuxiliary(exchangeRemovable(v));
    }

    private boolean isLeaf(Vertex v) {
        return v.left == null && v.right == null;
    }

    private Vertex exchange(Vertex v1, Vertex v2) {
        Vertex aux = v1;
        v1.element = v2.element;
        v2.element = aux.element;
        return v2;
    }

    private Vertex maximumInSubtree(Vertex v) {
        return !v.hasRight() ? v : maximumInSubtree(v.right);
    }

    protected Vertex exchangeRemovable(Vertex vertex) {
        Vertex v = maximumInSubtree(vertex.left);
        return vertex = exchange(vertex, v);
    }

    protected void removeVertex(Vertex vertex) {
        Vertex son = vertex.left != null ? vertex.left : vertex.right;
        if (vertex.father == null) 
            root = son;
        else if (vertex.father.left == vertex)
            vertex.father.left = son;
        else vertex.father.right = son;
        if (son != null) son.father = vertex.father;
    }

    @Override public LinkedList<BinaryTreeVertex<T>> search(T element) {
        LinkedList<BinaryTreeVertex<T>> results = new LinkedList<>();
        return searchVertex(root, element, results);
    }

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
            } else 
                return (comparison > 0) ?
                    searchVertex(v.left, element, results) :
                    searchVertex(v.right, element, results);
        }
    }

    public BinaryTreeVertex<T> getLastAdded() {
        return lastAdded;
    }

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
            else u.left = p;
        } else {
            p.father = null;
            root = p;
        }
    }

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

    @Override public Iterator<T> iterator() {
        return new TreeIterator();
    }
}
