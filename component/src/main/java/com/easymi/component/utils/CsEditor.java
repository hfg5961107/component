package com.easymi.component.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.easymi.component.Config;
import com.easymi.component.app.XApp;

/**
 * Created by liuzihao on 2019/3/1.
 */

public class CsEditor {

    private SharedPreferences.Editor editor;

    private CsEditor() {
    }

    private static class SingletonHolder {
        private static final CsEditor INSTANCE = new CsEditor();
    }

    public static CsEditor getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = XApp.getInstance().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE).edit();
        }
        return editor;
    }

    public CsEditor putString(String key, String value) {
        if (TextUtils.isEmpty(value)) {
            getEditor().remove(key);
        } else {
            getEditor().putString(key, EncApi.getInstance().en(new Loader().getRsaPs().substring(0, 16), value));
        }
        return this;
    }

    public CsEditor putLong(String key, long value) {
        getEditor().putString(key, EncApi.getInstance().en(new Loader().getRsaPs().substring(0, 16), String.valueOf(value)));
        return this;
    }

    public CsEditor putFloat(String key, float value) {
        getEditor().putString(key, EncApi.getInstance().en(new Loader().getRsaPs().substring(0, 16), String.valueOf(value)));
        return this;
    }

    public CsEditor putInt(String key, int value) {
        getEditor().putString(key, EncApi.getInstance().en(new Loader().getRsaPs().substring(0, 16), String.valueOf(value)));
        return this;
    }

    public CsEditor putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value);
        return this;
    }

    public void apply() {
        getEditor().apply();
    }

    public CsEditor remove(String key) {
        getEditor().remove(key);
        return this;
    }

    public CsEditor clear() {
        getEditor().clear();
        return this;
    }
}
