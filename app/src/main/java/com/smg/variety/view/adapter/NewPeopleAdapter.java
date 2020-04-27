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
import com.smg.variety.bean.RenWuBean;

import java.util.List;

/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class NewPeopleAdapter extends BaseQuickAdapter<RenWuBean, BaseViewHolder> {
    private Context mContext;

    public NewPeopleAdapter(List<RenWuBean> data, Context mContext) {
        super(R.layout.item_new_people, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RenWuBean item) {

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