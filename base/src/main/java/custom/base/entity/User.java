package custom.base.entity;

import java.io.Serializable;

import custom.base.entity.base.UserBase;

/**
 * @ClassName: UserInfo
 * @Description: 用户信息
 */
public class User extends UserBase implements Serializable {
    private static final long serialVersionUID = -8118856362323651092L;

    private String phone;//手机号
    private String address;//地址
    private String latitude;//纬度
    private String longitude;//经度
    private String userCard;//用户卡号
    private String setPasswordFlag;//是否设置密码标记
    private String token;//token

    private String email;//email
    private String carTypeId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(String carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public String getSetPasswordFlag() {
        return setPasswordFlag;
    }

    public void setSetPasswordFlag(String setPasswordFlag) {
        this.setPasswordFlag = setPasswordFlag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", userCard='" + userCard + '\'' +
                ", setPasswordFlag='" + setPasswordFlag + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", carTypeId='" + carTypeId + '\'' +
                '}';
    }
}
