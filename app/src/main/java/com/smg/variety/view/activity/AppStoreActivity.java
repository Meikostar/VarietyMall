package com.smg.variety.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;


import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.apply;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.view.adapter.AppBasedapter;

import java.util.List;

import butterknife.BindView;

public class AppStoreActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivBack;

    @BindView(R.id.listview)
    ListView listview;

    private AppBasedapter adapter;




    @Override
    public int getLayoutId() {
        return R.layout.activity_app_store;
    }

    @Override
    public void initView() {
        adapter = new AppBasedapter(this);
        listview.setAdapter(adapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        getImageCode();
    }

    /**
     *
     */
    private void getImageCode() {
        DataManager.getInstance().getApps(new DefaultSingleObserver<List<apply>>() {
            @Override
            public void onSuccess(List<apply> object) {
                adapter.setData(object);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                LogUtil.i(TAG, "RxLog-Thread: onError() = " + Long.toString(Thread.currentThread().getId()));
            }
        });
    }

    public void setData() {

    }




    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new AppBasedapter.OnItemClickListener() {
            @Override
            public void onItemClick(apply data) {
                Intent intent = new Intent(AppStoreActivity.this, WebViewActivity.class);

                intent.putExtra(WebViewActivity.WEBTITLE, data.application_image_name);
                intent.putExtra(WebViewActivity.WEBURL, data.url);
                startActivity(intent   );
            }
        });
    }
}
