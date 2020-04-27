package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.bean.RenWuBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.MyOrderDetailActivity;
import com.smg.variety.view.activity.ShopDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class RenWuAdapter extends BaseQuickAdapter<RenWuBean, BaseViewHolder> {
    private Context mContext;

    public RenWuAdapter(List<RenWuBean> data, Context mContext) {
        super(R.layout.item_renwu_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RenWuBean item) {
        if (item != null) {
            if(item.poistion==1){
                helper.setText(R.id.tv_name,"新手任务");
            }else {
                helper.setText(R.id.tv_name,"日常任务");
            }
            RecyclerView recyclerView = helper.getView(R.id.item_order_goods_list);
            setGoodsListData(recyclerView,item.list);
        }
    }
    private void setGoodsListData(RecyclerView recyclerView, List<RenWuBean> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        RenWuListAdapter mAdapter = new RenWuListAdapter(items,mContext);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_go:
                        RenWuBean item = (RenWuBean) adapter.getItem(position);
                        if(mListener!=null){
                            mListener.taskListener(item);
                        }
                        break;

                }
            }
        });

    }
    public interface ItemTaskListener{
        void taskListener(RenWuBean bean);
    }
     public ItemTaskListener mListener;
    public void setTaskListener(ItemTaskListener listener){
          mListener=listener;
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}