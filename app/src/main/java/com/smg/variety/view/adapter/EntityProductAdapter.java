package com.smg.variety.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.donkingliang.labels.LabelsView;
import com.smg.variety.R;
import com.smg.variety.bean.BaseDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;

/**
 * 关注店铺
 */
public class EntityProductAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    public EntityProductAdapter(Context context) {
        super(R.layout.entity_product_item, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {


        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_id, item.getId());
        helper.setText(R.id.tv_kc, "库存："+item.stock+"件");
        helper.setText(R.id.tv_xs, "已销售："+item.sales_count+"件");
        TextView tv_del = helper.getView(R.id.tv_del);
        if(item.on_sale){
            tv_del.setText("下架");
            tv_del.setTextColor(mContext.getResources().getColor(R.color.white));
            tv_del.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_red));
        }else {
            tv_del.setText("上架");
            tv_del.setTextColor(mContext.getResources().getColor(R.color.my_color_333333));
            tv_del.setBackground(mContext.getResources().getDrawable(R.drawable.carda0_regrect_line9));
        }

        TextView textview = helper.getView(R.id.tv_prices);

        LabelsView labelsView = helper.getView(R.id.tv_item_select_commodity_datas);
        labelsView.setSelectType(LabelsView.SelectType.SINGLE_IRREVOCABLY);

        labelsView.setLabels(item.labels, new LabelsView.LabelTextProvider<String>() {
            @Override
            public CharSequence getLabelText(TextView label, int position, String name) {

                return name;
            }
        });


        helper.addOnClickListener(R.id.tv_del);
        helper.addOnClickListener(R.id.ll_bg);
        helper.addOnClickListener(R.id.iv_bj);
        helper.setText(R.id.tv_price,"¥"+ item.getPrice());
        GlideUtils.getInstances().loadProcuctNormalImg(mContext, helper.getView(R.id.iv_img),  item.getCover());
 }
}
