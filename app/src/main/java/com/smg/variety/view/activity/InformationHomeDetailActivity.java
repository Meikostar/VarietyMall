package com.smg.variety.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.NewsDetailDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.common.utils.WebViewUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.widgets.autoview.NoScrollWebView;

import java.util.Objects;

import butterknife.BindView;

/**
 * 資訊詳情
 */
public class InformationHomeDetailActivity extends BaseActivity {

    @BindView(R.id.iv_item_head_line_icon)
    ImageView       ivItemHeadLineIcon;
    @BindView(R.id.tv_item_head_line_title)
    TextView        tvItemHeadLineTitle;
    @BindView(R.id.tv_item_head_line_time)
    TextView        tvItemHeadLineTime;
    @BindView(R.id.tv_look_num)
    TextView        tvLookNum;
    //    @BindView(R.id.content_tv)
//    TextView contentTv;
    @BindView(R.id.webView_content)
    NoScrollWebView webView_content;

    @Override
    public int getLayoutId() {
        return R.layout.activity_information_home_detail;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        actionbar.setTitle("详情");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            actionbar.setTitle(intent.getStringExtra("title"));
            String article_id = intent.getStringExtra("article_id");
            String type = intent.getStringExtra("type");
            if (TextUtil.isEmpty(article_id)) {
//                ToastUtil.showToast(getString(R.string.fail));
                SystemClock.sleep(500);
                finish();
            } else {
                getNewsDetail(type, article_id);
            }
        }
    }

    @Override
    public void initListener() {

    }

    private void setData(HttpResult<NewsDetailDto> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }
        NewsDetailDto item = httpResult.getData();
        tvItemHeadLineTitle.setText(item.getTitle());
        tvItemHeadLineTime.setText(item.getCreated_at());
//        tvLookNum.setText(String.format("%s%s", item.getClick(), getString(R.string.browse)));
//        if (item.getImg() != null) {
//            GlideUtils.getInstances().loadNormalImg(this, ivItemHeadLineIcon, Constants.WEB_IMG_URL_UPLOADS + item.getImg());
//            ivItemHeadLineIcon.setVisibility(View.VISIBLE);
//        }
//        contentTv.setText(item.getContent());
        WebViewUtil.setWebView(webView_content, BaseApplication.getInstance());
        //java回调js代码，不要忘了@JavascriptInterface这个注解，不然点击事件不起作用
        webView_content.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void saveImg(String url) {
                filePath = url;

            }
        }, "jsCallJavaObj");

        webView_content.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setWebImageClick(view);
            }
        });
        WebViewUtil.setWebView(webView_content, Objects.requireNonNull(this));
//        webView_content.setBackgroundColor(0); // 设置背景色
//        webView_content.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

        WebViewUtil.loadHtml(webView_content, item.getContent());
//        webView_content.setVisibility(View.VISIBLE); // 加载完之后进行设置显示，以免加载时初始化效果不好看
    }
    private interface JsCallJavaObj {
        void saveImg(String url);
    }


    private void donwloadImg() {



    }



    private static Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //ToastUtil.showToast(mSaveMessage);
        }
    };
    private        String  filePath;
    private        Uri     uri;
    private        Bitmap  mBitmap;

    /**
     * 设置网页中图片点击事件
     */
    private void setWebImageClick(WebView view) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].onclick=function(){" +
                "window.jsCallJavaObj.saveImg(this.src);" +
                "}}})()";
        webView_content.loadUrl(jsCode);
    }
    /**
     * 文章详情
     *
     * @param type 企業榮耀:information_glory,案例分享:case_share,水蛭素:hirudin,企業簡介:informationinline 英文后面加_en(如information_glory_en)
     * @param id
     */
    private void getNewsDetail(String type, String id) {


        DataManager.getInstance().getNewsDetail(new DefaultSingleObserver<HttpResult<NewsDetailDto>>() {
            @Override
            public void onSuccess(HttpResult<NewsDetailDto> httpResult) {
                super.onSuccess(httpResult);
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(throwable.getMessage());
            }
        }, type, id);
    }

}
