package custom.base.entity;

import java.io.Serializable;
import java.util.ArrayList;

import custom.base.entity.base.Images;
import custom.base.entity.base.UserBase;

/**
 * Created by thl on 2016-06-06.
 */
public class ChargerFriends implements Serializable {

    private static final long serialVersionUID = -6356682245301124561L;

    private String areaCode;//区域编码
    private String content;//评论内容
    private ArrayList<Images> images;//本次分享图片列表
    private ArrayList<ShareLabel> labels;//标签
    private String latitude;//区域编码
    private String longitude;//经度
    private String shareAddr;//纬度
    private String shareId;//分享ID
    private String shareTime;//分享时间
    private ArrayList<SharesCommentInfos> sharesCommentInfos;//分享评论列表
    private ArrayList<UserBase> thumbUpUserList;//点赞的用户列表
    private String userId;//本条评论的用户ID
    private UserBase userInfo;//本条分享用户

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public ArrayList<ShareLabel> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<ShareLabel> labels) {
        this.labels = labels;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getShareAddr() {
        return shareAddr;
    }

    public void setShareAddr(String shareAddr) {
        this.shareAddr = shareAddr;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public ArrayList<SharesCommentInfos> getSharesCommentInfos() {
        return sharesCommentInfos;
    }

    public void setSharesCommentInfos(ArrayList<SharesCommentInfos> sharesCommentInfos) {
        this.sharesCommentInfos = sharesCommentInfos;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public ArrayList<UserBase> getThumbUpUserList() {
        return thumbUpUserList;
    }

    public void setThumbUpUserList(ArrayList<UserBase> thumbUpUserList) {
        this.thumbUpUserList = thumbUpUserList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserBase getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserBase userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "ChargerFriends{ " +
                "areaCode='" + areaCode + '\'' +
                ", content='" + content + '\'' +
                ", images='" + images + '\'' +
                ", labels='" + labels + '\'' +
                ", latitude=" + latitude +
                ", longitude='" + longitude + '\'' +
                ", shareAddr='" + shareAddr + '\'' +
                ", shareId='" + shareId + '\'' +
                ", shareTime=" + shareTime +
                ", sharesCommentInfos='" + sharesCommentInfos + '\'' +
                ", thumbUpUserList='" + thumbUpUserList + '\'' +
                ", userId='" + userId + '\'' +
                ", userInfo='" + userInfo + '\'' +
                '}';
    }


}
