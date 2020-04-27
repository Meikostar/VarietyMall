package com.smg.variety.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.BaseDto2;
import com.smg.variety.view.widgets.FullyGridLayoutManager;

import java.util.List;

/**
 * Created by lihaoqi on 2019/1/25.
 */

public class ClassifyTwoAdapter extends BaseQuickAdapter<BaseDto2,BaseViewHolder> {
    private Context mContext;

    public ClassifyTwoAdapter(Context context) {
        super(R.layout.item_classify_two);
        this.mContext = context;
    }


    public ClassifyTwoAdapter(Context context, @Nullable List<BaseDto2> data) {
        super(R.layout.item_classify_two, data);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, BaseDto2 item) {
        helper.setText(R.id.tv_name,item.title);
        RecyclerView rvlist = helper.getView(R.id.rv_three);
        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(mContext,3);
        rvlist.setLayoutManager(gridLayoutManager);
        ClassifyThreeAdapter classifyThreeAdapter=null;
        if(item.children==null){
            if(item.mallBrands!=null){
                for(BaseDto2 baseDto2:item.mallBrands.data){
                    baseDto2.brand="1";
                }
            }
            classifyThreeAdapter = new ClassifyThreeAdapter(mContext,item.mallBrands.data);
        }else {
            classifyThreeAdapter = new ClassifyThreeAdapter(mContext,item.children.data);
        }

        classifyThreeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                //TODO 实现 跳转
            }
        });
        rvlist.setAdapter(classifyThreeAdapter);
    }


}
