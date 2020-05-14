package com.smg.variety.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.smg.variety.base.BaseApplication;
import com.smg.variety.bean.AddressDto;
import com.smg.variety.bean.BalanceDto;
import com.smg.variety.bean.CheckOutOrderResult;
import com.smg.variety.bean.WEIXINREQ;
import com.smg.variety.common.utils.PayUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.SettingPasswordActivity;
import com.smg.variety.view.activity.PaySuccessActivity;
import com.smg.variety.view.widgets.InputPwdDialog;
import com.smg.variety.view.widgets.MCheckBox;
import com.smg.variety.view.widgets.PhotoPopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.smg.variety.R;
import com.smg.variety.base.BaseFragment;
import com.smg.variety.bean.CouponCodeInfo;
import com.smg.variety.bean.MyOrderDto;
import com.smg.variety.bean.MyOrderItemDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.activity.MyOrderEvaluateListActivity;
import com.smg.variety.view.activity.MyOrderLogisticsActivity;
import com.smg.variety.view.activity.MyOrderReturnActivity;
import com.smg.variety.view.adapter.OrderAdapter;
import com.smg.variety.view.widgets.CustomDividerItemDecoration;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.dialog.CouponCodeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 收藏 商品
 */
@SuppressLint("ValidFragment")
public class OrderFragment extends BaseFragment {
    //更新列表
    private static int UPLOAD_DATA = 100;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private String type = "";
    private String currentPage = "1";
    private int mPage = 1;

    public OrderFragment(String type) {
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        initRecyclerView();

    }
    private boolean ishow;
    @Override
    public void onResume() {
        super.onResume();
        if(ishow){
            mRefreshLayout.autoRefresh();
        }else {
            ishow=true;
        }
    }

    @Override
    protected void initData() {
        mRefreshLayout.autoRefresh();
        getAddressListData();
        getBalance();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mPage = 1;
            loadData();
        }
    }
    @Override
    protected void initListener() {
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
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        mAdapter = new OrderAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    switch (textView.getText().toString()) {
                        case "去付款":
                            checkOutOrder(mAdapter.getItem(position).getNo(),mAdapter.getItem(position).getPay_total());
                            break;
                        case "取消订单":
                            cancelOrder(mAdapter.getItem(position).getId());
                            break;
                        case "取消退款":
                            cancelRefuendOrder(mAdapter.getItem(position).getRefundInfo().getData().getId());
                            break;

                        case "催发货":
                            hurryOrder(mAdapter.getItem(position).getId());
                            break;
                        case "确认收货":
                            confirmOrder(mAdapter.getItem(position).getId());
                            break;
                        case "去评价":
                            Bundle bundle1 = new Bundle();
                            bundle1.putString(Constants.INTENT_ID, mAdapter.getItem(position).getId());
                            gotoActivity(MyOrderEvaluateListActivity.class, false, bundle1);
                            break;
                        case "申请退款":
                            Bundle bundle2 = new Bundle();
                            bundle2.putString(Constants.INTENT_ID, mAdapter.getItem(position).getId());
                            bundle2.putString(Constants.TYPE, mAdapter.getItem(position).getType());
                            if (mAdapter.getItem(position).getItems() != null && mAdapter.getItem(position).getItems().getData() != null && mAdapter.getItem(position).getItems().getData().size() > 0) {
                                MyOrderItemDto myOrderItemDto = mAdapter.getItem(position).getItems().getData().get(0);
                                if (myOrderItemDto.getProduct().getData().getCover() != null) {
                                    bundle2.putString("cover", myOrderItemDto.getProduct().getData().getCover());
                                }
                                if ("ax".equals(mAdapter.getItem(position).getType())) {
                                    bundle2.putString("score", myOrderItemDto.getScore());
                                    bundle2.putBoolean("isFlag2", true);
                                } else {
                                    bundle2.putString("price", myOrderItemDto.getPrice());
                                    bundle2.putBoolean("isFlag2", false);
                                }
                                bundle2.putString("title", myOrderItemDto.getTitle());
                                bundle2.putString("num", myOrderItemDto.getQty());
                            }
                            gotoActivity(MyOrderReturnActivity.class, false, bundle2, UPLOAD_DATA);
                            break;
                        case "查看物流":
                            Bundle bundle3 = new Bundle();
                            bundle3.putString("id", mAdapter.getItem(position).getId());
                            if(TextUtil.isNotEmpty(mAdapter.getItem(position).express_no)){
                                bundle3.putString("express_no", mAdapter.getItem(position).express_no);
                            }

                            gotoActivity(MyOrderLogisticsActivity.class, false, bundle3);
                            break;
                        case "查看兑换券":
                            MyOrderDto item = mAdapter.getItem(position);
                            if (item.getItems() != null && item.getItems().getData().size() > 0) {
                                CouponCodeInfo codeInfo = item.getItems().getData().get(0).getVcode().getData();
                                CouponCodeDialog dialog = new CouponCodeDialog(getActivity(), codeInfo.getCode());
                                dialog.show();
                            }

                            break;
                        case "再来一单":
                            if (mAdapter.getItem(position).getItems() !=null && mAdapter.getItem(position).getItems().getData() != null && mAdapter.getItem(position).getItems().getData().size() > 0){
                                ArrayList<String> product_id = new ArrayList<>();
                                ArrayList<String> qty = new ArrayList<>();
                                ArrayList<String> stock_id = new ArrayList<>();
                                for (MyOrderItemDto myOrderItemDto: mAdapter.getItem(position).getItems().getData()){
                                    product_id.add(myOrderItemDto.getProduct_id());
                                    qty.add(myOrderItemDto.getQty());
                                    stock_id.add(myOrderItemDto.getSku_id());
                                }
                                showLoadDialog();

                                HashMap<String,Object> map = new HashMap<>();
                                map.put("qty",qty);
                                if(product_id!= null && product_id.size()>0){
                                    for(int i =0;i< product_id.size();i++){
                                        if(!TextUtils.isEmpty(product_id.get(i))){
                                            map.put("product_id["+i+"]",product_id.get(i)) ;
                                        }
                                    }
                                }
                                if(stock_id!= null && stock_id.size()>0){
                                    for(int i =0;i< stock_id.size();i++){
                                        if(!TextUtils.isEmpty(stock_id.get(i))){
                                            map.put("stock_id["+i+"]",stock_id.get(i)) ;
                                        }
                                    }
                                }
                                DataManager.getInstance().addShoppingCart(new DefaultSingleObserver<HttpResult<Object>>() {
                                    @Override
                                    public void onSuccess(HttpResult<Object> result) {
                                        dissLoadDialog();
                                        getActivity().finish();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(MainActivity.PAGE_INDEX, 3);
                                        gotoActivity(MainActivity.class, true, bundle);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        dissLoadDialog();

                                    }
                                }, "default", map);


                            }
                            break;
                    }
                }
            }
        });


    }


    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        switch (type) {
            case Constants.TREE_ORDER_TYPE_ALL:
                map.put("include", "shop,shopUser,items.product,shipment,refundInfo");
                break;
            case Constants.TREE_ORDER_TYPE_PAYMENT://待支付
                map.put("filter[status]", "created");
                map.put("include", "shop,shopUser,items.product,shipment,refundInfo");
                break;
            case Constants.TREE_ORDER_TYPE_DELIVERY://待发货
                map.put("filter[status]", "paid");
                map.put("include", "shop,shopUser,items.product,shipment,refundInfo");
                break;
            case Constants.TREE_ORDER_TYPE_RECEIVE://待收货
                map.put("filter[status]", "shipping");
                map.put("include", "shop,shopUser,items.product,shipment,refundInfo");
                break;
            case Constants.TREE_ORDER_TYPE_EVALUATE://待评价
                map.put("filter[status]", "shipped");
                map.put("include", "shop,shopUser,items.product,shipment,refundInfo");
                break;
        }
        map.put("page", mPage + "");
//        map.put("api_nested_no_data", "1");
        DataManager.getInstance().getAllUserOrders(new DefaultSingleObserver<HttpResult<List<MyOrderDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<MyOrderDto>> httpResult) {
                if(states==1){
                    return;
                }
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(getActivity().isFinishing()){
                    return;
                }
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, map);
    }


    private void setData(HttpResult<List<MyOrderDto>> httpResult) {
//        if (httpResult == null || httpResult.getData() == null) {
//            return;
//        }
        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    public void showPopPayWindows(String paytoal) {

        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.pay_popwindow_view, null);

            btSure = view.findViewById(R.id.bt_sure);
            iv_close = view.findViewById(R.id.iv_close);
            llBalance = view.findViewById(R.id.ll_balance);
            llZfb = view.findViewById(R.id.ll_zfb);
            llWx = view.findViewById(R.id.ll_wx);
            mcbBalance = view.findViewById(R.id.mcb_balance);
            mcbZfb = view.findViewById(R.id.mcb_zfb);
            mcbWx = view.findViewById(R.id.mcb_wx);

            llBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChoose(0);
                    state=0;
                }
            });
            llWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state=1;
                    setChoose(1);
                }
            });
            llZfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state=2;
                    setChoose(2);
                }
            });
            btSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state){
                        case 0:
                            if (Double.valueOf(GdBalance) < Double.valueOf(paytoal)) {
                                ToastUtil.showToast("余额不足");
                                return;
                            }
                            getMemberBaseInfo(new InputPwdDialog.InputPasswordListener() {
                                @Override
                                public void callbackPassword(String password) {
                                    if (Double.valueOf(GdBalance) < Double.valueOf(paytoal)) {
                                        ToastUtil.showToast("余额不足");
                                        return;
                                    }
                                    submitWalletOrder(password);
                                }
                            });
                            break;

                        case 1:
                            submitWxOrder();
                            break;
                        case 2:
                            submitOrder();
                            break;
                    }
                    mWindowAddPhoto
                            .dismiss();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhoto.dismiss();
                }
            });

            mWindowAddPhoto = new PhotoPopupWindow(getActivity()).bindView(view);
            mWindowAddPhoto.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhoto.showAtLocation(mRecyclerView, Gravity.BOTTOM, 0, 0);
        }


    }

    private double GdBalance;

    public void getBalance() {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                super.onSuccess(balanceDto);
                if(states==1){
                    return;
                }
                GdBalance = balanceDto.getMoney();

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }
    private int states;
    @Override
    public void onDestroy() {
        super.onDestroy();
        states=1;
    }

    /**
     * zfb
     */
    private void submitWalletOrder(String payment_password) {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        for (CheckOutOrderResult result : ruslts) {
            map.put("order_no[" + i + "]", result.no);
            i++;
        }
        map.put("platform", "wallet");
        map.put("scene", "balance");
        map.put("payment_password", payment_password);
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitWalletOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                Intent intent = new Intent(getActivity(), PaySuccessActivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    Intent intent = new Intent(getActivity(), PaySuccessActivity.class);
                    intent.putExtra("state", 1);
                    startActivity(intent);

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);


    }
    /**
     * 余额支付时，查询是否设置支付密码
     */
    private void getMemberBaseInfo(InputPwdDialog.InputPasswordListener listener) {
        if (BaseApplication.isSetPay == 1) {
            InputPwdDialog inputPasswordDialog = new InputPwdDialog(getActivity(), listener);
            inputPasswordDialog.show();
        } else {
            gotoActivity(SettingPasswordActivity.class);
        }
    }
    /**
     * 获取收货地址
     */
    private List<AddressDto> mAddressDatas = new ArrayList<>();
    private String           addressId;
    private void getAddressListData() {
        //showLoadDialog();
        DataManager.getInstance().getAddressesList(new DefaultSingleObserver<List<AddressDto>>() {
            @Override
            public void onSuccess(List<AddressDto> addressDtos) {
                //dissLoadDialog();
                if(states==1){
                    return;
                }
                mAddressDatas.clear();
                mAddressDatas.addAll(addressDtos);
                if (mAddressDatas.size() > 0) {
                    for (int i = 0; i < mAddressDatas.size(); i++) {
                        if (mAddressDatas.get(i).getIs_default().equals("1")) {
                            addressId = mAddressDatas.get(i).getId()+"";
                        }
                    }
                }


            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private List<CheckOutOrderResult> ruslts =new ArrayList<>();
    private int                       RQ_WEIXIN_PAY = 12;
    private void checkOutOrder(String no,String paytotal) {
        CheckOutOrderResult checkOutOrderResult = new CheckOutOrderResult();
        checkOutOrderResult.no=no;
        ruslts.clear();
        ruslts.add(checkOutOrderResult);
        showPopPayWindows(paytotal);
//        HashMap<String, String> map = new HashMap<>();
//        map.put("rows[0]", no);
//        map.put("address_id", addressId);
//        DataManager.getInstance().checkOutOrder(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
//            @Override
//            public void onSuccess(HttpResult<List<CheckOutOrderResult>> result) {
//                ruslts=result.getData();
//                if(ruslts!=null&&ruslts.size()>0){
//
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                if (ApiException.getInstance().isSuccess()) {
//                    ToastUtil.showToast("下单成功");
//                } else {
//                    ToastUtil.showToast("下单失败");
//                }
//                getActivity().finish();
//            }
//        }, "default", map);
    }

    /**
     * zfb
     */
    private void submitOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i=0;
        for(CheckOutOrderResult result:ruslts){
            map.put("order_no["+i+"]",result.no);
            i++;
        }
        map.put("platform", "alipay");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitZfbOrder(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> httpResult) {
                dissLoadDialog();
                if(httpResult != null && !TextUtils.isEmpty(httpResult.getData())){
                    PayUtils.getInstances().zfbPaySync(getActivity(), httpResult.getData(), new PayResultListener() {
                        @Override
                        public void zfbPayOk(boolean payOk) {
                            Intent intent = new Intent(getActivity(), PaySuccessActivity.class);

                            if(payOk){
                                intent.putExtra("state",1);
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                ToastUtil.showToast("支付已取消");
                                //                                finish();
                            }


                        }

                        @Override
                        public void wxPayOk(boolean payOk) {

                        }
                    });
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);


    }
    /**
     * wx
     */
    private void submitWxOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i=0;
        for(CheckOutOrderResult result:ruslts){
            map.put("order_no["+i+"]",result.no);
            i++;
        }
        map.put("platform", "wechat");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney


        DataManager.getInstance().submitWxOrder(new DefaultSingleObserver<HttpResult<WEIXINREQ>>() {
            @Override
            public void onSuccess(HttpResult<WEIXINREQ> httpResult) {
                dissLoadDialog();
                PayUtils.getInstances().WXPay(getActivity(), httpResult.getData());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }
    private View             mView;

    private RecyclerView     recyclerView;
    private Button           btSure;
    private TextView         tv_title;
    private ImageView        iv_close;
    private View             view;
    private PhotoPopupWindow mWindowpayPhoto;
    private LinearLayout     llBalance;
    private LinearLayout     llZfb;
    private LinearLayout     llWx;
    private MCheckBox        mcbBalance;
    private MCheckBox        mcbZfb;
    private MCheckBox        mcbWx;
    private double           total;
    private double           totals;
    public void setChoose(int state){
        switch (state){
            case 0:
                mcbBalance.setChecked(true);
                mcbZfb.setChecked(false);
                mcbWx.setChecked(false);
                break;

            case 1:
                mcbBalance.setChecked(false);
                mcbZfb.setChecked(false);
                mcbWx.setChecked(true);
                break;
            case 2:
                mcbBalance.setChecked(false);
                mcbZfb.setChecked(true);
                mcbWx.setChecked(false);
                break;
        }
    }
    private int state;
    private PhotoPopupWindow mWindowAddPhoto;
    /**
     * 取消的订单
     *
     * @param id
     */
    private void cancelRefuendOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().getOrdercancel(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("取消成功");
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("取消成功");
                    mRefreshLayout.autoRefresh();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, id);
    }
    /**
     * 取消的订单
     *
     * @param id
     */
    private void cancelOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().cancelOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("取消成功");
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("取消成功");
                    mRefreshLayout.autoRefresh();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, id);
    }

    /**
     * 催发货
     *
     * @param id
     */
    private void hurryOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().hurryOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("催促中");
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("催促中");
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, id);
    }

    /**
     * 确认收货
     *
     * @param id
     */
    private void confirmOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().confirmOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("确认收货成功");
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("确认收货成功");
                    mRefreshLayout.autoRefresh();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_DATA && mRefreshLayout != null) {
            mRefreshLayout.autoRefresh();
        }
    }
}
