package com.smg.variety.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.bean.ScoreIncomeBean;
import com.smg.variety.utils.TextUtil;


/**
 * 流水明细
 * Created by dahai on 2019/01/18.
 */
public class RunningWaterDetailsAdapter extends BaseQuickAdapter<IncomeDto, BaseViewHolder> {
    public RunningWaterDetailsAdapter(Context context) {
        super(R.layout.item_running_water_detail, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, IncomeDto item) {


        int type = item.status;
        TextView view = helper.getView(R.id.account_tv);
        TextView view2 = helper.getView(R.id.type_name_tv);
        view.setText(item.value);
        if(TextUtil.isNotEmpty(item.type)){
            if(item.type.equals("withdraw")){
                view2.setText(item.type_name);
                view.setTextColor(mContext.getResources().getColor(R.color.my_color_18B1));
            }else {
                view2.setText(item.type_name);
                view.setTextColor(mContext.getResources().getColor(R.color.my_color_999999));
            }


        }else {
            view2.setText(item.type_name);
            view.setTextColor(mContext.getResources().getColor(R.color.my_color_18B1));
        }

        helper.setText(R.id.create_time_tv, item.created_at);

    }

}