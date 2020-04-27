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
        view.setText("-"+item.money);
        if(type==0){
            view2.setText("申请提现");
            view.setTextColor(mContext.getResources().getColor(R.color.my_color_18B1));

        }else if(type==1){
            view2.setText("提现到账");
            view.setTextColor(mContext.getResources().getColor(R.color.my_color_18B1));
        }else if(type==2){
            view2.setText("提现失败");
            view.setTextColor(mContext.getResources().getColor(R.color.my_color_999999));
        }

        helper.setText(R.id.create_time_tv, item.created_at);

    }

}