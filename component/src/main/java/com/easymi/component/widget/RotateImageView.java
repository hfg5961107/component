package com.easymi.component.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/4/11. 旋转ImageView
 */
public class RotateImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "RotateImageView";

    private float mRotateDegree = 0;    //起始角度
    private Thread rotateThread;
    private boolean isLoop = true;   //线程逻辑是否一直循环
    private boolean isPause = false;

    public RotateImageView(Context context) {
        super(context);
        rotateThread = new Thread(new RotateRunnable()); //旋转线程
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        rotateThread = new Thread(new RotateRunnable()); //旋转线程
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rotateThread = new Thread(new RotateRunnable()); //旋转线程
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Matrix matrix = new Matrix();
//        Matrix matrix = canvas.getMatrix();
        //以图片中心旋转
        matrix.postRotate(mRotateDegree, getWidth() / 2, getHeight() / 2);
        canvas.concat(matrix);  //加矩阵加载到canvas中

        super.onDraw(canvas);
    }


    /**
     * 暂停旋转
     */
    public void pauseRotate() {

        if (!rotateThread.isAlive()) {
            //当线程销毁后,该方法无效
            return;
        }
        isPause = true;
    }

    /**
     * 开始旋转
     */
    public void startRotate() {
        if (isPause) {
            //处于暂停状态时
            synchronized (rotateThread) {
                isPause = false;
                rotateThread.notify();
            }
        } else {

            if (rotateThread.isAlive()) {
                //线程正在正常运行时
                return;
            }
            //线程已经销毁时,重启线程
            isLoop = true;
            rotateThread = new Thread(new RotateRunnable());
            rotateThread.start();

        }

    }

    /**
     * 退出旋转
     */
    public void destroyRotate() {
        isLoop = false;   //不循环
        isPause = false;   //非暂停
        synchronized (rotateThread) {
            rotateThread.notify();
        }
    }

    /**
     * 将图片恢复至0角度,并且暂停旋转
     */
    public void reset() {
        pauseRotate();
        mRotateDegree = 0;  //还原成0度
        invalidate();   //重绘
    }


    /**
     * 旋转线程
     */
    private class RotateRunnable implements Runnable {

        @Override
        public void run() {

            while (isLoop) {

                if (!isPause) {
                    //不暂停线程
                    mRotateDegree += 1.44f;  //每次增加1.44角度
                    if (mRotateDegree > 72000000) {   //避免溢出成负数
                        mRotateDegree %= 360;
                    }
                    postInvalidate();
                    try {
                        Thread.currentThread().sleep(4);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //暂停线程
                    synchronized (rotateThread) {
                        try {
                            //暂停该线程
                            rotateThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }


}
