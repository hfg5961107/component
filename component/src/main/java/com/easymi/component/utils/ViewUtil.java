package com.easymi.component.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: shine
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class ViewUtil {
    public static View findAllScrollViews(ViewGroup mViewGroup) {
        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View mView = mViewGroup.getChildAt(i);
            if (mView.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (isScrollableView(mView)) {
                return mView;
            }
            if (mView instanceof ViewGroup) {
                mView = findAllScrollViews((ViewGroup) mView);
                if (mView != null) {
                    return mView;
                }
            }
        }
        return null;
    }

    public static boolean isScrollableView(View mView) {
        return mView instanceof ScrollView
                || mView instanceof HorizontalScrollView
                || mView instanceof NestedScrollView
                || mView instanceof AbsListView
                || mView instanceof RecyclerView
                || mView instanceof ViewPager
                || mView instanceof WebView;
    }

    public static boolean contains(View mView, float x, float y) {
        Rect localRect = new Rect();
        mView.getGlobalVisibleRect(localRect);
        return localRect.contains((int) x, (int) y);
    }


    private static final int MIN_CLICK_DELAY_TIME = 300;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
