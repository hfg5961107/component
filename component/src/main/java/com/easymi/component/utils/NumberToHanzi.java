package com.easymi.component.utils;

public class NumberToHanzi {


    public static String number2hanzi(String s) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            String current = s.substring(i, i + 1);
            if (current.equals("0")) {
                current = "零";
            } else if (current.equals("1")) {
                current = "一";
            } else if (current.equals("2")) {
                current = "二";
            } else if (current.equals("3")) {
                current = "三";
            } else if (current.equals("4")) {
                current = "四";
            } else if (current.equals("5")) {
                current = "五";
            } else if (current.equals("6")) {
                current = "六";
            } else if (current.equals("7")) {
                current = "七";
            } else if (current.equals("8")) {
                current = "八";
            } else if (current.equals("9")) {
                current = "九";
            }
            sb.append(current);
        }

        return sb.toString();
    }
}
