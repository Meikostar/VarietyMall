package com.smg.variety.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.utils.InputUtil;
import com.smg.variety.eventbus.LiveProduct;
import com.smg.variety.eventbus.LiveSearch;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.fragments.AttentionLiveFragment;
import com.smg.variety.view.fragments.AttentionLiveRoomFragment;
import com.smg.variety.view.mainfragment.learn.CollectViewPagerAdapter;
import com.smg.variety.view.widgets.autoview.ClearEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关注
 */
public class SearchLiveActivity extends BaseActivity {

    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager        mViewPager;
    @BindView(R.id.actionbar_back)
    ImageView        actionbar_back;
    @BindView(R.id.acb_status_bar)
    ImageView        acbStatusBar;
    @BindView(R.id.et_search)
    ClearEditText    etSearch;
    @BindView(R.id.tv_search)
    TextView         tvSearch;

    private String[] titles = {"直播", "直播间"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_live;
    }

    private int poition;

    @Override
    public void initView() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new AttentionLiveFragment());
        fragments.add(new AttentionLiveRoomFragment());
        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(position==0){
                    etSearch.setHint("输入昵称/账号");
                }else {
                    etSearch.setHint("输入直播标题/直播房间号");
                }
                poition=position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtil.isEmpty(s.toString())){
                    LiveSearch liveProduct = new LiveSearch();
                    liveProduct.content="";
                    liveProduct.state=poition;
                    EventBus.getDefault().post(liveProduct);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //防止两次发送请求
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            String seachStr = etSearch.getText().toString().trim();
                            LiveSearch liveProduct = new LiveSearch();
                            liveProduct.content=seachStr;
                            liveProduct.state=poition;
                            EventBus.getDefault().post(liveProduct);
                            InputUtil.HideKeyboard(etSearch);
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
