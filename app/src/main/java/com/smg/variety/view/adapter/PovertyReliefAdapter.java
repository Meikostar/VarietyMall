package com.smg.variety.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;

public class PovertyReliefAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PovertyReliefAdapter(Context context) {
        super(R.layout.item_poverty_relief, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
