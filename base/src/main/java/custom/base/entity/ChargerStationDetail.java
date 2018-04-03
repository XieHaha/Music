package custom.base.entity;

/**
 * Created by leeyx on 2014/9/28.
 */

import java.io.Serializable;
import java.util.ArrayList;

import custom.base.entity.base.ChargerStation;

public class ChargerStationDetail extends ChargerStation implements Serializable {
    private static final long serialVersionUID = 2668780384400211560L;
    private String circle;//楼圈
    private String establishTime;//建立时间
    private String collectionFlag;//是否收藏的标记
    private String parkingAddr;//停车场具体位置
    private String stationId;// 站点id
    private String stationName;//    站点名称
    private String operMerchantId;//    运营商ID
    private String operName;//运营商 名称
    private String districtCode;// 地区编码
    private String districtName;//   地区名称
    private String parkingType;//  停车处：1，停车楼 2，室外 3，地下室
    private String totalCharger;// 总电桩数
    private String stationType;//  电站类型 1直流 2交流 3混合
    private String booking;//   是否预约
    private String address;// 详细地址
    private String currentState;//   当前状态
    private String directNum;//    快充数量
    private String alternateNum;//        慢充数量
    private String telNo;//电话
    private String reviewState;//   审核状态
    private String reviewText;//   审核说明
    private String latitude;//  纬度
    private String longitude;//经度
    private String describes;//  电站说明
    private String grade;//星级
    private ArrayList<String> stationImages;//站点图片
    //====默认数据
    private String workOpenTime;//工作日开放时间
    private String holidayOpenTime;//节假日开放时间
    private String chargePriceDescription;//充电费描述
    private String servicePriceDescription;//服务费描述
    private String parkingDescription;//停车费描述


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(String establishTime) {
        this.establishTime = establishTime;
    }

    public String getCollectionFlag() {
        return collectionFlag;
    }

    public void setCollectionFlag(String collectionFlag) {
        this.collectionFlag = collectionFlag;
    }

    public String getParkingAddr() {
        return parkingAddr;
    }

    public void setParkingAddr(String parkingAddr) {
        this.parkingAddr = parkingAddr;
    }

    @Override
    public String getStationId() {
        return stationId;
    }

    @Override
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    @Override
    public String getStationName() {
        return stationName;
    }

    @Override
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public String getOperMerchantId() {
        return operMerchantId;
    }

    @Override
    public void setOperMerchantId(String operMerchantId) {
        this.operMerchantId = operMerchantId;
    }

    @Override
    public String getDistrictCode() {
        return districtCode;
    }

    @Override
    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    @Override
    public String getDistrictName() {
        return districtName;
    }

    @Override
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Override
    public String getParkingType() {
        return parkingType;
    }

    @Override
    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    @Override
    public String getTotalCharger() {
        return totalCharger;
    }

    @Override
    public void setTotalCharger(String totalCharger) {
        this.totalCharger = totalCharger;
    }

    @Override
    public String getStationType() {
        return stationType;
    }

    @Override
    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    @Override
    public String getBooking() {
        return booking;
    }

    @Override
    public void setBooking(String booking) {
        this.booking = booking;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getCurrentState() {
        return currentState;
    }

    @Override
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    @Override
    public String getDirectNum() {
        return directNum;
    }

    @Override
    public void setDirectNum(String directNum) {
        this.directNum = directNum;
    }

    @Override
    public String getAlternateNum() {
        return alternateNum;
    }

    @Override
    public void setAlternateNum(String alternateNum) {
        this.alternateNum = alternateNum;
    }

    @Override
    public String getTelNo() {
        return telNo;
    }

    @Override
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getReviewState() {
        return reviewState;
    }

    public void setReviewState(String reviewState) {
        this.reviewState = reviewState;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    @Override
    public String getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public ArrayList<String> getStationImages() {
        return stationImages;
    }

    public void setStationImages(ArrayList<String> stationImages) {
        this.stationImages = stationImages;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getWorkOpenTime() {
        return workOpenTime;
    }

    public void setWorkOpenTime(String workOpenTime) {
        this.workOpenTime = workOpenTime;
    }

    public String getHolidayOpenTime() {
        return holidayOpenTime;
    }

    public void setHolidayOpenTime(String holidayOpenTime) {
        this.holidayOpenTime = holidayOpenTime;
    }

    public String getChargePriceDescription() {
        return chargePriceDescription;
    }

    public void setChargePriceDescription(String chargePriceDescription) {
        this.chargePriceDescription = chargePriceDescription;
    }

    public String getServicePriceDescription() {
        return servicePriceDescription;
    }

    public void setServicePriceDescription(String servicePriceDescription) {
        this.servicePriceDescription = servicePriceDescription;
    }

    public String getParkingDescription() {
        return parkingDescription;
    }

    public void setParkingDescription(String parkingDescription) {
        this.parkingDescription = parkingDescription;
    }

    @Override
    public String toString() {
        return "ChargerStationDetail{" +
                "circle='" + circle + '\'' +
                ", establishTime='" + establishTime + '\'' +
                ", collectionFlag='" + collectionFlag + '\'' +
                ", parkingAddr='" + parkingAddr + '\'' +
                ", stationId='" + stationId + '\'' +
                ", stationName='" + stationName + '\'' +
                ", operMerchantId='" + operMerchantId + '\'' +
                ", operName='" + operName + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", districtName='" + districtName + '\'' +
                ", parkingType='" + parkingType + '\'' +
                ", totalCharger='" + totalCharger + '\'' +
                ", stationType='" + stationType + '\'' +
                ", booking='" + booking + '\'' +
                ", address='" + address + '\'' +
                ", currentState='" + currentState + '\'' +
                ", directNum='" + directNum + '\'' +
                ", alternateNum='" + alternateNum + '\'' +
                ", telNo='" + telNo + '\'' +
                ", reviewState='" + reviewState + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", describes='" + describes + '\'' +
                ", grade='" + grade + '\'' +
                ", stationImages=" + stationImages +
                ", workOpenTime='" + workOpenTime + '\'' +
                ", holidayOpenTime='" + holidayOpenTime + '\'' +
                ", chargePriceDescription='" + chargePriceDescription + '\'' +
                ", servicePriceDescription='" + servicePriceDescription + '\'' +
                ", parkingDescription='" + parkingDescription + '\'' +
                '}';
    }
}
