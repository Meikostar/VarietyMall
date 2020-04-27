package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.bean.RenWuBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.MyOrderDetailActivity;

import java.util.List;

/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class RenWuListAdapter extends BaseQuickAdapter<RenWuBean, BaseViewHolder> {
    private Context mContext;

    public RenWuListAdapter(List<RenWuBean> data, Context mContext) {
        super(R.layout.item_renwu_list_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RenWuBean item) {
        if (item != null) {
            helper.addOnClickListener(R.id.tv_go);
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_img), item.icon);
            helper.setText(R.id.tv_one, TextUtil.isEmpty(item.title) ? "" : item.title)
                    .setText(R.id.tv_two, TextUtil.isEmpty(item.description) ? "" : item.description);
            String flag = item.flag;
            String content = "";
            TextView tvGo = helper.getView(R.id.tv_go);
            if (TextUtil.isNotEmpty(flag)) {
                if (flag.equals("task_welcome")) {

                    content = "去领取";
                } else if (flag.equals("task_set_avatar")) {
                    content = "去设置";
                } else if (flag.equals("task_set_name")) {
                    content = "去设置";
                } else if (flag.equals("task_complete_info")) {
                    content = "去完善";
                } else if (flag.equals("task_first_share")) {
                    content = "去分享";
                } else if (flag.equals("task_first_follow")) {
                    content = "去关注";
                } else if (flag.equals("task_first_favorite")) {
                    content = "去收藏";
                } else if (flag.equals("task_first_order")) {
                    content = "去购物";
                } else if (flag.equals("task_first_comment")) {
                    content = "去评论";
                } else if (flag.equals("task_daily_check_in")) {
                    content = "去签到";
                } else if (flag.equals("task_daily_share_product")) {
                    content = "去分享";
                } else if (flag.equals("task_daily_invitation")) {
                    content = "去邀请";
                } else if (flag.equals("task_daily_child_order")) {
                    content = "去邀请";
                } else if (flag.equals("task_daily_watch_live")) {
                    content = "去观看";
                } else if (flag.equals("task_daily_live")) {
                    content = "去开播";
                }
                if (item.task_status == 0) {
                    tvGo.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_15_rede32));
                    tvGo.setText(content);
                } else if (item.task_status == 1) {
                    tvGo.setText("待领取");
                    tvGo.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_15_rede32));
                } else if (item.task_status == 2) {
                    String conts = content.substring(1, content.length());
                    tvGo.setText("已" + conts);
                    tvGo.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_15_back));
                }

            }

        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}