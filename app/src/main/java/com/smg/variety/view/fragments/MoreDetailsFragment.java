package com.smg.variety.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.ScoreIncomeBean;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.DateUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.MoreDetailAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.picker.TimePickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 流水明细
 */
public class MoreDetailsFragment extends BaseFragment {
    @BindView(R.id.recy_my_order)
    RecyclerView       rvList;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_date_star)
    TextView           tvDateStar;
    @BindView(R.id.tv_date_end)
    TextView           tvDateEnd;
    @BindView(R.id.tv_find)
    TextView           tvFind;
    @BindView(R.id.ll_bg)
    LinearLayout       llBg;
    @BindView(R.id.header)
    MaterialHeader     header;
    Unbinder unbinder;

    private MoreDetailAdapter mOrderListAdapter;

    private int mOrderStatus;
    private int mPage = 1;


    public static MoreDetailsFragment newInstance(int type) {
        MoreDetailsFragment fragment = new MoreDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_running_water_details;
    }

    @Override
    protected void initView() {
        llBg.setVisibility(View.VISIBLE);
        mOrderStatus = getArguments().getInt("type");
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mOrderListAdapter = new MoreDetailAdapter(getActivity());
        rvList.setAdapter(mOrderListAdapter);
    }

    @Override
    protected void initData() {
    }

    private int yearStr;
    private int monthStr;
    private int dayStr;
    private int type;
    private void setBridthDayTv(Date date) {
        if (date != null) {
            String str = DateUtils.formatDate(date, "yyyy-MM-dd ");


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //            LogUtil.d(calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + getString(R.string.nxzdrq) + str);
            if(type==0){
                tvDateStar.setText(str);
            }else {
                tvDateEnd.setText(str);
            }


            yearStr = calendar.get(Calendar.YEAR);
            monthStr = calendar.get(Calendar.MONTH) + 1;
            dayStr = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            if(type==0){
                tvDateStar.setText("");
            }else {
                tvDateEnd.setText("");
            }
            yearStr = 0;
            monthStr = 0;
            dayStr = 0;
        }
    }
    private TimePickerView timePickerView;

    private void showTimeSelector() {

        if (timePickerView == null) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Integer.valueOf(DateUtils.formatToYear(System.currentTimeMillis())), Integer.valueOf(DateUtils.formatToMonth(System.currentTimeMillis())) == 0 ? 12 : Integer.valueOf(DateUtils.formatToMonth(System.currentTimeMillis())) - 1, Integer.valueOf(DateUtils.formatToDay(System.currentTimeMillis())), 0, 0);
            timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {

                    setBridthDayTv(date);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                    .setCancelText("清空")//取消按钮文字
                    .setSubmitText("确定")//确认按钮文字
                    .setContentSize(20)//滚轮文字大小
                    .setTitleSize(18)//标题文字大小
                    .setTitleText("选择日期")//标题文字
                    .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                    .setTitleColor(getResources().getColor(R.color.my_color_333333))//标题文字颜色
                    .setSubmitColor(getResources().getColor(R.color.my_color_333333))//确定按钮文字颜色
                    .setCancelColor(getResources().getColor(R.color.my_color_333333))//取消按钮文字颜色
                    //                .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
                    //                .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
                    .setRange(1950, 2100)
                    .setDate(calendar)
                    .setLabel(getString(R.string.yea), getString(R.string.yeu), getString(R.string.riis), "时", "", "")
                    .setLineSpacingMultiplier(2f)
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                    .isDialog(false)
                    .build();
        }
        timePickerView.setState(type);
        timePickerView.show();
    }
    @Override
    protected void initListener() {
        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage = 1;
                loadData();
            }
        });
        tvDateStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                showTimeSelector();
            }
        });
        tvDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                showTimeSelector();
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                loadData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                loadData();
            }
        });
        mRefreshLayout.autoRefresh();
    }

    public void RefreshData() {
        mRefreshLayout.autoRefresh();
    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        map.put("money_type", "bonus");
        if (TextUtil.isNotEmpty(tvDateStar.getText().toString())) {
            if (TextUtil.isNotEmpty(tvDateEnd.getText().toString())) {
                map.put("filter[created_at]", tvDateStar.getText().toString()+ "00:00:00" + ":::" + tvDateEnd.getText().toString()+ "00:00:00");
            } else {
                map.put("filter[created_at]", tvDateStar.getText().toString()+ "00:00:00" + ":::");
            }

        } else {
            if (TextUtil.isNotEmpty(tvDateEnd.getText().toString())) {
                map.put("filter[created_at]", ":::" + tvDateEnd.getText().toString()+ "00:00:00");
            }
        }
        if (mOrderStatus == 1) {
            map.put("type", "distribution_level_1,distribution_level_2");
        } else if (mOrderStatus == 2) {
            map.put("type", "pull_new_reward");
        } else if (mOrderStatus == 3) {
            map.put("type", "platform_reward");
        }
//        else if (mOrderStatus == 4) {
//            map.put("type", "day_income");
//        }
        //        map.put("include", "order.user,order.address");
        DataManager.getInstance().walletLog(new DefaultSingleObserver<HttpResult<List<ScoreIncomeBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<ScoreIncomeBean>> httpResult) {
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.setEnableRefresh(true);

            }
        }, map);
    }

    private void setData(HttpResult<List<ScoreIncomeBean>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mOrderListAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mOrderListAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mOrderListAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
