package com.easymi.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.easymi.component.R;

/**
 * Created by xyin on 2016/3/pay_normal.
 * 圆形的imageView.
 */
public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    //--------------------------相关属性------------------------
    private int mBorderColor = DEFAULT_BORDER_COLOR;    //描边的颜色
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;    //描边的宽度

    private boolean mReady; //是否准备OK
    private boolean mSetupPending;

    private Bitmap mBitmap;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;

    private float mDrawableRadius;
    private float mBorderRadius;

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    int padding = 0;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //读取设置的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_imv_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleImageView_imv_border_color, DEFAULT_BORDER_COLOR);

        a.recycle();

        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();    //做一些设置
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mBitmap == null) {
            return;
        }

        //画一个圆图, mBitmapPaint 设置了BitmapShader,会按图片着色
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mDrawableRadius, mBitmapPaint);

        if (mBorderWidth != 0) {
            //绘制周围边
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, mBorderRadius, mBorderPaint);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setup();
    }


    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    /**
     * 初始化准备
     */
    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    /**
     * 获得bitmap对象.
     *
     * @param drawable drawable
     * @return Bitmap
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 测量的到图形的半径.
     */
    private void setup() {

        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (getWidth() == 0 && getHeight() == 0) {
            return;
        }

        if (mBitmap == null) {
            invalidate();
            return;
        }

        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingLeft = getPaddingLeft();

        padding = Math.max(Math.max(paddingBottom, paddingTop), Math.max(paddingRight, paddingLeft));

        //设置着色
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);    //给图片上色
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);  //用这里mBitmapPaint将会绘制出图片
        //设置描边
        mBorderPaint.setStyle(Paint.Style.STROKE);  //设置为描外模式
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        //测量半径,用一个左上坐标为(padding,padding),右下坐标为(getWidth()-padding,getWidth()-padding)的矩形测量
        mBorderRect.set(padding, padding, getWidth() - padding, getHeight() - padding);
        //表示从圆心到mBorderRadius这段半径范围内为空心,在mBorderRadius处开始绘制mBorderWidth宽度的圆环
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f);

        //获得图的半径, 控件尺寸/2
        mDrawableRect.set(mBorderRect);
        mDrawableRadius = Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f);

        updateShaderMatrix();
        invalidate();

    }

    /**
     * 开始作画.
     */
    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;

        mShaderMatrix.set(null);    //设置矩阵

        /**
         *   图片实际尺寸和显示控件尺寸比较.
         *   判断:mBitmapWidth/mBitmapHeight 比例与 mDrawableRect.width()/mDrawableRect.height() 比例大小
         *   mBitmap 是 实际图片尺寸 , mDrawableRect是显示控件尺寸
         */

        //图片实际尺寸宽高比大一些
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight; //缩放比
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        mShaderMatrix.setScale(scale, scale);   //设置x,和y的缩放比例
        //设置平移
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }


}
