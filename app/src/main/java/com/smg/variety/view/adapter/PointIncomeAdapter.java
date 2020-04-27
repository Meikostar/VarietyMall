package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.ScoreIncomeBean;

public class PointIncomeAdapter extends BaseQuickAdapter<ScoreIncomeBean, BaseViewHolder> {
    public PointIncomeAdapter() {
        super(R.layout.point_income_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreIncomeBean item) {
        helper.setText(R.id.tv_title, item.getType_name())
                .setText(R.id.tv_time, item.getCreated_at())
                .setText(R.id.tv_get_score, item.getValue());

    }
}
