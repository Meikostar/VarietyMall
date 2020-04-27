package com.smg.variety.view.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.ServiceMenuBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;

public class ServiceMenuAdapter extends BaseQuickAdapter<ServiceMenuBean, BaseViewHolder> {
    public ServiceMenuAdapter() {
        super(R.layout.item_service_menu);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceMenuBean item) {
        helper.setText(R.id.tv_title,item.getTitle());
        GlideUtils.getInstances().loadNormalImg(mContext,(ImageView) helper.getView(R.id.img_logo), item.getImages());
    }
}
