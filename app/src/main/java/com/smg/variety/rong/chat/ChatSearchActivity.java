package com.smg.variety.rong.chat;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioButton;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.MhSearchDto;
import com.smg.variety.common.utils.InputUtil;
import com.smg.variety.http.DefaultSingleObserver;
import com.smg.variety.http.manager.DataManager;
import com.smg.variety.view.adapter.ChatFriendSearchAdapter;
import com.smg.variety.view.adapter.ChatGroupSearchAdapter;
import com.smg.variety.view.widgets.autoview.ClearEditText;
import com.smg.variety.view.widgets.autoview.EmptyView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 好友/群组模糊搜索
 */
public class ChatSearchActivity extends BaseActivity implements TextView.OnEditorActionListener {
    @BindView(R.id.et_search_procdut)
    ClearEditText et_search_procdut;
    @BindView(R.id.rb_chat_search_friend)
    RadioButton rb_chat_search_friend;
    @BindView(R.id.rb_chat_search_group)
    RadioButton rb_chat_search_group;
    @BindView(R.id.recyclerview_friend)
    RecyclerView recyclerview_friend;
    @BindView(R.id.recyclerview_group)
    RecyclerView recyclerview_group;
    private ChatFriendSearchAdapter friendAdapter;
    private ChatGroupSearchAdapter groupAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat_search;
    }

    @Override
    public void initView() {
        LinearLayoutManager fLayoutManager = new LinearLayoutManager(ChatSearchActivity.this);
        recyclerview_friend.setLayoutManager(fLayoutManager);
        friendAdapter = new ChatFriendSearchAdapter(ChatSearchActivity.this);
        recyclerview_friend.setAdapter(friendAdapter);

        LinearLayoutManager CLayoutManager = new LinearLayoutManager(ChatSearchActivity.this);
        recyclerview_group.setLayoutManager(CLayoutManager);
        groupAdapter = new ChatGroupSearchAdapter(ChatSearchActivity.this);
        recyclerview_group.setAdapter(groupAdapter);
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String from = bundle.getString("from_group");
            if(from.equals("from_group")){
                rb_chat_search_friend.setChecked(false);
                rb_chat_search_group.setChecked(true);
                recyclerview_friend.setVisibility(View.GONE);
                recyclerview_group.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initListener() {
        et_search_procdut.setOnEditorActionListener(this);
        et_search_procdut.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable edit) {

            }
        });
    }

    @OnClick({R.id.iv_search_back, R.id.rb_chat_search_friend, R.id.rb_chat_search_group})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.rb_chat_search_friend:
                recyclerview_friend.setVisibility(View.VISIBLE);
                recyclerview_group.setVisibility(View.GONE);
                break;
            case R.id.rb_chat_search_group:
                recyclerview_friend.setVisibility(View.GONE);
                recyclerview_group.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //防止两次发送请求
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
              switch (event.getAction()) {
                 case KeyEvent.ACTION_UP:
                     String searchStr = et_search_procdut.getText().toString();
                     getSearchLists(searchStr);
                     InputUtil.HideKeyboard(et_search_procdut);
                     return true;
                  default:
                     return true;
                }
            }
        return false;
    }

    private void getSearchLists(String name) {
        DataManager.getInstance().getMhSearchInfo(new DefaultSingleObserver<MhSearchDto>() {
            @Override
            public void onSuccess(MhSearchDto mhSearchDto) {
                dissLoadDialog();
                if (mhSearchDto == null || mhSearchDto.getFriend_list().size() == 0) {
                    friendAdapter.setNewData(null);
                    friendAdapter.setEmptyView(new EmptyView(ChatSearchActivity.this));
                }else{
                    friendAdapter.setNewData(mhSearchDto.getFriend_list());
                }
                if (mhSearchDto == null || mhSearchDto.getGroup_list().size() == 0) {
                    groupAdapter.setNewData(null);
                    groupAdapter.setEmptyView(new EmptyView(ChatSearchActivity.this));
                }else{
                    groupAdapter.setNewData(mhSearchDto.getGroup_list());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, name);
    }
}
