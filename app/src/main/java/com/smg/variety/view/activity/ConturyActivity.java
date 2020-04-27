package com.smg.variety.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.adapter.Areadapter;
import com.smg.variety.view.adapter.ConsumePushAdapter;
import com.smg.variety.view.adapter.ConturyAdapter;
import com.smg.variety.view.adapter.ConturyCagoriadapter;
import com.smg.variety.view.adapter.ConturyProcutAdapter;
import com.smg.variety.view.mainfragment.consume.BrandShopDetailActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.widgets.AutoLocateHorizontalView;
import com.smg.variety.view.widgets.RecyclerItemDecoration;
import com.smg.variety.view.widgets.autoview.ActionbarView;
import com.smg.variety.view.widgets.autoview.CustomView;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 *
 */
public class ConturyActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView            customActionBar;

    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;

    private int                    mCurrentPage = Constants.PAGE_NUM;

    private int                    mPage        = 1;
    private LinearLayoutManager    layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 888;
    private final int TYPE_PULL_MORE = 889;
    @Override
    public void initListener() {

    }

    private int state;

    @Override
    public int getLayoutId() {
        return R.layout.ui_contury_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private ConturyAdapter testAdapter;
    private boolean        isShow;
    private BaseDto        spikeDto;
    private Areadapter     mHomedapter;
    private String id;
    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        actionbar.setTitle("国家地区馆");
        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });

        mProcutAdapter = new ConsumePushAdapter(lists, this);


        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        initHeaderView();
        mSuperRecyclerView.addItemDecoration(new RecyclerItemDecoration(6, 2));
        mSuperRecyclerView.setLayoutManager(gridLayoutManager2);
        //        layoutManager = new LinearLayoutManager(this);
        //        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mProcutAdapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage=1;
                getConturyProducts(TYPE_PULL_REFRESH,id);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);



    }

    private List<AreaDto>        datas = new ArrayList<>();
    private List<AreaDto>        datass = new ArrayList<>();
    private List<NewListItemDto> lists = new ArrayList<>();
    private BannerItemDto        ban;

    private Banner    banner;
    private NoScrollGridView         gridContent;
    private ImageView                ivOne;
    private NoScrollGridView         gridContent1;
    private AutoLocateHorizontalView autoScroll;
    private LinearLayout             llBg;
    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.contury_header_view, null);
        banner = mHeaderView.findViewById(R.id.banner);
        gridContent = mHeaderView.findViewById(R.id.grid_content);
        ivOne = mHeaderView.findViewById(R.id.iv_one);
        gridContent1 = mHeaderView.findViewById(R.id.grid_content1);
        autoScroll = mHeaderView.findViewById(R.id.auto_scroll);
        llBg = mHeaderView.findViewById(R.id.ll_bg);

        mProcutAdapter.addHeaderView(mHeaderView);
    }


    private void getConturyProduct() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "category");
        map.put("filter[is_recommend]", 1 + "");
        DataManager.getInstance().getConturyProduct(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {

                        mCagoriadapter.setData(result.getData());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, map);
    }

    private ConsumePushAdapter   mProcutAdapter;
    private ConturyCagoriadapter mCagoriadapter;
    private int currpage=1;
    private void getConturyProducts(int loadtype,String id) {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "category");
        map.put("filter[area_id]", id + "");
        map.put("page", currpage + "");

        DataManager.getInstance().getConturyProduct1(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                onDataLoaded(loadtype,result.getData().size()==Constants.PAGE_SIZE,result.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }
    public void onDataLoaded(int loadType, boolean haveNext, List<NewListItemDto> lists) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            list.addAll(lists);
        } else {
            list.addAll(lists);
        }

        mProcutAdapter.setNewData(list);

        mProcutAdapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (haveNext)
                        getConturyProducts(TYPE_PULL_MORE,id);
                    mSuperRecyclerView.hideMoreProgress();

                }
            }, 1);
        } else {
            if (mSuperRecyclerView != null) {
                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
            }


        }


    }

    private List<NewListItemDto> list = new ArrayList<>();


    private void getCategorisContury() {
        //showLoadDialog();
        DataManager.getInstance().getCategorisContury(new DefaultSingleObserver<HttpResult<List<AreaDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AreaDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        datass.clear();
                        datas.clear();
                        datas.addAll(result.getData());
                        datass.addAll(result.getData());
                        mHomedapter.setData(datass);
                        AreaDto liveCatesBean = new AreaDto();
                        liveCatesBean.title = "";
                        liveCatesBean.setId(-2);
                        datas.add(liveCatesBean);
                        testAdapter.setDatas(datas);
                        testAdapter.notifyDataSetChanged();
                        if (datas.size() > 0) {
                            id=datas.get(0).id + "";
                            currpage=1;

                            getConturyProducts(TYPE_PULL_REFRESH,id);
                        }

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private void getTopBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().country_list_top;
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "country_list_top");
    }

    private void getBottenBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().country_list_below_country.size() > 0) {
                        List<BannerItemDto> bannerList = result.getData().country_list_below_country;
                        if (bannerList != null) {
                            ban = bannerList.get(0);
                            GlideUtils.getInstances().loadNormalImg(ConturyActivity.this, ivOne, bannerList.get(0).getPath(), R.drawable.moren_fldb);
                            ivOne.setOnClickListener(new View.OnClickListener() {
                                //                product_default:商品
                                //                seller_default:商家
                                @Override
                                public void onClick(View v) {
                                    //                    Intent intent = new Intent(getActivity(), WebUtilsActivity.class);
                                    //                    intent.putExtra("weburl","http://pszlgl.zhinf.net/guangming/#/pages/authorize/index");
                                    //                    intent.putExtra("webtitle","webtitle");
                                    //                    startActivity(intent);
                                    if (ban.getClick_event_type().equals("product_default")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, ban.getClick_event_value());
                                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                                        Intent intent = new Intent(ConturyActivity.this, CommodityDetailActivity.class);
                                        if (bundle != null) {
                                            intent.putExtras(bundle);
                                        }
                                        startActivity(intent);
                                    } else if (ban.getClick_event_type().equals("seller_default")) {
                                        Intent intent = new Intent(ConturyActivity.this, BrandShopDetailActivity.class);
                                        intent.putExtra("id", ban.getClick_event_value());
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "country_list_below_country");
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


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerItemDto slidersDto = (BannerItemDto) path;
            String imgStr = slidersDto.getPath();
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadNormalImg(ConturyActivity.this, imageView, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadNormalImg(ConturyActivity.this, imageView, imgStr, R.mipmap.img_default_2);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
                    if (slidersDto.getClick_event_type().equals("product_default")) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                        Intent intent = new Intent(ConturyActivity.this, CommodityDetailActivity.class);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        startActivity(intent);
                    } else if (slidersDto.getClick_event_type().equals("seller_default")) {
                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
                        intent.putExtra("id", slidersDto.getClick_event_value());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    @Override
    public void initData() {
        mHomedapter = new Areadapter(this);
        gridContent.setAdapter(mHomedapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        autoScroll.setHasFixedSize(true);
        autoScroll.setLayoutManager(linearLayoutManager);
        autoScroll.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                //                                viewpagerMain.setCurrentItem(pos, false);

                if(datas!=null&&pos>=0){
                    showLoadDialog();
                    id=datas.get(pos).id+"";
                    currpage=1;
                    getConturyProducts(TYPE_PULL_REFRESH,id);
                }


            }
        });

        testAdapter = new ConturyAdapter();

        mCagoriadapter = new ConturyCagoriadapter(this);
        gridContent1.setAdapter(mCagoriadapter);
        testAdapter.setItemClick(new ConturyAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, AreaDto data) {
                autoScroll.moveToPosition(poition);
            }
        });
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
        getTopBanner();
        getBottenBanner();
        getCategorisContury();
        getConturyProduct();

        getHomeCategorie();
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(GlobeConturyActivity.class);
            }
        });

    }

    private List<BannerDto> data = new ArrayList<>();

    private void getHomeCategorie() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        //        map.put("filter[is_hot]","1");
        DataManager.getInstance().AllCategorie(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }


}
