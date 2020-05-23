package com.smg.variety.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.CarrieryDto;
import com.smg.variety.bean.MyOrderLogisticsDto;
import com.smg.variety.bean.SBHoutaibean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.MyOrderLogisticsAdapter;
import com.smg.variety.view.adapter.MyOrderLogisticsItemAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 物流信息
 */
public class MyOrderLogisticsActivity extends BaseActivity {
    @BindView(R.id.recy_logistics)
    RecyclerView recyclerView;
    @BindView(R.id.wuliu_state_tv)
    TextView     wuliu_state_tv;
    @BindView(R.id.crly_tv)
    TextView     crly_tv;
    @BindView(R.id.ddyh_tv)
    TextView     ddyh_tv;
    @BindView(R.id.shop_icon_iv)
    ImageView    shop_icon_iv;
    @BindView(R.id.empty_view)
    View         empty_view;
    @BindView(R.id.top_view_ll)
    LinearLayout top_view_ll;

    private MyOrderLogisticsItemAdapter mAdapter;
    private String                      orderId, express_no;

    @Override
    public int getLayoutId() {
        return R.layout.ui_myorder_logistics_layout;
    }
    private SBHoutaibean data;
    @Override
    public void initView() {

        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        actionbar.setTitle("查看物流");
        initAdapter();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString("id");
            express_no = bundle.getString("express_no");
            data = (SBHoutaibean) bundle.getSerializable("data");
            setData();
        }

    }

    @Override
    public void initData() {
//        getLogisticsInfo();
    }

    @Override
    public void initListener() {

    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyOrderLogisticsItemAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void getLogisticsInfo() {
        if (TextUtils.isEmpty(orderId)) {
            ToastUtil.showToast("暂无物流");
            return;
        }
        showLoadDialog();
        DataManager.getInstance().getLogisticsList(new DefaultSingleObserver<List<MyOrderLogisticsDto>>() {
            @Override
            public void onSuccess(List<MyOrderLogisticsDto> myOrderLogisticsDto) {
                dissLoadDialog();
                if (myOrderLogisticsDto != null && myOrderLogisticsDto.size() > 0) {
                    top_view_ll.setVisibility(View.VISIBLE);
//                    setData(myOrderLogisticsDto.get(0));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                LogUtil.e("物流", throwable.getMessage());
                //                if (!ApiException.getInstance().isSuccess()) {
                ToastUtil.showToast("暂无物流");
                empty_view.setVisibility(View.VISIBLE);
                //                } else
                //                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, express_no, orderId);
    }

    private void setData() {
        wuliu_state_tv.setText(data.data.get(0).infos.name);
//        crly_tv.setText("" + myOrderLogisticsDto.getCarrier_code());
        ddyh_tv.setText("物流编号:" +data.data.get(0).express_no);
        GlideUtils.getInstances().loadRoundImg(this,shop_icon_iv,data.data.get(0).infos.logo);
        if (data.data.get(0).track.data!= null ) {
            mAdapter.setNewData(data.data.get(0).track.data);
        } else {
            mAdapter.setEmptyView(new EmptyView(this));
        }
//        getCarriery(myOrderLogisticsDto.getCarrier_code());
    }

    private void getCarriery(String code) {
        showLoadDialog();
        DataManager.getInstance().getCarriery(new DefaultSingleObserver<HttpResult<List<CarrieryDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CarrieryDto>> myOrderLogisticsDto) {
                dissLoadDialog();
                if (myOrderLogisticsDto != null && myOrderLogisticsDto.getData() != null && myOrderLogisticsDto.getData().size() > 0) {
                    CarrieryDto carrieryDto = myOrderLogisticsDto.getData().get(0);
                    if (!TextUtil.isEmpty(carrieryDto.getPicture())) {
                        GlideUtils.getInstances().loadNormalImg(MyOrderLogisticsActivity.this, shop_icon_iv, "http:" + carrieryDto.getPicture());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, code);
    }

}
