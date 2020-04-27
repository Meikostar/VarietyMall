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
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.view.mainfragment.consume.BrandShopDetailActivity;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;

import java.util.List;


/**
 * 消费商品推荐适配器
 * Created by rzb on 2019/4/20
 */
public class BrandsAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public BrandsAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_brands_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {

            if(item.ext!=null&&!TextUtils.isEmpty(item.ext.slogan)){

                helper.setText(R.id.tv_tag,item.ext.slogan);
            }

            NoScrollGridView gridContent = helper.getView(R.id.grid_content);
            Brandsdapter brandsdapter=new Brandsdapter(mContext);
            gridContent.setAdapter(brandsdapter);
            brandsdapter.setData(item.ext.imgs);
//            if(item.ext!=null&&item.ext.imgs!=null){
//                if(item.ext.imgs.size()==1){
//                    helper.setVisible(R.id.iv_img1,true);
//                    GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img1),item.ext.imgs.get(0),R.drawable.moren_sf);
//                }else if(item.ext.imgs.size()==2){
//                    helper.setVisible(R.id.iv_img1,true);
//                    helper.setVisible(R.id.iv_img2,true);
//                    GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img1),item.ext.imgs.get(0),R.drawable.moren_sf);
//                    GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img2),item.ext.imgs.get(1),R.drawable.moren_sf);
//
//                }else if(item.ext.imgs.size()==3){
//                    helper.setVisible(R.id.iv_img1,true);
//                    helper.setVisible(R.id.iv_img2,true);
//                    helper.setVisible(R.id.iv_img3,true);
//                    GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img1),item.ext.imgs.get(0),R.drawable.moren_product);
//                    GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img2),item.ext.imgs.get(1),R.drawable.moren_product);
//                    GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_img3),item.ext.imgs.get(2),R.drawable.moren_product);
//                }else if(item.ext.imgs.size()==0){
//                    helper.setVisible(R.id.iv_img1,false);
//                    helper.setVisible(R.id.iv_img2,false);
//                    helper.setVisible(R.id.iv_img3,false);
//                }
//
//
//            }

            GlideUtils.getInstances().loadRoundImg(mContext,helper.getView(R.id.iv_avatar),item.logo,R.drawable.moren_product);

            if(!TextUtils.isEmpty(item.name)){

                helper.setText(R.id.tv_sign,item.name);
            }

            if(!TextUtils.isEmpty(item.description)){

                helper.setText(R.id.tv_detail,item.description);
            }

            helper.getView(R.id.tv_go).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
//                    gotoActivity(CommodityDetailActivity.class, bundle);
                    Intent intent = new Intent(mContext, BrandShopDetailActivity.class);
                    intent.putExtra("id",item.getId());
                    mContext.startActivity(intent);
                }
            });
            helper.getView(R.id.iv_item_home_push).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //                    Bundle bundle = new Bundle();
                    //                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
                    //                    gotoActivity(CommodityDetailActivity.class, bundle);
                    Intent intent = new Intent(mContext, BrandShopDetailActivity.class);
                    intent.putExtra("id",item.getId());
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