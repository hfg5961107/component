package com.easymi.component.utils;

import android.content.Context;

/**
 * Created by Shine on 2014/7/7.
 */
public class StringUtils {

    public static final String EMPTY_STRING = "";

    public static String trimToEmpty(String str) {

        if (null == str) {
            return EMPTY_STRING;
        }

        return str.trim();
    }

    public static boolean isBlank(String str) {

        if (null == str) {
            return true;
        }

        if (str.equals("null")) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            Character character = str.charAt(i);
            if (!Character.isWhitespace(character)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNotBlank(String str) {

        return !isBlank(str);
    }

    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

}
