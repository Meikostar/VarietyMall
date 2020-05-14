package com.smg.variety.view.mainfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.GAME;
import com.smg.variety.bean.HeadLineDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.LocationUtils;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.activity.AppNewPeopleActivity;
import com.smg.variety.view.activity.AppRenWuActivity;
import com.smg.variety.view.activity.AppStoreActivity;
import com.smg.variety.view.activity.BrandsActivity;
import com.smg.variety.view.activity.ConturyActivity;
import com.smg.variety.view.activity.GiftSendActivity;
import com.smg.variety.view.activity.HotActivity;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.activity.MenberShareActivity;
import com.smg.variety.view.activity.MessageCenterActivity;
import com.smg.variety.view.activity.OnlyNewPeopleActivity;
import com.smg.variety.view.activity.ShopStoreDetailActivity;
import com.smg.variety.view.activity.SpikeActivity;
import com.smg.variety.view.activity.WebViewActivity;
import com.smg.variety.view.activity.WebViewsActivity;
import com.smg.variety.view.adapter.ChatMenberAdapter;
import com.smg.variety.view.adapter.ConsumePushAdapter;
import com.smg.variety.view.adapter.Homedapter;
import com.smg.variety.view.mainfragment.consume.BrandShopDetailActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.mainfragment.consume.EntityStoreActivity;
import com.smg.variety.view.mainfragment.consume.ProductSearchActivity;
import com.smg.variety.view.mainfragment.consume.SelectCityActivity;
import com.smg.variety.view.widgets.RecyclerItemDecoration;
import com.smg.variety.view.widgets.autoview.CustomView;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 消费
 * Created by rzb on 2019/04/18.
 */
public class ConsumeFragment extends BaseFragment {


    @BindView(R.id.header)
    MaterialHeader   header;
    @BindView(R.id.banner)
    Banner           banner;
    @BindView(R.id.grid_content)
    NoScrollGridView gridContent;
    @BindView(R.id.rl_vp_container)
    RelativeLayout   rlVpContainer;
    @BindView(R.id.iv_one)
    ImageView        ivOne;

    @BindView(R.id.iv_two)
    ImageView          ivTwo;
    @BindView(R.id.iv_three)
    ImageView          ivThree;
    @BindView(R.id.iv_four)
    ImageView          ivFour;
    @BindView(R.id.iv_five)
    ImageView          ivFive;
    @BindView(R.id.consume_push_recy)
    RecyclerView       consumePushRecy;
    @BindView(R.id.consume_scrollView)
    CustomView         consume_scrollView;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_location)
    ImageView          ivLocation;
    @BindView(R.id.tv_location)
    TextView           tv_location;
    @BindView(R.id.find)
    TextView           find;
    @BindView(R.id.iv_scan)
    ImageView          ivScan;
    @BindView(R.id.iv_message)
    ImageView          ivMessage;
    @BindView(R.id.topLayout)
    LinearLayout       topLayout;
    @BindView(R.id.ll_fl)
    LinearLayout       ll_fl;
    @BindView(R.id.grid)
    GridView           gridView;
    @BindView(R.id.iv_red)
    ImageView          iv_red;

    private List<RecommendListDto> brandList    = new ArrayList<RecommendListDto>();
    private ConsumePushAdapter     mConsumePushAdapter;
    private List<NewListItemDto>   goodsLists   = new ArrayList<NewListItemDto>();
    private ArrayList<HeadLineDto> lineLists    = new ArrayList<HeadLineDto>();
    private List<String>           lineStrLists = new ArrayList<String>();
    private int                    mPage        = 1;
    private int                    hPage        = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_consume;
    }

    @Override
    protected void initView() {

        header.setColorSchemeResources(R.color.my_color_head1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mHomedapter = new Homedapter(getActivity());
        adapter = new ChatMenberAdapter(getActivity());
        gridContent.setAdapter(mHomedapter);

    }

    private void iniGridView(final List<GAME> list) {

        int length = 62;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if (dm == null || getActivity() == null) {
            return;
        }
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int gridviewWidth = (int) (list.size() * (length + 5) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(8, 0, 0, 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(10); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(list.size()); // 设置列数量=列表集合数
        adapter.setDatas(list);
        gridView.setAdapter(adapter);
        adapter.setClickListener(new ChatMenberAdapter.ClickListener() {
            @Override
            public void clicks(BannerDto game) {
                if (game.url.contains("::") && game.url.contains("seller")) {
                    String[] split = game.url.split("=");
                    if (split != null && split.length == 2) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ShopStoreDetailActivity.SHOP_DETAIL_ID, split[1]);
                        gotoActivity(ShopStoreDetailActivity.class, false, bundle);
                    }
                } else {
                    if(game.title.equals("附近商家")){
                        Intent intent = new Intent(getActivity(), EntityStoreActivity.class);
                        startActivity(intent);
                    }else {
                        if(game.url.contains("{user_id}")){
                            String replace = game.url.replace("{user_id}", TextUtil.isNotEmpty(ShareUtil.getInstance().get(Constants.USER_ID))?ShareUtil.getInstance().get(Constants.USER_ID):"0");
                            if (replace.contains("https://s.click.taobao.com") || replace.contains("https://m.tb.cn")) {
                                Intent intent = new Intent(getActivity(), WebViewsActivity.class);

                                intent.putExtra(WebViewActivity.WEBTITLE, game.title);
                                intent.putExtra(WebViewActivity.WEBURL, replace);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);

                                intent.putExtra(WebViewActivity.WEBTITLE, game.title);
                                intent.putExtra(WebViewActivity.WEBURL, replace);
                                startActivity(intent);
                            }
                        } else  if(game.url.equals("free_get")){

                                Intent intent = new Intent(getActivity(), GiftSendActivity.class);

                                startActivity(intent);

                        }else {
                            if (game.url.contains("https://s.click.taobao.com") || game.url.contains("https://m.tb.cn")) {
                                Intent intent = new Intent(getActivity(), WebViewsActivity.class);

                                intent.putExtra(WebViewActivity.WEBTITLE, game.title);
                                intent.putExtra(WebViewActivity.WEBURL, game.url);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);

                                intent.putExtra(WebViewActivity.WEBTITLE, game.title);
                                intent.putExtra(WebViewActivity.WEBURL, game.url);
                                startActivity(intent);
                            }
                        }

                    }

                }


            }
        });

    }

    private Homedapter        mHomedapter;
    private ChatMenberAdapter adapter;
    private Gson              gson = new Gson();
    ;

    @Override
    protected void initData() {


        refreshLayout.autoRefresh();
        initViewPager();
        setData();
        Location location = LocationUtils.getInstance(getActivity()).showLocation();
        if (location != null) {
            getLocation();
        }
        String cacheToken = ShareUtil.getInstance().getString(Constants.USER_ID, null);
        if(TextUtil.isNotEmpty(cacheToken)){
            getUserInfo();
        }else {
            getFourBanner();
        }
        getHomeCategorie1();
        getTopBanner();

        getHeadLines();
        //        getHomeRecommendList();
        //        findGoodLists();
    }
    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {

                BaseApplication.level=personalInfoDto.level;
                BaseApplication.is_new=personalInfoDto.is_new_pull==null?1:Integer.valueOf(personalInfoDto.is_new_pull);
                getFourBanner();

            }

            @Override
            public void onError(Throwable throwable) {
                getFourBanner();
            }
        });
    }
    public void setData() {
        BannerInfoDto home_one = gson.fromJson(ShareUtil.getInstance().get("home_one"), BannerInfoDto.class);
        BannerInfoDto home_three = gson.fromJson(ShareUtil.getInstance().get("home_three"), BannerInfoDto.class);
        BaseDto home_two = gson.fromJson(ShareUtil.getInstance().get("home_two"), BaseDto.class);
        BannerItemDto home_last = gson.fromJson(ShareUtil.getInstance().get("home_last"), BannerItemDto.class);
        BaseDto home_four = gson.fromJson(ShareUtil.getInstance().get("home_four"), BaseDto.class);
        if (home_one != null) {
            List<BannerItemDto> bannerList = home_one.getIndex_top();
            startBanner(bannerList);
        }
        if (home_two != null) {
            mHomedapter.setData(home_two.home_two);
        }
        if (home_three != null) {
            //            GlideUtils.getInstances().loadNormalImg(getActivity(),ivOne,home_three.index_after_category_1.get(0).getPath());
            GlideUtils.getInstances().loadNormalImg(getActivity(), ivTwo, home_three.index_after_category_2_left.get(0).getPath());
            GlideUtils.getInstances().loadNormalImg(getActivity(), ivThree, home_three.index_after_category_2_right_top.get(0).getPath());
            GlideUtils.getInstances().loadNormalImg(getActivity(), ivFour, home_three.index_after_category_2_right_bottom.get(0).getPath());
            //            GlideUtils.getInstances().loadNormalImg(getActivity(), ivOne,  home_three.index_after_category_1.get(0).getPath(), R.drawable.home_1);
            //            GlideUtils.getInstances().loadNormalImg(getActivity(), ivTwo,   home_three.index_after_category_2_left.get(0).getPath(), R.drawable.home_2);
            //
            //            GlideUtils.getInstances().loadNormalImg(getActivity(), ivThree,  home_three.index_after_category_2_right_top.get(0).getPath(), R.drawable.home_3);
            //            GlideUtils.getInstances().loadNormalImg(getActivity(), ivFour,  home_three.index_after_category_2_right_bottom.get(0).getPath(), R.drawable.home_4);

        }
        if (home_four != null) {
            mConsumePushAdapter.setNewData(home_four.home_four);
            if (home_four.home_four == null || home_four.home_four.size() == 0) {
                mConsumePushAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
            refreshLayout.finishLoadMore();

        }
        if (home_last != null) {
            ban = home_last;
            ShareUtil.getInstance().save("home_last", gson.toJson(ban));
            String imgStr = ban.getPath();
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadNormalImg(getActivity(), ivFive, imgStr);
                    //                    GlideUtils.getInstances().loadNormalImg(getActivity(), ivFive, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadNormalImg(getActivity(), ivFive, Constants.WEB_IMG_URL_UPLOADS + imgStr);
                    //                    GlideUtils.getInstances().loadNormalImg(getActivity(), ivFive, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.drawable.home_5);
                }
            }
            ivFive.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = new Intent(getActivity(), CommodityDetailActivity.class);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        startActivity(intent);
                    } else if (ban.getClick_event_type().equals("seller_default")) {
                        Intent intent = new Intent(getActivity(), BrandShopDetailActivity.class);
                        intent.putExtra("id", ban.getClick_event_value());
                        startActivity(intent);
                    }
                }
            });
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        iv_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                    gotoActivity(LoginActivity.class);

                } else {
                    gotoActivity(AppRenWuActivity.class);
                }

            }
        });
        bindClickEvent(tv_location, () -> {

            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStore");
            gotoActivity(SelectCityActivity.class, false, bundle);
        });
        bindClickEvent(ivLocation, () -> {

            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStore");
            gotoActivity(SelectCityActivity.class, false, bundle);
        });

        bindClickEvent(find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "consume_index");
            gotoActivity(ProductSearchActivity.class, false, bundle);
        });

        //        bindClickEvent(ll_add, () -> {
        //            HomeMorePopWindow morePopWindow = new HomeMorePopWindow(this.getActivity());
        //            morePopWindow.showPopupWindow(ll_add);
        //        });
        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });

        ivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseApplication.is_new == 1) {
                    startActivity(new Intent(getActivity(), OnlyNewPeopleActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), SpikeActivity.class));
                }

            }
        });
        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HotActivity.class));
            }
        });
        ivThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ConturyActivity.class));
            }
        });
        ivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BrandsActivity.class));
            }
        });

        ll_fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 启动扫一扫
                 */
                startActivity(new Intent(getActivity(), MenberShareActivity.class));
                //                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                //                ZxingConfig config = new ZxingConfig();
                //                config.setReactColor(R.color.my_color_009AFF);//设置扫描框四个角的颜色 默认为白色
                //                config.setScanLineColor(R.color.my_color_009AFF);//设置扫描线的颜色 默认白色
                //                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                //                config.setShowbottomLayout(false);
                //                startActivityForResult(intent, Constants.HOME_REQUEST_CODE_SCAN);

            }
        });
        ;


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                findGoodLists();
            }
        });


        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                findGoodLists();
            }
        });

        /**
         * 滑动时标题栏渐变
         */
        consume_scrollView.setScrollViewListener(new CustomView.ScrollViewListener() {
            @Override
            public void onScrollChanged(CustomView customView, int x, int y, int oldx, int oldy) {
                int toolbarHeight = topLayout.getHeight();
                if (y <= 0) {
                    topLayout.setBackgroundColor(Color.argb((int) 0, 1, 165, 241));//AGB由相关工具获得，或者美工提供
                } else if (y > 0 && y <= toolbarHeight) {
                    //获取ScrollView向下滑动图片消失的比例getResources().getColor(R.color.my_color_zs)
                    float scale = (float) y / toolbarHeight;
                    //更加这个比例,让标题颜色由浅入深
                    float alpha = (255 * scale);
                    // 只是layout背景透明
                    topLayout.setBackgroundColor(Color.argb((int) alpha, 1, 165, 241));
                }
            }
        });


        BroadcastManager.getInstance(getActivity()).addAction(Constants.CHOICE_CITY, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String cName = intent.getStringExtra("String");
                    tv_location.setText(cName);
                }
            }
        });

        getProductTopBanner();
    }

    private void initViewPager() {


        mConsumePushAdapter = new ConsumePushAdapter(goodsLists, getActivity());
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(6, 2));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mConsumePushAdapter);

    }


    private void getLocation() {
        //showLoadDialog();
        DataManager.getInstance().getLocation(new DefaultSingleObserver<HttpResult<AreaDto>>() {
            @Override
            public void onSuccess(HttpResult<AreaDto> result) {
                //dissLoadDialog();
                if (result != null) {

                    if (result.getData() != null) {
                        String cityName = result.getData().getName();
                        tv_location.setText(cityName);
                    }
                }
                LocationUtils.getInstance(getActivity()).removeLocationUpdatesListener();
            }

            @Override
            public void onError(Throwable throwable) {
                LocationUtils.getInstance(getActivity()).removeLocationUpdatesListener();
                //dissLoadDialog();
            }
        });
    }


    private void getHomeCategorie() {
        //showLoadDialog();
        DataManager.getInstance().banCategorie(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null) {
                    BaseDto baseDto = new BaseDto();
                    baseDto.home_two = result.getData();
                    ShareUtil.getInstance().save("home_two", gson.toJson(baseDto));
                    mHomedapter.setData(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();

            }
        });
    }

    private List<BannerDto> list   = new ArrayList<>();
    private List<GAME>      mGAMES = new ArrayList<>();

    private void getHomeCategorie1() {
        //showLoadDialog();
        DataManager.getInstance().getApplication(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null) {
                    list.clear();
                    mGAMES.clear();
                    int size = result.getData().size();
                    if (size >= 5) {
                        for (int i = 0; i < 5; i++) {
                            list.add(result.getData().get(i));
                        }
                        int cout = (size - 4) / 2;
                        int zoe = (size - 4) % 2;
                        int len = zoe == 0 ? cout : cout + 1;
                        for (int a = 5; a < 5 + len; a++) {
                            GAME game = new GAME();


                            if (a == len + 4) {
                                if (zoe == 0) {
                                    BannerDto bannerDto = new BannerDto();
                                    bannerDto.title = "更多";
                                    game.one = result.getData().get(a);
                                    game.two = bannerDto;
                                } else {
                                    game.one = result.getData().get(a);
                                    //                                 game.two=result.getData().get(len+a);
                                }
                            } else if (a == len + 3) {
                                if (zoe != 0) {
                                    BannerDto bannerDto = new BannerDto();
                                    bannerDto.title = "更多";
                                    game.one = result.getData().get(a);
                                    game.two = bannerDto;
                                } else {
                                    game.one = result.getData().get(a);
                                    game.two = result.getData().get(len + a);
                                }
                            } else {
                                game.one = result.getData().get(a);
                                game.two = result.getData().get(len + a);
                            }
                            mGAMES.add(game);
                        }

                    } else {
                        for (int i = 0; i < size; i++) {
                            list.add(result.getData().get(i));
                        }
                    }

                    mHomedapter.setData(list);
                    iniGridView(mGAMES);

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

                        ShareUtil.getInstance().save("home_one", gson.toJson(result.getData()));
                        List<BannerItemDto> bannerList = result.getData().getIndex_top();
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_top");
    }

    private void getProductTopBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().index_product_list_top;
                        if (bannerList != null) {

                            ban = bannerList.get(0);
                            ShareUtil.getInstance().save("home_last", gson.toJson(ban));
                            String imgStr = ban.getPath();
                            if (imgStr != null) {
                                if (imgStr.contains("http://")) {
                                    GlideUtils.getInstances().loadNormalImg(getActivity(), ivFive, imgStr, R.mipmap.img_default_2);
                                } else {
                                    GlideUtils.getInstances().loadNormalImg(getActivity(), ivFive, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.drawable.home_5);
                                }
                            }
                            ivFive.setOnClickListener(new View.OnClickListener() {
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
                                        Intent intent = new Intent(getActivity(), CommodityDetailActivity.class);
                                        if (bundle != null) {
                                            intent.putExtras(bundle);
                                        }
                                        startActivity(intent);
                                    } else if (ban.getClick_event_type().equals("seller_default")) {
                                        Intent intent = new Intent(getActivity(), BrandShopDetailActivity.class);
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
        }, "index_product_list_top");
    }

    private BannerItemDto ban;

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
        banner.setBannerAnimation(Transformer.DepthPage);
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
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 5, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 5, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_2);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
                    //                    Intent intent = new Intent(getActivity(), WebUtilsActivity.class);
                    //                    intent.putExtra("weburl","http://pszlgl.zhinf.net/guangming/#/pages/authorize/index");
                    //                    intent.putExtra("webtitle","webtitle");
                    //                    startActivity(intent);
                    if (slidersDto.getClick_event_type().equals("product_default")) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                        Intent intent = new Intent(context, CommodityDetailActivity.class);
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

    private void getMiddleBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getIndex_middle();

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_middle");
    }

    private void getFourBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        ShareUtil.getInstance().save("home_three", gson.toJson(result.getData()));
                        List<BannerItemDto> bannerList = result.getData().getIndex_middle();
                        if (BaseApplication.is_new == 1) {
                            GlideUtils.getInstances().loadNormalImg(getActivity(), ivOne, result.getData().home_new_pull.get(0).getPath());
                        } else {
                            GlideUtils.getInstances().loadNormalImg(getActivity(), ivOne, result.getData().index_after_category_1.get(0).getPath());
                        }

                        GlideUtils.getInstances().loadNormalImg(getActivity(), ivTwo, result.getData().index_after_category_2_left.get(0).getPath());
                        GlideUtils.getInstances().loadNormalImg(getActivity(), ivThree, result.getData().index_after_category_2_right_top.get(0).getPath());
                        GlideUtils.getInstances().loadNormalImg(getActivity(), ivFour, result.getData().index_after_category_2_right_bottom.get(0).getPath());
                        //                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivOne, 0f,  result.getData().index_after_category_1.get(0).getPath(), R.drawable.home_1);
                        //                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivTwo, 0f, result.getData().index_after_category_2_left.get(0).getPath(), R.drawable.home_2);
                        //                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivThree, 0f,  result.getData().index_after_category_2_right_top.get(0).getPath(), R.drawable.home_3);
                        //                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivFour, 0f,  result.getData().index_after_category_2_right_bottom.get(0).getPath(), R.drawable.home_4);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_after_category_1,index_after_category_2_left,index_after_category_2_right_top,index_after_category_2_right_bottom,home_new_pull");
    }

    private void getHeadLines() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("page", hPage + "");
        DataManager.getInstance().getHeadLines(new DefaultSingleObserver<HttpResult<List<HeadLineDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<HeadLineDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        lineLists.addAll(result.getData());
                        for (int j = 0; j < lineLists.size(); j++) {
                            lineStrLists.add(lineLists.get(j).getTitle());
                        }

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, map);
    }


    private void findGoodLists() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "brand.category");
        map.put("page", mPage + "");
        DataManager.getInstance().findHomeGoodLists(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                if (mPage == 1) {
                    BaseDto baseDto = new BaseDto();
                    baseDto.home_four = result.getData();
                    ShareUtil.getInstance().save("home_four", gson.toJson(baseDto));
                }
                dissLoadDialog();
                setData(result);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "default", map);
    }


    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }


        if (mPage <= 1) {
            mConsumePushAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mConsumePushAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableRefresh(true);
            mConsumePushAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }


    }

    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(getActivity()).destroy(Constants.CHOICE_CITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private Unbinder mUnbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
