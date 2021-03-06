package com.smg.variety.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.Param;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.Compressor;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.db.bean.StoreInfo;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.widgets.ShopTypeWindows;
import com.smg.variety.view.widgets.dialog.ShopTypeWindow;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonRequireActivity extends BaseActivity {

    UploadFilesDto uploadFilesDto;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.civ_user_avatar)
    ImageView      ivImg44;

    @BindView(R.id.et_content1)
    EditText       etContent1;
    @BindView(R.id.et_content2)
    EditText       etContent2;
    @BindView(R.id.et_content3)
    EditText     etContent3;
    @BindView(R.id.iv_img11)
    ImageView    ivImg11;
    @BindView(R.id.iv_img22)
    ImageView    ivImg22;
    @BindView(R.id.iv_img33)
    ImageView    ivImg33;
    @BindView(R.id.cb_register_check_box)
    CheckBox     cbRegisterCheckBox;
    @BindView(R.id.tv_submit)
    TextView     tvSubmit;
    @BindView(R.id.tv_type)
    TextView     tvType;
    @BindView(R.id.line)
    View         line;
    @BindView(R.id.ll_choose)
    LinearLayout ll_choose;
    @Override
    public int getLayoutId() {
        return R.layout.activity_person_reuqair;
    }

    @Override
    public void initView() {
        tvTitleText.setText("个人申请");
    }
    private ShopTypeWindows shoptypeWindow;
    private String ids;
    @Override
    public void initData() {
        getTags();
        shoptypeWindow = new ShopTypeWindows(this);
        shoptypeWindow.setSureListener(new ShopTypeWindows.ClickListener() {
            @Override
            public void clickListener(String id, String name) {
                ids=id;
                tvType.setText(name);


            }
        });
    }
    private List<AreaDto>  dto =null;//标签数据
    public void getTags(){
        DataManager.getInstance().getInducts(new DefaultSingleObserver<Param>() {
            @Override
            public void onSuccess(Param result) {
                dissLoadDialog();

                if(result!=null){
                    dto=result.data;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        });

    }
    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoptypeWindow.selectData(dto, "选择行业分类" );
                closeKeyBoard();
                shoptypeWindow.showAsDropDown(line);
            }
        });

    }

    @OnClick({

            R.id.iv_img11,
            R.id.iv_img22,
            R.id.iv_img33,
            R.id.rl_logo,

            R.id.tv_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_img11:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(1);
                break;
            case R.id.iv_img22:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(2);
                break;
            case R.id.iv_img33:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(3);
                break;
            case R.id.rl_logo:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(4);
                break;

            case R.id.tv_submit:
                upSellers();
                break;
        }

    }

    private int code;

    public void selectPictur(int poition) {
        if (poition == 1) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG1;
        } else if (poition == 2) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG2;
        } else if (poition == 3) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG3;
        } else if (poition == 4) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG4;
        }
        closeKeyBoard();
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
                                    .start(PersonRequireActivity.this, code);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            String imageUrl = "";
            if (resultList != null && resultList.size() > 0) {
                imageUrl = resultList.get(0).getImagePath();

            }
            if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1) {
                uploadImg(imageUrl, 1);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG2) {
                uploadImg(imageUrl, 2);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG3) {
                uploadImg(imageUrl, 3);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG4) {
                uploadImg(imageUrl, 4);
            }

        }
    }

    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;

    private void uploadImg(String imgPath, final int poition) {
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
            ToastUtil.showToast("上传失败");
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
                    if (poition == 1) {
                        img1 =object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg11, img1);

                    } else if (poition == 2) {
                        img2 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg22, img2);
                    } else if (poition == 3) {
                        img3 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg33, img3);
                    } else if (poition == 4) {
                        img4 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg44, img4);
                    }

                } else {
                    if (poition == 1) {
                        img1 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg11, R.drawable.icon_cer_1);

                    } else if (poition == 2) {
                        img2 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg22, R.drawable.icon_cer_2);
                    } else if (poition == 3) {
                        img3 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(PersonRequireActivity.this, ivImg33, R.drawable.icon_cer_3);
                    }
                    ToastUtil.showToast("上传图片失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                //                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "seller", part);
    }

    private List<String> imgs = new ArrayList<>();
    private List<String> imgs1 = new ArrayList<>();
    private String       content;
    private String       contents;

    public void upSellers() {

        if (TextUtils.isEmpty(etContent1.getText().toString().trim())) {
            ToastUtil.showToast("请输入店铺名称");
            return;
        }
        if (TextUtils.isEmpty(etContent2.getText().toString().trim())) {
            ToastUtil.showToast("请输入联系人姓名");
            return;
        }
        if (TextUtils.isEmpty(etContent3.getText().toString().trim())) {
            ToastUtil.showToast("请输入联系人电话");
            return;
        }
        if (TextUtils.isEmpty(ids)) {
            ToastUtil.showToast("请输入选择行业分类");
            return;
        }
        if (TextUtils.isEmpty(img1)) {
            ToastUtil.showToast("请上传法人身份证正面照");
            return;
        }
        if (TextUtils.isEmpty(img2)) {
            ToastUtil.showToast("请上传法人身份证背面照");
            return;
        }
        if (TextUtils.isEmpty(img3)) {
            ToastUtil.showToast("请上传品牌授权资质");
            return;
        }

        if (TextUtils.isEmpty(img4)) {
            ToastUtil.showToast("请上传logo");
            return;
        }

        if (!cbRegisterCheckBox.isChecked()) {
            ToastUtil.showToast("请同意相关条款");
            return;
        }
        showLoadDialog();
        content = "";
        imgs1.clear();
        imgs.clear();
        imgs1.add(img3);
        imgs.add(img1);
        imgs.add(img2);
        Gson g = new Gson();
        content = g.toJson(imgs);
        contents = g.toJson(imgs1);
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "personal");
        map.put("shop_name", etContent1.getText().toString().trim());
        map.put("name", etContent2.getText().toString().trim());
        map.put("phone", etContent3.getText().toString().trim());
        map.put("industry", ids);
        map.put("logo", img4);
        map.put("id_cards[0]", img1);
        map.put("id_cards[1]", img2);
        map.put("credentials", contents);
        StoreInfo info=new StoreInfo();
        info.type="personal";
        info.shop_name=etContent1.getText().toString().trim();
        info.name=etContent2.getText().toString().trim();
        info.phone=etContent3.getText().toString().trim();
        info.industry=ids;
        info.logo=img4;
        info.id_cards=imgs;
        info.credentials=imgs1;
        DataManager.getInstance().upSellers(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                ToastUtil.showToast("提交成功，请等待审核");
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("提交成功，请等待审核");
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, info);
    }


}
