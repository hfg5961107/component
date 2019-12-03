package com.easymi.component.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/2.
 * pinYinUtil.
 */

public class PinYinUtil {

    public static String getPinYin(String input) {
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(input);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (token.type == HanziToPinyin.Token.PINYIN) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    public static String getPinYinUpperCase(String input) {
        String str = getPinYin(input);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.toUpperCase();
    }


    public static String getFirstChar(String input) {
        String s = getPinYinUpperCase(input);
        String headChar = null;
        if (!TextUtils.isEmpty(s)) {
            char head = s.charAt(0);
            if (head < 'A' || head > 'Z') {
                head = '★';
            }
            headChar = head + "";
        }
        return headChar;
    }


    public static String getFirst(String pinYin) {
        String headChar = null;
        if (!TextUtils.isEmpty(pinYin)) {
            char head = pinYin.charAt(0);
            if (head < 'A' || head > 'Z') {
                head = '★';
            }
            headChar = head + "";
        }
        return headChar;
    }


}
