package com.easymi.component.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.StringDef;
import androidx.annotation.StringRes;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easymi.component.Config;
import com.easymi.component.R;
import com.easymi.component.base.RxBaseActivity;
import com.easymi.component.utils.StringUtils;
import com.easymi.component.widget.NoUnderLineSpan;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 *
 * @Author: shine
 * Date: 2018/12/24 下午5:00
 * Description:
 * History:
 */
@Route(path = "/component/WebActivity")
public class WebActivity extends RxBaseActivity implements View.OnClickListener {
    public String name;

    WebView webView;

    TextView title;

    TextView closeAll;

    /**
     * 进度条
     */
    private ProgressBar myProgressBar;
    private String url;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        init();
        initWeb();
    }


    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public boolean isEnableSwipe() {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        webView.clearCache(true);
        webView.destroy();
        super.onDestroy();
    }

    /**
     * 初始化
     */
    public void init() {

        String articleName = getIntent().getStringExtra("articleName");
        url = Config.H5_HOST + "#/protocol?articleName=" + articleName + "&appKey=" + Config.APP_KEY;
        if (!url.contains("http") && !url.contains("https")) {
            url = "http://" + url;
        }
        title = findViewById(R.id.title);
        int titleRes = getIntent().getIntExtra("titleRes", 0);
        if (titleRes > 0) {
            title.setText(titleRes);
        } else {
            title.setText("网页");
        }
        title.getPaint().setFakeBoldText(true);
        title.postInvalidate();

        webView = findViewById(R.id.web_view);
        closeAll = findViewById(R.id.close_all);
        closeAll.setOnClickListener(this);
        myProgressBar = findViewById(R.id.myProgressBar);
    }

    /**
     * webview加载完成与否显示关闭按钮
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (webView.canGoBack()) {
                        closeAll.setVisibility(View.VISIBLE);
                    } else {
                        closeAll.setVisibility(View.GONE);
                    }
                    break;
            }
            return true;
        }
    });

    /**
     * 初始化webview的设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    public void initWeb() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setSavePassword(false);

        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");


        //解决部分H5中的一些控件标签可能使用后android中不支持 造成的白屏不显示问题
        webView.getSettings().setDomStorageEnabled(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    myProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == myProgressBar.getVisibility()) {
                        myProgressBar.setVisibility(View.VISIBLE);
                    }
                    myProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.close_all) {
            finish();

        } else {
        }
    }

    /**
     * 设置返回键事件
     *
     * @param view
     */
    public void backAction(View view) {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        }
        return false;
    }

    public static void goWebActivity(Context context, @StringRes int res, @IWebVariableType String articleName) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("titleRes", res);
        intent.putExtra("articleName", articleName);
        context.startActivity(intent);
    }

    public interface IWebVariable {
        String DRIVER_HELP = "driverHelp";
        String DRIVER_LOGIN = "driverLogin";
        String DRIVER_PUT_FORWARD = "driverPutForward";
        String DRIVER_PRIVACY_POLICY = "privacyPolicyDriver";
    }

    @StringDef({IWebVariable.DRIVER_HELP
            , IWebVariable.DRIVER_LOGIN
            , IWebVariable.DRIVER_PUT_FORWARD
            , IWebVariable.DRIVER_PRIVACY_POLICY})
    @interface IWebVariableType {
    }
}
