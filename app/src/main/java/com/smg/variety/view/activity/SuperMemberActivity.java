package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SuperMemberActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.iv_img)
    ImageView      ivImg;
    @BindView(R.id.tv_next)
    TextView       tvNext;
     private int level;
    @Override
    public int getLayoutId() {
        return R.layout.activity_super_menber;
    }

    private             int    page_index = 0;
    public static final String PAGE_INDEX = "page_index";//调用者传递的名字

    @Override
    public void initView() {

        StatusBarUtils.StatusBarLightMode(this);
        level=getIntent().getIntExtra("level",0);
    }

    @Override
    public void initData() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(level==0){
           tvNext.setVisibility(View.VISIBLE);
        }else if(level==1){
            tvNext.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVipWelfare();
        getMembership();
    }

    @Override
    public void initListener() {
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state==-1){
                    Intent intent = new Intent(SuperMemberActivity.this, UploadSuperActivity.class);
                    intent.putExtra("level",level);
                    startActivity(intent);
                }else{
                    if(data==null){
                        return;
                    }
                    Intent intent = new Intent(SuperMemberActivity.this, LoadSuperActivity.class);
                    intent.putExtra("data",data);
                    startActivity(intent);

                }

            }
        });
    }
    private void getVipWelfare() {
        //showLoadDialog();
        DataManager.getInstance().getVipWelfare(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                   if(level==1){
                       GlideUtils.getInstances().loadNormalImg(SuperMemberActivity.this,ivImg,result.getData().level_2);
                   }else if(level==0){
                       GlideUtils.getInstances().loadNormalImg(SuperMemberActivity.this,ivImg,result.getData().level_1);

                   }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private int state=-1;
    private BannerInfoDto data;
    private void getMembership() {
        //showLoadDialog();
        DataManager.getInstance().getMembership(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                   if(TextUtil.isNotEmpty(result.getData().id)){
                       state=result.getData().status;
                       data=result.getData();
                   }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
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

