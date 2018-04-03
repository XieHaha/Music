package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/11/29.
 */

public class ForeignQQUser implements Serializable {
    private static final long serialVersionUID = 289064767297365304L;
    private String is_yellow_vip;
    private String screen_name;
    private String msg;
    private String vip;
    private String city;
    private String gender;
    private String province;
    private String is_yellow_year_vip;
    private String openid;
    private String yellow_vip_level;
    private String profile_image_url;
    private String access_token;
    private String uid;
    private String expires_in;
    private String level;

    public String getIs_yellow_vip() {
        return is_yellow_vip;
    }

    public void setIs_yellow_vip(String is_yellow_vip) {
        this.is_yellow_vip = is_yellow_vip;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getIs_yellow_year_vip() {
        return is_yellow_year_vip;
    }

    public void setIs_yellow_year_vip(String is_yellow_year_vip) {
        this.is_yellow_year_vip = is_yellow_year_vip;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getYellow_vip_level() {
        return yellow_vip_level;
    }

    public void setYellow_vip_level(String yellow_vip_level) {
        this.yellow_vip_level = yellow_vip_level;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    @Override
    public String toString() {
        return "ForeignQQUser{" +
                "is_yellow_vip='" + is_yellow_vip + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", msg='" + msg + '\'' +
                ", vip='" + vip + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", is_yellow_year_vip='" + is_yellow_year_vip + '\'' +
                ", openid='" + openid + '\'' +
                ", yellow_vip_level='" + yellow_vip_level + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", access_token='" + access_token + '\'' +
                ", uid='" + uid + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
