package com.smg.variety.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.WalletAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的收益
 */
public class GlodMoneyDetailActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView               ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView                mTitleText;
    @BindView(R.id.tv_title_right)
    TextView                tvTitleRight;

    @BindView(R.id.recy_wallet)
    RecyclerView            recyclerView;
    @BindView(R.id.refresh)
    SuperSwipeRefreshLayout swipeRefreshLayout;

    //    @BindView(R.id.tv_title_text)
    //    TextView mTitleText;
    //    @BindView(R.id.tv_title_right)
    //    TextView mTitleRight;
    //    /**余额*/
    //    @BindView(R.id.tv_earnings_balance)
    //    TextView mBalance;
    @Override
    public int getLayoutId() {
        return R.layout.activity_glod_money;
    }

    @Override
    public void initView() {
        state=getIntent().getIntExtra("state",0);
        if(state==0){
            mTitleText.setText("金币收益明细");
        }else {
            mTitleText.setText("现金收益明细");
        }

        tvTitleRight.setVisibility(View.VISIBLE);

    }
    private int mCurrentPage;
    private WalletAdapter mAdapter;
    @Override
    public void initData() {

        mCurrentPage = Constants.PAGE_NUM;

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new WalletAdapter();
        recyclerView.setAdapter(mAdapter);

    }
    private int state;
    private void loadData(boolean isLoad) {


        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "task_reward");
        if(state==0){
            map.put("money_type", "gold");
        }else {
            map.put("money_type", "money");
        }

        map.put("page", mCurrentPage + "");
        DataManager.getInstance().getUserlog(new DefaultSingleObserver<HttpResult<List<IncomeDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<IncomeDto>> result) {
                dissLoadDialog();
                if (null != result.getData() && result.getData().size() > 0) {

                    if (mCurrentPage == 1) {
                        mAdapter.setNewData(result.getData());

                    } else {

                        mAdapter.addData(result.getData());

                    }

                } else {
                    EmptyView emptyView = new EmptyView(GlodMoneyDetailActivity.this);

                    emptyView.setTvEmptyTip("暂无数据");

                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());


            }

            @Override
            public void onError(Throwable throwable) {


            }
        },map);

    }



    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;


    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setLoadMore(false);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData(true);
    }

    @Override
    public void initListener() {
        mSwipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        mSwipeRefreshLayoutUtil.setSwipeRefreshView(swipeRefreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
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
