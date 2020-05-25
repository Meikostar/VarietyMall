package com.smg.variety.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.db.bean.HomeDto;

import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by mykar on 161/4/13.
 */

public class GoupsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HomeDto> data;
    private int type = 0;//0 表示默认使用list数据
    private String types;


    private String[] names;

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setTypes(String types) {
        this.types = types;
        notifyDataSetChanged();
    }

    public GoupsAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<HomeDto> data) {
        this.data = data;

    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private Conversation.ConversationType conversationType;
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.rc_voip_listitem_select_member, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder = (ViewHolder) convertView.getTag();
//        holder.checkbox.setTag(mallMembers.get(position));
//        if (invitedMembers.contains(mallMembers.get(position))) {
//            holder.checkbox.setClickable(false);
//            holder.checkbox.setEnabled(false);
//            holder.checkbox.setImageResource(io.rong.callkit.R.drawable.rc_voip_icon_checkbox_checked);
//        } else {
//            if (selectedMember.contains(mallMembers.get(position))) {
//                holder.checkbox.setImageResource(io.rong.callkit.R.drawable.rc_voip_checkbox);
//                holder.checkbox.setSelected(true);
//            } else {
//                holder.checkbox.setImageResource(io.rong.callkit.R.drawable.rc_voip_checkbox);
//                holder.checkbox.setSelected(false);
//            }
//            holder.checkbox.setClickable(false);
//            holder.checkbox.setEnabled(true);
//        }
        holder.checkbox.setSelected(true);
        UserInfo userInfo = RongContext.getInstance().getUserInfoFromCache(data.get(i).id);
        String displayName = "";
        if (conversationType != null && conversationType.equals(Conversation.ConversationType.GROUP)) {
            GroupUserInfo groupUserInfo = RongUserInfoManager.getInstance().getGroupUserInfo("", data.get(i).id);
            if (groupUserInfo != null && !TextUtils.isEmpty(groupUserInfo.getNickname())) {
                displayName = groupUserInfo.getNickname();
                holder.name.setText(displayName);
            }
        }
        if (TextUtils.isEmpty(displayName)) {
            if (userInfo != null) {
                holder.name.setText(userInfo.getName());
            } else {
//                holder.name.setText(mallMembers.get(position));
            }
        }
        if (userInfo != null) {
            holder.portrait.setAvatar(userInfo.getPortraitUri());
        } else {
            holder.portrait.setAvatar(null);
        }
        // PROFILE_ITEM item = list.get(i);
        return convertView;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(HomeDto url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    class ViewHolder {
        ImageView      checkbox;
        AsyncImageView portrait;
        TextView       name;
    }
}
