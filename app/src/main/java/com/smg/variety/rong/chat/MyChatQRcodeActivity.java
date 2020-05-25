package com.smg.variety.rong.chat;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.widgets.CircleImageView;

import butterknife.BindView;

public class MyChatQRcodeActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.img_qrcode)
    ImageView img_qrcode;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civ_user_avatar;
    @BindView(R.id.tv_user_phone)
    TextView tv_user_phone;

    Bitmap bitmap;
    private PersonalInfoDto mPersonalInfoDto;
    private String shareUrl;

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat_qr_code;
    }

    @Override
    public void initView() {
        tvTitleText.setText("我的二维码");
        shareUrl = Constants.BASE_URL+"register?invite_code="+ "from_id_"+ ShareUtil.getInstance().getString(Constants.USER_ID,"");
        GlideUtils.getInstances().loadNormalImg(this,img_qrcode, Constants.BASE_URL+"api/package/user/invitation_img?user_id="+ ShareUtil.getInstance().getString(Constants.USER_ID,""));
    }

    @Override
    public void initData() {
        getUserInfo();
    }

    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                mPersonalInfoDto = personalInfoDto;
                setUserInfo();
            }
            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void setUserInfo() {
        if (mPersonalInfoDto == null) {
            return;
        }
        //用户名
        tv_user_name.setText(TextUtils.isEmpty(mPersonalInfoDto.getName()) ? "" : mPersonalInfoDto.getName());
        //电话号码
        tv_user_phone.setText(mPersonalInfoDto.getPhone());
        GlideUtils.getInstances().loadRoundCornerImg(MyChatQRcodeActivity.this, civ_user_avatar, 2.5f,Constants.WEB_IMG_URL_UPLOADS + mPersonalInfoDto.getAvatar());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
