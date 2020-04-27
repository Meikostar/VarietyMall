package com.smg.variety.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.LogoutEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.activity.LoginActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;

public class XgPwdActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.et_pwd1)
    EditText       etPwd1;
    @BindView(R.id.tv_register_password)
    TextView       tvRegisterPassword;
    @BindView(R.id.et_register_password)
    EditText       etRegisterPassword;
    @BindView(R.id.tv_sure)
    TextView       tvSure;
    @BindView(R.id.cb_register_check_box)
    CheckBox       cbRegisterCheckBox;
    @BindView(R.id.tv_register_protocol)
    TextView       tvRegisterProtocol;



    @Override
    public int getLayoutId() {
        return R.layout.activity_xg_pwd;
    }

    @Override
    public void initView() {
        mTitleText.setText("修改密码");
    }

    @Override
    public void initData() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etPwd1.getText().toString().trim())){
                    ToastUtil.showToast("请输入不少于6位数密码");
                    return ;
                }

                if(TextUtils.isEmpty(etRegisterPassword.getText().toString().trim())||etRegisterPassword.getText().toString().trim().length()<6){
                    ToastUtil.showToast("请输入不少于6位数密码");
                    return ;
                }
                commitSex(etPwd1.getText().toString().trim(),etRegisterPassword.getText().toString().trim());
            }
        });
    }


    @Override
    public void initListener() {

    }









    private void commitSex(String old_password,String password) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("password", "" + password);
        map.put("old_password", "" + old_password);
        showLoadDialog();
        DataManager.getInstance().modifUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                ToastUtil.showToast("修改成功");
                ShareUtil.getInstance().cleanUserInfo();
                EventBus.getDefault().post(new LogoutEvent());
                Bundle bundle = new Bundle();
                bundle.putString("log_out", "LOG_OUT");
                gotoActivity(LoginActivity.class, true, bundle);
                dissLoadDialog();

            }
            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                dissLoadDialog();

            }
        }, map);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
