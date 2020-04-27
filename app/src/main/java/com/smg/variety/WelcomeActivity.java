package com.smg.variety;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.LoginDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.request.UserRegister;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.adapter.SplashAdapter;
import com.smg.variety.view.widgets.RegisterArgeeDialog;
import com.tencent.smtt.sdk.TbsDownloader;

import butterknife.BindView;
import butterknife.OnClick;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.time)
    TextView  time;
    @BindView(R.id.btn)
    View      btn;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private RegisterArgeeDialog dialog;
    boolean isFirstRun = false;
    long    delay      = 2000;//lunbo 时间间隔
    @BindView(R.id.home_welcome)
    WebView webView;
    private CountDownTimer timer   = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long l) {
            time.setText("跳过 " + l / 1000 + "s");
        }

        @Override
        public void onFinish() {
            int anInt = ShareUtil.getInstance().getInt(Constants.IS_PASS, -1);

            if(state!=0){
                return;
            }
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    };
    private Handler        handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int postion = msg.what;
            postion++;
            if (postion <= 2) {
                viewpager.setCurrentItem(postion);
                handler.sendEmptyMessageDelayed(postion, delay);
            }
        }
    };


    @Override
    public void initListener() {

    }

    private void login(String username, String password) {
        UserRegister userRegister = new UserRegister();
        userRegister.setPhone(username);
        userRegister.setPassword(password);
        userRegister.setInclude("user.userExt");

        DataManager.getInstance().login(new DefaultSingleObserver<LoginDto>() {
            @Override
            public void onSuccess(LoginDto loginDto) {
                if (loginDto.getUser().getData().userExt != null && loginDto.getUser().getData().userExt.data != null) {
                    ShareUtil.getInstance().saveInt(Constants.IS_PASS, loginDto.getUser().getData().userExt.data.status);


                } else {
                    ShareUtil.getInstance().saveInt(Constants.IS_PASS, -1);
                }
                gotoActivity(LoginActivity.class, true);
            }

            @Override
            public void onError(Throwable throwable) {

                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, userRegister);
    }

    private void firstRun() {
        //        PreferenceManager.save("isFirstRun", false);

        // Toast.makeText(this,"第一次",Toast.LENGTH_LONG).show();

        viewpager.setVisibility(View.VISIBLE);
        viewpager.setAdapter(new SplashAdapter(this));
        btn.setVisibility(View.GONE);
        handler.sendEmptyMessageDelayed(0, delay);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

            }
        });
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
            /*viewpager.setOnLastPageSelectedListener(new MyRollPagerView.OnLastPageSelectedListener() {
                @Override
                public void onLastPageSelected(boolean isLashPage) {
                    if (isLashPage) {
                        btn.setVisibility(View.VISIBLE);
                    } else {
                        btn.setVisibility(View.GONE);
                    }
                }
            });*/

        //viewpager.setHintView(new ColorPointHintView(this, Color.parseColor("#00000000"), Color.parseColor("#00000000")));
    }

    @OnClick(R.id.time)
    public void onViewClicked() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    private MediaController mc;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    private Context mContext;
    private int state;
    @Override
    public void initView() {
        isFirstRun = ShareUtil.getInstance().getBoolean("isFirstRun");
        TbsDownloader.needDownload(this, false);
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                String name = uri.getQueryParameter("name");
                String age = uri.getQueryParameter("age");
            }

        }

        if(!isFirstRun){
            firstRun();

            state=1;
            dialog = new RegisterArgeeDialog(WelcomeActivity.this, new RegisterArgeeDialog.OnRigisterClickListener() {
                @Override
                public void onRisterClick(int type) {
                    if (type == 1||type==3) {
                        state=2;
                        dialog.dismiss();
                        if(type==1){
                            ShareUtil.getInstance().saveBoolean("isFirstRun", true);
                        }else {
                            finish();
                        }

                    }
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogs) {
                    if(state!=2){
                        dialog.show();
                    }
                }
            });
            dialog.show();

        }else {
            btn.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.setVerticalScrollBarEnabled(false); //垂直滚动条不显示
            webView.setHorizontalScrollBarEnabled(false);//水平不显示
            WebSettings webSettings = webView.getSettings();
            webSettings.setDisplayZoomControls(false);//隐藏webview缩放按钮
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);//屏幕适配:设置webview推荐使用的窗口，设置为true
            webSettings.setLoadWithOverviewMode(true);//设置webview加载的页面的模式，也设置为true
            webSettings.setAllowFileAccess(true);
            webSettings.setSupportZoom(true);//是否支持缩放
            webSettings.setBuiltInZoomControls(true);//添加对js功能的支持
            webView.setWebViewClient(new WebViewClient());
            webView.setBackgroundColor(0);
            String gifPath = "file:///android_asset/ic_splash.png";
            webView.loadUrl(gifPath);

            time.setVisibility(View.GONE);
            timer.start();
        }
             /*       new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);*/

    }

    @Override
    public void initData() {

    }
}
