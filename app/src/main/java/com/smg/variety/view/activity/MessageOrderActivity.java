package com.smg.variety.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.NoticeDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.OrderNotificalAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息中心
 */
public class MessageOrderActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView                mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView               ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView                tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout          layoutTop;
    @BindView(R.id.recy_my_comment)
    RecyclerView            rvList;
    @BindView(R.id.refresh)
    SuperSwipeRefreshLayout refreshLayout;

    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;
    private OrderNotificalAdapter  mAdapter;
    private List<NoticeDto>        data = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_xt;
    }

    @Override
    public void initView() {
        mTitleText.setText("订单通知");
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new OrderNotificalAdapter(this);
        rvList.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        loadData(true);
    }

    @Override
    public void initListener() {
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
    private void loadData(boolean isLoad) {
        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mCurrentPage + "");
        map.put("filter[type]","SMG\\Mall\\Notifications\\OrderNotify");
        DataManager.getInstance().getNoticeList(new DefaultSingleObserver<HttpResult<List<NoticeDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NoticeDto>> result) {
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
                    EmptyView emptyView = new EmptyView(MessageOrderActivity.this);

                    emptyView.setTvEmptyTip("暂无通知");

                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());


            }

            @Override
            public void onError(Throwable throwable) {


            }
        },map);


    }
    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
            refreshLayout.setLoadMore(false);
        }
    }



    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }


}
