package data_structures;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * Represents a Red-Black Tree, which is a self-balancing binary search tree.
 * It extends the OrderedBinaryTree class and implements the necessary methods
 * to maintain the Red-Black Tree properties.
 *
 * @param <T> the type of elements stored in the Red-Black Tree
 */
public class RedBlackTree<T extends Comparable<T>> extends OrderedBinaryTree<T> {
    
    /**
     * Represents a vertex in a Red-Black Tree.
     */
    protected class RedBlackVertex extends Vertex {

        /**
         * The color of the vertex.
         */
        public Color color;

        /**
         * Constructs a new RedBlackVertex object with the specified element.
         *
         * @param element the element to be stored in the vertex
         */
        public RedBlackVertex(T element) {
            super(element);
            color = Color.NONE;
        }

        /**
         * Returns a string representation of the vertex.
         *
         * @return a string representation of the vertex
         */
        @Override
        public String toString() {
            if (this.color == Color.RED)
                return String.format("R{%s}", this.element);
            else
                return String.format("B{%s}", this.element);
        }

        /**
         * Checks if this vertex is equal to the specified object.
         *
         * @param object the object to compare with
         * @return true if the object is equal to this vertex, false otherwise
         */
        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            @SuppressWarnings("unchecked")
            RedBlackVertex vertex = (RedBlackVertex) object;

            return this.color == vertex.color && super.equals(object);
        }
    }

    /**
     * Creates a Red-Black Tree which has no comparators neither elements.
     * Just calls the super class constructor.
     */
    public RedBlackTree() {
        super();
        comparator = null;
    }

    /**
     * Creates a Red-Black Tree which has no comparators but with the elements 
     * of a determined collection of T objects. Just calls the super class constructor.
     * 
     * @param collection the collection. 
     */
    public RedBlackTree(Collection<T> collection) {
        super(collection);
        comparator = null;
    }

    /**
     * Creates a Red-Black Tree which has a comparator to compare its elements to 
     * order them, but has no elements. It just calles the super class constructor.
     * 
     * @param comparator the comparator.
     */
    public RedBlackTree(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    /**
     * Creates a Red-Black Tree which has a comparator to compare its elements 
     * to order them, and the elements of a determined collection.
     * 
     * @param comparator the comparator.
     * @param collection the collection.
     */
    public RedBlackTree(Comparator<T> comparator, Collection<T> collection) {
        super(collection);
        this.comparator = comparator;
    }

    /**
     * Creates a new Vertex instance of RedBlackVertex.
     * 
     * @param T element.
     */
    @Override protected Vertex newVertex(T element) {
        return new RedBlackVertex(element);
    }

    /**
     * Gets the color of a determined vertex.
     * @param vertex the vertex.
     * @return the color of the vertex.
     */
    public Color getColor(BinaryTreeVertex<T> vertex) {
        if (vertex instanceof RedBlackVertex) {
            RedBlackVertex v = (RedBlackVertex)vertex;
            return v.color;
        } else 
            throw new ClassCastException("Cannot get the vertex's color. " + 
                "It's not instance of RedBlackVertex.\n");
    }


    /**
     * Adds the specified element to the Red-Black Tree.
     * Overrides the add method from the superclass.
     * After adding the element, the method rebalances the tree to maintain the 
     * Red-Black Tree properties.
     *
     * @param element the element to be added to the tree
     */
    @Override public void add(T element) {
        super.add(element);
        RedBlackVertex v = redBlackVertex(lastAdded);
        v.color = Color.RED;
        rebalanceToAdd(v);
    }

    /**
     * Rebalances the Red-Black Tree after adding a vertex.
     * This method is called recursively to ensure that the Red-Black Tree 
     * properties are maintained.
     * 
     * @param v the vertex to be rebalanced.
     */
    private void rebalanceToAdd(RedBlackVertex v) {
        RedBlackVertex father = getFather(v);
        // Case 1.
        if (father == null) {
            v.color = Color.BLACK;
            return;
        } else if (color(father) == Color.BLACK) // Case 2.
            return;
        else {
            // Case 3.
            RedBlackVertex grandpa = getGrandpa(v);
            RedBlackVertex uncle = getUncle(v);
            if (color(uncle) == Color.RED) {
                uncle.color = Color.BLACK;
                father.color = Color.BLACK;
                grandpa.color = Color.RED;
                rebalanceToAdd(grandpa);
                return;
            } else {
                // Case 4.
                if (isCrossedRight(v)) {
                    super.turnLeft(father);
                    RedBlackVertex aux = v;
                    v = father;
                    father = aux;
                } else if (isCrossedLeft(v)) {
                    super.turnRight(father);
                    RedBlackVertex aux = v;
                    v = father;
                    father = aux;
                }
                // Case 5.
                father.color = Color.BLACK;
                grandpa.color = Color.RED;
                if (areLeft(v))
                    super.turnRight(grandpa);
                else if (areRight(v))
                    super.turnLeft(grandpa);
                return;
            }
        }
    }

    /**
     * Removes the specified element from the Red-Black Tree.
     * If the element is found multiple times in the tree, all occurrences will be removed.
     * 
     * @param element the element to be removed from the tree
     */
    @Override public void remove(T element) {
        LinkedList<BinaryTreeVertex<T>> list = search(element);
        for (BinaryTreeVertex<T> vertex : list) {
            RedBlackVertex v = redBlackVertex(vertex);
            if (v == null) 
                return;
            elements--;
            RedBlackVertex son = null;
            if (hasTwoChildren(v)) {
                v = redBlackVertex(exchangeRemovable(v));
                son = (RedBlackVertex)v.left;
            } else if (onlyHasLeft(v))
                son = (RedBlackVertex)v.left;
            else if (onlyHasRight(v))
                son = (RedBlackVertex)v.right;

            if (color(son) == Color.RED) {
                removeVertex(v);
                son.color = Color.BLACK;
                return;
            } else if (color(v) == Color.RED) {
                removeVertex(v);
                return;
            } else if (son == null && color(v) == Color.BLACK) {
                rebalanceToRemove(v);
                removeVertex(v);
            } else if (son != null && color(son) == Color.BLACK && color(v) == Color.BLACK) {
                removeVertex(v);
                rebalanceToRemove(son);
            }
        }
    }

    /**
     * Rebalances the Red-Black Tree after removing a vertex.
     * This method is called recursively to maintain the Red-Black Tree properties.
     *
     * @param v The vertex to rebalance from.
     */
    private void rebalanceToRemove(RedBlackVertex v) {
        RedBlackVertex father = getFather(v);
        // Case 1.
        if (father == null)
            return;
        // Case 2.
        RedBlackVertex brother = getBrother(v);
        if (color(brother) == Color.RED) {
            father.color = Color.RED;
            brother.color = Color.BLACK;
            if (isLeft(v))
                super.turnLeft(father);
            else   
                super.turnRight(father);
            brother = getBrother(v);
        }
        // Case 3.
        RedBlackVertex leftSon = redBlackVertex(brother.left);
        RedBlackVertex rightSon = redBlackVertex(brother.right);
        if (color(father) == Color.BLACK && color(brother) == Color.BLACK &&
            color(leftSon) == Color.BLACK && color(rightSon) == Color.BLACK) {
            brother.color = Color.RED;
            rebalanceToRemove(father);
            return;
        }
        // Case 4.
        if (color(brother) == Color.BLACK && color(leftSon) == Color.BLACK &&
            color(rightSon) == Color.BLACK && color(father) == Color.RED) {
            brother.color = Color.RED;
            father.color = Color.BLACK;
            return;
        }
        // Case 5.
        if (isLeft(v) && color(leftSon) == Color.RED && color(rightSon) == Color.BLACK) {
            brother.color = Color.RED;
            leftSon.color = Color.BLACK;
            super.turnRight(brother);
            brother = getBrother(v);
            leftSon = redBlackVertex(brother.left);
            rightSon = redBlackVertex(brother.right);
        } else if (isRight(v) && color(leftSon) == Color.BLACK && color(rightSon) == Color.RED) {
            brother.color = Color.RED;
            rightSon.color = Color.BLACK;
            super.turnLeft(brother);
            brother = getBrother(v);
            leftSon = redBlackVertex(brother.left);
            rightSon = redBlackVertex(brother.right);
        }
        // Case 6.
        brother.color = father.color;
        father.color = Color.BLACK;
        if (isLeft(v)) {
            rightSon.color = Color.BLACK;
            super.turnLeft(father);
        } else {
            leftSon.color = Color.BLACK;
            super.turnRight(father);
        }
    }

    /**
     * This method is used to perform a left rotation on the specified vertex 
     * in a Red-Black Tree.
     * 
     * @param vertex the vertex on which the left rotation is to be performed.
     * @throws UnsupportedOperationException if the user attempts to turn a 
     *          Red-Black Tree to the left
     */
    @Override public void turnLeft(BinaryTreeVertex<T> vertex) {
        throw new UnsupportedOperationException("Red-Black Trees cannot be " + 
            "turned to the left by the user.\n");
    }

    /**
     * This method is used to perform a right rotation on the specified vertex 
     * in a Red-Black Tree.
     * 
     * @param vertex the vertex on which the right rotation is to be performed.
     * @throws UnsupportedOperationException if the user tries to perform a 
     *          right rotation on a Red-Black Tree
     */
    @Override public void turnRight(BinaryTreeVertex<T> vertex) {
        throw new UnsupportedOperationException("Red-Black Trees cannot be " + 
            "turned to the right by the user.\n");
    }

    /**
     * Returns the father (parent) of the given vertex.
     * 
     * @param v the vertex whose father is to be returned
     * @return the father of the given vertex, or null if the vertex is null
     */
    private RedBlackVertex getFather(RedBlackVertex v) {
        return v != null ? redBlackVertex(v.father) : null;
    }

    /**
     * Returns the grandfather (parent of the parent) of the given vertex.
     * 
     * @param v the vertex whose grandfather is to be returned
     * @return the grandfather of the given vertex, or null if the vertex has no grandfather
     */
    private RedBlackVertex getGrandpa(RedBlackVertex v) {
        return getFather(getFather(v));
    }

    /**
     * Returns the uncle (sibling of the parent) of the given vertex.
     * 
     * @param v the vertex whose uncle is to be returned
     * @return the uncle of the given vertex, or null if the vertex has no uncle
     */
    private RedBlackVertex getUncle(RedBlackVertex v) {
        return getBrother(getFather(v));
    }

    /**
     * Returns the brother (sibling) of the given vertex.
     * 
     * @param v the vertex whose brother is to be returned
     * @return the brother of the given vertex, or null if the vertex has no brother
     */
    private RedBlackVertex getBrother(RedBlackVertex v) {
        RedBlackVertex father = getFather(v);
        if (v != null && father != null) {
            if (isLeft(v))
                return father.hasRight() ? redBlackVertex(father.right) : null;
            else
                return father.hasLeft() ? redBlackVertex(father.left) : null;
        } else return null;
    }

    /**
     * Checks if the vertex is the right child of its left uncle.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex is the right child of its left uncle, false otherwise
     */
    private boolean isCrossedRight(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasLeft() && grandpa.left.hasRight())
            return grandpa.left.right == v;
        return false;
    }

    /**
     * Checks if the vertex is the left child of its right uncle.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex is the left child of its right uncle, false otherwise
     */
    private boolean isCrossedLeft(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasRight() && grandpa.right.hasLeft())
            return grandpa.right.left == v;
        return false;
    }

    /**
     * Checks if the vertex is the left child of its left uncle.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex is the left child of its left uncle, false otherwise
     */
    private boolean areLeft(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasLeft() && grandpa.left.hasLeft())
            return grandpa.left.left == v;
        return false;
    }

    /**
     * Checks if the vertex is the right child of its right uncle.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex is the right child of its right uncle, false otherwise
     */
    private boolean areRight(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasRight() && grandpa.right.hasRight())
            return grandpa.right.right == v;
        return false;
    }

    /**
     * Checks if the vertex has only a left child.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex has only a left child, false otherwise
     */
    private boolean onlyHasLeft(RedBlackVertex v) {
        return !v.hasRight() && v.hasLeft();
    }

    /**
     * Checks if the vertex has only a right child.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex has only a right child, false otherwise
     */
    private boolean onlyHasRight(RedBlackVertex v) {
        return !v.hasLeft() && v.hasRight();
    }

    /**
     * Checks if the vertex has both left and right children.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex has both left and right children, false otherwise
     */
    private boolean hasTwoChildren(RedBlackVertex v) {
        return v.hasLeft() && v.hasRight();
    }

    /**
     * Returns the color of the vertex. If the vertex is null, it returns BLACK.
     * 
     * @param v the vertex whose color is to be returned
     * @return the color of the vertex, or BLACK if the vertex is null
     */
    private Color color(RedBlackVertex v) {
        return v != null ? v.color : Color.BLACK;
    }

    /**
     * Casts a BinaryTreeVertex to a RedBlackVertex.
     * 
     * @param v the vertex to be cast
     * @return the casted RedBlackVertex
     */
    private RedBlackVertex redBlackVertex(BinaryTreeVertex<T> v) {
        return (RedBlackVertex)v;
    }

    /**
     * Checks if the vertex is the left child of its parent.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex is the left child of its parent, false otherwise
     */
    private boolean isLeft(RedBlackVertex v) {
        return getFather(v).left == v;
    }

    /**
     * Checks if the vertex is the right child of its parent.
     * 
     * @param v the vertex to be checked
     * @return true if the vertex is the right child of its parent, false otherwise
     */
    private boolean isRight(RedBlackVertex v) {
        return getFather(v).right == v;
    }

}
