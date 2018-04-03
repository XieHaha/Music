package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-05-18.
 */
public class CarLocationInfo implements Serializable {

    private static final long serialVersionUID = -2037114307716065643L;
    private String vin;  //vin号码
    private String longitude;    //经度
    private String latitude;    //纬度
    private String speed;    //速度,精确到0.1km/h
    private String direction;    //方向，有效值范围：0～359，正北为0，顺时针
    private String altitude;    //海拔高度，单位为米(m)
    private String collectionTime;    //采集时间

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "CarLocationInfo {" +
                "vin='" + vin + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", speed='" + speed + '\'' +
                ", direction='" + direction + '\'' +
                ", altitude='" + altitude + '\'' +
                ", collectionTime='" + collectionTime + '\'' +
                '}';
    }

}
