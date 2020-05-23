package com.smg.variety.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.bean.BaseType;
import com.smg.variety.bean.CheckOutOrderResult;
import com.smg.variety.bean.GiftBean;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.bean.WEIXINREQ;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.DensityUtil;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.PayUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.qiniu.AVStreamingActivity;
import com.smg.variety.qiniu.MediaController;
import com.smg.variety.qiniu.adapter.DanmuAdapter;
import com.smg.variety.qiniu.adapter.DanmuEntity;
import com.smg.variety.qiniu.adapter.LiveVideoViewAdapter;
import com.smg.variety.qiniu.chatroom.ChatroomKit;
import com.smg.variety.qiniu.chatroom.gift.GiftSendModel;
import com.smg.variety.qiniu.chatroom.gift.GiftView;
import com.smg.variety.qiniu.chatroom.message.ChatroomBarrage;
import com.smg.variety.qiniu.chatroom.message.ChatroomEnd;
import com.smg.variety.qiniu.chatroom.message.ChatroomGift;
import com.smg.variety.qiniu.chatroom.message.ChatroomJifen;
import com.smg.variety.qiniu.chatroom.message.ChatroomLike;
import com.smg.variety.qiniu.chatroom.message.ChatroomUser;
import com.smg.variety.qiniu.chatroom.message.ChatroomUserQuit;
import com.smg.variety.qiniu.chatroom.message.ChatroomWelcome;
import com.smg.variety.qiniu.chatroom.messageview.HeartLayout;
import com.smg.variety.qiniu.live.ui.InputPanel;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.SettingPasswordActivity;
import com.smg.variety.view.adapter.LiveProductItemAdapter;
import com.smg.variety.view.fragments.PayResultListener;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.mainfragment.consume.ConfirmOrderActivity;
import com.smg.variety.view.widgets.Custom_TagBtn;
import com.smg.variety.view.widgets.FlexboxLayout;
import com.smg.variety.view.widgets.InputPwdDialog;
import com.smg.variety.view.widgets.MCheckBox;
import com.smg.variety.view.widgets.PhotoPopupWindow;
import com.smg.variety.view.widgets.dialog.BaseDialog;
import com.smg.variety.view.widgets.dialog.ConfirmDialogs;
import com.smg.variety.view.widgets.dialog.GifListDialog;
import com.smg.variety.view.widgets.dialog.InputPasswordDialog;
import com.smg.variety.view.widgets.dialog.PointAmountDialog;
import com.smg.variety.view.widgets.dialog.ShareModeDialog;
import com.orzangleli.xdanmuku.DanmuContainerView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnAudioFrameListener;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoFrameListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * This is a demo activity of PLVideoView
 * 直播播放
 */
public class LiveVideoViewActivity extends BaseActivity implements Handler.Callback {
    public static final  String SDCARD_DIR        = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final  String DEFAULT_CACHE_DIR = SDCARD_DIR + "/PLDroidPlayer";
    private static final String TAG               = LiveVideoViewActivity.class.getSimpleName();
    @BindView(R.id.VideoView)
    PLVideoView mVideoView;
    @BindView(R.id.iv_icon)
    ImageView   iv_icon;
    @BindView(R.id.iv_point_amount)
    ImageView   iv_point_amount;
    @BindView(R.id.iv_tanmu)
    ImageView   iv_tanmu;

    @BindView(R.id.tv_name)
    TextView     tv_name;
    @BindView(R.id.tv_count)
    TextView     tv_count;
    @BindView(R.id.tv_ren)
    TextView     tv_ren;

    @BindView(R.id.tv_room_num)
    TextView     tv_room_num;
    @BindView(R.id.iv_mute)
    ImageView    ivMute;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_shop)
    RecyclerView recyclerShop;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;
    @BindView(R.id.iv_shop)
    ImageView    iv_shop;
    @BindView(R.id.iv_san)
    ImageView    iv_san;

    @BindView(R.id.view)
    View     mView;
    @BindView(R.id.tv_size)
    TextView tv_size;
    private LiveVideoViewAdapter   mAdapter;
    private LiveProductItemAdapter mProductAdapter;
    @BindView(R.id.giftView)
    GiftView           giftView;
    @BindView(R.id.iv_attention)
    ImageView          iv_attention;
    @BindView(R.id.heart_layout)
    HeartLayout        heartLayout;
    @BindView(R.id.input_panel)
    InputPanel         inputPanel;
    @BindView(R.id.rl_bottom)
    LinearLayout       rl_bottom;
    @BindView(R.id.ll_ax)
    LinearLayout       ll_ax;

    @BindView(R.id.danmuContainerView)
    DanmuContainerView danmuContainerView;

    private int     mDisplayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT;
    private boolean mIsLiveStreaming;
    //聊天室ID
    private String  roomId, videoId;
    private List<GiftBean>    giftBeans;
    private String            authorPhone;
    private String            authorId;
    private boolean           isMute  = false;//是否静音播放
    private GifListDialog     gifListDialog;
    private PointAmountDialog pointAmountDialog;
    private Handler           handler = new Handler(this);
    private boolean           isAttention;
    long currentTime = 0;
    int  clickCount  = 0;
    private       Random random = new Random();
    private       long chatterTotal;
    public static int    state  = 0;
    public static int    ste  = -1;

    @Override
    public int getLayoutId() {
        setKeepOn();
        return state == 0 ? R.layout.activity_alive_video_view : R.layout.activity_alive_video_views;
    }

    private String userId;

    @Override
    public void initView() {
        initRecyclerView();
        String videoPath = getIntent().getStringExtra("videoPath");
        userId = getIntent().getStringExtra("userId");
        mIsLiveStreaming = getIntent().getIntExtra("liveStreaming", 1) == 1;

        //设置全屏
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        View mCoverView = findViewById(R.id.CoverView);
        mVideoView.setCoverView(mCoverView);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming ? 1 : 0);
        boolean disableLog = getIntent().getBooleanExtra("disable-log", false);
        // options.setString(AVOptions.KEY_DNS_SERVER, "127.0.0.1");
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!mIsLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, DEFAULT_CACHE_DIR);
        }
        boolean vcallback = getIntent().getBooleanExtra("video-data-callback", false);
        if (vcallback) {
            options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);
        }
        boolean acallback = getIntent().getBooleanExtra("audio-data-callback", false);
        if (acallback) {
            options.setInteger(AVOptions.KEY_AUDIO_DATA_CALLBACK, 1);
        }
        if (!mIsLiveStreaming) {
            int startPos = getIntent().getIntExtra("start-pos", 0);
            options.setInteger(AVOptions.KEY_START_POSITION, startPos * 1000);
        }
        mVideoView.setAVOptions(options);

        // Set some listeners
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnVideoFrameListener(mOnVideoFrameListener);
        mVideoView.setOnAudioFrameListener(mOnAudioFrameListener);
        mVideoView.setVideoPath(videoPath);
        mVideoView.setLooping(getIntent().getBooleanExtra("loop", false));

        ChatroomKit.addEventHandler(handler);
        giftView.setViewCount(2);
        giftView.init();
        countDownTimers.start();
        danmuContainerView.setAdapter(new DanmuAdapter(this));
    }

    @Override
    public void initData() {
        roomId = getIntent().getStringExtra("roomId");
        videoId = getIntent().getStringExtra("videoId");
        //加入聊天室
        liveVideosInfo();
        getLiveGift();
        joinChatRoom();
        getBalance();
    }

    private View view;

    @Override
    public void initListener() {


        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveVideoViewActivity.this, LiverDetailActivity.class);

                intent.putExtra("userId", authorId);
                intent.putExtra("id", videoId);

                startActivity(intent);
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_bg.getVisibility() == View.VISIBLE) {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_hide));
                    ll_bg.setVisibility(View.GONE);
//                    iv_tanmu.setVisibility(View.VISIBLE);
                    rl_bottom.setVisibility(View.VISIBLE);
                    ll_ax.setVisibility(View.VISIBLE);
                }
            }
        });
        iv_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveVideosInfo();
                if (products == null) {
                    ToastUtil.showToast("暂无商品");
                    return;
                }
                if (ll_bg.getVisibility() == View.VISIBLE) {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_hide));
                    ll_bg.setVisibility(View.GONE);
                } else {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_show));
                    ll_bg.setVisibility(View.VISIBLE);
                    iv_tanmu.setVisibility(View.GONE);
                    rl_bottom.setVisibility(View.GONE);
                    ll_ax.setVisibility(View.GONE);
                }
            }
        });
        iv_san.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopPayWindows();
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

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }


    public void getLivek() {

        Map<String, String> mParamsMaps = new HashMap<>();
        mParamsMaps.put("id", videoId);
        mParamsMaps.put("click_like_count", clickCount + "");

        DataManager.getInstance().UpdateliveVideo(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> data) {

                liveVideosInfo();


            }

            @Override
            public void onError(Throwable throwable) {

                if (ApiException.getInstance().isSuccess()) {
                    liveVideosInfo();

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, mParamsMaps);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //        mMediaController.getWindow().dismiss();
        mVideoView.pause();
    }

    private int states;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        countDownTimers.cancel();
        states = 1;
        Map<String, String> map = new HashMap<>();
        map.put("room_id", roomId);

        DataManager.getInstance().quitChatter(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object balanceDto) {


            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        }, map);
        ChatroomKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                ChatroomKit.removeEventHandler(handler);
                ChatroomUserQuit userQuit = new ChatroomUserQuit();
                userQuit.setId(getUserId());
                userQuit.setName(getUserName());
                userQuit.setUrl(getUserUrl());
                ChatroomKit.sendMessage(userQuit);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ChatroomKit.removeEventHandler(handler);
                Log.i(TAG, "errorCode = " + errorCode);
            }
        });
    }

    public void onClickSwitchScreen(View v) {
        mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        switch (mVideoView.getDisplayAspectRatio()) {
            case PLVideoView.ASPECT_RATIO_ORIGIN:
                //                Utils.showToastTips(this, "Origin mode");
                break;
            case PLVideoView.ASPECT_RATIO_FIT_PARENT:
                //                Utils.showToastTips(this, "Fit parent !");
                break;
            case PLVideoView.ASPECT_RATIO_PAVED_PARENT:
                //                Utils.showToastTips(this, "Paved parent !");
                break;
            case PLVideoView.ASPECT_RATIO_16_9:
                //                Utils.showToastTips(this, "16 : 9 !");
                break;
            case PLVideoView.ASPECT_RATIO_4_3:
                //                Utils.showToastTips(this, "4 : 3 !");
                break;
            default:
                break;
        }
    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "first video render time: " + extra + "ms");
                    Log.i(TAG, "Response: " + mVideoView.getResponseInfo());
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVideoView.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    //                    updateStatInfo();
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_LOOP_DONE:
                    Log.i(TAG, "Loop done");
                    break;
                case PLOnInfoListener.MEDIA_INFO_CACHE_DOWN:
                    Log.i(TAG, "Cache done");
                    break;
                case PLOnInfoListener.MEDIA_INFO_STATE_CHANGED_PAUSED:
                    Log.i(TAG, "State paused");
                    break;
                case PLOnInfoListener.MEDIA_INFO_STATE_CHANGED_RELEASED:
                    Log.i(TAG, "State released");
                    break;
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    Log.e(TAG, "IO Error!");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "failed to seek !");
                    return true;
                case PLOnErrorListener.ERROR_CODE_CACHE_FAILED:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "failed to cache url !");
                    break;
                default:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "unknown error !");
                    break;
            }
            //            finish();
            return true;
        }
    };

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.i(TAG, "Play Completed !");
            //            Utils.showToastTips(PLVideoViewActivity.this, "Play Completed !");
            if (!mIsLiveStreaming) {
                //                mMediaController.refreshProgress();
            }
            //finish();
        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLOnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private PLOnVideoFrameListener mOnVideoFrameListener = new PLOnVideoFrameListener() {
        @Override
        public void onVideoFrameAvailable(byte[] data, int size, int width, int height, int format, long ts) {
            Log.i(TAG, "onVideoFrameAvailable: " + size + ", " + width + " x " + height + ", " + format + ", " + ts);
            if (format == PLOnVideoFrameListener.VIDEO_FORMAT_SEI && bytesToHex(Arrays.copyOfRange(data, 19, 23)).equals("74733634")) {
                // If the RTMP stream is from Qiniu
                // Add &addtssei=true to the end of URL to enable SEI timestamp.
                // Format of the byte array:
                // 0:       SEI TYPE                    This is part of h.264 standard.
                // 1:       unregistered user data      This is part of h.264 standard.
                // 2:       payload length              This is part of h.264 standard.
                // 3-18:    uuid                        This is part of h.264 standard.
                // 19-22:   ts64                        Magic string to mark this stream is from Qiniu
                // 23-30:   timestamp                   The timestamp
                // 31:      0x80                        Magic hex in ffmpeg
                Log.i(TAG, " timestamp: " + Long.valueOf(bytesToHex(Arrays.copyOfRange(data, 23, 31)), 16));
            }
        }
    };

    private PLOnAudioFrameListener mOnAudioFrameListener = new PLOnAudioFrameListener() {
        @Override
        public void onAudioFrameAvailable(byte[] data, int size, int samplerate, int channels, int datawidth, long ts) {
            Log.i(TAG, "onAudioFrameAvailable: " + size + ", " + samplerate + ", " + channels + ", " + datawidth + ", " + ts);
        }
    };

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };

    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerShop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveVideoViewAdapter();
        mProductAdapter = new LiveProductItemAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerShop.setAdapter(mProductAdapter);
        mProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                String id = products.get(position).getId();
                //                        bundle.getString(CommodityDetailActivity.PRODUCT_ID, id);
                //                        gotoActivity(CommodityDetailActivity.class, false, bundle);
                //                        Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.PRODUCT_ID, id);
                bundle.putString("authorId", authorId);
                bundle.putInt("live", 8);
                Intent intent = new Intent(LiveVideoViewActivity.this, CommodityDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //        chatListAdapter = new ChatListAdapter(this);
        //        chatListView.setAdapter(chatListAdapter);
    }

    private List<MyOrderItemDto> products;

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosInfo() {
        //        showLoadDialog();
        DataManager.getInstance().liveVideosInfo(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
                //                dissLoadDialog();
                if (states == 1) {
                    return;
                }
                if (result != null && result.getData() != null) {
                    chatterTotal = Long.valueOf(result.getData().getChatter_total());
                    tv_count.setText(chatterTotal + "人");
                    tv_ren.setText(result.getData().click_like_count+"" );
                    if (result.getData().getUser() != null && result.getData().getUser().getData() != null) {
                        GlideUtils.getInstances().loadRoundImg(LiveVideoViewActivity.this, iv_icon, Constants.WEB_IMG_URL_UPLOADS + result.getData().getUser().getData().getAvatar(), R.drawable.moren_ren);
                        authorPhone = result.getData().getUser().getData().getPhone();
                        authorId = result.getData().getUser().getData().getId();
                        if (TextUtils.isEmpty(result.getData().getUser().getData().getName())) {
                            tv_name.setText(authorPhone);
                        } else {
                            tv_name.setText(result.getData().getUser().getData().getName());
                        }
                        if (result.getData().videoproducts != null && result.getData().videoproducts.data != null) {
                            products = result.getData().videoproducts.data;
                            mProductAdapter.setNewData(products);
                            tv_size.setText("全部宝贝" + products.size());
                            //                            tv_cout.setText("" + products.size());
                        }
                                                isAttention = result.getData().getUser().getData().isFollowed;
                                                if (isAttention) {
                                                    iv_attention.setImageResource(R.drawable.live_gz);
                                                } else {
                                                    iv_attention.setImageResource(R.drawable.live_nogz);
                                                }
                    }
                    if (result.getData().getRoom() != null && result.getData().getRoom().getData() != null) {
                        tv_room_num.setText("房间号:" + result.getData().getRoom().getData().getRoom_name());
                    }
                }


            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();

            }
        }, videoId);
//        DataManager.getInstance().liveApply(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
//            @Override
//            public void onSuccess(HttpResult<VideoLiveBean> result) {
//                //                dissLoadDialog();
//                if (result != null && result.getLive_apply() != null && result.getLive_apply().getUser() != null) {
//                    ;
//                    if (result.getLive_apply().getUser().is_followed != null) {
//                        iv_attention.setImageResource(R.drawable.live_nogz);
//                    } else {
//                        iv_attention.setImageResource(R.drawable.live_gz);
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                //                dissLoadDialog();
//
//            }
//        }, userId);


    }

    private void getLiveGift() {
        DataManager.getInstance().getLiveGift(new DefaultSingleObserver<HttpResult<List<GiftBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<GiftBean>> result) {
                giftBeans = result.getData();
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void setKeepOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private String userLiveId;
    private String no;


    private CountDownTimer countDownTimers = new CountDownTimer(3600000*24, 1000*30) {
        @Override
        public void onTick(long millisUntilFinished) {
            long looklive = ShareUtil.getInstance().getLong("lookvideo", 0);
            if(looklive>=900){
                putLookLive();
                ShareUtil.getInstance().saveLong("lookvideo",0);
            }else {
                ShareUtil.getInstance().saveLong("lookvideo",(looklive+30));
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
        }, "task_daily_watch_live");
    }
    private void liveRewardMoney(String total) {
        HashMap<String, String> map = new HashMap<>();
        map.put("object", "Modules\\Base\\Entities\\User");
        map.put("id", authorId);
        map.put("total", total);
        DataManager.getInstance().getTips(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                no = result.getData().no;
                if (ste == 0) {

                    getMemberBaseInfo(new InputPwdDialog.InputPasswordListener() {
                        @Override
                        public void callbackPassword(String password) {
                            if (Double.valueOf(GdBalance) < Double.valueOf(palyMoney)) {
                                ToastUtil.showToast("余额不足");
                                return;
                            }
                            submitWalletOrder(password);
                        }
                    });
                } else if (ste == 1) {//微信
                    submitWxOrder();
                } else if (ste == 2) {//支付宝
                    submitOrder();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }

    /**
     * zfb
     */
    private void submitOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        map.put("order_no[" + 0 + "]",no);
        map.put("platform", "alipay");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitZfbOrder(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> httpResult) {
                dissLoadDialog();
                if (httpResult != null && !TextUtils.isEmpty(httpResult.getData())) {
                    PayUtils.getInstances().zfbPaySync(LiveVideoViewActivity.this, httpResult.getData(), new PayResultListener() {
                        @Override
                        public void zfbPayOk(boolean payOk) {
                            if(payOk){
                                sendMony(palyMoney);
                                ToastUtil.showToast("打赏成功");

                            }

                        }

                        @Override
                        public void wxPayOk(boolean payOk) {

                        }
                    });
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);


    }

    /**
     * zfb
     */
    private void submitWalletOrder(String payment_password) {

        HashMap<String, String> map = new HashMap<>();
        map.put("order_no[" + 0 + "]",no);
        map.put("platform", "wallet");
        map.put("scene", "balance");
        map.put("payment_password", payment_password);
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitWalletOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
               ToastUtil.showToast("打赏成功");
                sendMony(palyMoney);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("打赏成功");
                    sendMony(palyMoney);
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);


    }

    /**
     * wx
     */
    private void submitWxOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;


        map.put("order_no[" + 0 + "]", no);
        map.put("platform", "wechat");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney


        DataManager.getInstance().submitWxOrder(new DefaultSingleObserver<HttpResult<WEIXINREQ>>() {
            @Override
            public void onSuccess(HttpResult<WEIXINREQ> httpResult) {
                dissLoadDialog();
                PayUtils.getInstances().WXPay(LiveVideoViewActivity.this, httpResult.getData());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }

    private int RQ_WEIXIN_PAY = 12;
    private int RQ_PAYPAL_PAY = 16;
    private int RQ_ALIPAY_PAY = 10;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RQ_WEIXIN_PAY) {
                ToastUtil.showToast("打赏成功");
                sendMony(palyMoney);
                //           if (requestCode == RQ_WEIXIN_PAY) {
                //                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
                //            }
            }

        }
    }

    /**
     * 余额支付时，查询是否设置支付密码
     */
    private void getMemberBaseInfo(InputPwdDialog.InputPasswordListener listener) {
        if (BaseApplication.isSetPay == 1) {
            InputPwdDialog inputPasswordDialog = new InputPwdDialog(LiveVideoViewActivity.this, listener);
            inputPasswordDialog.show();
        } else {
            gotoActivity(SettingPasswordActivity.class);
        }
    }

    private void liveRewardScore(String total, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pay_password", password);
        map.put("type", "score");
        map.put("to", authorPhone);
        map.put("total", total);
        DataManager.getInstance().liveRewardScore(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                if (pointAmountDialog != null) {
                    pointAmountDialog.dismiss();
                }
                //                ToastUtil.showToast("支付成功");
                sendScoreMessage(total);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    if (pointAmountDialog != null) {
                        pointAmountDialog.dismiss();
                    }
                    ToastUtil.showToast("支付成功");
                    sendScoreMessage(total);
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }

    private void liveReward(GiftBean giftBean) {
        HashMap<String, String> map = new HashMap<>();
        map.put("gift_id", giftBean.getId());
        map.put("to", authorId);
        map.put("to_type", "id");
        DataManager.getInstance().liveReward(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                if (gifListDialog != null) {
                    gifListDialog.dismiss();
                }
                //                ToastUtil.showToast("支付成功");
                sendGifMessage(giftBean);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    if (gifListDialog != null) {
                        gifListDialog.dismiss();
                    }
                    //                    ToastUtil.showToast("支付成功");
                    sendGifMessage(giftBean);
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);
    }

    /**
     * 加入聊天室
     */
    private void joinChatRoom() {
        ChatroomKit.joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
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
                ToastUtil.showToast("加入聊天室失败,请重新登录");
                finish();
            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("room_id", roomId);
        DataManager.getInstance().joinChatter(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object balanceDto) {

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                Log.e("lg", "");
            }
        }, map);
    }

    private TextView      tvPay;
    private EditText      et_money;
    private FlexboxLayout fblZw;

    public void showPopPayWindows() {
        tags.clear();
        BaseType baseType = new BaseType();
        baseType.name = "1元";
        baseType.cout = 1;
        tags.add(baseType);
        BaseType baseType1 = new BaseType();
        baseType1.name = "5元";
        baseType1.cout = 5;
        tags.add(baseType1);
        BaseType baseType2 = new BaseType();
        baseType2.name = "10元";
        baseType2.cout = 10;
        tags.add(baseType2);
        BaseType baseType3 = new BaseType();
        baseType3.name = "50元";
        baseType3.cout = 50;
        tags.add(baseType3);
        BaseType baseType4 = new BaseType();
        baseType4.name = "100元";
        baseType4.cout = 100;
        tags.add(baseType4);
        BaseType baseType5 = new BaseType();
        baseType5.name = "500元";
        baseType5.cout = 500;
        tags.add(baseType5);
        BaseType baseType6 = new BaseType();
        baseType6.name = "1000元";
        baseType6.cout = 1000;
        tags.add(baseType6);
        BaseType baseType7 = new BaseType();
        baseType7.name = "5000元";
        baseType7.cout = 5000;
        tags.add(baseType7);

        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.pay_ds_popwindow_view, null);
            tvPay = view.findViewById(R.id.tv_pay);
            et_money = view.findViewById(R.id.et_money);
            fblZw = view.findViewById(R.id.fbl_zw);
            setTagAdapter();
            mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
            mWindowAddPhoto.showAtLocation(recyclerView, Gravity.BOTTOM, 0, 0);
            tvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    palyMoney = et_money.getText().toString();

                    if (TextUtil.isEmpty(palyMoney) || Integer.valueOf(palyMoney) < 0) {
                        ToastUtil.showToast("请输入打赏金额");
                    }
                    mWindowAddPhoto.dismiss();
                    showPopPayTypeWindows();
                }
            });
        } else {
            mWindowAddPhoto.showAtLocation(recyclerView, Gravity.BOTTOM, 0, 0);
        }


    }

    private String           palyMoney;
    private PhotoPopupWindow mWindowAddPhoto;

    /**
     * 创建流式布局item
     *
     * @param content
     * @return
     */
    public Custom_TagBtn createBaseFlexItemTextView(BaseType content) {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = DensityUtil.dip2px(this, 10);
        lp.leftMargin = DensityUtil.dip2px(this, 7);
        lp.rightMargin = DensityUtil.dip2px(this, 7);


        Custom_TagBtn view = (Custom_TagBtn) LayoutInflater.from(this).inflate(R.layout.dish_item, null);

        if (content.isChoos) {

            view.setColors(R.color.white);
            view.setBg(getResources().getDrawable(R.drawable.shape_radius_16));
        } else {
            view.setColors(R.color.white);
            view.setBg(getResources().getDrawable(R.drawable.shape_radius_16ddd));
        }
        int width = (int) DensityUtil.getWidth(this) / 3;
        String name = content.name;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(15);
        int with = (int) textPaint.measureText(name);
        view.setSize(66, 33, 15, 1);
        view.setLayoutParams(lp);
        view.setCustomText(content.name);

        return view;
    }

    private String         id;
    private List<BaseType> tags = new ArrayList<>();
    private int  postion;
    public void setTagAdapter() {

        fblZw.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            final Custom_TagBtn tagBtn = createBaseFlexItemTextView(tags.get(i));
            final int position = i;

            tagBtn.setCustom_TagBtnListener(new Custom_TagBtn.Custom_TagBtnListener() {
                @Override
                public void clickDelete(int type) {

                    for (int j = 0; j < tags.size(); j++) {
                        if (position == j) {
                            postion=j;
                            id = tags.get(j).classifyId;
                            tags.get(j).isChoos = true;
                            handler.sendEmptyMessage(88);
                            et_money.setText(tags.get(j).cout+"");
                        } else {
                            tags.get(j).isChoos = false;
                        }
                    }

                    setTagAdapter();
                }
            });
            fblZw.addView(tagBtn);
        }

    }
     private boolean isFisrt;
    public void getBalance() {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                GdBalance = balanceDto.getMoney();
                super.onSuccess(balanceDto);
                if(isFisrt&&tv_balance!=null){
                    tv_balance.setText("(" + GdBalance + ")");
                }else {
                    isFisrt=true;
                }


            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    private void checkWallet(String string, int type, GiftBean giftBean) {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                if (balanceDto != null && balanceDto.isPay_password()) {
                    if (type != 1) {
                        //送礼物
                        liveReward(giftBean);
                        return;
                    }
                    new InputPasswordDialog(LiveVideoViewActivity.this, new InputPasswordDialog.InputPasswordListener() {
                        @Override
                        public void callbackPassword(String password) {
                            //打赏积分
                            liveRewardScore(string, password); //自定义积分

                        }
                    }).show();
                } else {
                    ToastUtil.toast("请先设置支付密码");
                    gotoActivity(SettingPasswordActivity.class);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    private Button           btSure;
    private TextView         tv_title;
    private ImageView        iv_close;
    private View             views;
    private View             vis;
    private PhotoPopupWindow mWindowpayPhoto;
    private LinearLayout     llBalance;
    private LinearLayout     llZfb;
    private LinearLayout     llWx;
    private MCheckBox        mcbBalance;
    private MCheckBox        mcbZfb;
    private MCheckBox        mcbWx;
    private TextView         tv_balance;
    private double           total;
    private double           totals;

    public void setChoose(int state) {
        switch (state) {
            case 0:
                mcbBalance.setChecked(true);
                mcbZfb.setChecked(false);
                mcbWx.setChecked(false);
                break;

            case 1:
                mcbBalance.setChecked(false);
                mcbZfb.setChecked(false);
                mcbWx.setChecked(true);
                break;
            case 2:
                mcbBalance.setChecked(false);
                mcbZfb.setChecked(true);
                mcbWx.setChecked(false);
                break;
        }
    }

    private double GdBalance;

    private PhotoPopupWindow mWindowAddPhotos;

    public void showPopPayTypeWindows() {

        if (views == null) {
            views = LayoutInflater.from(this).inflate(R.layout.pay_popwindow_view, null);

            btSure = views.findViewById(R.id.bt_sure);
            vis = views.findViewById(R.id.views);
            iv_close = views.findViewById(R.id.iv_close);
            llBalance = views.findViewById(R.id.ll_balance);
            llZfb = views.findViewById(R.id.ll_zfb);
            llWx = views.findViewById(R.id.ll_wx);
            mcbBalance = views.findViewById(R.id.mcb_balance);
            mcbZfb = views.findViewById(R.id.mcb_zfb);
            mcbWx = views.findViewById(R.id.mcb_wx);
            tv_balance = views.findViewById(R.id.tv_balance);
            if (GdBalance != 0) {
                tv_balance.setText("(" + GdBalance + ")");
            }
            vis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhotos.dismiss();
                }
            });
            llBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChoose(0);
                    ste = 0;
                }
            });
            llWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ste = 1;
                    setChoose(1);
                }
            });
            llZfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ste = 2;
                    setChoose(2);
                }
            });
            btSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ste == -1) {

                            ToastUtil.showToast("请选择支付方式");
                            return;

                    }
                    if (ste == 0) {
                        if (Double.valueOf(GdBalance) < Double.valueOf(palyMoney)) {
                            ToastUtil.showToast("余额不足");
                            return;
                        }
                    }

                    liveRewardMoney(palyMoney);
                    mWindowAddPhotos.dismiss();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhotos.dismiss();
                }
            });

            mWindowAddPhotos = new PhotoPopupWindow(this).bindView(views);
            mWindowAddPhotos.showAtLocation(iv_shop, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhotos.showAtLocation(iv_shop, Gravity.BOTTOM, 0, 0);
        }


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

    /**
     * 打赏积分
     */
    private void sendScoreMessage(String score) {

        ChatroomJifen jifen = new ChatroomJifen();
        jifen.setName(getUserName());
        jifen.setUrl(getUserUrl());
        jifen.setJifen(score);
        ChatroomKit.sendMessage(jifen);
    }

    /**
     * 发送礼物类型
     */
    private void sendGifMessage(GiftBean giftBean) {
        ChatroomGift gift = new ChatroomGift();
        gift.setId(giftBean.getId());
        gift.setName(getUserName());
        gift.setUrl(getUserUrl());
        gift.setGift_name(giftBean.getGift_name());
        gift.setGiftURL(Constants.WEB_IMG_URL_UPLOADS + giftBean.getThumb());
        gift.setG_id(giftBean.getId());
        ChatroomKit.sendMessage(gift);
    }
    /**
     * 发送礼物类型
     */
    private void sendMony(String total) {
        ChatroomGift gift = new ChatroomGift();
        gift.setId("ds");
        gift.setName(getUserName());
        gift.setUrl(getUserUrl());
        gift.setGift_name(""+total);
        gift.setGiftURL("ds");
//        gift.setTotal(Integer.valueOf(total));
        gift.setG_id("ds");
        ChatroomKit.sendMessage(gift);
        getBalance();
    }
    /**
     * 点赞
     */
    private void sendZanMessage() {
        ChatroomLike likeMessage = new ChatroomLike();
        likeMessage.setCounts(clickCount);
        likeMessage.setUrl(getUserUrl());
        likeMessage.setName(getUserName());
        //        likeMessage.setId(getUserId());
        ChatroomKit.sendMessage(likeMessage);

        getLivek();
    }

    /**
     * 弹幕
     */
    private void sendDanmuMessage(String text) {
        ChatroomBarrage barrage = new ChatroomBarrage();
        barrage.setContent(text);
        barrage.setType(0);
        barrage.setName(getUserUrl());
        barrage.setUrl(getUserName());
        ChatroomKit.sendMessage(barrage);
    }

    private UserInfo getUserInfo() {
        Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null));
        String userId = ShareUtil.getInstance().getString(Constants.USER_ID, "");
        String nickName = ShareUtil.getInstance().getString(Constants.USER_NAME, "");
        return new UserInfo(userId, nickName, RongHeadImg);
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

        //         model.setUserAvatarRes();
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
            danmuEntity.setName(barrage.getUrl());
            danmuEntity.setUrl(barrage.getName());
            danmuEntity.setType(barrage.getType());
            danmuContainerView.addDanmu(danmuEntity);
        } else if (messageContent instanceof ChatroomEnd) {
            //主播结束直播
            ConfirmDialogs dialog = new ConfirmDialogs(this);
            dialog.setTitle("温馨提示");
            dialog.setMessage("主播已离开");
            dialog.setCancelGone();
            dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {

                @Override
                public void onYesClick() {
                    finish();
                    dialog.dismiss();
                }
            });
            dialog.show();

        } else if (messageContent instanceof ChatroomUser) {
            //房间人数
            ChatroomUser chatroomUser = (ChatroomUser) messageContent;
            tv_count.setText(chatroomUser.getExtra() + "人");
        } else {
            if (messageContent instanceof ChatroomGift && ((ChatroomGift) messageContent).getType() == 0) {
                //收到礼物
                showGif(((ChatroomGift) messageContent));
            }else if (messageContent instanceof ChatroomWelcome) {
                chatterTotal=chatterTotal+1;
                tv_count.setText((chatterTotal+1) + "人");
            } else if (messageContent instanceof ChatroomUserQuit) {
                chatterTotal=chatterTotal-1;
                tv_count.setText((chatterTotal<=0?0:chatterTotal )+ "人");
            }
            mAdapter.addData(messageContent);
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    private void postAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", authorId);
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("关注成功");
                isAttention = true;
                iv_attention.setImageResource(R.drawable.live_gz);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isAttention = true;
                    iv_attention.setImageResource(R.drawable.live_gz);
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", authorId);
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("取消关注成功");
                isAttention = false;
                iv_attention.setImageResource(R.drawable.live_nogz);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isAttention = false;
                    iv_attention.setImageResource(R.drawable.live_nogz);
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back, R.id.iv_gift, R.id.iv_mute, R.id.iv_attention, R.id.iv_share, R.id.input_editor, R.id.iv_zan,
            R.id.iv_tanmu, R.id.rl_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            //            case R.id.iv_point_amount:

            //                pointAmountDialog = new PointAmountDialog(LiveVideoViewActivity.this);
            //                pointAmountDialog.setYesOnclickListener1("去支付", new PointAmountDialog.OnYesClickListener1() {
            //                    @Override
            //                    public void onYesClick(String total, String inputAmount) {
            //                        String mTotal = total;
            //                        if (!TextUtils.isEmpty(inputAmount) && Double.valueOf(inputAmount) > 0) {
            //                            mTotal = inputAmount; //自定义积分
            //                        }
            //                        checkWallet(mTotal, 1, null);
            //                    }
            //                });
            //                pointAmountDialog.setCancleClickListener("取消", new BaseDialog.OnCloseClickListener() {
            //
            //                    @Override
            //                    public void onCloseClick() {
            //                        pointAmountDialog.dismiss();
            //                    }
            //                });
            //                pointAmountDialog.show();
            //                break;
            case R.id.iv_gift:
                gifListDialog = new GifListDialog(LiveVideoViewActivity.this, giftBeans);
                gifListDialog.setYesOnclickListener1("赠送", new GifListDialog.OnYesClickListener1() {
                    @Override
                    public void onYesClick(GiftBean giftBean) {
                        checkWallet("", 2, giftBean);

                    }
                });
                gifListDialog.show();
                break;
            case R.id.iv_mute:
                //声音切换
                isMute = !isMute;
                if (isMute) {
                    ivMute.setImageResource(R.mipmap.shengyin1);
                    mVideoView.setVolume(0f, 0f);
                } else {
                    ivMute.setImageResource(R.mipmap.shengyin2);
                    mVideoView.setVolume(1f, 1f);
                }

                break;
            case R.id.iv_zan:
                //点赞
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        heartLayout.addHeart(rgb);
                    }
                });
                clickCount++;
                currentTime = System.currentTimeMillis();
                checkAfter(currentTime);
                //                sendZanMessage();
                break;
            case R.id.iv_attention:
                if (isAttention) {
                    deleteAttention();
                } else {
                    postAttention();
                }
                break;
            case R.id.iv_share:
                //分享
                ShareModeDialog dialog = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                    @Override
                    public void sureItem(int position) {
                        boolean isTimelineCb = false;
                        String url =Constants.BASE_URLS+"h5/#/liveVideo/" + videoId;
                        String title = ShareUtil.getInstance().get(Constants.USER_NAME)+"正在5G社交直播带货平台直播";
                        if (position == ShareModeDialog.SHARE_PYQ) {
                            isTimelineCb = true;
                        }
                        ShareUtil.sendToWeaChat(LiveVideoViewActivity.this, isTimelineCb, title, url);
                    }
                });
                dialog.show();
                break;
            case R.id.input_editor:
                //说点什么吧
                inputPanel.setVisibility(View.VISIBLE);
                inputPanel.requestTextFocus();
                inputPanel.setType(InputPanel.TYPE_TEXTMESSAGE);
                break;
            case R.id.iv_tanmu:
                //弹幕
                inputPanel.setVisibility(View.VISIBLE);
                inputPanel.requestTextFocus();
                inputPanel.setType(InputPanel.TYPE_BARRAGE);
                break;
            case R.id.rl_root:
                inputPanel.setVisibility(View.GONE);
                inputPanel.hideKeyboard();
                break;
        }

    }

    //500毫秒后做检查，如果没有继续点击了，发消息
    public void checkAfter(final long lastTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lastTime == currentTime) {
                    sendZanMessage();
                    clickCount = 0;
                }
            }
        }, 500);
    }

    //    @Override
    //    public boolean handleMessage(android.os.Message msg) {
    //        switch (msg.what) {
    //            case ChatroomKit.MESSAGE_ARRIVED:
    //            case 30001:
    //                joinCha-`-
    // +    +`-``+
    //
    //
    // ·+-0987654321tRoom();
    //                break;
    //            case ChatroomKit.MESSAGE_SENT: {
    //                MessageContent messageContent = ((Message) msg.obj).getContent();
    //                setData(messageContent);
    //                break;
    //            }
    //
    //        }
    //        return false;
    //    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case ChatroomKit.MESSAGE_ARRIVED:
            case ChatroomKit.MESSAGE_SENT: {
                MessageContent messageContent = ((Message) msg.obj).getContent();
                setData(messageContent);
                break;

            }

        }
        return false;
    }
}
