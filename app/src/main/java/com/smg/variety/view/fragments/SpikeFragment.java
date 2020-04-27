package com.smg.variety.view.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.ShopCartDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.ProductListAdapter;
import com.smg.variety.view.widgets.RecyclerItemDecoration;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;
import com.smg.variety.view.widgets.dialog.LoadingDialog;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 购物车
 * Created by dahai on 2019/01/18.
 */

public class SpikeFragment extends BaseFragment implements LoadingDialog.LoadingListener {
    private static final String TAG = SpikeFragment.class.getSimpleName();


    Unbinder unbinder;
    @BindView(R.id.common_recycler)
    RecyclerView            recySpikeCart;
    @BindView(R.id.common_refresh)
    SuperSwipeRefreshLayout refreshLayout;

//    private ShopSpikeAdapter shopSpikeAdapter;
    SwipeRefreshLayoutUtil swipeRefreshLayoutUtil;

    private Set<ShopCartDto> selectList = new HashSet<>();//被选中的资源
    public String            id;

    public void setId(String id) {
        this.id = id;
    }
    public int state;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_spike_layout;
    }

    private LoadingDialog loadingDialog;
    private ProductListAdapter mAdapter;
    @Override
    protected void initView() {
        loadingDialog = LoadingDialog.show(getActivity());
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCallbackListener(this);

        recySpikeCart.addItemDecoration(new RecyclerItemDecoration(10, 2));
        recySpikeCart.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mAdapter = new ProductListAdapter();
        recySpikeCart.setAdapter(mAdapter);



    }

    private List<NewListItemDto>       datas = new ArrayList<>();
    private HashMap<String, String> mParamsMaps;
    private             int                     mCurrentPage = 1;
    @Override
    protected void initData() {

        mParamsMaps = new HashMap<>();
        getProductListData();
//
//        shopSpikeAdapter = new ShopSpikeAdapter(getActivity(),null);
//        recySpikeCart.setAdapter(shopSpikeAdapter);
//        shopSpikeAdapter.setState(state);
//        findGoodsSedKill(true,currentPage,20);

    }

    private void getProductListData() {
        showLoadDialog();
        mParamsMaps.put("page", currentPage + "");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category");
        mParamsMaps.put("filter[is_new]", 1+"");

        if (TextUtil.isNotEmpty(id)&&!id.equals("-1")) {
            mParamsMaps.put("filter[category_id]", id);
        } else {
            mParamsMaps.remove("filter[category_id]");
        }


        DataManager.getInstance().findGoodsList(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> data) {
                dissLoadDialog();

                if (null != data.getData() && data.getData().size() > 0) {

                    if (currentPage == 1) {
                        datas.clear();
                        datas.addAll(data.getData());
                        mAdapter.setNewData(data.getData());
                        refreshLayout.setRefreshing(false);
                    } else {
                        datas.addAll(data.getData());
                        mAdapter.addData(data.getData());
                        refreshLayout.setLoadMore(false);
                    }


                } else {
                    EmptyView emptyView = new EmptyView(getActivity());
                    emptyView.setTvEmptyTip("暂无商品数据");
                    mAdapter.setEmptyView(emptyView);


                }
                swipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, data.getMeta().getPagination().getTotal());
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (mCurrentPage == 1) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 滚动监听
     *
     * @param rows 每页加载行数
     */
    private int currentPage=1;
    private void setScrollListener(int rows) {
        swipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        swipeRefreshLayoutUtil.setBgColor(R.color.my_color_greenss);

//        swipeRefreshLayout.setEnablePull(false);
        swipeRefreshLayoutUtil.setSwipeRefreshView(refreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                currentPage = Constants.PAGE_NUM;
                swipeRefreshLayoutUtil.setCanLoadMore(true);
                getProductListData();

            }

            @Override
            public void onLoadMore() {
                swipeRefreshLayoutUtil.setCanLoadMore(true);
                currentPage++;
                getProductListData();

            }
        },R.color.my_color_greenss);
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }

    @Override
    public void onResume() {
        selectList.clear();


        super.onResume();
    }
    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (refreshLayout != null) {
            if(loadingDialog!=null&&refreshLayout!=null){
                loadingDialog.cancelDialog();
                refreshLayout.setRefreshing(false);
                refreshLayout.setLoadMore(false);
            }

        }
    }
    @Override
    protected void initListener() {


        setScrollListener(20);
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
    public void onLoadCompleted(int level) {
        if (level == 5) {
            loadingDialog.cancelDialog();

        }
    }
}
