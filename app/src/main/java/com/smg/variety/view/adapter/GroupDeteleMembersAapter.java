package com.smg.variety.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ErrorUtil;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

/**
 * 删除群成员
 * Created by rzb on 2019/6/20
 */
public class GroupDeteleMembersAapter extends MyBaseAdapter<GroupUserItemInfoDto> {
    ViewHolder holder;
    Context   mContext;
    private   RefreshListsListener mRefreshListsListener;

    public GroupDeteleMembersAapter(Context context, List<GroupUserItemInfoDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_detele_member_gridview, null);
        holder = new ViewHolder();
        holder.iv_delete_member = (ImageView)view.findViewById(R.id.iv_delete_member);
        holder.tv_delete_member = (TextView)view.findViewById(R.id.tv_delete_member);
        holder.iv_delete =(ImageView)view.findViewById(R.id.iv_delete);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, GroupUserItemInfoDto var3) {
        holder = (ViewHolder)var1.getTag();
        if(var3.getGroup_nickname() != null) {
            holder.tv_delete_member.setText(var3.getGroup_nickname());
        }else{
            holder.tv_delete_member.setText(var3.getUser().getData().getName());
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ids = new ArrayList<>();
                ids.add(var3.getUser_id());
                delGroupUser(var3.getGroup_id(), ids,var3.getUser().getData().getName());
            }
        });
        GlideUtils.getInstances().loadRoundCornerImg(mContext, holder.iv_delete_member, 2.5f, Constants.WEB_IMG_URL_UPLOADS + var3.getUser().getData().getAvatar());
    }

    class ViewHolder {
        ImageView iv_delete_member;
        TextView tv_delete_member;
        ImageView iv_delete;
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
            }

            @Override
            public void onSuccess(Message message) {

                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
    }
    private void delGroupUser(String groupId, List<String> idLists,String name) {
        DataManager.getInstance().delGroupUser(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                sendOpenRedPacketMessage(groupId,Conversation.ConversationType.GROUP,name+"被移除群聊");
                ToastUtil.showToast("删除群成员成功");
                refreshList(true);
            }
            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
            }
        },groupId, idLists);
    }

    private void refreshList(boolean isFresh){
        if (mRefreshListsListener != null) {
            mRefreshListsListener.OnRefreshListener(isFresh);
        }
    }

    public void setRefreshListsListener(RefreshListsListener refreshListsListener) {
        this.mRefreshListsListener = refreshListsListener;
    }

    public interface RefreshListsListener {
        void OnRefreshListener(boolean isFresh);
    }
}
