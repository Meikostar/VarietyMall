package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.fragments.RunningWaterDetailsFragment;
import com.smg.variety.view.widgets.autoview.ActionbarView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 */
public class TxActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;

    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.tv_total)
    TextView     tvTotal;

    private RunningWaterDetailsFragment[] fragments        = new RunningWaterDetailsFragment[3];
    private String                        fragments_name[] = {"all", "income", "expenditure"};

    private ArrayList<CustomTabEntity> mSubTabEntities = new ArrayList<>();
    private int                        type;
    private int                        currentTabIndex = -1;

    private boolean isInit = false;
    private String  money;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        actionbar.setTitle("提现记录");

        switchFragment(0);
    }

    @Override
    public void initData() {
        getBalance();
        getUserInfo();
    }
    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {

                if(TextUtil.isNotEmpty(personalInfoDto.withdraw)){
                    tvTotal.setText(personalInfoDto.withdraw);
                }


            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }
    private void getBalance() {
        showLoadDialog();
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                dissLoadDialog();
                money = String.valueOf(balanceDto.getMoney());
                tvTotal.setText(money);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dissLoadDialog();
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
            }
        });
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
            fragments[index] = RunningWaterDetailsFragment.newInstance(index);
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
