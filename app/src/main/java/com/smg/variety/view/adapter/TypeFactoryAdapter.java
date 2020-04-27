package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.ProductDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.activity.SuperMemberActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import java.util.List;

/**
 * 爱心工厂类别推荐适配器
 * Created by rzb on 2019/6/20
 */
public class TypeFactoryAdapter extends BaseQuickAdapter<ProductDto, BaseViewHolder> {
    private Context mContext;

    public TypeFactoryAdapter(List<ProductDto> data, Context mContext) {
        super(R.layout.item_type_factory_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductDto item) {
        if (item != null) {
            helper.setText(R.id.tv_type_factory_title, item.getTitle());
            helper.setText(R.id.tv_item_type_factory_price, item.getPrice());
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_type_factory),  item.getCover());
            helper.getView(R.id.layout_type_factory).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
                    bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                    gotoActivity(CommodityDetailActivity.class, bundle);
                }
            });

            TextView tvSj = helper.getView(R.id.tv_sj);
            ImageView iv_sj = helper.getView(R.id.iv_sj);
            if(BaseApplication.level==0){
                iv_sj.setVisibility(View.VISIBLE);
                tvSj.setText("升级为掌柜再省¥"+item.save_money);
                View view = helper.getView(R.id.ll_sj);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, SuperMemberActivity.class);
                        intent.putExtra("level",BaseApplication.level);
                        mContext.startActivity(intent);

                    }
                });
            }else {
                iv_sj.setVisibility(View.GONE);
                tvSj.setText("推广挣¥"+item.save_money);
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