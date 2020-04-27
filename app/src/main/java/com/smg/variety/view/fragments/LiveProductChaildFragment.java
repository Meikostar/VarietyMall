package com.smg.variety.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.ExtDto;
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
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.ProductListActivity;
import com.smg.variety.view.activity.ShopStoreDetailActivity;
import com.smg.variety.view.adapter.LiveChaildProductAdapter;
import com.smg.variety.view.adapter.LiveProductAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 关注 店铺
 */
@SuppressLint("ValidFragment")
public class LiveProductChaildFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView  mRecyclerView;
    private LiveChaildProductAdapter mAdapter;
    private int                      mPage = 1;



    public LiveProductChaildFragment(String id) {
        this.id = id;
    }

    private String id;
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
    private             HashMap<String, String> mParamsMaps;
    private void getProductListData() {
        showLoadDialog();
        mParamsMaps = new HashMap<>();
        mParamsMaps.put("rows", Constants.PAGE_SIZE + "");
        mParamsMaps.put("page", mPage + "");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category");

        mParamsMaps.put("filter[category_id]", id);
        mParamsMaps.put("filter[is_live]", "1");


        DataManager.getInstance().findGoodsLists(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
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
    private List<NewListItemDto> datas=new ArrayList<>();

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
        mAdapter = new LiveChaildProductAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                addGroupUser(mAdapter.getItem(position).id);
            }
        });

    }


    private void addGroupUser(String id) {
        showLoadDialog();
        DataManager.getInstance().addLiveProduct(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("添加成功");
                EventBus.getDefault().post(new LiveProduct());
                mPage = 1;
                getProductListData();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("添加成功");
                    EventBus.getDefault().post(new LiveProduct());
                    mPage = 1;
                    getProductListData();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        },id);
    }
}
