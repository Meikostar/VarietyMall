package com.smg.variety.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.view.fragments.Incomeragment;
import com.smg.variety.view.mainfragment.ShopClassFragment;
import com.smg.variety.view.widgets.autoview.TabEntity;

import java.util.ArrayList;

public class MenberShareActivity extends BaseActivity {
    private static final String TAG = MenberShareActivity.class.getSimpleName();

    private String[]                   mSubTitles      = {"全部"};
    private ArrayList<CustomTabEntity> mSubTabEntities = new ArrayList<>();
    ShopClassFragment fragments[] = new ShopClassFragment[3];
    private String fragments_name[] = {"all", "inCome", "outCome"};

    int currentTabIndex = -1;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_income_layout;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        actionbar.setTitle("收入流水");
        actionbar.setVisibility(View.GONE);
        StatusBarUtils.StatusBarLightMode(this);
        initTab();
    }

    @Override
    public void initData() {

        switchFragment(0);
    }

    private void initTab() {
        for (int i = 0; i < mSubTitles.length; i++) {
            mSubTabEntities.add(new TabEntity(mSubTitles[i], R.mipmap.icon_arrow, R.mipmap.icon_arrow));
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
        if (fragments[index] == null) {
            fragments[index] = new ShopClassFragment();
            trx.add(R.id.content_layout, fragments[index], fragments_name[index]);
        }

        if (currentTabIndex != -1) {
            trx.hide(fragments[currentTabIndex]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
    }




}
