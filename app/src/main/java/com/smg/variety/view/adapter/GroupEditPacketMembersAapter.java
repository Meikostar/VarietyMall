package com.smg.variety.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.bean.SortBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;

import java.util.List;

public class GroupEditPacketMembersAapter extends MyBaseAdapter<SortBean> {
    ViewHolder holder;
    Context    mContext;
    private    RefreshListsListener mRefreshListsListener;

    public GroupEditPacketMembersAapter(Context context, List<SortBean> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_detele_member_gridview, null);
        holder = new ViewHolder();
        holder.iv_delete_member = (ImageView)view.findViewById(R.id.iv_delete_member);
        holder.tv_delete_member = (TextView)view.findViewById(R.id.tv_delete_member);
        holder.iv_delete =(ImageView)view.findViewById(R.id.iv_delete);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, SortBean var3) {
         holder = (ViewHolder)var1.getTag();
         if(var3.getRemarkName() != null) {
            holder.tv_delete_member.setText(var3.getRemarkName());
         }else{
            holder.tv_delete_member.setText(var3.getName());
         }
         holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDate(var3);
            }
         });
         GlideUtils.getInstances().loadRoundCornerImg(mContext, holder.iv_delete_member,
                2.5f, Constants.WEB_IMG_URL_UPLOADS + var3.getIcon());
    }

    class ViewHolder {
        ImageView iv_delete_member;
        TextView tv_delete_member;
        ImageView iv_delete;
    }

    private void refreshDate(SortBean sBean){
        if (mRefreshListsListener != null) {
            mRefreshListsListener.OnRefreshListener(sBean);
        }
    }

    public void setRefreshListsListener(RefreshListsListener refreshListsListener) {
        this.mRefreshListsListener = refreshListsListener;
    }

    public interface RefreshListsListener {
        void OnRefreshListener(SortBean sortBean);
    }
}
