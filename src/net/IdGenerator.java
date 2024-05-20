package net;

public class IdGenerator {
    private static int bookIdCounter = 1;

    public static int generateUniqueId() {
        return bookIdCounter++;
    }
}
