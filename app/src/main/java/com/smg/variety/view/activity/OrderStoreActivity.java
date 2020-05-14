package com.smg.variety.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.Constants;
import com.smg.variety.utils.DateUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.fragments.OrderFragmentStore;
import com.smg.variety.view.fragments.OrderFragmentTg;
import com.smg.variety.view.fragments.ToFragmentListener;
import com.smg.variety.view.mainfragment.learn.CollectViewPagerAdapter;
import com.smg.variety.view.widgets.picker.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单
 */
public class OrderStoreActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView         mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.vp_action)
    ViewPager        mViewPager;
    @BindView(R.id.iv_title_back)
    ImageView        ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView         tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout   layoutTop;
    @BindView(R.id.tv_date_star)
    TextView         tvDateStar;
    @BindView(R.id.tv_date_end)
    TextView         tvDateEnd;
    @BindView(R.id.tv_find)
    TextView         tvFind;
    private String[]           titles = {"全部"};

    private int                mCurrPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_state;
    }

    @Override
    public void initView() {
        mTitleText.setText("订单管理");
        mCurrPage = getIntent().getIntExtra("page", 0);
        one=new OrderFragmentStore(Constants.TREE_ORDER_TYPE_ALL);


    }
    private OrderFragmentStore one;
    @Override
    public void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(one);

        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mCurrPage=position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mViewPager.setCurrentItem(mCurrPage);

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
