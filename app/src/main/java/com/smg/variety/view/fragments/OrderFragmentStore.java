package com.smg.variety.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.bean.CheckOutOrderResult;
import com.smg.variety.bean.CouponCodeInfo;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.WEIXINREQ;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.PayUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.SettingPasswordActivity;
import com.smg.variety.view.activity.MyOrderEvaluateListActivity;
import com.smg.variety.view.activity.MyOrderLogisticsActivity;
import com.smg.variety.view.activity.MyOrderReturnActivity;
import com.smg.variety.view.activity.PaySuccessActivity;
import com.smg.variety.view.adapter.OrderStoreAdapter;
import com.smg.variety.view.adapter.OrderTgAdapter;
import com.smg.variety.view.widgets.CustomDividerItemDecoration;
import com.smg.variety.view.widgets.InputPwdDialog;
import com.smg.variety.view.widgets.MCheckBox;
import com.smg.variety.view.widgets.PhotoPopupWindow;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.dialog.CouponCodeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 收藏 商品
 */
@SuppressLint("ValidFragment")
public class OrderFragmentStore extends BaseFragment implements ToFragmentListener {
    //更新列表
    private static int UPLOAD_DATA = 100;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    @BindView(R.id.header)
    MaterialHeader     header;
    Unbinder unbinder;
    private OrderStoreAdapter mAdapter;
    private String            types       = "";
    private String            currentPage = "1";
    private int               mPage       = 1;

    public OrderFragmentStore(String type) {
        this.types = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_store;
    }


    @Override
    protected void initView() {
        initRecyclerView();

    }

    private boolean ishow;

    @Override
    public void onResume() {
        super.onResume();
        if (ishow) {
            mRefreshLayout.autoRefresh();
        } else {
            ishow = true;
        }
    }

    @Override
    protected void initData() {
        mRefreshLayout.autoRefresh();


    }

    private boolean isFirst;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirst) {
                mPage = 1;
                loadData();
            } else {
                isFirst = true;
            }

        }
    }

    @Override
    protected void initListener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                loadData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                loadData();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        mAdapter = new OrderStoreAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


            }
        });


    }


    private void loadData() {
        HashMap<String, String> map = new HashMap<>();

        map.put("page", mPage + "");
        map.put("include", "items.product");
        map.put("mall_type", "default");
//        map.put("api_nested_no_data", "1");
        DataManager.getInstance().getShopOrders(new DefaultSingleObserver<HttpResult<List<MyOrderDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<MyOrderDto>> httpResult) {
                if (states == 1) {
                    return;
                }
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (getActivity().isFinishing()) {
                    return;
                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, map);
    }

    private String star;
    private String end;

    private void setData(HttpResult<List<MyOrderDto>> httpResult) {
        //        if (httpResult == null || httpResult.getData() == null) {
        //            return;
        //        }
        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }




    private int states;

    @Override
    public void onDestroy() {
        super.onDestroy();
        states = 1;
    }



    /**
     * 获取收货地址
     */
    private List<AddressDto> mAddressDatas = new ArrayList<>();
    private String           addressId;



    private List<CheckOutOrderResult> ruslts        = new ArrayList<>();
    private int                       RQ_WEIXIN_PAY = 12;




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_DATA && mRefreshLayout != null) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onTypeClick(String star, String end) {
        this.star = star;
        this.end = end;
        mPage = 1;
        loadData();
    }
}
