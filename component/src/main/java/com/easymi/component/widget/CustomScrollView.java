package com.easymi.component.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.easymi.component.utils.Log;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: CustomScrollView
 * @Author: hufeng
 * @Date: 2019/3/13 下午2:12
 * @Description:
 * @History:
 */
public class CustomScrollView extends ScrollView {

    //回调监听接口
    private OnScrollChangeListener mOnScrollChangeListener;
    //标识是否滑动到顶部
    private boolean isScrollToStart = false;
    //标识是否滑动到底部
    private boolean isScrollToEnd = false;
    private static final int CODE_TO_START = 0x001;
    private static final int CODE_TO_END = 0x002;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_TO_START:
                    //重置标志“滑动到顶部”时的标志位
                    isScrollToStart = false;
                    break;
                case CODE_TO_END:
                    //重置标志“滑动到底部”时的标志位
                    isScrollToEnd = false;
                    break;
                default:
                    break;
            }
        }
    };

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            Log.i("CustomScrollView", "scrollY:" + getScrollY());
            //滚动到顶部，ScrollView存在回弹效果效应（这里只会调用两次，如果用<=0,会多次触发）
            if (getScrollY() == 0) {
                //过滤操作，优化为一次调用
                if (!isScrollToStart) {
                    isScrollToStart = true;
                    mHandler.sendEmptyMessageDelayed(CODE_TO_START, 200);
                    Log.e("CustomScrollView", "toStart");
                    mOnScrollChangeListener.onScrollToStart();
                }
            } else {
                View contentView = getChildAt(0);
                if (contentView != null && contentView.getMeasuredHeight() == (getScrollY() + getHeight())) {
                    //滚动到底部，ScrollView存在回弹效果效应
                    //优化，只过滤第一次
                    if (!isScrollToEnd ) {
                        isScrollToEnd = true;
                        mHandler.sendEmptyMessageDelayed(CODE_TO_END, 200);
                        Log.e("CustomScrollView", "toEnd,scrollY:" + getScrollY());
                        mOnScrollChangeListener.onScrollToEnd();
                    }

                }
            }
        }
    }

    //滑动监听接口
    public interface OnScrollChangeListener {

        //滑动到顶部时的回调
        void onScrollToStart();

        //滑动到底部时的回调
        void onScrollToEnd();
    }

    public void setScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }

}