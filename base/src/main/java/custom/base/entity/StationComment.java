package custom.base.entity;

import java.io.Serializable;

import custom.base.entity.base.UserBase;

/**
 * Created by thl on 2016-04-09.
 */
public class StationComment implements Serializable {
    private static final long serialVersionUID = -6103985623067150742L;
    private String commentId;//评论id
    private String stationId;//站点id
    private String commentTime;//添加时间
    private String address;//地址
    private String content;//内容
    private String grade;//星级
    private UserBase user;//用户信息
    private String auditFlag;//审核状态
    private String isThumbUp;//该用户是否已经点赞
    private int thumbUpNum;//点赞数
    private String replyCommentId;//回复评论
    private String replyNum;//回复数量
    private UserBase replyUser;//回复用户


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public UserBase getUser() {
        return user;
    }

    public void setUser(UserBase user) {
        this.user = user;
    }

    public String getAuditFlag() {
        return auditFlag;
    }

    public void setAuditFlag(String auditFlag) {
        this.auditFlag = auditFlag;
    }

    public boolean getIsThumbUp() {
        if ("true".equals(isThumbUp)) {
            return true;
        } else {
            return false;
        }
    }

    public void setThumbUp(boolean isThumbUp) {
        if (isThumbUp) {
            this.isThumbUp = "true";
        } else {
            this.isThumbUp = "false";
        }
    }

    public void setIsThumbUp(String isThumbUp) {
        this.isThumbUp = isThumbUp;
    }

    public int getThumbUpNum() {
        return thumbUpNum;
    }

    public void setThumbUpNum(int thumbUpNum) {
        this.thumbUpNum = thumbUpNum;
    }

    public String getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(String replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public UserBase getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserBase replyUser) {
        this.replyUser = replyUser;
    }

    @Override
    public String toString() {
        return "StationComment{" +
                "commentId='" + commentId + '\'' +
                ", stationId='" + stationId + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", address='" + address + '\'' +
                ", content='" + content + '\'' +
                ", grade='" + grade + '\'' +
                ", user=" + user +
                ", auditFlag='" + auditFlag + '\'' +
                ", isThumbUp='" + isThumbUp + '\'' +
                ", thumbUpNum=" + thumbUpNum +
                ", replyCommentId='" + replyCommentId + '\'' +
                ", replyNum='" + replyNum + '\'' +
                ", replyUser=" + replyUser +
                '}';
    }
}
