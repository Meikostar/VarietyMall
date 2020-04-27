package com.smg.variety.view.mainfragment.consume;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.ShopInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.MessageCenterActivity;
import com.smg.variety.view.adapter.ConsumePushAdapter;
import com.smg.variety.view.widgets.RecyclerItemDecoration;
import com.smg.variety.view.widgets.autoview.CustomView;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 品牌店铺详情
 * Created by rzb on 2019/4/22.
 */
public class BrandShopDetailActivity extends BaseActivity {
    public static final String SHOP_DETAIL_ID = "shop_detail_id";
    public static final String MALL_TYPE      = "mall_type";


    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;

    private int                    mCurrentPage = Constants.PAGE_NUM;


    private int                 currpage        = 1;
    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int                            TYPE_PULL_REFRESH = 888;
    private final int                            TYPE_PULL_MORE = 889;
    private String                               id;
    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_shop_detail;
    }

    @Override
    public void initView() {
        id=getIntent().getStringExtra("id");



        mAdapter = new ConsumePushAdapter(goodsLists, this);


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


        mSuperRecyclerView.setAdapter(mAdapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage=1;
                getStProductList(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);




        tvShopProduct1.setSelected(true);
        tvShopProduct2.setSelected(false);
        tvShopProduct3.setSelected(false);
    }

    @Override
    public void initData() {

        getStProductList(TYPE_PULL_REFRESH);
        getShopDetailInfo();
    }
    private void setTopTitlesView(int poition) {
        mCurrentPage = 1;

        switch (poition) {
            case 1:
                sortStr="";
                ivShopProduct3.setTag("select");
                tvShopProduct1.setSelected(true);
                tvShopProduct2.setSelected(false);
                tvShopProduct3.setSelected(false);
                ivShopProduct3.setImageResource(R.mipmap.arrow_defaut);
                getStProductList(TYPE_PULL_REFRESH);
                break;
            case 2:
                sortStr="-sales_count";
                isNew=1;
                ivShopProduct3.setTag("select");
                tvShopProduct1.setSelected(false);
                tvShopProduct2.setSelected(true);
                tvShopProduct3.setSelected(false);
                ivShopProduct3.setImageResource(R.mipmap.arrow_defaut);
                getStProductList(TYPE_PULL_REFRESH);

                break;
            case 3:
                tvShopProduct1.setSelected(false);
                tvShopProduct2.setSelected(false);
                tvShopProduct3.setSelected(true);
                setTopTitles3View();
                break;

        }
    }



    private   ShopInfoDto data;
    private List<BannerItemDto> lists=new ArrayList<>();
    private void getShopDetailInfo() {


        DataManager.getInstance().getBrandInfo(new DefaultSingleObserver<HttpResult<ShopInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<ShopInfoDto> countOrderBean) {

                if (countOrderBean != null && countOrderBean.getData() != null) {
                    data = countOrderBean.getData();
                    if(data!=null){
                        lists.clear();
                        GlideUtils.getInstances().loadRoundCornerImg(BrandShopDetailActivity.this,ivImg,3,data.logo,R.drawable.moren_product);
                        if(data.ext!=null&&data.ext.brand_page_background!=null){
//                            for(String url:data.ext.imgs){
                                BannerItemDto dto=new BannerItemDto();
                                dto.setPath(data.ext.brand_page_background);
                                lists.add(dto);
//                            }
                        }
                        startBanner(lists);
                        if(TextUtil.isNotEmpty(data.name)){
                            tvTitle.setText(data.name);

                        }
                        String attion="";
                        if(TextUtil.isNotEmpty(data.followersCount)){
                            attion=data.followersCount;
                        }else {
                            attion="0";
                        }
                        if(data.category!=null&&data.category.data!=null&&TextUtil.isNotEmpty(data.category.data.title)){
                            tvContent.setText(data.category.data.title);
                        }else {
                            tvContent.setText("");
                        }

                        if(data.isFollowed){

                            tvShopFollow.setText("已关注");
                            tvShopFollow.setTextColor(getResources().getColor(R.color.my_color_FC6B00));
                            tvShopFollow.setBackgroundResource(R.drawable.shape_radius_orgin_14);
                        }else {
                            tvShopFollow.setText(data.shop_name);
                            tvShopFollow.setText("+关注");
                            tvShopFollow.setBackgroundResource(R.drawable.shape_radius_14);
                            tvShopFollow.setTextColor(getResources().getColor(R.color.my_color_333333));
                        }


                            if(TextUtil.isNotEmpty(data.description)){
                                tv_detail.setText(data.description);
                                tv_detail.setVisibility(View.VISIBLE);
                            }else {
                                tv_detail.setVisibility(View.VISIBLE);
                            }




                    }

                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, id,"followersCount,isFollowed,category");
    }


    @Override
    public void initListener() {

        tvShopProduct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopTitlesView(1);
            }
        });
        tvShopProduct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopTitlesView(2);
            }
        });
        llShopProduct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopTitlesView(3);
            }
        });
        ivLabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });
        actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        bindClickEvent(tvShopFollow, () -> {
            //关注和取消关注
            if (isFollow) {
                deleteAttention();
            } else {
                postAttention();
            }
        });
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (TextUtil.isNotEmpty(charSequence.toString())) {
//
//                    searchKey = charSequence.toString();
//                } else {
//                    searchKey = "";
//                    getStProductList();
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                //点击搜索要做的操作
                if (TextUtil.isNotEmpty(etSearch.getText().toString())) {

                    searchKey = etSearch.getText().toString();

                } else {
                    searchKey = "";


                }
                getStProductList(TYPE_PULL_REFRESH);
                return false;
            }
        });

    }
    private List<NewListItemDto>   goodsLists   = new ArrayList<NewListItemDto>();

    private Banner    banner;
    private ImageView acbStatusBar;
    private ImageView actionbarBack;
    private EditText  etSearch;

    private ImageView ivLabe;
    private ImageView ivImg;
    private TextView  tvTitle;
    private TextView  tv_detail;
    private TextView                tvContent;
    private  TextView                tvShopFollow;
    private TextView                tvShopProduct1;
    private TextView                tvShopProduct2;
    private TextView                tvShopProduct3;
    private ImageView               ivShopProduct3;
    private LinearLayout       llShopProduct3;
    private TextView           tvShopProduct4;
    private ImageView          ivShopProduct4;
    private LinearLayout       llShopProduct4;
    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.layout_shop_details, null);
        banner = mHeaderView.findViewById(R.id.banner);
        acbStatusBar = mHeaderView.findViewById(R.id.acb_status_bar);
        actionbarBack = mHeaderView.findViewById(R.id.actionbar_back);
        etSearch = mHeaderView.findViewById(R.id.et_search);
        ivLabe = mHeaderView.findViewById(R.id.iv_labe);
        ivImg = mHeaderView.findViewById(R.id.iv_img);
        tvTitle = mHeaderView.findViewById(R.id.tv_title);
        tv_detail = mHeaderView.findViewById(R.id.tv_detail);
        tvContent = mHeaderView.findViewById(R.id.tv_content);
        tvShopFollow = mHeaderView.findViewById(R.id.tv_attention);
        tvShopProduct1 = mHeaderView.findViewById(R.id.tv_shop_product_1);
        tvShopProduct2 = mHeaderView.findViewById(R.id.tv_shop_product_2);
        tvShopProduct3 = mHeaderView.findViewById(R.id.tv_shop_product_3);
        tvShopProduct4= mHeaderView.findViewById(R.id.tv_shop_product_4);
        ivShopProduct3 = mHeaderView.findViewById(R.id.iv_shop_product_3);
        llShopProduct3 = mHeaderView.findViewById(R.id.ll_shop_product_3);
        ivShopProduct3 = mHeaderView.findViewById(R.id.iv_shop_product_4);
        llShopProduct4 = mHeaderView.findViewById(R.id.ll_shop_product_4);
        mAdapter.addHeaderView(mHeaderView);
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
                    GlideUtils.getInstances().loadNormalImg(BrandShopDetailActivity.this, imageView, imgStr, R.drawable.img_default_three);
                } else {
                    GlideUtils.getInstances().loadNormalImg(BrandShopDetailActivity.this, imageView,  imgStr,  R.drawable.img_default_three);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
//                    if(slidersDto.getClick_event_type().equals("product_default")){
//                        Bundle bundle = new Bundle();
//                        bundle.putString(CommodityDetailActivity.FROM, "gc");
//                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
//                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
//                        Intent intent = new Intent(context, CommodityDetailActivity.class);
//                        if (bundle != null) {
//                            intent.putExtras(bundle);
//                        }
//                        startActivity(intent);
//                    }else if(slidersDto.getClick_event_type().equals("seller_default")){
//                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
//                        intent.putExtra("id",slidersDto.getClick_event_value());
//                        context.startActivity(intent);
//                    }
                }
            });

        }
    }
    private void setTopTitles3View() {
        mCurrentPage = 1;

        if (ivShopProduct3.getTag().equals("select")) {

            ivShopProduct3.setTag("unselect");
            ivShopProduct3.setImageResource(R.mipmap.shop_product_price_drop);
            sortStr="-price";
        } else {

            ivShopProduct3.setTag("select");
            ivShopProduct3.setImageResource(R.mipmap.shop_product_price_litre);
            sortStr="price";
        }
        getStProductList(TYPE_PULL_REFRESH);
    }

    private ConsumePushAdapter mAdapter;

    private void postAttention() {

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Mall\\Models\\MallBrand");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                getShopDetailInfo();
                isFollow = true;
                //                tvShopFollow.setText("已关注");
            }

            @Override
            public void onError(Throwable throwable) {
                getShopDetailInfo();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isFollow = true;
                    //                    tvShopFollow.setText("已关注");
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private boolean isFollow;

    private void deleteAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Mall\\Models\\MallBrand");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");
                isFollow = false;
                //                tvShopFollow.setText("+关注店铺");
                getShopDetailInfo();
            }

            @Override
            public void onError(Throwable throwable) {
                getShopDetailInfo();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isFollow = false;
//                    tvShopFollow.setText("+关注店铺");
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }
    private String sortStr;
    private int mPage;
    private int isNew;

    public void onDataLoaded(int loadType, boolean haveNext, List<NewListItemDto> lists) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            list.addAll(lists);
           if(list.size()==0){
               if (!TextUtils.isEmpty(searchKey)) {

                   ToastUtil.showToast(String.format("没搜索到%s相关数据", searchKey));
               } else {
                   ToastUtil.showToast("暂无商品数据");
               }
           }
        } else {
            list.addAll(lists);
        }

        mAdapter.setNewData(list);

        mAdapter.notifyDataSetChanged();
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
                        getStProductList(TYPE_PULL_MORE);
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
    private void getStProductList(int loadtype) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[brand_id]", id);
        map.put("page", mCurrentPage + "");

        if (!TextUtils.isEmpty(searchKey)) {
            map.put("filter[scopeSearch]", searchKey);
        }
        if(isNew!=0){
            isNew=0;
            map.put("filter[is_new]", 1 + "");
        }
        if (!TextUtils.isEmpty(sortStr)) {
            map.put("sort", sortStr);
        }
        if(map==null||map.size()==0){
            return;
        }
        DataManager.getInstance().getStProductList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                onDataLoaded(loadtype,result.getData().size()==Constants.PAGE_SIZE,result.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, "", map);
    }
    private String searchKey;
}
