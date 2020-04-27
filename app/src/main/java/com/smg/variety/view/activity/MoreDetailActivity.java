package com.smg.variety.view.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.view.fragments.MoreDetailsFragment;
import com.smg.variety.view.widgets.autoview.ActionbarView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 我的钱包
 */
public class MoreDetailActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;

    @BindView(R.id.content_layout)
    LinearLayout contentLayout;


    private MoreDetailsFragment[] fragments        = new MoreDetailsFragment[1];
    private String                fragments_name[] = {"all", "income", "expenditure"};

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
        return R.layout.activity_more_detail;
    }
    private int index;
    @Override
    public void initView() {
        index=getIntent().getIntExtra("index",1);
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        if(index==1){
            actionbar.setTitle("今日推广收益");
        }else if(index==2){
            actionbar.setTitle("今日推荐收益");
        }else if(index==3){
            actionbar.setTitle("今日平台全部收益");
        }else if(index==4){
            actionbar.setTitle("今日全部收益");
        }

        switchFragment(index);
    }

    @Override
    public void initData() {

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
        if (fragments[0] == null) {
            isUpdate = true;
            fragments[0] = MoreDetailsFragment.newInstance(index);
            trx.add(R.id.content_layout, fragments[0], fragments_name[0]);
        }

        trx.show(fragments[0]).commit();

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
