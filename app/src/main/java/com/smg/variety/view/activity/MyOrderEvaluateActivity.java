package com.smg.variety.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.utils.TextUtil;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.RateBean;
import com.smg.variety.bean.UploadFilesDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.Compressor;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.view.adapter.ReasonUploadPicAdapter;
import com.smg.variety.view.widgets.ratingbar.BaseRatingBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyOrderEvaluateActivity extends BaseActivity {
    @BindView(R.id.ed_info)
    EditText ed_info;
    @BindView(R.id.tv_ed_number)
    TextView tv_ed_number;
    @BindView(R.id.recy_evaluate)
    RecyclerView recyPic;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_item_order_status)
    TextView tvItemOrderStatus;
    @BindView(R.id.tv_evaluate_logo)
    ImageView tvEvaluateLogo;
    @BindView(R.id.tv_evaluate_des)
    TextView tvEvaluateDes;
    @BindView(R.id.tv_evaluate_money)
    TextView tvEvaluateMoney;
    @BindView(R.id.tv_order_goods_params)
    TextView tvOrderGoodsParams;
    @BindView(R.id.tv_evaluate_number)
    TextView tvEvaluateNumber;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_evaluate_1)
    TextView tvEvaluate1;
    @BindView(R.id.rb_evaluate_1)
    BaseRatingBar rbEvaluate1;
    @BindView(R.id.tv_evaluate_2)
    TextView      tvEvaluate2;
    @BindView(R.id.rb_evaluate_2)
    BaseRatingBar rbEvaluate2;
    @BindView(R.id.tv_evaluate_3)
    TextView      tvEvaluate3;
    @BindView(R.id.rb_evaluate_3)
    BaseRatingBar rbEvaluate3;
    @BindView(R.id.tv_evaluate_4)
    TextView      tvEvaluate4;
    @BindView(R.id.rb_evaluate_4)
    BaseRatingBar rbEvaluate4;

    @BindView(R.id.sr_view)
    ScrollView    sr_view;

    private int upPicPosition;
    private List<String> areadUploadImg = new ArrayList<>();
    private ReasonUploadPicAdapter mAdapter;

    String orderNo, itemId,orderId,type;
    @Override
    public void initListener() {
        bindClickEvent(btn_sure, () -> {
            initUploadData();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_myorder_evaluate_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("发表评价");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemId = bundle.getString(Constants.INTENT_ID);
            orderNo = bundle.getString(Constants.INTENT_ORDER_NO);
            type = bundle.getString("type");
            tvOrderNo.setText("订单编号："+orderNo);
            orderId = bundle.getString("orderId");
            tvItemOrderStatus.setText(getIntent().getStringExtra(Constants.INTENT_ORDER_STATUS));
            String imgUrl =  bundle.getString(Constants.IMAGEITEM_IMG_URL);
            GlideUtils.getInstances().loadNormalImg(this, tvEvaluateLogo, imgUrl,R.drawable.moren_sf);
            tvEvaluateDes.setText(getIntent().getStringExtra("goods_title"));
            tvEvaluateMoney.setText("¥"+getIntent().getStringExtra("goods_price"));
            tvEvaluateNumber.setText("x"+getIntent().getStringExtra("goods_num"));
            tvOrderGoodsParams.setText(getIntent().getStringExtra("goods_options"));
            tvTotalPrice.setText("合计：¥"+getIntent().getStringExtra("goods_price"));
        }
        initPicAdapter();

    }

    @Override
    public void initData() {
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void keyBoardShow(int height) {
                Log.e(TAG, "键盘显示 高度" + height);
//                ll_bg.setPadding(0, 0, 0, height);
                sr_view.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
                ed_info.setFocusable(true);
                ed_info.setFocusableInTouchMode(true);
                ed_info.requestFocus();
            }

            @Override
            public void keyBoardHide(int height) {
                Log.e(TAG, "键盘隐藏 高度" + height);
                sr_view.fullScroll(ScrollView.FOCUS_UP);//滚动到底部

                ed_info.setFocusable(true);
                ed_info.setFocusableInTouchMode(true);
                ed_info.requestFocus();
            }
        });
    }

    //获取键盘的高度
    private int keyboardHeight;




    private void initUploadData() {

        if(TextUtil.isEmpty(ed_info.getText().toString())){
            ToastUtil.showToast("请输入评价内容");
            return;
        }
        showLoadDialog();
        upPicPosition = 0;
        areadUploadImg.clear();
        dealUpload();
    }

    private void dealUpload() {
        List<ImageBean> imageBeanList = mAdapter.getData();
        if (imageBeanList != null && upPicPosition < imageBeanList.size()) {
            String imgPath = imageBeanList.get(upPicPosition).getImagePath();
            upPicPosition = upPicPosition + 1;
            if (!Constants.IMAGEITEM_DEFAULT_ADD.equals(imgPath)) {
                uploadImg(imgPath);
            } else {//添加图片
                dealUpload();
            }

        } else {
            upLoadFinish();
        }

    }
     private String ids;
    private void upLoadFinish() {
        ids="";
        Map<String, Object> map = new HashMap<>();
        map.put("id", itemId);
        map.put("object","SMG\\Mall\\Models\\MallProduct");
        //评价星级
        map.put("rate",new RateBean(rbEvaluate1.getRating(),rbEvaluate2.getRating(),rbEvaluate3.getRating(),rbEvaluate4.getRating()));
        map.put("comment", ed_info.getText().toString());
        if (areadUploadImg.size() > 0) {
            for (int i = 0; i < areadUploadImg.size(); i++) {
               if(i==0){
                   ids= areadUploadImg.get(i);
               }else {
               ids=ids+","+ areadUploadImg.get(i);

            }
        }
        if(TextUtil.isNotEmpty(ids)){
            map.put("images",ids);
            }
        }

        map.put("flag",orderId);
        DataManager.getInstance().toComment(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                Intent intent = new Intent(MyOrderEvaluateActivity.this, MyOrderEvaluateSuccessActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);

                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    Intent intent = new Intent(MyOrderEvaluateActivity.this, MyOrderEvaluateSuccessActivity.class);
                    intent.putExtra("type",type);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);
    }

    private void initPicAdapter() {
        mAdapter = new ReasonUploadPicAdapter(null, 6, "evaluate_pic");
        mAdapter.refresh();
        recyPic.setLayoutManager(new GridLayoutManager(this, 3));
        recyPic.setHasFixedSize(true);
        recyPic.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Constants.IMAGEITEM_DEFAULT_ADD.equals(mAdapter.getItem(position).getImagePath())) {
                    //打开选择
                    new RxPermissions(MyOrderEvaluateActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (!aBoolean) {
                                        return;
                                    }
                                    // 用户已经同意该权限
                                    new ImagePicker()
                                            .pickType(ImagePickType.MULTI)//设置选取类型(拍照、单选、多选)
                                            .maxNum(7 - mAdapter.getItemCount())//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                            .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                            .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                                            .start(MyOrderEvaluateActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
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
                        mAdapter.remove(position);
                        mAdapter.refresh();
                        break;
                }
            }
        });
    }

    private void uploadImg(String imgPath) {
        File file = new File(imgPath);
        File compressedImage = null;
        if (file.exists()) {
            //压缩文件
            try {
                compressedImage = new Compressor(this).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (compressedImage == null) {
                compressedImage = file;
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
                @Override
                public void onSuccess(UploadFilesDto object) {
                    if (object != null) {
                        Log.d("onSuccess() = ", "UploadFilesDto==" + object.getName());
                        areadUploadImg.add(object.getId() + "");
                    }
                    dealUpload();
                }

                @Override
                public void onError(Throwable throwable) {
                    dissLoadDialog();
//                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }, "comment", part);
        } else {
            dealUpload();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                mAdapter.addData(0, resultList);
                mAdapter.refresh();
            }
        }

    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
