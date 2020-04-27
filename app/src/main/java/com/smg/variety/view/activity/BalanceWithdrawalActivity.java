package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BankCardDto;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.StatusBarUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.widgets.ConfigPasswordDialog;
import com.smg.variety.view.widgets.autoview.ActionbarView;
import com.smg.variety.view.widgets.autoview.ClearEditText;
import com.smg.variety.view.widgets.autoview.ObservableScrollView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 余额提现
 */
public class BalanceWithdrawalActivity extends BaseActivity {
    @BindView(R.id.all_bank_care_rl)
    RelativeLayout       all_bank_care_rl;
    @BindView(R.id.custom_action_bar)
    ActionbarView        customActionBar;
    @BindView(R.id.agricultural_bank_logo_iv)
    ImageView            agriculturalBankLogoIv;
    @BindView(R.id.bank_number_tv)
    TextView             bankNumberTv;
    @BindView(R.id.bank_name_tv)
    TextView             bank_name_tv;
    @BindView(R.id.cardholder_tv)
    TextView             cardholder_tv;
    @BindView(R.id.total_balance_tv)
    TextView             total_balance_tv;
    @BindView(R.id.full_cash_withdrawal_tv)
    TextView             fullCashWithdrawalTv;
    @BindView(R.id.et_login_phone)
    ClearEditText        etLoginPhone;
    @BindView(R.id.sure_btn)
    Button               sureBtn;
    @BindView(R.id.rl_me_scroll_view)
    ObservableScrollView rlMeScrollView;
    @BindView(R.id.ll_tx)
    LinearLayout         llTx;
    private boolean     isSelAddress;
    private BankCardDto bankCardDto;
    private double      money;

    @Override
    public void initListener() {
        llTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_balance_withdrawal;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        actionbar.setTitle("提现");
        actionbar.setTextAction("添加银行卡", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(BalanceWithdrawalActivity.this, BankCardManagerActivity.class), 0x101);

            }
        });
        actionbar.setTextActionColor(getResources().getColor(R.color.my_color_212121));

    }

    @Override
    public void initData() {
        money = getIntent().getDoubleExtra("money", 0);
        total_balance_tv.setText(String.format("%s%s%s", "当前可提现余额", money, "元"));
        loadData();
        getDefaultCard();
    }

    @OnClick({R.id.all_bank_care_rl, R.id.full_cash_withdrawal_tv, R.id.sure_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.full_cash_withdrawal_tv:
                etLoginPhone.setText(money + "");
                break;
            case R.id.all_bank_care_rl:
                startActivityForResult(new Intent(BalanceWithdrawalActivity.this, BankCardManagerActivity.class).putExtra("selCard", true), 0x102);
                break;
            case R.id.sure_btn:
                if (bankCardDto == null || TextUtil.isEmpty(bankCardDto.getNumber())) {
                    ToastUtil.showToast("请添加银行卡");
                    return;
                }
                String trim = etLoginPhone.getText().toString().trim();
                if (TextUtil.isEmpty(trim) || trim.equals("0")) {
                    ToastUtil.showToast("提现金额");
                    return;
                }
                try {
                    double trimMonet = Double.parseDouble(trim);
                    if (money <= 0 || trimMonet <= 0 || trimMonet > money) {
                        ToastUtil.showToast("提现金额不足");
                        return;
                    }

                    new ConfigPasswordDialog(BalanceWithdrawalActivity.this, new ConfigPasswordDialog.ConfigPasswordListener() {
                        @Override
                        public void callbackPassword(String password) {
                            withdraw(trimMonet, password);
                        }
                    }).show();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtil.showToast("失败");
                }
                break;
        }
    }

    //提现
    private void withdraw(double trimMonet, String password) {
        Map map = new HashMap();
        map.put("card_id", bankCardDto.getId());
        map.put("money", trimMonet);
        //        map.put("pay_password", MD5Utils.getMD5Str(password));
        map.put("pay_password", password);

        DataManager.getInstance().withdraw(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object balanceDto) {
                refresh(trimMonet);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    refresh(trimMonet);
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }

    private void refresh(double trimMonet) {


    }

    private void getDefaultCard() {
        DataManager.getInstance().getBankCardDefault(new DefaultSingleObserver<HttpResult<BankCardDto>>() {
            @Override
            public void onSuccess(HttpResult<BankCardDto> result) {
                bankCardDto = result.getData();
                setCardView();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
            }
        });
    }

    private void setCardView() {
        if (bankCardDto != null && !TextUtil.isEmpty(bankCardDto.getNumber())) {
            all_bank_care_rl.setVisibility(View.VISIBLE);
            cardholder_tv.setText(String.format("%s  %s", "持卡人", bankCardDto.getName()));
            String typeName = "储蓄卡";
            if ("CC".equals(bankCardDto.getType())) {
                typeName = "信用卡";
            }
            String bankName = bankCardDto.getOrg();
            bank_name_tv.setText(bankName + "(" + typeName + ")");

            String cardNumber = bankCardDto.getNumber();
            if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() > 5) {
                int cardLength = cardNumber.length();
                cardNumber = cardNumber.substring(cardLength - 4, cardLength);
                cardNumber = "**** **** **** " + cardNumber;
            }
            bankNumberTv.setText(cardNumber);
            if (!TextUtil.isEmpty(bankCardDto.getBank_logo())) {
                String bankLogo = Constants.WEB_IMG_URL_CID + bankCardDto.getBank_logo();
                GlideUtils.getInstances().loadNormalImg(this, agriculturalBankLogoIv, bankLogo, R.mipmap.bank_card_icon_normal);
            }
            String org = bankCardDto.getCode();
            if (!TextUtils.isEmpty(org)) {
                if (org.equals("ABC") || org.equals("BOC") || org.equals("CCB") || org.equals("CMB") || org.equals("ICBC")) {
                    String mDrawableName = "icon_" + toLowerCase(org);
                    int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    all_bank_care_rl.setBackground(getResources().getDrawable(resID));
                } else if (org.equals("COOM") || org.equals("BOCM") || org.equals("BOCOM")) {
                    //                        imageView.setImageResource(R.drawable.icon_coom);
                    all_bank_care_rl.setBackground(getResources().getDrawable(R.drawable.icon_coom));
                } else {
                    //                        imageView.setImageResource(R.drawable.icon_ty);
                    all_bank_care_rl.setBackground(getResources().getDrawable(R.drawable.icon_ty));
                }
            } else {
                //                    imageView.setImageResource(R.drawable.icon_ty);
                all_bank_care_rl.setBackground(getResources().getDrawable(R.drawable.icon_ty));
            }

        } else {
            ToastUtil.showToast("请添加银行卡");
        }
    }

    //该函数用来将大写字母转换为小写字母
    public static String toLowerCase(String str) {

        if (str == null) { //字符串为空时
            return null;
        }

        //创建一个字符数组，保存转换之后的结果
        char[] array = new char[str.length()];

        //当字符串不为空时，遍历字符串的每一个字符，将大写转换为小写，小写保持不变
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 65 && str.charAt(i) <= 90) {
                char newChar = (char) (str.charAt(i) + 32);
                array[i] = newChar;
            } else {
                array[i] = (char) str.charAt(i);
            }
        }
        return new String(array);
    }

    /*
     *当前登陆用户信息
     */
    private void loadData() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                if (personalInfoDto != null) {

                    total_balance_tv.setText(String.format("%s%s%s", "当前可提现余额", money, "元"));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x102:
                if (data != null) {
                    BankCardDto bankCardDto = (BankCardDto) data.getSerializableExtra("cardItem");
                    if (bankCardDto != null) {
                        this.bankCardDto = bankCardDto;
                        setCardView();
                    }

                }

                break;
            case 0x101:
                if (resultCode == -1) {
                    getDefaultCard();
                }
                break;
        }

    }


}
