package com.smg.variety.view.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.bean.RecommendListDto;
import com.smg.variety.bean.ScoreBean;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.view.adapter.RenWuAdapter;
import com.smg.variety.view.widgets.InputPwdDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
@SuppressLint("ResourceType")
public class AppQianDaoActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView    ivBack;
    @BindView(R.id.iv_qd)
    ImageView    ivQd;
    @BindView(R.id.iv_qd1)
    ImageView    ivQd1;
    @BindView(R.id.iv_img1)
    ImageView    ivImg1;
    @BindView(R.id.tv_b1)
    TextView     tvB1;
    @BindView(R.id.iv_img2)
    ImageView    ivImg2;
    @BindView(R.id.tv_b2)
    TextView     tvB2;
    @BindView(R.id.iv_img3)
    ImageView    ivImg3;
    @BindView(R.id.tv_b3)
    TextView     tvB3;
    @BindView(R.id.iv_img4)
    ImageView    ivImg4;
    @BindView(R.id.tv_b4)
    TextView     tvB4;
    @BindView(R.id.iv_img5)
    ImageView    ivImg5;
    @BindView(R.id.tv_b5)
    TextView     tvB5;
    @BindView(R.id.iv_img6)
    ImageView    ivImg6;
    @BindView(R.id.tv_b6)
    TextView     tvB6;
    @BindView(R.id.iv_img7)
    ImageView    ivImg7;
    @BindView(R.id.tv_b7)
    TextView     tvB7;
    @BindView(R.id.iv_img8)
    ImageView    ivImg8;
    @BindView(R.id.tv_b8)
    TextView     tvB8;
    @BindView(R.id.ll_bg1s)
    LinearLayout llBg1s;
    @BindView(R.id.ll_bg2s)
    LinearLayout llBg2s;
    @BindView(R.id.ll_bg3s)
    LinearLayout llBg3s;
    @BindView(R.id.ll_bg4s)
    LinearLayout llBg4s;
    @BindView(R.id.ll_bg5s)
    LinearLayout llBg5s;
    @BindView(R.id.ll_bg6s)
    LinearLayout llBg6s;
    @BindView(R.id.ll_bg7s)
    LinearLayout llBg7s;
    @BindView(R.id.ll_bg8s)
    LinearLayout llBg8s;
    @BindView(R.id.ll_bg1)
    LinearLayout llBg1;
    @BindView(R.id.ll_bg2)
    LinearLayout llBg2;
    @BindView(R.id.ll_bg3)
    LinearLayout llBg3;
    @BindView(R.id.ll_bg4)
    LinearLayout llBg4;
    @BindView(R.id.ll_bg5)
    LinearLayout llBg5;
    @BindView(R.id.ll_bg6)
    LinearLayout llBg6;
    @BindView(R.id.ll_bg7)
    LinearLayout llBg7;
    @BindView(R.id.ll_bg8)
    LinearLayout llBg8;
    private LinearLayout[] mLayout1s=new LinearLayout[8];
    private LinearLayout[] mLayout2s=new LinearLayout[8];
    @Override
    public int getLayoutId() {
        return R.layout.activity_qian_dao;
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {
        mLayout1s[0]=llBg1;
        mLayout1s[1]=llBg2;
        mLayout1s[2]=llBg3;
        mLayout1s[3]=llBg4;
        mLayout1s[4]=llBg5;
        mLayout1s[5]=llBg6;
        mLayout1s[6]=llBg7;
        mLayout1s[7]=llBg8;
        mLayout2s[0]=llBg1s;
        mLayout2s[1]=llBg2s;
        mLayout2s[2]=llBg3s;
        mLayout2s[3]=llBg4s;
        mLayout2s[4]=llBg5s;
        mLayout2s[5]=llBg6s;
        mLayout2s[6]=llBg7s;
        mLayout2s[7]=llBg8s;
        getCheckIn();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private Handler mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            putCheckIn();

            return false;
        }
    });
    private void putCheckIn() {

        DataManager.getInstance().putCheckIn(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                checkIn=true;
                DialogUtils.showQianDao(AppQianDaoActivity.this, (poistion+1)+"", "10", "10", (7-poistion)+"", new DialogUtils.OnClickDialogListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    checkIn=true;
                    DialogUtils.showQianDao(AppQianDaoActivity.this, (poistion+1)+"", "10", "10", (7-poistion)+"", new DialogUtils.OnClickDialogListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        });
    }
   private boolean checkIn;
   private int poistion;
    private void getCheckIn() {

        DataManager.getInstance().getCheckIn(new DefaultSingleObserver<HttpResult<ScoreBean>>() {
            @Override
            public void onSuccess(HttpResult<ScoreBean> result) {
               if(result!=null&&result.getData()!=null&&result.getData().days!=null){
                   checkIn=result.getData().today_check;
                   if(checkIn){
                       ivQd.setVisibility(View.INVISIBLE);
                   }else {
                       ivQd.setVisibility(View.VISIBLE);
                   }
                   for(int a=0;a<result.getData().days.size();a++){
                       if(!result.getData().days.get(a).checked){
                           poistion=a;
                           selectPoistion();
                           return;
                       }
                   }
               }
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        });
    }
     public void selectPoistion(){
        if(poistion==1){
            llBg1.setVisibility(View.INVISIBLE);
        }else  if(poistion==2){
            llBg1.setVisibility(View.INVISIBLE);
            llBg2.setVisibility(View.INVISIBLE);

        }else  if(poistion==3){
            llBg1.setVisibility(View.INVISIBLE);
            llBg2.setVisibility(View.INVISIBLE);
            llBg3.setVisibility(View.INVISIBLE);

        }else  if(poistion==4){
            llBg1.setVisibility(View.INVISIBLE);
            llBg2.setVisibility(View.INVISIBLE);
            llBg3.setVisibility(View.INVISIBLE);
            llBg4.setVisibility(View.INVISIBLE);

        }else  if(poistion==5){
            llBg1.setVisibility(View.INVISIBLE);
            llBg2.setVisibility(View.INVISIBLE);
            llBg3.setVisibility(View.INVISIBLE);
            llBg4.setVisibility(View.INVISIBLE);
            llBg5.setVisibility(View.INVISIBLE);

        }else  if(poistion==6){
            llBg1.setVisibility(View.INVISIBLE);
            llBg2.setVisibility(View.INVISIBLE);
            llBg3.setVisibility(View.INVISIBLE);
            llBg4.setVisibility(View.INVISIBLE);
            llBg5.setVisibility(View.INVISIBLE);
            llBg6.setVisibility(View.INVISIBLE);

        }else  if(poistion==7){
            llBg1.setVisibility(View.INVISIBLE);
            llBg2.setVisibility(View.INVISIBLE);
            llBg3.setVisibility(View.INVISIBLE);
            llBg4.setVisibility(View.INVISIBLE);
            llBg5.setVisibility(View.INVISIBLE);
            llBg6.setVisibility(View.INVISIBLE);
            llBg7.setVisibility(View.INVISIBLE);

        }

     }
    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivQd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkIn){
                    AnimatorSet inAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(AppQianDaoActivity.this, R.anim.rotate_in_anim);
                    AnimatorSet outAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(AppQianDaoActivity.this, R.anim.rotate_out_anim);
                    int distance = 16000;
                    float scale = getResources().getDisplayMetrics().density * distance;
                    ivQd.setCameraDistance(scale); //设置镜头距离
                    ivQd1.setCameraDistance(scale); //设置镜头距离
                    outAnimator.setTarget(ivQd);
                    inAnimator.setTarget(ivQd1);
                    outAnimator.start();
                    inAnimator.start();
                    changeQd(mLayout1s[poistion],mLayout2s[poistion]);
                    mHandler.sendEmptyMessageDelayed(1,100);
                    outAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                        }
                    });
                }else {
                    ToastUtil.showToast("今天已签到，明天再来吧！");
                }

                //                Animation animation = AnimationUtils.loadAnimation(AppQianDaoActivity.this, R.anim.rotate_in_anim);
                //                animation.setAnimationListener(new Animation.AnimationListener() {
                //                    @Override
                //                    public void onAnimationStart(Animation animation) {
                //
                //                    }
                //
                //                    @Override
                //                    public void onAnimationEnd(Animation animation) {
                //                        ivQd.setImageResource(R.drawable.qiand_2);
                //                    }
                //
                //                    @Override
                //                    public void onAnimationRepeat(Animation animation) {
                //
                //                    }
                //                });
                //                ivQd.startAnimation(animation);
            }
        });

    }


    private RenWuAdapter           mEntityStoreAdapter;
    private List<RecommendListDto> storeLists = new ArrayList<RecommendListDto>();


   public void changeQd(LinearLayout view1,LinearLayout view2){
       AnimatorSet inAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(AppQianDaoActivity.this, R.anim.rotate_in_anim);
       AnimatorSet outAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(AppQianDaoActivity.this, R.anim.rotate_out_anim);
       int distance = 16000;
       float scale = getResources().getDisplayMetrics().density * distance;
       view1.setCameraDistance(scale); //设置镜头距离
       view2.setCameraDistance(scale); //设置镜头距离
       outAnimator.setTarget(view1);
       inAnimator.setTarget(view2);
       outAnimator.start();
       inAnimator.start();
       outAnimator.addListener(new AnimatorListenerAdapter() {
           @Override
           public void onAnimationEnd(Animator animation) {
               super.onAnimationEnd(animation);
               view1.setVisibility(View.INVISIBLE);
           }
       });
   }
}
