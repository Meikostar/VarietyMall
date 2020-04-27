package com.smg.variety.qiniu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.smg.variety.R;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.bean.LiveVideoInfo;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.RoomBean;
import com.smg.variety.bean.User;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StringUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.FinishEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.qiniu.adapter.DanmuAdapter;
import com.smg.variety.qiniu.adapter.DanmuEntity;
import com.smg.variety.qiniu.adapter.LiveVideoViewAdapter;
import com.smg.variety.qiniu.adapter.LiverAdapter;
import com.smg.variety.qiniu.chatroom.ChatroomKit;
import com.smg.variety.qiniu.chatroom.gift.GiftSendModel;
import com.smg.variety.qiniu.chatroom.gift.GiftView;
import com.smg.variety.qiniu.chatroom.message.ChatroomBarrage;
import com.smg.variety.qiniu.chatroom.message.ChatroomEnd;
import com.smg.variety.qiniu.chatroom.message.ChatroomGift;
import com.smg.variety.qiniu.chatroom.message.ChatroomUser;
import com.smg.variety.qiniu.chatroom.message.ChatroomUserQuit;
import com.smg.variety.qiniu.chatroom.message.ChatroomWelcome;
import com.smg.variety.qiniu.live.gles.FBO;
import com.smg.variety.qiniu.live.ui.CameraPreviewFrameView;
import com.smg.variety.qiniu.live.ui.InputPanel;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.activity.LiveVideoViewActivity;
import com.smg.variety.view.activity.OnlineLiveFinishActivity;
import com.smg.variety.view.activity.RoomUserListActivity;
import com.smg.variety.view.adapter.LiveProductItemAdapter;
import com.smg.variety.view.adapter.LiverProductAdapter;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.widgets.InputPwdDialog;
import com.smg.variety.view.widgets.MCheckBox;
import com.smg.variety.view.widgets.PhotoPopupWindow;
import com.smg.variety.view.widgets.dialog.BaseDialog;
import com.smg.variety.view.widgets.dialog.BeautyDialog;
import com.smg.variety.view.widgets.dialog.ConfirmDialog;
import com.smg.variety.view.widgets.dialog.ShareModeDialog;
import com.orzangleli.xdanmuku.DanmuContainerView;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;
import com.qiniu.pili.droid.streaming.av.common.PLFourCC;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

public class AVStreamingActivity extends StreamingBaseActivity implements StreamingPreviewCallback, CameraPreviewFrameView.Listener,
        SurfaceTextureCallback, Handler.Callback {
    //    @BindView(R.id.iv_mute)
    //    ImageView          ivMute;
    //    @BindView(R.id.iv_scan)
    //    ImageView          ivScan;
    //    @BindView(R.id.tv_name)
    //    TextView           tvName;
    //    @BindView(R.id.tv_count)
    //    TextView           tvCount;
    //    @BindView(R.id.tv_room_num)
    //    TextView           tvRoomNum;
    //    @BindView(R.id.iv_icon)
    //    ImageView        setData  ivIcon;
    //    @BindView(R.id.giftView)
    //    GiftView           giftView;
    //    @BindView(R.id.recyclerView)
    //    RecyclerView       recyclerView;
    //    @BindView(R.id.danmuContainerView)
    //    DanmuContainerView danmuContainerView;
    //    @BindView(R.id.input_panel)
    //    InputPanel         inputPanel;

    private static final String TAG = "AVStreamingActivity";
    @BindView(R.id.cameraPreview_surfaceView)
    CameraPreviewFrameView cameraPreviewSurfaceView;
    @BindView(R.id.iv_icon)
    ImageView              ivIcon;
    @BindView(R.id.tv_name)
    TextView               tvName;
    @BindView(R.id.ll_bg)
    LinearLayout               ll_bg;
    @BindView(R.id.ll_ax)
    LinearLayout               ll_ax;
    @BindView(R.id.rl_bottom)
    LinearLayout               rl_bottom;

    @BindView(R.id.tv_count)
    TextView               tvCount;
    @BindView(R.id.rl_author_info)
    RelativeLayout         rlAuthorInfo;
    @BindView(R.id.iv_title_back)
    ImageView              ivTitleBack;
    @BindView(R.id.tv_room_num)
    TextView               tvRoomNum;
    @BindView(R.id.danmuContainerView)
    DanmuContainerView     danmuContainerView;
    @BindView(R.id.giftView)
    GiftView               giftView;
    @BindView(R.id.input_editor)
    TextView               inputEditor;
    @BindView(R.id.tv_ren)
    TextView               tv_ren;

    @BindView(R.id.iv_scan)
    ImageView              ivScan;
    @BindView(R.id.iv_beauty)
    ImageView              ivBeauty;
    @BindView(R.id.iv_mute)
    ImageView              ivMute;
    @BindView(R.id.iv_user_list)
    ImageView              ivUserList;
    @BindView(R.id.iv_share)
    ImageView              ivShare;
    @BindView(R.id.iv_shop)
    ImageView              iv_shop;
    @BindView(R.id.iv_tanmu)
    ImageView              ivTanmu;
    @BindView(R.id.recyclerView)
    RecyclerView           recyclerView;
    @BindView(R.id.recycler_shop)
    RecyclerView           recyclerShop;
    @BindView(R.id.input_panel)
    InputPanel             inputPanel;
    @BindView(R.id.content)
    RelativeLayout         content;
    @BindView(R.id.view)
    View         mView;
    @BindView(R.id.tv_sx)
    TextView         tv_sx;

    private LiveVideoViewAdapter   mAdapter;
    private CameraStreamingSetting mCameraStreamingSetting;
    private boolean                mIsNeedMute         = false;
    private int                    mCurrentZoom        = 0;
    private int                    mMaxZoom            = 0;
    private boolean                mOrientationChanged = false;
    private int                    mCurrentCamFacingIndex;
    private FBO                    mFBO                = new FBO();
    private Switcher               mSwitcher           = new Switcher();
    private ImageSwitcher          mImageSwitcher;
    private MediaStreamingManager  mMediaStreamingManager;
    private Handler                mHandler;
    private int                    mTimes              = 0;
    private boolean                mIsPictureStreaming = false;
    RoomBean roomBean;
    String   currentRoomId;
    String   id;
    private long  chatterTotal;
    private Handler handler = new Handler(this);

    @Override
    public int getLayoutId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return R.layout.activity_av_streaming;
    }

    @Override
    public void initView() {
        initRecyclerView();
        mCameraStreamingSetting = buildCameraStreamingSetting();
        mCurrentCamFacingIndex = 1;
        giftView.setViewCount(2);
        giftView.init();
        recyclerShop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveVideoViewAdapter();
        mProductAdapter = new LiverProductAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerShop.setAdapter(mProductAdapter);
        mProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {

                    case R.id.tv_del:
                        NewListItemDto item = (NewListItemDto) adapter.getItem(position);
                        ids=item.getId();
                        if(item.is_liver_product){
                            type=1;
                        }else {
                            type=0;

                        }

                        UpdateliveVideo();

                }
            }
        });
    }

    private LiverProductAdapter mProductAdapter;

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        Serializable serializable = getIntent().getSerializableExtra("room");
        if (serializable != null) {
            roomBean = (RoomBean) serializable;
            currentRoomId = roomBean.getId();
        }
        ChatroomKit.addEventHandler(handler);
        ChatroomKit.setCurrentUser(new UserInfo(Constants.USER_ID, Constants.USER_NAME, null));
        joinChatRoom();
        liveVideosInfo();

        GlideUtils.getInstances().loadRoundImg(AVStreamingActivity.this, ivIcon, Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().get(Constants.USER_HEAD), R.drawable.moren_ren);
        tvName.setText(ShareUtil.getInstance().get(Constants.USER_NAME));
        countDownTimer.start();
        countDownTimers.start();
        danmuContainerView.setAdapter(new DanmuAdapter(this));
    }

    @Override
    public void initListener() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_bg.getVisibility() == View.VISIBLE) {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_hide));
                    ll_bg.setVisibility(View.GONE);
                    ll_ax.setVisibility(View.VISIBLE);
                    rl_bottom.setVisibility(View.VISIBLE);
                }
            }
        });
        inputPanel.setPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text, int type) {
                if (type == InputPanel.TYPE_TEXTMESSAGE) {
                    sendTextMessage(text);
                } else if (type == InputPanel.TYPE_BARRAGE) {
                    sendDanmuMessage(text);
                }

            }
        });
    }
  private    CountDownTimer countDownTimer = new CountDownTimer(3600000*24, 5000) {
        @Override
        public void onTick(long millisUntilFinished) {
           liveVideosInfo();
        }

        @Override
        public void onFinish() {

                if (countDownTimer != null) {
                    countDownTimer.start();
                }

        }
    };

    private    CountDownTimer countDownTimers = new CountDownTimer(3600000*24, 1000*30) {
        @Override
        public void onTick(long millisUntilFinished) {
            long looklive = ShareUtil.getInstance().getLong("looklive", 0);
            if(looklive>=1800){
                putLookLive();
                ShareUtil.getInstance().saveLong("looklive",0);
            }else {
                ShareUtil.getInstance().saveLong("looklive",looklive+30);
            }

        }

        @Override
        public void onFinish() {

            if (countDownTimers != null) {
                countDownTimers.start();
            }

        }
    };

    public void putLookLive() {

        DataManager.getInstance().putLookLive(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, "task_daily_live");
    }

    private void joinChatRoom() {
        RongIMClient.ConnectionStatusListener.ConnectionStatus currentConnectionStatus = RongIMClient.getInstance().getCurrentConnectionStatus();

        if (currentConnectionStatus == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
            if (roomBean != null) {
                ChatroomKit.joinChatRoom(roomBean.getId(), 50, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        ChatroomWelcome welcomeMessage = new ChatroomWelcome();
                        welcomeMessage.setId(getUserId());
                        welcomeMessage.setName(getUserName());
                        welcomeMessage.setUrl(getUserUrl());
                        ChatroomKit.sendMessage(welcomeMessage);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        ToastUtil.showToast(errorCode.getMessage().toString());
                    }
                });
            }
        }
    }

    /**
     * 弹幕
     */
    private void sendDanmuMessage(String text) {
        ChatroomBarrage barrage = new ChatroomBarrage();
        barrage.setContent(text);
        barrage.setName(getUserUrl());
        barrage.setUrl(getUserName());
        barrage.setType(0);
        ChatroomKit.sendMessage(barrage);
    }

    /**
     * 结束直播
     */

    private int state;

    private void sendEndLive() {
        ChatroomEnd chatroomEnd = new ChatroomEnd();
        //        chatroomEnd.setDuration(60);
        chatroomEnd.setExtra("附加信息");
        chatroomEnd.setUrl(getUserUrl());
        chatroomEnd.setName(getUserName());
        ChatroomKit.sendMessage(chatroomEnd);
        state = 1;
    }

    @OnClick({R.id.iv_title_back, R.id.iv_scan, R.id.iv_mute, R.id.iv_beauty, R.id.iv_user_list,
            R.id.input_editor, R.id.iv_share, R.id.iv_shop, R.id.iv_tanmu, R.id.content,R.id.tv_sx})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                //结束直播
                ConfirmDialog dialogs = new ConfirmDialog(AVStreamingActivity.this);
                dialogs.setTitle("温馨提示");
                dialogs.setMessage("确定要退出直播？");
                dialogs.setCancelable(false);
                dialogs.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {

                    @Override
                    public void onYesClick() {
                        sendEndLive();
                        //                        sendTextMessage("#@@直播已结束@@#");
                        //结束直播

                        dialogs.dismiss();
                    }
                });
                dialogs.setCancleClickListener("继续直播", new BaseDialog.OnCloseClickListener() {
                    @Override
                    public void onCloseClick() {
                        dialogs.dismiss();
                    }
                });
                dialogs.show();
                //                break;
                //结束直播
                //                liveVideosClose();
                break;
            case R.id.iv_scan:
                //切换摄像头
                if (isPictureStreaming()) {
                    return;
                }
                ivScan.removeCallbacks(mSwitcher);
                ivScan.postDelayed(mSwitcher, 100);
                break;
            case R.id.iv_shop:
                if (mProductAdapter.getItemCount()==0) {
                    ToastUtil.showToast("暂无商品");
                    return;
                }
                if (ll_bg.getVisibility() == View.VISIBLE) {
                    ll_ax.setVisibility(View.GONE);
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_hide));
                    ll_bg.setVisibility(View.GONE);
                } else {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_show));
                    ll_bg.setVisibility(View.VISIBLE);
                    ll_ax.setVisibility(View.GONE);
                    rl_bottom.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_mute:
                //静音
                mIsNeedMute = !mIsNeedMute;
                mMediaStreamingManager.mute(mIsNeedMute);
                if (mIsNeedMute) {
                    ivMute.setImageResource(R.mipmap.unmai);
                } else {
                    ivMute.setImageResource(R.mipmap.mai);
                }
                break;
            case R.id.tv_sx:
                //静音
           getProductListDatas();
                break;

            case R.id.iv_beauty:
                //美白
                if (dialog == null) {
                    dialog = new BeautyDialog(AVStreamingActivity.this, new BeautyDialog.SeekBarChangeListener() {

                        @Override
                        public void onSeekBarChange(int progress) {
                            CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                            fbSetting.whiten = progress / 100.0f;
                            mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                        }

                        @Override
                        public void onMoPiSeekBarChange(int progress) {
                            //磨皮
                            CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                            fbSetting.beautyLevel = progress / 100.0f;
                            mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                        }
                    });
                } else {
                    dialog.show();
                }


                break;
            case R.id.iv_user_list:
                //房间成员列表
                Intent intent = new Intent(AVStreamingActivity.this, RoomUserListActivity.class);
                intent.putExtra("room_id", roomBean.getId());
                startActivity(intent);
                break;
            case R.id.iv_share:
                //分享
                ShareModeDialog dialog1 = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                    @Override
                    public void sureItem(int position) {
                        boolean isTimelineCb = false;
                        //http://bbsc.885505.com/api/package/user/invitation_img?user_id=14
                        String url = Constants.BASE_URL + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, "");
                        String title = "我的推广码";
                        if (position == ShareModeDialog.SHARE_PYQ) {
                            isTimelineCb = true;
                        }
                        ShareUtil.sendToWeaChat(AVStreamingActivity.this, isTimelineCb, title, url);
                    }
                });
                dialog1.show();
                break;
            case R.id.input_editor:
                //说点什么吧
                inputPanel.setVisibility(View.VISIBLE);
                inputPanel.requestTextFocus();
                inputPanel.setType(InputPanel.TYPE_TEXTMESSAGE);
                break;
            case R.id.iv_tanmu:
                //弹幕
//                inputPanel.setVisibility(View.VISIBLE);
//                inputPanel.requestTextFocus();
//                inputPanel.setType(InputPanel.TYPE_BARRAGE);
                break;
            case R.id.content:
                inputPanel.setVisibility(View.GONE);
                inputPanel.hideKeyboard();
                break;
        }
    }

    private BeautyDialog dialog;

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        normalPause();
    }

    private void normalPause() {
        mIsReady = false;
        mShutterButtonPressed = false;
        mIsPictureStreaming = false;
        if (mHandler != null) {
            mHandler.getLooper().quit();
        }
        mMediaStreamingManager.pause();
    }

    @Override
    public void onBackPressed() {
        //结束直播
        ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("确定要退出直播？");
        dialog.setCancelable(false);
        dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {

            @Override
            public void onYesClick() {

                //                sendTextMessage("#@@!直播已结束!@@#");
                sendEndLive();
                //结束直播

                dialog.dismiss();
            }
        });
        dialog.setCancleClickListener("继续直播", new BaseDialog.OnCloseClickListener() {
            @Override
            public void onCloseClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
        ChatroomKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                ChatroomKit.removeEventHandler(handler);
                //                sendEndLive();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ChatroomKit.removeEventHandler(handler);
                Log.i(TAG, "errorCode = " + errorCode);
            }
        });
        countDownTimer.cancel();
        countDownTimers.cancel();
    }

    @Override
    protected void initStreamingManager() {
        CameraPreviewFrameView cameraPreviewFrameView = (CameraPreviewFrameView) findViewById(R.id.cameraPreview_surfaceView);
        mMediaStreamingManager = new MediaStreamingManager(this, cameraPreviewFrameView, AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC);
        mProfile.setPictureStreamingResourceId(R.drawable.pause_publish);

        MicrophoneStreamingSetting microphoneStreamingSetting = null;
        if (mAudioStereoEnable) {
            /**
             * Notice !!! {@link AudioFormat#CHANNEL_IN_STEREO} is NOT guaranteed to work on all devices.
             */
            microphoneStreamingSetting = new MicrophoneStreamingSetting();
            microphoneStreamingSetting.setChannelConfig(AudioFormat.CHANNEL_IN_STEREO);
        }
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mProfile);
        mMediaStreamingManager.setAutoRefreshOverlay(true);
        mMediaStreamingManager.setSurfaceTextureCallback(this);
        cameraPreviewFrameView.setListener(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
        mMediaStreamingManager.setStreamStatusCallback(this);
        mMediaStreamingManager.setAudioSourceCallback(this);
        mMediaStreamingManager.setStreamingStateListener(this);
        int mp = ShareUtil.getInstance().getInt("live_mp", 0);
        int mb = ShareUtil.getInstance().getInt("live_mb", 0);

        if (mp != 0) {
            CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
            fbSetting.whiten = mb / 100.0f;
            fbSetting.beautyLevel = mp / 100.0f;
            mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
        }

    }

    @Override
    protected boolean startStreaming() {
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    protected boolean stopStreaming() {
        return mMediaStreamingManager.stopStreaming();
    }


    //    private class EncodingOrientationSwitcher implements Runnable {
    //        @Override
    //        public void run() {
    //            Log.i(TAG, "mIsEncOrientationPort:" + mIsEncOrientationPort);
    //            mOrientationChanged = true;
    //            mIsEncOrientationPort = !mIsEncOrientationPort;
    //            mProfile.setEncodingOrientation(mIsEncOrientationPort ? StreamingProfile.ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
    //            mMediaStreamingManager.setStreamingProfile(mProfile);
    //            stopStreamingInternal();
    //            setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    //            mMediaStreamingManager.notifyActivityOrientationChanged();
    ////            updateOrientationBtnText();
    //            Toast.makeText(AVStreamingActivity.this, Config.HINT_ENCODING_ORIENTATION_CHANGED,
    //                    Toast.LENGTH_SHORT).show();
    //            Log.i(TAG, "EncodingOrientationSwitcher -");
    //        }
    //    }

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();
            CameraStreamingSetting.CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);

        }
    }


    /**
     * switch picture during streaming
     */
    private class ImageSwitcher implements Runnable {
        @Override
        public void run() {
            if (!mIsPictureStreaming) {
                Log.d(TAG, "is not picture streaming!!!");
                return;
            }

            if (mTimes % 2 == 0) {
                mMediaStreamingManager.setPictureStreamingResourceId(R.mipmap.ic_launcher);
            } else {
                mMediaStreamingManager.setPictureStreamingResourceId(R.drawable.pause_publish);
            }
            mTimes++;
            if (mHandler != null && mIsPictureStreaming) {
                mHandler.postDelayed(this, 1000);
            }
        }
    }

    private boolean isPictureStreaming() {
        if (mIsPictureStreaming) {
            Toast.makeText(AVStreamingActivity.this, "is picture streaming, operation failed!", Toast.LENGTH_SHORT).show();
        }
        return mIsPictureStreaming;
    }

    private void togglePictureStreaming() {
        boolean isOK = mMediaStreamingManager.togglePictureStreaming();
        if (!isOK) {
            Toast.makeText(AVStreamingActivity.this, "toggle picture streaming failed!", Toast.LENGTH_SHORT).show();
            return;
        }

        mIsPictureStreaming = !mIsPictureStreaming;

        mTimes = 0;
        if (mIsPictureStreaming) {
            if (mImageSwitcher == null) {
                mImageSwitcher = new ImageSwitcher();
            }

            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
            mHandler.postDelayed(mImageSwitcher, 1000);
        } else {
            if (mHandler != null) {
                mHandler.getLooper().quit();
            }
        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null)
                    bos.close();
            }

            final String info = "Save frame to:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AVStreamingActivity.this, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private CameraStreamingSetting buildCameraStreamingSetting() {

        CameraStreamingSetting cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setContinuousFocusModeEnabled(true)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

        return cameraStreamingSetting;
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        /**
         * You should choose a suitable size to avoid image scale
         * eg: If streaming size is 1280 x 720, you should choose a camera preview size >= 1280 x 720
         */
        Camera.Size size = null;
        if (list != null) {
            StreamingProfile.VideoEncodingSize encodingSize = mProfile.getVideoEncodingSize(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9);
            for (Camera.Size s : list) {
                if (s.width >= encodingSize.width && s.height >= encodingSize.height) {
                    size = s;
                    Log.d(TAG, "selected size :" + size.width + "x" + size.height);
                    break;
                }
            }
        }
        return size;
    }


    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        /**
         * general states are handled in the `StreamingBaseActivity`
         */
        super.onStateChanged(streamingState, extra);
        switch (streamingState) {
            case READY:
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
                break;
            case SHUTDOWN:
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreamingInternal();
                }
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case CAMERA_SWITCHED:
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");
                final int currentCamId = (Integer) extra;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //                        updateCameraSwitcherButtonText(currentCamId);
                    }
                });
                break;
            case TORCH_INFO:
                if (extra != null) {
                    final boolean isSupportedTorch = (Boolean) extra;
                    Log.i(TAG, "isSupportedTorch=" + isSupportedTorch);
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //                            if (isSupportedTorch) {
                            //                                mTorchBtn.setVisibility(View.VISIBLE);
                            //                            } else {
                            //                                mTorchBtn.setVisibility(View.GONE);
                            //                            }
                        }
                    });
                }
                break;
        }
    }

    protected void setFocusAreaIndicator() {
        //        if (mRotateLayout == null) {
        //            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
        //            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
        //                    mRotateLayout.findViewById(R.id.focus_indicator));
        //        }
    }

    private void setTorchEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                String flashlight = enabled ? getString(R.string.flash_light_off) : getString(R.string.flash_light_on);
                //                mTorchBtn.setText(flashlight);
            }
        });
    }

    //    private void updateOrientationBtnText() {
    //        if (mIsEncOrientationPort) {
    //            mEncodingOrientationSwitcherBtn.setText("Land");
    //        } else {
    //            mEncodingOrientationSwitcherBtn.setText("Port");
    //        }
    //    }
    //
    //    private void updateFBButtonText() {
    //        if (mFaceBeautyBtn != null) {
    //            mFaceBeautyBtn.setText(mIsNeedFB ? "FB Off" : "FB On");
    //        }
    //    }
    //
    //    private void updateMuteButtonText() {
    //        if (mMuteButton != null) {
    //            mMuteButton.setText(mIsNeedMute ? "Unmute" : "Mute");
    //        }
    //    }
    //
    //    private void updateCameraSwitcherButtonText(int camId) {
    //        if (mCameraSwitchBtn == null) {
    //            return;
    //        }
    //        if (camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
    //            mCameraSwitchBtn.setText("Back");
    //        } else {
    //            mCameraSwitchBtn.setText("Front");
    //        }
    //    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());
        if (mIsReady) {
            setFocusAreaIndicator();
            mMediaStreamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        if (mIsReady && mMediaStreamingManager.isZoomSupported()) {
            mCurrentZoom = (int) (mMaxZoom * factor);
            mCurrentZoom = Math.min(mCurrentZoom, mMaxZoom);
            mCurrentZoom = Math.max(0, mCurrentZoom);
            Log.d(TAG, "zoom ongoing, scale: " + mCurrentZoom + ",factor:" + factor + ",maxZoom:" + mMaxZoom);
            mMediaStreamingManager.setZoomValue(mCurrentZoom);
        }
        return false;
    }

    @Override
    public void onSurfaceCreated() {
        Log.i(TAG, "onSurfaceCreated");
        /**
         * only used in custom beauty algorithm case
         */
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
        /**
         * only used in custom beauty algorithm case
         */
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        Log.i(TAG, "onSurfaceDestroyed");
        /**
         * only used in custom beauty algorithm case
         */
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        /**
         * When using custom beauty algorithm, you should return a new texId from the SurfaceTexture.
         * newTexId should not equal with texId, Otherwise, there is no filter effect.
         */
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
        return newTexId;
    }

    @Override
    public boolean onPreviewFrame(byte[] bytes, int width, int height, int rotation, int fmt, long tsInNanoTime) {
        Log.i(TAG, "onPreviewFrame " + width + "x" + height + ",fmt:" + (fmt == PLFourCC.FOURCC_I420 ? "I420" : "NV21") + ",ts:" + tsInNanoTime + ",rotation:" + rotation);
        /**
         * When using custom beauty algorithm in sw encode mode, you should change the bytes array's values here
         * eg: byte[] beauties = readPixelsFromGPU();
         * System.arraycopy(beauties, 0, bytes, 0, bytes.length);
         */
        return true;
    }

    private Map<String, String> maps = new HashMap<>();

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosInfo() {
        DataManager.getInstance().liveVideosInfo(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
                if (result != null && result.getData() != null) {

//                    tvCount.setText(chatterTotal + "人");
                    if (result.getData().getRoom() != null && result.getData().getRoom().getData() != null) {
                        tvRoomNum.setText("房间号:" + result.getData().getRoom().getData().getRoom_name());
                    }
                    if (result.getData().videoproducts != null && result.getData().videoproducts.data != null) {
                        for (MyOrderItemDto dto : result.getData().videoproducts.data) {
                            maps.put(dto.getId(), dto.getId());
                        }
                    }
                    tv_ren.setText(result.getData().click_like_count+"" );
                    getProductListData();
                }


            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, id);
    }

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosClose() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        DataManager.getInstance().liveVideosClose(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                Intent intent = new Intent(AVStreamingActivity.this, OnlineLiveFinishActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, map);
    }



    private int    type;
    private String ids;

    private void UpdateliveVideo() {

        Map<String, String> mParamsMaps = new HashMap<>();
        mParamsMaps.put("id", id);
        if (type == 0) {
            mParamsMaps.put("add_product_ids", ids);
        } else {
            mParamsMaps.put("del_product_ids", ids);
        }

        DataManager.getInstance().UpdateliveVideo(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> data) {
                if (type == 0) {
                    ToastUtil.showToast("添加成功");
                } else {
                    ToastUtil.showToast("移除成功");
                    maps.remove(ids);
                }
                liveVideosInfo();



            }

            @Override
            public void onError(Throwable throwable) {

                if (type == 0) {
                    ToastUtil.showToast("添加成功");
                } else {
                    ToastUtil.showToast("移除成功");
                    maps.remove(ids);
                }
                liveVideosInfo();
            }
        }, mParamsMaps);
    }

    private void getProductListData() {

        Map<String, String> mParamsMaps = new HashMap<>();
        mParamsMaps.put("rows", "100");
        mParamsMaps.put("page", "1");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category");
        mParamsMaps.put("filter[is_live]", "1");


        DataManager.getInstance().findGoodsLives(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> data) {
                dissLoadDialog();
                if (data != null && data.getData() != null && data.getData().size() > 0) {
                    List<NewListItemDto> data1 = data.getData();
                    for(NewListItemDto dto:data1){
                        String s = maps.get(dto.getId());
                        if(TextUtil.isEmpty(s)){
                            dto.is_liver_product=false;
                        }else {
                            dto.is_liver_product=true;
                        }
                    }
                    mProductAdapter.setNewData(data.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        });
    }
    private void getProductListDatas() {

        Map<String, String> mParamsMaps = new HashMap<>();
        mParamsMaps.put("rows", "100");
        mParamsMaps.put("page", "1");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category");
        mParamsMaps.put("filter[is_live]", "1");


        DataManager.getInstance().findGoodsLives(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> data) {
                dissLoadDialog();
                if (data != null && data.getData() != null && data.getData().size() > 0) {
                    List<NewListItemDto> data1 = data.getData();
                    for(NewListItemDto dto:data1){
                        String s = maps.get(dto.getId());
                        if(TextUtil.isEmpty(s)){
                            dto.is_liver_product=false;
                        }else {
                            dto.is_liver_product=true;
                        }
                    }
                    mProductAdapter.setNewData(data.getData());
                    ToastUtil.showToast("刷新成功");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        });
    }
    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 30001:
                joinChatRoom();
                break;
            case ChatroomKit.MESSAGE_ARRIVED:
            case ChatroomKit.MESSAGE_SENT: {
                if (state == 1) {
                    state = 0;
                    liveVideosClose();
                } else {
                    MessageContent messageContent = ((Message) msg.obj).getContent();
                    setData(messageContent);
                }

                break;
            }

        }
        return false;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveVideoViewAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 发送文本内容
     */
    private void sendTextMessage(String text) {
        if (!TextUtils.isEmpty(text)) {
            final TextMessage content = TextMessage.obtain(text);
            content.setUserInfo(getUserInfo());
            ChatroomKit.sendMessage(content);
        } else {
            ToastUtil.showToast("请输入要发送的内容");
        }

    }

    private UserInfo getUserInfo() {
        Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null));
        String userId = ShareUtil.getInstance().getString(Constants.USER_ID, "");
        String nickName = ShareUtil.getInstance().getString(Constants.USER_NAME, "");
        return new UserInfo(userId, nickName, RongHeadImg);
    }

    private void showGif(ChatroomGift chatroomGift) {
        GiftSendModel model = new GiftSendModel(1);

        switch (chatroomGift.getG_id()) {
            case "1":
                model.setGiftRes(R.mipmap.icon_gift_huoguo);
                break;
            case "2":
                model.setGiftRes(R.mipmap.icon_gift_dalibao);
                break;
            case "3":
                model.setGiftRes(R.mipmap.icon_gift_dangao);
                break;
            case "4":
                model.setGiftRes(R.mipmap.icon_gift_xianhua);
                break;
            case "5":
                model.setGiftRes(R.mipmap.icon_gift_baoshi);
                break;
            case "6":
                model.setGiftRes(R.mipmap.icon_gift_huangguan);
                break;
            case "7":
                model.setGiftRes(R.mipmap.icon_gift_liushengji);
                break;
            case "8":
                model.setGiftRes(R.mipmap.icon_gift_aixin);
                break;
            default:
                model.setGiftRes(R.mipmap.gift_ring);
                break;

        }

        //        if (chatroomGift.getUserInfo() != null) {
        //            model.setNickname(chatroomGift.getUserInfo().getName());
        //            model.setSig("送出礼物");
        //            model.setUserAvatarRes(chatroomGift.getUserInfo().getPortraitUri().toString());
        //        }
        model.setUserAvatarRes(chatroomGift.getUrl());
        model.setNickname(chatroomGift.getName());
        model.setSig(chatroomGift.getGift_name());
        if(chatroomGift.getG_id().equals("ds")){
            if(!TextUtil.isEmpty(chatroomGift.getGift_name())){
                model.money=Integer.valueOf(chatroomGift.getGift_name());
            }

        }
        giftView.addGift(model);
    }

    /**
     * 接收消息,更新适配器
     */
    private void setData(MessageContent messageContent) {
        if (messageContent instanceof ChatroomBarrage) {
            ChatroomBarrage barrage = (ChatroomBarrage) messageContent;
            DanmuEntity danmuEntity = new DanmuEntity();
            danmuEntity.setContent(barrage.getContent());
            danmuEntity.setUrl(barrage.getName());
            danmuEntity.setName(barrage.getUrl());
            danmuEntity.setType(barrage.getType());
            danmuContainerView.addDanmu(danmuEntity);
        } else if (messageContent instanceof ChatroomUser) {
            //房间人数
            ChatroomUser chatroomUser = (ChatroomUser) messageContent;
            tvCount.setText(chatroomUser.getExtra() + "人");
        } else {
            if (messageContent instanceof ChatroomGift && ((ChatroomGift) messageContent).getType() == 0) {
                //收到礼物
                showGif(((ChatroomGift) messageContent));
            }else if (messageContent instanceof ChatroomWelcome) {
                chatterTotal=chatterTotal+1;
                tvCount.setText(chatterTotal + "人");
            } else if (messageContent instanceof ChatroomUserQuit) {
                chatterTotal=chatterTotal-1;
                tvCount.setText((chatterTotal<=0?0:chatterTotal)+ "人");
            }
            mAdapter.addData(messageContent);
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    private String getUserId() {
        return ShareUtil.getInstance().getString(Constants.USER_ID, "");
    }

    private String getUserName() {
        return ShareUtil.getInstance().getString(Constants.USER_NAME, "");
    }

    private String getUserUrl() {
        return Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null);
    }
}
