package com.smg.variety.bean;

public class ScoreIncomeBean {
//
//    {
//        "id": 35,
//            "name": "13755342886",
//            "email": null,
//            "avatar": null,
//            "phone": "13755342886",
//            "im_token": "3i53Tty+1sostwbnnmVKxVB0yHY9PsvJrULe1eyTwsw7qnOgvqq99Pb2Yxk9VqnzOiN0xm2HBASHoHYFJH65qg==",
//            "is_live": true,
//            "created_at": "2019-11-20 16:50:39",
//            "updated_at": "2019-11-20 17:14:29",
//            "sex": 0,
//            "level": 0,
//            "level_name": "注册会员",
//            "wechat_number": null,
//            "followersCount": 0,
//            "withdraw": 0,
//            "withdraw_bonus": 0,
//            "live_reward": 0,
//            "isFollowed": false,
//            "followingsCount": 0
//    }
public String id;
    public String name;
    public String email;
    public String avatar;
    public String phone;
    public String im_token;
    public String is_live;
    public String created_at;
    public String sex;
    public int level;
    public String level_name;
    public String wechat_number;
    public String withdraw;
    public String withdraw_bonus;
    public String live_reward;
    public String isFollowed;
    public String followingsCount;
    private String user_id;
    private String value;
    private String type;
    private String updated_at;
    private String type_id;
    private String wallet_type;
    private String type_name;
    private String wallet_type_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getWallet_type_name() {
        return wallet_type_name;
    }

    public void setWallet_type_name(String wallet_type_name) {
        this.wallet_type_name = wallet_type_name;
    }
}
