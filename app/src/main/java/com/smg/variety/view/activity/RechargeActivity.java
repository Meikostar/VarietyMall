package com.smg.variety.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {
    public static final int CHECK_BANK_CARD = 101;

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_recharge_money)
    EditText etRechargeMoney;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        mTitleText.setText("充值");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void moneyRecharges() {
        if(TextUtils.isEmpty(etRechargeMoney.getText().toString().trim())){
            ToastUtil.showToast("请输入充值金额");
            return;
        }
        Map map = new HashMap();
        map.put("total",etRechargeMoney.getText().toString().trim());
        DataManager.getInstance().moneyRecharges(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                Bundle bundle= new Bundle();
                bundle.putString(Constants.INTENT_WEB_URL,result.getData());
                bundle.putString(Constants.INTENT_WEB_TITLE,"充值");
                gotoActivity(RechargeWebActivity.class,false,bundle);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back
            , R.id.tv_recharge_affirm
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_recharge_affirm://确认充值
                moneyRecharges();
                break;
        }
    }


}
