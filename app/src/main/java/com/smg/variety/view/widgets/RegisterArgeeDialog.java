package com.smg.variety.view.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.smg.variety.R;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.common.utils.WebViewUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 拆红包
 * Created by Administrator on 2017/2/15.
 */

public class RegisterArgeeDialog extends Dialog {

    @BindView(R.id.iv_close)
    ImageView      iv_close;
    @BindView(R.id.tv_argee)
    TextView       tv_argee;
    @BindView(R.id.webview)
    WebView        webviewWebView;
    @BindView(R.id.rl_red_packet_open)
    RelativeLayout rlRedPacketOpen;
    @BindView(R.id.tv_miss)
    TextView       tv_miss;

    private OnRigisterClickListener listener;
   private Context mContext;
    public RegisterArgeeDialog(Context context, OnRigisterClickListener onClickListener) {
        super(context, R.style.dialog_with_alpha);
        //按空白处不能取消动画
        //        setCanceledOnTouchOutside(false);
        this.listener = onClickListener;
        mContext=context;
        setContentView(R.layout.dialog_register_layout);
        ButterKnife.bind(this);
        initView();
        reg_agreement();
        initListener();
    }

    private void initView() {

    }



    private void reg_agreement() {

        DataManager.getInstance().getShopService(new DefaultSingleObserver<HttpResult<DetailDto>>() {
            @Override
            public void onSuccess(HttpResult<DetailDto> imgUrl) {
                DetailDto data = imgUrl.getData();
                if(data!=null){


                    if (imgUrl != null && !TextUtils.isEmpty(data.content)) {

                        WebViewUtil.setWebView(webviewWebView, Objects.requireNonNull(mContext));
                        WebViewUtil.loadHtmls(webviewWebView, data.content);
                    }
                }



            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }
    private void initListener() {
        tv_argee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRisterClick(1);

                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRisterClick(2);

                }
            }
        });
        tv_miss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRisterClick(3);

                }
            }
        });

    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface OnRigisterClickListener {
        void onRisterClick(int type);
    }

    /**
     * 基本点击事件跳转默认节流1000毫秒
     *
     * @param view   绑定的view
     * @param action 跳转的Acticvity
     */
    protected void bindClickEvent(View view, final Action action) {
        bindClickEvent(view, action, 1000);
    }

    protected void bindClickEvent(View view, final Action action, long frequency) {
        Observable<Object> observable = RxView.clicks(view);
        if (frequency > 0) {
            observable.throttleFirst(frequency, TimeUnit.MILLISECONDS);
        }
        observable.subscribe(trigger -> action.run());
    }
}
