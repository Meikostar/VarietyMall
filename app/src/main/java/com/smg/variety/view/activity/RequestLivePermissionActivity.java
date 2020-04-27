package com.smg.variety.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.smg.variety.common.utils.StringUtil;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.Compressor;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestLivePermissionActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_id_card)
    EditText et_id_card;
    @BindView(R.id.iv_id_card)
    ImageView iv_id_card;
    UploadFilesDto uploadFilesDto;
    private int state;
    @Override
    public int getLayoutId() {
        return R.layout.activity_request_live_permission;
    }

    @Override
    public void initView() {
        state=getIntent().getIntExtra("state",0);
        if(state==1){
            mTitleText.setText("实名认证");
        }else {
            mTitleText.setText("实名认证");
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back,
            R.id.tv_submit,
            R.id.iv_id_card
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_id_card:
//                ToastUtil.showToast("上传身份证正面照");
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
//                                             .doCrop(1, 1, 300, 300)
                                            .start(RequestLivePermissionActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            }
                        });
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    ToastUtil.showToast("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(et_id_card.getText().toString().trim())) {
                    ToastUtil.showToast("请输入身份证");
                    return;
                }
                try {
                    if (!StringUtil.IDCardValidate(et_id_card.getText().toString().trim())) {
                        ToastUtil.showToast("请输入正确的身份证");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (uploadFilesDto == null) {
                    ToastUtil.showToast("请上传身份证");
                    return;
                }
                HashMap<String,String> map = new HashMap<>();
                map.put("real_name",et_name.getText().toString().trim());
                map.put("no",et_id_card.getText().toString().trim());
                map.put("imgs[0]",uploadFilesDto.getId()+"");
                map.put("status",0+"");

                    applyLive(map);


                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                String imageUrl = resultList.get(0).getImagePath();
                uploadImg(imageUrl);
            }
        }
    }

    private void uploadImg(String imgPath) {
        uploadFilesDto = null;
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
            ToastUtil.showToast("上传身份证失败");
            return;
        }
        showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                dissLoadDialog();
                if (object != null) {
                    uploadFilesDto = object;
                    GlideUtils.getInstances().loadNormalImg(RequestLivePermissionActivity.this, iv_id_card, Constants.BASE_URL+object.getUrl());
                } else {
                    ToastUtil.showToast("上传身份证失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
//                ToastUtil.toast(ApiException.getInstance().getErrorMsg());

                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "image", part);
    }
    public void applyLive(HashMap<String, String> map) {
        showLoadDialog();
        DataManager.getInstance().applyrealName(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                ToastUtil.showToast("实名认证已提交，请耐心等待审核!");
                finish();
//                gotoActivity(LiveCheckingActivity.class,true);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("实名认证已提交，请耐心等待审核!");
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);
    }


}
