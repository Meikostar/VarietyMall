package com.smg.variety.rong;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.utils.ScreenUtils;
import com.smg.variety.view.MainActivity;

import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SealNotificationReceiver extends PushMessageReceiver {
    private static final String TAG = SealNotificationReceiver.class.getSimpleName();

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage message) {
        LogUtil.e(TAG, "-- onNotificationMessageArrived pushType = " + pushType.getName() + " message=" + message.getPushId() + ",PushData=" + message.getPushData());
//
//        String transactionId;
//        if (null != message.getPushData() ) {
//            try {
//                JSONObject object = new JSONObject(message.getPushData());
//
//                transactionId = object.isNull("transactionId") ? "" : object.getString("transactionId");
//                if (!TextUtils.isEmpty(transactionId)) {
//                    //保存push过来的id，唯一值，在onRecevied会用到
////                    SPInfo.saveNodeNotificationId(context, transactionId);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }

        return false;
    }
    private static int num = 0;//初始通知数量为1

    //按钮点击事件（通知栏）
    public static void ShowNotification(Message message, int left) {
        LogUtil.e("ChatroomKit", "消息接收监听者=" + message.toString());
        num++;
        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
        NotificationManager manager = (NotificationManager) BaseApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        //8.0 以后需要加上channelId 才能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        //设置TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(BaseApplication.getInstance());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(BaseApplication.getInstance(), "default");
        builder.setSmallIcon(R.mipmap.ic_logo);
        RemoteViews rv = new RemoteViews(BaseApplication.getInstance().getPackageName(), R.layout.custom_notification_view);
        TextMessage content = (TextMessage) message.getContent();
        rv.setTextViewText(R.id.content_tv,content.getContent());//修改自定义View中的歌名
        rv.setTextViewText(R.id.create_time_tv, ScreenUtils.formatToFileDi(message.getSentTime()));
        rv.setTextViewText(R.id.number_tv, "还有" + left + "个通知");
        //修改自定义View中的图片(两种方法)
        //        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
        //        rv.setImageViewBitmap(R.id.iv, BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.mipmap.ic_logo));
        builder.setContent(rv);

        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
        //        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
        //        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) BaseApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0x1, notification);
    }
    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage message) {
        LogUtil.e(TAG, "-- onNotificationMessageClicked pushType = " + pushType.getName() + " message=" + message.getPushId());
        LogUtil.i("pushExtra", "onNotificationMessageClicked" + "\t" + message.getTargetId() + "\t" + message.getExtra() + "\t" + message.getPushContent() + "\t" + message.getPushData() +
                "\t" + message.getPushId());

//        String transactionId;
//        if (null != message.getPushData() && Utils.isJson(message.getPushData())) {
//            try {
//                JSONObject object = new JSONObject(message.getPushData());
//                transactionId = object.isNull("transactionId") ? "" : object.getString("transactionId");
//                //点击中也添加，防止onNotificationMessageArrived没调
//                if (!TextUtils.isEmpty(transactionId)) {
//                    SPInfo.saveNodeNotificationId(context, transactionId);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        return false;
    }

    @Override
    public void onThirdPartyPushState(PushType pushType, String action, long resultCode) {
        super.onThirdPartyPushState(pushType, action, resultCode);
        LogUtil.e(TAG, "-- onThirdPartyPushState pushType = " + pushType.getName() + " action=" + action + " resultCode" + resultCode);
        if ("com.xiaomi.mipush.CLEAR_NOTIFICATION".equals(action)) {
            LogUtil.e(TAG, "-- onThirdPartyPushState action=" + action);
        }
    }
}
