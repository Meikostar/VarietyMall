package com.smg.variety.view.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.utils.TextUtil;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewActivity extends BaseActivity {

    public static final String WEBURL   = "weburl";
    public static final String WEBTITLE = "webtitle";
    public static final String TYPE     = "type";
    @BindView(R.id.tv_title)
    TextView     tvTitle;
    @BindView(R.id.iv_back)
    ImageView    ivBack;
    @BindView(R.id.navigationBar)
    LinearLayout navigationBar;
    @BindView(R.id.webview_webView)
    WebView      webviewWebView;


    private TextView  title;
    private ImageView back;
    private String urls;


    private int type = -1;

    private boolean mIsRedirect;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
         urls =intent.getStringExtra(WEBURL);
        String webTitle = intent.getStringExtra(WEBTITLE);
        type = intent.getIntExtra(TYPE, -1);

        if (!TextUtils.isEmpty(webTitle)) {
            tvTitle.setText(webTitle);
        }


        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //        webviewWebView.setWebViewClient(new WebViewClient() { //通过webView打开链接，不调用系统浏览器
        //
        //            @Override
        //            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //                // TODO Auto-generated method stub
        //                view.loadUrl(url);
        //                return true;
        //            }
        //
        //            @Override
        //            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        //                // Toast.makeText(getApplicationContext(), "网络连接失败 ,请连接网络。", Toast.LENGTH_SHORT).show();
        //            }
        //        });
        webviewWebView.getSettings().setJavaScriptEnabled(true);
        webviewWebView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webviewWebView.setWebViewClient(new WebViewClient() {

//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                url=request.getUrl().toString();
//                if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
//                    view.loadUrl(url);
//                    return false; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走
//
//                } else { //加载的url是自定义协议地址
//
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        finish();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return true;
//                }
//            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlStr) {
                mIsRedirect = true;
                WebView.HitTestResult hit = view.getHitTestResult();
                url = urlStr;
                try {
                    url = java.net.URLDecoder.decode(url , "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!TextUtil.isEmpty(url)) {
                    if(url.contains("https://s.click.taobao.com")){
                        super.shouldOverrideUrlLoading(view,url);
                        Log.d("WebView","Handle url with system~~");
                        return false;
                    }else {
                        //hit.getExtra()为null或者hit.getType() == 0都表示即将加载的URL会发生重定向，需要做拦截处理
                        if (TextUtils.isEmpty(hit.getExtra()) || hit.getType() == 0) {
                            //通过判断开头协议就可解决大部分重定向问题了，有另外的需求可以在此判断下操作
                            //                    Log.e("重定向", "重定向: " + hit.getType() + " && EXTRA（）" + hit.getExtra() + "------");
                            //                    Log.e("重定向", "GetURL: " + view.getUrl() + "\n" + "getOriginalUrl()" + view.getOriginalUrl());
                            //                    Log.d("重定向", "URL: " + url);
                        }

                        if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
                            view.loadUrl(url);
                            return false; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走

                        } else { //加载的url是自定义协议地址

                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }

                } else {
                    // Do your special things
                    return true;
                }


            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("WebView", "onPageStarted : " + url);
                if (!TextUtil.isEmpty(url)) {
                    super.onPageStarted(view,url,favicon);
                    Log.d("WebView","Handle url with system~~");
                    return;
                } else {
                    // Do your special things
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView","onPageFinished : " + url);
                if (!TextUtil.isEmpty(url)) {
                    super.onPageFinished(view,url);
                    Log.d("WebView","Handle url with system~~");
                    return;
                } else {
                    // Do your special things
                }
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webviewWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                // TODO: 2017-5-6 处理下载事件
                downloadByBrowser(url);
            }
        });
        //        WebSettings webSettings = webviewWebView.getSettings();
        //        webSettings.setJavaScriptEnabled(true);
        //        webSettings.setBuiltInZoomControls(false);


        if (null != urls) {
            webviewWebView.loadUrl(urls);
        }

        webviewWebView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (webviewWebView.canGoBack()) {
                    webviewWebView.goBack();
                } else {

                }
            }
        });


    }
    private String url="";
    private void downloadByBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    @Override
    public void initListener() {

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

    @Override
    public int getLayoutId() {
        return R.layout.webview;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
