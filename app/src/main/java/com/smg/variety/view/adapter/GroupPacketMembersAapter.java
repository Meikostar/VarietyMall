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

/**
 * 积分红包好友圈
 * Created by rzb on 2019/6/20
 */
public class GroupPacketMembersAapter extends MyBaseAdapter<SortBean> {
    ViewHolder holder;
    Context   mContext;
    private   RefreshListsListener mRefreshListsListener;

    public GroupPacketMembersAapter(Context context, List<SortBean> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_group_packet_members, null);
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
                refreshData(var3);
            }
        });
        GlideUtils.getInstances().loadRoundCornerImg(mContext, holder.iv_delete_member, 2.5f, Constants.WEB_IMG_URL_UPLOADS + var3.getIcon());
    }

    class ViewHolder {
        ImageView iv_delete_member;
        TextView tv_delete_member;
        ImageView iv_delete;
    }

    private void refreshData(SortBean sortBean){
        if (mRefreshListsListener != null) {
            mRefreshListsListener.OnRefreshListener(sortBean);
        }
    }

    public void setRefreshListsListener(RefreshListsListener refreshListsListener) {
        this.mRefreshListsListener = refreshListsListener;
    }

    public interface RefreshListsListener {
        void OnRefreshListener(SortBean stBean);
    }
}
