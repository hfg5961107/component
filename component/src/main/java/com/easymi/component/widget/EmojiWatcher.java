package com.easymi.component.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.easymi.component.utils.CommonUtil;

/**
 * Created by xyin on 2017/1/4.
 * EditText监听并限制emoji表情输入的监听器.
 */

public class EmojiWatcher implements TextWatcher {

    private EditText editText;

    public EmojiWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //禁止输入emoji表情
        if (editText != null && CommonUtil.isEmoji(s.toString())) {
            editText.setText(s.toString().substring(0, start));
            editText.setSelection(start);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
