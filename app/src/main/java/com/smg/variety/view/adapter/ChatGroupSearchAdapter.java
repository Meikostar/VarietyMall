package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.GroupListDto;
import com.smg.variety.common.utils.GlideUtils;
//import com.smg.variety.rong.chat.ConversationActivity;
import com.smg.variety.rong.chat.ConversationActivity;

import io.rong.imlib.model.Conversation;

public class ChatGroupSearchAdapter extends BaseQuickAdapter<GroupListDto, BaseViewHolder> {

    public ChatGroupSearchAdapter(Context context) {
        super(R.layout.item_chat_group_layout);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListDto item) {
        helper.setText(R.id.tv_chat_group_name, item.getGroup_name());
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.tv_chat_group_icon), item.getAvatar());
        helper.getView(R.id.chat_group_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(ConversationActivity.TARGET_ID, item.getId());
                bundle.putString(ConversationActivity.TITLE, item.getGroup_name());
                bundle.putInt(ConversationActivity.CONVERSATION_TYPE, Conversation.ConversationType.GROUP.getValue());
                gotoActivity(ConversationActivity.class, bundle);
            }
        });
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
