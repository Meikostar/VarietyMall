package com.smg.variety.view.widgets;


import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.smg.variety.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 输入交易密码对话框
 */
public class ConfigPasswordDialog extends Dialog {
    private EditText               et1;
    private TextView               dialog_ok;
    private TextView               dialog_cancel;
    private ConfigPasswordListener sexSelectListener;
    private Context                mContext;
    private int                    keyNum = 0;

    public ConfigPasswordDialog(Context context, ConfigPasswordListener listener) {
        super(context, R.style.dialog_with_alpha);
        //        setCanceledOnTouchOutside(false);//设置点击外部不可以取消;
        this.mContext = context;
        this.sexSelectListener = listener;
        //        setContentView(R.layout.config_password_dialog);
       setCancelable(false);
       setCanceledOnTouchOutside(false);
        setContentView(R.layout.write_group_name);
        initView();
        initListener();
    }

    private void initView() {
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottomAnimStyle);
        //        ((TextView) findViewById(R.id.tv_title)).setText("房间密码");
        //        dialog_cancel = findViewById(R.id.txt_cancel);
        et1 = findViewById(R.id.edit_reson);
        //        et1.setHint("请输入房间密码");
        et1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog_ok = findViewById(R.id.txt_sure);
    }

    private void initListener() {
        bindClickEvent(dialog_ok, () -> {
            //            if (keyNum < 6) {
            //                ToastUtil.showToast("请输入完整的密码");
            //            } else {
            if (null != this.sexSelectListener) {
                String pwd1 = et1.getText().toString();
                sexSelectListener.callbackPassword(pwd1);
            }
            this.cancel();
            //            }
        });
        //        bindClickEvent(dialog_cancel, () -> {
        //            this.cancel();
        //        });
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

    public interface ConfigPasswordListener {
        void callbackPassword(String password);
    }
}

