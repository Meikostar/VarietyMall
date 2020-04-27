package com.smg.variety.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.bean.TopicListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.activity.HelpCenterActivity;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.activity.SuperYqYlActivity;
import com.smg.variety.view.adapter.GlAdapter;
import com.smg.variety.view.adapter.HelperAdapter;
import com.smg.variety.view.adapter.TopicListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 热门话题
 * Created by rzb on 2018/5/20.
 */
public class PostFragment extends BaseFragment {
    private String tab;
    @BindView(R.id.topic_refreshLayout)
    RefreshLayout topic_refreshLayout;
    @BindView(R.id.recycle_topic)
    RecyclerView  recycle_topic;
    private int                    mPage     = 1;
    private GlAdapter              mAdapter;
    private List<DetailDto> topicList = new ArrayList<DetailDto>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic_content;
    }
    public static PostFragment newInstance(String tab) {
        PostFragment fragment = new PostFragment();
        fragment.tab = tab;


        return fragment;
    }

    @Override
    protected void initView() {
        initAdapter();
    }

    @Override
    protected void initData() {
        if(tab != null) {
            getTopicRecommendList();
        }
    }

    @Override
    protected void initListener() {
        topic_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getTopicRecommendList();
            }
        });

        topic_refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getTopicRecommendList();
            }
        });
    }

    private void initAdapter() {

        recycle_topic.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new GlAdapter(getActivity(), topicList, new GlAdapter.TopListItemClick() {
            @Override
            public void onItemClick(DetailDto post, int position) {
               showYqFive(getActivity(), post.img);
            }
        });
        recycle_topic.setAdapter(mAdapter);
    }
    private  Dialog dialog;
    private  View   ll_bg=null;

    public  void showYqFive(Context context, String url1) {


            dialog = new Dialog(context, R.style.loading_dialog);
            dialog.setCancelable(true);

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.dialog_yqyl_five, null);

            ll_bg = view.findViewById(R.id.ll_bg);
            TextView tvSp = view.findViewById(R.id.tv_code);
            TextView  tvDs = view.findViewById(R.id.tv_name);

            tvSp.setText("邀请码:"+ShareUtil.getInstance().get(Constants.USER_PHONE));
            tvDs.setText(ShareUtil.getInstance().get(Constants.USER_NAME));

            ImageView tvNo = view.findViewById(R.id.iv_close);
            ImageView ivImg = view.findViewById(R.id.iv_img);
            ImageView iv_code = view.findViewById(R.id.iv_code);
            ImageView civ_user_avatar = view.findViewById(R.id.civ_user_avatar);
            GlideUtils.getInstances().loadNormalImg(context, iv_code, Constants.BASE_URL + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, ""));
            GlideUtils.getInstances().loadNormalImg(context, ivImg, url1);
            GlideUtils.getInstances().loadNormalImg(context, civ_user_avatar, ShareUtil.getInstance().get(Constants.USER_HEAD));
            tvNo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });
            ll_bg.setDrawingCacheEnabled(true);
            ll_bg.buildDrawingCache();
            TextView tvSure = view.findViewById(R.id.tv_exam_sure);
            tvSure.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                 DialogUtils.creatPicture(getActivity(),ll_bg);
                }
            });
            dialog.setContentView(view);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            WindowManager wm = ((Activity)(context)).getWindowManager();
            Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
            p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
            p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
            dialogWindow.setAttributes(p);
            dialog.show();




    }
    //获取热门话题列表(推荐)
    private void getTopicRecommendList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
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

   private HelperAdapter mAdapters;
    private String content;


    private void setData(HttpResult<List<DetailDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {

            return;
        }
        if (mPage <= 1) {
            topicList.clear();
            topicList.addAll(httpResult.getData());
            mAdapter.notifyDataSetChanged();
            if(httpResult.getData() == null || httpResult.getData().size() == 0); {
            }
            topic_refreshLayout.finishRefresh();
            topic_refreshLayout.setEnableLoadMore(true);
        } else {
            topic_refreshLayout.finishLoadMore();
            topic_refreshLayout.setEnableRefresh(true);
            topicList.addAll(httpResult.getData());
            mAdapter.notifyDataSetChanged();
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                topic_refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }



    private void postAttention(TopicListItemDto post) {
        Map<String, String> map = new HashMap<>();
        map.put("id", post.getUser_id());
        map.put("object", "Modules\\Base\\Entities\\User");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                mPage = 1;
                getTopicRecommendList();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    mPage = 1;
                    getTopicRecommendList();
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention(TopicListItemDto post) {
        Map<String, String> map = new HashMap<>();
        map.put("id", post.getUser_id());
        map.put("object", "Modules\\Base\\Entities\\User");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");
                 mPage = 1;
                getTopicRecommendList();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    mPage = 1;
                    getTopicRecommendList();
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
