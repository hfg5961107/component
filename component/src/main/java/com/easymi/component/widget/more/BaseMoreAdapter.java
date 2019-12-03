package com.easymi.component.widget.more;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easymi.component.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyin on 2016/12/29.
 * 使用MoreRecyclerView时的base adapter.
 */

public abstract class BaseMoreAdapter extends MoreRecyclerView.MoreAdapter {

    //more adapter中数据源
    protected List<Object> mList = new ArrayList<>();

    @NonNull
    @Override
    protected View createFootView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.lib_rv_foot, parent, false);
    }

    @Override
    protected int getCount() {
        return mList.size();
    }

    @Override
    protected void onNoMore(View footView) {
        setTips(footView, View.GONE, R.string.lib_no_more_data);
    }

    @Override
    protected void onLoadError(View footView) {
        setTips(footView, View.GONE, R.string.lib_again_up);
    }

    @Override
    protected void onLoading(View footView) {
        setTips(footView, View.VISIBLE, R.string.lib_loading);
    }

    @Override
    protected void onHideLoading(View footView) {
        if (footView != null) {
            footView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 添加数据到adapter的数据源中.
     *
     * @param list    外部需要被添加的数据
     * @param isClean 如果需要清除adapter中原先的数据时传入true,否则为false
     */
    public <T> void addData(List<T> list, boolean isClean) {
        if (list != null && !list.isEmpty()) {
            if (isClean) {
                mList.clear();
            }
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置底部显示的内容.
     *
     * @param footView   foot view
     * @param visibility progressbar的显示状态{@link View#VISIBLE, View#GONE}
     * @param tipsId     显示的文字提示资源id
     */
    private void setTips(View footView, int visibility, int tipsId) {
        if (footView != null) {
            footView.setVisibility(View.VISIBLE);
            View view = footView.findViewById(R.id.foot_progressbar);
            if (view != null) {
                view.setVisibility(visibility);
            }
            TextView tv = (TextView) footView.findViewById(R.id.foot_tv);
            if (tv != null) {
                tv.setText(tipsId);
            }
        }
    }


}
