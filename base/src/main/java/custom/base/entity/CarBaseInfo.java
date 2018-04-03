package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-05-17.
 */
public class CarBaseInfo implements Serializable {

    private static final long serialVersionUID = -3528627678964224520L;

    private String vin;    //车辆识别码
    private String vehicleModelName;    //车型名称
    private String manufacturerName;    //厂商名称
    private String plateNo;    //车牌号码
    private String plateColor;    //车牌颜色
    private String buyDate;    //购买日期

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    @Override
    public String toString() {
        return "CarBaseInfo {" +
                "vin='" + vin + '\'' +
                ", vehicleModelName='" + vehicleModelName + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", plateNo='" + plateNo + '\'' +
                ", plateColor='" + plateColor + '\'' +
                ", buyDate='" + buyDate + '\'' +
                '}';
    }

}
