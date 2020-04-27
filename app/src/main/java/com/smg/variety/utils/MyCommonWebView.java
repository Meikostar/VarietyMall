package com.smg.variety.utils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.Constants;
import com.smg.variety.view.widgets.X5WebView;

import butterknife.BindView;


public class MyCommonWebView extends BaseActivity {
    @BindView(R.id.my_web_view)
    X5WebView my_web_view;

    @Override
    public void initListener() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_ui_common_webview_layout;
    }

    @Override
    public void initView() {
        initHardwareAccelerate();
        initWebView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String urlPath = bundle.getString(Constants.INTENT_WEB_URL);
            my_web_view.loadUrl(urlPath);
        }
    }

    @Override
    public void initData() {

    }

    private void initWebView() {
        my_web_view.addJavascriptInterface(new MyCommonWebView.JSInterface(), "web_js");
    }


    /**
     * 启用硬件加速
     */
    private void initHardwareAccelerate() {
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class JSInterface {
        /**
         * @param url    url地址
         * @param type   1-微信好友，2-朋友圈
         * @param title  标题
         * @param imgUrl 图片的URL
         */
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void webNewsShare(String url, String type, String title, String imgUrl) {

        }

        /**
         * 关闭页面
         */
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        public void closePage() {
            finish();
        }
    }
}
