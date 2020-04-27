package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.view.fragments.PostFragment;

import com.smg.variety.view.fragments.SportFragment;
import com.smg.variety.view.widgets.autoview.ActionbarView;
import com.smg.variety.view.widgets.autoview.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 */
public class SuperListActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;
    @BindView(R.id.tv_all)
    TextView      tvAll;
    @BindView(R.id.tv_sl)
    TextView      tvSl;
    @BindView(R.id.tv_zc)
    TextView      tvZc;
    @BindView(R.id.content_layout)
    LinearLayout  contentLayout;
    private BaseFragment[] fragments        = new BaseFragment[3];
    private String         fragments_name[] = {"all", "income", "expenditure"};

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
        return R.layout.activity_super_list;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        actionbar.setTitle("攻略");

        initTab();
    }

    @Override
    public void initData() {

    }



    private void initTab() {
        String[] mSubTitles = {"新人上手", "进阶学习", "海报分享"};
        for (int i = 0; i < mSubTitles.length; i++) {
            mSubTabEntities.add(new TabEntity(mSubTitles[i], R.mipmap.icon_arrow, R.mipmap.icon_arrow));
        }
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPion(0);
            }
        });
        tvSl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPion(1);
            }
        });
        tvZc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPion(2);
            }
        });
        switchFragment(0);

    }

    public void selectPion(int poition) {
        switchFragment(poition);
        switch (poition) {
            case 0:

                tvAll.setTextColor(getResources().getColor(R.color.white));
                tvAll.setBackground(getResources().getDrawable(R.drawable.shape_radius_30blue));
                tvSl.setTextColor(getResources().getColor(R.color.color_66));
                tvSl.setBackground(getResources().getDrawable(R.drawable.shape_radius_30h));
                tvZc.setTextColor(getResources().getColor(R.color.color_66));
                tvZc.setBackground(getResources().getDrawable(R.drawable.shape_radius_30h));
                break;
            case 1:
                tvAll.setTextColor(getResources().getColor(R.color.color_66));
                tvAll.setBackground(getResources().getDrawable(R.drawable.shape_radius_30h));
                tvSl.setTextColor(getResources().getColor(R.color.white));
                tvSl.setBackground(getResources().getDrawable(R.drawable.shape_radius_30blue));
                tvZc.setTextColor(getResources().getColor(R.color.color_66));
                tvZc.setBackground(getResources().getDrawable(R.drawable.shape_radius_30h));
                break;
            case 2:
                tvAll.setTextColor(getResources().getColor(R.color.color_66));
                tvAll.setBackground(getResources().getDrawable(R.drawable.shape_radius_30h));
                tvSl.setTextColor(getResources().getColor(R.color.color_66));
                tvSl.setBackground(getResources().getDrawable(R.drawable.shape_radius_30h));
                tvZc.setTextColor(getResources().getColor(R.color.white));
                tvZc.setBackground(getResources().getDrawable(R.drawable.shape_radius_30blue));
                break;
        }
    }

    /**
     * 切换页面    新手上路：greenhand；进阶学习：learning；邀请海报：；
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
            if (index == 0) {
                fragments[index] = new SportFragment().newInstance("greenhand");
            } else if (index == 1) {
                fragments[index] = SportFragment.newInstance("learning");
            }  else  {
                fragments[index] = PostFragment.newInstance("poster");
            }

            trx.add(R.id.content_layout, fragments[index], fragments_name[index]);
        }

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

        }
    }

}
