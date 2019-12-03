package com.easymi.component.pay;

//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import com.easymi.component.utils.Log;
//import android.widget.Toast;
//
//import com.alipay.sdk.app.PayTask;
//import com.tencent.mm.sdk.modelpay.PayReq;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.unionpay.UPPayAssistEx;
//import com.easymi.component.app.XApp;
//import com.easymi.component.utils.Log;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Map;

/**
 * Created by xyin on 2017/1/9.
 * 支付功能.
 */

public class Payer {

//    private static final String TAG = "Payer";
//
//    /*支付宝支付*/
//    private OnAlipayListener alipayListener;    //支付宝支付结果listener
//    private static final int REQUEST_ALIPAY = 1;    //支付宝
//    public static final String ALIPAY_SUCCEED = "9000";    //支付宝支付成功
//
//    /*银联支付*/
//    private static final String UNIONPAY_MODE = "01";    //01测试环境,00正式环境
//    public static final int PLUGIN_VALID = 0;
//    public static final int PLUGIN_NOT_INSTALLED = -1;
//    public static final int PLUGIN_NEED_UPGRADE = 2;
//
//
//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            switch (msg.what) {
//                case REQUEST_ALIPAY:
//                    //noinspection unchecked
//                    handleAlipay((Map<String, String>) msg.obj);
//                    break;
//            }
//            return true;
//        }
//    });
//
//    /**
//     * 构造方法.
//     */
//    public Payer() {
//    }
//
//
//    /**
//     * <p>调用银联支付,在onActivityResult通过以下方式获取支付结果,
//     * String resultStr = data.getExtras().getString("pay_result");
//     * if ("fail".equalsIgnoreCase(resultStr)) {
//     * //支付失败
//     * } else if ("cancel".equalsIgnoreCase(resultStr)) {
//     * //取消支付
//     * } else if ("success".equalsIgnoreCase(resultStr)) {
//     * //支付成功
//     * }</p>
//     *
//     * @param context context
//     * @param tn      交易流水号
//     */
//    public static void payUnionPay(final Context context, String tn) {
//        int ret = UPPayAssistEx.startPay(context, null, null, tn, UNIONPAY_MODE);
//        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
//            new AlertDialog.Builder(context)
//                    .setTitle("提示")
//                    .setMessage("完成购买需要安装银联支付控件，是否安装？")
//                    .setNegativeButton("确认", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            UPPayAssistEx.installUPPayPlugin(context);
//                            dialog.dismiss();
//                        }
//                    })
//                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).create().show();
//        }
//    }
//
//
//    /**
//     * 调起支付宝支付.
//     *
//     * @param activity         activity对象
//     * @param url              需要支付的url
//     * @param onAlipayListener 支付结果监听
//     */
//    public void alipay(final Activity activity, final String url, OnAlipayListener onAlipayListener) {
//        if (activity == null || TextUtils.isEmpty(url)) {
//            Log.e(TAG, "activity is null or url is empty");
//            return;
//        }
//        alipayListener = onAlipayListener;  //设置listener
//        //开启线程调用支付宝支付
//        new Thread() {
//            @Override
//            public void run() {
//                PayTask payTask = new PayTask(activity);
//                Map<String, String> result = payTask.payV2(url, true);
//                Message message = new Message();
//                message.what = REQUEST_ALIPAY;
//                message.obj = result;
//                handler.sendMessage(message);
//            }
//        }.start();
//    }
//
//    /**
//     * 调用微信支付,微信支付结果在WXPayEntryActivity中处理.
//     *
//     * @param context     context对象
//     * @param jsonObject  从服务器获取的支付信息
//     * @param transaction 支付请求标志,用来区分各种支付场景
//     */
//    public void wxPay(Context context, JSONObject jsonObject, @NonNull String transaction) {
//        if (context == null || jsonObject == null) {
//            Log.e(TAG, "wxpay context is null or jsonObject is null");
//            return;
//        }
//        try {
//            if (!jsonObject.has("retcode")) {
//                PayReq req = new PayReq();
//                req.appId = jsonObject.getString("appid");
//                req.partnerId = jsonObject.getString("partnerid");
//                req.prepayId = jsonObject.getString("prepayid");
//                req.nonceStr = jsonObject.getString("noncestr");
//                req.timeStamp = jsonObject.getString("timestamp");
//                req.packageValue = jsonObject.getString("package");
//                req.sign = jsonObject.getString("sign");
//                req.extData = "app data"; // optional
//
//                req.transaction = transaction;  //该字段标识一个唯一请求,标注支付场景场景
//                //保存支付来源
//                req.transaction = transaction;  //该字段标识一个唯一请求,标注支付场景场景
//                CsEditor et = XApp.getEditor();
//                et.putString("wxPayFrom", transaction);
//                et.apply();
//
//                Toast.makeText(context, "正在启动微信支付...", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "invoke wechat pay succeed");
//
//                //调起支付,支付的结果回调在WXPayEntryActivity中处理
//                IWXAPI api = WXAPIFactory.createWXAPI(context, req.appId);
//                api.sendReq(req);
//
//            } else {
//                Log.e(TAG, "wxPay error -->" + jsonObject.getString("retmsg"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, "json fail,json -->" + jsonObject.toString());
//        }
//    }
//
//    /**
//     * 处理支付宝支付结果.
//     *
//     * @param result 支付后的数据
//     */
//    private void handleAlipay(Map<String, String> result) {
//        AlipayResult alipayResult = new AlipayResult(result);
//        String resultStatus = alipayResult.getResultStatus();
//        Log.d(TAG, "alipay result --> " + resultStatus);
//        if (alipayListener != null) {
//            alipayListener.payResult(resultStatus);
//        }
//    }
//
//    /**
//     * 获取需要展示给用户的提示信息.
//     *
//     * @param resultStatus 支付结果的状态码
//     * @return 提示文本的资源id
//     */
//    public static String getAlipayTips(String resultStatus) {
//        String tips = "正在处理中,请稍后查询";
//        if (resultStatus != null) {
//            switch (resultStatus) {
//                case "9000":
//                    tips = "支付成功";
//                    break;
//
//                case "8000":
//                    //正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//                    tips = "正在处理中,请稍后查询";
//                    break;
//
//                case "4000":
//                    //订单支付失败
//                    tips = "支付失败,请重试或选择其他支付方式";
//                    break;
//
//                case "5000":
//                    //重复请求
//                    tips = "重复的支付请求";
//                    break;
//
//                case "6001":
//                    //用户中途取消
//                    tips = "支付已被取消";
//                    break;
//
//                case "6002":
//                    //网络连接出错
//                    tips = "网络异常,请稍后再试";
//                    break;
//
//                case "6004":
//                    //支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
//                    tips = "正在处理中,请稍后查询";
//                    break;
//                default:
//                    break;
//            }
//        }
//        return tips;
//    }
//
//    /**
//     * 支付宝支付结果回调.
//     */
//    public interface OnAlipayListener {
//        /**
//         * 获取支付回调.
//         *
//         * @param resultStatus {@link #getAlipayTips}中"9000"
//         */
//        void payResult(String resultStatus);
//    }


}
