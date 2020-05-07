package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BalanceDto;
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
import com.smg.variety.qiniu.live.utils.Config;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.utils.DownLoadServerice;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.adapter.WalletAdapter;
import com.smg.variety.view.widgets.ConfigPasswordDialog;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;
import com.smg.variety.view.widgets.updatadialog.UpdataCallback;
import com.smg.variety.view.widgets.updatadialog.UpdataDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.tv_dh)
    TextView                tvDh;
    @BindView(R.id.tv_state1)
    TextView                tvState1;
    @BindView(R.id.tv_state2)
    TextView                tvState2;

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

    private int           mCurrentPage;
    private WalletAdapter mAdapter;

    @Override
    public void initData() {

        mCurrentPage = Constants.PAGE_NUM;
        setChoose(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new WalletAdapter();
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
    public void exchanges(String pay_password,String total ) {

        Map<String, String> mParamsMaps = new HashMap<>();
        mParamsMaps.put("pay_password", pay_password);
        mParamsMaps.put("from" ,"gold");
        mParamsMaps.put("to",  "money");
        mParamsMaps.put("total", total + "");

        DataManager.getInstance().Updateexchange(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> data) {

                loadData(false);
                getGold();
                getUserInfo();
                DialogUtils.showChangeSuccessDialog(MyEarningsActivity.this, Long.valueOf(total)*figs+"", new DialogUtils.OnClickDialogListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

            }

            @Override
            public void onError(Throwable throwable) {

                if (ApiException.getInstance().isSuccess()) {
                    loadData(false);
                    getGold();
                    getUserInfo();
                    DialogUtils.showChangeSuccessDialog(MyEarningsActivity.this, Long.valueOf(total)*figs+"", new DialogUtils.OnClickDialogListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, mParamsMaps);

    }
    private void loadData(boolean isLoad) {


        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
        if(state==2){
            map.put("type","exchange");
            map.put("money_type","gold");
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
                    EmptyView emptyView = new EmptyView(MyEarningsActivity.this);
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

    public void setChoose(int state){
        if(state==1){
            tvState1.setTextColor(getResources().getColor(R.color.my_color_111));
            tvState2.setTextColor(getResources().getColor(R.color.my_color_535353));
        }else {
            tvState1.setTextColor(getResources().getColor(R.color.my_color_535353));
            tvState2.setTextColor(getResources().getColor(R.color.my_color_111));
        }
    }
    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                if (personalInfoDto.wallet != null && personalInfoDto.wallet.data != null)
                    mBalance.setText(personalInfoDto.wallet.data.getMoney() + "");
                if (TextUtil.isNotEmpty(personalInfoDto.withdraw)) {

                }
                if (TextUtil.isNotEmpty(personalInfoDto.live_reward)) {
                    tvSy.setText(personalInfoDto.live_reward);
                }


            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }
   private int state=1;
    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        getGold();
        getConfigs();
        loadData(true);
    }
    private String totalGold;


    private void getGold() {
        showLoadDialog();
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                dissLoadDialog();
                if (balanceDto.gold != null) {
                    totalGold=balanceDto.gold;
                    tvTx.setText(totalGold+"");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dissLoadDialog();
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
            }
        });

    }

    @Override
    public void initListener() {
        tvState1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=1;
                setChoose(1);
                loadData(true);
            }
        });
        tvState2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state=2;
                setChoose(2);
                loadData(true);
            }
        });
        tvDh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showChangeDialog(MyEarningsActivity.this, totalGold,figs, new DialogUtils.OnClickDialogCoutsListener() {
                    @Override
                    public void showCouts(String one, String two) {
                        new ConfigPasswordDialog(MyEarningsActivity.this, new ConfigPasswordDialog.ConfigPasswordListener() {
                            @Override
                            public void callbackPassword(String password) {
                                exchanges(password,one);
                            }
                        }).show();
                    }
                });
            }
        });
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
