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
import com.smg.variety.bean.AttentionCommunityBean;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.LiveVideoViewActivity;
import com.smg.variety.view.widgets.CircleImageView;

import java.util.List;

public class AttentionLiveRoomAdapter extends BaseQuickAdapter<VideoLiveBean, BaseViewHolder> {
    public AttentionLiveRoomAdapter(Context context) {
        super(R.layout.item_live_view, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoLiveBean item) {
        ImageView iv_one = helper.getView(R.id.iv_one);
        ImageView iv_img = helper.getView(R.id.iv_img);
        ImageView view = helper.getView(R.id.iv_two);
        ImageView view1 = helper.getView(R.id.iv_three);
        TextView text1 = helper.getView(R.id.tv_price);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView text2 = helper.getView(R.id.tv_prices);
        TextView tv_title = helper.getView(R.id.tv_title);
        TextView tv_see = helper.getView(R.id.tv_see);
        TextView tv_care = helper.getView(R.id.tv_care);
        view.setVisibility(View.INVISIBLE);
        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);
        view1.setVisibility(View.INVISIBLE);
        tv_title.setText(item.getTitle());
        tv_see.setText(item.chatter_total+"观看");
        tv_care.setText(item.click_like_count+"");
        GlideUtils.getInstances().loadNormalImg(mContext,iv_one,item.images);
        GlideUtils.getInstances().loadNormalImg(mContext,iv_img,item.getUser().getData().getAvatar(),R.drawable.moren_ren);
        tv_name.setText(item.getUser().getData().getName());
        if(item.videoproducts!=null&&item.videoproducts.data!=null){
            List<MyOrderItemDto> dats=   item.videoproducts.data;
            if(item.videoproducts.data.size()>=2){
                view.setVisibility(View.VISIBLE);
                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                GlideUtils.getInstances().loadNormalImg(mContext,view,dats.get(0).getCover());
                GlideUtils.getInstances().loadNormalImg(mContext,view1,dats.get(1).getCover());
                text1.setText("¥" + dats.get(0).getPrice());
                text2.setText("¥" + dats.get(1).getPrice());
            }else if(item.videoproducts.data.size()==1){
                view.setVisibility(View.VISIBLE);
                text1.setVisibility(View.VISIBLE);
                GlideUtils.getInstances().loadNormalImg(mContext,view,dats.get(0).getCover());
                text1.setText("¥" + dats.get(0).getPrice());

            }
        }
        helper.getView(R.id.iv_item_home_push).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveVideoActivity(item);
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
    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
