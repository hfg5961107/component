package com.easymi.component.widget;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.easymi.component.utils.Log;

/**
 * Created by xyin on 2017/1/20.
 * 输入金额限制监听器.
 */

public class MoneyWatcher implements TextWatcher {
    private static final String TAG = "MoneyWatcher";

    private EditText editText;
    private double moneyLimit = Integer.MAX_VALUE;
    private int Limit = 1; //限制输入到小数点后的位数

    public MoneyWatcher(EditText editText) {
        this.editText = editText;
    }

    /**
     * 设置可以输入的最大金额.
     *
     * @param moneyLimit 限制的金额
     */
    public MoneyWatcher setMaxMoney(double moneyLimit) {
        this.moneyLimit = moneyLimit;
        return this;
    }

    public MoneyWatcher setLimit(int limit) {
        this.Limit = limit;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            return;
        }

        if (s.toString().startsWith(".")) {
            s = "0.";   //如果以小数点开始前面自动补0
        } else if (s.toString().startsWith("0") && s.toString().trim().length() > 1
                && !s.toString().substring(1, 2).equals(".")) {
            s = s.subSequence(1, 2); //禁止小数点前输入多个0
        } else if (s.toString().contains(".") && s.toString().length() - 1 - s.toString().indexOf(".") > Limit) {
            s = s.toString().subSequence(0, s.toString().indexOf(".") + Limit + 1); //限制输入到小数点后limit位
        } else if (getNumber(s) > moneyLimit) {
            s = String.valueOf(moneyLimit); //限制最大输入
        } else {
            return;
        }

        editText.setText(s);
        editText.setSelection(s.length());

    }

    @Override
    public void afterTextChanged(Editable s) {
        //do nothing
    }

    /**
     * 获取当前输入的数字.
     *
     * @param s 输入的字符串
     * @return 返回解析后的支付串
     */
    private double getNumber(CharSequence s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }

        double number = 0;
        try {
            number = Double.parseDouble(s.toString());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            editText.setText(null);
            Log.e(TAG, "parse double error, string --> " + s);
        }
        return number;
    }

}
