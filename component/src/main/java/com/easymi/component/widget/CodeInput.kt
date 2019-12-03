package com.easymi.component.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import com.easymi.component.R

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: CodeInput
 * @Author: hufeng
 * @Date: 2019/4/9 下午3:21
 * @Description: 验证码输入框
 * @History:
 */
class CodeInput : View {

    //默认绘制线的高度
    private val DEFAULT_LINE_HEIGHT = 2f
    //cursor宽度的一半值
    private val DEFAULT_half_CURSOR_WIDTH = 2f
    //光标显示时的周期
    private val DURATION = 1000L

    //xml配置属性
    private val boxWidth: Int
    private val boxHeight: Int
    private val boxSpace: Int
    private val boxCount: Int
    private val textSize: Float
    private var boxBackground: Drawable? = null
    private val textColor: Int
    private val themeColor: Int
    private val showCursor: Boolean

    //根据颜色判断
    private val paint: Paint
    private var codeBuilder: StringBuilder
    private val textHeight: Float
    private val textWidth: Float
    private val baseLineH: Float
    private var onCodeListener: ((code: String) -> Unit)? = null

    //上一次cursor显示时间
    private var lastCursorTime: Long = 0
    // cursor锁,true表示退出闪烁
    private var loopLock: Boolean = false
    //当前cursor是否对用户可见
    private var mCursorVisible = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //能获取焦点才能弹出软键盘
        isFocusableInTouchMode = true
        isFocusable = true

        val a = context!!.obtainStyledAttributes(attrs, R.styleable.CodeInput, defStyleAttr, 0)
        boxWidth = a.getDimensionPixelOffset(R.styleable.CodeInput_ci_boxWidth, 40)
        boxHeight = a.getDimensionPixelOffset(R.styleable.CodeInput_ci_boxHeight, 40)
        boxCount = a.getInt(R.styleable.CodeInput_ci_boxCount, 4)
        boxSpace = a.getDimensionPixelOffset(R.styleable.CodeInput_ci_boxSpace, 2)
        textSize = a.getDimension(R.styleable.CodeInput_ci_textSize, 28f)
        textColor = a.getColor(R.styleable.CodeInput_ci_textColor, Color.BLACK)
        themeColor = a.getColor(R.styleable.CodeInput_ci_themeColor, Color.BLACK)
        boxBackground = a.getDrawable(R.styleable.CodeInput_ci_background)
        showCursor = a.getBoolean(R.styleable.CodeInput_ci_showCursor, true)
        a.recycle()

        paint = Paint().apply {
            textSize = this@CodeInput.textSize
            textAlign = Paint.Align.CENTER
        }
        val fontMetrics = paint.fontMetrics
        textHeight = fontMetrics.bottom - fontMetrics.top
        textWidth = paint.measureText("0A") * 0.5f
        baseLineH = Math.abs(fontMetrics.top)

        codeBuilder = StringBuilder(boxCount)
        showSoftInput()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((boxCount * boxWidth + boxCount * boxSpace - boxSpace), boxHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        drawBox(canvas!!)
        drawCode(canvas)
        drawCursor(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loopLock = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loopLock = true
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        loopLock = visibility != View.VISIBLE
    }

    private fun drawBox(canvas: Canvas) {
        if (boxBackground != null) {
            for (i in 0 until boxCount) {
                val x = i * (boxWidth + boxSpace)
                boxBackground!!.setBounds(x, 0, x + boxWidth, boxHeight)
                boxBackground!!.draw(canvas)
            }
        } else {
            with(paint) {
                reset()
                isAntiAlias = true
                color = themeColor
            }
            for (i in 0 until boxCount) {
                val x = (i * (boxWidth + boxSpace)).toFloat()
                canvas.drawRect(x, boxHeight - DEFAULT_LINE_HEIGHT, x + boxWidth, boxHeight.toFloat(), paint)
            }
        }
    }

    private fun drawCode(canvas: Canvas) {
        if (codeBuilder.isEmpty()) {
            return
        }
        with(paint) {
            reset()
            isAntiAlias = true
            style = Paint.Style.FILL
            color = textColor
            textSize = this@CodeInput.textSize
        }
        val xOffset = (boxWidth - textWidth) * 0.5f
        val textY = (boxHeight - textHeight) * 0.5f + baseLineH
        val chars = codeBuilder.toString().toCharArray()
        for (i in 0 until codeBuilder.length) {
            val x = i * (boxWidth + boxSpace)
            canvas.drawText(chars, i, 1, x + xOffset, textY, paint)
        }
    }

    private fun drawCursor(canvas: Canvas) {
        if (!showCursor || codeBuilder.length >= boxCount || loopLock) {
            //没有光标||输入完毕||锁住不允许闪烁
            return
        }
        val currentTime = SystemClock.uptimeMillis()
        if (currentTime - lastCursorTime < DURATION) {
            //重绘制时间还没有到
            return
        }
        if (mCursorVisible) {
            with(paint) {
                reset()
                isAntiAlias = true
                color = themeColor
            }
            val x = (codeBuilder.length * (boxWidth + boxSpace)) + boxWidth * 0.5f
            val yOffset = boxHeight * 0.2f
            canvas.drawRect(x - DEFAULT_half_CURSOR_WIDTH, yOffset, x + DEFAULT_half_CURSOR_WIDTH, boxHeight - yOffset, paint)
        }

        //发送下次绘制的消息
        mCursorVisible = !mCursorVisible
        lastCursorTime = currentTime
        postInvalidateDelayed(DURATION)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER
        return super.onCreateInputConnection(outAttrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        showSoftInput()
        return true
    }

    private fun showSoftInput() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!hasFocus()) {
            requestFocus()
        }
        imm.showSoftInput(this, 0)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        //接收按键事件，67是删除键(backspace),7-16就是0-9
        if (keyCode == 67 && codeBuilder.isNotEmpty()) {
            codeBuilder.deleteCharAt(codeBuilder.length - 1)
        } else if (keyCode in 7..16 && codeBuilder.length < boxCount) {
            codeBuilder.append(keyCode - 7)
        }
        if (codeBuilder.length >= boxCount) {
            onCodeListener?.invoke(codeBuilder.toString())
        }
        invalidate()
        return super.onKeyDown(keyCode, event)
    }

    fun getCode(): String = codeBuilder.toString()

    fun setCode(code: String) {
        if (TextUtils.isEmpty(code)) {
            codeBuilder.setLength(0)
        } else {
            val str: String
            if (code.length >= boxCount) {
                str = code.substring(0, boxCount)
                onCodeListener?.invoke(str)
            } else {
                str = code
            }
            if (codeBuilder.isNotEmpty()) {
                codeBuilder.delete(0, codeBuilder.length)
            }
            codeBuilder.append(str)
        }
        invalidate()
    }

    fun setOnCodeListener(listener: (code: String) -> Unit) {
        this.onCodeListener = listener
    }


}