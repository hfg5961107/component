package com.easymi.component.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class StickyDecoration<T extends Sticky> extends RecyclerView.ItemDecoration {

    private int mStickyHeight;
    private int mDividerHeight;

    public StickyDecoration(int mStickyHeight, int mDividerHeight) {
        this.mStickyHeight = mStickyHeight;
        this.mDividerHeight = mDividerHeight;
    }

    /**
     * 是否为一个组的末尾.
     */
    private boolean isLastInGroup(StickyAdapter adapter, int position) {
        Sticky current = adapter.getSticky(position);
        Sticky next = adapter.getSticky(position + 1);
        return next == null || !TextUtils.equals(next.groupTag(), current.groupTag());
    }


    /**
     * 是否在一个组的首部.
     */
    private boolean isFirstInGroup(StickyAdapter adapter, int position) {
        if (position == 0) {
            return true;
        }
        Sticky current = adapter.getSticky(position);
        Sticky lastSticky = adapter.getSticky(position - 1);
        return lastSticky == null || !TextUtils.equals(current.groupTag(), lastSticky.groupTag());
    }

    /**
     * 参数检测.
     */
    private void check(RecyclerView recyclerView) {
        if (!(recyclerView.getAdapter() instanceof StickyAdapter)) {
            throw new ClassCastException("adapter must implements StickyAdapter");
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        check(parent);
        StickyAdapter adapter = (StickyAdapter) parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);
        Sticky sticky = adapter.getSticky(position);
        if (sticky == null) {
            super.getItemOffsets(outRect, view, parent, state);
        } else {
            if (isFirstInGroup(adapter, position)) {
                outRect.top = mStickyHeight;
            } else {
                outRect.top = mDividerHeight;
            }
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        check(parent);

        StickyAdapter adapter = (StickyAdapter) parent.getAdapter();

        //屏幕正显示的view数量
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            Sticky sticky = adapter.getSticky(position);
            if (sticky == null) {
                super.onDrawOver(c, parent, state);
                continue;
            }

            if (i == 0) {
                int top = 0;
                if (isLastInGroup(adapter, position)) {
                    int realTop = view.getBottom() - mStickyHeight;
                    top = Math.min(realTop, top);
                }
                int bottom = top + mStickyHeight;
                onDrawSticky(c, (T) sticky, left, top, right, bottom);
            } else if (isFirstInGroup(adapter, position)) {
                int top = view.getTop() - mStickyHeight;
                int bottom = view.getTop();
                onDrawSticky(c, (T) sticky, left, top, right, bottom);
            } else {
                super.onDrawOver(c, parent, state);
            }
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        check(parent);
        StickyAdapter adapter = (StickyAdapter) parent.getAdapter();

        //屏幕正显示的view数量
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        //开始绘制
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            Sticky sticky = adapter.getSticky(position);
            if (sticky != null && !isFirstInGroup(adapter, position)) {
                int top = view.getTop() - mDividerHeight;
                int bottom = view.getTop();
                onDrawDivider(c, (T) sticky, left, top, right, bottom);
            } else {
                super.onDraw(c, parent, state);
            }
        }

    }

    /**
     * 绘制粘性头.
     *
     * @param canvas canvas
     * @param data   adapter绑定的数据
     * @param left   绘制区域rect相对x轴的left
     * @param top    绘制区域rect相对y轴的top
     * @param right  绘制区域rect相对x轴的right
     * @param bottom 绘制区域rect相对y轴的bottom
     */
    protected abstract void onDrawSticky(Canvas canvas, T data,
                                         float left, float top, float right, float bottom);

    /**
     * 绘制分割线.
     *
     * @param canvas canvas
     * @param data   adapter绑定的数据
     * @param left   绘制区域rect相对x轴的left
     * @param top    绘制区域rect相对y轴的top
     * @param right  绘制区域rect相对x轴的right
     * @param bottom 绘制区域rect相对y轴的bottom
     */
    protected abstract void onDrawDivider(Canvas canvas, T data,
                                          float left, float top, float right, float bottom);


}
