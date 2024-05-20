package data_structures;

import java.util.Comparator;

public class RedBlackTree<T extends Comparable<T>> extends OrderedBinaryTree<T> {
    
    protected class RedBlackVertex extends Vertex {

        public Color color;

        public RedBlackVertex(T element) {
            super(element);
            color = Color.NONE;
        }

        @Override public String toString() {
            if (this.color == Color.RED)
                return String.format("R{%s}", this.element);
            else return String.format("B{%s}", this.element);
        }

        @Override public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass())
                return false;
            @SuppressWarnings("unchecked")
                RedBlackVertex vertex = (RedBlackVertex)object;

            return this.color == vertex.color && super.equals(object);
        }
    }

    public RedBlackTree() {
        super();
        comparator = null;
    }

    public RedBlackTree(Collection<T> collection) {
        super(collection);
        comparator = null;
    }

    public RedBlackTree(Comparator<T> comparator) {
        super();
        this.comparator = comparator;
    }

    public RedBlackTree(Comparator<T> comparator, Collection<T> collection) {
        super(collection);
        this.comparator = comparator;
    }

    @Override protected Vertex newVertex(T element) {
        return new RedBlackVertex(element);
    }

    public Color getColor(BinaryTreeVertex<T> vertex) {
        if (vertex instanceof RedBlackVertex) {
            RedBlackVertex v = (RedBlackVertex)vertex;
            return v.color;
        } else 
            throw new ClassCastException("Cannot get the vertex's color. " + 
                "It's not instance of RedBlackVertex.\n");
    }

    @Override public void add(T element) {
        super.add(element);
        RedBlackVertex v = redBlackVertex(lastAdded);
        v.color = Color.RED;
        rebalanceToAdd(v);
    }

    private void rebalanceToAdd(RedBlackVertex v) {
        RedBlackVertex father = (RedBlackVertex)v.father();
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
                if (isCrossedLeft(v)) {
                    super.turnLeft(father);
                    RedBlackVertex aux = v;
                    v = father;
                    father = aux;
                } else if (isCrossedRight(v)) {
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

    @Override public void remove(T element) {
        RedBlackVertex v = redBlackVertex(search(element));
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

    @Override public void turnLeft(BinaryTreeVertex<T> vertex) {
        throw new UnsupportedOperationException("Red-Black Trees cannot be " + 
            "turned to the left by the user.\n");
    }

    @Override public void turnRight(BinaryTreeVertex<T> vertex) {
        throw new UnsupportedOperationException("Red-Black Trees cannot be " + 
            "turned to the right by the user.\n");
    }

    private RedBlackVertex getFather(RedBlackVertex v) {
        return v != null ? redBlackVertex(v.father) : null;
    }

    private RedBlackVertex getGrandpa(RedBlackVertex v) {
        return getFather(getFather(v));
    }

    private RedBlackVertex getUncle(RedBlackVertex v) {
        return getBrother(getFather(v));
    }

    private RedBlackVertex getBrother(RedBlackVertex v) {
        RedBlackVertex father = getFather(v);
        if (v != null && father != null) {
            if (isLeft(v))
                return father.hasRight() ? redBlackVertex(father.right) : null;
            else 
                return father.hasLeft() ? redBlackVertex(father.left) : null;
        } else return null;
    }

    private boolean isCrossedRight(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasLeft() && grandpa.left.hasRight())
            return grandpa.left.right == v;
        return false;
    }

    private boolean isCrossedLeft(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasRight() && grandpa.right.hasLeft())
            return grandpa.right.left == v;
        return false;
    }

    private boolean areLeft(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasLeft() && grandpa.left.hasLeft())
            return grandpa.left.left == v;
        return false;
    }

    private boolean areRight(RedBlackVertex v) {
        RedBlackVertex grandpa = getGrandpa(v);
        if (grandpa != null && grandpa.hasRight() && grandpa.right.hasRight())
            return grandpa.right.right == v;
        return false;
    }

    private boolean onlyHasLeft(RedBlackVertex v) {
        return !v.hasRight() && v.hasLeft();
    }

    private boolean onlyHasRight(RedBlackVertex v) {
        return !v.hasLeft() && v.hasRight();
    }

    private boolean hasTwoChildren(RedBlackVertex v) {
        return v.hasLeft() && v.hasRight();
    }

    private Color color(RedBlackVertex v) {
        return v!= null ? v.color : Color.BLACK;
    }

    private RedBlackVertex redBlackVertex(BinaryTreeVertex<T> v) {
        return (RedBlackVertex)v;
    }

    private boolean isLeft(RedBlackVertex v) {
        return getFather(v).left == v;
    }

    private boolean isRight(RedBlackVertex v) {
        return getFather(v).right == v;
    }
}
