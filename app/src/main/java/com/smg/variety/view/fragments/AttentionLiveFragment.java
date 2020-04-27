package com.smg.variety.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SearchEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.LiveProduct;
import com.smg.variety.eventbus.LiveSearch;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.ShopStoreDetailActivity;
import com.smg.variety.view.adapter.AttentionLiveAdapter;
import com.smg.variety.view.adapter.AttentionStoreAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 关注 店铺
 */
public class AttentionLiveFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView  mRecyclerView;
    private AttentionLiveAdapter mAdapter;
    private int                  mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention_community;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initRecyclerView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveSearch event) {

        if(event.state==0){
            mPage = 1;
            content=event.content;
            getAttentionList();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
    private String content;
    /**
     * 关注店铺
     */
    private void getAttentionList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage +"");
        if(TextUtil.isNotEmpty(content)){
            map.put("user.name", content);
        }
        map.put("include", "user");
        DataManager.getInstance().getShopsList(new DefaultSingleObserver<HttpResult<List<AttentionCommunityBean>>>() {
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
    private String authorId;
    private void postAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", authorId);
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("关注成功");
                getAttentionList();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    getAttentionList();
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", authorId);
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("取消关注成功");
                getAttentionList();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    getAttentionList();
                } else {
                    ToastUtil.toast("取消关注失败");
                }
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
        mAdapter = new AttentionLiveAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AttentionCommunityBean item = (AttentionCommunityBean) adapter.getItem(position);
                PersonalInfoDto user=item.user.getData();
                authorId=user.getId();
                if(user.isFollowed){
                    deleteAttention();
                }else {
                    postAttention();
                }

            }
        });
    }
}
