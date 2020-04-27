package com.smg.variety.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.RoomUserBean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 直播房间用户列表
 */
public class RoomUserListActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    String roomId;
    private int mPage = 1;
    private RoomUserListAdapter mAdapter;

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                liveChatter();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                liveChatter();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room_user_list;
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new RoomUserListAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mTitleText.setText("用户列表");
        roomId = getIntent().getStringExtra("room_id");
        mRefreshLayout.autoRefresh();
    }

    public void liveChatter() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("page", mPage + "");
        map.put("include","user");
        DataManager.getInstance().liveChatter(new DefaultSingleObserver<HttpResult<List<RoomUserBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<RoomUserBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, map);
    }

    private void setData(HttpResult<List<RoomUserBean>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                //mAdapter.setEmptyView(new EmptyView(CollectionActivity.this));
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

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    class RoomUserListAdapter extends BaseQuickAdapter<RoomUserBean, BaseViewHolder> {

        public RoomUserListAdapter() {
            super(R.layout.activity_room_user_item, null);
        }

        @Override
        protected void convert(BaseViewHolder helper, RoomUserBean item) {
            if (item.user != null && item.user.getData() != null){
                helper.setText(R.id.tv_id,"ID:"+item.user.getData().name)
                        .setText(R.id.tv_time,item.user.getData().phone);
            }else {
                helper.setText(R.id.tv_id,"ID:")
                        .setText(R.id.tv_time,"");
            }
            TextView tvState = helper.getView(R.id.tv_state);
            View ivState = helper.getView(R.id.iv_state);
            GlideUtils.getInstances().loadUserRoundImg(mContext,helper.getView(R.id.iv_img),item.user.getData().avatar);
            if (item.user.getData().level == 0) {
                tvState.setText("注册会员");
                ivState.setVisibility(View.GONE);
            } else if (item.user.getData().level == 1) {
                tvState.setText("掌柜");
                ivState.setVisibility(View.VISIBLE);
            } else if (item.user.getData().level == 2) {
                tvState.setText("导师");
                ivState.setVisibility(View.GONE);
            }
        }
    }
}
