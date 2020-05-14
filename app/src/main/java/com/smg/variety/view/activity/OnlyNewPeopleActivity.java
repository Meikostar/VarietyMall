package com.smg.variety.view.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.NewListItemDto;
import com.smg.variety.bean.NewPeopleBeam;
import com.smg.variety.bean.VideoLiveBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.SwipeRefreshLayoutUtil;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ApiException;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.TextUtil;
import com.smg.variety.view.adapter.BannerLiveAdapter;
import com.smg.variety.view.adapter.NewPeopleAdapter;
import com.smg.variety.view.adapter.ProductHotAdapter;
import com.smg.variety.view.widgets.autoview.EmptyView;
import com.smg.variety.view.widgets.autoview.MaxRecyclerView;
import com.smg.variety.view.widgets.autoview.SuperSwipeRefreshLayout;
import com.smg.variety.view.widgets.banner.BannerBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 *
 */
public class OnlyNewPeopleActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView         ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView          tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView          tvTitleRight;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_only_new_person;
    }

    @Override
    public void initView() {
        tvTitleText.setText("新人专享");
        initAdapter();

    }


    @Override
    public void initData() {
        getProductListData(TYPE_PULL_REFRESH);
    }

    @Override
    protected void onResume() {
        super.onResume();


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
    private List<NewListItemDto>       datas = new ArrayList<>();
    private HashMap<String, String> mParamsMaps;
    private boolean allGet;
    private void getProductListData(int loadtype) {
        showLoadDialog();
        mParamsMaps = new HashMap<>();
        mParamsMaps.put("page", mCurrentPage + "");
        mParamsMaps.put("include", "brand.category");
        mParamsMaps.put("filter[flag1]", 1+"");

//        if (TextUtil.isNotEmpty(id)&&!id.equals("-1")) {
//            mParamsMaps.put("filter[category_id]", id);
//        } else {
//            mParamsMaps.remove("filter[category_id]");
//        }


        DataManager.getInstance().findGoodsList(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> data) {
                dissLoadDialog();

                if (null != data.getData() && data.getData().size() > 0) {
                    onDataLoaded(loadtype, data.getData().size() == Constants.PAGE_SIZE, data.getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        });
    }
    private List<NewListItemDto> list = new ArrayList<>();
    public void onDataLoaded(int loadType, boolean haveNext, List<NewListItemDto> lists) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            list.addAll(lists);
        } else {
            list.addAll(lists);
        }

        mProcutAdapter.setNewData(list);

        mProcutAdapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (haveNext)
                        getProductListData(TYPE_PULL_MORE);
                    mSuperRecyclerView.hideMoreProgress();

                }
            }, 1);
        } else {
            if (mSuperRecyclerView != null) {
                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
            }


        }


    }



    private NewPeopleAdapter mEntityStoreAdapter;
    private int mCurrentPage = Constants.PAGE_NUM;
   private int currpage=1;
    private int                 mPage = 1;
    private LinearLayoutManager mLinearLayoutManager;

    private       SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int                                  TYPE_PULL_REFRESH = 888;
    private final int                                  TYPE_PULL_MORE    = 889;
    private       ProductHotAdapter                    mProcutAdapter;
    private void initAdapter() {
        mProcutAdapter=new ProductHotAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);

        mSuperRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mProcutAdapter);

        initHeaderView();
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                currpage = 1;
                getProductListData(TYPE_PULL_REFRESH);

                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }


            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);
    }
    private View mHeaderView;
    private void initHeaderView() {
        mHeaderView = View.inflate(this, R.layout.only_new_head_view, null);
        mProcutAdapter.addHeaderView(mHeaderView);

    }
    private List<NewPeopleBeam> mlist = new ArrayList<>();


}
