package com.smg.variety.view.mainfragment.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.FriendListItemDto;
import com.smg.variety.bean.GroupAddBean;
import com.smg.variety.bean.GroupInfoDto;
import com.smg.variety.bean.GroupUserItemInfoDto;
import com.smg.variety.bean.SortBean;
import com.smg.variety.common.Constants;
import com.smg.variety.common.utils.PinyinUtils;
import com.smg.variety.common.utils.ToastUtil;
import com.smg.variety.common.PinyinComparator;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.error.ErrorUtil;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.http.response.HttpResult;
import com.smg.variety.utils.ShareUtil;
import com.smg.variety.view.adapter.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;

/**
 * 修改群成员
 */
public class ModifyGroupActivity extends BaseActivity {
    public static final int GROUP_DEL = 3000;
    public static final int GROUP_ADD = 3001;
    @BindView(R.id.iv_title_back)
    ImageView    iv_title_back;
    @BindView(R.id.contacts_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.add_friend_title)
    TextView     add_friend_title;
    @BindView(R.id.tv_number)
    TextView     tv_number;
    @BindView(R.id.btn_confirm)
    TextView     btn_confirm;
    private SortAdapter    adapter;
    private List<SortBean> selSortBeanList = new ArrayList<>();
    private ArrayList<SortBean> sourceDate;
    //根据拼音来排列RecyclerView里面的数据类
    private PinyinComparator pinyinComparator;
    LinearLayoutManager manager;
    private String userId;

    private        int                     type;
    private        String                  groupId;
    private static ArrayList<GroupAddBean> userLists;

    @Override
    public int getLayoutId() {
        return R.layout.ui_create_group_layout;
    }

    @Override
    public void initView() {
        pinyinComparator = new PinyinComparator();
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        groupId = getIntent().getExtras().getString(Constants.GROUP_ID);
        type = getIntent().getExtras().getInt(Constants.TYPE);
        userId = ShareUtil.getInstance().get(Constants.USER_ID);
    }

    @Override
    public void initData() {
        adapter = new SortAdapter(this, null);
        adapter.setSel(true);
        recyclerView.setAdapter(adapter);
        selSortBeanList.clear();
        if (type == GROUP_DEL) {
            add_friend_title.setText("删除成员");
            groupInfo();
        } else {
            add_friend_title.setText("添加成员");
            getFriendList();
        }
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SortBean item = (SortBean) adapter.getItem(position);
                item.setSel(!item.isSel());
                adapter.getmData().set(position, item);
                adapter.notifyDataSetChanged();
                deanSelBean(item);
            }
        });

        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(btn_confirm, () -> {
            if (selSortBeanList.size() == 0) {
                ToastUtil.showToast("请选择联系人");
                return;
            }
            if (type == GROUP_ADD) {
                addGroupUser();
            } else {
                delGroupUser();
            }
        });
    }

    /**
     * 获取用户好友信息列表
     */
    private void getFriendList() {
        showLoadDialog();
        DataManager.getInstance().getFriendList(new DefaultSingleObserver<HttpResult<ArrayList<FriendListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<FriendListItemDto>> result) {
                if (result != null) {
                    if (result.getData() != null) {
                        ArrayList<FriendListItemDto> fList = result.getData();
                        sourceDate = filledData(fList);
                        //根据a-z进行排序源数据
                        Collections.sort(sourceDate, pinyinComparator);
                        adapter.updateList(sourceDate);
                    }
                }
                dissLoadDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
                dissLoadDialog();
            }
        });
    }

    private void groupInfo() {
        showLoadDialog();
        DataManager.getInstance().getGroupInfo(new DefaultSingleObserver<HttpResult<GroupInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<GroupInfoDto> result) {
                dissLoadDialog();
                if(result.getData() != null){
                    ArrayList<GroupUserItemInfoDto> guiList = result.getData().getGroup_users().getData();
                    for(int i=0; i<guiList.size(); i++){
                       if(userId.equals(guiList.get(i).getUser_id())){
                           guiList.remove(i);
                           break;
                       }
                    }
                    sourceDate = filledDataGroupUser(guiList);
                    //根据a-z进行排序源数据
                    Collections.sort(sourceDate, pinyinComparator);
                    adapter.updateList(sourceDate);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, groupId);
    }

    /**
     * 封装RecyclerView数据
     * @return
     */
    private ArrayList<SortBean> filledDataGroupUser(ArrayList<GroupUserItemInfoDto> data) {
        ArrayList<SortBean> sortList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            SortBean item = new SortBean();

            item.setName(data.get(i).getGroup_nickname());
            item.setIcon(data.get(i).getUser().getData().getAvatar());
            item.setId(data.get(i).getUser_id());
            item.setPhone(data.get(i).getUser().getData().getPhone());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(TextUtils.isEmpty(item.getName()) ? "*" : item.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                item.setLetters(sortString.toUpperCase());
            } else {
                item.setLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }

    /**
     * 封装RecyclerView数据
     * @return
     */
    private ArrayList<SortBean> filledData(ArrayList<FriendListItemDto> date) {
        ArrayList<SortBean> sortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortBean item = new SortBean();
            if (!TextUtils.isEmpty(date.get(i).getRemark_name())) {
                item.setName(date.get(i).getRemark_name());
            } else {
                item.setName(date.get(i).getFriend_user().getData().getName());
            }
            item.setIcon(date.get(i).getFriend_user().getData().getAvatar());
            item.setId(date.get(i).getFriend_user_id());
            item.setPhone(date.get(i).getFriend_user().getData().getPhone());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(TextUtils.isEmpty(item.getName()) ? "*" : item.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                item.setLetters(sortString.toUpperCase());
            } else {
                item.setLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }

    private void deanSelBean(SortBean item) {
        if (item.isSel()) {
            selSortBeanList.add(item);
        } else {
            for (int i = 0; i < selSortBeanList.size(); i++) {
                SortBean sortBean = selSortBeanList.get(i);
                if (item.getId().equals(sortBean.getId())) {
                    selSortBeanList.remove(i);
                    break;
                }
            }
        }
        tv_number.setText("(" + selSortBeanList.size() + ")");
    }


    private List<String> getSelSorBeanIDs() {
        List<String> ids = new ArrayList<>();
        for (SortBean bean : selSortBeanList) {
            ids.add(bean.getId());
        }
        return ids;
    }

    private void addGroupUser() {
        showLoadDialog();
        DataManager.getInstance().addGroupUser(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("添加群成员成功");
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
            }
        },groupId, getSelSorBeanIDs());
    }

    private void delGroupUser() {
        showLoadDialog();
        DataManager.getInstance().delGroupUser(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("删除群成员成功");
                groupInfo();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
            }
        },groupId, getSelSorBeanIDs());
    }
}
