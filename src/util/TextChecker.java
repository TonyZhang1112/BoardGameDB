package ca.ubc.cs304.util;

public class TextChecker {
    public TextChecker() {}

    public static boolean hasSemicolon(String string) {
        return string.contains(";");
    }

    public static boolean hasSemicolonAny(String[] strings) {
        for (String s: strings) {
            if (hasSemicolon(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmpty(String string) {
        return string.length() == 0;
    }

    public static boolean isEmptyAny(String[] strings) {
        for (String s: strings) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }
}
