package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.activity.MyOrderDetailActivity;

import java.util.List;

public class OrderTgAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    public OrderTgAdapter(Context context) {
        super(R.layout.item_ordertg_fragment, null);
        mContext = context;
    }
//    public static void loadCircle(GlideContextWrapper glideContextWrapper, ImageView imageView, String url, float borderWidth, int borderColor, @IdRes int placeholder) {
//        RequestOptions options = new RequestOptions()
//                .placeholder(placeholder)
//                .fallback(placeholder)
//                .centerCrop()
//                .bitmapTransform(new GlideCircleBorderTransform(borderWidth, borderColor))
//                .diskCacheStrategy(DiskCacheStrategy.DATA);
//        ImageLoader.getRequestManager(glideContextWrapper)
//                .load(url)
//                .apply(options)
//                .into(imageView);
//    }
    @Override
    protected void convert(BaseViewHolder helper, MyOrderDto item) {

        helper.addOnClickListener(R.id.item_order_content);
        if (item.getShop_id()!= null) {
            helper.setText(R.id.tv_shop_name, item.getShop().getShop_name());
            GlideUtils.getInstances().loadRoundImg(mContext,helper.getView(R.id.iv_img),"http://bbsc.885505.com/seller/"+item.getShop_id()+"/logo",R.drawable.moren_ren);

        } else {
            helper.setText(R.id.tv_shop_name, "");
        }
        helper.setText(R.id.tv_no, item.getNo())
        .setText(R.id.tv_down_time,"下单时间:"+item.getCreated_at())
        .setText(R.id.tv_prices,"¥"+item.commission)
        .setText(R.id.tv_tgsy,"¥"+item.distribution)
        .setText(R.id.tv_time,"完成时间:"+item.getUpdated_at());
        RecyclerView recyclerView = helper.getView(R.id.item_order_goods_list);

        if (item.getItems() != null && item.getItems().getData() != null && item.getItems().getData().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            setGoodsListData(recyclerView, item.getItems().getData(), item);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
        helper.setGone(R.id.tv_button_1, false)
                .setGone(R.id.tv_button_2, false)
                .addOnClickListener(R.id.tv_button_1)
                .addOnClickListener(R.id.tv_button_2);
//        if (item.getTotal() != null) {
//            helper.setText(R.id.tv_prices, "¥"+item.getTotal());
//        }
        View view = helper.getView(R.id.ll_bg);
        helper.setText(R.id.tv_status, item.getStatus_msg());
//        helper.setText(R.id.tv_detail, "共"+item.getCount()+"件商品, "+"已付款");
        switch (item.getStatus()) {
            case "created"://待支付
                helper.setVisible(R.id.tv_button_1, true)
                        .setVisible(R.id.tv_button_2, true)
                        .setText(R.id.tv_button_1, "取消订单")
                        .setText(R.id.tv_button_2, "去付款");

                break;
            case "paid"://待发货
                helper.setVisible(R.id.tv_button_1, true);
                if (item.getRefundInfo() != null && item.getRefundInfo().getData() != null) {
                    //退款中

                    helper.setText(R.id.tv_button_1, "取消退款");
                } else {
                    helper.setVisible(R.id.tv_button_2, true)
                            .setText(R.id.tv_button_1, "申请退款")
                            .setText(R.id.tv_button_2, "催发货");
                }


                break;
            case "shipping"://已发货
                helper.setVisible(R.id.tv_button_2, true);
                if ("待消费".equals(item.getStatus_msg())) {
                    helper.setVisible(R.id.tv_button_1, false)
                            .setText(R.id.tv_button_2, "查看兑换券");
                } else {
                    helper.setVisible(R.id.tv_button_1, true)
                            .setText(R.id.tv_button_1, "查看物流")
                            .setText(R.id.tv_button_2, "确认收货");
                }
                break;
            case "shipped"://待评价
//                helper.setText(R.id.tv_item_order_status, "待评价");
                helper.setVisible(R.id.tv_button_1, true)
                        .setVisible(R.id.tv_button_2, true)
                        .setText(R.id.tv_button_1, "再来一单")
                        .setText(R.id.tv_button_2, "去评价");
                break;
            default:
                view.setVisibility(View.GONE);
                helper.setVisible(R.id.tv_button_1, true)
                        .setText(R.id.tv_button_1, item.getStatus_msg());
                break;
        }
        view.setVisibility(View.GONE);
    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items, final MyOrderDto item) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        OrderGoodsListAdapter mAdapter = new OrderGoodsListAdapter(items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_bg:
                        if (item != null) {
                            Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                            intent.putExtra(Constants.INTENT_ID, item.getId());
                            intent.putExtra(Constants.INTENT_TYPE, item.getType());
                            mContext.startActivity(intent);

                        }
                        break;

                }
            }
        });

    }
}
