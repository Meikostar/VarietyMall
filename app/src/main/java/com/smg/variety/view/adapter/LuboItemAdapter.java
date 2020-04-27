package com.smg.variety.view.adapter;

import android.content.Context;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.TimeUtil;

/**
 * 订单列表中 商品的adapter
 */
public class LuboItemAdapter extends BaseQuickAdapter<VideoLiveBean, BaseViewHolder> {

    public LuboItemAdapter(Context context) {
        super(R.layout.item_lubo_view, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoLiveBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.img), item.images,R.drawable.moren_sf);
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_see, item.play_count+"观看");
        helper.setText(R.id.tv_product_cout, item.products==null?0+"宝贝":item.products.size()+"宝贝");
        helper.setText(R.id.tv_time, TimeUtil.formatChatTime(TimeUtil.getStringToDate(item.getCreated_at())));


        helper.addOnClickListener(R.id.img);

    }
}
