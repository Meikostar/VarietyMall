package com.smg.variety.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.ConsumePushAdapter;
import com.smg.variety.view.adapter.ConturyCagoriadapter;
import com.smg.variety.view.adapter.LiveItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我是主播
 * Created by rzb on 2019/4/22.
 */
public class LiverMineActivity extends BaseActivity {
    public static final String SHOP_DETAIL_ID = "shop_detail_id";
    public static final String MALL_TYPE      = "mall_type";


    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;


    private LinearLayoutManager mLinearLayoutManager;

    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_liver_mine;
    }


    private ImageView ivImg;
    private ImageView actionbar_back;
    private ImageView iv_state;
    private TextView  tvTitle;
    private TextView  tv_gz;
    private TextView  tv_one;
    private TextView  tv_two;
    private TextView  tv_three;

    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.liver_detail_headview, null);

        ivImg = mHeaderView.findViewById(R.id.iv_img);
        actionbar_back = mHeaderView.findViewById(R.id.actionbar_back);
        iv_state = mHeaderView.findViewById(R.id.iv_state);
        tvTitle = mHeaderView.findViewById(R.id.tv_title);
        tv_gz = mHeaderView.findViewById(R.id.tv_gz);
        tv_one = mHeaderView.findViewById(R.id.tv_one);
        tv_two = mHeaderView.findViewById(R.id.tv_two);
        tv_three = mHeaderView.findViewById(R.id.tv_three);

        mProcutAdapter.addHeaderView(mHeaderView);
        actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private LiveItemAdapter mProcutAdapter;
    /**
     * 获取首页直播列表数据
     */
    private boolean isAttention;
    private String authorPhone;
    private String authorId;
    private void liveVideosInfo() {
        //        showLoadDialog();
        DataManager.getInstance().liveVideosInfo(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
                //                dissLoadDialog();

                if (result != null && result.getData() != null) {

                    tv_two.setText(result.getData().click_like_count+"" );
                    tv_one.setText(result.getData().getUser().getData().followersCount+"" );
                    if (result.getData().getUser() != null && result.getData().getUser().getData() != null) {
                        GlideUtils.getInstances().loadRoundImg(LiverMineActivity.this, ivImg, Constants.WEB_IMG_URL_UPLOADS + result.getData().getUser().getData().getAvatar(), R.drawable.moren_ren);
                        authorPhone = result.getData().getUser().getData().getPhone();
                        authorId = result.getData().getUser().getData().getId();
                        if (TextUtils.isEmpty(result.getData().getUser().getData().getName())) {
                            tvTitle.setText(authorPhone);
                        } else {
                            tvTitle.setText(result.getData().getUser().getData().getName());
                        }
                        if (!TextUtil.isEmpty(result.getData().getUser().getData().level_name)) {
                            tv_three.setText(result.getData().getUser().getData().level_name);
                        }
                        isFollow = result.getData().getUser().getData().isFollowed;
                        if (isFollow) {
                            tv_gz.setText("已关注");
                        } else {
                            tv_gz.setText("关注");
                        }
                    }

                }


            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();

            }
        }, videoId);



    }
    private String videoId;
    private String userId;
    @Override
    public void initView() {

        videoId=getIntent().getStringExtra("id");
        userId=getIntent().getStringExtra("userId");
        mProcutAdapter = new LiveItemAdapter(list, this);


        mLinearLayoutManager = new LinearLayoutManager(this);

        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mProcutAdapter);
        initHeaderView();
        liveVideosInfo();
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage = 1;
                liveVideos(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);

    }


    public void refresh() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }

    @Override
    public void initData() {

        reflash();

    }

    private void reflash() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }


    private final int TYPE_PULL_REFRESH = 888;
    private final int TYPE_PULL_MORE    = 889;
    private       int currpage          = 1;//第几页


    @Override
    public void initListener() {

        bindClickEvent(tv_gz, () -> {
            //关注和取消关注
            if (isFollow) {
                deleteAttention();
            } else {
                postAttention();
            }
        });


    }

    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();


    private ConsumePushAdapter mAdapter;

    private void postAttention() {

        Map<String, String> map = new HashMap<>();
        map.put("id", userId);
        map.put("object", "SMG\\Seller\\Seller");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                liveVideosInfo();

                //                tvShopFollow.setText("已关注");
            }

            @Override
            public void onError(Throwable throwable) {

                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    liveVideosInfo();

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
        map.put("id", userId);
        map.put("object", "SMG\\Seller\\Seller");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");

                liveVideosInfo();
                //                tvShopFollow.setText("+关注店铺");

            }

            @Override
            public void onError(Throwable throwable) {

                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    liveVideosInfo();

                    //                    tvShopFollow.setText("+关注店铺");
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }


    private ConturyCagoriadapter mCagoriadapter;


    private void liveVideos(int loadtype) {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

        map.put("liver_user_id", userId);

        map.put("page", currpage + "");
        map.put("live_type", "2");
        map.put("include", "apply,room,user,videoproducts");
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

    private List<VideoLiveBean> list = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
