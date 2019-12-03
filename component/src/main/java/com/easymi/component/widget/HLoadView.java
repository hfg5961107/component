package com.easymi.component.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.easymi.component.R;

/**
 * Created by xyin on 2017/11/13.
 * 横向的loadView.
 */

public class HLoadView extends View {

    private final Paint mPaint;
    private float percent;  //百分比进度,显示宽度w = mWidth*percent
    private ValueAnimator mHAnimator;


    public HLoadView(Context context) {
        this(context, null);
    }

    public HLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HLoadView, defStyleAttr, 0);
        int duration = a.getInt(R.styleable.HLoadView_hlv_duration, 4000);
        int color = a.getColor(R.styleable.HLoadView_hlv_color, Color.parseColor("#232323"));
        a.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);

        startAnimator(duration, 0, 1f);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float xs = (width - width * percent) * 0.5f;
        canvas.drawRect(xs, 0, width - xs, height, mPaint);
    }

    private void startAnimator(int duration, float... value) {
        if (mHAnimator == null) {
            mHAnimator = ValueAnimator.ofFloat(value);
        }
        mHAnimator.cancel();
        mHAnimator.setDuration(duration);
        mHAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mHAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mHAnimator.setInterpolator(new AccelerateInterpolator());
        mHAnimator.addUpdateListener(value1 -> {
            percent = (float) value1.getAnimatedValue();
            invalidate();
        });
        mHAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHAnimator != null) {
            mHAnimator.cancel();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (mHAnimator == null) {
            return;
        }
        if (visibility != VISIBLE) {
            mHAnimator.cancel();
        } else if (!mHAnimator.isRunning()) {
            mHAnimator.start();
        }
    }

}
