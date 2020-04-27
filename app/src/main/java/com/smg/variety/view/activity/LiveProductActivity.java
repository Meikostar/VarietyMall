package com.smg.variety.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.view.fragments.AttentionCommunityFragment;
import com.smg.variety.view.fragments.AttentionStoreFragment;
import com.smg.variety.view.fragments.LiveMoreFragment;
import com.smg.variety.view.fragments.LiveProductFragment;
import com.smg.variety.view.mainfragment.learn.CollectViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关注
 */
public class LiveProductActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager        mViewPager;
    private String[] titles = {"我的直播",  "直播商品"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_live_product;
    }

    @Override
    public void initView() {
        mTitleText.setText("直播商品");

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new LiveProductFragment());
        fragments.add(new LiveMoreFragment());
        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

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
