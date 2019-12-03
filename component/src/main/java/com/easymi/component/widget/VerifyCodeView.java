package com.easymi.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.easymi.component.R;
import com.easymi.component.utils.PhoneUtil;
import com.easymi.component.utils.StringUtils;


/**
 * Created by Administrator on 2017/10/9 0009.
 * 验证码输入框.
 */

public class VerifyCodeView extends View {

    private static final float CURSOR_WIDTH = 4;  //光标的宽度

    private int boxNum;    //box的个数
    private int borderColor;    //描边的颜色
    private float borderWidth;    //描边的宽度
    private float boxSize;  //每个格子的大小
    private float boxSpace;  //格子间的间距
    private int textColor;
    private float textSize;
    private float baseLineH;
    private float textWidth;
    private float textHeight;
    private int boxBackground;
    private float boxRadius;
    private int borderSelectedColor;    //选中的box边框颜色

    private String inputType;

    private boolean showCursor;
    private boolean mCursorVisible;

    private long lockTime;
    private boolean loopLock;   //true表示上锁

    private RectF mRectF;   //圆角矩形框
    private Paint mPaint;
    private StringBuilder codeBuilder;

    private OnCodeListener listener;

    public VerifyCodeView(Context context) {
        this(context, null);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusableInTouchMode(true);  //能获取焦点才能弹出软键盘

        //获取属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeView, defStyleAttr, 0);
        textSize = a.getDimension(R.styleable.VerifyCodeView_textSize, 14);
        boxSpace = a.getDimension(R.styleable.VerifyCodeView_boxSpace, 2);
        float boxPadding = a.getDimension(R.styleable.VerifyCodeView_boxPadding, 0);
        boxNum = a.getInteger(R.styleable.VerifyCodeView_boxNum, 1);
        borderColor = a.getColor(R.styleable.VerifyCodeView_borderColor, 0xFF000000);
        borderWidth = a.getDimension(R.styleable.VerifyCodeView_borderWidth, 2);
        textColor = a.getColor(R.styleable.VerifyCodeView_textColor, 0xFF000000);
        boxBackground = a.getColor(R.styleable.VerifyCodeView_boxBackground, 0xFFFFFFFF);
        boxRadius = a.getDimension(R.styleable.VerifyCodeView_borderRadius, 0);
        borderSelectedColor = a.getColor(R.styleable.VerifyCodeView_borderSelectedColor, 0xFF000000);
        showCursor = a.getBoolean(R.styleable.VerifyCodeView_showCursor, false);
        inputType = a.getString(R.styleable.VerifyCodeView_inputType);
        a.recycle();

        if (StringUtils.isBlank(inputType)) {
            inputType = "text";//默认输入text
        }

        //计算每个box的大小
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = fontMetrics.bottom - fontMetrics.top;
        textWidth = paint.measureText("0");
        baseLineH = Math.abs(fontMetrics.top);
        boxSize = Math.max(textHeight, textWidth) + boxPadding + borderWidth;

        codeBuilder = new StringBuilder(boxNum);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制矩形框
        mPaint.setColor(borderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);

        int index = codeBuilder.length();   //选中的box

        float right;
        float offset = borderWidth / 2; //边框矩形绘制以边框中心为基准
        //这里光标高度固定为boxSize/2,topBottomOffset = (boxSize - boxSize / 2) / 2;
        float topBottomOffset = boxSize / 4;
        float rightLeftOffset = (boxSize - CURSOR_WIDTH) / 2;

        for (int i = 0; i < boxNum; i++) {
            right = i * (boxSize + boxSpace);

            //底色
            mPaint.setColor(boxBackground);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(right + borderWidth, borderWidth, right + boxSize - borderWidth, boxSize - borderWidth, mPaint);

            //圆角边框
            int color = i == index ? borderSelectedColor : borderColor;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(borderWidth);
            mPaint.setColor(color);
            mRectF.set(right + offset, offset, right + boxSize - offset, boxSize - offset);
            canvas.drawRoundRect(mRectF, boxRadius, boxRadius, mPaint);

            //没有光标或者不是绘制该box，直接结束，否则绘制光标
            if (!showCursor || i != index) continue;

            if (!mCursorVisible) {
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(right + rightLeftOffset, topBottomOffset, right + boxSize - rightLeftOffset, boxSize - topBottomOffset, mPaint);
            }
            mCursorVisible = !mCursorVisible;
        }

        //填充验证码
        if (codeBuilder.length() > 0) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            float y = (boxSize - textHeight) / 2 + baseLineH;
            offset = (boxSize - textWidth) / 2;
            char[] codeStr = codeBuilder.toString().toCharArray();
            for (int i = 0; i < codeStr.length; i++) {
                right = i * (boxSize + boxSpace);
                canvas.drawText(codeStr, i, 1, right + offset, y, mPaint);
            }
        }

        if (codeBuilder.length() >= boxNum) {
            return;  //已经填充完毕不需要闪烁光标
        }

        long t = SystemClock.uptimeMillis();
        if (!loopLock && showCursor && t - lockTime >= 800) {
            lockTime = t;
            postInvalidateDelayed(800);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据box的个数来确定大小
        setMeasuredDimension((int) (boxSize * boxNum + boxNum * boxSpace - boxSpace), (int) boxSize);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        loopLock = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        loopLock = true;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        loopLock = visibility != View.VISIBLE;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //接收按键事件，67是删除键(backspace),7-16就是0-9
        if (keyCode == 67 && codeBuilder.length() > 0) {
            codeBuilder.deleteCharAt(codeBuilder.length() - 1);
            invalidate();
        } else if (keyCode >= 7 && keyCode <= 16 && codeBuilder.length() < boxNum) {
            codeBuilder.append(keyCode - 7);
            invalidate();
        } else if (keyCode >= 29 && keyCode <= 54 && inputType.equals("text") && codeBuilder.length() < boxNum) {//inputType为text
            codeBuilder.append(PhoneUtil.code2Str(keyCode));
            invalidate();
        }
        if (codeBuilder.length() >= boxNum) {
            //达到位数自动隐藏键盘
            InputMethodManager imm = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);

            if (listener != null) {
                listener.onCodeComplete(codeBuilder.toString());
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //在View上点击时弹出软键盘
        requestFocus();
        PhoneUtil.showKeyboard(this);
        return super.onTouchEvent(event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (inputType.equals("text")) {
            outAttrs.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD;
        } else if (inputType.equals("phone")) {
            outAttrs.inputType = InputType.TYPE_CLASS_PHONE;
        } else if (inputType.equals("number")) {
            outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        }
        return super.onCreateInputConnection(outAttrs);
    }

    public String getCode() {
        return codeBuilder.toString();
    }

    public void setCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return;
        }
        String str;
        if (code.length() >= boxNum) {
            InputMethodManager imm = (InputMethodManager) getContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindowToken(), 0);

            str = code.substring(0, boxNum);
            if (listener != null) {
                listener.onCodeComplete(str);
            }
        } else {
            str = code;
        }
        if (codeBuilder.length() > 0) {
            codeBuilder.delete(0, codeBuilder.length());
        }
        codeBuilder.append(str);
        invalidate();
    }

    /**
     * set a OnCodeListener.
     *
     * @param listener onCodeCompleteListener
     */
    public void setOnCodeListener(OnCodeListener listener) {
        this.listener = listener;
    }
}
