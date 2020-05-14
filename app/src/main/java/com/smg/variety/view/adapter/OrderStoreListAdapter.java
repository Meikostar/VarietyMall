package com.smg.variety.view.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.common.utils.GlideUtils;

import java.util.List;
import java.util.Map;

/**
 * 订单列表中 商品的adapter
 */
public class OrderStoreListAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {

    public OrderStoreListAdapter(List<MyOrderItemDto> items) {
        super(R.layout.item_order_store_goods_list, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_img), item.getProduct().getData().getCover());
        helper.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_cout, "数量：" + item.getQty())
                .setText(R.id.tv_price, "价格：￥" + item.getPrice());
        String content="";
        helper.addOnClickListener(R.id.rl_bg);
        TextView view = helper.getView(R.id.tv_one);
        TextView view1 = helper.getView(R.id.tv_two);
        if (item.getOptions() != null&&item.getOptions().size()>0) {
            int i=0;
            if(item.getOptions().size()==1){
                view1.setVisibility(View.INVISIBLE);
            }
            for(Map.Entry<String, String> a:item.getOptions().entrySet()){
                if(i==0){
                    view.setVisibility(View.VISIBLE);
                    view.setText(a.getKey()+":"+a.getValue());
                }
                if(i==1){
                    view1.setVisibility(View.VISIBLE);
                    view1.setText(a.getKey()+":"+a.getValue());
                }
                System.out.println("键是"+a.getKey());

                System.out.println("值是"+a.getValue());
                i++;
            }

        } else {
            view.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.INVISIBLE);
        }



    }
}
