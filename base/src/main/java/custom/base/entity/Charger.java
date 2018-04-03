package custom.base.entity;

import java.io.Serializable;
import java.util.List;

import custom.base.entity.base.Plug;

/**
 * Created by luozi on 2016/4/21.
 * 桩详情
 */
public class Charger implements Serializable {
    private static final long serialVersionUID = 2245198163258036666L;
    private String chargerId;//电桩号
    private String chargerIp;//桩ip地址
    private String devSn;//出厂序列号
    private String encryptType;//加密方式
    private String operMerchantId;//运营商ID
    private List<Plug> plugNoList;//枪头列表
    private String portQuantity;//总插头数量
    private String protocolType;//通讯协议类型
    private String protocolVersion;//通讯协议版本号
    private String qrCode;//二维码号
    private String serialNo;//桩编号
    private String softwareVersion;//软件版本
    private String wifiAp;//是否有wifi热点
    private String chargerType;//交流直流 (1：直流   2：交流  3:混合)
    private String stationId;//站id
    private String isParallelCharge;//多枪是否可同时充电
    private String installationTime;//桩安装时间
    private String cardWifi;//wifi编号
    private String activeFlag;//是否已激活
    private String isAllowOpen;//桩是否对外开放
    private String unregisterReason;//注销原因
    private String reviewState;//审核状态
    private String reviewText;//审核说明

    //2016年11月25日15:02:24 新增
    private String stationName;//站名称

    private PileDevice deviceType; //桩 设备

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public PileDevice getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(PileDevice deviceType) {
        this.deviceType = deviceType;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getChargerIp() {
        return chargerIp;
    }

    public void setChargerIp(String chargerIp) {
        this.chargerIp = chargerIp;
    }

    public String getDevSn() {
        return devSn;
    }

    public void setDevSn(String devSn) {
        this.devSn = devSn;
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType;
    }

    public String getOperMerchantId() {
        return operMerchantId;
    }

    public void setOperMerchantId(String operMerchantId) {
        this.operMerchantId = operMerchantId;
    }

    public List<Plug> getPlugNoList() {
        return plugNoList;
    }

    public void setPlugNoList(List<Plug> plugNoList) {
        this.plugNoList = plugNoList;
    }

    public String getPortQuantity() {
        return portQuantity;
    }

    public void setPortQuantity(String portQuantity) {
        this.portQuantity = portQuantity;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getWifiAp() {
        return wifiAp;
    }

    public void setWifiAp(String wifiAp) {
        this.wifiAp = wifiAp;
    }

    public String getChargerType() {
        return chargerType;
    }

    public void setChargerType(String chargerType) {
        this.chargerType = chargerType;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getIsParallelCharge() {
        return isParallelCharge;
    }

    public void setIsParallelCharge(String isParallelCharge) {
        this.isParallelCharge = isParallelCharge;
    }

    public String getInstallationTime() {
        return installationTime;
    }

    public void setInstallationTime(String installationTime) {
        this.installationTime = installationTime;
    }

    public String getCardWifi() {
        return cardWifi;
    }

    public void setCardWifi(String cardWifi) {
        this.cardWifi = cardWifi;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getIsAllowOpen() {
        return isAllowOpen;
    }

    public void setIsAllowOpen(String isAllowOpen) {
        this.isAllowOpen = isAllowOpen;
    }

    public String getUnregisterReason() {
        return unregisterReason;
    }

    public void setUnregisterReason(String unregisterReason) {
        this.unregisterReason = unregisterReason;
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
    public String toString() {
        return "Charger{" +
                "stationName='" + stationName + '\'' +
                "chargerId='" + chargerId + '\'' +
                ", chargerIp='" + chargerIp + '\'' +
                ", devSn='" + devSn + '\'' +
                ", encryptType='" + encryptType + '\'' +
                ", operMerchantId='" + operMerchantId + '\'' +
                ", plugNoList=" + plugNoList +
                ", portQuantity='" + portQuantity + '\'' +
                ", protocolType='" + protocolType + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", wifiAp='" + wifiAp + '\'' +
                ", chargerType='" + chargerType + '\'' +
                ", stationId='" + stationId + '\'' +
                ", isParallelCharge=" + isParallelCharge +
                ", installationTime='" + installationTime + '\'' +
                ", cardWifi='" + cardWifi + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                ", isAllowOpen='" + isAllowOpen + '\'' +
                ", unregisterReason='" + unregisterReason + '\'' +
                ", reviewState='" + reviewState + '\'' +
                ", reviewText='" + reviewText + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
