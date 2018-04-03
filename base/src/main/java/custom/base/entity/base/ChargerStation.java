package custom.base.entity.base;

import java.io.Serializable;

/**
 * 电桩基类
 * Created by luozi on 2015/9/8.
 */
public class ChargerStation implements Serializable {
    private static final long serialVersionUID = -3971650267699830211L;
    private String booking = "";//是否可预约 1可以 0不可以
    private String currentState;//状态 1运营总 2安装中 3规划中
    private String operMerchantId;//充电运营服务商ID
    private String stationId;//站点id
    private String totalCharger;//总电桩数量
    private String districtName;//区县名称
    private String districtCode;//区县代码
    private String alternateNum;//交流数量
    private String directNum;//直流数量
    private String address;//详细地址
    private String parkingType;//停车处：1，停车楼 2，室外 3，地下室
    private String stationName;//站点名称
    private String telNo;//自营电话
    private String stationType;//站点类型 1直流2交流3混合
    private String provinceId;//省份id
    private String latitude;//维度
    private String longitude;//经度
    //========本地距离计算所需要的参数
    private double distance;//距离,单位为m
    private int chargePortType;//充电接口类型
    private String score;//评分

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getOperMerchantId() {
        return operMerchantId;
    }

    public void setOperMerchantId(String operMerchantId) {
        this.operMerchantId = operMerchantId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getTotalCharger() {
        return totalCharger;
    }

    public void setTotalCharger(String totalCharger) {
        this.totalCharger = totalCharger;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getAlternateNum() {
        return alternateNum;
    }

    public void setAlternateNum(String alternateNum) {
        this.alternateNum = alternateNum;
    }

    public String getDirectNum() {
        return directNum;
    }

    public void setDirectNum(String directNum) {
        this.directNum = directNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getChargePortType() {
        return chargePortType;
    }

    public void setChargePortType(int chargePortType) {
        this.chargePortType = chargePortType;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ChargerStation{" +
                "booking='" + booking + '\'' +
                ", currentState='" + currentState + '\'' +
                ", operMerchantId='" + operMerchantId + '\'' +
                ", stationId='" + stationId + '\'' +
                ", totalCharger='" + totalCharger + '\'' +
                ", districtName='" + districtName + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", alternateNum='" + alternateNum + '\'' +
                ", directNum='" + directNum + '\'' +
                ", address='" + address + '\'' +
                ", parkingType='" + parkingType + '\'' +
                ", stationName='" + stationName + '\'' +
                ", telNo='" + telNo + '\'' +
                ", stationType='" + stationType + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", distance=" + distance +
                ", chargePortType=" + chargePortType +
                ", score='" + score + '\'' +
                '}';
    }
}
