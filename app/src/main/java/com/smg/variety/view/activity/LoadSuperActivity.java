package com.smg.variety.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.SuperAdapter;
import com.smg.variety.view.adapter.UploadPicAdapter;
import com.smg.variety.view.widgets.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 掌柜
 */
public class LoadSuperActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView        ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView         tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView         tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout   layoutTop;
    @BindView(R.id.grid_content)
    NoScrollGridView gridContent;
    @BindView(R.id.tv_state)
    TextView         tvState;
    @BindView(R.id.tv_content)
    TextView         tvContent;
    @BindView(R.id.tv_yy)
    TextView         tvYy;
    @BindView(R.id.ll_bg)
    LinearLayout     llBg;
    @BindView(R.id.tv_next)
    TextView         tv_next;

    @Override
    public int getLayoutId() {
        return R.layout.activity_super_load;
    }

    private BannerInfoDto data;

    @Override
    public void initView() {
        data = (BannerInfoDto) getIntent().getSerializableExtra("data");
        tvTitleText.setText("掌柜");
    }

    private SuperAdapter brandsdapter;
   private List<String> urls=new ArrayList<>();
    @Override
    public void initData() {
        if (data != null) {
            if (data.status == 0) {
               llBg.setVisibility(View.GONE);
                tv_next.setVisibility(View.GONE);
                tvState.setTextColor(getResources().getColor(R.color.my_color_ffa));
                tvState.setText("待审核");
                tvContent.setText("您已成功上传截图，请耐心等待审核通过！");
                if(TextUtil.isNotEmpty(data.img1)){
                    urls.add(data.img1);
                }  if(TextUtil.isNotEmpty(data.img2)){
                    urls.add(data.img2);
                }

            } else {
                tv_next.setVisibility(View.VISIBLE);
                llBg.setVisibility(View.VISIBLE);
                tvState.setText("审核未通过");
                tvContent.setText("很抱歉您未通过审核！");
                tvState.setTextColor(getResources().getColor(R.color.red_dark));
            }
        }
        brandsdapter = new SuperAdapter(this);
        gridContent.setAdapter(brandsdapter);
        if(urls.size()>0){
            brandsdapter.setData(urls);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private UploadPicAdapter mAdapter;

    @Override
    public void initListener() {
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(UploadSuperActivity.class);
            }
        });
    }

    private Map<String, String> map = new HashMap<>();

    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;

        }

    }



}
