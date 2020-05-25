package com.smg.variety.rong.chat;

import android.app.Activity;
import android.content.Intent;
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
import com.smg.variety.view.adapter.GroupMembersAapter;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupMembersActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.group_members_gv)
    NoScrollGridView group_members_gv;
    @BindView(R.id.btn_add)
    TextView btn_add;
    @BindView(R.id.btn_del)
    TextView btn_del;

    private String groupId;
    private GroupInfoDto mGroupInfoDto;
    private GroupMembersAapter mGroupMembersAapter;
    private List<GroupUserItemInfoDto> userLists = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_members_layout;
    }

    @Override
    public void initView() {
        initAdapter();
    }
    private boolean isFirt;

    @Override
    protected void onResume() {
        super.onResume();
        if(isFirt){
            groupInfo();
        }else {
          isFirt=true;
        }
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(btn_add, () -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, ModifyGroupActivity.GROUP_ADD);
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(ModifyGroupActivity.class, false, bundle,ModifyGroupActivity.GROUP_ADD);
        });

        bindClickEvent(btn_del, () -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, GroupDeteleMembersActivity.GROUP_DEL);
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(GroupDeteleMembersActivity.class, false, bundle, GroupDeteleMembersActivity.GROUP_DEL);
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
        mGroupMembersAapter = new GroupMembersAapter(GroupMembersActivity.this, userLists);
        group_members_gv.setAdapter(mGroupMembersAapter);

    }

    private void initDataView() {
        if (mGroupInfoDto != null) {
           if(mGroupInfoDto.getGroup_users().getData() != null) {
               List<GroupUserItemInfoDto> guiDtoLists = mGroupInfoDto.getGroup_users().getData();
               tv_number.setText("(" + guiDtoLists.size() + ")");
               userLists.clear();
               userLists.addAll(guiDtoLists);
               mGroupMembersAapter.notifyDataSetChanged();
           }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            return;
        }
//        if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
//            if (resultList != null && resultList.size() > 0) {
//                String imageUrl = resultList.get(0).getImagePath();
//                uploadImg(imageUrl);
//            }
//            return;
//        }
        //if (requestCode == ModifyGroupActivity.GROUP_DEL) {
        //            int number = data.getIntExtra(Constants.NUMBER, 0);
        //            if (number < 3) {
        //                setResult(Activity.RESULT_OK);
        //                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupId, null);
        //                Intent intent = new Intent(Constants.BROADCAST_CHAT);
        //                发送本地广播
        //                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        //                setResult(Activity.RESULT_OK);
        //                getActivity().onBackPressed();
        //                finish();
        //            } else {
        //                Bundle bundle = new Bundle();
        //                bundle.putString(Constants.GROUP_ID, data.getStringExtra(Constants.GROUP_ID));
        //                gotoActivity(GroupDetailActivity.class, true, bundle);
        //        }
        //
        //            return;
        //        } else if (requestCode == ModifyGroupActivity.GROUP_CHANGE || requestCode == ModifyGroupActivity.GROUP_ADD) {
        //            Bundle bundle = new Bundle();
        //            bundle.putString(Constants.GROUP_ID, data.getStringExtra(Constants.GROUP_ID));
        //            gotoActivity(GroupDetailActivity.class, true, bundle);
        //        }
        //
        //        if (requestCode == UserNameActivity.TYPE_GROUP_NAME) {
        //            if (mGroupInfoBean != null) {
        //                RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, data.getStringExtra(Constants.GROUP_NAME), Uri.parse(mGroupInfoBean.getGroupHeader())));
        //            }
        //            tv_group_name.setText(data.getStringExtra(Constants.GROUP_NAME));
        //        } else if (requestCode == UserNameActivity.TYPE_GROUP_NICK) {
        //            tv_group_nick.setText(data.getStringExtra(Constants.GROUP_NICK));
        //        }
        //
        //        if(requestCode == GroupRemarkActivity.MODIFY_GROUP_MARK){
        //            String markStr = data.getStringExtra(Constants.GROUP_REMARK);
        //            BroadcastManager.getInstance(this).sendBroadcast(MODIFY_REMARK_TEXT,markStr);
        //        }
        //    }
    }
}