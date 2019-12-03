package com.easymi.component.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easymi.component.rxmvp.RxManager;
import com.easymi.component.widget.RxProgressHUD;
import com.trello.rxlifecycle.components.support.RxFragment;
/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:RxLazyFragment
 * @Author: hufeng
 * Date: 2018/12/24 下午1:10
 * Description: 懒加载fragment 适用于ViewPager中预加载的那种fragment
 * History:
 */
public abstract class RxLazyFragment extends RxFragment {
    private View parentView;
    private FragmentActivity activity;
    /**
     * 标志位 标志已经初始化完成。
     */
    protected boolean isPrepared;
    /**
     * 标志位 fragment是否可见
     */
    protected boolean isVisible;

    protected RxProgressHUD hud;

    /**
     * rx请求管理者
     */
    protected RxManager mRxManager = new RxManager();

    public abstract
    @LayoutRes
    int getLayoutResId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getSupportActivity();
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finishCreateView(savedInstanceState);
    }

    /**
     * 初始化views
     *
     * @param state
     */
    public abstract void finishCreateView(Bundle state);


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRxManager.clear();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null;
    }


    public FragmentActivity getSupportActivity() {
        return super.getActivity();
    }


    public android.app.ActionBar getSupportActionBar() {
        return getSupportActivity().getActionBar();
    }


    public Context getApplicationContext() {
        return this.activity == null ? (getActivity() == null ?
                null : getActivity().getApplicationContext()) : this.activity.getApplicationContext();
    }


    /**
     * Fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * fragment显示时才加载数据
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * fragment懒加载方法
     */
    protected void lazyLoad() {
    }

    /**
     * fragment隐藏
     */
    protected void onInvisible() {
    }

    /**
     * 加载数据
     */
    protected void loadData() {
    }

    /**
     * 显示进度条
     */
    protected void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgressBar() {
    }

    /**
     * 初始化recyclerView
     */
    protected void initRecyclerView() {
    }

    /**
     * 初始化refreshLayout
     */
    protected void initRefreshLayout() {
    }

    /**
     * 设置数据显示
     */
    protected void finishTask() {
    }


    @SuppressWarnings("unchecked")
    public <T extends View> T $(int id) {
        return (T) parentView.findViewById(id);
    }

    protected void showLoading(boolean cancelable, String title, String message, DialogInterface.OnDismissListener listener) {
        hud = new RxProgressHUD.Builder(getActivity())
                .setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setOnDismissListener(listener)
                .create();
        hud.show();
    }

    protected void hideLoading() {
        if (null != hud && hud.isShowing() && getActivity() != null) {
            hud.dismiss();
        }
    }

}
