package com.smg.variety.view.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.WebViewUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.widgets.autoview.ActionbarView;

import java.util.Objects;

import butterknife.BindView;

/**
 * 商城快讯
 * Created by xld021 on 2018/4/14.
 */

public class WebUtilsActivity extends BaseActivity {
    private static final String TAG        = WebUtilsActivity.class.getSimpleName();
    public static final  String MESSAGE_ID = "message_id";//调用者传递的名字
    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;

    private long message_id;//消息ID
    @BindView(R.id.shop_message_webview)
    WebView webviewWebView;
    public static final String WEBURL = "weburl";
    public static final String WEBTITLE = "webtitle";

    @Override
    public int getLayoutId() {
        return R.layout.ui_shop_message_layout;
    }
  private String id="";
  private int state;
    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
    }
    private int type;

    @Override
    public void initData() {
        type=getIntent().getIntExtra("type",1);
        String url = getIntent().getStringExtra(WEBURL);
        id = getIntent().getStringExtra("id");
        String webTitle = getIntent().getStringExtra(WEBTITLE);
        if(!TextUtils.isEmpty(id)){
            getIdInfo();
        }else {

            if(!TextUtils.isEmpty(url)){
                webviewWebView.getSettings().setJavaScriptEnabled(true);
                customActionBar.setTitle(webTitle);

                webviewWebView.setWebViewClient(new WebViewClient() { //通过webView打开链接，不调用系统浏览器

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        // Toast.makeText(getApplicationContext(), "网络连接失败 ,请连接网络。", Toast.LENGTH_SHORT).show();
                    }
                });


                WebSettings webSettings = webviewWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setBuiltInZoomControls(false);


                if (null != url) {
                    webviewWebView.loadUrl(url);
                }

                webviewWebView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (webviewWebView.canGoBack()) {
                            webviewWebView.goBack();
                        } else {

                        }
                    }
                });
            }else {
                if(type==1){
                    getInfoYs();
                }else if(type==2){
                    getInfoFw();
                }else if(type==3){
                    getInfoDp();
                }else if(type==5){
                    getShopService();
                }else if(type==6){
                    reg_agreement();
                }else  if(type==7){
                    task_rules();
                }
            }


        }



    }
    private String url;
    @Override
    public void initListener() {

    }


    private void getInfoFw() {
        showLoadDialog();
        DataManager.getInstance().privacy_policy(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }
    private void getIdInfo() {
        showLoadDialog();
        DataManager.getInstance().getHelpDetail(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        },"help",id);
    }
    private void getShopService() {
        showLoadDialog();
        DataManager.getInstance().getShopService(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    private void getInfoYs() {
        showLoadDialog();
        DataManager.getInstance().tos(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {
                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    private void task_rules() {
        showLoadDialog();
        DataManager.getInstance().task_rules(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }
    private void reg_agreement() {
        showLoadDialog();
        DataManager.getInstance().reg_agreement(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    private void getInfoDp() {
        showLoadDialog();
        DataManager.getInstance().shop_service(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){
                    if(!TextUtils.isEmpty(data.title)){
                        actionbar.setTitle(data.title);
                    }

                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(WebUtilsActivity.this));
                        WebViewUtil.loadHtml(webviewWebView, data.content);
                    }
                }

                dissLoadDialog();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }
    @Override
    protected void onDestroy() {
        webviewWebView.destroy();
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webviewWebView.canGoBack()) {
                webviewWebView.goBack();
            } else {
                finish();
            }
        }
        return true;
    }


}
