package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.activity.RefundAfterSalesDetailActivity;

import java.util.List;

public class RefundAfterSalesAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    public RefundAfterSalesAdapter(Context context) {
        super(R.layout.item_refund_after_sales, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderDto item) {
        if (item.getOrder() != null&&item.getOrder().getData() != null && item.getOrder().getData().getShop() != null) {
            helper.setText(R.id.tv_item_order_store_name, item.getOrder().getData().getShop().getShop_name());
//            GlideUtils.getInstances().loadRoundImg(mContext,helper.getView(R.id.iv_img),item.getOrder().getData().getShop().getLogo(),R.drawable.moren_ren);
            GlideUtils.getInstances().loadRoundImg(mContext,helper.getView(R.id.iv_img),"http://bbsc.885505.com/seller/"+ item.getShop_id()+"/logo",R.drawable.moren_ren);

        } else {
            helper.setText(R.id.tv_item_order_store_name, "");
        }

        RecyclerView recyclerView = helper.getView(R.id.item_order_goods_list);
        if (item.getOrder() != null&&item.getOrder().getData() != null && item.getOrder().getData().getItems() != null && item.getOrder().getData().getItems().getData() != null && item.getOrder().getData().getItems().getData().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            setGoodsListData(recyclerView, item.getOrder().getData().getItems().getData(),item.getId());
        } else {
            recyclerView.setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.tv_itme_refund_after_sales_text);
        LinearLayout llbg = helper.getView(R.id.ll_bg);
        helper.setText(R.id.tv_status,item.getStatus_msg());
        switch (item.getStatus_msg()) {
            case "再来一单"://待支付
                llbg.setVisibility(View.VISIBLE);
                    if (item.getOrder() != null&&item.getOrder().getData() != null ) {
                        helper.setText(R.id.tv_prices, "¥"+item.getOrder().getData().pay_total);
                        helper.setVisible(R.id.tv_itme_refund_after_sales_text, true)
                                .setText(R.id.tv_itme_refund_after_sales_text, "再来一单")
                                .setText(R.id.tv_detail, "共"+item.getOrder().getData().count+"件商品, "+"待退款");
                    }


                break;
            case "退款成功"://待支付
                llbg.setVisibility(View.GONE);
                if (item.getOrder() != null&&item.getOrder().getData() != null ) {
                    helper.setText(R.id.tv_prices, "¥"+item.getOrder().getData().pay_total);
                    helper.setVisible(R.id.tv_itme_refund_after_sales_text, true)
                            .setText(R.id.tv_itme_refund_after_sales_text, "再来一单")
                            .setText(R.id.tv_detail, "共"+item.getOrder().getData().count+"件商品, "+"待退款");
                }


                break;
            case "审核中"://待支付
                llbg.setVisibility(View.VISIBLE);
                if (item.getOrder() != null&&item.getOrder().getData() != null ) {
                    helper.setText(R.id.tv_prices, "¥"+item.getOrder().getData().pay_total);
                    helper.setVisible(R.id.tv_itme_refund_after_sales_text, true)
                            .setText(R.id.tv_itme_refund_after_sales_text, "取消申请")
                            .setText(R.id.tv_detail, "共"+item.getOrder().getData().count+"件商品, "+"待退款");
                }

                break;
            case "待审核"://待支付
                llbg.setVisibility(View.VISIBLE);
                if (item.getOrder() != null&&item.getOrder().getData() != null ) {
                    helper.setText(R.id.tv_prices, "¥"+item.getOrder().getData().pay_total);
                    helper.setVisible(R.id.tv_itme_refund_after_sales_text, true)
                            .setText(R.id.tv_itme_refund_after_sales_text, "取消申请")
                            .setText(R.id.tv_detail, "共"+item.getOrder().getData().count+"件商品, "+"待退款");
                }

                break;
            case "取消申请"://待支付
                llbg.setVisibility(View.VISIBLE);
                if (item.getOrder() != null&&item.getOrder().getData() != null ) {
                    helper.setText(R.id.tv_prices, "¥"+item.getOrder().getData().pay_total);
                    helper.setVisible(R.id.tv_itme_refund_after_sales_text, true)
                            .setText(R.id.tv_itme_refund_after_sales_text, "再来一单")
                            .setText(R.id.tv_detail, "共"+item.getOrder().getData().count+"件商品, "+"待退款");
                }


                break;
                default:
                    llbg.setVisibility(View.VISIBLE);
                    break;

        }

    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items,final String id) {
        OrderRefundGoodsListAdapter orderGoodsListAdapter = new OrderRefundGoodsListAdapter(items);
        orderGoodsListAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_bg:
                        Intent intent = new Intent(mContext,RefundAfterSalesDetailActivity.class);
                        intent.putExtra("id",id);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(orderGoodsListAdapter);
    }

}
