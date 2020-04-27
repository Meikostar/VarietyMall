package com.smg.variety.view.mainfragment.learn;

import android.webkit.WebView;

import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.VideoBean;

import butterknife.BindView;

public class CourseWarehouseGraphicFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;

    VideoBean videoBean;
    public static CourseWarehouseGraphicFragment newInstance(VideoBean videoBean) {
        CourseWarehouseGraphicFragment fragment = new CourseWarehouseGraphicFragment();
        fragment.videoBean = videoBean;
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.course_warehouse_graphic_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (videoBean != null){
            webView.loadData(videoBean.getContent(), "text/html; charset=UTF-8", null);
        }
    }

    @Override
    protected void initListener() {

    }
}
