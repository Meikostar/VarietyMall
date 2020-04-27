package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改昵称
 */
public class ModifyNicknameActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mRightText;
    @BindView(R.id.et_user_name)
    EditText mUserName;
    PersonalInfoDto mPersonalInfoDto;
    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_nickname;
    }
    private int type;
    @Override
    public void initView() {
        type=getIntent().getIntExtra("type",0);
        if(type==0){
            mTitleText.setText("修改昵称");
        }else {
            mTitleText.setText("编辑微信号");

        }

        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("完成");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({  R.id.iv_title_back
            , R.id.tv_title_right
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finishActivity();
                break;
            case R.id.tv_title_right:
                commitUserName();
                break;
        }

    }

    private void commitUserName() {
        String name = mUserName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            if(type==1){
                ToastUtil.showToast("请输入昵称");
            }else {
                ToastUtil.showToast("请输入微信号");

            }


            return;
        }

        HashMap<String, String> map = new HashMap<>();
        if(type==1){
            map.put("wechat_number", "" + name);
        }else {
            map.put("name", "" + name);

        }

        showLoadDialog();
        DataManager.getInstance().modifUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                dissLoadDialog();
                if (personalInfoDto != null) {
                    mPersonalInfoDto = personalInfoDto;
                }
                ToastUtil.showToast("修改成功");
                finish();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(throwable.getMessage());
            }
        }, map);
    }

    private void finishActivity() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("userInfo", mPersonalInfoDto);
        intent.putExtras(bundle);
        setResult(0, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }
}
