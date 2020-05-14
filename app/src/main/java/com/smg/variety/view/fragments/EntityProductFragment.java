package com.smg.variety.view.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.smg.variety.bean.ExtDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.LiveProduct;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.StoreOrderDetailActivity;
import com.smg.variety.view.adapter.EntityProductAdapter;
import com.smg.variety.view.adapter.LiveProductAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.dialog.BaseDialog;
import com.smg.variety.view.widgets.dialog.ConfirmDialog;
import com.smg.variety.view.widgets.dialog.StokcInputDialog;

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
public class EntityProductFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    private EntityProductAdapter mAdapter;
    private int                  mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_entity_product;
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

    private Map<String, String> mParamsMaps;

    private void getProductListData() {
        showLoadDialog();
        mParamsMaps = new HashMap<>();
        mParamsMaps.put("rows", Constants.PAGE_SIZE + "");
        mParamsMaps.put("page", mPage + "");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category,attrs");

        mParamsMaps.put("filter[shop_id]", ShareUtil.getInstance().get(Constants.USER_ID));

        DataManager.getInstance().findSellersGoodsLists(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
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
        if (httpResult == null || httpResult.getData() == null) {
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

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new EntityProductAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NewListItemDto item = mAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_del:

                        ConfirmDialog confirmDialog = new ConfirmDialog(getActivity());
                        confirmDialog.setCancelText("取消");
                        confirmDialog.setTitle("温馨提示");
                        confirmDialog.setMessage("是否" + (item.on_sale ? "下架" : "上架"));
                        confirmDialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                            @Override
                            public void onYesClick() {
                                confirmDialog.dismiss();
                                if(item.on_sale ){
                                    on_sale="0";
                                }else {
                                    on_sale="1";
                                }
                                editProducts(item.id,0);

                            }
                        });
                        confirmDialog.show();
                        break;
                    case R.id.iv_bj:

                        new StokcInputDialog(getActivity(), new StokcInputDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirmClick(String pwd) {
                                if(TextUtil.isNotEmpty(pwd)){
                                    stock=pwd;
                                    editProducts(item.id,1);
                                }

                            }
                        }).show();
                        break;
                    case R.id.ll_bg:


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

    private String stock;
    private String on_sale;

    private void editProducts(String id, int type) {

        Map<String, Object> map = new HashMap<>();
        if (type == 1) {
            map.put("stock", stock);
        } else {
            map.put("on_sale", on_sale);
        }


        showLoadDialog();
        DataManager.getInstance().editProducts(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                ToastUtil.showToast("操作成功");
                mPage = 1;
                getProductListData();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("操作成功");
                    mPage = 1;
                    getProductListData();
                } else {
                    //                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, id, map);
    }
}
