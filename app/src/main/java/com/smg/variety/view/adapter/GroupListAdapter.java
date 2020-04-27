package com.smg.variety.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.GroupListDto;
import com.smg.variety.common.utils.GlideUtils;

public class GroupListAdapter extends BaseQuickAdapter<GroupListDto, BaseViewHolder> {

    public GroupListAdapter() {
        super(R.layout.item_group_list_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListDto item) {
        helper.setText(R.id.tv_group_name, item.getGroup_name());
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.tv_group_icon), item.getAvatar());
    }
}
