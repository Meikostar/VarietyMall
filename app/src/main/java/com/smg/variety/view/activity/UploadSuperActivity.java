package com.smg.variety.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.LiveVideoInfo;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.Compressor;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.qiniu.AVStreamingActivity;
import com.smg.variety.qiniu.StreamingBaseActivity;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.UploadPicAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 掌柜
 */
public class UploadSuperActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.recy_view)
    RecyclerView   recyPic;
    @BindView(R.id.btn_open_live)
    TextView       btnOpenLive;
    @BindView(R.id.tv_content)
    TextView       tvContent;
    @BindView(R.id.tv_fz)
    TextView       tvFz;
    @Override
    public int getLayoutId() {
        return R.layout.activity_super_upload;
    }

    private int level;

    @Override
    public void initView() {
        level = getIntent().getIntExtra("level", 0);
        tvTitleText.setText("掌柜");
        tvFz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MQUtils.clipText(UploadSuperActivity.this,tvContent.getText().toString());
                ToastUtil.showToast("复制成功");
            }
        });

    }


    @Override
    public void initData() {
        getDataInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public void getDataInfo() {
        DataManager.getInstance().getVipData(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> object) {
                dissLoadDialog();
                if (object != null) {
                    tvContent.setText(object.getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                //                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        });
    }
    private void uploadData() {

        Map map = new HashMap();
        map.put("later_level",level+1+"");
        map.put("img1",img1);
        map.put("img2",img2);
        DataManager.getInstance().getCreate(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                ToastUtil.showToast("上传成功");
                finish();
            }

            @Override
            public void onError(Throwable throwable) {

                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("上传成功");
                    finish();

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }
    private UploadPicAdapter mAdapter;

    @Override
    public void initListener() {

        mAdapter = new UploadPicAdapter(null, 9, "evaluate_pic");

        mAdapter.refresh();
        recyPic.setLayoutManager(new GridLayoutManager(this, 3));
        recyPic.setHasFixedSize(true);
        recyPic.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Constants.IMAGEITEM_DEFAULT_ADD.equals(mAdapter.getItem(position).getImagePath())) {

                    //打开选择
                    new RxPermissions(UploadSuperActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (!aBoolean) {
                                        return;
                                    }
                                    // 用户已经同意该权限
                                    new ImagePicker()
                                            .pickType(ImagePickType.MULTI)//设置选取类型(拍照、单选、多选)
                                            .maxNum(3 - mAdapter.getItemCount())//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                            .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                            .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                                            .start(UploadSuperActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            });
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_del:

                        List<ImageBean> data = mAdapter.getData();
                        map.remove(data.get(position).getImagePath());
                        mAdapter.remove(position);
                        mAdapter.refresh();

                        break;
                }
            }
        });
        btnOpenLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isEmpty(img1)){
                    ToastUtil.showToast("请上传图片");
                }   if(TextUtil.isEmpty(img2)){
                    ToastUtil.showToast("请上传图片");
                }
                uploadData();
            }
        });
    }

    private Map<String, String> map = new HashMap<>();

    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {


            ArrayList<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                if (mAdapter.getData().size() > 0) {
                    List<ImageBean> newList = mAdapter.getData();
                    newList.addAll(mAdapter.getData().size() - 1, resultList);
                    mAdapter.setNewData(newList);
                } else {
                    mAdapter.addData(0, resultList);
                }
                mAdapter.refresh();
            }
            imageBeanList = mAdapter.getData();
            map.clear();
            urls.clear();
            upPicPosition = 0;
            showLoadDialog();
            dealUpload();
        }


    }

    private List<String>    urls = new ArrayList<>();
    private int             upPicPosition;
    private List<ImageBean> imageBeanList;

    private void dealUpload() {

        if (imageBeanList != null && upPicPosition < imageBeanList.size()) {
            String imgPath = imageBeanList.get(upPicPosition).getImagePath();

            if (!Constants.IMAGEITEM_DEFAULT_ADD.equals(imgPath)) {
                uploadImg(imgPath,upPicPosition);
            }
            upPicPosition = upPicPosition + 1;
        }else {
            dissLoadDialog();
        }
    }
    private String img1;
    private String img2;
    private void uploadImg(String imgPath,int upPicPosition) {
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
            ToastUtil.showToast("上传封面图片失败");
            return;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {

                if (object != null) {
                    if(upPicPosition==0){
                        img1=object.getPath();
                    }else if(upPicPosition==1){
                        img2=object.getPath();
                        dissLoadDialog();
                    }
//                    map.put(imgPath, object.getPath());
//                    urls.add(object.getPath());
                    dealUpload();

                } else {
                    dissLoadDialog();
                    ToastUtil.showToast("上传封面图片失败");
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

}
