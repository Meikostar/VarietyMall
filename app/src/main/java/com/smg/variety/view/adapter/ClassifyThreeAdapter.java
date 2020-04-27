package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.BaseDto2;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.activity.ProductListActivity;
import com.smg.variety.view.mainfragment.consume.ProductSearchResultActivity;

import java.util.List;

/**
 * Created by lihaoqi on 2019/1/25.
 */

public class ClassifyThreeAdapter extends BaseQuickAdapter<BaseDto2, BaseViewHolder> {
    private Context mContext;

    public ClassifyThreeAdapter(Context context, @Nullable List<BaseDto2> data) {
        super(R.layout.item_classify_three, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseDto2 item) {
        helper.setText(R.id.tv_name, item.title);
        ImageView imageView = helper.getView(R.id.iv_img);
        if(item.brand!=null){
            GlideUtils.getInstances().loadNormalImg(mContext, imageView, item.logo, R.drawable.moren_sf);
        }else {
            GlideUtils.getInstances().loadNormalImg(mContext, imageView, item.icon, R.drawable.moren_sf);
        }

        helper.getView(R.id.ll_item_classify_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProductListActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString(ProductSearchResultActivity.ACTION_SEARCH_ID,item.id);
                if(item.brand!=null){
                    bundle.putString("brand","1");
                }

                intent.putExtras(bundle);
                mContext.startActivity(intent);
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
