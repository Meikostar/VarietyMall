package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.fragments.RunningWaterDetailsFragment;
import com.smg.variety.view.fragments.StoreWaterDetailsFragment;
import com.smg.variety.view.widgets.autoview.ActionbarView;
import com.smg.variety.view.widgets.autoview.TabEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 */
public class SthoreWalletActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;

    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.currency_tv)
    TextView     currencyTv;
    @BindView(R.id.total_tv)
    TextView     totalTv;
    @BindView(R.id.tv_one)
    TextView     tvOne;
    @BindView(R.id.tv_two)
    TextView     tvTwo;
    @BindView(R.id.tv_three)
    TextView     tvThree;
    @BindView(R.id.ll_cz)
    LinearLayout llCz;
    @BindView(R.id.ll_tx)
    LinearLayout llTx;
    @BindView(R.id.tv_ch1)
    TextView     tvCh1;
    @BindView(R.id.tv_ch2)
    TextView     tvCh2;
    @BindView(R.id.tv_ch3)
    TextView     tvCh3;

    private StoreWaterDetailsFragment[] fragments        = new StoreWaterDetailsFragment[3];
    private String                        fragments_name[] = {"all", "income", "expenditure"};

    private ArrayList<CustomTabEntity> mSubTabEntities = new ArrayList<>();
    private int                        type;
    private int                        currentTabIndex = -1;

    private boolean isInit = false;
    private String  money;

    @Override
    public void initListener() {
        bindClickEvent(llCz, () -> {
            startActivityForResult(new Intent(this, RechargeActivity.class), 0x101);
        });
        bindClickEvent(llTx, () -> {
            startActivityForResult(new Intent(this, BalanceWithdrawalActivity.class), 0x101);

            //            if (MainActivity.mPersonalInfoDto.getUsacarry() <= 0) {
            //                ToastUtil.showToast(getString(R.string.ktxjebz));
            //            } else
        });
        tvCh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTab(1);
            }
        });
        tvCh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTab(2);
            }
        });
        tvCh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTab(3);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_wallet;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        actionbar.setTitle("财务管理");
        Bundle bundle = getIntent().getExtras();

        initTab(1);
    }

    @Override
    public void initData() {
        getBalance();
    }
    private void getBalance() {

        DataManager.getInstance().getStorelog(new DefaultSingleObserver<HttpResult<IncomeDto>>() {
            @Override
            public void onSuccess(HttpResult<IncomeDto> httpResult) {
                if(httpResult.getData()!=null){
                    tvOne.setText(httpResult.getData().all_income);
                    tvTwo.setText(httpResult.getData().month_income);
                    tvThree.setText(httpResult.getData().day_income);
                    totalTv.setText(httpResult.getData().money);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));

            }
        });
    }


    private void initTab(int type) {
      if(type==1){
          tvCh1.setBackgroundColor(getResources().getColor(R.color.my_color_blue));
          tvCh2.setBackgroundColor(getResources().getColor(R.color.white));
          tvCh3.setBackgroundColor(getResources().getColor(R.color.white));
          tvCh1.setTextColor(getResources().getColor(R.color.white));
          tvCh2.setTextColor(getResources().getColor(R.color.my_color_212121));
          tvCh3.setTextColor(getResources().getColor(R.color.my_color_212121));
          switchFragment(0);
      }else  if(type==2){
          tvCh1.setBackgroundColor(getResources().getColor(R.color.white));
          tvCh2.setBackgroundColor(getResources().getColor(R.color.my_color_blue));
          tvCh3.setBackgroundColor(getResources().getColor(R.color.white));
          tvCh1.setTextColor(getResources().getColor(R.color.my_color_212121));
          tvCh2.setTextColor(getResources().getColor(R.color.white));
          tvCh3.setTextColor(getResources().getColor(R.color.my_color_212121));
          switchFragment(1);
      }else  if(type==3){
          tvCh1.setBackgroundColor(getResources().getColor(R.color.white));
          tvCh2.setBackgroundColor(getResources().getColor(R.color.white));
          tvCh3.setBackgroundColor(getResources().getColor(R.color.my_color_blue));
          tvCh1.setTextColor(getResources().getColor(R.color.my_color_212121));
          tvCh2.setTextColor(getResources().getColor(R.color.my_color_212121));
          tvCh3.setTextColor(getResources().getColor(R.color.white));
          switchFragment(2);
      }

    }

    /**
     * 切换页面
     *
     * @param index
     */
    private void switchFragment(int index) {
        if (currentTabIndex == index) {
            return;
        }
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        boolean isUpdate = false;
        if (fragments[index] == null) {
            isUpdate = true;
            fragments[index] = StoreWaterDetailsFragment.newInstance(index);
            trx.add(R.id.content_layout, fragments[index], fragments_name[index]);
        }
        //        if (index == 0) {
        //            fragments[index].setType("");
        //        } else if (index == 1) {
        //            fragments[index].setType("1");
        //        } else if (index == 2) {
        //            fragments[index].setType("2");
        //        } else if (index == 3) {
        //            fragments[index].setType("3");
        //        } else if (index == 4) {
        //            fragments[index].setType("4");
        //        }
        if (currentTabIndex != -1) {
            trx.hide(fragments[currentTabIndex]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
        //        if (isUpdate) {
        //            fragments[index].loadData(true);
        //        }
    }

    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //        if (isInit && fragments[currentTabIndex] != null) {
    //            fragments[currentTabIndex].loadData(true);
    //        }
    //        isInit = true;
    //    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x101 && resultCode == -1) {
            if (fragments[currentTabIndex] != null) {
                fragments[currentTabIndex].RefreshData();
            }
        }
    }

}
