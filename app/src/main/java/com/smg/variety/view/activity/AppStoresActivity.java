package com.smg.variety.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.widgets.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class AppStoresActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView       ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView        tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView        tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout  layoutTop;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_date)
    TextView        tvDate;
    @BindView(R.id.tv_jy1)
    TextView        tvJy1;
    @BindView(R.id.tv_jy2)
    TextView        tvJy2;
    @BindView(R.id.tv_jy3)
    TextView        tvJy3;
    @BindView(R.id.tv_jy4)
    TextView        tvJy4;
    @BindView(R.id.ll_bg)
    LinearLayout    llBg;
    @BindView(R.id.tv_dd1)
    TextView        tvDd1;
    @BindView(R.id.tv_dd2)
    TextView        tvDd2;
    @BindView(R.id.tv_dd3)
    TextView        tvDd3;
    @BindView(R.id.tv_dd4)
    TextView        tvDd4;
    @BindView(R.id.ll_bgs)
    LinearLayout    llBgs;
    @BindView(R.id.tv_sp1)
    TextView        tvSp1;
    @BindView(R.id.tv_sp2)
    TextView        tvSp2;
    @BindView(R.id.tv_sp3)
    TextView        tvSp3;
    @BindView(R.id.ll_bg1)
    LinearLayout    llBg1;
    @BindView(R.id.tv_tz1)
    TextView        tvTz1;
    @BindView(R.id.view_one)
    View            viewOne;
    @BindView(R.id.tv_tz2)
    TextView        tvTz2;
    @BindView(R.id.view_two)
    View            viewTwo;
    @BindView(R.id.tv_tz3)
    TextView        tvTz3;
    @BindView(R.id.view_three)
    View            viewThree;
    @BindView(R.id.ll_bg2)
    LinearLayout    llBg2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_stores;
    }

    @Override
    public void initView() {

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

    }

    private boolean allGet;

    private void getCouPonList() {
        allGet = false;
        showLoadDialog();
        DataManager.getInstance().getCouPonList(new DefaultSingleObserver<HttpResult<List<NewPeopleBeam>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewPeopleBeam>> result) {
                dissLoadDialog();
                if (result.getData() != null) {
                    for (NewPeopleBeam mean : result.getData()) {
                        if (mean.userCoupon == null) {
                            allGet = true;
                        }
                    }

                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                allGet = true;
            }
        }, "coupon.shop");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
