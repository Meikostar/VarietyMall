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
public class MyEarningsActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView               ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView                mTitleText;
    @BindView(R.id.tv_title_right)
    TextView                tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout          layoutTop;
    @BindView(R.id.tv_ye)
    TextView                mBalance;
    @BindView(R.id.tv_submit)
    TextView                tvSubmit;
    @BindView(R.id.tv_sy)
    TextView                tvSy;
    @BindView(R.id.tv_tx)
    TextView                tvTx;
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
        return R.layout.activity_my_earnings;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的钱包");
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
    private void loadData(boolean isLoad) {


        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
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
                    EmptyView emptyView = new EmptyView(MyEarningsActivity.this);

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


    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                if(personalInfoDto.wallet!=null&&personalInfoDto.wallet.data!=null)
                mBalance.setText(personalInfoDto.wallet.data.getMoney() + "");
              if(TextUtil.isNotEmpty(personalInfoDto.withdraw)){
                  tvTx.setText(personalInfoDto.withdraw);
              }  if(TextUtil.isNotEmpty(personalInfoDto.live_reward)){
                    tvSy.setText(personalInfoDto.live_reward);
                }


            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
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
            , R.id.tv_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
//            case R.id.tv_title_right://明细
//                gotoActivity(RechargeWithdrawDetailActivity.class);
//                break;
//            case R.id.tv_earnings_recharge://充值
//                gotoActivity(RechargeActivity.class);
//                break;
            case R.id.tv_submit://提现
                gotoActivity(WithdrawActivity.class);
                break;
        }
    }


}
