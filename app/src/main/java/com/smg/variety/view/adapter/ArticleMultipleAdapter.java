package com.smg.variety.view.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.smg.variety.R;
import com.smg.variety.bean.ArticleBean;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ScreenSizeUtil;
import com.smg.variety.qiniu.live.utils.Config;
import com.smg.variety.utils.UIUtils;

import java.util.List;

/**
 * 文章 多布局 Adapter
 */
public class ArticleMultipleAdapter extends BaseQuickAdapter<DetailDto, BaseViewHolder> {

//    private final int mWidth;

    public ArticleMultipleAdapter(Context mContext, List<DetailDto> data) {
        super(R.layout.layout_item_home_view_simple, data);
        this.mContext = mContext;
//        addItemType(Config.TYPE_SIMPLE, R.layout.layout_item_home_view_simple);
//
//        int screenWidth = ScreenSizeUtil.getScreenWidth(mContext);
//        mWidth = (int) ((screenWidth - DensityUtil.dp2px(30)) * 1.0f / 3 + .5f);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailDto item) {

                setSimpleData(helper, item);

    }


    private void setSimpleData(BaseViewHolder helper, DetailDto item) {
        String content = item.content;//content含有HTML标签
        content = content.replace("<em>", "");
        content = content.replace("</em>", "");
        content = content.replace("<br>", "\n");
        content = content.replace("</br>", "");
        content = content.replace("&nbsp;", " ");
        content = content.replace("<div>", "\n");
        content = content.replace("</div>", "");
        content = content.replace("<p>", "");
        content = content.replace("</p>", "");
        helper.setText(R.id.tv_home_simple_title, content);
        helper.setText(R.id.tv_tl, item.title);

        ImageView imageView = helper.getView(R.id.iv_home_simple_icon);
        GlideUtils.getInstances().loadNormalImg(mContext, imageView, item.img);
    }




}

