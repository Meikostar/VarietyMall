package com.smg.variety.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.widgets.CircleImageView;

/**
 * 关注店铺
 */
public class AttentionLiveAdapter extends BaseQuickAdapter<AttentionCommunityBean, BaseViewHolder> {
    public AttentionLiveAdapter(Context context) {
        super(R.layout.item_attention_live, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AttentionCommunityBean item) {
        PersonalInfoDto  user=item.user.getData();
        TextView view = helper.getView(R.id.tv_state);
        GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img),user.getAvatar(),R.drawable.moren_ren);
        helper.setText(R.id.tv_name,user.getName())
                .setText(R.id.tv_fs,user.followingsCount+"粉丝");
        if (user.isFollowed) {
            view.setText("已关注");
            view.setTextColor(mContext.getResources().getColor(R.color.white));
            view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_14_blue));
        } else {
            view.setText("关注");
            view.setTextColor(mContext.getResources().getColor(R.color.my_color_blue));
            view.setBackground(mContext.getResources().getDrawable(R.drawable.blue_regrect_line15));
        }
        helper.addOnClickListener(R.id.tv_state);
    }
}
