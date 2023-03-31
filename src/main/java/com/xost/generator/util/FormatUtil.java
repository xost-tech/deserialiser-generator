package com.xost.generator.util;

public class FormatUtil {
    public String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String firstCharLower(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
