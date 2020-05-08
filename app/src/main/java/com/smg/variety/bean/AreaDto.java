package com.smg.variety.bean;

import java.io.Serializable;

public class AreaDto implements Serializable {
    public String title;
    public String icon;
    public String logo;
    public String type;
    public String description;



    public String to;
    public String from;
    public String created_at;

    public boolean check;
    public boolean is_recommend;


    public int id;
    public  String name;
    long depth;
    int parent_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDepth() {
        return depth;
    }

    public void setDepth(long depth) {
        this.depth = depth;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
}
