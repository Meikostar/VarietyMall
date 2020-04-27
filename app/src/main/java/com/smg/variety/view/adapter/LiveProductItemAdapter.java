package com.smg.variety.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;

/**
 * 订单列表中 商品的adapter
 */
public class LiveProductItemAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {

    public LiveProductItemAdapter(Context context) {
        super(R.layout.item_live_product_item, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {

        int position = helper.getPosition();
        helper.addOnClickListener(R.id.iv_car);

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_cout, (position+1)+"");

        TextView textview = helper.getView(R.id.tv_prices);
        TextView tv_del = helper.getView(R.id.tv_del);
        if(TextUtil.isNotEmpty(item.market_price)){
            String name = item.market_price;
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(12);
            int with = (int) textPaint.measureText(name);

            textview.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线下划线
            textview.getPaint().setAntiAlias(true);// 抗锯齿
            textview.setText("¥"+item.market_price);
        }else{
            textview.setText("");
        }


        helper.addOnClickListener(R.id.ll_bg);
        helper.setText(R.id.tv_price,"¥"+ item.getPrice());
        GlideUtils.getInstances().loadProcuctNormalImg(mContext, helper.getView(R.id.iv_img),  item.getCover());
    }
}
