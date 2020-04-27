package com.smg.variety.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.InviteFriendBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.InviteFiendAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {
    @BindView(R.id.recy_friend)
    RecyclerView            recyFriend;
    @BindView(R.id.tv_title_text)
    TextView                tvTitleText;
    @BindView(R.id.refresh)
    SuperSwipeRefreshLayout refreshLayout;

    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;
    InviteFiendAdapter mAdapter;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void initView() {
        tvTitleText.setText("我的推广会员");
        recyFriend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new InviteFiendAdapter();
        recyFriend.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        loadData(true);
    }

    private void loadData(boolean isLoad) {
        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mCurrentPage + "");
        DataManager.getInstance().getUserChildren(new DefaultSingleObserver<HttpResult<List<InviteFriendBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<InviteFriendBean>> result) {
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
                    EmptyView emptyView = new EmptyView(InviteFriendsActivity.this);

                    emptyView.setTvEmptyTip("暂无数据");

                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta()!=null?result.getMeta().getPagination().getTotal():0);

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
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

    @OnClick({R.id.iv_title_back})
    public void toClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }

    }
}
