package com.smg.variety.view.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.StoreCategoryDto;

import java.util.List;

;


/**
 * Created by lihaoqi on 2019/01/25.
 */

public class ClassifyTwoAdapters extends BaseQuickAdapter<StoreCategoryDto,BaseViewHolder> {



    public int selctedPos = 0;

    public ClassifyTwoAdapters() {
        super(R.layout.item_classify_twos);
    }

    public ClassifyTwoAdapters(@Nullable List<StoreCategoryDto> data) {
        super(R.layout.item_classify_twos, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreCategoryDto item) {
        int pos = helper.getAdapterPosition();
        View view = helper.getView(R.id.iv_choose);
        if(selctedPos==pos){
            view.setVisibility(View.VISIBLE);
            helper.setTextColor(R.id.tv_name,Color.parseColor("#318fef"));
            helper.setBackgroundColor(R.id.tv_name,Color.parseColor("#ffffff"));
        }else{
            view.setVisibility(View.GONE);
            helper.setTextColor(R.id.tv_name,Color.parseColor("#212121"));
            helper.setBackgroundColor(R.id.tv_name,Color.parseColor("#ffffff"));
        }
        helper.setText(R.id.tv_name,item.getCategoryName());
    }


    @Override
    public void setNewData(@Nullable List<StoreCategoryDto> data) {
        super.setNewData(data);
        selctedPos = 0;
    }


}
