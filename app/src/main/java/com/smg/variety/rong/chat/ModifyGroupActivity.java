package com.smg.variety.rong.chat;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.smg.variety.view.widgets.autoview.ClearEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

/**
 * 添加群成员
 */
public class ModifyGroupActivity extends BaseActivity {
    public static final int GROUP_ADD = 3001;
    @BindView(R.id.iv_title_back)
    ImageView     iv_title_back;
    @BindView(R.id.contacts_recycler_view)
    RecyclerView  recyclerView;
    @BindView(R.id.add_friend_title)
    TextView      add_friend_title;
    @BindView(R.id.tv_number)
    TextView      tv_number;
    @BindView(R.id.btn_confirm)
    TextView      btn_confirm;
    @BindView(R.id.et_contacts_search_str)
    ClearEditText et_search_str;
    private SortAdapter    adapter;
    private List<SortBean> selSortBeanList = new ArrayList<>();
    private ArrayList<SortBean> sourceDate;
    //根据拼音来排列RecyclerView里面的数据类
    private PinyinComparator pinyinComparator;
    LinearLayoutManager manager;
    private String userId;
    private int  type;
    private String groupId;
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
        add_friend_title.setText("添加群成员");
        getFriendList();
    }

    @Override
    public void initListener() {

        //根据输入框输入值的改变来过滤搜索
        et_search_str.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable edit) {
            }
        });
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
            selSortBeanList.clear();
            List<SortBean> sortBeans = adapter.getmData();
            message="";
            int i=0;
            for(SortBean bean:sortBeans){
                if(bean.isSel()){
                    if(i==0){
                        message=bean.getName();
                    }else {
                        message=message+","+bean.getName();
                    }
                    selSortBeanList.add(bean);
                    i++;
                }
            }
            if (selSortBeanList.size() == 0) {
                ToastUtil.showToast("请选择联系人");
                return;
            }
            addGroupUser();
        });
    }
    private String message;
    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortBean> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {

            filterDateList = sourceDate;
        } else {

            filterDateList.clear();
            if (sourceDate != null && !sourceDate.isEmpty()) {
                for (SortBean item : sourceDate) {
                    String phone = item.getPhone() == null ? "*" : item.getPhone();
                    String name = item.getName() == null ? "*" : item.getName();
                    String remarkName = item.getRemarkName() == null ? "*" : item.getRemarkName();
                    if (phone.indexOf(filterStr.toString()) != -1 || name.indexOf(filterStr.toString()) != -1 ||remarkName.indexOf(filterStr.toString()) != -1 ||
                            PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                            //不区分大小写
                            || PinyinUtils.getFirstSpell(phone).toLowerCase().startsWith(filterStr.toString())
                            || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                            || PinyinUtils.getFirstSpell(remarkName).toUpperCase().startsWith(filterStr.toString())
                            ) {
                        filterDateList.add(item);
                    }
                }
            }
        }

        // 根据a-z进行排序
        if(filterDateList != null) {
            if(filterDateList.size() > 0) {
                Collections.sort(filterDateList, pinyinComparator);
                adapter.updateList(filterDateList);
            }
        }else{

        }
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
            item.setRemarkName(date.get(i).getRemark_name());
            item.setName(date.get(i).getFriend_user().getData().getName());
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
    /**
     * 小灰色条消息
     */
    private void sendOpenRedPacketMessage(String targetId, Conversation.ConversationType conversationType, String message) {
        InformationNotificationMessage myTextMessage = InformationNotificationMessage.obtain(message);
        Message myMessage = Message.obtain(targetId, conversationType, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, message, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
    }

    private void addGroupUser() {
        showLoadDialog();
        DataManager.getInstance().addGroupUser(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                sendOpenRedPacketMessage(groupId,Conversation.ConversationType.GROUP,message+"加入群聊");
                ToastUtil.showToast("添加群成员成功");

            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
            }
        },groupId, getSelSorBeanIDs());
    }
}
