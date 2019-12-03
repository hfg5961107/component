package com.easymi.component.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import com.easymi.component.activity.WebActivity;

public class NoUnderLineSpan extends URLSpan {
    private Context mContext;
    private String articleName;
    private int titleId;
    public NoUnderLineSpan(Context context, String articleName, int titleId) {
        super(articleName);
        mContext = context;
        this.articleName = articleName;
        this.titleId = titleId;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(Color.parseColor("#00B2EE"));
    }

    @Override
    public void onClick(View widget) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("articleName", articleName);
        intent.putExtra("titleRes", titleId);
        mContext.startActivity(intent);
    }
}
