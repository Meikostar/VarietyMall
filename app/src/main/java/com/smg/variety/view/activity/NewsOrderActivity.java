package com.smg.variety.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.ArticleTypeBean;
import com.smg.variety.view.adapter.MyViewPagerAdapter;
import com.smg.variety.view.fragments.MembersFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 政令法规
 */
public class NewsOrderActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView         mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager        mViewPager;
    @Override
    public int getLayoutId() {
        return R.layout.activity_tablayout_layout_width95;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的会员");
    }

    @Override
    public void initData() {
        ArticleTypeBean articleTypeBean = new ArticleTypeBean();
        articleTypeBean.title="用户注册";
        articleTypeBean.id="0";
        ArticleTypeBean articleTypeBean1 = new ArticleTypeBean();
        articleTypeBean1.title="掌柜";
        articleTypeBean1.id="1";
        ArticleTypeBean articleTypeBean2 = new ArticleTypeBean();
        articleTypeBean2.title="导师";
        articleTypeBean2.id="2";
        beanList.add(articleTypeBean);
        beanList.add(articleTypeBean1);
        beanList.add(articleTypeBean2);
        setData();
    }

    @Override
    public void initListener() {

    }


    private List<ArticleTypeBean> beanList =new ArrayList<>();
    private void setData() {
        if (beanList == null || beanList.size() == 0) {
            return;
        }
        String[] titles = new String[beanList.size()];
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            titles[i] = beanList.get(i).title;
            fragments.add(MembersFragment.newInstance(beanList.get(i).id));
        }
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);
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
