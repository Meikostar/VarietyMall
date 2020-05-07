package com.smg.variety.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/7/8
 */
public class ConfigDto implements Serializable {
    private HotSearchDto hot_search;
    public ConfigDto rights_pull_new;
    public ConfigDto apk;
    public ConfigDto exchange;
    public String level_1;
    public String level_2;
    public String gold_money;
    public String update_msg;
    public String update_time;
    public String download_url;
    public String version;


    private List<String> st_search_distance;

    public HotSearchDto getHot_search() {
        return hot_search;
    }

    public void setHot_search(HotSearchDto hot_search) {
        this.hot_search = hot_search;
    }

    public List<String> getSt_search_distance() {
        return st_search_distance;
    }

    public void setSt_search_distance(List<String> st_search_distance) {
        this.st_search_distance = st_search_distance;
    }
}
