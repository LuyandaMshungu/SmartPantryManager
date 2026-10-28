package com.example.smartpantrymanager; // change to your own package name

import java.util.Locale;

public class IngredientUtils {

    public static String normalize(String name) {
        if (name == null) {
            return "";
        }

        String n = name.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");

        if (n.length() > 3) {
            if (n.endsWith("ies")) {
                n = n.substring(0, n.length() - 3) + "y";
            } else if (n.endsWith("oes")) {
                n = n.substring(0, n.length() - 2);
            } else if (n.endsWith("s") && !n.endsWith("ss")) {
                n = n.substring(0, n.length() - 1);
            }
        }
        return n;
    }
}
