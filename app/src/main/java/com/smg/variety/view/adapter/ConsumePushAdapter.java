package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ScreenSizeUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.SuperMemberActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;


/**
 * 消费商品推荐适配器
 * Created by rzb on 2019/4/20
 */
public class ConsumePushAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public ConsumePushAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_consume_push_layout, data);
        this.mContext = mContext;
    }
    private int TYPE_HEADER = 1001;
    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {
            helper.setText(R.id.tv_title, item.getTitle());
            TextView tvsin = helper.getView(R.id.tv_sign);
            if(item.labels!=null&&item.labels.size()>0){
                tvsin.setVisibility(View.VISIBLE);
                if(TextUtil.isNotEmpty(item.labels.get(0))){
                    tvsin.setText(item.labels.get(0));
                }else {
                    tvsin.setVisibility(View.GONE);
                }

            }else {
                tvsin.setVisibility(View.GONE);
            }
            if(item.ext!=null&&!TextUtils.isEmpty(item.ext.slogan)){

                helper.setText(R.id.tv_tag,item.ext.slogan);
            }
            if(item.brand!=null&&item.brand.data.category.data!=null&&!TextUtils.isEmpty(item.brand.data.category.data.title)){

                helper.setText(R.id.tv_contury,item.brand.data.category.data.title);
                GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_contury), item.brand.data.category.data.icon);

            }
            FrameLayout frameLayout = helper.getView(R.id.fl_line);
            View line = helper.getView(R.id.line);
            if(!TextUtils.isEmpty(item.market_price)){
                frameLayout.setVisibility(View.VISIBLE);

                String name = "¥"+item.market_price;
                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(12);
                int with = (int) textPaint.measureText(name);
                FrameLayout.LayoutParams linearParams =(FrameLayout.LayoutParams) line.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

                linearParams.width = ScreenSizeUtil.dp2px(with-1);// 控件的宽强制设成30

                line.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                helper.setText(R.id.tv_price,name);
            }else {
                frameLayout.setVisibility(View.GONE);
            }

            if(item.ext!=null&&TextUtils.isEmpty(item.ext.slogan)){
                helper.setText(R.id.tv_tag,item.ext.slogan);
            }
            helper.setText(R.id.tv_item_home_good_shop_price,  item.getPrice());
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

            GlideUtils.getInstances().loadProcuctNormalImg(mContext, helper.getView(R.id.iv_item_consume_push_img), item.getCover());
            helper.getView(R.id.iv_item_home_push).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommodityDetailActivity.FROM, "gc");
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
                    bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                    gotoActivity(CommodityDetailActivity.class, bundle);
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