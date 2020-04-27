package com.smg.variety.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.qiniu.chatroom.ChatroomKit;
import com.smg.variety.utils.DownLoadServerice;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.mainfragment.LearnFragment;
import com.smg.variety.view.mainfragment.community.TopicPublishActivity;
import com.smg.variety.view.widgets.autoview.AutoViewPager;
import com.smg.variety.view.widgets.updatadialog.UpdataCallback;
import com.smg.variety.view.widgets.updatadialog.UpdataDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;


public class ShopCarActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener {
    private static final String TAG = ShopCarActivity.class.getSimpleName();

    @BindView(R.id.main_viewpager)
    AutoViewPager  mViewPager;


    private List<Fragment> fragments = new ArrayList<Fragment>();
    //会话列表的fragment


    public static final String APP_EXIT = "app_exit";//退出应用


    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_car;
    }

    private             int    page_index = 0;
    public static final String PAGE_INDEX = "page_index";//调用者传递的名字

    @Override
    public void initView() {

//        checkPermissioin();
        StatusBarUtils.StatusBarLightMode(this);

        live=getIntent().getIntExtra("live",live);
        authorId=getIntent().getStringExtra("authorId");
        initMainViewPager();
        mViewPager.setCurrentItem(0, false);

    }


   private int live;
   private String authorId;

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void initMainViewPager() {
        LearnFragment learnFragment = new LearnFragment();
        learnFragment.setShow(true);
        learnFragment.setLive(live,TextUtil.isNotEmpty(authorId)?authorId:"");
        fragments.add(learnFragment);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setScanScroll(false);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_PUBLISH && data != null) {
            ArrayList<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.INTENT_RESULT_DATA, resultList);
                gotoActivity(TopicPublishActivity.class, false, bundle, Constants.INTENT_ADD_FRIEND);
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.HOME_REQUEST_CODE_SCAN && data != null) {

        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CHAT_REQUEST_CODE_SCAN && data != null) {

        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(2, false);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

