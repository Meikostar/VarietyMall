package com.smg.variety.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smg.variety.R;
import com.smg.variety.bean.BannerDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StringUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.LiveVideoViewActivity;
import com.smg.variety.view.widgets.banner.BannerBaseAdapter;
import com.youth.banner.Banner;

import java.util.List;

public class BannerLiveAdapter extends BannerBaseAdapter {

    private List<BannerDto> list;
    private int             type=0;//1是用药记录item

    public BannerLiveAdapter(Context context) {
        super(context);
    }


    public interface ItemCliks{
        void getItem(BannerDto menu, int type);//type 1表示点击事件2 表示长按事件
    }
    private ItemCliks listener;
    public void setClickListener(ItemCliks listener){
        this.listener=listener;
    }



    @Override
    protected int getLayoutResID() {

        return R.layout.banner_live_item;
    }
    private ImageView img;
    private Banner    banner;
    @Override
    protected void convert(View convertView, Object data) {
        VideoLiveBean bean= (VideoLiveBean) data;
        LinearLayout ll_bg = convertView.findViewById(R.id.ll_bg);
        LinearLayout ll_bg1 = convertView.findViewById(R.id.ll_bg1);
        ImageView ivOne = convertView.findViewById(R.id.iv_one);
        ImageView ivTwo = convertView.findViewById(R.id.iv_two);
        TextView tvOne = convertView.findViewById(R.id.tv_one);
        TextView tvOnes = convertView.findViewById(R.id.tv_ones);
        TextView tvTwo= convertView.findViewById(R.id.tv_two);
        TextView tvTwos = convertView.findViewById(R.id.tv_twos);
        GlideUtils.getInstances().loadNormalImg(mContext,ivOne,bean.one.images);
        if(bean.two!=null&&bean.two.images!=null){
            GlideUtils.getInstances().loadNormalImg(mContext,ivTwo,bean.two.images);
            tvTwo.setText(bean.two.getTitle());
            tvTwos.setText(bean.two.desc);
            ll_bg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLiveVideoActivity(bean.two);
                }
            });
        }else {
            ll_bg1.setVisibility(View.INVISIBLE);
        }

        tvOne.setText(bean.one.getTitle());
        tvOnes.setText(bean.one.desc);


        ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveVideoActivity(bean.one);
            }
        });

    }

    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(mContext, LiveVideoViewActivity.class);

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
        mContext.startActivity(intent);
    }
}
