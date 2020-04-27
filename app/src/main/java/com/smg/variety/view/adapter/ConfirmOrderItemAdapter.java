package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.OrderProductDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import java.util.List;

/**
 * 确认订单Item项的订单
 * Created by rzb on 2019/06/19
 */
public class ConfirmOrderItemAdapter extends BaseQuickAdapter<OrderProductDto, BaseViewHolder> {

    public ConfirmOrderItemAdapter(List<OrderProductDto> data) {
        super(R.layout.item_confirm_order_level_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderProductDto item) {
        helper.setText(R.id.tv_item_confirm_order_title, item.getName())
                .setText(R.id.tv_item_confirm_order_price, "¥"+item.getPrice())
                .setText(R.id.tv_count, "x"+item.getQty());
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_confirm_order_cover),
                 item.getCover());
    }
}