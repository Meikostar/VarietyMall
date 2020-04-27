package com.smg.variety.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.DetailDto;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.CallPhone;
import com.smg.variety.view.adapter.HelperAdapter;
import com.smg.variety.view.widgets.autoview.ClearEditText;

import java.util.List;

import butterknife.BindView;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.et_search_str)
    ClearEditText  etSearchStr;
    @BindView(R.id.listview)
    ListView       listview;

    @Override
    public int getLayoutId() {
        return R.layout.activity_help_center;
    }
     private HelperAdapter mAdapter;
    @Override
    public void initView() {
        mTitleText.setText("帮助中心");
        getHelpInfo();

    }
    private void getHelpInfo() {
        DataManager.getInstance().getHelpData(new DefaultSingleObserver<HttpResult<List<DetailDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<DetailDto>> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {
                    mAdapter=new HelperAdapter(HelpCenterActivity.this,countOrderBean.getData());
                    listview.setAdapter(mAdapter);

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        },"help");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    @OnClick({R.id.iv_title_back
//            , R.id.rl_help_about_us
//            , R.id.rl_help_message_feedback
//            , R.id.rl_help_enterprise_profile
//            , R.id.rl_help_service_tel
//    })
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_title_back:
//                finish();
//                break;
//            case R.id.rl_help_about_us://关于我们
//                gotoActivity(AboutUsActivity.class);
//                break;
//            case R.id.rl_help_message_feedback://留言反馈
//                ToastUtil.showToast("留言反馈");
//                gotoActivity(MessageFeedbackActivity.class);
//                break;
//            case R.id.rl_help_enterprise_profile://企业简介
//                gotoActivity(EnterpriseProfileActivity.class);
//                break;
//            case R.id.rl_help_service_tel://拨打电话
//                callTel();
//                break;
//        }
//    }

    /**
     * 拨打客服电话
     */
    private void callTel() {
        CallPhone.diallPhone(this, "0755-123456000");
    }


}
