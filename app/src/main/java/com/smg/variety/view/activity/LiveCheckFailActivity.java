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

public class LiveCheckFailActivity extends BaseActivity {
    @BindView(R.id.tv_fail_reason)
    TextView tvFailReason;
    @Override
    public int getLayoutId() {
        return R.layout.ui_live_check_fail;
    }

    @Override
    public void initView() {
       String reason = getIntent().getExtras().getString("reasonTip");
       tvFailReason.setText(reason);
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
                applyLives();
                break;
        }
    }
    public void applyLives() {
        //        showLoadDialog();
        DataManager.getInstance().applyLive(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                //                dissLoadDialog();
                gotoActivity(LiveCheckingActivity.class);
                                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    gotoActivity(LiveCheckingActivity.class);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                //                dissLoadDialog();
            }
        });
    }
}
