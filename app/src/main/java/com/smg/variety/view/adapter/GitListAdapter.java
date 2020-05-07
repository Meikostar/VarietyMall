package com.smg.variety.view.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.IncomeDto;
import com.smg.variety.utils.TextUtil;


/**
 * 钱包 记录适配器
 */
public class GitListAdapter extends BaseQuickAdapter<IncomeDto, BaseViewHolder> {


    public GitListAdapter() {
        super(R.layout.item_gits_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, IncomeDto item) {

        helper.setText(R.id.tv_date, item.created_at);
        TextView view = helper.getView(R.id.tv_money);
        if(!TextUtil.isEmpty(item.gift_name)){
            helper.setText(R.id.tv_des, item.gift_name+"" );
        }
        helper.setText(R.id.tv_money, "+" + item.qty+"个");
    }
}