package com.easymi.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.easymi.component.R;

/**
 * Created by DMing on 2016/7/18.
 * 自定义评价条.
 */
public class RatingBar extends View {

    private float MIN_MARK = 0;

    private int starDistance = 0; //星星间距
    private int starCount = 5;  //星星个数
    private int starSize;     //星星高度大小，星星一般正方形，宽度等于高度
    private float starMark = 0.0F;   //评分星星
    private Bitmap starFillBitmap; //亮星星
    private Drawable starEmptyDrawable; //暗星星
    private OnStarChangeListener onStarChangeListener;//监听星星变化接口
    private Paint paint;         //绘制星星画笔
    private boolean integerMark = false;
    private boolean isIndicator;
    private float stepSize = 0.1F;  //默认步长

    public RatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(true);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        this.starDistance = (int) mTypedArray.getDimension(R.styleable.RatingBar_starDistance, 0);
        this.starSize = (int) mTypedArray.getDimension(R.styleable.RatingBar_starSize, 20);
        this.starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        this.stepSize = mTypedArray.getFloat(R.styleable.RatingBar_stepSize, 0.1F);
        if (stepSize < 0.1) {
            stepSize = 0.1F;
        }
        this.starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        this.starFillBitmap = drawableToBitmap(mTypedArray.getDrawable(R.styleable.RatingBar_starFill));
        isIndicator = mTypedArray.getBoolean(R.styleable.RatingBar_isIndicator, false);
        mTypedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(starFillBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

    }


    /**
     * 设置是否需要整数评分.
     *
     * @param integerMark true表示为整数评分
     */
    public void setIntegerMark(boolean integerMark) {
        this.integerMark = integerMark;
    }

    public void setMinMark(float minMark) {
        this.MIN_MARK = minMark;
    }

    /**
     * 设置显示的星星的分数
     *
     * @param mark 评分
     */
    public void setStarMark(float mark) {
        if (integerMark) {
            starMark = (int) Math.ceil(mark);
        } else {
            int num = (int) (mark / stepSize);
            float d = mark % stepSize;
            if (d >= 0.1 && d / stepSize >= 0.5) {
                mark = num * stepSize + stepSize;
            } else {
                mark = num * stepSize;
            }
            starMark = Math.round(mark * 10) * 1.0f / 10;
        }

//        if (this.onStarChangeListener != null) {
//            this.onStarChangeListener.onStarChange(starMark);  //调用监听接口
//        }
        starMark = starMark < MIN_MARK ? MIN_MARK : starMark;
        invalidate();
    }

    /**
     * 获取显示星星评分.
     *
     * @return starMark
     */
    public float getStarMark() {
        return starMark;
    }

    /**
     * 定义星星点击的监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    /**
     * 设置监听.
     *
     * @param onStarChangeListener onStarChangeListener
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starSize * starCount + starDistance * (starCount - 1), starSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (starFillBitmap == null || starEmptyDrawable == null) {
            return;
        }

        //绘制空白星星
        for (int i = 0; i < starCount; i++) {
            starEmptyDrawable.setBounds((starDistance + starSize) * i, 0, (starDistance + starSize) * i + starSize, starSize);
            starEmptyDrawable.draw(canvas);
        }

        if (starMark > 1) {
            canvas.drawRect(0, 0, starSize, starSize, paint);
            if (starMark - (int) (starMark) == 0) {
                for (int i = 1; i < starMark; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
            } else {
                for (int i = 1; i < starMark - 1; i++) {
                    canvas.translate(starDistance + starSize, 0);
                    canvas.drawRect(0, 0, starSize, starSize, paint);
                }
                canvas.translate(starDistance + starSize, 0);
                canvas.drawRect(0, 0, starSize * (Math.round((starMark - (int) (starMark)) * 10) * 1.0f / 10), starSize, paint);
            }
        } else {
            canvas.drawRect(0, 0, starSize * starMark, starSize, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isIndicator) {
            return super.onTouchEvent(event);
        }
        int x = (int) event.getX();
        if (x < 0) {
            x = 0;
        }
        if (x > getMeasuredWidth()) {
            x = getMeasuredWidth();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / starCount));
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (this.onStarChangeListener != null) {
                    this.onStarChangeListener.onStarChange(starMark);  //调用监听接口
                }
                break;
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    /**
     * drawable转bitmap.
     *
     * @param drawable 需要操作的drawable
     * @return Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(starSize, starSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, starSize, starSize);
            drawable.draw(canvas);
        }
        return bitmap;
    }
}
