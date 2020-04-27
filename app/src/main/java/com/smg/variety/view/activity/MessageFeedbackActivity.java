package com.smg.variety.view.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.HeadLineDto;
import com.smg.variety.bean.MessageFeedbackTypeBean;
import com.smg.variety.common.utils.LogUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.RegexUtils;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.MessageFeedbackGridAdapter;
import com.smg.variety.view.widgets.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 留言反馈
 */
public class MessageFeedbackActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView         mTitleText;
    @BindView(R.id.gv_feedback_type)
    NoScrollGridView mGridView;
    @BindView(R.id.tv_feedback_content_num)
    TextView         mContentNum;
    @BindView(R.id.tv_submit)
    TextView         tv_submit;
    @BindView(R.id.iv_title_back)
    ImageView        iv_title_back;

    @BindView(R.id.et_feedback_content)
    EditText         mContent;
    private MessageFeedbackGridAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_feedback;
    }

    @Override
    public void initView() {
        mTitleText.setText("我要反馈");
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putFeedback();
            }
        });
        iv_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        getTypes();
    }



    private void getTypes() {
        //showLoadDialog();


        DataManager.getInstance().getTypes(new DefaultSingleObserver<HttpResult<List<String>>>() {
            @Override
            public void onSuccess(HttpResult<List<String>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        ArrayList<MessageFeedbackTypeBean> lists = new ArrayList<>();
                        int i=0;
                        for(String types:result.getData()){
                            MessageFeedbackTypeBean bean5 = new MessageFeedbackTypeBean();
                            bean5.setType(types);
                            if(i==0){
                                bean5.setSelect(true);
                                type=0+"";
                            }else {
                                bean5.setSelect(false);
                            }
                            lists.add(bean5);
                            i++;
                        }
                        mAdapter = new MessageFeedbackGridAdapter(MessageFeedbackActivity.this, lists);
                        mGridView.setAdapter(mAdapter);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private String type;
    private void putFeedback() {

        if(TextUtil.isEmpty(mContent.getText().toString().trim())){
            ToastUtil.showToast("请输入反馈内容");
            return;
        }
        if(TextUtil.isEmpty(type)){
            ToastUtil.showToast("请选择反馈类型");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("type",type);
        map.put("desc", mContent.getText().toString());

        DataManager.getInstance().putFeedback(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                ToastUtil.showToast("反馈成功");
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    ToastUtil.showToast("反馈成功");
                    finish();
                }
            }
        }, map);

    }
    @Override
    public void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.RefreshData(position);
                MessageFeedbackTypeBean bean = (MessageFeedbackTypeBean) mAdapter.getItem(position);
                type=position+"";
            }
        });

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mContentNum.setText("0/200");
                } else {
                    mContentNum.setText(s.toString().length() + "/200");
                }
            }
        });
    }


}
