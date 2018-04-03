package custom.base.entity;

/**
 * Created by Administrator on 2015/9/21.
 */
public class ZhuangyouSimpleInfo {

    private int user_id = -1;
    private String nickname = "";
    private String profile_image_url = "";
    private String gender = "";
    private String car_modle = "";
    private String charge_port_type = "";
    private String distance = "";
    private int is_att = 0;
    private String signature;


    public String getCar_modle() {
        return car_modle;
    }

    public void setCar_modle(String car_modle) {
        this.car_modle = car_modle;
    }

    public String getCharge_port_type() {
        return charge_port_type;
    }

    public void setCharge_port_type(String charge_port_type) {
        this.charge_port_type = charge_port_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIsatt() {
        return is_att;
    }

    public void setIsatt(int isatt) {
        this.is_att = isatt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "ZhuangyouSimpleInfo{" +
                "user_id=" + user_id +
                ", nickname='" + nickname + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", gender='" + gender + '\'' +
                ", car_modle='" + car_modle + '\'' +
                ", charge_port_type='" + charge_port_type + '\'' +
                ", distance='" + distance + '\'' +
                ", is_att=" + is_att +
                ", signature='" + signature + '\'' +
                '}';
    }
}
