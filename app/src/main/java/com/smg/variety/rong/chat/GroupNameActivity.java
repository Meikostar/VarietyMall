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
import com.smg.variety.eventbus.ChangeTitleEvent;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class GroupNameActivity extends BaseActivity {
    public static final int MODIFY_GROUP_NAME = 3322;
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_right)
    TextView  mRightText;
    @BindView(R.id.ed_name)
    EditText  ed_name;
    private String groupId;
    private String groupName;

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(mRightText, () -> {
            if (TextUtils.isEmpty(ed_name.getText().toString())) {
                ToastUtil.showToast("请输入群名称");
                return;
            }
            edGroupName(groupId,ed_name.getText().toString());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_name_layout;
    }

    @Override
    public void initView() {
        mTitleText.setText("修改群名称");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("完成");
    }

    @Override
    public void initData() {
        groupId = getIntent().getStringExtra(Constants.GROUP_ID);
        groupName = getIntent().getStringExtra(Constants.GROUP_NAME);
        ed_name.setText(groupName);
    }

    /**
     * 修改群名称
     */
    private void edGroupName(String groupId, String groupName) {
        showLoadDialog();
        DataManager.getInstance().edGroupName(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();

            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(ApiException.getInstance().isSuccess()){
                    ChangeTitleEvent logoutEvent = new ChangeTitleEvent();
                    logoutEvent.title=ed_name.getText().toString();
                    EventBus.getDefault().post(logoutEvent);
                    ToastUtil.showToast("修改成功");
                    Intent intent = new Intent();
                    intent.putExtra(Constants.GROUP_NAME, ed_name.getText().toString());
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    ToastUtil.showToast(getModifyGroupNameErrorMessage(throwable));
                }
            }
        }, groupId, groupName);
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
