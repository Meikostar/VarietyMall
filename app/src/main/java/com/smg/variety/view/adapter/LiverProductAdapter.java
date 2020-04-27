package com.smg.variety.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;

/**
 * 关注店铺
 */
public class LiverProductAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    public LiverProductAdapter(Context context) {
        super(R.layout.liver_product_item, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {

        int position = helper.getPosition();

        helper.setText(R.id.tv_title, item.getTitle());
            helper.setText(R.id.tv_cout, (position+1)+"");

        TextView textview = helper.getView(R.id.tv_prices);
        TextView tv_del = helper.getView(R.id.tv_del);
        TextView tv_stock = helper.getView(R.id.tv_stock);
        tv_stock.setText("库存："+item.stock);
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
        if(item.is_liver_product){
            tv_del.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_hs));
            tv_del.setText("移除直播");
            helper.addOnClickListener(R.id.tv_del);
        }else {
            tv_del.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_zs));
            tv_del.setText("加入直播");
            helper.addOnClickListener(R.id.tv_del);
        }

        helper.setText(R.id.tv_price,"¥"+ item.getPrice());
        GlideUtils.getInstances().loadProcuctNormalImg(mContext, helper.getView(R.id.iv_img),  item.getCover());


    }
    public String getSapce(int length){
        String content="\t\t\t\t\t";
        for(int i=0;i<length;i++){
            content=content+"\t";
        }
        return content;
    }
}
