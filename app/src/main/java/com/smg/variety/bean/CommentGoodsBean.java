package com.smg.variety.bean;

/**
 * 我的 商品评价
 */
public class CommentGoodsBean {

    private String    comment;//textcomment
    private Commented commented;//Object
    private int       commented_id;//            1
    private String    commented_type;//            Modules\Project\Entities\Post
    private String    created_at;//        2019-05-29 10:43:31
    private String    flag;//    1559097811
    private int       id;//	2
    private int       user_id;//            1

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Commented getCommented() {
        return commented;
    }

    public void setCommented(Commented commented) {
        this.commented = commented;
    }

    public int getCommented_id() {
        return commented_id;
    }

    public void setCommented_id(int commented_id) {
        this.commented_id = commented_id;
    }

    public String getCommented_type() {
        return commented_type;
    }

    public void setCommented_type(String commented_type) {
        this.commented_type = commented_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
