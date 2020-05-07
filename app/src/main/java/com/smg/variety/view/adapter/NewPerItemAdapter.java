package com.smg.variety.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.bean.MapDto;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.common.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author zaaach on 2016/1/26.
 */
public class NewPerItemAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;

    private Context mContext;


    public NewPerItemAdapter(Context context) {
        this.mContext = context;

    }

    private List<NewPeopleBeam> list;

    public void setData(List<NewPeopleBeam> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public NewPeopleBeam getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            view = View.inflate(mContext, R.layout.item_new_people, null);
            holder = new ViewHolder(view);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        NewPeopleBeam item=list.get(position);
        holder.tvTitle.setText(item.coupon.data.name);
        holder.tvTitle.setText(item.coupon.data.description);

        if(item.coupon.data.shop_id!=null){
            if(item.coupon.data.shop_id.equals("0")){
                holder.tvTy.setText("全场通用");

            }else {
                if(item.coupon.data.shop!=null&&item.coupon.data.shop.data!=null){
                    holder.tvTy.setText("仅"+item.coupon.data.shop.data.shop_name+"店铺可用");
                }else {
                    holder.tvTy.setText("全场通用");
                }

            }
        }
        if(item.coupon.data.discount_type==1){
            holder.tvFh.setVisibility(View.VISIBLE);
            holder.tvCout.setText(item.coupon.data.discount_value);
        }else if(item.coupon.data.discount_type==2){
            holder.tvFh.setVisibility(View.GONE);
            holder.tvCout.setText((int)(Double.valueOf(item.coupon.data.discount_value)*10)+"折");
        }else {
            holder.tvFh.setVisibility(View.VISIBLE);
            holder.tvCout.setText(item.coupon.data.discount_value);
        }
        if (item.userCoupon != null && item.userCoupon.data != null) {
            holder.ivState.setVisibility(View.VISIBLE);
            long stringToDate = TimeUtil.getStringToDate(item.userCoupon.data.expire_at);
            long currentTimeMillis = System.currentTimeMillis();
            holder.tvTime.setText( item.userCoupon.data.expire_at+"到期");

            holder.tvGet.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_bfbfbf));
            if(stringToDate>currentTimeMillis){
                holder.tvGet.setText("去使用");
                holder.tvGet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.taskListener(null);
                        }
                    }
                });
            }else {
                holder.tvGet.setText("已过期");
            }
        } else {
            holder.ivState.setVisibility(View.GONE);
            if (item.day_type == 2) {
                holder.tvGet.setText("领券");
                holder.tvGet.setBackground(mContext.getResources().getDrawable(R.drawable.new_people_bg));
                holder.tvTime.setText("领券"+ item.end+"内可用");


            } else if (item.day_type == 1) {

                holder.tvTime.setText( item.end+"到期");
            }else if (item.day_type == 3) {
                holder.tvTime.setText( "领取后第二天内可用");

            }
        }
        holder.tvGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.taskListener(item);
                }
            }
        });
        return view;
    }

    public interface TakeawayListener {
        void listener(int poistion);

    }
    public interface ItemTaskListener {
        void taskListener(NewPeopleBeam bean);
    }

    public NewPeopleAdapter.ItemTaskListener mListener;

    public void setTaskListener(NewPeopleAdapter.ItemTaskListener listener) {
        mListener = listener;
    }

    private TakeawayListener listener;

    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }



    static class ViewHolder {
        @BindView(R.id.tv_title)
        TextView  tvTitle;
        @BindView(R.id.tv_time)
        TextView  tvTime;
        @BindView(R.id.tv_fh)
        TextView  tvFh;
        @BindView(R.id.tv_cout)
        TextView  tvCout;
        @BindView(R.id.tv_desc)
        TextView  tvDesc;
        @BindView(R.id.tv_ty)
        TextView  tvTy;
        @BindView(R.id.tv_get)
        TextView  tvGet;
        @BindView(R.id.iv_state)
        ImageView ivState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
