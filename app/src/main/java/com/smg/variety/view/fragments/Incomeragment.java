package com.smg.variety.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.IncomeRecordAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class Incomeragment extends BaseFragment {
    @BindView(R.id.recy_my_comment)
    RecyclerView            rvList;
    @BindView(R.id.refresh)
    SuperSwipeRefreshLayout refreshLayout;

    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;

    private String type = "";
    IncomeRecordAdapter mAdapter;

    public Incomeragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_layout;
    }

    @Override
    protected void initView() {
        initAdapter();


    }

    @Override
    protected void initData() {
        mCurrentPage = Constants.PAGE_NUM;
        loadData(true);
    }

    @Override
    protected void initListener() {
        setListener();
    }

    private int                    mPage        = 1;
    private void loadData(boolean isLoad) {
        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mCurrentPage + "");
        DataManager.getInstance().getUserlog(new DefaultSingleObserver<HttpResult<List<IncomeDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<IncomeDto>> result) {
                dissLoadDialog();
                if (null != result.getData() && result.getData().size() > 0) {

                    if (mCurrentPage == 1) {

                        mAdapter.setNewData(result.getData());
                        refreshLayout.setRefreshing(false);
                    } else {

                        mAdapter.addData(result.getData());
                        refreshLayout.setLoadMore(false);
                    }

                } else {
                    EmptyView emptyView = new EmptyView(getActivity());

                        emptyView.setTvEmptyTip("暂无数据");

                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());


            }

            @Override
            public void onError(Throwable throwable) {

                
            }
        },map);


    }


    private void initAdapter() {
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new IncomeRecordAdapter();
        rvList.setAdapter(mAdapter);
    }


    private void setListener() {
        mSwipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        mSwipeRefreshLayoutUtil.setSwipeRefreshView(refreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = Constants.PAGE_NUM;
                loadData(false);
            }

            @Override
            public void onLoadMore() {
                mCurrentPage++;
                loadData(false);
            }
        });
    }

    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoadMore(false);
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
