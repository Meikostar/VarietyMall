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
public class WalletAdapter extends BaseQuickAdapter<IncomeDto, BaseViewHolder> {


    public WalletAdapter() {
        super(R.layout.item_wallet_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, IncomeDto item) {

        helper.setText(R.id.tv_date, item.created_at);
        TextView view = helper.getView(R.id.tv_money);
        if(!TextUtil.isEmpty(item.type_name)){
            helper.setText(R.id.tv_des, item.type_name+"" );
        }
        if(Double.valueOf(item.value)>0){
            view.setTextColor(mContext.getResources().getColor(R.color.pldroid_streaming_red));
            helper.setText(R.id.tv_money, "+" + item.value);
//            helper.setText(R.id.tv_des, "充值" );
//            helper.setText(R.id.tv_type, "充值成功" );
        }else {
            view.setTextColor(mContext.getResources().getColor(R.color.my_color_333333));
            helper.setText(R.id.tv_money, item.value);
//            helper.setText(R.id.tv_des, "提现" );
//            helper.setText(R.id.tv_type, "提现成功" );
        }
    }
}