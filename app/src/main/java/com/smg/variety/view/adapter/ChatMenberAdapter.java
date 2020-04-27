package com.smg.variety.view.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smg.variety.R;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.GAME;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StringUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.AppStoreActivity;
import com.smg.variety.view.widgets.autoview.CircleTransform;

import java.util.List;

public class ChatMenberAdapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<GAME>     foodlist;
    private String         types;


    public void setDatas(List<GAME> foodlist) {
        this.foodlist = foodlist;
        notifyDataSetChanged();
    }

    public ChatMenberAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public List<GAME> getFoodlist() {
        return foodlist;
    }

    @Override
    public int getCount() {
        return foodlist != null ? foodlist.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return foodlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.chat_menber_adapter, null);

            holder.txt_text = view.findViewById(R.id.tv_text);
            holder.txt_text1 = view.findViewById(R.id.tv_text1);
            holder.iv_img = view.findViewById(R.id.iv_img);
            holder.iv_img1 = view.findViewById(R.id.iv_img1);
            holder.ll_bg1 = view.findViewById(R.id.ll_bg1);
            holder.ll_bg2 = view.findViewById(R.id.ll_bg2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(foodlist.size()-1==i){
            if(foodlist.get(foodlist.size()-1).two!=null){
                holder.txt_text1.setText("更多");
                GlideUtils.getInstances().loadNormalImg(context,holder.iv_img1,R.drawable.moren_home,R.drawable.moren_sf);
            }
        }else if(foodlist.size()-2==i){
            if(foodlist.get(foodlist.size()-2).two==null){
                holder.txt_text1.setText("更多");
                GlideUtils.getInstances().loadNormalImg(context,holder.iv_img1,R.drawable.moren_home,R.drawable.moren_sf);
            }
        }
        if(foodlist.get(i).one!=null){
            holder.txt_text.setText(foodlist.get(i).one.title);
            GlideUtils.getInstances().loadNormalImg(context,holder.iv_img,foodlist.get(i).one.img,R.drawable.moren_sf);
            holder.ll_bg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtil.isNotEmpty(foodlist.get(i).one.url)){
                        mListener.clicks(foodlist.get(i).one);
                    }else {
                        ToastUtil.showToast("暂未开放");
                    }

                }
            });
        }

        if(foodlist.get(i).two!=null){
            if(foodlist.get(i).two.title.equals("更多")){
                holder.ll_bg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AppStoreActivity.class);
                        context.startActivity(intent);

                    }
                });
            }else {
                holder.txt_text1.setText(foodlist.get(i).two.title);
                GlideUtils.getInstances().loadNormalImg(context,holder.iv_img1,foodlist.get(i).two.img,R.drawable.moren_sf);
                holder.ll_bg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(TextUtil.isNotEmpty(foodlist.get(i).two.url)){
                            mListener.clicks(foodlist.get(i).two);
                        }else {
                            ToastUtil.showToast("暂未开放");
                        }
                    }
                });
            }

        }
        return view;
    }
    public void setClickListener( ClickListener mListener){
        this.mListener= mListener;
    }
    private ClickListener mListener;
    public interface  ClickListener{
        void clicks(BannerDto game);
    }
    public class ViewHolder {
        TextView     txt_text;
        TextView     txt_text1;
        ImageView    iv_img;
        ImageView    iv_img1;
        LinearLayout ll_bg1;
        LinearLayout    ll_bg2;

    }
}

