package com.smg.variety.view.activity;

import android.view.View;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by winder on 2019/7/14.
 * 主播审核失败
 */

public class ShopCheckFailActivity extends BaseActivity {
    @BindView(R.id.tv_fail_reason)
    TextView tvFailReason;
    @Override
    public int getLayoutId() {
        return R.layout.ui_shop_check_fail;
    }

    @Override
    public void initView() {

       tvFailReason.setText("审核失败");
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
                gotoActivity(StoreDetailActivity.class);
                finish();
                break;
        }
    }

}
