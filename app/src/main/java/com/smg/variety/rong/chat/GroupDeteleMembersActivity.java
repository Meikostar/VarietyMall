package com.smg.variety.rong.chat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.GroupInfoDto;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.adapter.GroupDeteleMembersAapter;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupDeteleMembersActivity extends BaseActivity {
    public static final int GROUP_DEL = 3000;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.group_members_gv)
    NoScrollGridView group_members_gv;

    private String                   groupId;
    private GroupInfoDto             mGroupInfoDto;
    private GroupDeteleMembersAapter mGroupMembersAapter;
    private List<GroupUserItemInfoDto> userLists = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_delete_members_layout;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        mGroupMembersAapter.setRefreshListsListener(new GroupDeteleMembersAapter.RefreshListsListener() {
            @Override
            public void OnRefreshListener(boolean isFresh) {
                if(isFresh){
                    if(userLists != null){
                        userLists.clear();
                    }
                    groupInfo();
                }
            }
        });
    }


    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getString(Constants.GROUP_ID);
        }
        groupInfo();
    }

    private void groupInfo() {
          showLoadDialog();
          DataManager.getInstance().getGroupInfo(new DefaultSingleObserver<HttpResult<GroupInfoDto>>() {
             @Override
             public void onSuccess(HttpResult<GroupInfoDto> result) {
                dissLoadDialog();
                if(result.getData() != null){
                    mGroupInfoDto = result.getData();
                    initDataView();
                }
             }

             @Override
             public void onError(Throwable throwable) {
                 dissLoadDialog();
                }
            }, groupId);
    }

    private void initAdapter(){
        mGroupMembersAapter = new GroupDeteleMembersAapter(GroupDeteleMembersActivity.this, userLists);
        group_members_gv.setAdapter(mGroupMembersAapter);
    }

    private void initDataView() {
        if (mGroupInfoDto != null) {
           if(mGroupInfoDto.getGroup_users().getData() != null) {
               List<GroupUserItemInfoDto> guiDtoLists = mGroupInfoDto.getGroup_users().getData();
               List<GroupUserItemInfoDto> gList = new ArrayList<>();
               for(int i=0; i<guiDtoLists.size(); i++){
                   if(!guiDtoLists.get(i).getUser_id().equals(ShareUtil.getInstance().get(Constants.USER_ID))){
                       gList.add(guiDtoLists.get(i));
                   }
               }
               tv_number.setText("(" + gList.size() + ")");
               userLists.addAll(gList);
               mGroupMembersAapter.notifyDataSetChanged();
           }
        }
    }
}