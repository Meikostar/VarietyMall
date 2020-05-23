package com.smg.variety.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.bean.CheckOutOrderResult;
import com.smg.variety.bean.WEIXINREQ;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.PayUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.SettingPasswordActivity;
import com.smg.variety.view.fragments.PayResultListener;
import com.smg.variety.view.mainfragment.consume.ConfirmOrderActivity;
import com.smg.variety.view.widgets.InputPwdDialog;
import com.smg.variety.view.widgets.MCheckBox;
import com.smg.variety.view.widgets.PhotoPopupWindow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {
    public static final int CHECK_BANK_CARD = 101;

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_recharge_money)
    EditText etRechargeMoney;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        mTitleText.setText("充值");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void moneyRecharges() {
        if (TextUtils.isEmpty(etRechargeMoney.getText().toString().trim())) {
            ToastUtil.showToast("请输入充值金额");
            return;
        }
        closeKeyBoard();
        money=etRechargeMoney.getText().toString().trim();
        showPopPayWindows();
    }

    @OnClick({R.id.iv_title_back
            , R.id.tv_recharge_affirm
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_recharge_affirm://确认充值

                moneyRecharges();
                break;
        }
    }

    public void showPopPayWindows() {

        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.pay_popwindow_view, null);

            btSure = view.findViewById(R.id.bt_sure);
            lines = view.findViewById(R.id.lines);
            iv_close = view.findViewById(R.id.iv_close);
            llBalance = view.findViewById(R.id.ll_balance);
            llZfb = view.findViewById(R.id.ll_zfb);
            llWx = view.findViewById(R.id.ll_wx);
            mcbBalance = view.findViewById(R.id.mcb_balance);
            mcbZfb = view.findViewById(R.id.mcb_zfb);
            mcbWx = view.findViewById(R.id.mcb_wx);
            tv_balance = view.findViewById(R.id.tv_balance);

            llBalance.setVisibility(View.GONE);
            lines.setVisibility(View.GONE);
            llWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 1;
                    setChoose(1);
                }
            });
            llZfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 2;
                    setChoose(2);
                }
            });
            btSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state) {


                        case 1:
                            submitWxOrder();
                            break;
                        case 2:
                            submitOrder();
                            break;
                    }
                    mWindowAddPhoto.dismiss();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhoto.dismiss();
                }
            });

            mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
            mWindowAddPhoto.showAtLocation(mTitleText, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhoto.showAtLocation(mTitleText, Gravity.BOTTOM, 0, 0);
        }


    }


    /**
     * zfb
     */
    private void submitOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        map.put("money", money);
        map.put("platform", "alipay");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitZfbRecharge(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> httpResult) {
                dissLoadDialog();
                if (httpResult != null && !TextUtils.isEmpty(httpResult.getData())) {
                    PayUtils.getInstances().zfbPaySync(RechargeActivity.this, httpResult.getData(), new PayResultListener() {
                        @Override
                        public void zfbPayOk(boolean payOk) {
                            Intent intent = new Intent(RechargeActivity.this, PaySuccessActivity.class);

                            if (payOk) {
                                ToastUtil.showToast("充值成功");
                                finish();
                            } else {
                                ToastUtil.showToast("支付已取消");

                                finish();
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

    private int RQ_WEIXIN_PAY = 12;
    private int RQ_PAYPAL_PAY = 16;
    private int RQ_ALIPAY_PAY = 10;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RQ_WEIXIN_PAY) {
                ToastUtil.showToast("充值成功");


                finish();
                //           if (requestCode == RQ_WEIXIN_PAY) {
                //                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
                //            }
            }

        }
    }

    private String money;

    /**
     * wx
     */
    private void submitWxOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;

        map.put("money", money);

        map.put("platform", "wechat");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney


        DataManager.getInstance().submitWxRecharge(new DefaultSingleObserver<HttpResult<WEIXINREQ>>() {
            @Override
            public void onSuccess(HttpResult<WEIXINREQ> httpResult) {
                dissLoadDialog();
                PayUtils.getInstances().WXPay(RechargeActivity.this, httpResult.getData().pay_params);

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }

    private View mView;

    private RecyclerView     recyclerView;
    private Button           btSure;
    private View             lines;
    private TextView         tv_title;
    private ImageView        iv_close;
    private View             view;
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

    private int              state;
    private PhotoPopupWindow mWindowAddPhoto;
}
