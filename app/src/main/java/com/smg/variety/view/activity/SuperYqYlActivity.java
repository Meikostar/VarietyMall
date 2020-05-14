package com.smg.variety.view.activity;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.NewsDetailDto;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.utils.TextUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SuperYqYlActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.tv_yq)
    TextView       tvYq;
    @BindView(R.id.tv_sm)
    TextView       tvSm;
    @BindView(R.id.ll_fx)
    LinearLayout   llFx;
    @BindView(R.id.ll_lj)
    LinearLayout   llLj;
    @BindView(R.id.ll_jl)
    LinearLayout   llJl;
    @BindView(R.id.tv_sp)
    TextView       tvSp;
    @BindView(R.id.tv_ds)
    TextView       tvDs;
    @BindView(R.id.tv_gz)
    TextView       tv_gz;
    @BindView(R.id.tv_time)
    TextView       tv_time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_super_yqyl;
    }

    private             int    page_index = 0;
    public static final String PAGE_INDEX = "page_index";//调用者传递的名字

    @Override
    public void initView() {

        StatusBarUtils.StatusBarLightMode(this);

    }

    @Override
    public void initData() {
        getConfigs();
        getNewsDetail();
    }

    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isEmpty(content)){
                    return;
                }
                DialogUtils.showYqOne(SuperYqYlActivity.this, content, new DialogUtils.OnClickDialogListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
        tvYq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isEmpty(one)||TextUtil.isEmpty(content)){
                    return;
                }
                DialogUtils.showYqTwo(SuperYqYlActivity.this, content, one,two,new DialogUtils.OnClickDialogListener() {
                    @Override
                    public void onClick(View v) {
                      gotoActivity(MyQRcodeActivity.class);
                    }
                });
            }
        });
        tvSm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isEmpty(one)){
                    return;
                }
                DialogUtils.showYqThree(SuperYqYlActivity.this, two,new DialogUtils.OnClickDialogListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }
    private String content;
    private void getNewsDetail() {
        DataManager.getInstance().getNewsDetail(new DefaultSingleObserver<HttpResult<NewsDetailDto>>() {
            @Override
            public void onSuccess(HttpResult<NewsDetailDto> httpResult) {
                super.onSuccess(httpResult);
                content=httpResult.getData().content;
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, "page", "invite_rules");
    }

    private String one;
    private String two;
    private String three;

    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getConfigs(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().rights_pull_new != null) {
                        one = result.getData().rights_pull_new.level_1;
                        two = result.getData().rights_pull_new.level_2;
                        three = result.getData().rights_pull_new.new_pull_end_at;
                        tvSp.setText(one);
                        tvDs.setText(two);
                        tv_time.setText(three);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }


}

