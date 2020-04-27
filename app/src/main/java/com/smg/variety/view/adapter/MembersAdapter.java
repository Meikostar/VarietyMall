package com.smg.variety.view.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.ScoreIncomeBean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.TimeUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.utils.TextUtil;


/**
 * 流水明细
 * Created by dahai on 2019/01/18.
 */
public class MembersAdapter extends BaseQuickAdapter<ScoreIncomeBean, BaseViewHolder> {
    public MembersAdapter(Context context) {
        super(R.layout.item_members, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreIncomeBean item) {
        GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.civ_user_avatar),item.avatar);
        ImageView ivState = helper.getView(R.id.iv_state);
        TextView tvState = helper.getView(R.id.tv_state);
        TextView account_tv = helper.getView(R.id.account_tv);
        account_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtil.isNotEmpty(item.wechat_number)){
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", item.wechat_number);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtil.showToast("复制成功");
                }else {
                    ToastUtil.showToast("会员未上传微信号");
                }

            }
        });
        ivState.setVisibility(View.GONE);
        if (item.level==0) {
            tvState.setText("注册会员");
        } else   if (item.level==1) {
            tvState.setText("掌柜");
            ivState.setVisibility(View.VISIBLE);
        } else if (item.level==2)  {
            tvState.setText("导师");
            ivState.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_wx,"微信号："+(TextUtil.isNotEmpty(item.wechat_number)?item.wechat_number:""))
                .setText(R.id.create_time_tv,item.created_at)
                .setText(R.id.tv_name,item.name);

    }

}