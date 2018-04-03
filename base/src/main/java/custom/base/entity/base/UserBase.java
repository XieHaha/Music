package custom.base.entity.base;

import java.io.Serializable;

/**
 * Created by luozi on 2016/3/25.
 */
public class UserBase implements Serializable {
    private static final long serialVersionUID = -3207032348457807867L;
    private String userId;//用户id
    private String userType;//用户类型
    private String nickName;//用户昵称
    private Images headImage;//头像地址
    private String signature;//签名
    private String sex;//性别  1男  0女
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Images getHeadImage() {
        return headImage;
    }

    public void setHeadImage(Images headImage) {
        this.headImage = headImage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserBase{" +
                "userId='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImage=" + headImage +
                ", signature='" + signature + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
