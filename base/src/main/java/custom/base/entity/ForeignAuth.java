package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/12/5.
 */

public class ForeignAuth  implements Serializable{

    private static final long serialVersionUID = -3668866339976840102L;
    /**
     * 绑定类型
     * */
    private String bindType;
    /**
     * 用户id
     * */
    private String userId;
    /**
     * 第三方用户id
     * */
    private String thirdUuuid;
    /**
     * 是否有效
     * */
    private int isValidate;
    /**
     * 绑定时间
     * */
    private long bindTime;

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThirdUuuid() {
        return thirdUuuid;
    }

    public void setThirdUuuid(String thirdUuuid) {
        this.thirdUuuid = thirdUuuid;
    }

    public int getIsValidate() {
        return isValidate;
    }

    public void setIsValidate(int isValidate) {
        this.isValidate = isValidate;
    }

    public long getBindTime() {
        return bindTime;
    }

    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }

    @Override
    public String toString() {
        return "ForeignAuth{" +
                "bindType='" + bindType + '\'' +
                ", userId='" + userId + '\'' +
                ", thirdUuuid='" + thirdUuuid + '\'' +
                ", isValidate=" + isValidate +
                ", bindTime=" + bindTime +
                '}';
    }
}
