package com.smg.variety.bean;

public class RoomUserBean {
    private String id;
    public String user_id;
    public String chat_room_id;
    public String role;
    public String status;
    public String created_at;
    public RoomInfo user;
    public RoomInfo data;
    public String room_name;
    public String room_type;

    public String name;
    public String phone;
    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getChat_room_id() {
        return chat_room_id;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }
}
