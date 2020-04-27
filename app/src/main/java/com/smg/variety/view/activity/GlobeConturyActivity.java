package com.smg.variety.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.GlobeAdapter;
import com.smg.variety.view.widgets.RecyclerItemDecoration;
import com.smg.variety.view.widgets.autoview.ActionbarView;
import com.smg.variety.view.widgets.autoview.CustomView;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 *
 */
public class GlobeConturyActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView      customActionBar;
    @BindView(R.id.consume_push_recy)
    RecyclerView       consumePushRecy;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.consume_scrollView)
    CustomView         consumeScrollView;

    @Override
    public void initListener() {

    }

    private int state;

    @Override
    public int getLayoutId() {
        return R.layout.ui_contury_globe_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;

    private boolean              isShow;
    private BaseDto              spikeDto;

    private GlobeAdapter   mProcutAdapter;
    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();

    @Override
    public void initView() {
        id=getIntent().getStringExtra("id");

        actionbar.setImgStatusBar(R.color.my_color_white);

        actionbar.setTitle("全球大牌");


        getConturyProducts();

        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };


        mProcutAdapter = new GlobeAdapter(goodsLists, this);
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(3, 3));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mProcutAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getConturyProducts();
            }
        });


        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getConturyProducts();
            }
        });

    }


    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mProcutAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mProcutAdapter.setEmptyView(new EmptyView(GlobeConturyActivity.this));
            }
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableRefresh(true);
            mProcutAdapter.addData(httpResult.getData());
            mProcutAdapter.notifyDataSetChanged();
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }


    }
    private int                    mPage        = 1;
    private String id;
    private String title;

    private void getConturyProducts() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();

        map.put("page", mPage + "");
        map.put("include", "category");

        DataManager.getInstance().getConturyProduct(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                //dissLoadDialog();
                dissLoadDialog();
                setData(result);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    @Override
    public void initData() {

    }


}
