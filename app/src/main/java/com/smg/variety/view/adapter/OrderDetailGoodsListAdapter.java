package com.smg.variety.view.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;

/**
 * 订单详情中 商品的adapter
 */
public class OrderDetailGoodsListAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {

    public OrderDetailGoodsListAdapter(List<MyOrderItemDto> items) {
        super(R.layout.item_order_detail_goods_list, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_order_goods_icon), item.getProduct().getData().getCover());
        helper.setText(R.id.tv_order_goods_name, item.getTitle())
                .setText(R.id.tv_order_goods_price, "¥" + item.getPrice())
                .setText(R.id.tv_order_goods_num, "x" + item.getQty());
        String content="";
        View view = helper.getView(R.id.rl_bg);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.FROM, "gc");
                bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getProduct_id());
                bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                gotoActivity(CommodityDetailActivity.class, bundle);
            }
        });
        if(item.getOptions()!=null){
            int i=0;

            //value
            for(String value : item.getOptions().values()){
                if(i==0){
                    content=value;
                }else {
                    content=content+","+value;
                }
            }
            helper.setText(R.id.tv_gg,content);
        }else {
            helper.setText(R.id.tv_gg,"");
        }
    }
    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
