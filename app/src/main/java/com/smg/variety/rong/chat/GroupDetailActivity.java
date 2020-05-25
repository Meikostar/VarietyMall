package com.smg.variety.rong.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.GroupInfoDto;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.FinshEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.rong.utils.OperationRong;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.adapter.GroupMembersAapter;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;
import com.smg.variety.view.widgets.dialog.BaseDialog;
import com.smg.variety.view.widgets.dialog.ConfirmDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

public class GroupDetailActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.group_members_gv)
    NoScrollGridView group_members_gv;
    @BindView(R.id.btn_add)
    TextView btn_add;
    @BindView(R.id.btn_del)
    TextView btn_del;
    @BindView(R.id.tv_more)
    TextView tv_more;
    @BindView(R.id.tv_group_name)
    TextView tv_group_name;
    @BindView(R.id.tv_group_notice)
    TextView tv_group_notice;
    @BindView(R.id.tv_group_my_nick)
    TextView tv_group_my_nick;
    @BindView(R.id.ll_group_clean_data)
    TextView ll_group_clean_data;
    @BindView(R.id.btn_logout)
    TextView btn_logout;
    @BindView(R.id.ll_group_name)
    LinearLayout ll_group_name;
    @BindView(R.id.ll_group_remark)
    LinearLayout ll_group_remark;
    @BindView(R.id.ll_group_nick)
    LinearLayout ll_group_nick;
    private String groupId;
    private GroupInfoDto mGroupInfoDto;
    private GroupMembersAapter mGroupMembersAapter;
    private List<GroupUserItemInfoDto> userLists = new ArrayList<>();

    @BindView(R.id.img_voice_switch)
    ImageView img_voice_switch;
    private boolean isVoiceSwitch;

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_detail_layout;
    }

    @Override
    public void initView() {

        userid = ShareUtil.getInstance().get(Constants.USER_ID);
        initAdapter();

        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP,
                userid, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(final Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        final int value = conversationNotificationStatus.getValue();
//                        String title;
//                        final Conversation.ConversationNotificationStatus conversationNotificationStatus1;
                        if (value == 1) {
                            isVoiceSwitch = false;
//                            conversationNotificationStatus1 = conversationNotificationStatus.setValue(0);
//                            title = "免打扰";
                        } else {
//                            conversationNotificationStatus1 = conversationNotificationStatus.setValue(1);
//                            title = "取消免打扰";
                            isVoiceSwitch = true;
                        }
                        img_voice_switch.setSelected(isVoiceSwitch);


                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
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
            gotoActivity(ModifyGroupActivity.class, false, bundle, ModifyGroupActivity.GROUP_ADD);
        });

        bindClickEvent(btn_del, () -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.TYPE, GroupDeteleMembersActivity.GROUP_DEL);
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(GroupDeteleMembersActivity.class, false, bundle, GroupDeteleMembersActivity.GROUP_DEL);
        });

        bindClickEvent(tv_more, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(GroupMembersActivity.class, false, bundle);
        });

        bindClickEvent(ll_group_name, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.GROUP_NAME, mGroupInfoDto.getGroup_name());
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(GroupNameActivity.class, false, bundle, GroupNameActivity.MODIFY_GROUP_NAME);
        });

        bindClickEvent(ll_group_remark, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.GROUP_NOTICE, mGroupInfoDto.getNotice());
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(GroupNoticeActivity.class, false, bundle, GroupNoticeActivity.MODIFY_GROUP_NOTICE);
        });

        bindClickEvent(ll_group_nick, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.GROUP_NICK, tv_group_my_nick.getText().toString());
            bundle.putString(Constants.GROUP_ID, groupId);
            gotoActivity(GroupNickActivity.class, false, bundle, GroupNickActivity.MODIFY_GROUP_NICK);
        });

        bindClickEvent(ll_group_clean_data, () -> {
            ConfirmDialog dialog = new ConfirmDialog(GroupDetailActivity.this);
            dialog.setMessage("确定清空聊天记录");
            dialog.setTitle("温馨提示");
            dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                @Override
                public void onYesClick() {
                    dialog.dismiss();
                    if (RongIM.getInstance() != null) {

                        RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                ToastUtil.showToast("清除成功");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                ToastUtil.showToast("清除失败");
                            }
                        });
                        RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP, groupId, System.currentTimeMillis(), null);
                    }
                }
            });
            dialog.show();
        });

        bindClickEvent(btn_logout, () -> {
            if (mGroupInfoDto == null) {
                return;
            }
            ConfirmDialog dialog = new ConfirmDialog((GroupDetailActivity.this));
            if (userid.equals(mGroupInfoDto.user_id)) {
                dialog.setMessage("是否解散该群组");

            } else {
                dialog.setMessage("是否退出该群组");
            }

            dialog.setTitle("温馨提示");
            dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                @Override
                public void onYesClick() {
                    dialog.dismiss();
                    if (userid.equals(mGroupInfoDto.user_id)) {
                        state = 1;
                        sendOpenRedPacketMessage(groupId, Conversation.ConversationType.GROUP, "改群已被解散");

                    } else {
                        state = 0;
                        quitGroup();
                    }
                }
            });
            dialog.show();
        });

        img_voice_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoiceSwitch) {
                    img_voice_switch.setSelected(false);
                    OperationRong.setConverstionNotif(GroupDetailActivity.this, Conversation.ConversationType.GROUP, userid, false);
                } else {
                    img_voice_switch.setSelected(true);
                    OperationRong.setConverstionNotif(GroupDetailActivity.this, Conversation.ConversationType.GROUP, userid, true);
                }
                isVoiceSwitch = !isVoiceSwitch;
            }
        });
    }

    private boolean isFirt;

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirt) {
            groupInfo();
        } else {
            isFirt = true;
        }
    }

    private int state;
    private String userid;

    public void delGroup() {


        DataManager.getInstance().DelGroup(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                ToastUtil.showToast("群已解散");
                clearData();
                state = 0;


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, groupId);

    }

    /**
     * 小灰色条消息
     */
    private void sendOpenRedPacketMessage(String targetId, Conversation.ConversationType conversationType, String message) {
        InformationNotificationMessage myTextMessage = InformationNotificationMessage.obtain(message);
        Message myMessage = Message.obtain(targetId, conversationType, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, message, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
                ToastUtil.showToast("退群成功");
            }

            @Override
            public void onSuccess(Message message) {
                if (state == 1) {
                    delGroup();

                } else {
                    ToastUtil.showToast("退群成功");
                    clearData();
                }


                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                ToastUtil.showToast("退群失败");
                //消息发送失败的回调
            }
        });
    }

    public void quitGroup() {
        DataManager.getInstance().QuitGroup(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                sendOpenRedPacketMessage(groupId, Conversation.ConversationType.GROUP, ShareUtil.getInstance().get(Constants.USER_NAME) + "退出群聊");


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, groupId);

    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            groupId = bundle.getString(Constants.GROUP_ID);
        }
        groupInfo();
    }

    public void clearData() {
        RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                EventBus.getDefault().post(new FinshEvent());
                finish();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                ToastUtil.showToast("清除成功");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtil.showToast("清除失败");
            }
        });
        RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP, groupId, System.currentTimeMillis(), null);
    }

    private void groupInfo() {
        showLoadDialog();
        DataManager.getInstance().getGroupInfo(new DefaultSingleObserver<HttpResult<GroupInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<GroupInfoDto> result) {
                dissLoadDialog();
                if (result.getData() != null) {
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

    private void initAdapter() {
        mGroupMembersAapter = new GroupMembersAapter(GroupDetailActivity.this, userLists);
        group_members_gv.setAdapter(mGroupMembersAapter);
    }

    private void initDataView() {
        if (mGroupInfoDto != null) {
            tv_group_name.setText(mGroupInfoDto.getGroup_name());
            if (mGroupInfoDto.getNotice() != null) {
                tv_group_notice.setText(mGroupInfoDto.getNotice());
            } else {
                tv_group_notice.setText("未设置");
            }
            if (mGroupInfoDto.getSelf_group_user() != null) {
                if (mGroupInfoDto.getSelf_group_user().getRemark() != null) {
                    tv_group_my_nick.setText(mGroupInfoDto.getSelf_group_user().getGroup_nickname());
                } else {
                    tv_group_my_nick.setText(mGroupInfoDto.getSelf_group_user().getRemark());
                }
            }
            if (mGroupInfoDto.getGroup_users().getData() != null) {
                List<GroupUserItemInfoDto> guiDtoLists = mGroupInfoDto.getGroup_users().getData();
                userLists.clear();
                userLists.addAll(guiDtoLists);
                mGroupMembersAapter.notifyDataSetChanged();
            }
            if (!TextUtils.isEmpty(mGroupInfoDto.user_id)) {
                if (mGroupInfoDto.user_id.equals(userid)) {
                    btn_del.setVisibility(View.VISIBLE);
                } else {
                    btn_del.setVisibility(View.GONE);
                }
            }

        }
    }

    private void modifyGroup(HashMap<String, String> map, String url) {
//        showLoadDialog();
//        DataManager.getInstance().modifyGroup(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@Nullable Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                dissLoadDialog();
//                if (response.code() == 200) {
//                    ToastUtil.showToast("修改成功");
//                    String groupHead = map.get("groupHeader");
//                    Group group = new Group(groupId, mGroupInfoBean.getGroupName(), Uri.parse(groupHead));
//                    RongIM.getInstance().refreshGroupInfoCache(group);
//                    GlideUtils.getInstances().loadRoundImg(GroupDetailActivity.this, img_group_header, url, R.mipmap.icon_group_default);
//                } else {
//                    ToastUtil.showToast(ApiException.getShowToast(response));
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
//                dissLoadDialog();
//                ToastUtil.showToast("失败");
//            }
//        }, map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == GroupNameActivity.MODIFY_GROUP_NAME) {
            String gName = data.getStringExtra(Constants.GROUP_NAME);
            if (gName != null) {
                tv_group_name.setText(data.getStringExtra(Constants.GROUP_NAME));
                RongIM.getInstance().refreshGroupInfoCache(new Group(groupId, gName, Uri.parse(Constants.WEB_IMG_URL_UPLOADS + mGroupInfoDto.getAvatar())));
            }
        }
        if (requestCode == GroupNoticeActivity.MODIFY_GROUP_NOTICE) {
            String gNotice = data.getStringExtra(Constants.GROUP_NOTICE);
            if (gNotice != null) {
                tv_group_notice.setText(data.getStringExtra(Constants.GROUP_NOTICE));
            }
        }
        if (requestCode == GroupNickActivity.MODIFY_GROUP_NICK) {
            String gNick = data.getStringExtra(Constants.GROUP_NICK);
            if (gNick != null) {
                tv_group_my_nick.setText(data.getStringExtra(Constants.GROUP_NICK));
            }
        }
    }
}