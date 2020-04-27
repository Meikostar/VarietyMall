package com.smg.variety.view.activity;

import android.os.Bundle;
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
import com.smg.variety.view.fragments.OrderFragmentTg;
import com.smg.variety.view.fragments.ToFragmentListener;
import com.smg.variety.view.mainfragment.learn.CollectViewPagerAdapter;
import com.smg.variety.view.widgets.picker.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单
 */
public class OrderTgActivity extends BaseActivity {
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
    private String[]           titles = {"全部", "待付款", "已付款", "已完成", "无效"};
    private ToFragmentListener mFragmentListener1;
    private ToFragmentListener mFragmentListener2;
    private ToFragmentListener mFragmentListener3;
    private ToFragmentListener mFragmentListener4;
    private ToFragmentListener mFragmentListener5;
    private int                mCurrPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_tg;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的推广订单");
        mCurrPage = getIntent().getIntExtra("page", 0);
        one=new OrderFragmentTg(Constants.TREE_ORDER_TYPE_ALL);
        two=new OrderFragmentTg(Constants.TREE_ORDER_TYPE_PAYMENT);
        three=new OrderFragmentTg(Constants.TREE_ORDER_TYPE_DELIVERY);
        four=new OrderFragmentTg(Constants.TREE_ORDER_TYPE_RECEIVE);
        five=new OrderFragmentTg(Constants.TREE_ORDER_TYPE_EVALUATE);
        mFragmentListener1=one;
        mFragmentListener2=two;
        mFragmentListener3=three;
        mFragmentListener4=four;
        mFragmentListener5=five;

    }
    private OrderFragmentTg one;
    private OrderFragmentTg two;
    private OrderFragmentTg three;
    private OrderFragmentTg four;
    private OrderFragmentTg five;
    @Override
    public void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(one);
        fragments.add(two);
        fragments.add(three);
        fragments.add(four);
        fragments.add(five);

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
    private int yearStr;
    private int monthStr;
    private int dayStr;
    private int type;

    private void setBridthDayTv(Date date) {
        if (date != null) {
            String str = DateUtils.formatDate(date, "yyyy-MM-dd");


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //            LogUtil.d(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + getString(R.string.nxzdrq) + str);
            if (type == 0) {
                tvDateStar.setText(str );
            } else {
                tvDateEnd.setText(str );
            }


            yearStr = calendar.get(Calendar.YEAR);
            monthStr = calendar.get(Calendar.MONTH) + 1;
            dayStr = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            if (type == 0) {
                tvDateStar.setText("");
            } else {
                tvDateEnd.setText("");
            }
            yearStr = 0;
            monthStr = 0;
            dayStr = 0;
        }
    }

    private TimePickerView timePickerView;

    private void showTimeSelector() {

        if (timePickerView == null) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Integer.valueOf(DateUtils.formatToYear(System.currentTimeMillis())), Integer.valueOf(DateUtils.formatToMonth(System.currentTimeMillis())) == 0 ? 12 : Integer.valueOf(DateUtils.formatToMonth(System.currentTimeMillis())) - 1, Integer.valueOf(DateUtils.formatToDay(System.currentTimeMillis())), 0, 0);
            timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {

                    setBridthDayTv(date);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText("清空")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .setContentSize(20)//滚轮文字大小
                    .setTitleSize(18)//标题文字大小
                    .setTitleText("选择日期")//标题文字
                    .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                    .setTitleColor(getResources().getColor(R.color.my_color_333333))//标题文字颜色
                    .setSubmitColor(getResources().getColor(R.color.my_color_333333))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.my_color_333333))//取消按钮文字颜色
                    //                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                    //                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                    .setRange(1950, 2100)
                    .setDate(calendar)
                    .setLabel(getString(R.string.yea), getString(R.string.yeu), getString(R.string.riis), "时", "", "")
                    .setLineSpacingMultiplier(2f)
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false)
                    .build();
        }
        timePickerView.setState(type);
        timePickerView.show();
    }
    @Override
    public void initListener() {
        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrPage==0){
                    one.onTypeClick(TextUtil.isEmpty(tvDateStar.getText().toString())?"":tvDateStar.getText().toString(),TextUtil.isEmpty(tvDateEnd.getText().toString())?"":tvDateEnd.getText().toString());

                }else  if(mCurrPage==1){
                    two.onTypeClick(TextUtil.isEmpty(tvDateStar.getText().toString())?"":tvDateStar.getText().toString(),TextUtil.isEmpty(tvDateEnd.getText().toString())?"":tvDateEnd.getText().toString());

                }else  if(mCurrPage==2){
                    three.onTypeClick(TextUtil.isEmpty(tvDateStar.getText().toString())?"":tvDateStar.getText().toString(),TextUtil.isEmpty(tvDateEnd.getText().toString())?"":tvDateEnd.getText().toString());

                }else  if(mCurrPage==3){
                    four.onTypeClick(TextUtil.isEmpty(tvDateStar.getText().toString())?"":tvDateStar.getText().toString(),TextUtil.isEmpty(tvDateEnd.getText().toString())?"":tvDateEnd.getText().toString());

                }else  if(mCurrPage==4){
                    five.onTypeClick(TextUtil.isEmpty(tvDateStar.getText().toString())?"":tvDateStar.getText().toString(),TextUtil.isEmpty(tvDateEnd.getText().toString())?"":tvDateEnd.getText().toString());

                }
            }
        });
        tvDateStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                showTimeSelector();
            }
        });
        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                showTimeSelector();
            }
        });
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
