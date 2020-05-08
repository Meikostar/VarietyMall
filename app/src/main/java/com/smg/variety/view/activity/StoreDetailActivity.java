package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreDetailActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.rl_gr)
    RelativeLayout rlGr;
    @BindView(R.id.rl_qy)
    RelativeLayout rlQy;

    @Override
    public void initListener() {
        rlGr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(PersonRequireActivity.class);
            }
        });
        rlQy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(EnterpireRequireActivity.class);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_sq;
    }

    @Override
    public void initView() {

        mTitleText.setText("店铺申请");

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.iv_title_back

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
