package com.easymi.component.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.AMapNaviRingType;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.easymi.component.ComponentService;
import com.easymi.component.Config;
import com.easymi.component.DJOrderStatus;
import com.easymi.component.R;
import com.easymi.component.base.RxBaseActivity;
import com.easymi.component.entity.DymOrder;
import com.easymi.component.entity.PassengerLcResult;
import com.easymi.component.entity.PassengerLocation;
import com.easymi.component.network.ApiManager;
import com.easymi.component.network.MySubscriber;
import com.easymi.component.push.PushEvent;
import com.easymi.component.utils.Log;
import com.easymi.component.utils.StringUtils;
import com.easymi.component.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 *
 * @Author: shine
 * Date: 2018/12/24 下午5:00
 * Description:
 * History:
 */

public class NaviActivity extends RxBaseActivity implements AMapNaviListener, AMapNaviViewListener {

    AMapNaviView mAMapNaviView;

    protected AMapNavi mAMapNavi;

    protected NaviLatLng mEndLatlng;
    protected NaviLatLng mStartLatlng;
    protected final List<NaviLatLng> sList = new ArrayList<>();
    protected final List<NaviLatLng> eList = new ArrayList<>();

    protected List<NaviLatLng> wayPoints;

    private long orderId;
    private String orderType;
    /**
     * 导航模式
     */
    private int naviMode;

    LinearLayout simpleFeeCon;
    TextView lcTxt;
    TextView feeTxt;
    private PassengerLocation mPlocation;
    private Marker plMaker;

    @Override
    public int getLayoutId() {
        return R.layout.activity_navi;
    }


    public void addPlMaker() {
        LatLng latLng = new LatLng(mPlocation.latitude, mPlocation.longitude);
        if (plMaker == null) {
            MarkerOptions markerOption = new MarkerOptions();
            markerOption.position(latLng);
            markerOption.draggable(false);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.ic_passenger_location)));
            plMaker = mAMapNaviView.getMap().addMarker(markerOption);
        } else {
            plMaker.setPosition(latLng);
        }
    }

    public void plChange(PassengerLocation plocation) {
        //订单只有在前往目的地前显示
        if (plocation != null) {
            mPlocation = plocation;
            addPlMaker();
        }
    }

    public void passengerLoc(long orderId) {
        Observable<PassengerLcResult> observable = ApiManager.getInstance().createApi(Config.HOST, ComponentService.class)
                .passengerLoc(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new MySubscriber<>(this, false, false, passengerLcResult -> {
            if (passengerLcResult.getCode() == 1) {
                plChange(passengerLcResult.data);
            }
        }));
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mStartLatlng = getIntent().getParcelableExtra("startLatlng");
        mEndLatlng = getIntent().getParcelableExtra("endLatlng");
        orderType = getIntent().getStringExtra("serviceType");
        orderId = getIntent().getLongExtra("orderId", -1);
        if (TextUtils.equals(Config.ZHUANCHE, orderType)) {
            passengerLoc(orderId);
        }
        naviMode = getIntent().getIntExtra(Config.NAVI_MODE, Config.DRIVE_TYPE);

        wayPoints = getIntent().getParcelableArrayListExtra("wayPoints");

        if (null == mStartLatlng || mEndLatlng == null) {
            finish();
            ToastUtil.showMessage(this, "数据出现错误,请重试...");
            return;
        }

        sList.add(mStartLatlng);
        eList.add(mEndLatlng);

        mAMapNaviView = findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.setUseInnerVoice(true);

    }

    @Override
    public boolean isEnableSwipe() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAMapNaviView != null) {
            mAMapNaviView.onDestroy();
        }
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        //mAMapNavi是全局的，执行订单页面还需要用，所以这里不能销毁资源
        if (mAMapNavi != null) {
            mAMapNavi.stopNavi();
            mAMapNavi.destroy();
        }
        EventBus.getDefault().unregister(this);
//        XApp.getInstance().stopVoice();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPushEvent(PushEvent event) {
        if (TextUtils.equals(Config.ZHUANCHE, orderType)) {
            plChange(event.getPassengerLocation());
        }
    }

    @Override
    public void onInitNaviFailure() {
        Log.e("NaviActivity", "初始化导航失败");
    }

    @Override
    public void onInitNaviSuccess() {
        if (naviMode == Config.WALK_TYPE) {
            mAMapNavi.calculateWalkRoute(mStartLatlng, mEndLatlng);
        } else {
//            /**
//             * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
//             *
//             * @congestion 躲避拥堵
//             * @avoidhightspeed 不走高速
//             * @cost 避免收费
//             * @hightspeed 高速优先
//             * @multipleroute 多路径
//             *
//             *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
//             *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
//             */
//            int strategy = 0;
//            try {
//                //再次强调，最后一个参数为true时代表多路径，否则代表单路径
//                strategy = mAMapNavi.strategyConvert(
//                        XApp.getMyPreferences().getBoolean(Config.SP_CONGESTION, false),
//                        XApp.getMyPreferences().getBoolean(Config.SP_AVOID_HIGH_SPEED, false),
//                        XApp.getMyPreferences().getBoolean(Config.SP_COST, false),
//                        XApp.getMyPreferences().getBoolean(Config.SP_HIGHT_SPEED, false),
//                        false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            mAMapNavi.calculateDriveRoute(sList, eList, null, 2);
        }

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {
//        XApp.getInstance().syntheticVoice(s, true); //设置使用高德内置语音时将不再回调此方法
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        ToastUtil.showMessage(this, "路线规划失败", Toast.LENGTH_LONG);
    }

    @Override
    public void onReCalculateRouteForYaw() {
//        XApp.getInstance().syntheticVoice("您已偏航，正在重新规划路径");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
//        XApp.getInstance().syntheticVoice("为躲避拥堵，正在重新规划路径");
    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
//        XApp.getInstance().syntheticVoice("路径规划成功");
        mAMapNavi.startNavi(NaviType.GPS);//驾车导航
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {
        if (i == AMapNaviRingType.RING_CAMERA) {
            //（导航状态）高速上通过测速电子眼的提示音

        } else if (i == AMapNaviRingType.RING_EDOG) {
            // 巡航状态下通过电子狗的提示音

        } else if (i == AMapNaviRingType.RING_REROUTE) {
            //偏航重算的提示音L

        } else if (i == AMapNaviRingType.RING_TURN) {
            // 马上到达转向路口的提示音

        }
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    public void showLaneInfo(AMapLaneInfo info) {

    }

    public void updateIntervalCameraInfo(AMapNaviCameraInfo info1, AMapNaviCameraInfo info2, int i) {

    }
}
