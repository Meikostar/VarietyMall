package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.NoticeDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.activity.MyOrderDetailActivity;

/**
 * 订单详情中 商品的adapter
 */
public class OrderNotificalAdapter extends BaseQuickAdapter<NoticeDto, BaseViewHolder> {
    private Context mContext;
    public OrderNotificalAdapter(Context  mContext) {
        super(R.layout.item_order_notific, null);
        this.mContext=mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_img), Constants.WEB_IMG_URL_UPLOADS+item.getData().data.img);
        helper.setText(R.id.tv_title, item.getData().subject)
                .setText(R.id.tv_content,  item.getData().content)
                .setText(R.id.tv_time,  item.created_at);
        TextView view = helper.getView(R.id.tv_detail);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                intent.putExtra(Constants.INTENT_ID, item.getData().id);
                intent.putExtra(Constants.INTENT_TYPE, item.getData().type);
                mContext.startActivity(intent);
            }
        });
    }
}
