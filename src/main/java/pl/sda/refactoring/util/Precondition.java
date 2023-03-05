package pl.sda.refactoring.util;

public final class Precondition {

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
