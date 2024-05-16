package net;
public enum BookField {
    NAME, 
    AUTHOR,
    CATEGORY,
    EDITORIAL;

    @Override public String toString() {
        switch (this) {
            case NAME:
                return "Name";
            case AUTHOR:
                return "Author";
            case CATEGORY:
                return "Category";
            case EDITORIAL:
                return "Editorial";
            default :
                throw new IllegalArgumentException();
        }
    }
}
