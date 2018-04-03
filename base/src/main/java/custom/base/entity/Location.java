package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/3/31.
 */
public class Location implements Serializable {
    private static final long serialVersionUID = -4313498035444508530L;

    private String province;//省份
    private String city;//城市
    private String shortAddress;//地址
    private String country;//国家
    private double lat;//纬度
    private double lng;//经度
    private String slat;//纬度
    private String slng;//经度
    private String desc;//地址详细描述
    private String cityCode;//城市编码

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShortAddress() {
        return shortAddress;
    }

    public void setShortAddress(String shortAddress) {
        this.shortAddress = shortAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getSlat() {
        return slat;
    }

    public void setSlat(String slat) {
        this.slat = slat;
    }

    public String getSlng() {
        return slng;
    }

    public void setSlng(String slng) {
        this.slng = slng;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    @Override
    public String toString() {
        return "Location{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", shortAddress='" + shortAddress + '\'' +
                ", country='" + country + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", slat='" + slat + '\'' +
                ", slng='" + slng + '\'' +
                ", desc='" + desc + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }
}
