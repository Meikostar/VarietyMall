package com.smg.variety;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.utils.RxTimerUtil;
import com.smg.variety.view.MainActivity;

import butterknife.BindView;

/**
 * 启动页面
 * Created by rzb on 2019/6/14.
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    @BindView(R.id.iv_img)
    ImageView ivImg;

    @Override
    public int getLayoutId() {
        return R.layout.ui_splash_layout;
    }

    @Override
    public void initView() {


    }
    private Context mContext;
    @Override
    public void initData() {



        RxTimerUtil.timer(1800, number -> {
            gotoActivity(MainActivity.class, true);
        });
    }

    @Override
    public void initListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        mContext=this;
        Glide.with(mContext).load(R.drawable.ic_splash).apply(options).into(ivImg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
