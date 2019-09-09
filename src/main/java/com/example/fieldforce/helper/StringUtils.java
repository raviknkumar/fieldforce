package com.example.fieldforce.helper;

public class StringUtils {

    public static String getCleanString(String s) {
        if(s == null || s.isEmpty())
            return "";
        return s.trim();
    }

    public static String getCleanString(Number n) {
        if(n == null)
            return "";
        return n.toString().trim();
    }
}
