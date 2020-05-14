package com.smg.variety.view.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderLogisticsDto;
import com.smg.variety.bean.SBHoutaibean;
import com.smg.variety.bean.TrackBean;

import java.util.List;


/**
 * 物流进度跟踪
 */
public class MyOrderLogisticsItemAdapter extends BaseQuickAdapter<SBHoutaibean, BaseViewHolder> {

    public MyOrderLogisticsItemAdapter() {
        super(R.layout.item_myorder_logistics_layout, null);
    }


    @Override
    protected void convert(BaseViewHolder helper, SBHoutaibean item) {
        helper.setText(R.id.tv_logistics_time, item.ftime)
                .setText(R.id.tv_logistics_des, item.context);
        if (helper.getPosition() == 0) {
            helper.setBackgroundRes(R.id.tv_logistics_flag, R.drawable.bg_logistics_flag_sel_shape)
                    .setVisible(R.id.tv_logistics_line_top, false)
                    .setTextColor(R.id.tv_logistics_des, Color.parseColor("#1BAF4F"));
        } else if (helper.getPosition() == getItemCount() - 1) {
            //            helper.setBackgroundRes(R.id.tv_logistics_flag, R.drawable.bg_logistics_flag_sel_shape)
            helper.setVisible(R.id.tv_logistics_line_end, false);
        } else {
            helper.setBackgroundRes(R.id.tv_logistics_flag, R.drawable.bg_logistics_flag_default_shape)
                    .setVisible(R.id.tv_logistics_line_end, true);

        }
    }
}
