package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 2016/11/30.
 */

public class AddressCity implements Serializable {
    private static final long serialVersionUID = 6187558334411188557L;
    private String id;
    private String provinceCode;
    private String cityCode;
    private String cityName;
    private String sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "[AddressProvince:] id:" + id +
                ",provinceCode:" + provinceCode +
                ",cityCode:" + cityCode +
                ",cityName:" + cityName +
                ",sort:" + sort +
                "]";
    }
}
