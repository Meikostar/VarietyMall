package com.smg.variety.view.mainfragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.CountOrderBean;
import com.smg.variety.bean.CountStatisticsBean;
import com.smg.variety.bean.MemberDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.ServiceMenuBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.LogoutEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.activity.AppNewPeopleActivity;
import com.smg.variety.view.activity.AppQianDaoActivity;
import com.smg.variety.view.activity.AppRenWuActivity;
import com.smg.variety.view.activity.HelpCenterActivity;
import com.smg.variety.view.activity.MoreDetailActivity;
import com.smg.variety.view.activity.NewsOrderActivity;
import com.smg.variety.view.activity.OrderTgActivity;
import com.smg.variety.view.activity.RechargeWebActivity;
import com.smg.variety.view.activity.SuperListActivity;
import com.smg.variety.view.activity.SuperMemberActivity;
import com.smg.variety.view.activity.SuperYqYlActivity;
import com.smg.variety.view.activity.WithdrawActivity;
import com.smg.variety.view.adapter.ServiceMenuAdapter;
import com.smg.variety.view.mainfragment.consume.BrandShopDetailActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.widgets.CircleImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 */
public class MemberFragment extends BaseFragment {


    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_state)
    TextView        tvState;
    @BindView(R.id.iv_state)
    ImageView       ivState;
    @BindView(R.id.tv_code)
    TextView        tvCode;
    @BindView(R.id.ll_wchat)
    LinearLayout    llWchat;
    @BindView(R.id.card_tx)
    LinearLayout        cardTx;
    @BindView(R.id.iv_banner)
    Banner          banner;
    @BindView(R.id.ll_sy)
    LinearLayout    llSy;
    @BindView(R.id.tv_one)
    TextView        tvOne;
    @BindView(R.id.tv_two)
    TextView        tvTwo;
    @BindView(R.id.tv_three)
    TextView        tvThree;
    @BindView(R.id.tv_four)
    TextView        tvFour;
    @BindView(R.id.iv_one)
    ImageView       ivOne;
    @BindView(R.id.iv_two)
    ImageView       ivTwo;
    @BindView(R.id.ll_hy)
    LinearLayout    llHy;
    @BindView(R.id.tv_five)
    TextView        tvFive;
    @BindView(R.id.tv_six)
    TextView        tvSix;
    @BindView(R.id.tv_seven)
    TextView        tvSeven;
    @BindView(R.id.tv_eight)
    TextView        tvEight;
    @BindView(R.id.ll_dd)
    LinearLayout    llDd;
    @BindView(R.id.tv_night)
    TextView        tvNight;
    @BindView(R.id.tv_ten)
    TextView        tvTen;
    @BindView(R.id.ll_one)
    LinearLayout    llOne;
    @BindView(R.id.ll_two)
    LinearLayout    llTwo;
    @BindView(R.id.ll_three)
    LinearLayout    llThree;
    @BindView(R.id.ll_four)
    LinearLayout    llFour;
    private PersonalInfoDto mPersonalInfoDto;
    ServiceMenuAdapter menuAdapter;
    Unbinder           unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.member_fragment;
    }

    @Override
    protected void initView() {

        menuAdapter = new ServiceMenuAdapter();

        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ServiceMenuBean item = menuAdapter.getItem(position);
                if ("help".equals(item.getClick_event_value())) {
                    gotoActivity(HelpCenterActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), RechargeWebActivity.class);
                    intent.putExtra(Constants.INTENT_WEB_URL, item.getClick_event_value());
                    intent.putExtra(Constants.INTENT_WEB_TITLE, item.getTitle());
                    startActivity(intent);
                }

            }
        });
    }

    private void startBanner(List<BannerItemDto> data) {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.CENTER);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(data);
        //设置banner动画效果
        //        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerItemDto slidersDto = (BannerItemDto) path;
            String imgStr = slidersDto.getPath();
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 5f, imgStr, R.mipmap.img_default_3);
                } else {
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 5f, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_3);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SuperMemberActivity.class);
                    intent.putExtra("level",level);
                    startActivity(intent);
                }
            });


        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void initData() {

        getBottenBanner();
        getBottenBanner1();
        getBottenBanner2();

    }

    @Override
    protected void initListener() {
        llWchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showExamNoticeDialog(getActivity(), wx, new DialogUtils.OnClickDialogListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        // 创建普通字符型ClipData
                        ClipData mClipData = ClipData.newPlainText("Label", wx);
                        // 将ClipData内容放到系统剪贴板里。
                        cm.setPrimaryClip(mClipData);
                        ToastUtil.showToast("复制成功");
                    }
                });
            }
        });
        ivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                gotoActivity(AppQianDaoActivity.class);
//                gotoActivity(AppNewPeopleActivity.class);
                Intent intent = new Intent(getActivity(), SuperYqYlActivity.class);

                startActivity(intent);

            }
        });
        ivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuperListActivity.class);

                startActivity(intent);

            }
        });

        llDd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), OrderTgActivity.class);

                startActivity(intent);
            }
        });
        llOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), MoreDetailActivity.class);
                intent.putExtra("index",1);
                startActivity(intent);
            }
        });
        llHy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(), NewsOrderActivity.class);
                startActivity(intent);
            }
        });

        llTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreDetailActivity.class);
                intent.putExtra("index",2);
                startActivity(intent);
            }
        });
        llThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreDetailActivity.class);
                intent.putExtra("index",3);
                startActivity(intent);
            }
        });
        llFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreDetailActivity.class);
                intent.putExtra("index",4);
                startActivity(intent);
            }
        });


        cardTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(WithdrawActivity.class);

            }
        });

    }
//    reg_count:我的注册用户数,upper_count:我的掌柜数量,upper_descendants_count:
//    全部掌柜数量,all_descendants_count:全部会员,day_promote_income:
//    今日推广收益,day_recommend_income:今日推荐收益,day_platform_income:
//    今日平台补贴,day_income:今日全部收益
    //获取新闻标签  "day_promote_count": 0,
//  "all_promote_count": 1
    private void getMemberInfo() {
        DataManager.getInstance().getMemberInfo(new DefaultSingleObserver<MemberDto>() {
            @Override
            public void onSuccess(MemberDto tagBean) {
                //dissLoadDialog();
                if(tagBean!=null){
                    if(TextUtil.isNotEmpty(tagBean.day_promote_income)){
                        tvOne.setText("¥"+tagBean.day_promote_income);
                    }    if(TextUtil.isNotEmpty(tagBean.day_recommend_income)){
                        tvTwo.setText("¥"+tagBean.day_recommend_income);
                    }    if(TextUtil.isNotEmpty(tagBean.day_platform_income)){
                        tvThree.setText("¥"+tagBean.day_platform_income);
                    }    if(TextUtil.isNotEmpty(tagBean.day_income)){
                        tvFour.setText("¥"+tagBean.day_income);
                    }    if(TextUtil.isNotEmpty(tagBean.reg_count)){
                        tvFive.setText(tagBean.reg_count);
                    }    if(TextUtil.isNotEmpty(tagBean.upper_count)){
                        tvSix.setText(tagBean.upper_count);
                    }    if(TextUtil.isNotEmpty(tagBean.upper_descendants_count)){
                        tvSeven.setText(tagBean.upper_descendants_count);
                    }    if(TextUtil.isNotEmpty(tagBean.all_descendants_count)){
                        tvEight.setText(tagBean.all_descendants_count);
                    }   if(TextUtil.isNotEmpty(tagBean.day_promote_count)){
                        tvNight.setText(tagBean.day_promote_count);
                    }   if(TextUtil.isNotEmpty(tagBean.all_promote_count)){
                        tvTen.setText(tagBean.all_promote_count);
                    }
                }

            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private void getBottenBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().user_supper_open.size() > 0) {
                        List<BannerItemDto> bannerList = result.getData().user_supper_open;
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "user_supper_open");
    }
    private void getBottenBanner1() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().user_invote_gift_left.size() > 0) {
                        List<BannerItemDto> bannerList = result.getData().user_invote_gift_left;

                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivOne, 5f,  bannerList.get(0).getPath());

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "user_invote_gift_left");
    }
    private void getBottenBanner2() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().user_invote_gift_right.size() > 0) {
                        List<BannerItemDto> bannerList = result.getData().user_invote_gift_right;
//                        GlideUtils.getInstances().loadUserRoundImg(getActivity(), ivTwo, bannerList.get(0).getPath());
                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivTwo, 5f,  bannerList.get(0).getPath());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "user_invote_gift_right");
    }
    private boolean isFisrt;
    @Override
    public void onResume() {
        super.onResume();
        if(isFisrt){

        }
        getUserInfo();
        getMemberInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getUserInfo();

        }
    }




    private String id;
    private int    state;
    private int    level;
    private int    states;
    private int    setPay_state;

    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                mPersonalInfoDto = personalInfoDto;
                id = personalInfoDto.getId();

                //                getShopInfo();
                if (mPersonalInfoDto != null) {
                    tvState.setVisibility(View.VISIBLE);
                    level=mPersonalInfoDto.level;
                    ivState.setVisibility(View.GONE);
                    if (mPersonalInfoDto.level==0) {
                        tvState.setText("注册会员");
                        state = 0;
                    } else  if (mPersonalInfoDto.level==1) {
                        tvState.setText("掌柜");
                        ivState.setVisibility(View.VISIBLE);
                        state = 1;

                    } else if (mPersonalInfoDto.level==2)  {
                        tvState.setText("导师");
                        ivState.setVisibility(View.GONE);
                    }

                    if (mPersonalInfoDto.wallet != null && mPersonalInfoDto.wallet.data != null) {
                        if (mPersonalInfoDto.wallet.data.isPay_password()) {
                            BaseApplication.isSetPay = 1;
                        } else {
                            BaseApplication.isSetPay = 0;
                        }
                    } else {
                        BaseApplication.isSetPay = 0;
                    }
                    ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
                    if (!TextUtils.isEmpty(personalInfoDto.getName())) {
                        ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getName());
                    } else {
                        ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());
                    }
                    setUserInfo();
                }

            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }
    private String wx="";
    private void setUserInfo() {
        GlideUtils.getInstances().loadUserRoundImg(getActivity(), civUserAvatar, mPersonalInfoDto.getAvatar());
        tvName.setText(mPersonalInfoDto.getName());
        if(TextUtil.isNotEmpty(mPersonalInfoDto.wechat_number)){
            wx=mPersonalInfoDto.wechat_number;
        }
        if(TextUtil.isNotEmpty(mPersonalInfoDto.getId())){
            tvCode.setText("邀请码:  "+mPersonalInfoDto.getPhone());
        }

    }

    @OnClick({R.id.civ_user_avatar

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_user_avatar:

                break;

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        resetUI();
    }

    /**
     * 退出登录 重置UI
     */
    private void resetUI() {

    }

}
