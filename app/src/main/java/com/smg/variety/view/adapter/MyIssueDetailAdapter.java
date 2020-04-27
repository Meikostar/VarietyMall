package com.smg.variety.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.CommentListBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;

public class MyIssueDetailAdapter extends BaseQuickAdapter<CommentListBean, BaseViewHolder> {
    public MyIssueDetailAdapter(Context context) {
        super(R.layout.item_my_issue_detail, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentListBean item) {
        helper.setText(R.id.tv_item_my_issue_detail_name, item.getUser().getData().getName())
                .setText(R.id.tv_item_my_issue_detail_content, item.getComment())
                .setText(R.id.tv_item_my_issue_detail_time, item.getCreated_at());

        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.iv_item_my_issue_detail_icon), Constants.WEB_IMG_URL_UPLOADS+item.getUser().getData().getAvatar());
    }
}
