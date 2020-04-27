package com.smg.variety.view.adapter;

import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.bean.ScoreIncomeBean;


/**
 * 流水明细
 * Created by dahai on 2019/01/18.
 */
public class MoreDetailAdapter extends BaseQuickAdapter<ScoreIncomeBean, BaseViewHolder> {
    public MoreDetailAdapter(Context context) {
        super(R.layout.item_running_water_detail, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreIncomeBean item) {

        helper.setText(R.id.type_name_tv, item.getType_name())
                .setText(R.id.create_time_tv, item.getCreated_at())
                .setText(R.id.account_tv, item.getValue());

    }

}