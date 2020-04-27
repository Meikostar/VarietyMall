package com.smg.variety.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.Compressor;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.type.SexType;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.XgPwdActivity;
import com.smg.variety.view.widgets.CircleImageView;
import com.smg.variety.view.widgets.dialog.PopDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView        mTitleText;
    @BindView(R.id.tv_sex)
    TextView        mSex;
    @BindView(R.id.tv_user_phone)
    TextView        mUserPhone;
    @BindView(R.id.tv_user_name)
    TextView        mUserName;
    @BindView(R.id.civ_user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.iv_title_back)
    ImageView       ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView        tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout  layoutTop;
    @BindView(R.id.rl_user_avatar)
    RelativeLayout  rlUserAvatar;
    @BindView(R.id.tv_wx)
    TextView        tvWx;
    @BindView(R.id.rl_wx)
    RelativeLayout  rlWx;
    @BindView(R.id.rl_user_name_container)
    RelativeLayout  rlUserNameContainer;
    @BindView(R.id.rl_xgmm)
    RelativeLayout  rlXgmm;
    @BindView(R.id.rl_sex_container)
    RelativeLayout  rlSexContainer;
    private PopDialog       mSexDialog;
    private PersonalInfoDto mPersonalInfoDto;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {

        mTitleText.setText("个人资料");

    }

    @Override
    public void initData() {
        setUserInfo();
    }

    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {

                mPersonalInfoDto = personalInfoDto;
                ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
                if (!TextUtils.isEmpty(personalInfoDto.getName())) {
                    ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getName());
                } else {
                    ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());
                }
                setUserInfo();
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo() {
        if (mPersonalInfoDto != null) {
            //用户名
            mUserName.setText(mPersonalInfoDto.getName());
            //电话
            mUserPhone.setText(mPersonalInfoDto.getPhone());
            if(TextUtil.isNotEmpty(mPersonalInfoDto.wechat_number)){
                tvWx.setText(mPersonalInfoDto.wechat_number);
            }

            //性别
            if (mPersonalInfoDto.getSex() == null) {
                mSex.setVisibility(View.GONE);
            } else {
                mSex.setText("1".equals(mPersonalInfoDto.getSex()) ? "男" : "女");
            }
            GlideUtils.getInstances().loadRoundImg(this, mUserAvatar, Constants.WEB_IMG_URL_UPLOADS + mPersonalInfoDto.getAvatar());
        }
    }

    @OnClick({R.id.iv_title_back
            , R.id.rl_sex_container
            , R.id.rl_user_name_container
            , R.id.rl_xgmm
            , R.id.rl_wx
            , R.id.rl_user_avatar
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.rl_sex_container:
                showSexDialog();
                break;
            case R.id.rl_user_name_container:
                gotoModifyNicknameActivity();
                break;
            case R.id.rl_wx:
                Intent intent = new Intent(this, ModifyNicknameActivity.class);
                intent.putExtra("type",1);
                startActivityForResult(intent, 100);
                break;
            case R.id.rl_xgmm:
                gotoActivity(XgPwdActivity.class);
                break;
            case R.id.rl_user_avatar:
                avatar();
                break;
        }

    }

    @Override
    protected void onResume() {
        getUserInfo();
        super.onResume();
    }

    private void gotoModifyNicknameActivity() {
        Intent intent = new Intent(this, ModifyNicknameActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 0 && data != null) {
            PersonalInfoDto personalInfoDto = (PersonalInfoDto) data.getSerializableExtra("userInfo");
            if (personalInfoDto != null) {
                mPersonalInfoDto = personalInfoDto;
                setUserInfo();
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                String imageUrl = resultList.get(0).getImagePath();
                uploadImg(imageUrl);
            }
        }
    }

    private void uploadImg(String imgPath) {
        File file = new File(imgPath);
        File compressedImage = null;
        if (file.exists()) {
            //压缩文件
            try {
                compressedImage = new Compressor(BaseApplication.getInstance()).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (compressedImage == null) {
                compressedImage = file;
            }
        } else {
            ToastUtil.showToast("上传头像失败");
            return;
        }
        showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                if (object != null) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("file_id", object.getId() + "");
                    updateUserHead(map, imgPath);
                } else {
                    dissLoadDialog();
                    ToastUtil.showToast("上传头像失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                //                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "avatar", part);
    }

    public void updateUserHead(HashMap<String, String> map, String imgPath) {
        DataManager.getInstance().modifUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                if (personalInfoDto != null) {
                    mPersonalInfoDto = personalInfoDto;
                }
                dissLoadDialog();
                ToastUtil.showToast("上传头像成功");
                GlideUtils.getInstances().loadRoundImg(UserInfoActivity.this, mUserAvatar, imgPath);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, map);
    }


    private void showSexDialog() {
        mSexDialog = new PopDialog(this, R.layout.layout_sex);
        mSexDialog.show();
        mSexDialog.findViewById(R.id.man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitSex(SexType.NAN.getType());
            }
        });
        mSexDialog.findViewById(R.id.woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitSex(SexType.NV.getType());
            }
        });
        mSexDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSexDialog.dismiss();
            }
        });
    }

    private void commitSex(int select) {
        HashMap<String, String> map = new HashMap<>();
        map.put("sex", "" + select);
        showLoadDialog();
        DataManager.getInstance().modifUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                mSexDialog.dismiss();
                if (personalInfoDto != null) {
                    mPersonalInfoDto = personalInfoDto;
                }
                dissLoadDialog();
                if (personalInfoDto != null) {
                    mSex.setText("1".equals(personalInfoDto.getSex()) ? "男" : "女");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                mSexDialog.dismiss();
                dissLoadDialog();
                ToastUtil.showToast(throwable.getMessage());
            }
        }, map);
    }

    @SuppressLint("CheckResult")
    public void avatar() {
        new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted && Manifest.permission.CAMERA.equals(permission.name)) {
                            // 用户已经同意该权限
                            new ImagePicker()
                                    .pickType(ImagePickType.SINGLE)//设置选取类型(拍照、单选、多选)
                                    .maxNum(1)//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                    .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                    .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                                    .doCrop(1, 1, 300, 300)
                                    .start(UserInfoActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
