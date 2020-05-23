package com.smg.variety.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQUtils;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.BaseDto2;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.ExtDto;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.SBHoutaibean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.OrderDetailGoodsListAdapter;
import com.smg.variety.view.widgets.ShopTypeWindows;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/*订单详情页*/
public class StoreOrderDetailActivity extends BaseActivity {


    String     id   = "";
    String     type = "";
    MyOrderDto mmyOrderDto;
    private static int UPLOAD_DATA = 100;

    @BindView(R.id.iv_code)
    ImageView      iv_code;
    @BindView(R.id.ll_kd)
    LinearLayout   ll_kd;
    @BindView(R.id.line)
    View           line;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.iv_chose)
    ImageView      ivChose;
    @BindView(R.id.iv_copy)
    ImageView      iv_copy;

    @BindView(R.id.ll_yes)
    LinearLayout   llYes;
    @BindView(R.id.iv_choses)
    ImageView      ivChoses;
    @BindView(R.id.ll_no)
    LinearLayout   llNo;
    @BindView(R.id.tv_kdgs)
    TextView       tvKdgs;
    @BindView(R.id.et_code)
    EditText       etCode;
    @BindView(R.id.ll_have)
    LinearLayout   llHave;
    @BindView(R.id.ll_state1)
    LinearLayout   llState1;
    @BindView(R.id.tv_info)
    TextView       tvInfo;
    @BindView(R.id.tv_wl_time)
    TextView       tvWlTime;
    @BindView(R.id.ll_go)
    LinearLayout   llGo;
    @BindView(R.id.ll_state2)
    LinearLayout   llState2;
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.tv_phone)
    TextView       tvPhone;
    @BindView(R.id.tv_address)
    TextView       tvAddress;
    @BindView(R.id.tv_state)
    TextView       tvState;
    @BindView(R.id.tv_no)
    TextView       tvNo;
    @BindView(R.id.tv_gk)
    TextView       tvGk;
    @BindView(R.id.tv_gkly)
    TextView       tv_gkly;

    @BindView(R.id.recy_my_order)
    RecyclerView   recyMyOrder;
    @BindView(R.id.tv_detail_price)
    TextView       tvDetailPrice;
    @BindView(R.id.tv_detail_express_money)
    TextView       tvDetailExpressMoney;
    @BindView(R.id.ll_detail_money)
    LinearLayout   llDetailMoney;
    @BindView(R.id.tv_detail_creat_time)
    TextView       tvDetailCreatTime;
    @BindView(R.id.tv_detail_shipped_time)
    TextView       tvDetailShippedTime;
    @BindView(R.id.tv_register)
    TextView       tvRegister;
    @BindView(R.id.ll_detail_bottom)
    LinearLayout   llDetailBottom;
    @BindView(R.id.et_content)
    EditText       et_content;

    private int state;

    @Override
    public void initListener() {
        bindClickEvent(ivTitleBack, () -> {
            finish();
        });
        iv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });
        ll_kd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoptypeWindow.selectData(dto, "选择快递公司");
                closeKeyBoard();
                shoptypeWindow.showAsDropDown(line);
            }
        });
        llGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle3 = new Bundle();
                bundle3.putString("id",id);
                bundle3.putSerializable("data",mSBHoutaibean);
                if(TextUtil.isNotEmpty(express_no)){
                    bundle3.putString("express_no", express_no);
                }
                gotoActivity(MyOrderLogisticsActivity.class,false,bundle3);
            }
        });
        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MQUtils.clipText(StoreOrderDetailActivity.this,tvAddress.getText().toString());
                ToastUtil.showToast("复制成功");
            }
        });
        llYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=0;
                llYes.setBackground(getResources().getDrawable(R.drawable.shape_radius_5_blue));
                llNo.setBackground(getResources().getDrawable(R.drawable.shape_radius_5_back));
                ivChose.setVisibility(View.VISIBLE);
                ivChoses.setVisibility(View.GONE);
                llHave.setVisibility(View.VISIBLE);
            }
        });
        llNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=1;
                llYes.setBackground(getResources().getDrawable(R.drawable.shape_radius_5_back));
                llNo.setBackground(getResources().getDrawable(R.drawable.shape_radius_5_blue));
                ivChose.setVisibility(View.GONE);
                ivChoses.setVisibility(View.VISIBLE);
                llHave.setVisibility(View.GONE);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0){
                    if(TextUtil.isEmpty(tvKdgs.getText().toString())){
                        ToastUtil.showToast("请选择快递公司");
                        return ;
                    }   if(TextUtil.isEmpty(etCode.getText().toString())){
                        ToastUtil.showToast("请输入快递单号");
                        return ;
                    }
                    editShipment(id,etCode.getText().toString(),et_content.getText().toString());
                }else {
                    editShipment(id,"",et_content.getText().toString());

                }

            }
        });

    }

    private List<AreaDto> dto = null;//标签数据
    private String ids;
    @Override
    protected void onResume() {
        super.onResume();

    }
    private String express_no;
    @Override
    public int getLayoutId() {
        return R.layout.ui_store_order_detail_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("订单详情");
        Intent intent = getIntent();
        id = intent.getStringExtra(Constants.INTENT_ID);
        type = intent.getStringExtra(Constants.INTENT_TYPE);
        shoptypeWindow = new ShopTypeWindows(this);
        shoptypeWindow.setSureListener(new ShopTypeWindows.ClickListener() {
            @Override
            public void clickListener(String id, String name) {
                tvKdgs.setText(name);
                express_code=id;
            }
        });
    }

    @Override
    public void initData() {
        getOrderDetail();
        getConfigs();

    }

    private void getOrderDetail() {
        DataManager.getInstance().getShopOrderDetail(new DefaultSingleObserver<HttpResult<MyOrderDto>>() {
            @Override
            public void onSuccess(HttpResult<MyOrderDto> result) {
                dealView(result.getData());
            }
        }, id, "shop,items.product,shipments,refundInfo,address,user","track");
    }
    private String express_code;

    private void editShipment(String order_id ,String express_no,String shop_remark) {

        Map<String, Object> map = new HashMap<>();
        map.put("order_id",order_id );
        if(TextUtil.isNotEmpty(express_code)){
            map.put("express_code",express_code);
        } if(TextUtil.isNotEmpty(express_no)){
            map.put("express_no", express_no);
        } if(TextUtil.isNotEmpty(shop_remark)){
            map.put("shop_remark", shop_remark);
        }




        showLoadDialog();
        DataManager.getInstance().editShipment(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                finish();
                ToastUtil.showToast("发货成功");

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    finish();
                    ToastUtil.showToast("发货成功");

                } else {
                    //                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }
    private ShopTypeWindows shoptypeWindow;
    private SBHoutaibean mSBHoutaibean;
    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getConfigs(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().shipment != null) {

                        dto = result.getData().shipment;
                        for(AreaDto areaDto:dto){
                            areaDto.title=areaDto.name;
                            areaDto.id=areaDto.code;
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.HOME_REQUEST_CODE_SCAN) {
            String stringExtra = data.getStringExtra(Constant.CODED_CONTENT);
            if (TextUtil.isNotEmpty(stringExtra)) {
                etCode.setText(stringExtra);
            }
        }
    }

    /**
     * 启动扫一扫
     */
    private void startScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setReactColor(R.color.my_color_009AFF);//设置扫描框四个角的颜色 默认为白色
        config.setScanLineColor(R.color.my_color_009AFF);//设置扫描线的颜色 默认白色
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        config.setShowbottomLayout(false);
        startActivityForResult(intent, Constants.HOME_REQUEST_CODE_SCAN);
    }
    public List<BaseDto2> datas;
    private void dealView(MyOrderDto myOrderDto) {
        if (myOrderDto != null) {
            mmyOrderDto = myOrderDto;
            if(myOrderDto.shipments!=null&&myOrderDto.shipments.data!=null&&myOrderDto.shipments.data.size()>0&&myOrderDto.shipments.data.get(0).track!=null&&myOrderDto.shipments.data.get(0).track.data!=null&&myOrderDto.shipments.data.get(0).track.data.size()>0){
                tvInfo.setText(myOrderDto.shipments.data.get(0).track.data.get(0).context);
                tvWlTime.setText(myOrderDto.shipments.data.get(0).track.data.get(0).ftime);
                express_no=myOrderDto.shipments.data.get(0).express_no;
                mSBHoutaibean=myOrderDto.shipments;
            }
            ids=myOrderDto.getId();
            tvState.setText(myOrderDto.getStatus_msg());
            if (myOrderDto.getAddress() != null && myOrderDto.getAddress().getData() != null) {
                tvName.setText( myOrderDto.getAddress().getData().getName());
                tvPhone.setText(myOrderDto.getAddress().getData().getMobile());
                tvAddress.setText( myOrderDto.getAddress().getData().getArea() + myOrderDto.getAddress().getData().getDetail());
            }
            if (myOrderDto.getItems() != null && myOrderDto.getItems().getData() != null && myOrderDto.getItems().getData().size() > 0) {
                recyMyOrder.setVisibility(View.VISIBLE);
                setGoodsListData(recyMyOrder, myOrderDto.getItems().getData());
            } else {
                recyMyOrder.setVisibility(View.GONE);
            }
            if(TextUtil.isNotEmpty(myOrderDto.getNo())){
                tvNo.setText(myOrderDto.getNo());
            }   if(myOrderDto.user!=null&&TextUtil.isNotEmpty(myOrderDto.user.data.getName())){
                tvGk.setText(myOrderDto.user.data.getName());
            }  if(TextUtil.isNotEmpty(myOrderDto.comment)){
                tv_gkly.setText(myOrderDto.comment);
            } if(TextUtil.isNotEmpty(myOrderDto.discount_info)){
                tvDetailPrice.setText(myOrderDto.discount_info);
             }
            if(TextUtil.isNotEmpty(myOrderDto.shop_remark)){

                et_content.setText(myOrderDto.shop_remark);
            } if(TextUtil.isNotEmpty(myOrderDto.getCreated_at())){
                tvDetailCreatTime.setText("创建时间：" +myOrderDto.getCreated_at());
            } if(TextUtil.isNotEmpty(myOrderDto.getPaid_at())){
                tvDetailShippedTime.setText("支付时间：" +myOrderDto.getPaid_at());
            }


            tvDetailExpressMoney.setText("¥" + myOrderDto.getTotal());


            switch (myOrderDto.getStatus()) {
                case "created":
                    llState1.setVisibility(View.GONE);
                    llState2.setVisibility(View.GONE);
                    tvRegister.setVisibility(View.GONE);
                    et_content.setFocusable(false);
                    et_content.setClickable(false);
                    et_content.setFocusableInTouchMode(false);
                    //待付款
                    break;
                case "paid":
                    llState1.setVisibility(View.VISIBLE);
                    llState2.setVisibility(View.GONE);
                    tvRegister.setVisibility(View.VISIBLE);
                    break;
                case "shipping"://待消费 待收货
                    llState1.setVisibility(View.GONE);
                    llState2.setVisibility(View.VISIBLE);
                    tvRegister.setVisibility(View.GONE);
                    et_content.setFocusable(false);
                    et_content.setClickable(false);
                    et_content.setFocusableInTouchMode(false);
                    break;
                case "shipped":
                    llState1.setVisibility(View.GONE);
                    llState2.setVisibility(View.VISIBLE);
                    tvRegister.setVisibility(View.GONE);
                    et_content.setFocusable(false);
                    et_content.setClickable(false);
                    et_content.setFocusableInTouchMode(false);
                case "closed":
                    llState1.setVisibility(View.GONE);
                    llState2.setVisibility(View.VISIBLE);
                    tvRegister.setVisibility(View.GONE);
                    et_content.setFocusable(false);
                    et_content.setClickable(false);
                    et_content.setFocusableInTouchMode(false);
                    break;
                case "completed":
                    llState1.setVisibility(View.GONE);
                    llState2.setVisibility(View.VISIBLE);
                    tvRegister.setVisibility(View.GONE);
                    et_content.setFocusable(false);
                    et_content.setClickable(false);
                    et_content.setFocusableInTouchMode(false);
                    break;


            }
        }
    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrderDetailGoodsListAdapter mAdapter = new OrderDetailGoodsListAdapter(items);
        recyclerView.setAdapter(mAdapter);
    }



}
