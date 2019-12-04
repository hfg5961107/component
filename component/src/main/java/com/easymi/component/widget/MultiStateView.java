package com.easymi.component.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;

import com.easymi.component.R;
import com.easymi.component.utils.Log;

/**
 * Created by xyin on 2016/11/2.
 * 多状态视图.
 */

public class MultiStateView extends FrameLayout implements View.OnClickListener {

    private static final String TAG = "MultiStateView";

    /*
     * view显示状态.
     */
    public static final int STATE_NORMAL = 10001;
    public static final int STATE_LOADING = 10002;
    public static final int STATE_ERROR = 10004;
    public static final int STATE_EMPTY = 10005;
    public static final int STATE_OTHER = 10006;

    private SparseArray<View> mStateViewArray = new SparseArray<>();
    private SparseIntArray mLayoutIDArray = new SparseIntArray();

    private View mContentView;  //正常显示的布局的view
    private int mCurrentState = STATE_NORMAL;  //当前的状态

    private OnClickStateListener onClickStateListener;    //非正常显示状态的view点击监听接口

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView);
        int resIdLoading = a.getResourceId(R.styleable.MultiStateView_loadingView, -1);
        int resIdError = a.getResourceId(R.styleable.MultiStateView_errorView, -1);
        int resIdEmpty = a.getResourceId(R.styleable.MultiStateView_emptyView, -1);
        int resIdOther = a.getResourceId(R.styleable.MultiStateView_otherView, -1);

        if (resIdLoading != -1) {
            addViewForStatus(STATE_LOADING, resIdLoading);
        }

        if (resIdError != -1) {
            addViewForStatus(STATE_ERROR, resIdError);
        }

        if (resIdEmpty != -1) {
            addViewForStatus(STATE_EMPTY, resIdEmpty);
        }

        if (resIdOther != -1) {
            addViewForStatus(STATE_OTHER, resIdOther);
        }

        a.recycle();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        validContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        validContentView(child);
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        validContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        validContentView(child);
        super.addView(child, index, params);
    }

    /**
     * 设置显示状态为的status对应的view.
     *
     * @param state status状态
     */
    public void setStatus(int state) {
        if (state == mCurrentState) {
            return;
        }

        getCurrentView().setVisibility(GONE);
        View view = getView(state);
        mCurrentState = state;
        if (view != null) {
            view.setVisibility(VISIBLE);
        } else {
            int resLayoutID = mLayoutIDArray.get(state);  //获取state状态对应的资源文件
            if (resLayoutID != 0) {
                view = LayoutInflater.from(getContext()).inflate(resLayoutID, this, false);
                mStateViewArray.put(state, view);   //将view存放在队列中
                addView(view);  //增加到parent中
                view.setVisibility(VISIBLE);
                if (onClickStateListener != null) {
                    view.setOnClickListener(this);
                }
            } else {
                Log.e(TAG, "resLayoutId does not exist");
            }
        }
    }

    /**
     * 获取当前状态的View.
     *
     * @return 当前状态的View
     */
    public View getCurrentView() {
        View view = getView(mCurrentState);
        if (view == null && mCurrentState == STATE_NORMAL) {
            throw new NullPointerException("content is null");
        } else if (view == null) {
            throw new NullPointerException("current state view is null, state = " + mCurrentState);
        }
        return view;
    }

    /**
     * 增加一个新的状态视图.
     *
     * @param state       新状态的flag值
     * @param resLayoutId 状态对应的资源id.
     */
    public void addStateView(int state, @LayoutRes int resLayoutId) {
        switch (state) {
            case STATE_EMPTY:
            case STATE_ERROR:
            case STATE_LOADING:
            case STATE_NORMAL:
            case STATE_OTHER:
                throw new IllegalArgumentException("state value already existed");
            default:
                break;
        }

        if (mLayoutIDArray.indexOfKey(state) >= 0) {
            throw new IllegalArgumentException("state value already existed");
        }

        addViewForStatus(state, resLayoutId);
    }

    /**
     * 获取指定状态的View.
     *
     * @param state 状态类型
     * @return 指定状态的View
     */
    public View getView(int state) {
        return mStateViewArray.get(state);
    }

    /**
     * 获取当前View的状态值.
     *
     * @return 当前的状态值 {@link #STATE_NORMAL}等
     */
    public int getCurrentState() {
        return mCurrentState;
    }

    /**
     * 检查当前view是否为content.
     *
     * @param view view
     * @return view是有效的contentView
     */
    private boolean isValidContentView(View view) {
        //当前没有contentView且存储队列里没有该view时,该view方可作为contentView
        //即view是通过xml布局文件加入的时即为content view
        return mContentView == null && mStateViewArray.indexOfValue(view) == -1;   //-1表示没有该元素
    }

    /**
     * 检查当前view是否为content.
     */
    private void validContentView(View view) {
        if (isValidContentView(view)) {
            mContentView = view;
            mStateViewArray.put(STATE_NORMAL, view);
        } else if (mCurrentState != STATE_NORMAL) {
            mContentView.setVisibility(GONE);
        }
    }

    /**
     * 将status状态值作为key,resLayoutId资源id作为value存储起来.
     *
     * @param status      状态值
     * @param resLayoutId 资源id
     */
    private void addViewForStatus(int status, int resLayoutId) {
        mLayoutIDArray.put(status, resLayoutId);
    }

    /**
     * 设置一个监听对象,注意监听器需要先于{@link #setStatus(int)}设置才生效.
     *
     * @param onClickStateListener onClickStateListener
     */
    public void setOnClickStateListener(OnClickStateListener onClickStateListener) {
        this.onClickStateListener = onClickStateListener;
    }

    @Override
    public void onClick(View v) {
        if (onClickStateListener != null) {
            onClickStateListener.onClickState(mCurrentState, v);
        }
    }

    /**
     * 非正常显示状态时view的点击监听接口.
     */
    public interface OnClickStateListener {
        void onClickState(int state, View view);
    }

}
