package com.smg.variety.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

/**
 * desc:
 * 2018/6/30 17:05
 *
 * @author dahai.zhou
 * @since 2018/6/30
 */
public class StoreInfo {
    /*数据库字段*/

    public String       type;
    public String       shop_name;
    public String       name;
    public String       phone;
    public String       industry;
    public String       logo;
    public String       business_license;
    public String       license;
    public StoreInfo       ext;
    public List<String> id_cards;
    public List<String> credentials;




}
