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

import com.smg.variety.R;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.AppNewPeopleActivity;
import com.smg.variety.view.activity.LiveVideoViewActivity;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.activity.WebViewActivity;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class VideoLivesAdapter extends BaseAdapter {
    private Context             context;
    private LayoutInflater      inflater;
    private List<VideoLiveBean> list;
    private int                 type = 0;//0 表示默认使用list数据
    private String              types;


    private int[] imgs;


    private String[] names;

    public VideoLivesAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<VideoLiveBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public List<VideoLiveBean> getData() {
       return list;
    }
    @Override
    public int getCount() {
        return list!=null?(list.size()):0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }



    @Override
    public long getItemId(int i) {
        return i;
    }
    public interface ItemClickListener{
        void itemClick(String id);
    }
    private ItemClickListener mListener;
    public void setListener(ItemClickListener listener){
        mListener=listener;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_video_live, null);
            holder.img_del = view.findViewById(R.id.img_del);
            holder.img_icon = view.findViewById(R.id.img_icon);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        VideoLiveBean bannerDto = list.get(i);

        if(bannerDto.isChoose){
            holder.img_del.setVisibility(View.VISIBLE);
        }else {
            holder.img_del.setVisibility(View.GONE);
        }
        holder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null){
                    mListener.itemClick(bannerDto.getId());
                }
            }
        });
        holder.img_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLiveVideoActivity(bannerDto);
            }
        });
        GlideUtils.getInstances().loadNormalImg(context,holder.img_icon,bannerDto.images,R.drawable.moren_sf);
        // PROFILE_ITEM item = list.get(i);
        return view;
    }
    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(context, LiveVideoViewActivity.class);

        if (videoLiveBean.apply != null && videoLiveBean.apply.getData() != null&&TextUtil.isEmpty(videoLiveBean.end_at)) {
            intent.putExtra("videoPath", videoLiveBean.apply.getData().rtmp_play_url);
            LiveVideoViewActivity.state=0;
        }else {
            if(TextUtil.isNotEmpty(videoLiveBean.end_at)&&TextUtil.isNotEmpty(videoLiveBean.play_url)){
                LiveVideoViewActivity.state=1;
                intent.putExtra("videoPath", "http://pili-vod.bbsc.2aa6.com/"+videoLiveBean.play_url);
            }
        }
        if (videoLiveBean.getRoom() != null && videoLiveBean.getRoom().getData() != null) {
            intent.putExtra("roomId", videoLiveBean.getRoom().getData().getId());
        }
        if (videoLiveBean.apply != null && videoLiveBean.apply.getData() != null) {
            intent.putExtra("userId", videoLiveBean.apply.getData().getUser_id());
        }

        intent.putExtra("videoId", videoLiveBean.getId());
        intent.putExtra("liveStreaming", 1);
        context.startActivity(intent);
    }


    class ViewHolder {

        ImageView    img_icon;
        ImageView    img_del;
        LinearLayout llbg;

    }
}
