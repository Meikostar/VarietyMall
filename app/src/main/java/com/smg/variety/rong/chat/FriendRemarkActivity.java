package com.smg.variety.rong.chat;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class FriendRemarkActivity extends BaseActivity {
    public static final int MODIFY_FRIEND_REMARK = 4422;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.tv_title_right)
    TextView  mRightText;
    @BindView(R.id.ed_friend_remark)
    EditText  ed_friend_remark;
    private String friendId;
    private String friendRemark;

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(mRightText, () -> {
            if (TextUtils.isEmpty(ed_friend_remark.getText().toString())) {
                ToastUtil.showToast("必须有备注名");
                return;
            }
            edFriendRemark(friendId,ed_friend_remark.getText().toString());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_friend_remark_layout;
    }

    @Override
    public void initView() {
        mTitleText.setText("修改好友备注名");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("完成");
    }

    @Override
    public void initData() {
        friendId = getIntent().getStringExtra(Constants.FRIEND_ID);
        friendRemark = getIntent().getStringExtra(Constants.FRIEND_REMARK);
        ed_friend_remark.setText(friendRemark);
    }

    /**
     * 修改好友备注名
     */
    private void edFriendRemark(String friendId, String friendRemark) {
        showLoadDialog();
        DataManager.getInstance().edFriendRemark(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("修改成功");
                Intent intent = new Intent();
                intent.putExtra(Constants.FRIEND_REMARK, ed_friend_remark.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("修改成功");
                }
            }
        }, friendId, friendRemark);
    }

    public String getModifyGroupNameErrorMessage(Throwable throwable){
        String message = null;
        if(throwable instanceof HttpException){
            ResponseBody body = ((HttpException) throwable).response().errorBody();
            if(null != body){
                try {
                    String json = body.string();
                    JSONObject jsonObject = JSON.parseObject(json);
                    if(null != jsonObject.getString("errors")){
                        JSONObject errorsObject= JSON.parseObject(jsonObject.getString("errors"));
                        JSONArray array = errorsObject.getJSONArray("group_name");
                        message = array.getString(0);
                    }else{
                        message = (String) jsonObject.get("message");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    message = e.getMessage();
                }
            }
        }else {
            message = throwable.getMessage();
        }
        return message;
    }
}
