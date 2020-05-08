package com.smg.variety.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BaseDto4;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.GitListAdapter;
import com.smg.variety.view.adapter.WalletAdapter;
import com.smg.variety.view.widgets.ConfigPasswordDialog;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 礼物
 */
public class MyGitListActivity extends BaseActivity {
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
        return R.layout.activity_my_gits;
    }

    @Override
    public void initView() {
        mTitleText.setText("礼物");
        tvTitleRight.setVisibility(View.VISIBLE);

    }

    private int            mCurrentPage;
    private GitListAdapter mAdapter;

    @Override
    public void initData() {

        mCurrentPage = Constants.PAGE_NUM;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new GitListAdapter();
        recyclerView.setAdapter(mAdapter);

    }
    private double figs;
    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getConfigs(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if(result.getData() != null&&result.getData().exchange!=null&&result.getData().exchange.gold_money!=null) {

                        figs=Double.valueOf(result.getData().exchange.gold_money);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private void loadData(boolean isLoad) {


        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();

        map.put("page", mCurrentPage + "");
        map.put("filter[type]",  "gift");
//        map.put("filter[reward_id]", "1");
        DataManager.getInstance().getGiftLog(new DefaultSingleObserver<HttpResult<List<IncomeDto>>>() {
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
                    EmptyView emptyView = new EmptyView(MyGitListActivity.this);
                    mAdapter.setNewData(null);
                    emptyView.setTvEmptyTip("暂无数据");

                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());


            }

            @Override
            public void onError(Throwable throwable) {


            }
        }, map);

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


   private int state=1;
    @Override
    protected void onResume() {
        super.onResume();


        loadData(true);
    }
    private String totalGold;

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
