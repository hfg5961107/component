package com.easymi.component.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.easymi.component.Config;
import com.easymi.component.R;
import com.easymi.component.app.ActManager;
import com.easymi.component.app.XApp;
import com.easymi.component.entity.TiredNotice;
import com.easymi.component.network.GsonUtil;
import com.easymi.component.receiver.GpsReceiver;
import com.easymi.component.receiver.NetWorkChangeReceiver;
import com.easymi.component.rxmvp.RxManager;
import com.easymi.component.utils.EmUtil;
import com.easymi.component.utils.NetUtil;
import com.easymi.component.utils.PhoneFunc;
import com.easymi.component.utils.StringUtils;
import com.easymi.component.utils.SysUtil;
import com.easymi.component.utils.ToastUtil;
import com.easymi.component.widget.swipeback.ikew.SwipeBackActivityBase;
import com.easymi.component.widget.swipeback.ikew.SwipeBackActivityHelper;
import com.easymi.component.widget.swipeback.ikew.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.Locale;

import io.reactivex.disposables.Disposable;

//import com.easymi.component.Glide4Engine;
//import com.zhihu.matisse.Matisse;
//import com.zhihu.matisse.MimeType;
//import com.zhihu.matisse.internal.entity.CaptureStrategy;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName: RxBaseActivity
 *
 * @Author: hufeng
 * Date: 2018/12/24 下午1:10
 * Description:Activity基类
 * History:
 */
public abstract class RxBaseActivity extends RxAppCompatActivity implements
        GpsReceiver.OnGpsStatusChangeListener,
        NetWorkChangeReceiver.OnNetChange, SwipeBackActivityBase {

    protected RxManager mRxManager;

    private GpsReceiver gpsReceiver;

    private NetWorkChangeReceiver netChangeReceiver;
    //疲劳驾驶
    private TiredReceiver tiredReceiver;

    private SwipeBackActivityHelper mHelper;

    protected long lastChangeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        mRxManager = new RxManager();
        ActManager.getInstance().addActivity(this);

        // TODO 2018/9/14 原始的侧滑返回问题严重，最大问题是不支持API28 ，暂时移除
        // TODO 见https://www.jianshu.com/p/26fac8d30058?from=groupmessage
//        if (isEnableSwipe()) {
//            mHelper = new SwipeBackActivityHelper(this);
//            mHelper.onActivityCreate();
//        }

        setContentView(getLayoutId());

        //初始化控件
        initViews(savedInstanceState);
        //初始化ToolBar
        initToolBar();

        rxPermissions = new RxPermissions(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (null != mHelper) {
            mHelper.onPostCreate();
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public com.easymi.component.widget.swipeback.ikew.SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public abstract boolean isEnableSwipe();

    @Override
    protected void onStart() {
        super.onStart();

        gpsReceiver = new GpsReceiver();
        gpsReceiver.setListener(this);
        IntentFilter intentFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(gpsReceiver, intentFilter, EmUtil.getBroadCastPermission(), null);

        netChangeReceiver = new NetWorkChangeReceiver();
        netChangeReceiver.setEvent(this);
        IntentFilter netFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netChangeReceiver, netFilter, EmUtil.getBroadCastPermission(), null);


        tiredReceiver = new TiredReceiver();
        IntentFilter tiredFilter = new IntentFilter(Config.TIRED_NOTICE);
        registerReceiver(tiredReceiver, tiredFilter, EmUtil.getBroadCastPermission(), null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //回到前台时停止播放静音音频
        XApp.getInstance().stopPlaySlientMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SysUtil.isRunningInBackground(this)) {
            //后台运行时 静音播放音频保活
            XApp.getInstance().playSlientMusic();
            Toast.makeText(this,
                    String.format(Locale.CHINESE, "已离开%s，注意信息安全", getString(R.string.app_name)),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {

        if (null != netDialog && netDialog.isShowing()) {
            netDialog.dismiss();
        }
        if (null != gpsDialog && gpsDialog.isShowing()) {
            gpsDialog.dismiss();
        }

        super.onStop();
        unregisterReceiver(gpsReceiver);
        unregisterReceiver(netChangeReceiver);
        unregisterReceiver(tiredReceiver);
    }

    /**
     * 设置布局layout
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化toolbar
     */
    public void initToolBar() {

    }

    /**
     * 加载数据
     */
    public void loadData() {
    }

    /**
     * 显示进度条
     */
    public void showProgressBar() {
    }

    /**
     * 隐藏进度条
     */
    public void hideProgressBar() {
    }

    /**
     * 初始化recyclerView
     */
    public void initRecyclerView() {
    }

    /**
     * 初始化refreshLayout
     */
    public void initRefreshLayout() {
    }

    /**
     * 设置数据显示
     */
    public void finishTask() {
    }

    @Override
    protected void onDestroy() {
        mRxManager.clear();
        super.onDestroy();
    }

    private AlertDialog gpsDialog;

    /**
     * 提醒用户前往设置界面开启GPS
     */
    @Override
    public void showGpsState(boolean isOpen) {
        if (!isOpen) {
            if (null == gpsDialog) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.please_open_gps));
                builder.setNegativeButton(getResources().getString(R.string.ok),
                        (dialog, which) -> {
                            if (!PhoneFunc.isGPSEnable(RxBaseActivity.this)) {
                                try {
                                    if ("ZTE".equalsIgnoreCase(Build.MANUFACTURER)) {
                                        ToastUtil.showMessage(RxBaseActivity.this, getResources().getString(R.string.please_open_gps));
                                    } else {
                                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                } catch (Exception e) {
                                    ToastUtil.showMessage(RxBaseActivity.this, getResources().getString(R.string.please_open_gps));
                                }
                            } else {
                                dialog.dismiss();
                            }
                        });
                gpsDialog = builder.create();
            } else {
                if (gpsDialog.isShowing()) {
                    return;
                }
            }
            gpsDialog.show();
        } else {
            if (null != gpsDialog && gpsDialog.isShowing()) {
                gpsDialog.dismiss();
            }
        }

    }

    private AlertDialog netDialog;

    @Override
    public void onNetChange(int status) {
        if (status == NetUtil.NETWORK_NONE) {
            if (netDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.lost_net_work));
                builder.setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss());
                netDialog = builder.create();
            } else {
                if (netDialog.isShowing()) {
                    return;
                }
            }
            netDialog.show();
        } else {
            if (null != netDialog && netDialog.isShowing()) {
                netDialog.dismiss();
            }
        }

    }

    /**
     * 疲劳驾驶接收提示
     */
    class TiredReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.isNotBlank(action)) {
                if (intent.getAction().equals(Config.TIRED_NOTICE)) {
                    TiredNotice tiredNotice = GsonUtil.parseJson(intent.getStringExtra("data"), TiredNotice.class);
                    String message = "";
                    if (tiredNotice.isTired == 1) {
                        message = "您已处于疲劳驾驶状态" + tiredNotice.tiredTime + "分钟，为了保障您和乘客的安全，请休息片刻再继续工作。休息时间：" + tiredNotice.relaxTime + "分钟。";
                    } else {
                        message = "您即将进入疲劳驾驶状态，请合理安排工作休息时间。正常工作状态剩余时间:" + tiredNotice.remainTime + "分钟。";
                    }

                    AlertDialog dialog = new AlertDialog.Builder(RxBaseActivity.this)
                            .setTitle("疲劳提醒")
                            .setMessage(message)
                            .setNegativeButton("好的，我知道了", (dialogInterface, i) -> dialogInterface.dismiss())
                            .create();
                    dialog.show();
                }
            }
        }
    }


    private RxPermissions rxPermissions;

    public void choicePic(int x, int y, int max) {
        Disposable d = rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA).subscribe(granted -> {
            if (granted) {
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                        .minSelectNum(1)// 最小选择数量 int
                        .maxSelectNum(max)// 最大图片选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/emdriver")// 自定义拍照保存路径,可不填
                        .enableCrop(true)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .withAspectRatio(x, y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                        .isGif(false)// 是否显示gif图片 true or false
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                        .openClickSound(false)// 是否开启点击声音 true or false
                        .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                        .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
                ToastUtil.showMessage(this, "请开启应用权限", Toast.LENGTH_SHORT);
            }
        });
    }

    public void takePictures(int x, int y) {
        // 进入相机 以下是例子：用不到的api可以不写
        PictureSelector.create(RxBaseActivity.this)
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/emdriver")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(x, y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }
}
