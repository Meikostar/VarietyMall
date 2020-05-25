package com.smg.variety.bean;

import java.io.Serializable;
import java.util.List;

public class MhSearchDto implements Serializable {
    private List<SearchFriendListDto> friend_list;
    private List<GroupListDto> group_list;

    public List<SearchFriendListDto> getFriend_list() {
        return friend_list;
    }

    public void setFriend_list(List<SearchFriendListDto> friend_list) {
        this.friend_list = friend_list;
    }

    public List<GroupListDto> getGroup_list() {
        return group_list;
    }

    public void setGroup_list(List<GroupListDto> group_list) {
        this.group_list = group_list;
    }
}
