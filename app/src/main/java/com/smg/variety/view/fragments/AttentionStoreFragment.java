package com.smg.variety.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.view.activity.ShopStoreDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.AttentionStoreAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 关注 店铺
 */
public class AttentionStoreFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView  mRecyclerView;
    private AttentionStoreAdapter mAdapter;
    private int  mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention_community;
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
                getAttentionList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getAttentionList();
            }
        });
    }

    /**
     * 关注店铺
     */
    private void getAttentionList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage +"");
        DataManager.getInstance().getFollowingShops(new DefaultSingleObserver<HttpResult<List<AttentionCommunityBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<AttentionCommunityBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void setData(HttpResult<List<AttentionCommunityBean>> httpResult) {
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

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new AttentionStoreAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AttentionCommunityBean item = mAdapter.getItem(position);

                String type = item.getType();

                if("st".equals(type)){
                    Bundle bundle = new Bundle();
//                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_ID, mAdapter.getItem(position).getId()+"");
                    bundle.putString(ShopStoreDetailActivity.SHOP_DETAIL_ID,mAdapter.getItem(position).getId()+"");
                    gotoActivity(ShopStoreDetailActivity.class, false, bundle);
//                    gotoActivity(ShopDetailActivity.class, false,bundle);
                }else{
                    Bundle bundle = new Bundle();
//                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_ID, mAdapter.getItem(position).getId()+"");
                    bundle.putString(ShopStoreDetailActivity.SHOP_DETAIL_ID,mAdapter.getItem(position).getId()+"");
                    gotoActivity(ShopStoreDetailActivity.class, false, bundle);
                }

            }
        });
    }
}
