package com.smg.variety.bean;

import java.io.Serializable;

public class GroupOwnerInfoDto implements Serializable{
    private GroupOwnerItemInfoDto  data;

    public GroupOwnerItemInfoDto getData() {
        return data;
    }

    public void setData(GroupOwnerItemInfoDto data) {
        this.data = data;
    }
}
