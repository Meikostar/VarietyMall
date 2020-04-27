package com.smg.variety.bean;

import java.io.Serializable;

public class TopicUserDto implements Serializable {

    private  TopicUserInfoDto data;

    public TopicUserInfoDto getData() {
        return data;
    }

    public void setData(TopicUserInfoDto data) {
        this.data = data;
    }
}
