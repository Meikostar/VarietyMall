package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.SuperMemberActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.widgets.CircleImageView;

/**
 * 关注店铺
 */
public class LiveChaildProductAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    public LiveChaildProductAdapter(Context context) {
        super(R.layout.live_product_item, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        int parentPosition = getParentPosition(item);
        helper.setText(R.id.tv_title, item.getTitle());
        View view = helper.getView(R.id.tv_cout);
        view.setVisibility(View.GONE);
        helper.setText(R.id.tv_cout, (parentPosition+1)+"");

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
        if(item.is_liver_product){
            tv_del.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_hs));
            tv_del.setText("已添加");
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
