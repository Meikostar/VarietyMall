package com.smg.variety.view.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.view.widgets.X5WebView;
import com.smg.variety.view.widgets.autoview.ActionbarView;
import butterknife.BindView;

public class RechargeWebActivity extends BaseActivity {
    @BindView(R.id.my_web_view)
    X5WebView my_web_view;
    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;

    @Override
    public void initListener() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_webview_layout;
    }

    @Override
    public void initView() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String urlPath = bundle.getString(Constants.INTENT_WEB_URL);
            String titleStr = bundle.getString(Constants.INTENT_WEB_TITLE);
            customActionBar.setTitle(titleStr);
            my_web_view.loadUrl(urlPath);
        }
        my_web_view.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                if (!TextUtils.isEmpty(url)){
                    if (url.contains("pay_success?success=1")){
                        ToastUtil.showToast("支付成功");
                        finish();
                    }
                    if (url.contains("pay_success?success=0")){
                        ToastUtil.showToast("支付失败");
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void initData() {

    }

}

