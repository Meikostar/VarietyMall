package com.smg.variety.view.widgets.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.utils.ShareUtil;

/**
 * 美白、磨皮
 * Created by Administrator on 2017/2/15.
 */

public class BeautyDialog extends BaseDialog {
    private SeekBar               mopiLevel_seekBar;
    private SeekBar               meibaiLevel_seekBar;
    private TextView              tv_bc;
    private TextView              tv_cz;
    private SeekBarChangeListener listener;


    public BeautyDialog(Activity context,SeekBarChangeListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_beauty;
    }
    private int mp;
    private int mb;
    /**
     * 初始化界面控件
     */
    @Override
    protected void initView() {
        //按空白处不能取消动画
        WindowManager manager = getWindow().getWindowManager();
        Display display = manager.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (display.getWidth());
        Window window = getWindow();
        window.setAttributes(p);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomAnimStyle);
        mp= ShareUtil.getInstance().getInt("live_mp",0);
        mb=ShareUtil.getInstance().getInt("live_mb",0);
        mopiLevel_seekBar = findViewById(R.id.mopiLevel_seekBar);
        tv_bc = findViewById(R.id.tv_bc);
        tv_cz = findViewById(R.id.tv_cz);
        meibaiLevel_seekBar = findViewById(R.id.meibaiLevel_seekBar);
        if(mp!=0){
            mopiLevel_seekBar.setProgress(mp);
        } if(mb!=0){
            meibaiLevel_seekBar.setProgress(mb);
        }

        mopiLevel_seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        meibaiLevel_seekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mopiLevel_seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        meibaiLevel_seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        tv_bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareUtil.getInstance().saveInt("live_mp",mopiLevel_seekBar.getProgress());
                ShareUtil.getInstance().saveInt("live_mb",meibaiLevel_seekBar.getProgress());
                dismiss();
            }
        });
        tv_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.getInstance().saveInt("live_mp",0);
                ShareUtil.getInstance().saveInt("live_mb",0);
                mopiLevel_seekBar.setProgress(0);
                meibaiLevel_seekBar.setProgress(0);
            }
        });
        mopiLevel_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null){
                    listener.onMoPiSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        meibaiLevel_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (listener != null){
                    listener.onSeekBarChange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    /**
     * 初始化界面控件的显示数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    @Override
    protected void initEvent() {
    }

   public interface SeekBarChangeListener{
       void onSeekBarChange(int progress);
       void onMoPiSeekBarChange(int progress);
   }
}
