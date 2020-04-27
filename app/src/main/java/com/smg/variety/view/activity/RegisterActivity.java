package com.smg.variety.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.CaptchaImgDto;
import com.smg.variety.bean.GetSmsCode;
import com.smg.variety.bean.RegisterDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.request.UserRegister;
import com.smg.variety.type.SmsType;
import com.smg.variety.utils.CountDownTimerUtils;
import com.smg.variety.utils.ImagePickerUtils;
import com.smg.variety.utils.RegexUtils;
import com.smg.variety.utils.ShareUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_register_phone)
    EditText  mRegisterPhone;
    /**图片验证码*/
    @BindView(R.id.et_register_icon_code)
    EditText  mRegisterIconCode;
    /**短信验证码*/
    @BindView(R.id.et_register_note_code)
    EditText  mRegisterNoteCode;
    /**短信验证码*/
    @BindView(R.id.tv_register_note_code)
    TextView  sendCode;
    /**密码*/
    @BindView(R.id.et_register_password)
    EditText  mRegisterPassword;
    @BindView(R.id.et_tv_yqm)
    EditText  et_tv_yqm;

    @BindView(R.id.iv_register_icon_code)
    ImageView mRegisterIcon;
    @BindView(R.id.cb_register_check_box)
    CheckBox cb_register_check_box;
    /**图形验证码key*/
    private String mImageCodeKey;
    CountDownTimerUtils countDownTimerUtils;


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        mTitleText.setText("注册");
    }

    @Override
    public void initData() {
        getImageCode();
    }


    @Override
    public void initListener() {

    }

    /**
     * 加载图形验证码
     */
    private void getImageCode() {
        DataManager.getInstance().getImageCode(new DefaultSingleObserver<CaptchaImgDto>() {
            @Override
            public void onSuccess(CaptchaImgDto object) {
                mImageCodeKey = object.getKey();
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() imgKey= " + object.getKey());
                Bitmap bitmap = ImagePickerUtils.getInstances().base64ToBitmap(object.getImg());
                mRegisterIcon.setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                LogUtil.i(TAG, "RxLog-Thread: onError() = " + Long.toString(Thread.currentThread().getId()));
            }
        });
    }

    /**
     * 获取短信验证码
     */
    private void getNoteCode() {
        GetSmsCode getSms = new GetSmsCode();

        getSms.setPhone(mRegisterPhone.getText().toString().trim());
        getSms.setCaptcha_key(mImageCodeKey);
        getSms.setType(SmsType.REGISTER.getType());
        getSms.setCaptcha_code(mRegisterIconCode.getText().toString().trim());

        DataManager.getInstance().getSmsCode(new DefaultSingleObserver<String>() {
            @Override
            public void onSuccess(String object) {
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() = " + Long.toString(Thread.currentThread().getId()));

                countDownTimerUtils = new CountDownTimerUtils(RegisterActivity.this, sendCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                    //                    tvVerification.setText("获取验证码");
                    if (countDownTimerUtils != null) {
                        countDownTimerUtils.cancel();
                        countDownTimerUtils.onFinish();
                    }
                } else {
                    ToastUtil.showToast("已发送验证码");
                }
            }
        }, getSms);
    }

    /**
     * 注册验证
     */
    private boolean verifyConditionRegister() {
        if(TextUtils.isEmpty(mRegisterPhone.getText().toString().trim())){
            ToastUtil.showToast("请输入手机号");
            return false;
        }
        if(!TextUtils.isEmpty(et_tv_yqm.getText().toString().trim())){
            if(!RegexUtils.isMobileExact(et_tv_yqm.getText().toString().trim())){
                ToastUtil.showToast("请输入正确邀请码");
                return false;
            }
        }

        if(!RegexUtils.isMobileExact(mRegisterPhone.getText().toString().trim())){
            ToastUtil.showToast("请输入合法手机号");
            return false;
        }

        if(TextUtils.isEmpty(mRegisterIconCode.getText().toString().trim())){
            ToastUtil.showToast("请输入图形验证码");
            return false;
        }
        if(TextUtils.isEmpty(mRegisterNoteCode.getText().toString().trim())){
            ToastUtil.showToast("请输入短信验证码");
            return false;
        }
        if(TextUtils.isEmpty(mRegisterPassword.getText().toString().trim())||mRegisterPassword.getText().toString().trim().length()<6){
            ToastUtil.showToast("请输入不少于6位数密码");
            return false;
        }

        if(!cb_register_check_box.isChecked()){
            ToastUtil.showToast("请先勾选注册协议");
            return false;
        }
        return true;
    }
    /**
     * 短信验证码验证
     */
    private boolean verifyConditionCode() {
        if(TextUtils.isEmpty(mRegisterPhone.getText().toString().trim())){
            ToastUtil.showToast("请输入手机号");
            return false;
        }

        if(!RegexUtils.isMobileExact(mRegisterPhone.getText().toString().trim())){
            ToastUtil.showToast("请输入合法手机号");
            return false;
        }

        if(TextUtils.isEmpty(mRegisterIconCode.getText().toString().trim())){
            ToastUtil.showToast("请输入图形验证码");
            return false;
        }
        return true;
    }

    @OnClick({R.id.iv_title_back,
            R.id.iv_register_icon_code,
            R.id.tv_register_note_code,
            R.id.tv_register_protocol,
            R.id.tv_register
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_register_icon_code://刷新图形验证码
                getImageCode();
                break;
            case R.id.tv_register_note_code://获取短信验证码
                if(verifyConditionCode()){
                    getNoteCode();
                }
                break;
            case R.id.tv_register://注册
                if(verifyConditionRegister()){
                    register();
                }
                break;
            case R.id.tv_register_protocol://点击注册协议
                Intent intent1 = new Intent(RegisterActivity.this, WebUtilsActivity.class);
                intent1.putExtra("type",6);
                startActivity(intent1);
                break;
        }

    }

    private void register() {
        UserRegister userRegister = new UserRegister();
        userRegister.setPassword(mRegisterPassword.getText().toString());
        userRegister.setPhone(mRegisterPhone.getText().toString().trim());
        if(!TextUtils.isEmpty(et_tv_yqm.getText().toString().trim())){
            userRegister.setInvite_code("from_phone_"+et_tv_yqm.getText().toString().trim());
        }

        String codeStr = mRegisterNoteCode.getText().toString().trim();
        userRegister.setCode(codeStr);
        DataManager.getInstance().register(new DefaultSingleObserver<RegisterDto>() {
            @Override
            public void onSuccess(RegisterDto object) {
                LogUtil.i(TAG, "RxLog-Thread: onSuccess() Data= " + object.getPhone());
                ToastUtil.toast("注册成功");
                Intent intent = new Intent();
                intent.putExtra("phone",mRegisterPhone.getText().toString().trim());
                if(!TextUtils.isEmpty(et_tv_yqm.getText().toString().trim())){
                    intent.putExtra("phones",et_tv_yqm.getText().toString().trim());
                }

                setResult(RESULT_OK,intent);
                ShareUtil.getInstance().save(Constants.USER_ACCOUNT_NUMBER, mRegisterPhone.getText().toString().trim());
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                LogUtil.i(TAG, "RxLog-Thread: onError() = " + Long.toString(Thread.currentThread().getId()));
            }
        }, userRegister);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
        }
    }

}
