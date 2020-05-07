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
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.common.utils.TimeUtil;

import java.util.List;

/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class NewPeopleAdapterss extends BaseQuickAdapter<NewPeopleBeam, BaseViewHolder> {
    private Context mContext;

    public NewPeopleAdapterss(List<NewPeopleBeam> data, Context mContext) {
        super(R.layout.item_new_peopless, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewPeopleBeam item) {
        if (item != null) {

            TextView view = helper.getView(R.id.tv_get);
            TextView tv_fh = helper.getView(R.id.tv_fh);
            TextView tv_ty = helper.getView(R.id.tv_ty);
            TextView tv_cout = helper.getView(R.id.tv_cout);
            ImageView iv_state = helper.getView(R.id.iv_state);
            helper.setText(R.id.tv_title, item.coupon.data.name)
                    .setText(R.id.tv_desc, item.coupon.data.description);
            if(item.coupon.data.shop_id!=null){
                if(item.coupon.data.shop_id.equals("0")){
                    tv_ty.setText("全场通用");

                }else {
                    if(item.coupon.data.shop!=null&&item.coupon.data.shop.data!=null){
                        tv_ty.setText("仅"+item.coupon.data.shop.data.shop_name+"店铺可用");
                    }else {
                        tv_ty.setText("全场通用");
                    }

                }
            }
            if(item.coupon.data.discount_type==1){
                tv_fh.setVisibility(View.VISIBLE);
                tv_cout.setText(item.coupon.data.discount_value);
            }else if(item.coupon.data.discount_type==2){
                tv_fh.setVisibility(View.GONE);
                tv_cout.setText((int)(Double.valueOf(item.coupon.data.discount_value)*10)+"折");
            }else {
                tv_fh.setVisibility(View.VISIBLE);
                tv_cout.setText(item.coupon.data.discount_value);
            }
            if (item.userCoupon != null && item.userCoupon.userCoupon.data != null) {
                iv_state.setVisibility(View.VISIBLE);
                long stringToDate = TimeUtil.getStringToDate(item.userCoupon.data.expire_at);
                long currentTimeMillis = System.currentTimeMillis();
                helper.setText(R.id.tv_time, item.userCoupon.data.expire_at+"到期");
                view.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_bfbfbf));
                if(stringToDate>currentTimeMillis){
                    view.setText("去使用");
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.taskListener(null);
                            }
                        }
                    });
                }else {
                    view.setText("已过期");
                }
            } else {
                iv_state.setVisibility(View.GONE);
                if (item.day_type == 2) {
                    view.setText("去使用");
                    view.setBackground(mContext.getResources().getDrawable(R.drawable.new_people_bg));
                    helper.setText(R.id.tv_time, item.coupon.data.name);
                } else if (item.day_type == 1) {
                    helper.setText(R.id.tv_time, item.end+"到期");

                }else if (item.day_type == 3) {
                    helper.setText(R.id.tv_time, "领取后第二天内可用");
                }
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.taskListener(item);
                    }
                }
            });

        }


    }

    public interface ItemTaskListener {
        void taskListener(NewPeopleBeam bean);
    }

    public ItemTaskListener mListener;

    public void setTaskListener(ItemTaskListener listener) {
        mListener = listener;
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}