package com.easymi.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.easymi.component.R;
import com.easymi.component.utils.Log;
import com.easymi.component.widget.slidinguppanel.ViewDragHelper;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2017/9/21 0021.
 * 自定义bottomBehavior.
 */

public class BottomBehavior extends CoordinatorLayout.Behavior<View> {

    /**
     * Callback for monitoring events about bottom sheets.
     */
    public interface BottomCallback {

        /**
         * Called when the bottom sheet changes its state.
         */
        void onStateChanged(@NonNull View bottomView, int newState);
    }

    /**
     * The bottom is expanded.
     */
    public static final int STATE_EXPANDED = 1;

    /**
     * The bottom is collapsed.
     */
    public static final int STATE_COLLAPSED = 2;


    private static final float DIV = 0.6F;   //触发滑动限定值
    private static final float VELOCITY_VALUE = 200;   //触发滑动事件加速度阈值

    private int mPeekHeight;
    private int mMaxOffset;
    private int mMarginTop;

    private int mState = STATE_COLLAPSED;   //初始状态

    private WeakReference<View> mViewRef;
    private ViewDragHelper mViewDragHelper;
    private BottomCallback mCallback;

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XBottomBehavior);
        mPeekHeight = a.getDimensionPixelOffset(R.styleable.XBottomBehavior_x_peekHeight, 0);
        mMarginTop = a.getDimensionPixelOffset(R.styleable.XBottomBehavior_x_margin_top, 0);
        mPeekHeight = Math.max(0, mPeekHeight);
        mMarginTop = Math.max(0, mMarginTop);
        a.recycle();

    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            ViewCompat.setFitsSystemWindows(child, true);
        }
        // First let the parent lay it out
        parent.onLayoutChild(child, layoutDirection);
        // Offset the bottom sheet
        int parentHeight = parent.getHeight();

        int minOffset = Math.max(0, parentHeight - child.getHeight());
        mMaxOffset = Math.max(parentHeight - mPeekHeight, minOffset);

        ViewCompat.offsetTopAndBottom(child, mMaxOffset);

        if (mViewDragHelper == null) {
            mViewDragHelper = ViewDragHelper.create(parent, 0.8F, mDragCallback);
        }

        mViewRef = new WeakReference<>(child);

        return true;
    }

    protected int getMaxOffset() {
        return mMaxOffset;
    }

    protected int getMarginTop() {
        return mMarginTop;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        mViewDragHelper.processTouchEvent(ev);
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 滑动回调.
     */
    private final ViewDragHelper.Callback mDragCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //返回值为true时,child可以被拖拽
            String tag = (String) child.getTag();
            return TextUtils.equals(tag, "drag_view");
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top >= mMaxOffset) {
                setStateInternal(STATE_COLLAPSED);
                return mMaxOffset;
            } else if (top <= mMarginTop) {
                setStateInternal(STATE_EXPANDED);
                return mMarginTop;
            }
            return top;
        }


        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;   //当child clickable 为true时,返回值为0的情况下,child不能别移动.
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int range = mMaxOffset - mMarginTop;
            if (range <= 0) {
                Log.e("BottomBehavior", "range less than 0");
                return;
            }

            float limitY = range * DIV + mMarginTop;
            float y = releasedChild.getY();

            int targetY;

            if (yvel < (-VELOCITY_VALUE)) {
                targetY = mMarginTop;
                setStateInternal(STATE_EXPANDED);
            } else if (yvel > VELOCITY_VALUE) {
                targetY = mMaxOffset;
                setStateInternal(STATE_COLLAPSED);
            } else if (y < limitY) {
                targetY = mMarginTop;
                setStateInternal(STATE_EXPANDED);
            } else {
                targetY = mMaxOffset;
                setStateInternal(STATE_COLLAPSED);
            }

            if (mViewDragHelper.settleCapturedViewAt(releasedChild.getLeft(), targetY)) {
                ViewCompat.postOnAnimation(releasedChild, new SettleRunnable(releasedChild));
            }
        }

    };


    public void reset() {
        mState = STATE_COLLAPSED;
    }

    /**
     * 滑动处理.
     */
    private class SettleRunnable implements Runnable {

        private final View mView;

        SettleRunnable(View view) {
            mView = view;
        }

        @Override
        public void run() {
            if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(mView, this);
            }
        }
    }

    public void setState(int state) {
        if (state == mState) {
            return;
        }
        if (mViewRef == null) {
            return;
        }
        final View child = mViewRef.get();
        if (child == null) {
            return;
        }
        setStateInternal(state);
        startSettlingAnimation(child, state);
    }

    public int getState() {
        return mState;
    }

    /**
     * 设置内部状态.
     *
     * @param state 需要设置的状态
     */
    private void setStateInternal(int state) {
        if (mState == state) {
            return;
        }
        mState = state;
        View view = mViewRef.get();
        if (view != null && mCallback != null) {
            mCallback.onStateChanged(view, state);
        }
    }

    public void toggleState() {
        int state = mState == STATE_COLLAPSED ? STATE_EXPANDED : STATE_COLLAPSED;
        setState(state);
    }

    private void startSettlingAnimation(View child, int state) {
        int top;
        if (state == STATE_COLLAPSED) {
            top = mMaxOffset;
        } else if (state == STATE_EXPANDED) {
            top = mMarginTop;
        } else {
            throw new IllegalArgumentException("Illegal state argument: " + state);
        }

        if (mViewDragHelper.smoothSlideViewTo(child, 0, top)) {
            ViewCompat.postOnAnimation(child, new SettleRunnable(child));
        }
    }

    /**
     * Sets a callback to be notified of bottom sheet events.
     *
     * @param callback The callback to notify when bottom sheet events occur.
     */
    public void setBottomSheetCallback(BottomCallback callback) {
        mCallback = callback;
    }

    /**
     * A utility function to get the {@link BottomBehavior} associated with the {@code view}.
     *
     * @param view The {@link View} with {@link BottomBehavior}.
     * @return The {@link BottomBehavior} associated with the {@code view}.
     */
    @SuppressWarnings("unchecked")
    public static BottomBehavior from(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof BottomBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BottomBehavior");
        }
        return (BottomBehavior) behavior;
    }


}
