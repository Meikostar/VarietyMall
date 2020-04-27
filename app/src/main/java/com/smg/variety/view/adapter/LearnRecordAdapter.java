package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.LearnRecordInfo;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;

public class LearnRecordAdapter extends BaseQuickAdapter<LearnRecordInfo, BaseViewHolder> {
    public LearnRecordAdapter() {
        super(R.layout.learn_record_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, LearnRecordInfo item) {
        if (item != null && item.getObject() != null && item.getObject().getData() != null){
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), item.getObject().getData().getCover());
            helper.setText(R.id.tv_title, item.getObject().getData().getTitle());
        }else {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), Constants.WEB_IMG_URL_UPLOADS);
            helper.setText(R.id.tv_title, "");
        }

    }
}
