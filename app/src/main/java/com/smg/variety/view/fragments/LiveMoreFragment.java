package com.smg.variety.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.LiveCatesBean;
import com.smg.variety.bean.StoreCategoryDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ErrorUtil;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.mainfragment.learn.CollectViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 订单
 */
public class LiveMoreFragment extends BaseFragment {

    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.vp_action)
    ViewPager        mViewPager;
    Unbinder unbinder;
    private String[] titles ;

    private int mCurrPage;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more_live;
    }

    @Override
    public void initView() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        findStoreCategory();
    }
    private List<String> tls = new ArrayList<>();

    private List<StoreCategoryDto> allData =new ArrayList<>();
    private void findStoreCategory() {
        //        showLoadDialog();
        DataManager.getInstance().findStoreCategory(new DefaultSingleObserver<HttpResult<List<StoreCategoryDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<StoreCategoryDto>> data) {

                tls.clear();
                ArrayList<Fragment> fragments = new ArrayList<>();


                for (StoreCategoryDto bean : data.getData()) {
                    fragments.add(new LiveProductChaildFragment(bean.getId()+""));
                    tls.add(bean.title);
                }
                titles = tls.toArray(new String[tls.size()]);
                mViewPager.setAdapter(new CollectViewPagerAdapter(getChildFragmentManager(), fragments));
                mTabLayout.setViewPager(mViewPager, titles);
                mViewPager.setCurrentItem(mCurrPage);


            }

            @Override
            public void onError(Throwable throwable) {
                String errMsg = ErrorUtil.getInstance().getErrorMessage(throwable);
                if(errMsg != null) {
                    if (errMsg.equals("appUserKey过期")) {
                        ToastUtil.showToast("appUserKey已过期，请重新登录");
                        ShareUtil.getInstance().cleanUserInfo();
                        gotoActivity(LoginActivity.class, true, null);
                    }
                }
                //                dissLoadDialog();
            }
        });
    }

}
