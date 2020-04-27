package com.smg.variety.view.activity;

import android.view.View;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by winder on 2019/7/14.
 * 主播审核中提示
 */

public class LiveCheckingActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.ui_live_checking;
    }

    @Override
    public void initView() {
       actionbar.setTitle("我要直播");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
    @OnClick({R.id.btn_sure})
    public void toOnclick(View view){
        switch (view.getId()){
            case R.id.btn_sure:
                finish();
                break;
        }
    }
}
