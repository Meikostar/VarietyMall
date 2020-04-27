package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.mainfragment.consume.BrandShopDetailActivity;

import java.util.List;


/**
 * 消费商品推荐适配器
 * Created by rzb on 2019/4/20
 */
public class ConturyProcutAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public ConturyProcutAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_contury_procut_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {


            if(item.category!=null&&item.category.data!=null&&!TextUtils.isEmpty(item.category.data.title)){

                helper.setText(R.id.tv_title, item.ext.slogan);
                GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_contury), Constants.WEB_IMG_URL_UPLOADS + item.category.data.icon);

            }
            if(!TextUtils.isEmpty(item.name)){
                helper.setText(R.id.tv_contury,item.name);
            }


            GlideUtils.getInstances().loadProcuctNormalImg(mContext, helper.getView(R.id.iv_item_consume_push_img), item.logo);
            helper.getView(R.id.iv_item_home_push).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, BrandShopDetailActivity.class);
                    intent.putExtra("id",item.id);
                    mContext.startActivity(intent);

                }
            });
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