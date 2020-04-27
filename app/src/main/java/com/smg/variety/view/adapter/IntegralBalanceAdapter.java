package com.smg.variety.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;

public class IntegralBalanceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public IntegralBalanceAdapter(Context context) {
        super(R.layout.item_integral_balance, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
