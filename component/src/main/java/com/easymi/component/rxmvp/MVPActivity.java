package com.easymi.component.rxmvp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.easymi.component.utils.Log;
import com.easymi.component.utils.ReflectUtil;

/**
 * Created by xyin on 2017/2/22.
 * MVP activity基类.
 */

public abstract class MVPActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {

    protected T mPresenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //反射得到presenter以及model
        mPresenter = ReflectUtil.getT(this, 0);
        E mModel = ReflectUtil.getT(this, 1);

        if (mPresenter == null || mModel == null) {
            Log.d("TAG", "this is general activity");
        } else if (this instanceof BaseView) {
            mPresenter.setMV(mModel, (BaseView) this);
        } else {
            throw new IllegalArgumentException("this activity must implements BaseView or implements it's subclass");
        }
    }

}
