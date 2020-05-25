package com.smg.variety.rong.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.smg.variety.R;
import com.smg.variety.base.BaseActivity;
import com.smg.variety.bean.SortBean;
import com.smg.variety.view.adapter.GroupPacketMembersAapter;
import com.smg.variety.view.widgets.autoview.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupRedPacketMembersActivity extends BaseActivity {
    public static final int GROUP_REDPACKET_MEMBERS_NO = 5777;
    public static final int GROUP_REDPACKET_MEMBERS_HAVE = 5776;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.btn_edit)
    TextView btn_edit;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.group_members_gv)
    NoScrollGridView group_members_gv;

    private GroupPacketMembersAapter mGroupMembersAapter;
    private List<SortBean> userLists = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.ui_group_redpacket_members_layout;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            Intent mIntent = new Intent();
            ArrayList<SortBean> sbList = new ArrayList();
            if(userLists != null) {
                if(userLists.size() > 0) {
                    for (int j = 0; j < userLists.size(); j++) {
                        sbList.add(userLists.get(j));
                    }
                }
            }
            mIntent.putExtra("group_redpacket_members", sbList);
            setResult(Activity.RESULT_OK, mIntent);
            finish();
        });

        bindClickEvent(btn_edit, () -> {
            if(userLists != null) {
//                if (userLists.size() == 0) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("from", "group");
//                    gotoActivity(EditGroupRedPaketMembersActivity.class, false, bundle, GROUP_REDPACKET_MEMBERS_NO);
//                } else {
//                    Bundle bundle = new Bundle();
//                    ArrayList<SortBean> uList = new ArrayList();
//                    for(int j=0; j<userLists.size(); j++){
//                        uList.add(userLists.get(j));
//                    }
//                    bundle.putSerializable("userLists", uList);
//                    bundle.putString("from", "group");
//                    gotoActivity(EditGroupRedPaketMembersActivity.class, false, bundle, GROUP_REDPACKET_MEMBERS_HAVE);
//                }
            }
        });

        mGroupMembersAapter.setRefreshListsListener(new GroupPacketMembersAapter.RefreshListsListener() {
            @Override
            public void OnRefreshListener(SortBean sortBean) {
                if(userLists != null){
                    userLists.remove(sortBean);
                    tv_number.setText("("+userLists.size()+")");
                    mGroupMembersAapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<SortBean> sList = (ArrayList<SortBean>) bundle.getSerializable("userLists");
            userLists.addAll(sList);
            mGroupMembersAapter.notifyDataSetChanged();
        }
    }

    private void initAdapter(){
        mGroupMembersAapter = new GroupPacketMembersAapter(GroupRedPacketMembersActivity.this, userLists);
        group_members_gv.setAdapter(mGroupMembersAapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if((resultCode == Activity.RESULT_OK && requestCode == GROUP_REDPACKET_MEMBERS_NO &&  data != null)){
            ArrayList<SortBean> sList = (ArrayList<SortBean>) data.getSerializableExtra("group_redpacket_members");
            for(int i=0; i<sList.size(); i++){
                userLists.add(sList.get(i));
            }
            tv_number.setText("("+userLists.size()+")");
            mGroupMembersAapter.notifyDataSetChanged();
        }

        if((resultCode == Activity.RESULT_OK && requestCode == GROUP_REDPACKET_MEMBERS_HAVE && data != null)){
            ArrayList<SortBean> sList = (ArrayList<SortBean>) data.getSerializableExtra("group_redpacket_members");
            if(sList.size() > 0) {
                if(userLists.size() > 0) {
                    for (int i = 0; i < sList.size(); i++) {
                        for(int j=0; j<userLists.size(); j++){
                            if(userLists.get(j).getId().equals(sList.get(i).getId())){
                                sList.remove(i);
                                continue;
                            }
                        }
                    }
                }
            }
            userLists.addAll(sList);
            tv_number.setText("("+userLists.size()+")");
            mGroupMembersAapter.notifyDataSetChanged();
        }
    }
}