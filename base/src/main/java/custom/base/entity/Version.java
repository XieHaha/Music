package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/5/30.
 */
public class Version implements Serializable {
    private static final long serialVersionUID = -8438526479985188981L;

    private String remark;//备注
    private String downloadUrl;//下载地址
    private String osType;//操作系统类型 1android 2ios 3 win
    private String releaseTime;// 发布时间
    private String supportOsVersion;// 支持的操作系统版本
    private String versionCode;//版本号

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getSupportOsVersion() {
        return supportOsVersion;
    }

    public void setSupportOsVersion(String supportOsVersion) {
        this.supportOsVersion = supportOsVersion;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "Version{" +
                "remark='" + remark + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", osType='" + osType + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                ", supportOsVersion='" + supportOsVersion + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }
}
