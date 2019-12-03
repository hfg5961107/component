package com.easymi.component.rxmvp;

/**
 * Created by xyin on 2016/9/29.
 * mvp中presenter层接口.
 */

public abstract class BasePresenter<M extends BaseModel, V extends BaseView> {
    protected M mModel;
    protected V mView;
    protected RxManager mRxManager = new RxManager();

    /**
     * 将model和view设置给presenter.
     */
    public BasePresenter setMV(M model, V view) {
        this.mModel = model;
        this.mView = view;
        this.onStart();
        return this;
    }

    /**
     * 可以在该方法中做一些初始化操作.
     */
    public void onStart() {
        //do something
    }

    /**
     * 结束该presenter相关的rxjava代码的生命周期.
     */
    public void onDestroy() {
        mRxManager.clear();
    }

}
