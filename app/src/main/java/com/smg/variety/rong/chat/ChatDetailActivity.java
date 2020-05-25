package com.smg.variety.rong.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.FriendPageDto;
import com.smg.variety.bean.UserInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.rong.utils.OperationRong;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.widgets.dialog.BaseDialog;
import com.smg.variety.view.widgets.dialog.ConfirmDialog;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 用户详细
 * Created by rzb on 2019/6/20
 */
public class ChatDetailActivity extends BaseActivity {
    private static final String TAG = ChatDetailActivity.class.getSimpleName();
    public static final String ITEM_USER = "item_user";
    public static final String SEARCH_RESULT = "SearchResult";//调用者传递的名字
    private boolean isSearchResult;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.img_top_chat)
    ImageView img_top_chat;
    @BindView(R.id.img_voice_switch)
    ImageView img_voice_switch;

    @BindView(R.id.ta_home_title)
    TextView ta_home_title;
    @BindView(R.id.iv_ta_home_user_icon)
    ImageView iv_ta_home_user_icon;
    @BindView(R.id.iv_ta_home_user_name)
    TextView iv_ta_home_user_name;
    @BindView(R.id.iv_ta_home_user_mname)
    TextView iv_ta_home_user_mname;
    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.iv_ta_home_user_phone)
    TextView iv_ta_home_user_phone;
    @BindView(R.id.iv_ta_home_update_tag)
    LinearLayout iv_ta_home_update_tag;
    @BindView(R.id.but_ta_home_user_send)
    RelativeLayout but_ta_home_user_send;
    @BindView(R.id.but_ta_home_user_add)
    RelativeLayout but_ta_home_user_add;
    @BindView(R.id.but_ta_home_user_del)
    RelativeLayout but_ta_home_user_del;

    private String userId;
    private String userHeader;
    private String nickName;
    private boolean isVoiceSwitch;

    @Override
    public int getLayoutId() {
        return R.layout.ui_chat_home_layout;
    }

    @Override
    public void initView() {
    }

    private int state;

    @Override
    public void initData() {
        Bundle userBundle = getIntent().getExtras();
        UserInfoDto userInfoDto = (UserInfoDto) userBundle.getSerializable(ITEM_USER);
        if (userInfoDto != null) {
            initDataView(userInfoDto);
        } else {
            userId = getIntent().getExtras().getString(Constants.USER_ID);
            if (userId != null) {
                queryUserInfoPage(userId);
            }
        }
        boolean aBoolean = ShareUtil.getInstance().getBoolean(content + userId);
        if (aBoolean) {
            state = 1;
            img_top_chat.setSelected(true);
        } else {
            img_top_chat.setSelected(false);
        }
        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE,
                userId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(final Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        final int value = conversationNotificationStatus.getValue();
//                        String title;
//                        final Conversation.ConversationNotificationStatus conversationNotificationStatus1;
                        if (value == 1) {
//                            conversationNotificationStatus1 = conversationNotificationStatus.setValue(0);
//                            title = "免打扰";
                            isVoiceSwitch = false;
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

    private String content = "LoveProject";

    @Override
    public void initListener() {
        img_top_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    ShareUtil.getInstance().saveBoolean(content + userId, false);
                    OperationRong.setConversationTop(ChatDetailActivity.this, Conversation.ConversationType.PRIVATE, userId, false);
                    state = 0;
                    img_top_chat.setSelected(false);
                } else {
                    ShareUtil.getInstance().saveBoolean(content + userId, true);
                    OperationRong.setConversationTop(ChatDetailActivity.this, Conversation.ConversationType.PRIVATE, userId, true);
                    state = 1;
                    img_top_chat.setSelected(true);
                }
            }
        });
        img_voice_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVoiceSwitch) {
                    img_voice_switch.setSelected(false);
                    OperationRong.setConverstionNotif(ChatDetailActivity.this, Conversation.ConversationType.PRIVATE, userId, false);
                } else {
                    img_voice_switch.setSelected(true);
                    OperationRong.setConverstionNotif(ChatDetailActivity.this, Conversation.ConversationType.PRIVATE, userId, true);
                }
                isVoiceSwitch = !isVoiceSwitch;
            }
        });
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(iv_ta_home_update_tag, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FRIEND_ID, userId);
            bundle.putString(Constants.FRIEND_REMARK, nickName);
            gotoActivity(FriendRemarkActivity.class, false, bundle, FriendRemarkActivity.MODIFY_FRIEND_REMARK);
        });

        bindClickEvent(but_ta_home_user_send, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(ConversationActivity.TITLE, nickName);
            bundle.putString(ConversationActivity.TARGET_ID, userId);
            gotoActivity(ConversationActivity.class, true, bundle);
        });

        bindClickEvent(but_ta_home_user_add, () -> {
            addFriend(userId, nickName, "");
        });

        bindClickEvent(but_ta_home_user_del, () -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(this);
            confirmDialog.setCancelText("取消");
            confirmDialog.setTitle("温馨提示");
            confirmDialog.setMessage("是否删除好友" + (nickName == null ? "" : nickName));
            confirmDialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                @Override
                public void onYesClick() {
                    confirmDialog.hide();
                    delFriend(userId);
                }
            });
            confirmDialog.show();
        });

        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE,
                userId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(final Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        final int value = conversationNotificationStatus.getValue();
                        String title;
                        final Conversation.ConversationNotificationStatus conversationNotificationStatus1;
                        if (value == 1) {
                            conversationNotificationStatus1 = conversationNotificationStatus.setValue(0);
                            title = "免打扰";
                        } else {
                            conversationNotificationStatus1 = conversationNotificationStatus.setValue(1);
                            title = "取消免打扰";
                        }


                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 发送请求添加好友
     */
    private void addFriend(String friendId, String remark_name, String msg) {
        showLoadDialog();
        DataManager.getInstance().addFriend(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("请求发送成功");
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, friendId, remark_name, msg);
    }

    private void queryUserInfoPage(String userId) {
        showLoadDialog();
        DataManager.getInstance().queryUserInfoPage(new DefaultSingleObserver<FriendPageDto>() {
            @Override
            public void onSuccess(FriendPageDto friendPageDto) {
                dissLoadDialog();
                if (friendPageDto != null) {
                    initFriendDataView(friendPageDto);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, userId);
    }

    private void initFriendDataView(FriendPageDto friendPageDto) {
        if (friendPageDto != null) {
            if (friendPageDto.getUser() != null) {
                userHeader = friendPageDto.getUser().getAvatar();
                if (friendPageDto.getUser().getFriend() != null) {
                    nickName = friendPageDto.getUser().getFriend().getRemark_name();
                } else {
                    nickName = friendPageDto.getUser().getName();
                }
                iv_ta_home_user_name.setText(nickName);
                tv_name.setText(nickName);
                iv_ta_home_user_mname.setText(friendPageDto.getUser().getName());


                iv_ta_home_user_phone.setText(friendPageDto.getUser().getPhone());
                GlideUtils.getInstances().loadRoundCornerImg(this, iv_ta_home_user_icon, 6, Constants.WEB_IMG_URL_UPLOADS + userHeader, R.mipmap.icon_user_default);
            }
            but_ta_home_user_send.setVisibility(View.GONE);
            but_ta_home_user_add.setVisibility(View.GONE);
            but_ta_home_user_del.setVisibility(View.GONE);
            iv_ta_home_update_tag.setVisibility(View.VISIBLE);
        }
    }

    private void initDataView(UserInfoDto userInfoDto) {
        if (userInfoDto != null) {
            userId = userInfoDto.getId();
            nickName = userInfoDto.getName();
            userHeader = userInfoDto.getAvatar();
            iv_ta_home_user_name.setText(userInfoDto.getName());
            tv_name.setText(userInfoDto.getName());
            iv_ta_home_user_phone.setText(userInfoDto.getPhone());
            GlideUtils.getInstances().loadUserRoundImg(this, iv_ta_home_user_icon, Constants.WEB_IMG_URL_UPLOADS + userHeader);

            but_ta_home_user_send.setVisibility(View.GONE);
            but_ta_home_user_add.setVisibility(View.GONE);
            but_ta_home_user_del.setVisibility(View.GONE);
//            iv_ta_home_update_tag.setVisibility(View.GONE);
        }
    }

    /**
     * 删除好友
     */
    private void delFriend(String friendId) {
        showLoadDialog();
        DataManager.getInstance().delFriend(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                finish();
                //ToastUtil.showToast("删除好友成功");
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("删除好友成功");
                } else {
                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                }
                finish();
            }
        }, friendId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FriendRemarkActivity.MODIFY_FRIEND_REMARK && resultCode == Activity.RESULT_OK && data != null) {
            iv_ta_home_user_name.setText(data.getStringExtra("friend_remark"));
            tv_name.setText(data.getStringExtra("friend_remark"));
        }
    }
}
