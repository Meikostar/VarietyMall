package com.smg.variety.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.LiveProduct;
import com.smg.variety.eventbus.LogoutEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.error.ErrorUtil;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.activity.PaySuccessActivity;
import com.smg.variety.view.activity.ShopStoreDetailActivity;
import com.smg.variety.view.adapter.AttentionStoreAdapter;
import com.smg.variety.view.adapter.LiveProductAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;

import butterknife.BindView;

/**
 * 关注 店铺
 */
public class LiveProductFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView  mRecyclerView;
    private LiveProductAdapter mAdapter;
    private int                mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_attention_community;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initRecyclerView();
    }

    @Override
    protected void initData() {
        mRefreshLayout.autoRefresh();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveProduct event) {
        mPage = 1;
        getProductListData();
    }
    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getProductListData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getProductListData();
            }
        });
    }
    private Map<String,String> mParamsMaps;
    private void getProductListData() {
        showLoadDialog();
        mParamsMaps = new HashMap<>();
        mParamsMaps.put("rows", Constants.PAGE_SIZE + "");
        mParamsMaps.put("page", mPage + "");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category");

        mParamsMaps.put("filter[is_live]", "1");


        DataManager.getInstance().findGoodsLives(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> data) {
                dissLoadDialog();

                setData(data);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        });
    }

    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
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
        mAdapter = new LiveProductAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NewListItemDto item = mAdapter.getItem(position);
                switch (view.getId()){
                    case R.id.tv_del:

                        delGroupUser(item.id);
                        break;
                    case R.id.ll_bg:

                        Intent intent = new Intent();
                        intent.putExtra("id",item.getId());
                        intent.putExtra("url",item.getCover());
                        getActivity().setResult(Activity.RESULT_OK,intent);
                        getActivity().finish();
                        break;
                }

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }
    private void delGroupUser(String id) {
        showLoadDialog();
        DataManager.getInstance().delLiveProduct(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("删除成功");
                mPage = 1;
                getProductListData();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("删除成功");
                    mPage = 1;
                    getProductListData();

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        },id);
    }
}
