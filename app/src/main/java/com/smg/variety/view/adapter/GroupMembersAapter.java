package com.smg.variety.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;

import java.util.List;

/**
 * 群成员
 * Created by rzb on 2019/6/20
 */
public class GroupMembersAapter extends MyBaseAdapter<GroupUserItemInfoDto> {
    ViewHolder holder;
    Context mContext;

    public GroupMembersAapter(Context context, List<GroupUserItemInfoDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_goods_gridview, null);
        holder = new ViewHolder();
        holder.iv = (ImageView) view.findViewById(R.id.iv);
        holder.tv = (TextView) view.findViewById(R.id.tv);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, GroupUserItemInfoDto var3) {
        holder = (ViewHolder) var1.getTag();
        if(var3.getGroup_nickname() != null) {
            holder.tv.setText(var3.getGroup_nickname());
        }else{
            holder.tv.setText(var3.getUser().getData().getName());
        }
        GlideUtils.getInstances().loadRoundCornerImg(mContext, holder.iv, 2.5f, Constants.WEB_IMG_URL_UPLOADS + var3.getUser().getData().getAvatar());
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
