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
import com.smg.variety.view.activity.StoreOrderDetailActivity;

import java.util.List;

public class OrderStoreAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    public OrderStoreAdapter(Context context) {
        super(R.layout.item_ordert_store_fragment, null);
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

        View view = helper.getView(R.id.item_order_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreOrderDetailActivity.class);
                intent.putExtra("id",item.getId());
                mContext.startActivity(intent);
            }
        });

        helper.setText(R.id.tv_no, item.getNo());
        helper.setText(R.id.tv_button_1, "共"+item.getCount()+"件商品，合计");
        helper.setText(R.id.tv_button_2, "￥"+item.getTotal());

        RecyclerView recyclerView = helper.getView(R.id.item_order_goods_list);

        if (item.getItems() != null && item.getItems().getData() != null && item.getItems().getData().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            setGoodsListData(recyclerView, item.getItems().getData(), item);
        } else {
            recyclerView.setVisibility(View.GONE);
        }

//        if (item.getTotal() != null) {
//            helper.setText(R.id.tv_prices, "¥"+item.getTotal());
//        }

        helper.setText(R.id.tv_status, item.getStatus_msg());
//        helper.setText(R.id.tv_detail, "共"+item.getCount()+"件商品, "+"已付款");

    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items, final MyOrderDto item) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        OrderStoreListAdapter mAdapter = new OrderStoreListAdapter(items);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_bg:
                        if (item != null) {
                            Intent intent = new Intent(mContext, StoreOrderDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mContext.startActivity(intent);


                        }
                        break;

                }
            }
        });

    }
}
