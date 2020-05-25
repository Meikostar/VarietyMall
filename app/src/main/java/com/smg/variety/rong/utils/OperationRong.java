package com.smg.variety.rong.utils;

import android.content.Context;
import android.text.TextUtils;

import com.smg.variety.common.utils.ToastUtil;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationKey;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by AMing on 16/4/11.
 * Company RongCloud
 */
public class OperationRong {

    public static void setConversationTop(final Context context, Conversation.ConversationType conversationType, String targetId, boolean state) {
        if (!TextUtils.isEmpty(targetId) && RongIM.getInstance() != null) {
            RongIM.getInstance().setConversationToTop(conversationType, targetId, state, new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
//                    NToast.shortToast(context, "设置成功");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
//                    NToast.shortToast(context, "设置失败");
                }
            });
        }
    }

    public static void setConverstionNotif(final Context context, Conversation.ConversationType conversationType, String targetId, boolean state) {
        Conversation.ConversationNotificationStatus cns;
        if (state) {
            cns = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
        } else {
            cns = Conversation.ConversationNotificationStatus.NOTIFY;
        }
//        Conversation.ConversationType.PRIVATE
        RongIM.getInstance().setConversationNotificationStatus(conversationType, targetId, cns, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                    ToastUtil.showToast("设置免打扰成功");
                } else if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.NOTIFY) {
                    ToastUtil.showToast("取消免打扰成功");
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtil.showToast( "设置失败");
            }
        });
    }

    /**
     * 获取免打扰
     *
     * @param conversationType
     * @param targetId
     * @param callback
     */
    public static void getConversationNotificationStatus(Conversation.ConversationType conversationType, String targetId, RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus> callback) {
        RongIM.getInstance().getConversationNotificationStatus(conversationType,
                targetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
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


    public static boolean getNotiffState(String id, Conversation.ConversationType type) {

        ConversationKey key = ConversationKey.obtain(id, type);

        Conversation.ConversationNotificationStatus notificationStatus = RongContext.getInstance().getConversationNotifyStatusFromCache(key);

        if (notificationStatus == Conversation.ConversationNotificationStatus.NOTIFY) {
            return true;
        } else {
            return false;

        }


    }

}
