package com.smg.variety.rong.chat;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;

import butterknife.BindView;

public class GroupNickActivity extends BaseActivity {
    public static final int MODIFY_GROUP_NICK = 3342;
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_right)
    TextView  mRightText;
    @BindView(R.id.ed_nick)
    EditText  ed_nick;
    private String groupId;
    private String groupNick;

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(mRightText, () -> {
            if (TextUtils.isEmpty(ed_nick.getText().toString())) {
                ToastUtil.showToast("请输入我在本群的群昵称");
                return;
            }
            edGroupNick(groupId,ed_nick.getText().toString());
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_nick_layout;
    }

    @Override
    public void initView() {
        mTitleText.setText("我在本群的昵称");
        mRightText.setVisibility(View.VISIBLE);
        mRightText.setText("完成");
    }

    @Override
    public void initData() {
        groupId = getIntent().getStringExtra(Constants.GROUP_ID);
        groupNick = getIntent().getStringExtra(Constants.GROUP_NICK);
        if(groupNick != null) {
            ed_nick.setText(groupNick);
        }
    }

    /**
     * 修改群公告
     */
    private void edGroupNick(String groupId, String groupName) {
        showLoadDialog();
        DataManager.getInstance().edGroupNick(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();

            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("修改成功");
                    Intent intent = new Intent();
                    intent.putExtra(Constants.GROUP_NICK, ed_nick.getText().toString());
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        }, groupId, groupName);
    }
}
