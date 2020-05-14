package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.ShopDetailActivity;
import com.smg.variety.view.activity.ShopStoreDetailActivity;

import java.util.List;
import java.text.DecimalFormat;

/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class EntityStoreAdapter extends BaseQuickAdapter<RecommendListDto, BaseViewHolder> {
    private Context mContext;

    public EntityStoreAdapter(List<RecommendListDto> data, Context mContext) {
        super(R.layout.item_entity_store_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendListDto item) {
        if (item != null) {
            helper.setText(R.id.tv_entity_store_name, item.getShop_name());
            if(item.getArea()!=null){
                String dec="";
                for(String dc:item.getArea()){
                    dec=dec+dc;
                }

                helper.setText(R.id.tv_entity_store_distance, dec+(TextUtil.isNotEmpty(item.getAddress())?item.getAddress():""));
            }else {
                helper.setText(R.id.tv_entity_store_distance, "");
            }


            double disDouble = Double.valueOf(item.getDistance());
            long total=(long)disDouble;
            String strDis = "";
            if(total>1000){
                float km=total*1.0f/1000;
                helper.setText(R.id.tv_jl, km + "km");

            }else {
                helper.setText(R.id.tv_jl, total + "m");
            }
            DecimalFormat format = new DecimalFormat("#0.00");

            TextView view = helper.getView(R.id.tv_label_one);
            TextView tv_pp = helper.getView(R.id.tv_pp);
            TextView view1 = helper.getView(R.id.tv_label_two);
            TextView view2 = helper.getView(R.id.tv_label_three);
            if(item.service_arr != null) {
              if(item.service_arr.size()==1){
                  view.setVisibility(View.VISIBLE);
                  view1.setVisibility(View.INVISIBLE);
                  view2.setVisibility(View.INVISIBLE);
                  view.setText(item.service_arr.get(0));
              }else   if(item.service_arr.size()==2){
                  view.setVisibility(View.VISIBLE);
                  view1.setVisibility(View.VISIBLE);
                  view2.setVisibility(View.INVISIBLE);
                  view.setText(item.service_arr.get(0));
                  view1.setText(item.service_arr.get(1));
              }else   if(item.service_arr.size()>=3){
                  view.setVisibility(View.VISIBLE);
                  view1.setVisibility(View.VISIBLE);
                  view2.setVisibility(View.VISIBLE);
                  view.setText(item.service_arr.get(0));
                  view1.setText(item.service_arr.get(1));
                  view2.setText(item.service_arr.get(2));
              }else {
                  view.setVisibility(View.INVISIBLE);
                  view1.setVisibility(View.INVISIBLE);
                  view2.setVisibility(View.INVISIBLE);
              }
            }else {
                view.setVisibility(View.INVISIBLE);
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
            }
            if(item.getExt()!=null){
                if(TextUtil.isNotEmpty(item.getExt().is_brand)){
                    if(item.getExt().is_brand.equals("1")){
                        tv_pp.setVisibility(View.VISIBLE);
                    }else {
                        tv_pp.setVisibility(View.GONE);
                    }
                }else {
                    tv_pp.setVisibility(View.GONE);
                }
            }else {
                tv_pp.setVisibility(View.GONE);
            }
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_entity_store), item.getPhotos());
            helper.getView(R.id.layout_entity_store_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ShopStoreDetailActivity.SHOP_DETAIL_ID, item.getId());
//                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_LAT, item.getLat());
//                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_LNG, item.getLng());
                    gotoActivity(ShopStoreDetailActivity.class, bundle);
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