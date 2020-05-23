package com.smg.variety.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.smg.variety.R;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.common.utils.WebViewUtil;
import com.smg.variety.view.widgets.autoview.NoScrollWebView;
import com.smg.variety.view.widgets.dialog.PopDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import static com.smg.variety.common.utils.ToastUtil.showToast;


public class DialogUtils {

    /**
     * 选择性别
     */
    public static void showSexDialog(Context context, OnCheckSexListener listener){
        PopDialog mSexDialog = new PopDialog(context, R.layout.layout_sex);
        mSexDialog.show();
        mSexDialog.findViewById(R.id.man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(v);
                }
                mSexDialog.dismiss();
            }
        });
        mSexDialog.findViewById(R.id.woman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(v);
                }
                mSexDialog.dismiss();
            }
        });
        mSexDialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSexDialog.dismiss();
            }
        });
    }

    /**
     * 考试提醒
     */
    public static void showChangeSuccessDialog(Context context, String noticeStr, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_success_item, null);

        TextView  tvContent = view.findViewById(R.id.tv_exam_no_notice);
        tvContent.setText(""+noticeStr);

        //        TextView tvNo = view.findViewById(R.id.tv_exam_no_notice);
        //        tvNo.setOnClickListener(new View.OnClickListener() {
        //            public void onClick(View view) {
        //                dialog.dismiss();
        //                if(listener!=null){
        //                    listener.onClick(view);
        //                }
        //            }
        //        });

        TextView tvSure = view.findViewById(R.id.tv_exam_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onClick(view);
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*0.8) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    /**
     * 考试提醒
     */
    public static void showChangeDialog(Context context, String cout,double figs, OnClickDialogCoutsListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.show_change_item, null);

        TextView  tvContent = view.findViewById(R.id.tv_use);
        tvContent.setText("可用"+cout+"个");

        //        TextView tvNo = view.findViewById(R.id.tv_exam_no_notice);
        //        tvNo.setOnClickListener(new View.OnClickListener() {
        //            public void onClick(View view) {
        //                dialog.dismiss();
        //                if(listener!=null){
        //                    listener.onClick(view);
        //                }
        //            }
        //        });

        EditText editText1 = view.findViewById(R.id.et_cout);
        TextView editText2 = view.findViewById(R.id.et_couts);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              if(TextUtil.isNotEmpty(s.toString())){

                  if(Long.valueOf(s.toString())>Double.valueOf(cout)){
                      double aDouble = Double.valueOf(cout);
                      ToastUtil.showToast("最多可兑换"+cout);
                      editText1.setText((long)aDouble+"");
                      editText2.setText((float)aDouble*figs+"");
                  }else {
                      double aDouble = Double.valueOf(cout);
                      editText2.setText((float)Long.valueOf(s.toString())*figs+"");
                  }
              }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextView tvSure = view.findViewById(R.id.tv_sure);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    if(!TextUtil.isNotEmpty(editText1.getText().toString())){
                       ToastUtil.showToast("请输入兑换数量");
                    }
                    listener.showCouts( editText1.getText().toString(),editText2.getText().toString());
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*0.8) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    /**
     * 考试提醒
     */
    public static void showExamNoticeDialog(Context context, String noticeStr, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.exam_notice, null);

        TextView  tvContent = view.findViewById(R.id.tv_notice_content);
        tvContent.setText("您的专属导师微信号："+noticeStr);

//        TextView tvNo = view.findViewById(R.id.tv_exam_no_notice);
//        tvNo.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                dialog.dismiss();
//                if(listener!=null){
//                    listener.onClick(view);
//                }
//            }
//        });

        TextView tvSure = view.findViewById(R.id.tv_exam_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onClick(view);
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*0.8) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }


    /**
     * 考试提醒
     */
    public static void showYqOne(Context context, String noticeStr, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_yqyl_one, null);

//        TextView  tvContent = view.findViewById(R.id.tv_exam_no_notice);
//        tvContent.setText(noticeStr);

        ImageView tvNo = view.findViewById(R.id.iv_close);
        NoScrollWebView webView_huodon = view.findViewById(R.id.webView_huodon);
                tvNo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });

        TextView tvSure = view.findViewById(R.id.tv_exam_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onClick(view);
                }
            }
        });
        WebViewUtil.setWebView(webView_huodon, Objects.requireNonNull(context));
        WebViewUtil.loadHtml(webView_huodon, noticeStr);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
        p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    /**
     * 考试提醒
     */
    public static void showYqTwo(Context context, String noticeStr,String one,String two, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_yqyl_two, null);

        NoScrollWebView webView_huodon = view.findViewById(R.id.webView_huodon);
        TextView  tvSp = view.findViewById(R.id.tv_sp);
        TextView  tvDs = view.findViewById(R.id.tv_ds);

        tvSp.setText(one);
        tvDs.setText(two);
        WebViewUtil.setWebView(webView_huodon, Objects.requireNonNull(context));
        WebViewUtil.loadHtml(webView_huodon, noticeStr);
        ImageView tvNo = view.findViewById(R.id.iv_close);
        tvNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        TextView tvSure = view.findViewById(R.id.tv_exam_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onClick(view);
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
        p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    /**
     * 考试提醒
     */
    public static void showQianDao(Context context, String one, String two, String three, String four, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_qian_dao, null);



        ImageView tvNo = view.findViewById(R.id.iv_close);
        TextView tvOne = view.findViewById(R.id.tv_one);
        TextView tvTwo = view.findViewById(R.id.tv_two);
        TextView tvThree = view.findViewById(R.id.tv_three);
        TextView tvFour = view.findViewById(R.id.tv_four);
        tvOne.setText("已连续签到"+one+"天");
        tvTwo.setText("+"+two);
        tvThree.setText("+"+three);
        if(four.equals("0")){
            tvFour.setText("你已连续签到8天可获得额外奖励哦");
        }else {
            tvFour.setText("再签到"+four+"天可获得额外奖励哦");
        }

        tvNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
        p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    /**
     * 考试提醒
     */
    public static void showYqThree(Context context, String noticeStr, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_yqyl_three, null);


        ImageView imgQrcode = view.findViewById(R.id.iv_img);
        ImageView tvNo = view.findViewById(R.id.iv_close);
        tvNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        GlideUtils.getInstances().loadNormalImg(context, imgQrcode, Constants.BASE_URLS + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, ""));


        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
        p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    /**
     * 考试提醒
     */
    public static void showYqFour(Context context, String url, OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_yqyl_four, null);


        ImageView imgQrcode = view.findViewById(R.id.iv_img);
        ImageView tvNo = view.findViewById(R.id.iv_close);
        tvNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        GlideUtils.getInstances().loadNormalImg(context, imgQrcode, url);


        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
        p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }
    private static Dialog dialog;
    private static   View ll_bg=null;

    public static void showYqFive(Context context, String url1,OnClickDialogListener listener) {

         if(dialog==null){
             dialog = new Dialog(context, R.style.loading_dialog);
             dialog.setCancelable(true);

             LayoutInflater inflater = LayoutInflater.from(context);
             View view = inflater.inflate(R.layout.dialog_yqyl_five, null);

              ll_bg = view.findViewById(R.id.ll_bg);
             TextView  tvSp = view.findViewById(R.id.tv_code);
             TextView  tvDs = view.findViewById(R.id.tv_name);

             tvSp.setText("邀请码:"+ShareUtil.getInstance().get(Constants.USER_PHONE));
             tvDs.setText(ShareUtil.getInstance().get(Constants.USER_NAME));

             ImageView tvNo = view.findViewById(R.id.iv_close);
             ImageView ivImg = view.findViewById(R.id.iv_img);
             ImageView iv_code = view.findViewById(R.id.iv_code);
             ImageView civ_user_avatar = view.findViewById(R.id.civ_user_avatar);
             GlideUtils.getInstances().loadNormalImg(context, iv_code, Constants.BASE_URLS + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, ""));
             GlideUtils.getInstances().loadNormalImg(context, ivImg, url1);
             GlideUtils.getInstances().loadNormalImg(context, civ_user_avatar, ShareUtil.getInstance().get(Constants.USER_HEAD));
             tvNo.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View view) {
                     dialog.dismiss();

                 }
             });
             ll_bg.setDrawingCacheEnabled(true);
             ll_bg.buildDrawingCache();
             TextView tvSure = view.findViewById(R.id.tv_exam_sure);
             tvSure.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View view) {
                     dialog.dismiss();
                     if(listener!=null){
                         listener.onClick(view);
                     }
                 }
             });
             dialog.setContentView(view);
             Window dialogWindow = dialog.getWindow();
             dialogWindow.setGravity(Gravity.CENTER);
             WindowManager wm = ((Activity)(context)).getWindowManager();
             Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
             WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
             p.width = (int) (d.getWidth()*1) ; // 宽度设置为屏幕的0.6
             p.height = (int) (d.getHeight()*1) ; // 宽度设置为屏幕的0.6
             dialogWindow.setAttributes(p);
             dialog.show();
         }else {
             dialog.show();
         }



    }


    public static  void creatPicture(Context  context,View view){
        viewSaveToImage(view,context);
    }
    public static  void creatPictures(Context  context,View view){

            viewSaveToImage(view,context);

    }
    public void savePicture(Bitmap bm, String fileName) {
        if (null == bm) {
            return;
        }
        File foder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/qufuuser");
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(foder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
            showToast("截图以保存到\n系统相册");
            //分享图片（myCaptureFile ）

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static Uri uri;
    public static void viewSaveToImage(View view,Context context) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        String fileName = Calendar.getInstance().getTimeInMillis() + ".png";
        // 把一个View转换成图片
        Bitmap bm = loadBitmapFromView(view);

        File sFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);
        if (!sFile.exists()) {
            File myCaptureFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);

            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把图片保存后声明这个广播事件通知系统相册有新图片到来
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            uri = Uri.fromFile(myCaptureFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
            ToastUtil.showToast("图片保存相册成功");


        } else {
            uri = Uri.fromFile(sFile);

        }


        view.destroyDrawingCache();
    }
    /**
     * 显示客服中心Dialog
     * @param context
     */
    public static void showCallCenterDialog(Context context, String phone,OnClickDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.call_center, null);

        TextView phone1 = view.findViewById(R.id.call_center_phone1);
        phone1.setText(phone);
        phone1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onClick(view);
                }
            }
        });
        TextView phone2 = view.findViewById(R.id.call_center_phone2);
        phone2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if(listener!=null){
                    listener.onClick(view);
                }
            }
        });
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*0.8) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);

        dialog.show();
    }
    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }
    // 2. 将布局转成bitmap
    public static void createPicture(View view) {
        Bitmap bitmap = view.getDrawingCache();
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        //3. 将bitmap存入本地
        String strPath = "/shareImg/" + UUID.randomUUID().toString() + ".png";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bbsc");;
            FileOutputStream fos = null;
            try {
                File file = new File(sdCardDir, strPath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(file);

                //当指定压缩格式为PNG时保存下来的图片显示正常
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                Log.e("MainActivity", "图片生成：" + file.getAbsolutePath());
                showToast("截图以保存到\n系统相册");
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                    if (!bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public interface OnCheckSexListener{
        void onClick(View v);
    }
    public interface OnClickDialogListener{
        void onClick(View v);
    }
    public interface OnClickDialogCoutsListener{
        void showCouts(String one,String two);
    }
}
