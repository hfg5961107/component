package com.easymi.component.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.easymi.component.utils.BitmapUtil;
import com.easymi.component.utils.Log;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * Created by xyin on 2017/3/4.
 * 分享相关.
 */

public class Sharer {

    private static final String TAG = "Sharer";

    /*微信和QQ分享id*/
//    private static final String WxAppId = Config.WxAppId;
//    private static final String QQAppId = Config.QQAppId;

    private static String wxAppId;
    private static String qqAppId;

    private IWXAPI iwxapi;
    private Tencent mTencent;

    private QQShareListener qqListener;

    public Sharer(Activity activity) {
        if (activity != null) {
            ApplicationInfo appInfo = null;
            try {
                appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (appInfo != null && appInfo.metaData != null) {
                qqAppId = appInfo.metaData.getString("xiaoka.qq.appid");
                wxAppId = appInfo.metaData.getString("xiaoka.wx.appid");
            }
        }
        reg2Wx(activity);
        reg2QQ(activity);
    }

    /**
     * 注册微信分享组件.
     *
     * @param context context
     */
    private void reg2Wx(Context context) {
        if (context != null) {
            iwxapi = WXAPIFactory.createWXAPI(context, wxAppId, true);
            iwxapi.registerApp(wxAppId);  //将应用app注册到微信
        }
    }

    /**
     * QQ分享注册.
     *
     * @param context context
     */
    private void reg2QQ(Context context) {
        if (context != null) {
            mTencent = Tencent.createInstance(qqAppId, context);
        }
    }


    /**
     * 分享到短信.
     *
     * @param req     分享参数.
     * @param context context
     */
    public void share2Sms(Context context, ShareReq req) {
        if (context != null && req != null) {
            Uri smsToUri = Uri.parse("smsto:");
            Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            intent.putExtra("sms_body", req.description);
            context.startActivity(intent);
        }
    }

    /**
     * 分享到QQ好友.
     *
     * @param activity activity
     * @param req      请求参数
     */
    public void share2QQ(Activity activity, ShareReq req) {
        if (qqListener == null) {
            throw new NullPointerException("qqListener is null, you must invoke setQQShareListener");
        }

        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);  //图文模式
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "  " + req.title);          //
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, req.description);       //
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, req.shareUrl);
        if (!TextUtils.isEmpty(req.imageUrl)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, req.imageUrl);    //设置分享的图片
        }
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);  //隐藏分享到QQ空间按钮

        if (mTencent != null) {
            mTencent.shareToQQ(activity, params, qqListener);
        } else {
            Log.e(TAG, "mTencent is null");
        }
    }

    /**
     * 分享到QQ空间.
     *
     * @param activity activity
     * @param req      分享参数
     */
    public void share2Qzone(Activity activity, ShareReq req) {

        if (qqListener == null) {
            throw new NullPointerException("qqListener is null, you must invoke setQQShareListener");
        }

        ArrayList<String> urls = new ArrayList<>(); //图片地址，最多支持9张图片
        urls.add(req.imageUrl);

        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "  " + req.title); //分享标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, req.description); //分享摘要
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, req.shareUrl); //点击后跳转到的URL
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, urls); //设置分享的图片url

        if (mTencent != null) {
            mTencent.shareToQzone(activity, params, qqListener);
        } else {
            Log.e(TAG, "mTencent is null");
        }
    }

    /**
     * QQ分享结果回调,该方法在onActivityResult中调用.
     *
     * @param requestCode     请求码
     * @param resultCode      结果码
     * @param data            data
     * @param qqShareListener QQ分享监听接口
     * @return 是否成功
     */
    public static boolean onActivityQQResult(int requestCode, int resultCode, Intent data, QQShareListener qqShareListener) {
        return Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
    }

    /**
     * 设置QQ分享监听接口.
     *
     * @param listener 监听接口
     */
    public void setQQShareListener(QQShareListener listener) {
        this.qqListener = listener;
    }

    /**
     * 分享到微信朋友圈.
     *
     * @param req 请求参数
     */
    public void share2Moments(ShareReq req) {
        share2Wx(req, SendMessageToWX.Req.WXSceneTimeline);
    }

    /**
     * 分享到微信好友列表.
     *
     * @param req 请求参数
     */
    public void share2Wechat(ShareReq req) {
        share2Wx(req, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 初始化微信分享结果回调(该方法在WXEntryActivity的onCreate中调用).
     *
     * @param context    context
     * @param data       WXEntryActivity页面的getIntent()
     * @param wxListener 微信分享监听接口
     */
    public static void initWxResult(Context context, Intent data, WxShareListener wxListener) {
        if (context != null && data != null && wxListener != null) {
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, wxAppId);
            iwxapi.registerApp(wxAppId);
            iwxapi.handleIntent(data, wxListener);
        }
    }

    /**
     * 分享到微信.
     *
     * @param req 分享配置
     */
    private void share2Wx(ShareReq req, int scene) {

        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = req.shareUrl;

        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = req.title;
        msg.description = req.description;

        //设置分享的略缩图
        Bitmap thumb = ThumbnailUtils.extractThumbnail(req.image, 90, 90);
        if (thumb != null) {
            msg.thumbData = BitmapUtil.bitmapToByteArray(thumb, true);
        }

        //构造一个微信Req
        SendMessageToWX.Req wxReq = new SendMessageToWX.Req();
        wxReq.transaction = req.transaction;   //该字段用于唯一标识一个请求
        wxReq.message = msg;

        //分享到朋友圈
        wxReq.scene = scene;

        Log.d(TAG, "invoke wechat share succeed!");

        if (iwxapi != null) {
            iwxapi.sendReq(wxReq);  // 调用api接口发送数据到微信
        } else {
            Log.e(TAG, "iwxapi is null");
        }
    }

    /**
     * 分享参数.
     */
    public static class ShareReq {

        private String title;   //分享的标题
        private String description; //分享内容
        private String shareUrl; //分享url
        private Bitmap image;   //分享图片
        private String imageUrl;    //分享图片的地址
        private String transaction; //事物标识

        public ShareReq() {
        }

        public ShareReq setDescription(@NonNull String description) {
            this.description = description;
            return this;
        }

        public ShareReq setShareUrl(@NonNull String shareUrl) {
            this.shareUrl = shareUrl;
            return this;
        }

        public ShareReq setImage(@NonNull Bitmap image) {
            this.image = image;
            return this;
        }

        public ShareReq setTitle(@NonNull String title) {
            this.title = title;
            return this;
        }

        public ShareReq setTransaction(@NonNull String transaction) {
            this.transaction = transaction;
            return this;
        }

        public ShareReq setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
    }

}
