package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.NoticeDto;

public class MessageTipAdapter extends BaseQuickAdapter<NoticeDto, BaseViewHolder> {
    public MessageTipAdapter() {
        super(R.layout.item_message_tip);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeDto item) {
       helper.setText(R.id.tv_time,item.getCreated_at());
       if(item.getData()!= null){
           helper.setText(R.id.tv_title,item.getData().getTitle())
                   .setText(R.id.tv_des,item.getData().getContent());
       }
    }
}
