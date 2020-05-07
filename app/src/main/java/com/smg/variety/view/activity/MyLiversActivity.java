package com.smg.variety.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.adapter.VideoLivesAdapter;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyLiversActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivBack;


    @BindView(R.id.tv_title_text)
    TextView         tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView         tvTitleRight;
    @BindView(R.id.iv_right)
    ImageView        ivRight;
    @BindView(R.id.layout_top)
    RelativeLayout   layoutTop;
    @BindView(R.id.grid_content)
    NoScrollGridView gridContent;

    private VideoLivesAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_app_lives;
    }

    @Override
    public void initView() {
        adapter = new VideoLivesAdapter(this);
        gridContent.setAdapter(adapter);
        adapter.setListener(new VideoLivesAdapter.ItemClickListener() {
            @Override
            public void itemClick(String id) {
                delVideos(id);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        liveVideos();
    }

    private void liveVideos() {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

        map.put("liver_user_id", ShareUtil.getInstance().get(Constants.USER_ID));

        map.put("page", 1 + "");
        map.put("per_page", 300 + "");

        map.put("live_type", "2");
        map.put("include", "apply,room,user,videoproducts");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();
                if (result != null && result.getData() != null) {
                    adapter.setData(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();


            }
        }, map);
    }

    private void delVideos(String id) {

        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();

        map.put("id", id);

        DataManager.getInstance().delVideos(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                liveVideos();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

                liveVideos();
            }
        }, map);
    }


    public void setData() {

    }

    private int state;

    @Override
    public void initListener() {
        tvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<VideoLiveBean> data = adapter.getData();

                if (state == 1) {
                    for (VideoLiveBean bean : data) {
                        bean.isChoose = false;
                    }
                    state = 0;
                    tvTitleRight.setText("编辑");
                } else {
                    state = 1;
                    for (VideoLiveBean bean : data) {
                        bean.isChoose = true;
                    }
                    tvTitleRight.setText("完成");
                }
                adapter.setData(data);
            }
        });
    }


}
