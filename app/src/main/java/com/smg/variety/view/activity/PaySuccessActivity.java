package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.eventbus.FinishEvent;
import com.smg.variety.view.MainActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.img_qrcode)
    ImageView      imgQrcode;
    @BindView(R.id.tv_state)
    TextView       tvState;
    @BindView(R.id.btn_save_qcode)
    TextView       btnSaveQcode;
    @BindView(R.id.ll_look)
    LinearLayout   llLook;
    @BindView(R.id.ll_back)
    LinearLayout   llBack;
    @BindView(R.id.tv_back)
    TextView       tv_back;

    @Override
    public void initListener() {

    }
    private int live;
    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_state;
    }

    @Override
    public void initView() {
        tvTitleText.setText("支付成功");
    }

    @Override
    public void initData() {

        live=getIntent().getIntExtra("live",0);
        if(live!=0){
            tv_back.setText("返回直播间");
        }else {
            tv_back.setText("返回首页");

        }
    }






    @OnClick({R.id.iv_title_back, R.id.ll_look, R.id.ll_back})
    public void toClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            //            case R.id.btn_friend:
            //                gotoActivity(InviteFriendsActivity.class);
            //                break;
            case R.id.ll_look:
                Intent intent = new Intent(PaySuccessActivity.this, OrderActivity.class);
                intent.putExtra("page", 2);
                startActivity(intent);
                break;
            case R.id.ll_back:
                if(live!=0){
                    EventBus.getDefault().post(new FinishEvent());
                    finish();
                }else {
                    finishAll();
                    Bundle bundle = new Bundle();
                    bundle.putInt(MainActivity.PAGE_INDEX, 0);
                    gotoActivity(MainActivity.class, true, bundle);
                }

                break;

        }

    }



}
