package com.smg.variety.view.fragments;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.eventbus.LiveSearch;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.AttentionCommunityAdapter;
import com.smg.variety.view.adapter.AttentionLiveRoomAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 关注 社区
 */
public class AttentionLiveRoomFragment extends BaseFragment {
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    private AttentionLiveRoomAdapter mAdapter;
    private int                      mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_live_room;
    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initRecyclerView();
    }
    private String content;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LiveSearch event) {
        mPage = 1;
        if(event.state==1){
            content=event.content;
            mPage = 1;
            liveVideos(TYPE_PULL_REFRESH);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        mPage = 1;
        liveVideos(TYPE_PULL_REFRESH);
    }
    private void liveVideos(int loadtype) {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();


       if(TextUtil.isNotEmpty(content)){
           map.put("filter[title]",  content);
       }
        map.put("page", mPage + "");
        map.put("live_type",  "1");

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
            mPage = 1;
            list.clear();
            list.addAll(lists);
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
                    mPage++;
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
    protected void initListener() {

    }




    private int mCurrentPage = Constants.PAGE_NUM;


    private LinearLayoutManager mLinearLayoutManager;

    private       SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int                                  TYPE_PULL_REFRESH = 888;
    private final int                                  TYPE_PULL_MORE    = 889;
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new AttentionLiveRoomAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mAdapter);


        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                mPage = 1;
                liveVideos(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);
    }
}
