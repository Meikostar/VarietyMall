package com.smg.variety.view.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.R;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.AnchorInfo;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BannerItemDto;
import com.smg.variety.bean.BaseDto1;
import com.smg.variety.bean.CountOrderBean;
import com.smg.variety.bean.CountStatisticsBean;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.bean.ServiceMenuBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.eventbus.LogoutEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.CallPhoneUtil;
import com.smg.variety.utils.DialogUtils;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.utils.UserHelper;
import com.smg.variety.view.activity.AccountSettingActivity;
import com.smg.variety.view.activity.AppRenWuActivity;
import com.smg.variety.view.activity.AppStoresActivity;
import com.smg.variety.view.activity.AttentionActivity;
import com.smg.variety.view.activity.BankCardManagerActivity;
import com.smg.variety.view.activity.CollectActivity;
import com.smg.variety.view.activity.CommentCenterActivity;
import com.smg.variety.view.activity.HelpCenterActivity;
import com.smg.variety.view.activity.LiveCheckFailActivity;
import com.smg.variety.view.activity.LiveCheckingActivity;
import com.smg.variety.view.activity.LiverInfoActivity;
import com.smg.variety.view.activity.LoginActivity;
import com.smg.variety.view.activity.MessageCenterActivity;
import com.smg.variety.view.activity.MineCouPonActivity;
import com.smg.variety.view.activity.MyBuyGoodActivity;
import com.smg.variety.view.activity.MyEarningsActivity;
import com.smg.variety.view.activity.MyFootprintActivity;
import com.smg.variety.view.activity.MyPublishActivity;
import com.smg.variety.view.activity.MyQRcodeActivity;
import com.smg.variety.view.activity.OrderActivity;
import com.smg.variety.view.activity.RechargeWebActivity;
import com.smg.variety.view.activity.RefundAfterSalesActivity;
import com.smg.variety.view.activity.RequestLivePermissionActivity;
import com.smg.variety.view.activity.ShippingAddressActivity;
import com.smg.variety.view.activity.ShopCheckFailActivity;
import com.smg.variety.view.activity.ShopCheckingActivity;
import com.smg.variety.view.activity.StartLiveActivity;
import com.smg.variety.view.activity.StoreDetailActivity;
import com.smg.variety.view.activity.SuperMemberActivity;
import com.smg.variety.view.activity.UserInfoActivity;
import com.smg.variety.view.adapter.ServiceMenuAdapter;
import com.smg.variety.view.mainfragment.consume.BrandShopDetailActivity;
import com.smg.variety.view.mainfragment.consume.CommodityDetailActivity;
import com.smg.variety.view.widgets.CircleImageView;
import com.smg.variety.view.widgets.RedDotLayout;
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
public class MeFragment extends BaseFragment {


    Unbinder unbinder;
    @BindView(R.id.iv_setting)
    ImageView       ivSetting;
    @BindView(R.id.iv_user_msg)
    ImageView       ivUserMsg;
    @BindView(R.id.iv_user_red_point)
    ImageView       ivUserRedPoint;
    @BindView(R.id.rl_mine_user_info)
    RelativeLayout  rlMineUserInfo;
    @BindView(R.id.tv_sc)
    TextView        tvSc;
    @BindView(R.id.ll_sc)
    LinearLayout    llSc;
    @BindView(R.id.tv_gz)
    TextView        tvGz;
    @BindView(R.id.ll_gz)
    LinearLayout    llGz;
    @BindView(R.id.tv_zj)
    TextView        tvZj;
    @BindView(R.id.ll_zj)
    LinearLayout    llZj;
    @BindView(R.id.iv_order_daifukuan)
    ImageView       ivOrderDaifukuan;
    @BindView(R.id.re_order_daifukuan)
    RedDotLayout    reOrderDaifukuan;
    @BindView(R.id.ll_order_daifukuan)
    LinearLayout    llOrderDaifukuan;
    @BindView(R.id.iv_order_daifahuo)
    ImageView       ivOrderDaifahuo;
    @BindView(R.id.re_order_daifahuo)
    RedDotLayout    reOrderDaifahuo;
    @BindView(R.id.ll_order_daifahuo)
    LinearLayout    llOrderDaifahuo;
    @BindView(R.id.iv_order_daishouhuo)
    ImageView       ivOrderDaishouhuo;
    @BindView(R.id.re_order_daishouhuo)
    RedDotLayout    reOrderDaishouhuo;
    @BindView(R.id.ll_order_daishouhuo)
    LinearLayout    llOrderDaishouhuo;
    @BindView(R.id.iv_order_daipingjia)
    ImageView       ivOrderDaipingjia;
    @BindView(R.id.re_order_daipingjia)
    RedDotLayout    reOrderDaipingjia;
    @BindView(R.id.ll_order_daipingjia)
    LinearLayout    llOrderDaipingjia;
    @BindView(R.id.iv_order_tuikuan)
    ImageView       ivOrderTuikuan;
    @BindView(R.id.re_order_tuikuan)
    RedDotLayout    reOrderTuikuan;
    @BindView(R.id.ll_refund_after_sales)
    LinearLayout    llRefundAfterSales;
    @BindView(R.id.ll_bg)
    LinearLayout    llBg;
    @BindView(R.id.iv_banner)
    Banner          banner;
    @BindView(R.id.ll_dp)
    LinearLayout    llDp;
    @BindView(R.id.ll_ewm)
    LinearLayout    llEwm;
    @BindView(R.id.ll_pj)
    LinearLayout    llPj;
    @BindView(R.id.ll_sm)
    LinearLayout    llSm;
    @BindView(R.id.ll_yh)
    LinearLayout    llYh;
    @BindView(R.id.ll_dz)
    LinearLayout    llDz;
    @BindView(R.id.ll_wt)
    LinearLayout    llWt;
    @BindView(R.id.ll_kf)
    LinearLayout    llKf;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.iv_state)
    ImageView       ivState;
    @BindView(R.id.tv_state)
    TextView        tvState;
    @BindView(R.id.tv_code)
    TextView        tvCode;
    @BindView(R.id.rl_bg)
    RelativeLayout  rlBg;

    private PersonalInfoDto mPersonalInfoDto;
    ServiceMenuAdapter menuAdapter;

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
        return R.layout.fragment_me;
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
        rlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SuperMemberActivity.class);
                intent.putExtra("level",level);
                startActivity(intent);
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
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 2, imgStr, R.mipmap.img_default_3);
                } else {
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 2, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_3);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
                    if (slidersDto.getClick_event_type().equals("product_default")) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                        gotoActivity(CommodityDetailActivity.class, bundle);
                    } else if (slidersDto.getClick_event_type().equals("seller_default")) {
                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
                        intent.putExtra("id", slidersDto.getClick_event_value());
                        context.startActivity(intent);
                    }
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
    }

    @Override
    protected void initListener() {

    }

    private void getBottenBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().user_center_center.size() > 0) {
                        List<BannerItemDto> bannerList = result.getData().user_center_center;
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "user_center_center");
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getUserInfo();
            getMenu();
        }
    }

    private void userCountStatistics() {
        DataManager.getInstance().userCountStatistics(new DefaultSingleObserver<HttpResult<CountStatisticsBean>>() {
            @Override
            public void onSuccess(HttpResult<CountStatisticsBean> countStatisticsBean) {
                if (countStatisticsBean != null && countStatisticsBean.getData() != null) {
                    if (!TextUtils.isEmpty(countStatisticsBean.getData().favorites_count))
                        tvSc.setText(countStatisticsBean.getData().favorites_count);
                    if (!TextUtils.isEmpty(countStatisticsBean.getData().followings_count))
                        tvGz.setText(countStatisticsBean.getData().followings_count);
                    if (!TextUtils.isEmpty(countStatisticsBean.getData().footprints_count))
                        tvZj.setText(countStatisticsBean.getData().footprints_count);

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    //    private String paid; //待发货
    //    private String shipped;//待评价
    //    private String shipping;//待收货
    //    private String created;//待付款
    private void getAllUserOrdersCount() {
        DataManager.getInstance().getAllUserOrdersCount(new DefaultSingleObserver<HttpResult<CountOrderBean>>() {
            @Override
            public void onSuccess(HttpResult<CountOrderBean> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {
                    CountOrderBean data = countOrderBean.getData();
                    if (TextUtil.isNotEmpty(data.getPaid())) {
                        reOrderDaifahuo.setText(data.getPaid());
                    } else {
                        reOrderDaifahuo.setText("");
                    }
                    if (TextUtil.isNotEmpty(data.getShipped())) {
                        reOrderDaipingjia.setText(data.getShipped());
                    } else {
                        reOrderDaipingjia.setText("");
                    }
                    if (TextUtil.isNotEmpty(data.getShipping())) {
                        reOrderDaishouhuo.setText(data.getShipping());
                    } else {
                        reOrderDaishouhuo.setText("");
                    }
                    if (TextUtil.isNotEmpty(data.getCreated())) {
                        reOrderDaifukuan.setText(data.getCreated());
                    } else {
                        reOrderDaifukuan.setText("");
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast("");
            }
        });
    }

    private void getAlOrdersRefundCount() {
        DataManager.getInstance().getAlOrdersRefundCount(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {
                    if (Double.valueOf(countOrderBean.getData()) != 0) {
                        reOrderTuikuan.setText(countOrderBean.getData());
                    } else {
                        reOrderTuikuan.setText("");
                    }

                }

            }

            @Override
            public void onError(Throwable throwable) {


            }
        });
    }


    private String   id;
    private int      state;
    private int      states;
    private int      level;
    private int      setPay_state;
    private BaseDto1 seller;

    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                mPersonalInfoDto = personalInfoDto;
                id = personalInfoDto.getId();
                userCountStatistics();
                getAllUserOrdersCount();
                getAlOrdersRefundCount();
                BaseApplication.is_new=personalInfoDto.is_new_pull==null?1:Integer.valueOf(personalInfoDto.is_new_pull);
                seller=personalInfoDto.seller;
                //                getShopInfo();
                if (mPersonalInfoDto != null) {
                    level=mPersonalInfoDto.level;
                    tvState.setVisibility(View.VISIBLE);
                    tvCode.setText("邀请码:  "+mPersonalInfoDto.getPhone());
                    ivState.setVisibility(View.GONE);
                    if (mPersonalInfoDto.level == 0) {
                        tvState.setText("注册会员");
                        states = 0;
                    } else if (mPersonalInfoDto.level == 1) {
                        tvState.setText("掌柜");
                        ivState.setVisibility(View.VISIBLE);
                        states = 1;

                    } else if (mPersonalInfoDto.level == 2) {
                        tvState.setText("导师");
                        ivState.setVisibility(View.GONE);
                    }


                    if(mPersonalInfoDto.real_name!=null&&mPersonalInfoDto.real_name.data!=null&&mPersonalInfoDto.real_name.data.status!=null){
                        BaseApplication.real_state=mPersonalInfoDto.real_name.data.status;
                        if(mPersonalInfoDto.real_name.data.status.equals("1")&&!TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                            tvStates="已认证";
                            state=1;
                        }else if(mPersonalInfoDto.real_name.data.status.equals("0")&&TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                            tvStates="认证中";
                            state=1;

                        }else if(mPersonalInfoDto.real_name.data.status.equals("2")&&!TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                            tvStates="认证失败";
                            state=0;
                        }else if(mPersonalInfoDto.real_name.data.status.equals("-1")&&TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                            tvStates="未认证";
                            state=0;
                        }else {
                            tvStates="认证中";
                            state=1;
                        }
                    }else {
                        state=0;

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
    private void isPlayer() {
        //showLoadDialog();
        DataManager.getInstance().isliveing(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto result) {
                //dissLoadDialog();


                if (result != null) {

                    if (result.is_realname) {
                        if (result.apply_check_status == 0) {
                            applyLives();

                            //                            gotoActivity(RequestLivePermissionActivity.class);
                        } else if (result.apply_check_status == 1) {
                            gotoActivity(LiveCheckingActivity.class);

                        } else if (result.apply_check_status == 2) {
                            if (result.is_live) {
                                gotoActivity(StartLiveActivity.class);
                            } else {

                            }

                        } else if (result.apply_check_status == 3) {
                            Bundle bundle = new Bundle();
                            bundle.putString("reasonTip", "");
                            gotoActivity(LiveCheckFailActivity.class, false, bundle);


                        }

                    } else {
                        if (BaseApplication.real_state.equals("-1")) {
                            gotoActivity(RequestLivePermissionActivity.class);
                        } else if (BaseApplication.real_state.equals("0")) {
                            ToastUtil.showToast("实名认证中，请耐心等待审核!");
                        } else if (BaseApplication.real_state.equals("1")) {

                        } else if (BaseApplication.real_state.equals("2")) {
                            gotoActivity(RequestLivePermissionActivity.class);
                        }

                    }


                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                if (ApiException.getInstance().getCode() == 500) {
                    gotoActivity(RequestLivePermissionActivity.class);
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        });
    }
    public void applyLives() {
        //        showLoadDialog();
        DataManager.getInstance().applyLive(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                //                dissLoadDialog();
                gotoActivity(LiveCheckingActivity.class);

                //                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    gotoActivity(LiveCheckingActivity.class);
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                //                dissLoadDialog();
            }
        });
    }
    private void setUserInfo() {
        GlideUtils.getInstances().loadUserRoundImg(getActivity(), civUserAvatar, mPersonalInfoDto.getAvatar());
        tvName.setText(mPersonalInfoDto.getName());
    }

    @OnClick({R.id.civ_user_avatar
            , R.id.iv_user_msg
            , R.id.iv_setting
            , R.id.ll_gz
            , R.id.ll_sc
            , R.id.ll_zj
            , R.id.ll_sm
            , R.id.ll_dp
            , R.id.ll_kf
            , R.id.ll_yh
            , R.id.ll_ewm
            , R.id.ll_pj
            , R.id.ll_wt
            , R.id.ll_order_daifukuan
            , R.id.ll_order_daifahuo
            , R.id.ll_order_daishouhuo
            , R.id.ll_order_daipingjia
            , R.id.ll_refund_after_sales
            , R.id.ll_dz
            , R.id.ll_bg
            , R.id.ll_mine_live
            , R.id.ll_sxy
            , R.id.ll_qd
            , R.id.ll_qb
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_mine_live:
                gotoActivity(LiverInfoActivity.class);
//                isPlayer();
                break;
            case R.id.ll_sxy:
                gotoActivity(MineCouPonActivity.class);

                break;
            case R.id.ll_qd:
                gotoActivity(AppRenWuActivity.class);


                break;
            case R.id.ll_qb:
                gotoActivity(MyEarningsActivity.class);
                break;
            case R.id.civ_user_avatar:
                clickUserInfo();
                break;
            case R.id.iv_user_msg://消息
                if (UserHelper.isLogin(getActivity())) {
                    gotoActivity(MessageCenterActivity.class);
                }
                break;
            case R.id.iv_setting://设置

                gotoActivity(AccountSettingActivity.class);
                break;
            case R.id.ll_gz://关注
                gotoActivity(AttentionActivity.class);
                break;
            case R.id.ll_sc://关注
                gotoActivity(CollectActivity.class);
                break;
            case R.id.ll_zj://关注
                gotoActivity(MyFootprintActivity.class);
                break;
            case R.id.ll_sm://实名
                if (state == 0) {
                    gotoActivity(RequestLivePermissionActivity.class);
                } else {
                    ToastUtil.showToast(tvStates);
                }

                break;
            case R.id.ll_dp://店铺
              if(seller==null){
                  gotoActivity(StoreDetailActivity.class);
              }else {
                 if(seller.data.status!=null){
                     if(seller.data.status.equals("1")){
                         gotoActivity(ShopCheckingActivity.class);
                     }else if(seller.data.status.equals("2")){
                         gotoActivity(AppStoresActivity.class);
                       //审核成功
                     }else if(seller.data.status.equals("3")){
                         gotoActivity(ShopCheckFailActivity.class);
                     }else if(seller.data.status.equals("4")){
                         ToastUtil.showToast("已禁用！请与管理员联系");
//                         gotoActivity(ShopCheckFailActivity.class);
                     }
                 }else {
                     gotoActivity(StoreDetailActivity.class);

                 }
              }
//              ToastUtil.showToast("暂未开放");
                break;
            case R.id.ll_kf://客服
                showCallCenterDialog();
                break;
            case R.id.ll_yh://银行卡
                gotoActivity(BankCardManagerActivity.class);
                break;
            case R.id.ll_ewm://银行卡
                gotoActivity(MyQRcodeActivity.class);
                break;
            case R.id.ll_pj://银行卡
                gotoActivity(CommentCenterActivity.class);
                break;
            case R.id.ll_wt://银行卡
                gotoActivity(HelpCenterActivity.class);
//                gotoActivity(MyEarningsActivity.class);
                break;


            //            case R.id.ll_mine_footprint://我的足迹
            //
            //                break;
            //            case R.id.ll_mine_issue://发布
            //                gotoActivity(MyIssueActivity.class);
            //                break;
            //            case R.id.ll_mine_comment://评论
            //                gotoActivity(CommentCenterActivity.class);
            //                break;
            //            case R.id.ll_mine_buy://我买到的
            //                goToMyBuyGoodActivity(0);
            //                break;
            //            case R.id.ll_mine_sell://我卖出的
            //                goToMyBuyGoodActivity(1);
            //                break;
            //            case R.id.ll_mine_publish://我发布的
            //                goToMyPublishActivity();
            //                break;

            //            case R.id.tv_mine_all_order://全部订单
            //                gotoOrderActivity(0);
            //                break;
            case R.id.ll_order_daifukuan://待付款
                gotoOrderActivity(1);
                break;
            case R.id.ll_bg://待付款
                gotoOrderActivity(0);

                break;
            case R.id.ll_order_daifahuo://待发货
                gotoOrderActivity(2);
                break;
            case R.id.ll_order_daishouhuo://待收货
                gotoOrderActivity(3);
                break;
            case R.id.ll_order_daipingjia://待评价
                gotoOrderActivity(4);
                break;
            case R.id.ll_refund_after_sales://退款售后
                gotoActivity(RefundAfterSalesActivity.class);
                break;
            case R.id.ll_dz://银行卡
                gotoActivity(ShippingAddressActivity.class);
                break;
            //            case R.id.ll_mine_earnings://收益
            //                bindClickIsLoginJumpUiEvent(MyEarningsActivity.class);
            //                break;
            //            case R.id.ll_mine_integral_balance://积分余额
            //                bindClickIsLoginJumpUiEvent(IntegralBalanceActivity.class);
            //                break;
            //            case R.id.ll_mine_card_bag://我的卡包
            //                bindClickIsLoginJumpUiEvent(MyCardBagActivity.class);
            //                break;
            //            //            case R.id.ll_mine_help_center://帮助中心
            //            //                gotoActivity(HelpCenterActivity.class);
            //            //                break;
            //            case R.id.ll_mine_qrcode:
            //                bindClickIsLoginJumpUiEvent(MyQRcodeActivity.class);
            //                break;
            //            case R.id.iv_mine_gwc:
            //                bindClickIsLoginJumpUiEvent(ShopCartActivity.class);
            //                break;
        }
    }

    String phone = "4006008079";
    private  String tvStates = "";
    private void showCallCenterDialog() {
        if(TextUtils.isEmpty(phone)){
            return;
        }
        DialogUtils.showCallCenterDialog(getActivity(),phone, new DialogUtils.OnClickDialogListener() {
            @Override
            public void onClick(View v) {

                callPhone(phone);
            }
        });
    }
    /**
     * 拨打电话
     *
     * @param phone
     */
    private void callPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }

        CallPhoneUtil.diallPhone(getActivity(), phone);
    }

    private void gotoOrderActivity(int page) {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra("page", page);
        startActivity(intent);
    }

    private void goToMyBuyGoodActivity(int i) {
        Intent intent = new Intent(getActivity(), MyBuyGoodActivity.class);
        intent.putExtra("type", i);
        startActivity(intent);
    }

    private void goToMyPublishActivity() {
        Intent intent = new Intent(getActivity(), MyPublishActivity.class);
        startActivity(intent);
    }

    private void clickUserInfo() {
        if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
            gotoActivity(LoginActivity.class);
        } else {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            startActivityForResult(intent, 100);

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
        GlideUtils.getInstances().loadUserRoundImg(getActivity(), civUserAvatar, "");
        tvName.setText("");

        tvSc.setText("");
        tvGz.setText("");
        tvZj.setText("");
        reOrderTuikuan.setText("");
        reOrderDaifahuo.setText("");
        reOrderDaipingjia.setText("");
        reOrderDaishouhuo.setText("");
        reOrderDaifukuan.setText("");

    }

    private void getMenu() {
        DataManager.getInstance().serviceMenu(new DefaultSingleObserver<HttpResult<List<ServiceMenuBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<ServiceMenuBean>> httpResult) {
                menuAdapter.setNewData(httpResult.getData());
            }
        });
    }
}