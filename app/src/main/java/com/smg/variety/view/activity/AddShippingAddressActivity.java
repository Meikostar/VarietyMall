package com.smg.variety.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.bean.AddressModel;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.StringUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.utils.RegexUtils;
import com.smg.variety.view.widgets.MCheckBox;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增收货地址
 */
public class AddShippingAddressActivity extends BaseActivity {

    public static String areaName = "";
    public static String areaId = "";

    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.tv_where_address)
    TextView  mAddress;
    @BindView(R.id.et_name)
    EditText  mName;
    @BindView(R.id.et_phone)
    EditText  mPhone;
    @BindView(R.id.et_detail_address)
    EditText  mDetailAddress;
    @BindView(R.id.et_code)
    EditText  et_code;

    @BindView(R.id.cb_default_address)
    MCheckBox mCheckBox;
    private AddressDto mAddressDto;
    private String mAddressId;
    private boolean  isEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_shipping_address;
    }
    private int tag;
    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tag=getIntent().getIntExtra("tag",0);
            mAddressDto = (AddressDto) bundle.getSerializable("address");
            if(mAddressDto!=null){
                mTitleText.setText("修改收货地址");
                isEdit  =true;
                mAddressId = mAddressDto.getId()+"";
                mName.setText(mAddressDto.getName());
                mPhone.setText(mAddressDto.getMobile());
                mAddress.setText(mAddressDto.getArea());
                mDetailAddress.setText(mAddressDto.getDetail());
                mCheckBox.setChecked("1".equals(mAddressDto.getIs_default()));

                if (!TextUtils.isEmpty(mAddressDto.getArea_ids()) && mAddressDto.getArea_ids().indexOf(",") > 0) {
                    String areaIdArray[] = mAddressDto.getArea_ids().split(",");
                    if (areaIdArray != null && areaIdArray.length > 0) {
                        int position = areaIdArray.length - 1;
                        areaId = areaIdArray[position];
                    }
                }
            }
        } else {
            mTitleText.setText("新增收货地址");
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {


    }

    @OnClick({  R.id.iv_title_back
            , R.id.rl_address_area_container
            , R.id.tv_confirm_address
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.rl_address_area_container:
                gotoActivity(AreaListActivity.class, false, null, Constants.INTENT_REQUESTCODE_AREA);
                break;
            case R.id.tv_confirm_address:
                confirm();
                break;
        }
    }

    private void confirm() {
        String name = mName.getText().toString();
        String mobile = mPhone.getText().toString();
        String detail = mDetailAddress.getText().toString();
        String area = mAddress.getText().toString();
//        String card = et_code.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入收货人的姓名", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
//        if (TextUtils.isEmpty(et_code.getText().toString().trim())) {
//            ToastUtil.showToast("请输入身份证号码");
//            return;
//        }
//        try {
//            if (!StringUtil.IDCardValidate(et_code.getText().toString().trim())) {
//                ToastUtil.showToast("请输入正确的身份证");
//                return;
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        if(!RegexUtils.isMobileExact(mobile)){
            ToastUtil.showToast("请输入合法手机号");
            return;
        }
//        if (mobile.length()!=11) {
//            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(this, "请选择省区", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            Toast.makeText(this, "请输入地区", Toast.LENGTH_LONG).show();
            return;
        }

        AddressModel addressModel = new AddressModel();
        addressModel.setName(name);
        addressModel.setMobile(mobile);
//        addressModel.setId_card_no(card);
        addressModel.setArea_id(areaId);
        addressModel.setDetail(detail);
        if (mCheckBox.isCheck()) {
            addressModel.setIs_default("1");
        } else {
            addressModel.setIs_default("0");
        }
        if(isEdit){
            updateAddress(addressModel);
        }else {
            addAddress(addressModel);
        }
    }

    private void addAddress(AddressModel addressModel) {
        showLoadDialog();
        DataManager.getInstance().addAddressesList(new DefaultSingleObserver<AddressDto>() {
            @Override
            public void onSuccess(AddressDto addressDtos) {
                dissLoadDialog();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, addressModel);
    }

    private void updateAddress(AddressModel addressModel) {
        showLoadDialog();
        DataManager.getInstance().updateAddresses(new DefaultSingleObserver<AddressDto>() {
            @Override
            public void onSuccess(AddressDto addressDtos) {
                dissLoadDialog();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, addressModel,mAddressId);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_REQUESTCODE_AREA && resultCode == Activity.RESULT_OK) {
            String allName = data.getStringExtra("areaName");
            areaId = data.getStringExtra("areaId");
            mAddress.setText(allName);
        }
    }
}
