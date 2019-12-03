package com.easymi.component.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.easymi.component.ComponentService;
import com.easymi.component.Config;
import com.easymi.component.R;
import com.easymi.component.entity.PayEvent;
import com.easymi.component.entity.PayResult;
import com.easymi.component.entity.RechargeResult;
import com.easymi.component.network.ApiManager;
import com.easymi.component.network.HttpResultFunc;
import com.easymi.component.network.HttpResultFunc2;
import com.easymi.component.network.MySubscriber;
import com.easymi.component.utils.Log;
import com.easymi.component.widget.ComPayDialog;
import com.google.gson.JsonElement;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class RxPayActivity extends RxBaseActivity implements ComPayDialog.OnCancelListener{

    protected long payOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResult(PayEvent event) {
        if (event.success) {
            onPaySuc();
        } else {
            onPayFail();
        }
    }

    public abstract void onPaySuc();

    public abstract void onPayFail();

    /**
     * 各种支付回调处理
     */
    protected Handler payHandler = new Handler(msg -> {
        if (msg.what == 0) {
            PayResult result = new PayResult((String) msg.obj);
            if (result.resultStatus.equals("9000")) {
                onPaySuc();
            } else {
                onPayFail();
            }
        } else {
            onPayFail();
        }
        return true;
    });


    /**
     * 调用支付包充值
     *
     * @param data
     */
    protected void launchZfb(String data) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RxPayActivity.this);
                String result = alipay
                        .pay(data, true);

                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        });
    }

    public void launchWeixin(JsonElement data) {
        JSONObject json;
        try {
            json = new JSONObject(data.toString());
            PayReq req = new PayReq();
            req.appId = json.getString("wx_app_id");
            req.partnerId = json.getString("wx_mch_id");
            req.prepayId = json.getString("wx_pre_id");
            req.nonceStr = json.getString("wx_app_nonce");
            req.timeStamp = json.getString("wx_app_ts");
            req.packageValue = json.getString("wx_app_pkg");
            req.sign = json.getString("wx_app_sign");
            req.extData = "app data";
            Log.e("wxPay", "正常调起支付");

            IWXAPI api = WXAPIFactory.createWXAPI(RxPayActivity.this, req.appId);
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信

            api.sendReq(req);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void showDialog(long orderId) {
        ComPayDialog comPayDialog = new ComPayDialog(this);
        comPayDialog.setOnMyClickListener(view -> {
            comPayDialog.dismiss();
            if (view.getId() == R.id.pay_wenXin) {
                toPay("CHANNEL_APP_WECHAT", orderId);
            } else if (view.getId() == R.id.pay_zfb) {
                toPay("CHANNEL_APP_ALI", orderId);
            } else if (view.getId() == R.id.pay_balance) {
                toPay("PAY_DRIVER_BALANCE", orderId);
            }
        });
        comPayDialog.show();
    }


    protected void showDialog(long orderId, double money) {
        ComPayDialog comPayDialog = null;
        if (comPayDialog == null){
            comPayDialog = new ComPayDialog(this);
            comPayDialog.setMoney(money);
            comPayDialog.setOnMyClickListener(view -> {
                if (view.getId() == R.id.pay_wenXin) {
                    toPay("CHANNEL_APP_WECHAT", orderId);
                } else if (view.getId() == R.id.pay_zfb) {
                    toPay("CHANNEL_APP_ALI", orderId);
                } else if (view.getId() == R.id.pay_balance) {
                    toPay("PAY_DRIVER_BALANCE", orderId);
                }
            });
        }
        comPayDialog.setOnCancelListener(this);
        comPayDialog.show();
    }

    /**
     * 支付
     *
     * @param payType
     */
    private void toPay(String payType, long orderId) {
        Observable<JsonElement> observable = ApiManager.getInstance().createApi(Config.HOST, ComponentService.class)
                .payOrder(true, orderId, payType)
                .map(new HttpResultFunc2<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mRxManager.add(observable.subscribe(new MySubscriber<>(this, true, true, jsonElement -> {
            payOrderId = orderId;
            if (payType.equals("CHANNEL_APP_WECHAT")) {
                launchWeixin(jsonElement);
            } else if (payType.equals("CHANNEL_APP_ALI")) {
                String url = null;
                try {
                    url = new JSONObject(jsonElement.toString()).getString("ali_app_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                launchZfb(url);
            } else if (payType.equals("PAY_DRIVER_BALANCE")) {
                //todo 司机代付
                onPaySuc();
            }
        })));
    }


    public void recharge(String payType, Double money) {
        Observable<RechargeResult> observable = ApiManager.getInstance().createApi(Config.HOST, ComponentService.class)
                .recharge(payType, money)
                .filter(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        mRxManager.add(observable.subscribe(new MySubscriber<>(this, true, true, rechargeResult -> {
            if (payType.equals("CHANNEL_APP_WECHAT")) {
                launchWeixin(rechargeResult.data);
            } else if (payType.equals("CHANNEL_APP_ALI")) {
                String url = null;
                try {
                    url = new JSONObject(rechargeResult.data.toString()).getString("ali_app_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                launchZfb(url);
            }
        })));
    }

}
