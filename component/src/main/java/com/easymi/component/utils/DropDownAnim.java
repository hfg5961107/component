package com.easymi.component.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: DropDownAnim
 * @Author: hufeng
 * @Date: 2019/2/18 下午1:37
 * @Description:
 * @History:
 */
public class DropDownAnim extends Animation {
    /**
     * 目标的高度
     */
    private int targetHeight;
    /**
     * 目标view
     */
    private View view;
    /**
     * 是否向下展开
     */
    private boolean down;

    /**
     * 构造方法
     *
     * @param targetview 需要被展现的view
     * @param vieweight  目的高
     * @param isdown     true:向下展开，false:收起
     */
    public DropDownAnim(View targetview, int vieweight, boolean isdown) {
        this.view = targetview;
        this.targetHeight = vieweight;
        this.down = isdown;
    }

    //down的时候，interpolatedTime从0增长到1，这样newHeight也从0增长到targetHeight
    @Override
    protected void applyTransformation(float interpolatedTime,
                                       Transformation t) {
        int newHeight;
        if (down) {
            newHeight = (int) (targetHeight * interpolatedTime);
        } else {
            newHeight = (int) (targetHeight * (1 - interpolatedTime));
        }
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}