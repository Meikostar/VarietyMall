package com.smg.variety.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.view.fragments.CommunityMessageFragment;
import com.smg.variety.view.fragments.GoodCommentFragment;
import com.smg.variety.view.mainfragment.learn.CollectViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论中心
 */
public class CommentCenterActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager  mViewPager;
    private String[] titles = {"商品评价"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment_center;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的评价");
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new GoodCommentFragment());
        mTabLayout.setVisibility(View.GONE);
//        fragments.add(new CommunityMessageFragment());
        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.setVisibility(View.GONE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
