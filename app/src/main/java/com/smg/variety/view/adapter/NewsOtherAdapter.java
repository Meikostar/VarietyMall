package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.NewsOtherListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.mainfragment.community.HuoDonDetailActivity;

import java.util.List;

public class NewsOtherAdapter extends BaseQuickAdapter<NewsOtherListItemDto, BaseViewHolder> {

    public NewsOtherAdapter(List<NewsOtherListItemDto> data, Context mContext) {
        super(R.layout.item_news_one_list_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsOtherListItemDto item) {
        helper.setText(R.id.tv_one_title, item.getTitle())
                .setText(R.id.tv_one_content, item.getIntroduce())
                .setText(R.id.tv_one_time, item.getCreated_at());
        GlideUtils.getInstances().loadRoundCornerImg(mContext,helper.getView(R.id.iv_one), 1.5f,Constants.WEB_IMG_URL_UPLOADS + item.getImg());
        helper.getView(R.id.layout_news_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("from","other");
                bundle.putSerializable(HuoDonDetailActivity.HUODON, item);
                gotoActivity(HuoDonDetailActivity.class, bundle);
            }
        });
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}