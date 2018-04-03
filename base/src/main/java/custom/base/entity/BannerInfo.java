package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-03-17.
 */
public class BannerInfo implements Serializable {

    private static final long serialVersionUID = 4259144694275473793L;

    //Banner id
    private String bannerId;
    //Banner 类型
    private int bannerType;
    //Banner 地址
    private String url;

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public int getBannerType() {
        return bannerType;
    }

    public void setBannerType(int bannerType) {
        this.bannerType = bannerType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BannerInfo {" +
                "bannerId='" + bannerId + '\'' +
                ", bannerType='" + bannerType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
