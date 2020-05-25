package com.smg.variety.rong.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.GroupListDto;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ErrorUtil;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.adapter.GroupListAdapter;

import java.util.List;

import butterknife.BindView;
import io.rong.imlib.model.Conversation;

public class GroupListActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    @BindView(R.id.ll_search)
    View ll_search;
    GroupListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_list_layout;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(ll_search, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("from_group", "from_group");
            gotoActivity(ChatSearchActivity.class,false, bundle);
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupListDto groupBean = mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(ConversationActivity.TARGET_ID, groupBean.getId());
                bundle.putString(ConversationActivity.TITLE, groupBean.getGroup_name());
                bundle.putInt(ConversationActivity.CONVERSATION_TYPE, Conversation.ConversationType.GROUP.getValue());
                gotoActivity(ConversationActivity.class, false, bundle);
            }
        });
    }

    private void initAdapter() {
        mAdapter = new GroupListAdapter();
        recy_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recy_view.setAdapter(mAdapter);
    }

    private void getGroupList(String type, String status) {
        showLoadDialog();
        DataManager.getInstance().getGroupList(new DefaultSingleObserver<HttpResult<List<GroupListDto>>>() {
                @Override
                public void onSuccess(HttpResult<List<GroupListDto>> result) {
                    dissLoadDialog();
                    if(result.getData() != null){
                        dealData(result.getData());
                    }
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
                    dissLoadDialog();
                }
            }, type, status);
    }

    private void dealData(List<GroupListDto> groupBeanList) {
        if (groupBeanList != null && groupBeanList.size() > 0) {
            mAdapter.setNewData(groupBeanList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupList("1", "1");
    }
}
