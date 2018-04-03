package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-05-01.
 * 充电详情
 */
public class ChargeDetail implements Serializable {

    private static final long serialVersionUID = 9124345197470840755L;

    private String chargeSn;//流水号
    private String chargerId;//电桩地址
    private String portNo;//充电枪口（插头）编号
    private String stationId;//站点id
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String duration;//充电时长 时分秒来表示
    private String userCard;//用户卡号
    private String startEm;//开始电表读数
    private String endEm;//结束电表读数
    private String thisReading;//充电电量
    private String thisAmountBefore;//结算前充电金额
    private String thisAmount;//结算后充电金额
    private String offlineBalanceStart;//充电前线下卡余额
    private String offlineBalanceEnd;//充电后线下卡余额
    private String charBillBefore;//结算前服务费
    private String charBill;//结算后服务费
    private String orderNo;//订单编号
    private String startType;//启动方式
    private String offlineIsPay;//是否结算
    private String chargeStartMode;//充电启动方式
    private String controlType;//控制类型

    //2016年11月25日14:57:56 新增
    private Charger chargerDetail;//桩 详情
    private ChargerStationDetail stationDetail;
    /**
     * 状态 0 开启充电中; 1 充电中 ：已确认桩回复；2 电充满未结束：如未拔枪则是此状态；
     * 3 关闭充电中; 4 充电结束 ：桩回复充电结束 ； 5 已生成账单；  6 已结算 ；
     * 7 已退款 ；8 充电失败。
     */
    private String status;


    public Charger getChargerDetail() {
        return chargerDetail;
    }

    public void setChargerDetail(Charger chargerDetail) {
        this.chargerDetail = chargerDetail;
    }

    public String getCharBill() {
        return charBill;
    }

    public void setCharBill(String charBill) {
        this.charBill = charBill;
    }

    public String getCharBillBefore() {
        return charBillBefore;
    }

    public void setCharBillBefore(String charBillBefore) {
        this.charBillBefore = charBillBefore;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getChargeSn() {
        return chargeSn;
    }

    public void setChargeSn(String chargeSn) {
        this.chargeSn = chargeSn;
    }

    public String getChargeStartMode() {
        return chargeStartMode;
    }

    public void setChargeStartMode(String chargeStartMode) {
        this.chargeStartMode = chargeStartMode;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEndEm() {
        return endEm;
    }

    public void setEndEm(String endEm) {
        this.endEm = endEm;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOfflineBalanceEnd() {
        return offlineBalanceEnd;
    }

    public void setOfflineBalanceEnd(String offlineBalanceEnd) {
        this.offlineBalanceEnd = offlineBalanceEnd;
    }

    public String getOfflineBalanceStart() {
        return offlineBalanceStart;
    }

    public void setOfflineBalanceStart(String offlineBalanceStart) {
        this.offlineBalanceStart = offlineBalanceStart;
    }

    public String getOfflineIsPay() {
        return offlineIsPay;
    }

    public void setOfflineIsPay(String offlineIsPay) {
        this.offlineIsPay = offlineIsPay;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getStartEm() {
        return startEm;
    }

    public void setStartEm(String startEm) {
        this.startEm = startEm;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThisAmount() {
        return thisAmount;
    }

    public void setThisAmount(String thisAmount) {
        this.thisAmount = thisAmount;
    }

    public String getThisAmountBefore() {
        return thisAmountBefore;
    }

    public void setThisAmountBefore(String thisAmountBefore) {
        this.thisAmountBefore = thisAmountBefore;
    }

    public String getThisReading() {
        return thisReading;
    }

    public void setThisReading(String thisReading) {
        this.thisReading = thisReading;
    }

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public ChargerStationDetail getStationDetail() {
        return stationDetail;
    }

    public void setStationDetail(ChargerStationDetail stationDetail) {
        this.stationDetail = stationDetail;
    }

    @Override
    public String toString() {
        return "ChargeDetail {" +
                "chargerDetail='" + chargerDetail + '\'' +
                "chargeSn='" + chargeSn + '\'' +
                ", chargerId='" + chargerId + '\'' +
                ", portNo='" + portNo + '\'' +
                ", stationId='" + stationId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration='" + duration + '\'' +
                ", userCard='" + userCard + '\'' +
                ", startEm='" + startEm + '\'' +
                ", endEm='" + endEm + '\'' +
                ", thisReading='" + thisReading + '\'' +
                ", thisAmountBefore='" + thisAmountBefore + '\'' +
                ", thisAmount='" + thisAmount + '\'' +
                ", offlineBalanceStart='" + offlineBalanceStart + '\'' +
                ", offlineBalanceEnd='" + offlineBalanceEnd + '\'' +
                ", charBillBefore='" + charBillBefore + '\'' +
                ", charBill='" + charBill + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", startType='" + startType + '\'' +
                ", offlineIsPay='" + offlineIsPay + '\'' +
                ", chargeStartMode='" + chargeStartMode + '\'' +
                ", controlType='" + controlType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
