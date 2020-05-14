package com.smg.variety.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.ConfigDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.qiniu.chatroom.ChatroomKit;
import com.smg.variety.utils.DownLoadServerice;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.mainfragment.LiveHomeFragmnt;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.mainfragment.ConsumeFragment;
import com.smg.variety.view.mainfragment.LearnFragment;
import com.smg.variety.view.mainfragment.MeFragment;
import com.smg.variety.view.mainfragment.MemberFragment;
import com.smg.variety.view.mainfragment.community.TopicPublishActivity;
import com.smg.variety.view.widgets.FirstPopupWindow;
import com.smg.variety.view.widgets.PhotoPopupWindow;
import com.smg.variety.view.widgets.autoview.AutoViewPager;
import com.smg.variety.view.widgets.dialog.MorePopWindow;
import com.smg.variety.view.widgets.updatadialog.UpdataCallback;
import com.smg.variety.view.widgets.updatadialog.UpdataDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.RongIMClient;


public class MainActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.tv_home1)
    TextView      tvHome1;
    @BindView(R.id.tv_home2)
    TextView      tvHome2;
    @BindView(R.id.tv_home3)
    TextView      tvHome3;
    @BindView(R.id.tv_home4)
    TextView      tvHome4;
    @BindView(R.id.tv_home5)
    TextView      tvHome5;
    @BindView(R.id.main_bottom)
    LinearLayout  mainBottom;
    @BindView(R.id.main_viewpager)
    AutoViewPager mainViewpager;
    private AutoViewPager  mViewPager;
    private Context        mContext;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    //会话列表的fragment


    public static final String APP_EXIT = "app_exit";//退出应用

    @BindView(R.id.consume_layout)
    LinearLayout consume_layout;
    @BindView(R.id.chat_layout)
    RelativeLayout chat_layout;
    @BindView(R.id.community_layout)
    RelativeLayout community_layout;
    @BindView(R.id.learn_layout)
    RelativeLayout learn_layout;
    @BindView(R.id.me_layout)
    RelativeLayout me_layout;

    @BindView(R.id.tab_img_consume)
    ImageView tab_img_consume;
    @BindView(R.id.tab_img_chat)
    ImageView tab_img_chat;
    @BindView(R.id.tab_img_community)
    ImageView tab_img_community;
    @BindView(R.id.tab_img_learn)
    ImageView tab_img_learn;
    @BindView(R.id.tab_img_me)
    ImageView tab_img_me;


    @BindView(R.id.ll_headview_top_message_view)
    View           ll_message_view_top;//消息顶部View
    @BindView(R.id.iv_message_actionbar_add)
    ImageView      iv_message_actionbar_add;
    @BindView(R.id.iv_message_contacts)
    ImageView      iv_message_contacts;
    @BindView(R.id.ll_search_friend_view)
    RelativeLayout ll_search_friend_view;

    private long firstClick  = 0;
    private long secondClick = 0;
    private long clickTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private             int    page_index = 0;
    public static final String PAGE_INDEX = "page_index";//调用者传递的名字

    @Override
    public void initView() {
        mContext = this;
        checkPermissioin();
        StatusBarUtils.StatusBarLightMode(this);
        mViewPager = findViewById(R.id.main_viewpager);
        page_index = getIntent().getIntExtra(PAGE_INDEX, 0);
        mWindow=new FirstPopupWindow(this);
        changeTextViewColor();
        initMainViewPager();
        mViewPager.setCurrentItem(page_index == 0 ? 0 : page_index , false);
        changeSelectedTabState(page_index == 0 ? 0 : page_index );
        getConfigs();
        mHandler.sendEmptyMessageDelayed(1,800);
    }
    private Handler          mHandler =new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(!ShareUtil.getInstance().isLogin()){
                mWindow.showAsDropDown(findViewById(R.id.lines));
            }
            return false;
        }
    });
    private     FirstPopupWindow mWindow;
    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getConfigs(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if(result.getData() != null) {
                        if (result.getData().apk!=null&&TextUtil.isNotEmpty(result.getData().apk.version)) {
                            if (getAppVersionName(MainActivity.this).compareTo(result.getData().apk.version) < 0) {
                                UpdataDialog dialog = new UpdataDialog(MainActivity.this, new UpdataCallback() {
                                    @Override
                                    public void goData() {

                                        Intent intent = new Intent(MainActivity.this, DownLoadServerice.class);
                                        intent.putExtra("url", result.getData().apk.download_url);
                                        intent.putExtra("versionName", result.getData().apk.version);
                                        startService(intent);

                                    }
                                });
                                dialog.setContent(result.getData().apk.update_msg);
                                dialog.setCancelable(false);
                                dialog.show();
                            }

                        }

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    /**
     * 获取app版本名
     *
     * @param context the context
     * @return the string
     */
    public  String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void initData() {
        String cacheToken = ShareUtil.getInstance().getString(Constants.APP_USER_KEY, null);


        if (!TextUtils.isEmpty(cacheToken)) {
            ChatroomKit.connect(cacheToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    LogUtil.d(TAG, "ConnectCallback connect onTokenIncorrect");
                    ShareUtil.getInstance().save(Constants.APP_USER_KEY, "");
                }

                @Override
                public void onSuccess(String userId) {
                    LogUtil.d(TAG, "--ConnectCallback connect onSuccess");
                    ShareUtil.getInstance().save(Constants.USER_ID, userId);
                }

                @Override
                public void onError(final RongIMClient.ErrorCode e) {
                    ToastUtil.showToast("" + e.getMessage());
                    LogUtil.d(TAG, "--ConnectCallback connect onError-ErrorCode=" + e.getMessage());
                }
            });

            RongIMClient.getInstance().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                @Override
                public void onChanged(ConnectionStatus status) {
                    switch (status) {

                        case CONNECTED://连接成功。
                            Log.i(TAG, "连接成功");
                            break;
                        case DISCONNECTED://断开连接。
                            Log.i(TAG, "断开连接");
                            break;
                        case CONNECTING://连接中。
                            Log.i(TAG, "连接中");
                            break;
                        case NETWORK_UNAVAILABLE://网络不可用。
                            Log.i(TAG, "网络不可用");
                            break;
                        case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                            Log.i(TAG, "用户账户在其他设备登录");
                            break;
                    }
                }
            });
        } else {
            //ToastUtil.showToast("当前用户token凭证为空");
        }

        getConversationPush();// 获取 push 的 id 和 target
    }

    @Override
    public void initListener() {
        bindClickEvent(consume_layout, () -> {
            if (mViewPager.getCurrentItem() == 0) {
                if (firstClick == 0) {
                    firstClick = System.currentTimeMillis();
                } else {
                    secondClick = System.currentTimeMillis();
                }

                if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {

                    firstClick = 0;
                    secondClick = 0;
                } else if (firstClick != 0 && secondClick != 0) {
                    firstClick = 0;
                    secondClick = 0;
                }
            }
            mViewPager.setCurrentItem(0, false);
        });
        bindClickEvent(community_layout, () -> {
            if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                gotoActivity(LoginActivity.class);
            } else {
                mViewPager.setCurrentItem(1, false);
            }
//            mViewPager.setCurrentItem(1, false);
        });
        bindClickEvent(chat_layout, () -> {
            if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                gotoActivity(LoginActivity.class);
            } else {
                mViewPager.setCurrentItem(2, false);
            }
        });
        bindClickEvent(learn_layout, () -> {
            if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                gotoActivity(LoginActivity.class);
            } else {
                mViewPager.setCurrentItem(3, false);
            }

        });
        bindClickEvent(me_layout, () -> {
            if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                gotoActivity(LoginActivity.class);
            } else {
                mViewPager.setCurrentItem(4, false);
            }

        });

        bindClickEvent(iv_message_contacts, () -> {
            //           gotoActivity(ContactsActivity.class);
        });
        bindClickEvent(iv_message_actionbar_add, () -> {
            MorePopWindow morePopWindow = new MorePopWindow(this);
            morePopWindow.showPopupWindow(iv_message_actionbar_add);
        });
        //设置会话列表界面item操作的监听器

        BroadcastManager.getInstance(mContext).addAction(APP_EXIT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                moveTaskToBack(false);
                finishAll();
            }
        });
    }

    private void initMainViewPager() {
        fragments.add(new ConsumeFragment());
//        fragments.add(new ShopClassFragment());
        fragments.add(new MemberFragment());
                fragments.add(new LiveHomeFragmnt());
        fragments.add(new LearnFragment());
        fragments.add(new MeFragment());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setScanScroll(false);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTextViewColor() {
        tab_img_consume.setBackgroundResource(R.mipmap.ic_xiaofei_def);
        tab_img_chat.setBackgroundResource(R.mipmap.ic_chat_def);
        tab_img_community.setBackgroundResource(R.drawable.mis_hys);
        tab_img_learn.setBackgroundResource(R.mipmap.ic_xuexi_def);
        tab_img_me.setBackgroundResource(R.mipmap.ic_my_def);
        tvHome1.setTextColor(getResources().getColor(R.color.my_color_3a3a));
        tvHome2.setTextColor(getResources().getColor(R.color.my_color_3a3a));
        tvHome3.setTextColor(getResources().getColor(R.color.my_color_3a3a));
        tvHome4.setTextColor(getResources().getColor(R.color.my_color_3a3a));
        tvHome5.setTextColor(getResources().getColor(R.color.my_color_3a3a));

    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:

                tab_img_consume.setBackgroundResource(R.mipmap.ic_xiaofei_sel);
                tvHome1.setTextColor(getResources().getColor(R.color.my_color_0266ca));
                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 1:
                tab_img_community.setBackgroundResource(R.drawable.mis_hy);
                tvHome2.setTextColor(getResources().getColor(R.color.my_color_0266ca));

                                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 2:
                tab_img_chat.setBackgroundResource(R.mipmap.ic_chat_sel);
                tvHome3.setTextColor(getResources().getColor(R.color.my_color_0266ca));
                ll_message_view_top.setVisibility(View.GONE);

                break;
            case 3:
                tab_img_learn.setBackgroundResource(R.mipmap.ic_xuexi_sel);
                tvHome4.setTextColor(getResources().getColor(R.color.my_color_0266ca));
                ll_message_view_top.setVisibility(View.GONE);

                //                tab_img_learn.setBackgroundResource(R.mipmap.ic_xuexi_sel);
                //                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 4:

                tvHome5.setTextColor(getResources().getColor(R.color.my_color_0266ca));
                tab_img_me.setBackgroundResource(R.mipmap.ic_my_sel);
                ll_message_view_top.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_PUBLISH && data != null) {
            ArrayList<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.INTENT_RESULT_DATA, resultList);
                gotoActivity(TopicPublishActivity.class, false, bundle, Constants.INTENT_ADD_FRIEND);
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.HOME_REQUEST_CODE_SCAN && data != null) {

        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.CHAT_REQUEST_CODE_SCAN && data != null) {

        }
    }


    private void getConversationPush() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(2, false);
        }
    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - clickTime < 2000) {
            moveTaskToBack(false);
            finishAll();
        } else {
            clickTime = System.currentTimeMillis();
            ToastUtil.toast("再按一次退出应用");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

