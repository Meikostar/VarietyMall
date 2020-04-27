package com.smg.variety.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.adapter.ShippingAddressAdapter;
import com.smg.variety.view.widgets.swipemenu.SwipeMenu;
import com.smg.variety.view.widgets.swipemenu.SwipeMenuCreator;
import com.smg.variety.view.widgets.swipemenu.SwipeMenuItem;
import com.smg.variety.view.widgets.swipemenu.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收货地址
 */
public class ShippingAddressActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView          mTitleText;
    @BindView(R.id.tv_title_right)
    TextView          mTitleRight;
    @BindView(R.id.listview)
    SwipeMenuListView mListView;
    private List<AddressDto> mAddressDatas = new ArrayList<>();
    private ShippingAddressAdapter mAddressAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shipping_address;
    }
     private int tag;
    @Override
    public void initView() {
        tag=getIntent().getIntExtra("tag",0);
        mTitleText.setText("收货地址");
    }

    @Override
    public void initData() {
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressListData();
    }

    private void getAddressListData() {
        showLoadDialog();
        DataManager.getInstance().getAddressesList(new DefaultSingleObserver<List<AddressDto>>() {
            @Override
            public void onSuccess(List<AddressDto> addressDtos) {
                dissLoadDialog();
                mAddressDatas.clear();
                mAddressDatas.addAll(addressDtos);
                mAddressAdapter.notifyDataSetChanged();
                if(mAddressDatas.size() > 0)
                    for (int i = 0; i < mAddressDatas.size(); i++) {
                        if(mAddressDatas.get(i).getIs_default().equals("1")){
                            ShareUtil.getInstance().save(Constants.ADDRESS_ID, String.valueOf(mAddressDatas.get(i).getId()));
                        }
                    }
                }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.toast(throwable.getMessage());
            }
        });
    }

    @Override
    public void initListener() {

    }

    private void initListView() {

        mAddressAdapter = new ShippingAddressAdapter(this, mAddressDatas);
        mAddressAdapter.setItemClickListener(new ShippingAddressAdapter.OnItemClickListeners() {
            @Override
            public void itemClick(AddressDto addressDto) {
                if(tag==1){

                    Intent intent = new Intent();
                    intent.putExtra("result", addressDto);
                    ShippingAddressActivity.this.setResult(RESULT_OK, intent);
                    //关闭Activity
                    ShippingAddressActivity.this.onBackPressed();
                }else {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address",addressDto);

                    Intent intent = new Intent(ShippingAddressActivity.this, AddShippingAddressActivity.class);
                    intent.putExtras(bundle);
                   startActivity(intent);
                }

            }
        });
        mListView.setAdapter(mAddressAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem delete = new SwipeMenuItem(ShippingAddressActivity.this);
                delete.setBackground(new ColorDrawable(Color.rgb(0xdf, 0x44, 0x3b)));
                delete.setWidth((int) getResources().getDimension(R.dimen.bj_60dp));
                delete.setTitle("删除");
                delete.setId(1);
                delete.setTitleSize(13);
                delete.setTitleColor(Color.WHITE);
                SwipeMenuItem bj = new SwipeMenuItem(ShippingAddressActivity.this);
                bj.setBackground(new ColorDrawable(Color.rgb(0xaa, 0xaa, 0xaa)));
                bj.setWidth((int) getResources().getDimension(R.dimen.bj_60dp));
                bj.setTitle("编辑");
                bj.setTitleSize(13);
                bj.setId(-1);
                bj.setTitleColor(Color.WHITE);
                menu.addMenuItem(bj);
                menu.addMenuItem(delete);
            }

        };
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AddressDto addressDto = mAddressDatas.get(position);

                if(menu.getMenuItem(index).getId()==1){
                    delAddress(String.valueOf(addressDto.getId()));
                }else {
                    Intent intent = new Intent(ShippingAddressActivity.this,AddShippingAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("address",addressDto);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                return false;
            }
        });
    }

    private void delAddress(String id) {
        showLoadDialog();
        DataManager.getInstance().delAddresses(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object s) {
                dissLoadDialog();
                ToastUtil.showToast("删除成功");
                getAddressListData();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("删除成功");
                    getAddressListData();
                }else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, id);
    }

    @OnClick({  R.id.iv_title_back
            , R.id.tv_add_shipping_address
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_add_shipping_address:
                gotoActivity(AddShippingAddressActivity.class);
                break;
        }
    }
}