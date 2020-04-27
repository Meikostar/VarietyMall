package com.smg.variety.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.bean.ScoreIncomeBean;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.RunningWaterDetailsAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 流水明细
 */
public class RunningWaterDetailsFragment extends BaseFragment {
    @BindView(R.id.recy_my_order)
    RecyclerView       rvList;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshLayout;

    private RunningWaterDetailsAdapter mOrderListAdapter;

    private int mOrderStatus;
    private int mPage = 1;


    public static RunningWaterDetailsFragment newInstance(int type) {
        RunningWaterDetailsFragment fragment = new RunningWaterDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_running_water_details;
    }

    @Override
    protected void initView() {
        mOrderStatus = getArguments().getInt("type");
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mOrderListAdapter = new RunningWaterDetailsAdapter(getActivity());
        rvList.setAdapter(mOrderListAdapter);
    }

    @Override
    protected void initData() {
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
        mRefreshLayout.autoRefresh();
    }

    public void RefreshData() {
        mRefreshLayout.autoRefresh();
    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mPage + "");

//        map.put("include", "order.user,order.address");
        DataManager.getInstance().getWithdraw(new DefaultSingleObserver<HttpResult<List<IncomeDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<IncomeDto>> httpResult) {
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.setEnableRefresh(true);

            }
        }, map);
    }

    private void setData(HttpResult<List<IncomeDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mOrderListAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mOrderListAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mOrderListAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

}
