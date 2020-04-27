package com.smg.variety.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.widgets.CircleImageView;

public class AttentionCommunityAdapter extends BaseQuickAdapter<AttentionCommunityBean, BaseViewHolder> {
    public AttentionCommunityAdapter(Context context) {
        super(R.layout.item_collect_community, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AttentionCommunityBean item) {
        helper.setVisible(R.id.iv_red_point, false)
                .setText(R.id.tv_collect_community_name, item.getName());
        CircleImageView imageView = helper.getView(R.id.iv_collect_community_icon);
        GlideUtils.getInstances().loadNormalImg(mContext, imageView,  item.getAvatar());
    }
}
