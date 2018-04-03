package custom.base.entity;


import java.io.Serializable;
import java.util.ArrayList;

import custom.base.entity.base.Images;

public class DzFriends implements Serializable {
    private static final long serialVersionUID = 2668780384400211561L;
    //	private String thumb;
    private int id;
    private String title;
    private String username;
    private String userphoto;
    private String gender;
    private ArrayList<Images> pics;
    private String content;
    private String addtime;
    private String day;
    private String time;
    private ArrayList<ShareLabel> share_label;

    private String user_id;
    private int pinglun_count;
    private ArrayList<Comment> pinglunlist;
    private int like_count;
    private ArrayList<UserSimpleInfo> dianzanlist;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getPinglun_count() {
        return pinglun_count;
    }

    public void setPinglun_count(int pinglun_count) {
        this.pinglun_count = pinglun_count;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public ArrayList<Images> getPics() {
        return pics;
    }

    public void setPics(ArrayList<Images> pics) {
        this.pics = pics;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public ArrayList<Comment> getPinglunlist() {
        return pinglunlist;
    }

    public void setPinglunlist(ArrayList<Comment> pinglunlist) {
        this.pinglunlist = pinglunlist;
    }

    public ArrayList<UserSimpleInfo> getDianzanlist() {
        return dianzanlist;
    }

    public void setDianzanlist(ArrayList<UserSimpleInfo> dianzanlist) {
        this.dianzanlist = dianzanlist;
    }

    public ArrayList<ShareLabel> getShare_label() {
        return share_label;
    }

    public void setShare_label(ArrayList<ShareLabel> share_label) {
        this.share_label = share_label;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "DzFriends [id=" + id + ", title=" + title + ", content=" + content + ", addtime=" + addtime
                + ", like_count=" + like_count + ", pinglun_count="
                + pinglun_count + ", username=" + username + ", user_id="
                + user_id + ", pics=" + pics + ", userphoto=" + userphoto
                + ", pinglunlist=" + pinglunlist + ", share_label=" + share_label + ", dianzanlist="
                + dianzanlist + ", day=" + day + ", time=" + time + ", gender="
                + gender + "]";
    }

}
