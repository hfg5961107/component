package com.easymi.component.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by xyin on 2016/10/24.
 * 动画类相关工具.
 */

public class AnimationUtil {

    /**
     * view跳一下动动画.
     *
     * @param fromYDelta fromYDelta
     * @param toYDelta   toYDelta
     * @param targetView view
     * @param listener   listener
     */
    public static void jumpAnimation(float fromYDelta, float toYDelta, long duration, View targetView,
                                     Animation.AnimationListener listener) {
        if (targetView == null) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(duration);
        animation.setFillAfter(false); //旋转后是否停留在这个状态
        if (listener != null) {
            animation.setAnimationListener(listener);
        }
        targetView.startAnimation(animation);
    }


    /**
     * 旋转动画.
     */
    public static void rotateView(View view, int fromDegrees, final int toDegrees, long duration) {
        if (view == null) {
            return;
        }

        //旋转起始角度fromDegrees,旋转结束角度toDegrees
        //x轴相对于自己,距离x轴边界50%,y轴相对于自己,距离y轴边界50%,即图片中心旋转
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true); //旋转后停留在这个状态
        view.startAnimation(rotateAnimation);
    }


}
