package com.smg.variety.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/8/22.
 */
public class MapDto implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}