package com.smg.variety.bean;

import java.io.Serializable;

/**
 * Created by rzb on 2019/7/10.
 */
public class NoticeDetailDto implements Serializable {
    public String send_user_id;
    public String content;
    public String title;
    public String opt_type;
    public String msg_type;
    public String subject;
    public String created_at;
    public String type;
    public String id;

    public NoticOrderImgDto data;

    public String getSend_user_id() {
        return send_user_id;
    }

    public void setSend_user_id(String send_user_id) {
        this.send_user_id = send_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOpt_type() {
        return opt_type;
    }

    public void setOpt_type(String opt_type) {
        this.opt_type = opt_type;
    }

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NoticOrderImgDto getData() {
        return data;
    }

    public void setData(NoticOrderImgDto data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }
}
