package com.smg.variety.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.AllCityDto;
import com.smg.variety.bean.BaseType;
import com.smg.variety.bean.LiveCatesBean;
import com.smg.variety.bean.LiveVideoInfo;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.Compressor;
import com.smg.variety.common.utils.DensityUtil;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.qiniu.AVStreamingActivity;
import com.smg.variety.qiniu.StreamingBaseActivity;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.ProcutChooseAdapter;
import com.smg.variety.view.mainfragment.learn.StartLiveCategoryAdapter;
import com.smg.variety.view.widgets.Custom_TagBtn;
import com.smg.variety.view.widgets.FlexboxLayout;
import com.smg.variety.view.widgets.InputPwdDialog;
import com.smg.variety.view.widgets.MCheckBox;
import com.smg.variety.view.widgets.PhotoPopupWindow;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
 * 开播(准备直播)
 */
public class StartLiveActivity extends BaseActivity {


    UploadFilesDto uploadFilesDto;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;

    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.tv_title_right)
    TextView       mRightText;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.fbl_zw)
    FlexboxLayout  fblZw;
    @BindView(R.id.ll_bg)
    LinearLayout   llBg;
    @BindView(R.id.img_add)
    ImageView      imgAdd;
    @BindView(R.id.et_des)
    EditText       etDes;
    @BindView(R.id.et_title)
    EditText       etTitle;
    @BindView(R.id.recy_view)
    RecyclerView   recyPic;
    @BindView(R.id.btn_open_live)
    TextView       btnOpenLive;


    @Override
    public int getLayoutId() {
        return R.layout.activity_start_live;
    }

    @Override
    public void initView() {

        mTitleText.setText("开播");
        mAdapter = new ProcutChooseAdapter(null);

        recyPic.setLayoutManager(new GridLayoutManager(this, 3));
        recyPic.setHasFixedSize(true);
        recyPic.setAdapter(mAdapter);
        mAdapter.refresh();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(StartLiveActivity.this, LiveProductActivity.class);
                startActivityForResult(intent, Constants.INTENT_REQUESTCODE_VERIFIED_IMG2);
            }
        });
    }


    @Override
    public void initData() {

        getLiveCates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private Map<String, String> map = new HashMap<>();
    private ProcutChooseAdapter mAdapter;

    /**
     * 创建流式布局item
     *
     * @param content
     * @return
     */
    public Custom_TagBtn createBaseFlexItemTextView(BaseType content) {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(this, 10);
        lp.leftMargin = DensityUtil.dip2px(this, 7);
        lp.rightMargin = DensityUtil.dip2px(this, 7);


        Custom_TagBtn view = (Custom_TagBtn) LayoutInflater.from(this).inflate(R.layout.dish_item, null);

        if (content.isChoos) {

            view.setColors(R.color.white);
            view.setBg(getResources().getDrawable(R.drawable.shape_radius_14_blue));
        } else {
            view.setColors(R.color.my_color_111);
            view.setBg(getResources().getDrawable(R.drawable.shape_radius_14_ddd));
        }
        int width = (int) DensityUtil.getWidth(this) / 3;
        String name = content.name;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(15);
        int with = (int) textPaint.measureText(name);
        view.setSize(with, 30, 15, 1);
        view.setLayoutParams(lp);
        view.setCustomText(content.name);

        return view;
    }

    private List<BaseType> tags = new ArrayList<>();

    public void setTagAdapter() {

        fblZw.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            final Custom_TagBtn tagBtn = createBaseFlexItemTextView(tags.get(i));
            final int position = i;

            tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                @Override
                public void clickDelete(int type) {


                    for (int j = 0; j < tags.size(); j++) {
                        if (position == j) {
                            id=tags.get(j).classifyId;
                            tags.get(j).isChoos = true;
                        } else {
                            tags.get(j).isChoos = false;
                        }
                    }

                    setTagAdapter();
                }
            });
            fblZw.addView(tagBtn);
        }

    }

    @Override
    public void initListener() {

    }
    private String ids;
    private String id;
    @OnClick({R.id.iv_title_back, R.id.btn_open_live,
            R.id.tv_title_right, R.id.img_add
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                gotoActivity(AttentionUserListActivity.class);
                break;
            case R.id.btn_open_live:

                if (TextUtils.isEmpty(etDes.getText().toString().trim())) {
                    ToastUtil.showToast("请输入详细内容");
                    return;
                }
                if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
                    ToastUtil.showToast("请输入直播标题");
                    return;
                }
                if (TextUtils.isEmpty(id)) {
                    ToastUtil.showToast("请选择分类");
                    return;
                }
                if (uploadFilesDto == null || TextUtils.isEmpty(uploadFilesDto.getPath())) {
                    ToastUtil.showToast("请添加封面图片");
                    return;
                }
                ids="";

                for (Map.Entry<String, ImageBean> entry : imgMap.entrySet()) {
                    if(TextUtil.isNotEmpty(ids)){
                        ids=ids+","+entry.getKey();
                    }else {
                        ids=entry.getKey();
                    }
                }
                showPopPayWindows();
                break;
            case R.id.img_add:
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
                                            .start(StartLiveActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            }
                        });
                break;
        }

    }

    public void getLiveCates() {
        showLoadDialog();
        DataManager.getInstance().getLiveCate(new DefaultSingleObserver<HttpResult<List<AllCityDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AllCityDto>> result) {
                dissLoadDialog();
                tags.clear();
                if (null != result.getData() && result.getData().size() > 0) {

                    for (AllCityDto bean : result.getData()) {
                        BaseType baseType = new BaseType();
                        baseType.name = bean.cat_name;
                        baseType.classifyId = bean.getId();
                        tags.add(baseType);
                    }
                    setTagAdapter();
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, 0);

    }

    private TextView         tv_two;
    private TextView         tv_one;
    private int state=0;
    private View             view;
    public void showPopPayWindows() {

        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.chose_popwindow_view, null);

            tv_one = view.findViewById(R.id.tv_one);
            tv_two = view.findViewById(R.id.tv_two);

            tv_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 1;
                    starLive();
                    mWindowAddPhoto.dismiss();
                }
            });
            tv_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 2;
                    starLive();
                    mWindowAddPhoto.dismiss();
                }
            });


            mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
            mWindowAddPhoto.showAtLocation(btnOpenLive, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhoto.showAtLocation(btnOpenLive, Gravity.BOTTOM, 0, 0);
        }


    }

    public void starLive(){
        if(state==0){
            ToastUtil.showToast("请选择直播方式");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("cate_id", id);
        if(TextUtil.isNotEmpty(ids)){
            map.put("product_ids", ids);
        }
        if(state==1){
            map.put("is_record",true);
        }else {
            map.put("is_record",false);
        }
        map.put("title", etTitle.getText().toString().trim());
        map.put("desc", etDes.getText().toString().trim());
        map.put("images", uploadFilesDto.getPath());
        map.put("include", "room,apply");
        openLive(map);
    }
   private PhotoPopupWindow mWindowAddPhoto ;
    private void uploadImgs(String imgPath) {
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
        showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                dissLoadDialog();
                if (object != null) {
                    uploadFilesDto = object;
                    GlideUtils.getInstances().loadNormalImg(StartLiveActivity.this, imgAdd, imgPath);
                } else {
                    ToastUtil.showToast("上传封面图片失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                //                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "live", part);
    }

    public void openLive(HashMap<String, Object> map) {
        showLoadDialog();
        DataManager.getInstance().openLive(new DefaultSingleObserver<HttpResult<LiveVideoInfo>>() {
            @Override
            public void onSuccess(HttpResult<LiveVideoInfo> result) {
                dissLoadDialog();
                if (result != null) {
                    Intent intent = new Intent(StartLiveActivity.this, AVStreamingActivity.class);
                    intent.putExtra(StreamingBaseActivity.PUBLIC_URL, result.getData().apply.getData().rtmp_publish_url);
                    intent.putExtra("id", result.getData().getId());
                    if (result.getData() != null && result.getData().getRoom() != null && result.getData().getRoom().getData() != null) {
                        intent.putExtra("room", result.getData().getRoom().getData());
                    }
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, map);
    }

    private Map<String,ImageBean> imgMap=new HashMap<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1) {
                List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
                if (resultList != null && resultList.size() > 0) {
                    String imageUrl = resultList.get(0).getImagePath();
                    uploadImgs(imageUrl);
                }
            } else {
                List<ImageBean> imgList=new ArrayList<>();
                String id =data.getStringExtra("id");
                String url =  data.getStringExtra("url");
                ImageBean mBean=new ImageBean();
                mBean.id=id;
                mBean.setImagePath(url);
                ImageBean bean = imgMap.get(id);

                if(bean!=null&&TextUtil.isNotEmpty(bean.id)){
                    ToastUtil.showToast("产品已添加");
                    return;
                }
                imgMap.put(id,mBean);
                for (Map.Entry<String, ImageBean> entry : imgMap.entrySet()) {
                    imgList.add(entry.getValue());
                }
                mAdapter.setNewData(imgList);
                mAdapter.refresh();
            }

        }
    }

    private List<String>    urls = new ArrayList<>();
    private int             upPicPosition;
    private List<ImageBean> imageBeanList;


}
