package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 2016/11/30.
 */

public class AddressProvince implements Serializable {
    private static final long serialVersionUID = 6187558334411188557L;
    private String id;
    private String countryCode;
    private String provinceCode;
    private String provinceName;
    private String sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
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
                ",countryCode:" + countryCode +
                ",provinceCode:" + provinceCode +
                ",provinceName:" + provinceName +
                ",sort:" + sort +
                "]";
    }
}
