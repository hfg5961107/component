package com.easymi.component.widget.more;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xyin on 2016/12/3.
 * 自动加载更多功能的RecyclerView.
 */

public class MoreRecyclerView extends RecyclerView {

    //表示滑到底部时提前几个就触发 eg:为1表示还剩一个item到底时就触发加载更多
    private static final int PRE_NUMBER = 2;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;  //加载中标志位

    public MoreRecyclerView(Context context) {
        super(context);
    }

    public MoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 执行加载更多.
     */
    private void loadMore() {
        if (onLoadMoreListener == null) {
            return;
        }
        LayoutManager manager = getLayoutManager();
        //获取已经绑定到RecyclerView中可见的view数量,包括在屏幕内invisible的view
        int visibleChildCount = manager.getChildCount();
        if (visibleChildCount > 1) {
            View lastVisibleView = getChildAt(getChildCount() - 1);
            int lastVisiblePosition = getChildLayoutPosition(lastVisibleView);  //最后一个可见的view position
            if (lastVisiblePosition >= manager.getItemCount() - 1 - PRE_NUMBER) {
                isLoading = true;
                Adapter adapter = getAdapter();
                if (adapter instanceof MoreAdapter) {
                    MoreAdapter moreAdapter = (MoreAdapter) adapter;
                    moreAdapter.onLoading(moreAdapter.mFootView);
                }
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    /**
     * 设置加载更多的监听器.
     *
     * @param onLoadMoreListener onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        if (this.onLoadMoreListener == null && onLoadMoreListener != null) {
            this.onLoadMoreListener = onLoadMoreListener;
            addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == SCROLL_STATE_IDLE && !isLoading) {
                        loadMore();    //松手时且没有在刷新中时才加载更多
                    }
                }
            });
        }
    }

    /**
     * 当请求更多数据成功时并且还可以加载更多的数据时调用该方法.
     */
    public void loadComplete() {
        if (onLoadMoreListener == null) {
            return;
        }

        Adapter adapter = getAdapter();
        if (adapter instanceof MoreAdapter) {
            MoreAdapter moreAdapter = (MoreAdapter) adapter;
            moreAdapter.onHideLoading(moreAdapter.mFootView);
        }
        isLoading = false;  //清除加载中标志位
    }

    /**
     * 当没有更多数据可以请求的时候需要调用该方法.
     */
    public void loadNoMore() {
        if (onLoadMoreListener == null) {
            return;
        }
        isLoading = true;  //设置加载中标志位,禁用加载更多功能
        Adapter adapter = getAdapter();
        if (adapter instanceof MoreAdapter) {
            MoreAdapter moreAdapter = (MoreAdapter) adapter;
            moreAdapter.onNoMore(moreAdapter.mFootView);
        }
    }

    /**
     * 请求更多数据失败是调用该方法.
     */
    public void loadError() {
        if (onLoadMoreListener == null) {
            return;
        }
        Adapter adapter = getAdapter();
        if (adapter instanceof MoreAdapter) {
            MoreAdapter moreAdapter = (MoreAdapter) adapter;
            moreAdapter.onLoadError(moreAdapter.mFootView);
        }
        isLoading = false;  //可以继续加载更多
    }

    /**
     * 加载更多的接口.
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    /**
     * 代理RecyclerView.Adapter的方法.
     */
    public static abstract class MoreAdapter extends Adapter {

        private static final int FOOT_ITEM = 99;    //该值不能和实际使用的类型相同
        private View mFootView;  //加载更多时底部显示的view

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FOOT_ITEM) {
                return new FootViewHolder(createFootView(parent));
            } else {
                return onCreateHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (!(holder instanceof FootViewHolder)) {
                onBindHolder(holder, position);
            }
        }

        @Override
        public int getItemCount() {
            return getCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() == position + 1) {
                return FOOT_ITEM;
            } else {
                return getViewType(position);
            }
        }

        /**
         * see{@link #onCreateViewHolder(ViewGroup, int)}.
         */
        protected abstract ViewHolder onCreateHolder(ViewGroup parent, int viewType);

        /**
         * see{@link #onBindViewHolder(ViewHolder, int)}.
         */
        protected abstract void onBindHolder(ViewHolder holder, int position);

        /**
         * see{@link #getItemCount()}.
         */
        protected abstract int getCount();

        /**
         * see{@link #getItemViewType(int)}.
         */
        protected int getViewType(int position) {
            return 0;
        }

        /**
         * 实例化一个foot view用来展示加载更多状态.
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to an
         *               adapter position.
         * @return {@link #mFootView}
         */
        @NonNull
        protected abstract View createFootView(ViewGroup parent);


        /**
         * 正在加载更多数据时会回调该方法,此时应该提示加载更多.
         *
         * @param footView {@link #mFootView}
         */
        protected abstract void onLoading(View footView);

        /**
         * 调用{@link #loadComplete()}关闭加载更多状态时,会回调该方法.
         *
         * @param footView {@link #mFootView}
         */
        protected abstract void onHideLoading(View footView);

        /**
         * 调用{@link #loadNoMore()}方法时会回调该方法,提示用户没有更多数据可加载了.
         *
         * @param footView {@link #mFootView}
         */
        protected abstract void onNoMore(View footView);

        /**
         * 调用{@link #loadError()}方法时会回调该方法,提示用户加载数据失败了.
         *
         * @param footView {@link #mFootView}
         */
        protected abstract void onLoadError(View footView);

        /**
         * 一个用来显示在底部显示加载状态的footViewHolder.
         */
        private class FootViewHolder extends ViewHolder {
            FootViewHolder(View itemView) {
                super(itemView);
                mFootView = itemView;
            }
        }
    }

}
