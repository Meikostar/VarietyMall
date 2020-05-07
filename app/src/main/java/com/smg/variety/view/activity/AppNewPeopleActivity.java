package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BaseDto4;
import com.smg.variety.bean.GroupInfoDto;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.bean.RenWuBean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.adapter.NewPeopleAdapter;
import com.smg.variety.view.adapter.RenWuAdapter;
import com.smg.variety.view.widgets.CustomDividerItemDecoration;
import com.smg.variety.view.widgets.autoview.MaxRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class AppNewPeopleActivity extends BaseActivity {


    @BindView(R.id.entity_store_push_recy)
    MaxRecyclerView entity_store_push_recy;
    @BindView(R.id.iv_title_back)
    ImageView       ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView        tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView        tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout  layoutTop;
    @BindView(R.id.iv_bg)
    ImageView       ivBg;
    @BindView(R.id.tv_lq)
    TextView        tvLq;


    @Override
    public int getLayoutId() {
        return R.layout.activity_new_person;
    }

    @Override
    public void initView() {
        tvTitleText.setText("新人礼包");
        initAdapter();
        getCouPonList();
    }


    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvLq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allGet){
                    putCouPon("all");

                }else {
                    ToastUtil.showToast("你已领完！");
                }

            }
        });
    }

    private boolean allGet;
    private void getCouPonList() {
        allGet=false;
        showLoadDialog();
        DataManager.getInstance().getCouPonList(new DefaultSingleObserver<HttpResult<List<NewPeopleBeam>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewPeopleBeam>> result) {
                dissLoadDialog();
                if(result.getData() != null){
                      for(NewPeopleBeam mean:result.getData()){
                          if(mean.userCoupon==null){
                              allGet=true;
                          }
                      }
                    mEntityStoreAdapter.setNewData(result.getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                allGet=true;
            }
        }, "coupon.shop");
    }

    private void putCouPon(String ids) {
        showLoadDialog();
        DataManager.getInstance().putCouPon(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                ToastUtil.showToast("领取成功");
                getCouPonList();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                   ToastUtil.showToast("领取成功");
                    getCouPonList();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, ids);
    }



    private NewPeopleAdapter mEntityStoreAdapter;


    private void initAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(AppNewPeopleActivity.this);
        entity_store_push_recy.setLayoutManager(layoutManager);
        mEntityStoreAdapter = new NewPeopleAdapter(mlist, this);
        mEntityStoreAdapter.setTaskListener(new NewPeopleAdapter.ItemTaskListener() {
            @Override
            public void taskListener(NewPeopleBeam bean) {
                if(bean==null){

                }else {
                    putCouPon(bean.id);
                }

            }
        });
        entity_store_push_recy.setAdapter(mEntityStoreAdapter);
    }

    private List<NewPeopleBeam> mlist = new ArrayList<>();


}
