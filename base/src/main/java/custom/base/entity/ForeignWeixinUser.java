package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/11/29.
 */

public class ForeignWeixinUser implements Serializable {
    private static final long serialVersionUID = 289064767297365304L;
    private String unionid;
    private String profile_image_url;
    private String country;
    private String screen_name;
    private String access_token;
    private String city;
    private String gender;//普通用户性别，1为男性，2为女性
    private String province;
    private String language;
    private String expires_in;
    private String refresh_token;
    private String openid;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "ForeignWeixinUser{" +
                "unionid='" + unionid + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", country='" + country + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", access_token='" + access_token + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", language='" + language + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                '}';
    }
}
