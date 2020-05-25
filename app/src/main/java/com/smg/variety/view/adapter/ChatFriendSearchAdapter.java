package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.SearchFriendListDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.rong.chat.ConversationActivity;

/**
 * 模糊搜索好友
 */
public class ChatFriendSearchAdapter extends BaseQuickAdapter<SearchFriendListDto, BaseViewHolder> {
    public ChatFriendSearchAdapter(Context context) {
        super(R.layout.item_chat_friend_search, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchFriendListDto item) {
        helper.setText(R.id.tv_chat_friend_name, item.getRemark_name());
        helper.setText(R.id.tv_chat_friend_phone, item.getPhone());
        ImageView imageView = helper.getView(R.id.item_chat_friend_icon);
        GlideUtils.getInstances().loadRoundCornerImg(mContext, imageView, 2.5f, Constants.WEB_IMG_URL_UPLOADS + item.getAvatar());
        helper.getView(R.id.chat_friend_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(ConversationActivity.TITLE, item.getRemark_name());
                bundle.putString(ConversationActivity.TARGET_ID, item.getFriend_user_id());
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
