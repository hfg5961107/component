package com.easymi.component.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.easymi.component.R;
import com.easymi.component.utils.DensityUtil;

/**
 * Created by xyin on 2017/12/3.
 * loading button.
 */

public class LoadingButton extends AppCompatButton {

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_NORMAL = 2;

    private final RectF rectF;
    private int angle;
    private ValueAnimator mHAnimator;
    private CharSequence mText;
    private boolean isLoading;  //表示是否在loading状态

    //属性
    private float radius;
    private int duration = 800;

    private Paint arcPaint;
    private Paint circlePaint;

    private float centerX, centerY;

    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, defStyleAttr, 0);
        duration = a.getInt(R.styleable.LoadingButton_lb_duration, 1000);
        int color = a.getColor(R.styleable.LoadingButton_lb_strokeColor, Color.rgb(255, 255, 255));
        radius = a.getDimension(R.styleable.LoadingButton_lb_radius, DensityUtil.dp2px(getContext(), 10));
        float strokeWidth = a.getDimension(R.styleable.LoadingButton_lb_strokeWidth, 10);
        a.recycle();

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(color);
        arcPaint.setStrokeWidth(strokeWidth);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(color);
        circlePaint.setAlpha(180);
        circlePaint.setStrokeWidth(strokeWidth * 0.9f);

        rectF = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLoading) {
            calculateSize();
            canvas.drawCircle(centerX, centerY, radius, circlePaint);
            canvas.rotate(angle, centerX, centerY);
            canvas.drawArc(rectF, 0, 160, false, arcPaint);
        }
    }

    private void calculateSize() {
        if (rectF.height() != 0) {
            return;
        }
        int width = getWidth();
        int height = getHeight();
        centerX = width * 0.5f;
        centerY = height * 0.5f;

        if (radius > 0) {
            rectF.set(centerX - radius, centerY - radius,
                    centerX + radius, centerY + radius);
            return;
        }

        float l;
        if (width < height) {
            l = width - getPaddingLeft() - getPaddingRight();
        } else {
            l = height - getPaddingTop() - getPaddingBottom();
        }
        radius = l * 0.5f;
        rectF.set(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);

    }

    public void setStatus(int status) {
        if (status == STATUS_LOADING) {
            startAnimator();
        } else if (status == STATUS_NORMAL) {
            stop();
        }
    }

    private void startAnimator() {
        if (mHAnimator == null) {
            mHAnimator = ValueAnimator.ofInt(0, 360);
        } else if (mHAnimator.isRunning()) {
            return;
        }

        mHAnimator.cancel();
        mHAnimator.setDuration(duration);
        mHAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mHAnimator.setInterpolator(new LinearInterpolator());
        mHAnimator.addUpdateListener(value1 -> {
            angle = (int) value1.getAnimatedValue();
            invalidate();
        });
        mHAnimator.start();
        mText = getText();
        setText("");
        isLoading = true;
    }

    private void stop() {
        if (mHAnimator != null) {
            mHAnimator.cancel();
        }
        isLoading = false;
        setText(mText);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHAnimator != null) {
            mHAnimator.cancel();
        }
    }


}
