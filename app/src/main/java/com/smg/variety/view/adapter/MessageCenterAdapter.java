package com.smg.variety.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.NoticeDto;

public class MessageCenterAdapter extends BaseQuickAdapter<NoticeDto, BaseViewHolder> {
    public MessageCenterAdapter(Context context) {
        super(R.layout.item_message_center, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeDto item) {
        helper.setText(R.id.tv_message_time, item.created_at)
                .setText(R.id.tv_message_type, item.title)
                .setText(R.id.tv_message_content, item.content);

//                .setVisible(R.id.iv_message_point, item.isHasNewMsg());
//        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_message_icon));
    }
}
