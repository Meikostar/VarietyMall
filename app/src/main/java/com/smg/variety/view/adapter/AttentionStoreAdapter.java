package com.smg.variety.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.widgets.CircleImageView;

/**
 * 关注店铺
 */
public class AttentionStoreAdapter extends BaseQuickAdapter<AttentionCommunityBean, BaseViewHolder> {
    public AttentionStoreAdapter(Context context) {
        super(R.layout.item_collect_store, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AttentionCommunityBean item) {
        helper.setText(R.id.tv_collect_store_name, item.getShop_name());
        helper.setText(R.id.tv_care, item.followers_count+"人关注");
        helper.setText(R.id.tv_couts, TextUtil.isNotEmpty(item.new_goods_count)?(item.new_goods_count.equals("0")?"暂无\n上新":item.new_goods_count+"\n上新"):"暂无\n上新");
        CircleImageView imageView = helper.getView(R.id.iv_collect_store_icon);
        GlideUtils.getInstances().loadNormalImg(mContext, imageView, Constants.WEB_IMG_URL_UPLOADS + item.getLogo());
    }
}
