package com.smg.variety.rong.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.FriendPageDto;
import com.smg.variety.bean.GroupInfoDto;
import com.smg.variety.bean.GroupListDto;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.eventbus.ChangeTitleEvent;
import com.smg.variety.eventbus.FinshEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.rong.widget.ConversationFragmentEx;
import com.smg.variety.utils.ShareUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import io.rong.callkit.util.SPUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 配置会话界面
 * Created by rzb on 2019/6/20.
 */
public class ConversationActivity extends BaseActivity {
    private static final String           TAG = ConversationActivity.class.getSimpleName();
    public static final String            TARGET_ID = "target_id";//调用者传递的名字
    public static final String            TITLE = "title";//调用者传递的名字
    public static final String            CONVERSATION_TYPE = "ConversationType";//调用者传递的名字 会话类型 1= 单聊（默认），3=群聊
    public static final int               GROUP_CONVERSATION_CLOSE = 10001;
    public static final int               INTENT_REQUESTCODE = 10002;
    private String                        targetId;
    private String                        title;
    private Conversation.ConversationType mConversationType;//会话类型

    @BindView(R.id.convesation_back)
    ImageView convesation_back;
    @BindView(R.id.convesation_title)
    TextView convesation_title;
    @BindView(R.id.convesation_right)
    RelativeLayout convesation_right;

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;
    private ConversationFragmentEx conversationFragmentEx;
    private LocalBroadcastManager  localBroadcastManager;
    LocalReceiver localReceiver;
    private boolean      isOpenAudio = false;//是否按住讲话;
    private GroupListDto mGroupBean  = null;


    @Override
    public int getLayoutId() {
        return R.layout.ui_conversation_layout;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        initBroadcast();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FinshEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void titleEvent(ChangeTitleEvent event) {
        setTitle(event.title);
    }
    @Override
    public void initData() {
        Intent intent = getIntent();
        Uri uri = getIntent().getData();
        if (uri != null) {
            targetId = intent.getData().getQueryParameter("targetId");
            title = intent.getData().getQueryParameter("title");
            mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                    .getLastPathSegment().toUpperCase(Locale.US));
        } else {
            targetId = intent.getExtras().getString(TARGET_ID, null);
            title = intent.getExtras().getString(TITLE, null);
            int conversationType = getIntent().getIntExtra(CONVERSATION_TYPE, 1);
            switch (conversationType) {
                case 1:
                    mConversationType = Conversation.ConversationType.PRIVATE;
                    break;
                case 3:
                    mConversationType = Conversation.ConversationType.GROUP;
                    break;
                default:
                    mConversationType = Conversation.ConversationType.PRIVATE;
                    break;
            }
        }
        if (targetId == null) {
            return;
        }
        setTitle(title);
        enterFragment(mConversationType, targetId, title);
        setTypingStatusListener(targetId);
        RongIM.getInstance().setMessageAttachedUserInfo(false);
        if (mConversationType == Conversation.ConversationType.GROUP) {
            groupInfo();
         }else if (mConversationType == Conversation.ConversationType.PRIVATE){
            queryUserInfoPage(targetId);
        }
    }

    @Override
    public void initListener() {
        bindClickEvent(convesation_back, () -> {
            finish();
        });

        bindClickEvent(convesation_right, () -> {
            if (mConversationType == Conversation.ConversationType.GROUP) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.GROUP_ID, targetId);
                gotoActivity(GroupDetailActivity.class, false, bundle, GROUP_CONVERSATION_CLOSE);
            }else if(mConversationType == Conversation.ConversationType.PRIVATE){
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_ID, targetId);
                gotoActivity(ChatDetailActivity.class, false, bundle, GROUP_CONVERSATION_CLOSE);
            }
        });
    }

    /**
     * 设置输入状态
     * @param targetId 会话 Id
     */
    private void setTypingStatusListener(String targetId) {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (targetId.equals(targetId)) {
                    int count = typingStatusSet.size();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();
                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });
    }

    private void queryUserInfoPage(String userId) {
        showLoadDialog();
        DataManager.getInstance().queryUserInfoPage(new DefaultSingleObserver<FriendPageDto>() {
            @Override
            public void onSuccess(FriendPageDto friendPageDto) {
                dissLoadDialog();
                if(friendPageDto != null) {
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

                if (friendPageDto.getUser().getFriend() != null) {
                    title = friendPageDto.getUser().getFriend().getRemark_name();
                } else {
                    title = friendPageDto.getUser().getName();
                }
                setTitle(title






                );
            }

        }
    }
    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     * @param mConversationType 会话类型
     * @param targetId          会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String targetId, String title) {
        conversationFragmentEx = new ConversationFragmentEx();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build();
        conversationFragmentEx.setUri(uri);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.conversation, conversationFragmentEx);
        transaction.commitAllowingStateLoss();
    }
    public String cont="";
    private void groupInfo() {
        showLoadDialog();
        DataManager.getInstance().getGroupInfo(new DefaultSingleObserver<HttpResult<GroupInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<GroupInfoDto> result) {
                dissLoadDialog();

                if(result.getData() != null&&result.getData().getGroup_users()!=null&&result.getData().getGroup_users().getData()!=null){
                    List<GroupUserItemInfoDto> guiDtoLists = result.getData().getGroup_users().getData();
                     int i=0;
                    for(GroupUserItemInfoDto dto:guiDtoLists){
                        if(!dto.getUser_id().equals(ShareUtil.getInstance().get(Constants.USER_ID))){
                            if(i==0){
                                cont=dto.getUser_id();
                            }else {
                                cont=cont+","+dto.getUser_id();
                            }
                            i++;
                        }


                    }
                    SPUtils.put(ConversationActivity.this,targetId,cont);
                    ShareUtil.getInstance().save(targetId,cont);

                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, targetId);
    }
    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    setTitle("对方正在输入...");
                    mHandler.removeMessages(SET_TEXT_TYPING_TITLE);
                    break;
                case SET_VOICE_TYPING_TITLE:
                    setTitle("对方正在讲话...");
                    mHandler.removeMessages(SET_VOICE_TYPING_TITLE);
                    break;
                case SET_TARGET_ID_TITLE:
                    setTitle(title);
                    mHandler.removeMessages(SET_TARGET_ID_TITLE);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    private void setTitle(String title) {
        convesation_title.setText(title == null ? " " : title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIMClient.setTypingStatusListener(null);
        localBroadcastManager.unregisterReceiver(localReceiver);
        if (mConversationType == Conversation.ConversationType.GROUP) {
            SPUtils.remove(ConversationActivity.this,targetId);
        }

        EventBus.getDefault().unregister(this);
    }

    private void initBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_CHAT);
        localReceiver = new LocalReceiver();
        //注册本地接收器
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    /**
     * @param type InputMethodManager.SHOW_FORCED    type = 0 hide
     */
    private void changeInput(int type) {
    }

    public void closeKeybord() {
    }
}
