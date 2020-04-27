package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.InviteFriendBean;
import com.smg.variety.common.utils.GlideUtils;

public class InviteFiendAdapter extends BaseQuickAdapter<InviteFriendBean, BaseViewHolder> {
    public InviteFiendAdapter() {
        super(R.layout.item_invite_friends);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteFriendBean item) {
        helper.setText(R.id.tv_phone,item.getPhone())
                .setText(R.id.tv_time,item.getCreated_at())
                .setText(R.id.tv_name,item.name);

        GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_img), item.avatar);

    }
}
