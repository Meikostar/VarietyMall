package com.smg.variety.view.activity;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.PersonalInfoDto;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;

import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.widgets.dialog.ShareModeDialog;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MyQRcodeActivity extends BaseActivity {


    String shareUrl;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.iv_img)
    ImageView      iv_img;

    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.tv_tgm)
    TextView       tvTgm;
    @BindView(R.id.img_qrcode)
    ImageView      imgQrcode;
    @BindView(R.id.ll_code)
    LinearLayout   llCode;
    @BindView(R.id.btn_save_qcode)
    TextView       btnSaveQcode;
    @BindView(R.id.ll_save)
    LinearLayout   llSave;
    @BindView(R.id.ll_share)
    LinearLayout   llShare;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_code;
    }

    @Override
    public void initView() {
        tvTitleText.setText("分享二维码");
        tvTgm.setText("邀请码:"+ShareUtil.getInstance().get(Constants.USER_PHONE));
        shareUrl = Constants.BASE_URLS + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, "");
        GlideUtils.getInstances().loadNormalImg(this, imgQrcode, Constants.BASE_URLS + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, ""));
    }

    @Override
    public void initData() {
        getUserInfo();
    }

    private void setUserInfo() {
        GlideUtils.getInstances().loadUserRoundImg(this, iv_img, mPersonalInfoDto.getAvatar());
        tvName.setText(mPersonalInfoDto.getName());
    }
    private PersonalInfoDto mPersonalInfoDto;
    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                      mPersonalInfoDto=personalInfoDto;
                    ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
                    if (!TextUtils.isEmpty(personalInfoDto.getName())) {
                        ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getName());
                    } else {
                        ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());
                    }
                    setUserInfo();


            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    @OnClick({R.id.iv_title_back,  R.id.btn_save_qcode,  R.id.ll_share})
    public void toClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.ll_share:
                ShareModeDialog dialog1 = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                    @Override
                    public void sureItem(int position) {
                        boolean isTimelineCb = false;
                        //http://bbsc.885505.com/api/package/user/invitation_img?user_id=14
                        String url = Constants.BASE_URLS+"h5/#/register?invite_code=from_phone_" + ShareUtil.getInstance().getString(Constants.USER_PHONE, "");
                        String title = "5G社交直播带货平台";
                        if (position == ShareModeDialog.SHARE_PYQ) {
                            isTimelineCb = true;
                        }
                        ShareUtil.sendToWeaChat(MyQRcodeActivity.this, isTimelineCb, title, url);
                    }
                });
                dialog1.show();
                break;
            case R.id.btn_save_qcode:
                downImg(shareUrl);
                break;
        }

    }

    private void downImg(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        showLoadDialog();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/love/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        final String pathFile = path + System.currentTimeMillis() + ".png";
        FileDownloader.getImpl().create(url)
                .setPath(pathFile, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "pending");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "progress" + soFarBytes + "totalBytes=" + totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        ToastUtil.showToast("保存失败");
                        dissLoadDialog();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        LogUtil.d("down", "connected");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        dissLoadDialog();
                        ToastUtil.showToast("保存成功");

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        LogUtil.d("down", "warn");
                    }
                }).start();
    }


}
