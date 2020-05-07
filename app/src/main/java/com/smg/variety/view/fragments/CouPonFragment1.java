package com.smg.variety.view.fragments;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.NewPeopleAdapter;
import com.smg.variety.view.adapter.NewPeopleAdapters;
import com.smg.variety.view.widgets.autoview.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 关注 店铺
 */
@SuppressLint("ValidFragment")
public class CouPonFragment1 extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView  mRecyclerView;
    private NewPeopleAdapters mAdapter;
    private int               mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention_community;
    }
    private String type;

    public CouPonFragment1(String type) {
        this.type = type;
    }
    @Override
    protected void initView() {
        initRecyclerView();
    }

    @Override
    protected void initData() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getCouPonList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getCouPonList();
            }
        });
    }



    private void getCouPonList() {
        Map<String, String> map = new HashMap<>();
        showLoadDialog();
        map.put("page", mPage +"");
        map.put("include", "coupon.shop");
        if(type.equals("2")){
            map.put("filter[scopeUsed]", "1");
        }else {
            map.put("filter[scopeExpired]", "1");
        }

        DataManager.getInstance().getCouPonLists(new DefaultSingleObserver<HttpResult<List<NewPeopleBeam>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewPeopleBeam>> result) {
                dissLoadDialog();
                if(result.getData() != null){
                    setData(result);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }
    private void setData(HttpResult<List<NewPeopleBeam>> httpResult) {
        if(httpResult == null || httpResult.getData()==null){
            return;
        }

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

        if(httpResult.getMeta()!=null && httpResult.getMeta().getPagination()!=null){
            if(httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }
    private List<NewPeopleBeam> mlist = new ArrayList<>();
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NewPeopleAdapters(mlist,getActivity(),type);
        mRecyclerView.setAdapter(mAdapter);

    }
}
