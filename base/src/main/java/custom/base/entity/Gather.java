package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/4/12.
 */
public class Gather implements Serializable {
    private static final long serialVersionUID = 1207020284932632674L;
    private int code;//编码
    private int size = 0;//大小
    private String name;//名称
    private double lat, lng;//经纬度

    public int getCode() {
        return code;
    }

    public Gather setCode(int code) {
        this.code = code;
        return this;
    }

    public int getSize() {
        return size;
    }

    public Gather setSize(int size) {
        this.size = size;
        return this;
    }

    public String getName() {
        return name;
    }

    public Gather setName(String name) {
        this.name = name;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Gather setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Gather setLng(double lng) {
        this.lng = lng;
        return this;
    }

    /**
     * 大小自增长
     */
    public void sizeSelfAdd() {
        size++;
    }

    @Override
    public String toString() {
        return "Gather{" +
                "code=" + code +
                ", size=" + size +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
