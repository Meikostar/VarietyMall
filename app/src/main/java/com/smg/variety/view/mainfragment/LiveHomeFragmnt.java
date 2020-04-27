package com.smg.variety.view.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.bean.AreaDto;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.LiveCatesBean;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.LiveCheckFailActivity;
import com.smg.variety.view.activity.LiveCheckingActivity;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.activity.MessageFeedbackActivity;
import com.smg.variety.view.activity.RequestLivePermissionActivity;
import com.smg.variety.view.activity.SearchLiveActivity;
import com.smg.variety.view.activity.StartLiveActivity;
import com.smg.variety.view.adapter.Areadapter;
import com.smg.variety.view.adapter.BannerLiveAdapter;
import com.smg.variety.view.adapter.ConsumePushAdapter;
import com.smg.variety.view.adapter.ConturyAdapter;
import com.smg.variety.view.adapter.ConturyCagoriadapter;
import com.smg.variety.view.adapter.LiveItemAdapter;
import com.smg.variety.view.widgets.banner.BannerBaseAdapter;
import com.smg.variety.view.widgets.banner.BannerView;
import com.smg.variety.view.widgets.tablayout.SlidingScaleTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 *
 */
public class LiveHomeFragmnt extends BaseFragment {


    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;

    private int mCurrentPage = Constants.PAGE_NUM;

    private int                 mPage = 1;
    private LinearLayoutManager mLinearLayoutManager;

    private       SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int                                  TYPE_PULL_REFRESH = 888;
    private final int                                  TYPE_PULL_MORE    = 889;

    @Override
    public void initListener() {

    }
    private Handler mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });

    private int state;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_live_home;
    }

    //    private SpikeChooseTimeAdapter testAdapter;

    private String id;

    @Override
    public void initView() {


        mProcutAdapter = new LiveItemAdapter(list, getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mProcutAdapter);

        initHeaderView();
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage = 1;
                liveVideos(TYPE_PULL_REFRESH);
                liveVideoBans();
                mHandler.sendEmptyMessageDelayed(1,1500);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);
        getLiveCates();
        liveVideoBan();


    }

    private List<AreaDto>         datas  = new ArrayList<>();
    private List<AreaDto>         datass = new ArrayList<>();
    private List<VideoLiveBean>   lists  = new ArrayList<>();
    private BannerItemDto         ban;
    private BannerLiveAdapter     bannerAdapter;
    private SlidingScaleTabLayout tablayout;

    private BannerView            bannerView;
    private ImageView            iv_more;
    private ImageView            iv_search;
    private ViewPager             viewPager;
    private String[]              titles ;
    private View mHeaderView;
    private void initHeaderView() {
         mHeaderView = View.inflate(getActivity(), R.layout.live_home_head_view, null);
        tablayout = mHeaderView.findViewById(R.id.tablayout);
        mPointContainer = mHeaderView.findViewById(R.id.ll_bg);
        viewPager = mHeaderView.findViewById(R.id.viewpager);
        bannerView = mHeaderView.findViewById(R.id.bannerView);
        iv_more = mHeaderView.findViewById(R.id.iv_more);
        iv_search = mHeaderView.findViewById(R.id.iv_search);

        bannerAdapter = new BannerLiveAdapter(getActivity());
        bannerAdapter.setScrollPostionListener(new BannerBaseAdapter.ScrollPostionListener() {
            @Override
            public void selectPosition(int position) {
                changePoint(position);
            }
        });
        bannerView.setAdapter(bannerAdapter);

        bannerAdapter.setOnPageTouchListener(new BannerBaseAdapter.OnPageTouchListener() {
            @Override
            public void onPageClick(int position, Object o) {

            }

            @Override
            public void onPageDown() {

            }

            @Override
            public void onPageUp() {

            }
        });
        mProcutAdapter.addHeaderView(mHeaderView);
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SearchLiveActivity.class);
            }
        });

    }
    private int mPointCount;
    /**
     *  圆点布局
     */
    private LinearLayout mPointContainer;





    /**
     *  圆点图片
     */
    private ImageView[] mPoints;
    /**
     *  加载圆点
     */
    private void initPoint() {
        if (mPointCount == 0) {
            return;
        }

        mPoints = new ImageView[mPointCount];
        // 清chu所有圆点
        mPointContainer.removeAllViews();
        for (int i=0;i < mPointCount;i++) {
            ImageView view = new ImageView(getContext());
            view.setImageResource(R.drawable.point_normal);
            mPointContainer.addView(view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin =14;
            params.rightMargin = 14;
            params.width=32;
            params.height=16;
            view.setLayoutParams(params);

            mPoints[i] = view;
        }
        if (mPoints[0] != null) {
            mPoints[0].setImageResource(R.drawable.point_selected);
        }
        mLastPos = 0;
    }
    public  int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    private int mLastPos;
    /**
     * 改变圆点位置
     */
    private void changePoint(int currentPoint) {
        if (mLastPos == currentPoint) {
            return;
        }
        mPoints[currentPoint].setImageResource(R.drawable.point_selected);
        mPoints[mLastPos].setImageResource(R.drawable.point_normal);
        mLastPos = currentPoint;
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return couts;
        }
        private int couts;
        public void setCount(int cout){
            couts=cout;
        }
        @Override
        public int getItemPosition(@NonNull Object object) {
            View view = (View) object;
            return (int) view.getTag();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return "标题位置" + position;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TextView textView = new TextView(getActivity());
            textView.setTag(position);
            container.addView(textView);
            return textView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
    private List<String> tls = new ArrayList<>();
    private  PopupWindow mPopWindow;

    //popowindow  使用
    private void showPopupWindow() {

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.tongxunlu_popuplayout, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);


        LinearLayout llZb = contentView.findViewById(R.id.ll_zb);
        LinearLayout llFk = contentView.findViewById(R.id.ll_fk);

        llZb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                    gotoActivity(LoginActivity.class);
                    return;
                }
                isPlayer();
//                gotoActivity(StartLiveActivity.class);
            }

        });
        llFk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindow.dismiss();
                gotoActivity(MessageFeedbackActivity.class);

            }
        });
        //tongxunluRecy.setOnClickListener(TongXunLuFragment.this);

        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);

        mPopWindow.showAsDropDown(iv_more, 0, 0);
    }
    private void isPlayer() {
        //showLoadDialog();
        DataManager.getInstance().isliveing(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto result) {
                //dissLoadDialog();


                if (result != null) {

                    if (result.is_realname) {
                        if (result.apply_check_status == 0) {
                            applyLives();

                            //                            gotoActivity(RequestLivePermissionActivity.class);
                        } else if (result.apply_check_status == 1) {
                            gotoActivity(LiveCheckingActivity.class);

                        } else if (result.apply_check_status == 2) {
                            if (result.is_live) {
                                gotoActivity(StartLiveActivity.class);
                            } else {

                            }

                        } else if (result.apply_check_status == 3) {
                            Bundle bundle = new Bundle();
                            bundle.putString("reasonTip", "");
                            gotoActivity(LiveCheckFailActivity.class, false, bundle);


                        }

                    } else {
                        if (BaseApplication.real_state.equals("-1")) {
                            gotoActivity(RequestLivePermissionActivity.class);
                        } else if (BaseApplication.real_state.equals("0")) {
                            ToastUtil.showToast("实名认证中，请耐心等待审核!");
                        } else if (BaseApplication.real_state.equals("1")) {

                        } else if (BaseApplication.real_state.equals("2")) {
                            gotoActivity(RequestLivePermissionActivity.class);
                        }

                    }


                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                if (ApiException.getInstance().getCode() == 500) {
                    gotoActivity(RequestLivePermissionActivity.class);
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        });
    }
    public void applyLives() {
        //        showLoadDialog();
        DataManager.getInstance().applyLive(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                //                dissLoadDialog();
                gotoActivity(LiveCheckingActivity.class);

                //                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    gotoActivity(LiveCheckingActivity.class);
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                //                dissLoadDialog();
            }
        });
    }
    public void getLiveCates() {

        //        showLoadDialog();
        DataManager.getInstance().getLiveCates(new DefaultSingleObserver<HttpResult<List<LiveCatesBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<LiveCatesBean>> result) {

                if (result != null && result.getData() != null && result.getData().size() > 0) {

                    tls.clear();
                    for (LiveCatesBean bean : result.getData()) {
                        tls.add(bean.cat_name);
                    }

                    if (!TextUtil.isEmpty(result.getData().get(0).getId())) {
                        id = result.getData().get(0).getId();
                        currpage = 1;
                        liveVideos(TYPE_PULL_REFRESH);
                    }

                    titles = tls.toArray(new String[tls.size()]);
                    MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
                    myViewPagerAdapter.setCount(tls.size());
                    viewPager.setAdapter(myViewPagerAdapter);
                    viewPager.setOffscreenPageLimit(tls.size()-1);

                    tablayout.setViewPager(viewPager, titles);
                    tablayout.setmTabsContainer(new SlidingScaleTabLayout.TabSelectionListener() {
                        @Override
                        public void selection(int poition) {
                            id = result.getData().get(poition).getId();
                            currpage = 1;
                            liveVideos(TYPE_PULL_REFRESH);
                        }
                    });
                    viewPager.setCurrentItem(0);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, 0);
    }




    private LiveItemAdapter      mProcutAdapter;
    private ConturyCagoriadapter mCagoriadapter;
    private int                  currpage = 1;


    private void liveVideoBan() {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

//        map.put("filter[live_video_cate_id]", id);
        map.put("per_page", 200 + "");
        map.put("page", currpage + "");
        map.put("live_type", "2");
        map.put("filter[is_recommended]",  "1");
        map.put("include", "apply,room");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();
              if(result!=null &&result.getData()!=null&&result.getData().size()>0){
                  videos.clear();
                  int size=result.getData().size()/2;
                  int st=result.getData().size()%2;
                  if(size>0){
                      for(int i=0;i<size;i++){
                          VideoLiveBean videoLiveBean = new VideoLiveBean();
                          videoLiveBean.one=result.getData().get(2*i);
                          videoLiveBean.two=result.getData().get(2*i+1);
                          videos.add(videoLiveBean);
                      }
                      if(st!=0){
                          VideoLiveBean videoLiveBean = new VideoLiveBean();
                          videoLiveBean.one=result.getData().get(2*size);
                          videos.add(videoLiveBean);
                      }
                  }else {
                      VideoLiveBean videoLiveBean = new VideoLiveBean();
                      videoLiveBean.one=result.getData().get(0);
                      videos.add(videoLiveBean);

                  }
                  mPointCount=videos.size();
                  initPoint();
                  bannerAdapter.setData(videos);
              }
            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();


            }
        }, map);
    }
    private void liveVideoBans() {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

//        map.put("filter[live_video_cate_id]", id);

        map.put("page", currpage + "");
        map.put("per_page", 200 + "");
        map.put("live_type", "2");
        map.put("filter[is_recommended]",  "1");
        map.put("include", "apply,room");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();
              if(result!=null &&result.getData()!=null&&result.getData().size()>0){
                  mProcutAdapter.setNewData(list);
                  mProcutAdapter.notifyDataSetChanged();
                  videos.clear();
                  int size=result.getData().size()/2;
                  int st=result.getData().size()%2;
                  if(size>0){
                      for(int i=0;i<size;i++){
                          VideoLiveBean videoLiveBean = new VideoLiveBean();
                          videoLiveBean.one=result.getData().get(2*i);
                          videoLiveBean.two=result.getData().get(2*i+1);
                          videos.add(videoLiveBean);
                      }
                      if(st!=0){
                          VideoLiveBean videoLiveBean = new VideoLiveBean();
                          videoLiveBean.one=result.getData().get(2*size);
                          videos.add(videoLiveBean);
                      }
                  }else {
                      VideoLiveBean videoLiveBean = new VideoLiveBean();
                      videoLiveBean.one=result.getData().get(0);
                      videos.add(videoLiveBean);

                  }
                  bannerAdapter.setData(videos);
                  mPointCount=videos.size();
                  initPoint();

              }
            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();

                mProcutAdapter.setNewData(list);
                mProcutAdapter.notifyDataSetChanged();
            }
        }, map);
    }
    private void liveVideos(int loadtype) {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

        map.put("filter[live_video_cate_id]", id);

        map.put("page", currpage + "");
        map.put("include", "apply,room,videoproducts,user");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();
                onDataLoaded(loadtype, result.getData().size() == Constants.PAGE_SIZE, result.getData());

            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();


            }
        }, map);
    }

    public void onDataLoaded(int loadType, boolean haveNext, List<VideoLiveBean> lists) {

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
                        liveVideos(TYPE_PULL_MORE);
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

    private List<VideoLiveBean> list = new ArrayList<>();
    private List<VideoLiveBean> videos = new ArrayList<>();


    @Override
    public void initData() {


    }


}
