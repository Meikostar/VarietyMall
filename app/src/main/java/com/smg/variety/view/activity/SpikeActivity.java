package com.smg.variety.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.FragmentViewPagerAdapter;
import com.smg.variety.view.adapter.TestAdapter;
import com.smg.variety.view.fragments.SpikeFragment;
import com.smg.variety.view.widgets.AutoLocateHorizontalView;
import com.smg.variety.view.widgets.NoScrollViewPager;
import com.smg.variety.view.widgets.autoview.ActionbarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class SpikeActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView            customActionBar;
    @BindView(R.id.auto_scroll)
    AutoLocateHorizontalView autoScroll;

    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    private int currentPage = Constants.PAGE_NUM;

    @Override
    public void initListener() {

    }
   private int state;
    @Override
    public int getLayoutId() {
        return R.layout.ui_spike_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private TestAdapter testAdapter;
    private boolean     isShow;
    private BaseDto    spikeDto;
    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        actionbar.setTitle("每日上新");
        actionbar.setTitleColor(R.color.my_color_212121);
        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });
        spikeDto= (BaseDto) getIntent().getSerializableExtra("data");
        state=getIntent().getIntExtra("state",0);
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(isFirst){
                    autoScroll.moveToPosition(position);
                }else {
                    isFirst=true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        StatusBarUtils.StatusBarLightMode(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        autoScroll.setHasFixedSize(true);
        autoScroll.setLayoutManager(linearLayoutManager);
        autoScroll.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                viewpagerMain.setCurrentItem(pos, false);


            }
        });

        testAdapter = new TestAdapter();
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
        testAdapter.setItemClick(new TestAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, BannerDto data) {
                autoScroll.moveToPosition(poition);
            }
        });
    }


    private boolean isFirst;
    @Override
    public void initData() {

        getHomeCategorie();



    }
    private List<BannerDto> data=new ArrayList<>();
    private void getHomeCategorie() {
        //showLoadDialog();
        DataManager.getInstance().banCategorie(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();
                data.clear();
                if (result != null&&result.getData()!=null) {
                    BannerDto bannerDto = new BannerDto();
                    bannerDto.title="全部";
                    bannerDto.id="-1";
                    data.add(bannerDto);
                    data.addAll(result.getData());

                    BannerDto bannerDto1 = new BannerDto();
                    bannerDto1.title="";
                    bannerDto1.id="-2";
                    data.add(bannerDto1);
                    testAdapter.setDatas(data);
                    testAdapter.notifyDataSetChanged();
                    initFragMents(state);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();

            }
        });
    }
    private String[]                 titles;
    private List<Fragment>           list_productfragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private void initFragMents(int poistion) {
        list_productfragment = new ArrayList<>();
       for (BannerDto dataDto:data){
           SpikeFragment spikeFragment = new SpikeFragment();
           spikeFragment.setId(dataDto.id);
           list_productfragment.add(spikeFragment);
       }

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), list_productfragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(data.size() - 1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(poistion);

    }


}
