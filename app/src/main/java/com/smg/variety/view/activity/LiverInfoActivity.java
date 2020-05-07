package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.widgets.CircleImageView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LiverInfoActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView       ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView        tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView        tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout  layoutTop;
    @BindView(R.id.civ_user_avatar)
    ImageView mUserAvatar;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_cout)
    TextView        tvCout;
    @BindView(R.id.rl_user_avatar)
    RelativeLayout  rlUserAvatar;
    @BindView(R.id.ll_more)
    LinearLayout    llMore;
    @BindView(R.id.iv_img1)
    ImageView       ivImg1;
    @BindView(R.id.fl_bg1)
    FrameLayout     flBg1;
    @BindView(R.id.iv_img2)
    ImageView       ivImg2;
    @BindView(R.id.fl_bg2)
    FrameLayout     flBg2;
    @BindView(R.id.iv_img3)
    ImageView       ivImg3;
    @BindView(R.id.fl_bg3)
    FrameLayout     flBg3;
    @BindView(R.id.tv_user_name)
    TextView        tvUserName;
    @BindView(R.id.rl_lw)
    RelativeLayout  rlLw;
    @BindView(R.id.tv_user_phone)
    TextView        tvUserPhone;
    @BindView(R.id.rl_ds)
    RelativeLayout  rlDs;
    @BindView(R.id.rl_yj)
    RelativeLayout  rlYj;
    @BindView(R.id.tv_sex)
    TextView        tvSex;
    @BindView(R.id.rl_product)
    RelativeLayout  rlProduct;
    @BindView(R.id.ll_bgs)
    LinearLayout  ll_bgs;
    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_liver_info;
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {

    }
    private void liveVideos() {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

        map.put("liver_user_id", ShareUtil.getInstance().get(Constants.USER_ID));

        map.put("page", 1 + "");
        map.put("live_type", "2");
        map.put("include", "apply,room,user,videoproducts");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();

               if(result!=null&&result.getData()!=null){
                   if(result.getData().size()==1){
                       flBg1.setVisibility(View.VISIBLE);
                       ll_bgs.setVisibility(View.VISIBLE);
                       flBg1.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               startLiveVideoActivity(result.getData().get(0));
                           }
                       });
                       GlideUtils.getInstances().loadNormalImg(LiverInfoActivity.this,ivImg1,result.getData().get(0).images);
                   }else if(result.getData().size()==2){
                       ll_bgs.setVisibility(View.VISIBLE);
                       flBg1.setVisibility(View.VISIBLE);
                       GlideUtils.getInstances().loadNormalImg(LiverInfoActivity.this,ivImg1,result.getData().get(0).images);
                       flBg2.setVisibility(View.VISIBLE);
                       GlideUtils.getInstances().loadNormalImg(LiverInfoActivity.this,ivImg2,result.getData().get(1).images);
                       flBg1.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               startLiveVideoActivity(result.getData().get(0));
                           }
                       });
                       flBg2.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               startLiveVideoActivity(result.getData().get(1));
                           }
                       });
                   }else if(result.getData().size()>=3){
                       ll_bgs.setVisibility(View.VISIBLE);
                       flBg1.setVisibility(View.VISIBLE);
                       GlideUtils.getInstances().loadNormalImg(LiverInfoActivity.this,ivImg1,result.getData().get(0).images);
                       flBg2.setVisibility(View.VISIBLE);
                       GlideUtils.getInstances().loadNormalImg(LiverInfoActivity.this,ivImg2,result.getData().get(1).images);
                       flBg3.setVisibility(View.VISIBLE);
                       GlideUtils.getInstances().loadNormalImg(LiverInfoActivity.this,ivImg3,result.getData().get(2).images);
                       flBg1.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               startLiveVideoActivity(result.getData().get(0));
                           }
                       });
                       flBg2.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               startLiveVideoActivity(result.getData().get(1));
                           }
                       });
                       flBg3.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               startLiveVideoActivity(result.getData().get(1));
                           }
                       });

                   }else {
                       ll_bgs.setVisibility(View.GONE);
                   }
               }else {
                   ll_bgs.setVisibility(View.GONE);
               }
            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();


            }
        }, map);
    }
    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {


                ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
                if (!TextUtils.isEmpty(personalInfoDto.getName())) {
                    tvName.setText(personalInfoDto.getName());

                } else {
                    tvName.setText(personalInfoDto.getPhone());
//                    ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());followingsCount
                }
                GlideUtils.getInstances().loadRoundImg(LiverInfoActivity.this, mUserAvatar, Constants.WEB_IMG_URL_UPLOADS + personalInfoDto.getAvatar(),R.drawable.moren_ren);
                tvCout.setText("粉丝数:"+personalInfoDto.followingsCount);

            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(LiverInfoActivity.this, LiveVideoViewActivity.class);

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
        startActivity(intent);
    }
    @OnClick({R.id.iv_title_back
            , R.id.ll_more
            , R.id.rl_lw
            , R.id.rl_ds
            , R.id.rl_yj
            , R.id.rl_product
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.ll_more:
                gotoActivity(MyLiversActivity.class);
                break;
            case R.id.rl_lw:
                gotoActivity(MyGitListActivity.class);
                break;
            case R.id.rl_ds:
                gotoActivity(MyTipsListActivity.class);

                break;
            case R.id.rl_yj:
                gotoActivity(MyYongjListActivity.class);
                break;
            case R.id.rl_product:
                gotoActivity(LiverProductsActivity.class);

                break;
        }

    }

    @Override
    protected void onResume() {
        getUserInfo();
        liveVideos();
        super.onResume();
    }



}
