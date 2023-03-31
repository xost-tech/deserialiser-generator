package com.xost.generator.util;

import java.util.ResourceBundle;

import static com.xost.generator.constant.Status.INVALID_PROPERTIES;

public class AppUtil {

    public String getProperties(String key, String file) {
        file = file.equals("app") ? "application" : file;
        String value;
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(file);
            value = resourceBundle.getString(key);
        }
        catch (Exception e) {
            value = String.valueOf(INVALID_PROPERTIES);
        }
        return value;
    }
}
