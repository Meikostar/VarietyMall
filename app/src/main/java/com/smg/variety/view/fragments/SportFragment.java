package com.smg.variety.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.ArticleBean;
import com.smg.variety.bean.BannerBean;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.activity.InformationHomeDetailActivity;
import com.smg.variety.view.adapter.ArticleMultipleAdapter;
import com.smg.variety.view.mainfragment.MemberFragment;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页
 */
public class SportFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;

    private View                   mHeaderView;
    private List<String>           images         = new ArrayList<>();
    private int                    mPage          = 1;
    private int                    mPageSize      = 10;
    /**首页Banner图*/
    private Banner                 banner;
    /**知识竞赛*/
    private ImageView              mKnowledgeContest;
    /**已阅读时间*/
    private TextView               mReadTime;
    /**首页推荐数据Adapter*/
    private ArticleMultipleAdapter mConsumePushAdapter;
    private List<DetailDto>      mRecommendList = new ArrayList<>();
    /**今日阅读时间*/
    private int                    mRunTime;
    //private AutoUpdateReadTime mAutoUpdateReadTime;
    private String tab;
    private String type;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sport;
    }
    public static SportFragment newInstance(String tab) {
        SportFragment fragment = new SportFragment();
        fragment.tab = tab;
        return fragment;
    }
    @Override
    protected void initView() {
        initRecyclerView();
    }

    @Override
    protected void initData() {
        if(tab.equals("greenhand")){
            type="new_go";
        }else {
            type="advanced_learning";
        }
        findGoodLists();
        getBottenBanner();
    }

    private void getBottenBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if(type.equals("advanced_learning")){
                        if (result.getData() != null && result.getData().advanced_learning.size() > 0) {
                            List<BannerItemDto> bannerList = result.getData().advanced_learning;
                            startBanner(bannerList);
                        }
                    }else {
                        if (result.getData() != null && result.getData().new_go.size() > 0) {
                            List<BannerItemDto> bannerList = result.getData().new_go;
                            startBanner(bannerList);

                        }
                    }

                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, type);
    }

    private void startBanner(List<BannerItemDto> data) {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.CENTER);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(data);
        //设置banner动画效果
        //        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    @Override
    public void onClick(View v) {

    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideUtils.getInstances().loadNormalImg(context, imageView, ((BannerItemDto)path).getPath());
        }
        //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义ImageView的创建
        @Override
        public ImageView createImageView(Context context) {
            return super.createImageView(context);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        /*if(mAutoUpdateReadTime != null){
            mAutoUpdateReadTime.stop();
        }*/
    }






    @Override
    protected void initListener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                findGoodLists();
            }
        });


        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                findGoodLists();
            }
        });


    }

    private void findGoodLists() {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        map.put("sort","-order");
        map.put("show_content","1");
        DataManager.getInstance().getHelpData(new DefaultSingleObserver<HttpResult<List<DetailDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<DetailDto>> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {

                    dissLoadDialog();
                    setData(countOrderBean);

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        },tab,map);
    }

    private void setData(HttpResult<List<DetailDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }


        if (mPage <= 1) {
            mConsumePushAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mConsumePushAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mConsumePushAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }


    }
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mConsumePushAdapter = new ArticleMultipleAdapter(getActivity(), mRecommendList);
        mRecyclerView.setAdapter(mConsumePushAdapter);

        mConsumePushAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DetailDto item = (DetailDto) adapter.getItem(position);
                startActivity(new Intent(getActivity(), InformationHomeDetailActivity.class)
                        .putExtra("title", item.title)
                        .putExtra("article_id", item.id)
                        .putExtra("type", tab));
            }
        });
        initHeadView();
    }

    private void initHeadView() {
        mHeaderView = View.inflate(getActivity(), R.layout.layout_home_head_view, null);
        banner = mHeaderView.findViewById(R.id.banner);

        mConsumePushAdapter.addHeaderView(mHeaderView);
    }






     @Override
     public void onResume() {
        super.onResume();

         banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }
}
