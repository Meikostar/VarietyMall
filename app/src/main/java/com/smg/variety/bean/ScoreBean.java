package com.smg.variety.bean;

import java.util.List;

public class ScoreBean {
    private String gift_name;
    private String thumb;
    private String price;
    public String day;
    public String tip;
    public boolean checked;
    public boolean today_check;
    public String reward_msg;

    public List<ScoreBean> days;
    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
