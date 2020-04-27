package com.smg.variety.view.mainfragment.learn;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.smg.variety.R;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.LiveVideoViewActivity;

public class OnlineLiveItemAdapter extends BaseQuickAdapter<VideoLiveBean, BaseViewHolder> {
    public OnlineLiveItemAdapter() {
        super(R.layout.item_online_live, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoLiveBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), item.getImages());
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_chatter_total, item.getChatter_total()+"人观看");
        RelativeLayout rlbg=helper.getView(R.id.rl_bg);
        rlbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLiveVideoActivity(item);
            }
        });
    }


    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(mContext, LiveVideoViewActivity.class);

        if (videoLiveBean.getRoom() != null && videoLiveBean.getRoom().getData() != null) {
            intent.putExtra("roomId", videoLiveBean.getRoom().getData().getId());
        }
        if (videoLiveBean.apply != null && videoLiveBean.apply.getData() != null&&TextUtil.isEmpty(videoLiveBean.end_at)) {
            intent.putExtra("videoPath", videoLiveBean.apply.getData().rtmp_play_url);
        }else {
            if(TextUtil.isNotEmpty(videoLiveBean.end_at)&&TextUtil.isNotEmpty(videoLiveBean.play_url)){
                intent.putExtra("videoPath", "http://pili-vod.bbsc.2aa6.com/"+videoLiveBean.play_url);
            }
        }
        intent.putExtra("videoId", videoLiveBean.getId());
        intent.putExtra("liveStreaming", 1);
        mContext.startActivity(intent);
    }
}
