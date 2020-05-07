package com.smg.variety.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.BannerInfoDto;
import com.smg.variety.bean.BaseDto4;
import com.smg.variety.bean.RenWuBean;
import com.smg.variety.common.utils.GlideUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.view.MainActivity;
import com.smg.variety.view.adapter.RenWuAdapter;
import com.smg.variety.view.widgets.CustomDividerItemDecoration;
import com.smg.variety.view.widgets.autoview.MaxRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class AppRenWuActivity extends BaseActivity {


    @BindView(R.id.entity_store_push_recy)
    MaxRecyclerView entity_store_push_recy;
    @BindView(R.id.tv_one)
    TextView        tvOne;
    @BindView(R.id.tv_two)
    TextView        tvTwo;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_gz)
    TextView        tvGz;
    @BindView(R.id.iv_bg)
    ImageView       iv_bg;
    @BindView(R.id.ll_glod)
    LinearLayout    llGlod;
    @BindView(R.id.ll_money)
    LinearLayout    llMoney;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ren_wu;
    }

    @Override
    public void initView() {
        initAdapter();
    }


    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getTaskBaner();
        getTasks();
        getMoney();
        getGold();
    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llGlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppRenWuActivity.this, GlodMoneyDetailActivity.class);
                intent.putExtra("state",0);
                startActivity(intent);
            }
        });
        llMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppRenWuActivity.this, GlodMoneyDetailActivity.class);
                intent.putExtra("state",1);
                startActivity(intent);
            }
        });
        iv_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(SuperYqYlActivity.class);
            }
        });
        tvGz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppRenWuActivity.this, WebUtilsActivity.class);
                intent.putExtra("type", 7);
                startActivity(intent);
            }
        });
    }

    private void getGold() {
        showLoadDialog();
        DataManager.getInstance().getTotalInofs(new DefaultSingleObserver<BaseDto4>() {
            @Override
            public void onSuccess(BaseDto4 result) {
                dissLoadDialog();
                if (result.data != null) {
                    tvOne.setText(result.data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "task_reward", "gold");
    }

    private void getMoney() {
        showLoadDialog();
        DataManager.getInstance().getTotalInofs(new DefaultSingleObserver<BaseDto4>() {
            @Override
            public void onSuccess(BaseDto4 result) {
                dissLoadDialog();
                if (result.data != null) {
                    tvTwo.setText(result.data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "task_reward", "money");
    }

    private void getTaskBaner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {

                        GlideUtils.getInstances().loadNormalImg(AppRenWuActivity.this, iv_bg, result.getData().task_list_top.get(0).getPath());


                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "task_list_top");
    }

    private RenWuAdapter mEntityStoreAdapter;


    private void initAdapter() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(AppRenWuActivity.this);
        entity_store_push_recy.setLayoutManager(layoutManager);
        entity_store_push_recy.addItemDecoration(new CustomDividerItemDecoration(AppRenWuActivity.this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_f5f5f5_1));
        mEntityStoreAdapter = new RenWuAdapter(mlist, this);
        mEntityStoreAdapter.setTaskListener(new RenWuAdapter.ItemTaskListener() {
            @Override
            public void taskListener(RenWuBean bean) {
                goTask(bean);
            }
        });
        entity_store_push_recy.setAdapter(mEntityStoreAdapter);
    }

    public void goTask(RenWuBean bean) {
        String flag = bean.flag;
        String content = "";


        if (bean.task_status == 0) {
            if (flag.equals("task_welcome")) {

            } else if (flag.equals("task_set_avatar")) {
                gotoActivity(UserInfoActivity.class);
            } else if (flag.equals("task_set_name")) {
                gotoActivity(UserInfoActivity.class);
            } else if (flag.equals("task_complete_info")) {
                gotoActivity(UserInfoActivity.class);
            } else if (flag.equals("task_first_share")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 0);
                gotoActivity(MainActivity.class, true, bundle);
            } else if (flag.equals("task_first_follow")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 2);
                gotoActivity(MainActivity.class, true, bundle);
            } else if (flag.equals("task_first_favorite")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 0);
                gotoActivity(MainActivity.class, true, bundle);
            } else if (flag.equals("task_first_order")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 0);
                gotoActivity(MainActivity.class, true, bundle);
            } else if (flag.equals("task_first_comment")) {
                Intent intent = new Intent(AppRenWuActivity.this, OrderActivity.class);
                intent.putExtra("page", 4);
                startActivity(intent);
            } else if (flag.equals("task_daily_check_in")) {
                gotoActivity(AppQianDaoActivity.class);
            } else if (flag.equals("task_daily_share_product")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 0);
                gotoActivity(MainActivity.class, true, bundle);
            } else if (flag.equals("task_daily_invitation")) {
                gotoActivity(SuperYqYlActivity.class);
            } else if (flag.equals("task_daily_child_order")) {
                gotoActivity(MyQRcodeActivity.class);
            } else if (flag.equals("task_daily_watch_live")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 2);
                gotoActivity(MainActivity.class, true, bundle);
            } else if (flag.equals("task_daily_live")) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 2);
                gotoActivity(MainActivity.class, true, bundle);
            }
        } else if (bean.task_status == 1) {
            putDailyShare(bean.task_log_id);
        } else if (bean.task_status == 2) {
            ToastUtil.showToast("该任务已完成了哦！明天再来吧");
        }
    }

    private List<RenWuBean> mlist = new ArrayList<>();

    private void getTasks() {
        showLoadDialog();
        DataManager.getInstance().getTasks(new DefaultSingleObserver<HttpResult<RenWuBean>>() {
            @Override
            public void onSuccess(HttpResult<RenWuBean> httpResult) {
                mlist.clear();
                dissLoadDialog();
                if (httpResult != null && httpResult.getData() != null) {
                    if (httpResult.getData().newbie != null&&httpResult.getData().newbie.size()>0) {
                        RenWuBean renWuBean = new RenWuBean();
                        renWuBean.list = httpResult.getData().newbie;
                        renWuBean.poistion = 1;
                        mlist.add(renWuBean);
                    }
                    if (httpResult.getData().daily != null) {
                        RenWuBean renWuBean = new RenWuBean();
                        renWuBean.list = httpResult.getData().daily;
                        renWuBean.poistion = 2;
                        mlist.add(renWuBean);
                    }
                    mEntityStoreAdapter.setNewData(mlist);

                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    public void putDailyShare(String id) {

        DataManager.getInstance().putReward(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                getTasks();
                getMoney();
                getGold();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                getTasks();
                getMoney();
                getGold();
            }
        }, id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
