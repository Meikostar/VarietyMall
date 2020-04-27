package com.smg.variety.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.smg.variety.qiniu.live.utils.Config;

import java.io.Serializable;
import java.util.List;

/**
 * 文章 列表数据
 */
public class ArticleBean implements MultiItemEntity,Serializable {
    private long         createTime;//         1561189567246
    public String       des;//     工业安全很重要工业安全很重要工业安全很重要
    public String       id;//         0
    private int          imgNum;//         	3
    private List<String> imgs;//     Array
    public String       title;//     工业安全很重要
    public String       payMoney;//     工业安全很重要
    private String       time;//     工业安全很重要
    private String       articlePublicTime;//          (string, optional): 文章发布时间 ,
    private String       arttileId;//          (integer, optional): 文章id ,
    private int          commentNum;//      (integer, optional): 评论数 ,
    private String       content;//      (string, optional): 评论内容 ,
    private String       hasThumsUp;//              (string, optional): 是否点赞 Y/N ,
    private int          thumsUpNum;//                  (integer, optional): 点赞数 ,
    private String       userHeader;//                                  (string, optional): 用户头像 ,
    private String       userId;//              (string, optional): 用户id ,
    private String       userName;//
    public String       videoUrl;//    (string, optional): 修改时间
    public String       hasVideo;//    (string, optional): 修改时间
    public Integer       videoDuration;//    (string, optional): 修改时间
    public String       videoCover;//    (string, optional): 修改时间

    public String price;

    public String acitve;
    public String startTime;
    public String endTime;
    public String status;
    public String signupFormId;
    public String testPaperId;
    public String img;
    public String orders;
    public String articleId;
    public String viewTime;
    public String isFinished;

    //       "id": 1,
    //               "title": "考证报名",
    //               "des": "考证简介考证简介考证简介考证简介考证简介考证简介考证简介",
    //               "price": 1000,
    //               "createTime": 1584454007000,
    //               "acitve": "Y",
    //               "startTime": 1584454007000,
    //               "endTime": 1584799607000,
    //               "signupFormId": 1,
    //               "testPaperId": 1,
    //               "img": "http://image.fcaqyd.com/-810741002704.jpg",
    //               "orders": 1
    public Integer getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Integer videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(String hasVideo) {
        this.hasVideo = hasVideo;
    }

    public String getArticlePublicTime() {
        return articlePublicTime;
    }

    public void setArticlePublicTime(String articlePublicTime) {
        this.articlePublicTime = articlePublicTime;
    }

    public String getArttileId() {
        return arttileId;
    }

    public void setArttileId(String arttileId) {
        this.arttileId = arttileId;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHasThumsUp() {
        return hasThumsUp;
    }

    public void setHasThumsUp(String hasThumsUp) {
        this.hasThumsUp = hasThumsUp;
    }

    public int getThumsUpNum() {
        return thumsUpNum;
    }

    public void setThumsUpNum(int thumsUpNum) {
        this.thumsUpNum = thumsUpNum;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImgNum() {
        return imgNum;
    }

    public void setImgNum(int imgNum) {
        this.imgNum = imgNum;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return "Y".equals(hasVideo) ? Config.TYPE_VIDEO : imgNum > 1 ? Config.TYPE_MULTIPLE : Config.TYPE_SIMPLE;
    }
}
