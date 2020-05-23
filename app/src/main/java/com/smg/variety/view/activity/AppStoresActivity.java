package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DownLoadServerice;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.widgets.CircleImageView;
import com.smg.variety.view.widgets.updatadialog.UpdataCallback;
import com.smg.variety.view.widgets.updatadialog.UpdataDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class AppStoresActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView       ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView        tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView        tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout  layoutTop;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_date)
    TextView        tvDate;
    @BindView(R.id.tv_jy1)
    TextView        tvJy1;
    @BindView(R.id.tv_jy2)
    TextView        tvJy2;
    @BindView(R.id.tv_jy3)
    TextView        tvJy3;
    @BindView(R.id.tv_jy4)
    TextView        tvJy4;
    @BindView(R.id.ll_bg)
    LinearLayout    llBg;
    @BindView(R.id.tv_dd1)
    TextView        tvDd1;
    @BindView(R.id.tv_dd2)
    TextView        tvDd2;
    @BindView(R.id.tv_dd3)
    TextView        tvDd3;
    @BindView(R.id.tv_dd4)
    TextView        tvDd4;
    @BindView(R.id.ll_bgs)
    LinearLayout    llBgs;
    @BindView(R.id.tv_sp1)
    TextView        tvSp1;
    @BindView(R.id.tv_sp2)
    TextView        tvSp2;
    @BindView(R.id.tv_sp3)
    TextView        tvSp3;
    @BindView(R.id.ll_bg1)
    LinearLayout    llBg1;
    @BindView(R.id.tv_tz1)
    TextView        tvTz1;
    @BindView(R.id.view_one)
    View            viewOne;
    @BindView(R.id.tv_tz2)
    TextView        tvTz2;
    @BindView(R.id.view_two)
    View            viewTwo;
    @BindView(R.id.tv_tz3)
    TextView        tvTz3;
    @BindView(R.id.view_three)
    View            viewThree;
    @BindView(R.id.ll_bg2)
    LinearLayout    llBg2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_stores;
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSellersData();
        getNotification();
        getBrandShopDetail();

    }

    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llBg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(EntityProductActivity.class);
            }
        });
        llBgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(OrderStoreActivity.class);
            }
        });
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SthoreWalletActivity.class);
            }
        });

    }

    private boolean allGet;
    //    today_order_count: 今日订单数 ;
    //    week_order_count: 近7天订单数;
    //    month_order_count: 近30天订单数;
    //    all_order_count: 全部订单数;
    //    created_order_count: 待支付订单数;
    //    paid_order_count: 待发货订单数 ;
    //    completed_order_count: 已完成订单数;
    //    refund_order_count: 待退款订单数;
    //    buy_product_count: 出售中商品 ;
    //    sale_product_count: 上架商品数;
    //    no_sale_product_count:下架商品数

    private void getNotification() {
        //showLoadDialog();
        DataManager.getInstance().getNotification(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        if (result.getData().system!=null){
                            if(result.getData().system.unread_count==0){
                                tvTz2.setText("0");
                                viewOne.setVisibility(View.INVISIBLE);
                            }else {
                                viewOne.setVisibility(View.VISIBLE);
                                tvTz2.setText(result.getData().system.unread_count+"");
                            }

                        }     if (result.getData().order!=null){
                            if(result.getData().order.unread_count==0){
                                viewTwo.setVisibility(View.INVISIBLE);
                                tvTz3.setText("0");
                            }else {
                                viewTwo.setVisibility(View.VISIBLE);
                                tvTz3.setText(result.getData().order.unread_count+"");
                            }

                        }
                        if (result.getData().system!=null){
                            if(result.getData().system.unread_count==0){
                                tvTz1.setText("0");
                            }else {
                                tvTz1.setText(result.getData().system.unread_count+"");
                            }

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
    private void getBrandShopDetail() {
        //showLoadDialog();
        DataManager.getInstance().getBrandShopDetail(new DefaultSingleObserver<HttpResult<RecommendListDto>>() {
            @Override
            public void onSuccess(HttpResult<RecommendListDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        tvName.setText(result.getData().getShop_name());
                        tvDate.setText("申请日期："+result.getData().getCreated_at());
                        GlideUtils.getInstances().loadNormalImg(AppStoresActivity.this,civUserAvatar,result.getData().getLogo());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        },ShareUtil.getInstance().get(Constants.USER_ID));
    }

    private void getSellersData() {
        //showLoadDialog();
        DataManager.getInstance().getSellersData(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        tvJy1.setText(result.getData().today_order_count);
                        tvJy2.setText(result.getData().yesterday_order_count);
                        tvJy3.setText(result.getData().week_order_count);
                        tvJy4.setText(result.getData().month_order_count);
                        tvDd1.setText(result.getData().created_order_count);
                        tvDd2.setText(result.getData().paid_order_count);
                        tvDd3.setText(result.getData().completed_order_count);
                        tvDd4.setText(result.getData().refund_order_count);
                        tvSp1.setText(result.getData().buy_product_count);
                        tvSp2.setText(result.getData().no_sale_product_count);
                        tvSp3.setText(result.getData().sale_product_count);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
}