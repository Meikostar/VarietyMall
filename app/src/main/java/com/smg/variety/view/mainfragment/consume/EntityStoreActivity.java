package com.smg.variety.view.mainfragment.consume;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.CommodityDetailAttrItemDto;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.FirstClassItem;
import com.smg.variety.bean.GoodsAttrDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.Param;
import com.smg.variety.bean.Params;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.bean.SecondClassItem;
import com.smg.variety.bean.StoreCategoryDto;
import com.smg.variety.bean.TagBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.common.utils.UIUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ScreenUtils;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.ClassifyOneAdapter;
import com.smg.variety.view.adapter.ClassifyOneAdapters;
import com.smg.variety.view.adapter.ClassifyTwoAdapters;
import com.smg.variety.view.adapter.EntityStoreAdapter;
import com.smg.variety.view.adapter.FirstClassAdapter;
import com.smg.variety.view.adapter.GoodsAttrAdapter;
import com.smg.variety.view.adapter.GoodsAttrAdapters;
import com.smg.variety.view.adapter.GoodsAttrAdapterss;
import com.smg.variety.view.adapter.LoopViewPagerAdapter;
import com.smg.variety.view.adapter.SecondClassAdapter;
import com.smg.variety.view.adapter.SingleFirstClassAdapter;
import com.smg.variety.view.widgets.CustomDividerItemDecoration;
import com.smg.variety.view.widgets.HorizontalDividerItemDecoration;
import com.smg.variety.view.widgets.RecyclerItemDecoration;
import com.smg.variety.view.widgets.autoview.ClearEditText;
import com.smg.variety.view.widgets.autoview.MaxRecyclerView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 实体店铺
 * Created by rzb on 2019/6/16.
 */
public class EntityStoreActivity extends BaseActivity {

    private EntityStoreAdapter     mEntityStoreAdapter;
    private List<RecommendListDto> storeLists      = new ArrayList<RecommendListDto>();
    //使用PopupWindow只显示一级分类
    private PopupWindow            levelsAllPopupWindow;
    private PopupWindow            levelsFjPopupWindow;
    private PopupWindow            levelsSxPopupWindow;
    //只显示一个ListView
    private RecyclerView           recyclerView;
    private TextView               tv_one;
    private TextView               tv_two;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    //分类数据
    private List<FirstClassItem>   singleFirstList = new ArrayList<FirstClassItem>();
    ;
    //使用PopupWindow显示一级分类和二级分类
    private PopupWindow levelsPopupWindow;
    //左侧和右侧两个ListView
    private ListView    leftLV, rightLV;
    //左侧一级分类的数据
    private List<FirstClassItem>  firstList  = new ArrayList<FirstClassItem>();
    //右侧二级分类的数据
    private List<SecondClassItem> secondList = new ArrayList<SecondClassItem>();
    private List<SecondClassItem> secondLists = new ArrayList<SecondClassItem>();


    private             LoopViewPagerAdapter loopViewPagerAdapter;
    private             List<BannerItemDto>  adsList    = new ArrayList<BannerItemDto>();
    public static final int                  GETCITY    = 1001;
    private             String               sitId      = null;
    private             String               categoryId = null;
    private             String               areaId     = null;
    private             String               distance   = null;


    @Override
    public int getLayoutId() {
        return R.layout.activity_entity_store;
    }

    @Override
    public void initView() {
        initLevelsAllPopup();
        initeSxPopup();
    }

    @Override
    public void initData() {
        initAdapter();
        getTopBanner();
        getConfigs();
        getTagsList();
        getInducts();
        getLocation();
        getShopList(TYPE_PULL_REFRESH);
    }

    @Override
    public void initListener() {
        bindClickEvent(store_layout_back, () -> {
            finish();
        });

        BroadcastManager.getInstance(this).addAction(Constants.CHOICE_CITYS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String cName = intent.getStringExtra("String");
                    tv_location.setText(cName);
                    tvLocation.setText(cName);
                    tvLocations.setText(cName);
                    if(tvLocats!=null){
                        tvLocats.setText(cName);
                    }


                }
            }
        });
        //        bindClickEvent(find, () -> {
        //            Bundle bundle = new Bundle();
        //            bundle.putString("includeStr", "st");
        //            gotoActivity(ProductSearchActivity.class,false, bundle);
        //        });

        bindClickEvent(store_ll_location, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStores");
            gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
        });

        bindClickEvent(layout_all, () -> {
            if (levelsAllPopupWindow.isShowing()) {
                levelsAllPopupWindow.dismiss();
            }
            levelsAllPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
//            levelsAllPopupWindow.showAsDropDown();
            levelsAllPopupWindow.setAnimationStyle(R.style.bottomAnimStyle);
        });
        bindClickEvent(layout_sx, () -> {
            if (levelsSxPopupWindow == null) {
                return;
            }
            if (levelsSxPopupWindow.isShowing()) {
                levelsSxPopupWindow.dismiss();

            }
            levelsSxPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);

            levelsSxPopupWindow.setAnimationStyle(R.style.bottomAnimStyle);
        });
        bindClickEvent(layout_near, () -> {
            if (levelsPopupWindow == null) {
                return;
            }
            if (levelsPopupWindow.isShowing()) {
                levelsPopupWindow.dismiss();

            }
            levelsPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);

//            levelsPopupWindow.showAsDropDown(findViewById(R.id.store_div_line_three));
            levelsPopupWindow.setAnimationStyle(R.style.bottomAnimStyle);
        });
        find.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              if(TextUtil.isEmpty(s.toString())){
                  shop_name = "";
                  getShopList(TYPE_PULL_REFRESH);
              }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        find.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                //点击搜索要做的操作
                if (TextUtil.isNotEmpty(find.getText().toString())) {

                    shop_name = find.getText().toString();

                } else {
                    shop_name = "";


                }

                getShopList(TYPE_PULL_REFRESH);
               closeKeyBoard();

                return false;
            }
        });
    }

    private String searchKey;

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(this).destroy(Constants.CHOICE_CITYS);
    }

    @Override
    public void onBackPressed() {

        if (levelsSxPopupWindow!=null&&levelsSxPopupWindow.isShowing()) {
            levelsSxPopupWindow.dismiss();
        }else {
            if (levelsAllPopupWindow!=null&&levelsAllPopupWindow.isShowing()) {
                levelsAllPopupWindow.dismiss();
            }else {
                if (levelsPopupWindow!=null&&levelsPopupWindow.isShowing()) {
                    levelsPopupWindow.dismiss();
                }else {
                    finish();
                }
            }
        }


    }

    private GoodsAttrAdapters  goodsAttrAdapter;
    private GoodsAttrAdapterss goodsAttrAdapters;
    List<GoodsAttrDto> gadList  = new ArrayList<>();
    List<GoodsAttrDto> gadLists = new ArrayList<>();

    private void initeSxPopup() {
        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高

        levelsSxPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_store_entity_sx, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_select_commodity);
        tv_one = (TextView) view.findViewById(R.id.but_one);
        tv_two = (TextView) view.findViewById(R.id.but_two);
        iv_backs = (ImageView) view.findViewById(R.id.iv_back);
        tvLocations = (TextView) view.findViewById(R.id.tv_location);
        finds3 = (TextView) view.findViewById(R.id.find);
        layout_all1 = (RelativeLayout) view.findViewById(R.id.layout_all);
        layout_near2 = (RelativeLayout) view.findViewById(R.id.layout_near);
        finds3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                find.requestFocus();
            }
        });
        layout_all1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();

                if (levelsAllPopupWindow == null) {
                    return;
                }
                if (levelsAllPopupWindow.isShowing()) {
                    levelsAllPopupWindow.dismiss();

                }
                levelsAllPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });
        layout_near2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
                if (levelsPopupWindow == null) {
                    return;
                }
                if (levelsPopupWindow.isShowing()) {
                    levelsPopupWindow.dismiss();

                }
                levelsPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });
        tvLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("from", "entityStores");
                gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
            }
        });
        tvLocations.setText(location);
        levelsSxPopupWindow.setContentView(view);
        levelsSxPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));

        levelsSxPopupWindow.setFocusable(false);
        levelsSxPopupWindow.setClippingEnabled(false);
        levelsSxPopupWindow.setHeight(height + 120);
        levelsSxPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsSxPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        tv_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsAttrDto> data = goodsAttrAdapter.getData();
                for (GoodsAttrDto dto : data) {
                    for (BaseDto baseDto : dto.data) {
                        baseDto.isChoose = false;
                    }
                }

                goodsAttrAdapter.notifyDataSetChanged();
                onsaleId="";
                serviceId="";
                per_consumption="";
                distance="";


            }
        });
        tv_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GoodsAttrDto> data = goodsAttrAdapter.getData();
                int a=0;
                onsaleId="";
                serviceId="";
                per_consumption="";
                distance="";
                for (GoodsAttrDto dto : data) {
                    for (BaseDto baseDto : dto.data) {
                        if(baseDto.isChoose){
                            if(dto.getKey().equals("优惠和权益")){
                                onsaleId=baseDto.id;
                            }
                            if(dto.getKey().equals("服务")){
                                serviceId=baseDto.id;
                            } if(dto.getKey().equals("价格")){
                                per_consumption=baseDto.id;
                            }   if(dto.getKey().equals("距离")){
                                distance=baseDto.id;
                            }

                        }


                    }
                }
                getShopList(TYPE_PULL_REFRESH);
                levelsSxPopupWindow.dismiss();
            }
        });
        iv_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsSxPopupWindow.dismiss();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setFocusable(false);
        goodsAttrAdapter = new GoodsAttrAdapters(gadList, this);
        goodsAttrAdapter.setGoodsSpecListener(new GoodsAttrAdapters.GoodsSpecListener() {
            @Override
            public void callbackGoodsSpec(String attStrs, String key) {

                mMap.put(key, attStrs);

            }
        });
        recyclerView.setAdapter(goodsAttrAdapter);
    }

    private RecyclerView        rvOne;
    private RecyclerView        rvTwo;
    private ImageView           iv_back;
    private ImageView           iv_backs;
    private ImageView           iv_backss;
    private TextView            tvLocation;
    private TextView            tvLocations;
    private ClassifyOneAdapters mClassifyOneAdapter;
    private String              location;

    private void initLevelsAllPopup() {
        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高
        levelsAllPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_store_entity_all, null);

        rvOne = (RecyclerView) view.findViewById(R.id.rv_one);
        rvTwo = (RecyclerView) view.findViewById(R.id.rv_two);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        tvLocation = (TextView) view.findViewById(R.id.tv_location);
        finds2 = (TextView) view.findViewById(R.id.find);
        layout_near1 = (RelativeLayout) view.findViewById(R.id.layout_near);
        layout_sx2 = (RelativeLayout) view.findViewById(R.id.layout_sx);
        finds2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                find.requestFocus();
            }
        });
        layout_sx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                if (levelsSxPopupWindow == null) {
                    return;
                }
                if (levelsSxPopupWindow.isShowing()) {
                    levelsSxPopupWindow.dismiss();

                }
                levelsSxPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });
        layout_near1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                if (levelsPopupWindow == null) {
                    return;
                }
                if (levelsPopupWindow.isShowing()) {
                    levelsPopupWindow.dismiss();

                }
                levelsPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("from", "entityStores");
                gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
            }
        });
        mClassifyOneAdapter = new ClassifyOneAdapters();
        rvOne.setLayoutManager(new LinearLayoutManager(this));
        rvOne.setAdapter(mClassifyOneAdapter);
        rvOne.setSelected(true);
        rvOne.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.parseColor("#f1f1f1"))
                .size(UIUtil.dp2px(1))
                .build());
        rvOne.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePos = mClassifyOneAdapter.selctedPos;  //之前的位置
                mClassifyOneAdapter.selctedPos = position; //之后选择的位置
                StoreCategoryDto item = (StoreCategoryDto) adapter.getItem(position);
                if (position != prePos) {//更新item的状态
                    mClassifyOneAdapter.notifyItemChanged(prePos);
                    mClassifyOneAdapter.notifyItemChanged(position);

                    goodsAttrAdapters.setNewData(maps.get(item.getId() + ""));
                }


            }
        });
        levelsAllPopupWindow.setContentView(view);
        levelsAllPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));
        levelsAllPopupWindow.setFocusable(false);
        levelsAllPopupWindow.setClippingEnabled(false);
        levelsAllPopupWindow.setHeight(height + 120);
        levelsAllPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsAllPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsAllPopupWindow.dismiss();
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTwo.setLayoutManager(mLinearLayoutManager);
        rvTwo.setFocusable(false);
        goodsAttrAdapters = new GoodsAttrAdapterss(gadLists, this);
        goodsAttrAdapters.setGoodsSpecListener(new GoodsAttrAdapterss.GoodsSpecListener() {
            @Override
            public void callbackGoodsSpec(String id, String key,boolean isselect) {

                levelsAllPopupWindow.dismiss();
                if(isselect){
                    industryId = id;
                }else {
                    industryId = "";
                }
                getShopList(TYPE_PULL_REFRESH);
            }
        });
        rvTwo.setAdapter(goodsAttrAdapters);
    }

    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManagers;


    private RecyclerView rvOnes;
    private RecyclerView rvTwos;

    private TextView            tvLocationss;
    private ClassifyOneAdapters mClassifyOneAdaps;
    private ClassifyTwoAdapters mClassifyTwoAdaps;

    private void initLevelsFjPopup() {
        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高
        levelsFjPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_store_entity_all, null);

        rvOnes = (RecyclerView) view.findViewById(R.id.rv_one);
        rvTwos = (RecyclerView) view.findViewById(R.id.rv_two);
        iv_backs = (ImageView) view.findViewById(R.id.iv_back);
        tvLocationss = (TextView) view.findViewById(R.id.tv_location);
        tvLocationss.setText(location);
        mClassifyOneAdaps = new ClassifyOneAdapters();
        rvOnes.setLayoutManager(new LinearLayoutManager(this));
        rvOnes.setAdapter(mClassifyOneAdaps);
        rvOnes.setSelected(true);
        rvOnes.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.parseColor("#f1f1f1"))
                .size(UIUtil.dp2px(1))
                .build());
        rvOnes.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePos = mClassifyOneAdaps.selctedPos;  //之前的位置
                mClassifyOneAdaps.selctedPos = position; //之后选择的位置
                StoreCategoryDto item = (StoreCategoryDto) adapter.getItem(position);
                if (position != prePos) {//更新item的状态
                    mClassifyOneAdaps.notifyItemChanged(prePos);
                    mClassifyOneAdaps.notifyItemChanged(position);

                    goodsAttrAdapters.setNewData(maps.get(item.getId() + ""));
                }


            }
        });

        levelsFjPopupWindow.setContentView(view);
        levelsFjPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));
        levelsFjPopupWindow.setFocusable(false);
        levelsFjPopupWindow.setClippingEnabled(false);
        levelsFjPopupWindow.setHeight(height + 620);
        levelsFjPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsFjPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        iv_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsFjPopupWindow.dismiss();
            }
        });
        mLinearLayoutManagers = new LinearLayoutManager(this);
        rvTwos.setLayoutManager(mLinearLayoutManagers);
        rvTwos.setFocusable(false);
        mClassifyTwoAdaps = new ClassifyTwoAdapters();
        rvTwos.setLayoutManager(new LinearLayoutManager(this));
        rvTwos.setAdapter(mClassifyTwoAdaps);
        rvTwos.setSelected(true);
        rvTwos.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.parseColor("#f1f1f1"))
                .size(UIUtil.dp2px(1))
                .margin(15, 15)
                .build());
        rvTwos.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePos = mClassifyTwoAdaps.selctedPos;  //之前的位置
                mClassifyTwoAdaps.selctedPos = position; //之后选择的位置
                StoreCategoryDto item = (StoreCategoryDto) adapter.getItem(position);


            }
        });

    }

    private Map<String, String> mMap = new HashMap<>();

    private void getTopBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {

                        List<BannerItemDto> bannerList = result.getData().near_seller_banner;
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "near_seller_banner");

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
                    GlideUtils.getInstances().loadNormalImg(EntityStoreActivity.this, imageView, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadNormalImg(EntityStoreActivity.this, imageView, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_2);
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

    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getConfigs(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<String> strLists = result.getData().search_distance;
                        for (int i = 0; i < strLists.size(); i++) {
                            SecondClassItem secondClassItem = new SecondClassItem();
                            secondClassItem.setName(strLists.get(i));
                            secondList.add(secondClassItem);
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

    private List<Params>                    datas;
    private List<StoreCategoryDto>          dtos = new ArrayList<>();
    private Map<String, List<GoodsAttrDto>> maps = new HashMap<>();
    private String                          firstId;

    public void getInducts() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("no_tree", "1");
        map.put("parent_id", "0");
        map.put("include", "children.children");
        DataManager.getInstance().getInducts(new DefaultSingleObserver<HttpResult<List<Params>>>() {
            @Override
            public void onSuccess(HttpResult<List<Params>> result) {


                if (result != null) {
                    datas = result.getData();
                    firstId = result.getData().get(0).id;
                    for (Params par : result.getData()) {
                        StoreCategoryDto dto = new StoreCategoryDto();
                        dto.setId(Long.valueOf(par.id));
                        dto.title = par.title;
                        dtos.add(dto);
                        List<GoodsAttrDto> gadList = new ArrayList<>();
                        for (Params ms : par.children.data) {
                            GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                            goodsAttrDto.setKey(ms.title);
                            List<BaseDto> basList = new ArrayList<>();
                            for (Params mss : ms.children.data) {
                                BaseDto baseDto = new BaseDto();
                                baseDto.id = mss.id;
                                baseDto.name = mss.title;
                                basList.add(baseDto);
                            }
                            goodsAttrDto.data = basList;
                            gadList.add(goodsAttrDto);
                        }
                        maps.put(par.id, gadList);
                    }
                    mClassifyOneAdapter.setNewData(dtos);
                    goodsAttrAdapters.setNewData(maps.get(firstId + ""));

                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);

    }

    public void getTagsList() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("group_by_type", "1");
        map.put("type", "onsale,service");
        DataManager.getInstance().getAllTags(new DefaultSingleObserver<HttpResult<Param>>() {
            @Override
            public void onSuccess(HttpResult<Param> result) {
                dissLoadDialog();
                gadList.clear();
                if (result != null) {

                    if (result.getData().onsale != null) {
                        GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                        goodsAttrDto.setKey("优惠和权益");
                        goodsAttrDto.data = result.getData().onsale;
                        gadList.add(goodsAttrDto);
                    }
                    if (result.getData().service != null) {
                        GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                        goodsAttrDto.setKey("服务");
                        goodsAttrDto.data = result.getData().service;
                        gadList.add(goodsAttrDto);

                    }
                    if (result.getData().search_price != null && result.getData().search_price.size() > 0) {
                        List<BaseDto> lists = new ArrayList<>();
                        for (List<String> dist : result.getData().search_price) {
                            BaseDto baseDto = new BaseDto();
                            baseDto.name = dist.get(0);
                            baseDto.id = dist.get(1) + ":::" + dist.get(2);
                            lists.add(baseDto);
                        }
                        GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                        goodsAttrDto.setKey("价格");
                        goodsAttrDto.data = lists;
                        gadList.add(goodsAttrDto);
                    }
                    if (result.getData().search_distance != null && result.getData().search_distance.size() > 0) {
                        List<BaseDto> lists = new ArrayList<>();
                        for (String dist : result.getData().search_distance) {
                            BaseDto baseDto = new BaseDto();
                            baseDto.name = dist;
                            baseDto.id = dist;
                            lists.add(baseDto);
                        }
                        GoodsAttrDto goodsAttrDto = new GoodsAttrDto();
                        goodsAttrDto.setKey("距离");
                        goodsAttrDto.data = lists;
                        gadList.add(goodsAttrDto);
                    }
                    goodsAttrAdapter.setNewData(gadList);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);

    }

    private void getLocation() {

        DataManager.getInstance().getLocation(new DefaultSingleObserver<HttpResult<AreaDto>>() {
            @Override
            public void onSuccess(HttpResult<AreaDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        location = result.getData().getName();
                        tv_location.setText(location);
                        tvLocation.setText(location);
                        tvLocations.setText(location);

                        sitId = result.getData().getId();

                        getArea(sitId);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private void getArea(String parendId) {
        //showLoadDialog();
        Map<String, String> map = new HashMap<String, String>();
        map.put("parent_id", parendId);
        DataManager.getInstance().getAreaList(new DefaultSingleObserver<List<AreaDto>>() {
            @Override
            public void onSuccess(List<AreaDto> areaDtos) {
                //dissLoadDialog();
                firstList.clear();
                if (areaDtos != null) {
                    FirstClassItem firstClassItemNear = new FirstClassItem();
                    firstClassItemNear.setName("附近");
                    firstClassItemNear.setId(1963);
                    firstClassItemNear.setSecondList(secondList);
                    firstList.add(firstClassItemNear);
                    for (int i = 0; i < areaDtos.size(); i++) {
                        FirstClassItem firstClassItem = new FirstClassItem();
                        firstClassItem.setId(Integer.valueOf(areaDtos.get(i).getId()));
                        firstClassItem.setName(areaDtos.get(i).getName());
                        firstClassItem.setSecondList(secondLists);
                        firstList.add(firstClassItem);
                    }
                }
                initLevelsNearPopup();
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, map);
    }

    private String industryId;
    private String onsaleId;
    private String serviceId;
    private String distanceId;
    private String per_consumption;
    private String area_id;
    private String shop_name;

    private void getShopList(int loadtype) {
        //showLoadDialog();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("extra_include", "distance,industry,onsale,service,area");
        if (TextUtil.isNotEmpty(industryId)) {
            map.put("industry", industryId);
        }
        if (TextUtil.isNotEmpty(onsaleId)) {
            map.put("onsale", onsaleId);
        }
        if (TextUtil.isNotEmpty(serviceId)) {
            map.put("service", serviceId);
        }
        if (TextUtil.isNotEmpty(distance)) {
            map.put("filter[scopeDistanceIn]", distance);
        }
        if (TextUtil.isNotEmpty(per_consumption)) {
            map.put("per_consumption", per_consumption);
        }
        if (TextUtil.isNotEmpty(area_id)) {
            map.put("filter[area_id]", area_id);
        }
        if (TextUtil.isNotEmpty(shop_name)) {
            map.put("filter[shop_name]", shop_name);
        }

        DataManager.getInstance().getShopList(new DefaultSingleObserver<HttpResult<List<RecommendListDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<RecommendListDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        onDataLoaded(loadtype, result.getData().size() == Constants.PAGE_SIZE, result.getData());
//
//                        List<RecommendListDto> recommendListDtoList = result.getData();
//                        storeLists.addAll(recommendListDtoList);
//                        mEntityStoreAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, map);
    }
    private List<RecommendListDto> dats  = new ArrayList<>();
    public void onDataLoaded(int loadType, final boolean haveNext, List<RecommendListDto> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            dats.clear();
            for (RecommendListDto info : list) {
                dats.add(info);
            }
        } else {
            for (RecommendListDto info : list) {
                dats.add(info);
            }
        }

        mEntityStoreAdapter.setNewData(dats);

        mEntityStoreAdapter.notifyDataSetChanged();
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
                        getShopList(TYPE_PULL_MORE);
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
     private LinearLayout   store_layout_back;
    private ClearEditText  find;
    private LinearLayout store_ll_location;
    private TextView       tv_location;
    private  Banner         banner;
    private  RelativeLayout layout_all;
    private  RelativeLayout layout_all1;
    private  RelativeLayout layout_all2;
    private TextView       tv_all;
    private ImageView      iv_all;
    private RelativeLayout layout_near;
    private RelativeLayout layout_near1;
    private RelativeLayout layout_near2;
    private RelativeLayout layout_sx;
    private RelativeLayout layout_sx1;
    private RelativeLayout layout_sx2;

    private TextView        tv_near;
   private ImageView       iv_near;

    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.layout_entity_headview, null);
        banner = mHeaderView.findViewById(R.id.banner);
        store_layout_back = mHeaderView.findViewById(R.id.store_layout_back);
        find = mHeaderView.findViewById(R.id.find);
        store_ll_location = mHeaderView.findViewById(R.id.store_ll_location);
        tv_location = mHeaderView.findViewById(R.id.tv_location);
        layout_all = mHeaderView.findViewById(R.id.layout_all);
        tv_all = mHeaderView.findViewById(R.id.tv_all);
        iv_all = mHeaderView.findViewById(R.id.iv_all);
        layout_near = mHeaderView.findViewById(R.id.layout_near);
        layout_sx = mHeaderView.findViewById(R.id.layout_sx);
        tv_near = mHeaderView.findViewById(R.id.tv_near);
        iv_near = mHeaderView.findViewById(R.id.iv_near);
        mEntityStoreAdapter.addHeaderView(mHeaderView);
    }

    private LinearLayoutManager layoutManager;
    private final int TYPE_PULL_REFRESH = 888;
    private final int TYPE_PULL_MORE    = 889;
    private       int currpage          = 1;//第几页
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private void initAdapter() {

        mEntityStoreAdapter = new EntityStoreAdapter(storeLists, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(EntityStoreActivity.this);

        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        layoutManager = new LinearLayoutManager(this);
        //        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mEntityStoreAdapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                getShopList(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);

        initHeaderView();
    }


    private TextView tvLocats;
    private TextView finds1;
    private TextView finds2;
    private TextView finds3;

    private void initLevelsNearPopup() {

        levelsPopupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.levels_popup_layout, null);
        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);
        tvLocats = (TextView) view.findViewById(R.id.tv_location);
        finds1 = (TextView) view.findViewById(R.id.find);
        iv_backss = (ImageView) view.findViewById(R.id.iv_back);
        layout_sx1 = (RelativeLayout) view.findViewById(R.id.layout_sx);
        layout_all2 = (RelativeLayout) view.findViewById(R.id.layout_all);
        tvLocats.setText(location);
        levelsPopupWindow.setContentView(view);
        tvLocats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("from", "entityStores");
                gotoActivity(SelectCityActivity.class, false, bundle, GETCITY);
            }
        });
        layout_sx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                levelsSxPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);

            }
        });
        layout_all2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                if (levelsAllPopupWindow == null) {
                    return;
                }
                if (levelsAllPopupWindow.isShowing()) {
                    levelsAllPopupWindow.dismiss();

                }
                levelsAllPopupWindow.showAtLocation(findViewById(R.id.store_div_line_three),Gravity.TOP,0,0);
            }
        });

        int height = getResources().getDisplayMetrics().heightPixels;// 屏幕的高
        levelsPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_radius000));
        levelsPopupWindow.setFocusable(false);
        levelsPopupWindow.setClippingEnabled(false);
        levelsPopupWindow.setHeight(height + 120);
        levelsPopupWindow.setWidth(ScreenUtils.getScreenW(this));
        levelsPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                leftLV.setSelection(0);
                rightLV.setSelection(0);
            }
        });
        iv_backss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
            }
        });
        finds1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelsPopupWindow.dismiss();
                find.requestFocus();
            }
        });
        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
        //加载一级分类
        final FirstClassAdapter firstAdapter = new FirstClassAdapter(this, firstList);
        leftLV.setAdapter(firstAdapter);
        //加载左侧第一行对应右侧二级分类
        secondList = new ArrayList<SecondClassItem>();
        secondList.addAll(firstList.get(0).getSecondList());
        final SecondClassAdapter secondAdapter = new SecondClassAdapter(this, secondList);
        rightLV.setAdapter(secondAdapter);
        //左侧ListView点击事件
        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //二级数据
                List<SecondClassItem> list2 = firstList.get(position).getSecondList();
                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }
                //根据左侧一级分类选中情况，更新背景色
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                //如果没有二级类，则直接跳转
                if (list2 == null || list2.size() == 0) {
                    levelsPopupWindow.dismiss();
                    iv_near.setImageResource(R.mipmap.icon_arrow_up);
                    tv_near.setTextColor(getResources().getColor(R.color.my_color_black));
                    int firstId = firstList.get(position).getId();
                    String selectedName = firstList.get(position).getName();
                    area_id=firstId+"";
                    distance="";
                    getShopList(TYPE_PULL_REFRESH);

//                    handleResult(firstId, "", selectedName);

                }

                //显示右侧二级分类
                updateSecondListView(list2, secondAdapter);
            }
        });

        //右侧ListView点击事件
        rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow，显示用户选择的分类
                //根据左侧一级分类选中情况，更新背景色
                SecondClassAdapter adapter = (SecondClassAdapter) (parent.getAdapter());
                //如果上次点击的就是这一个item，则不进行任何操作
                if (adapter.getSelectedPosition() == position) {
                    return;
                }
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                levelsPopupWindow.dismiss();

                int firstPosition = firstAdapter.getSelectedPosition();
                int firstId = firstList.get(firstPosition).getId();
                int secondId = firstList.get(firstPosition).getSecondList().get(position).getId();
                String selectedName = secondList.get(position)
                        .getName();
                area_id="";
                distance=selectedName+"";
                getShopList(TYPE_PULL_REFRESH);
            }
        });
    }

    //刷新右侧ListView
    private void updateSecondListView(List<SecondClassItem> list2, SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void handleResult(int firstId, String secondId, String selectedName) {
        //String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        //mainTab1TV.setText(selectedName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GETCITY:
                    //CityDto city= (CityDto) data.getExtras().getSerializable("city");
                    //if(city!= null) {
                    //    String cityName = city.getCityName();
                    //    tv_location.setText(cityName.substring(0,cityName.length()-1));
                    //}
                    String id = data.getExtras().getString("id");
                    getArea(id);
                    break;
            }
        }
    }
}
