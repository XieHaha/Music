package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 16/5/1.
 */
public class BookingDetail implements Serializable {
    private static final long serialVersionUID = -2371361311181976047L;


    private String reservationSn;

    private String userCard;//用户卡号

    private String plugNo;
    private String startTime;//开始时间
    private String estimateEndTime;//理论结束时间
    private String actualEndTime;//实际结束时间
    private String duration;//预约时长
    private String bespeakDealBefore;//预约结算前费用
    private String bespeakDealCharge;//最终预约结算费用
    private String stationId;//站点ID
    private String chargerId;//电桩地址
    private String orderNo;//订单号
    private String isDone;//是否完成
    /**
     * 1： 预约中
     * 2： 已预约
     * 3： 取消预约中
     * 4 ：已取消预约
     * 5： 充电
     * 流程1：1-2-3-4
     * 流程2：1-2-5
     */
    private String status;

    private Charger chargerDetail;
    private ChargerStationDetail stationDetail;

    public String getReservationSn() {
        return reservationSn;
    }

    public void setReservationSn(String reservationSn) {
        this.reservationSn = reservationSn;
    }

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public String getPlugNo() {
        return plugNo;
    }

    public void setPlugNo(String plugNo) {
        this.plugNo = plugNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(String estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBespeakDealBefore() {
        return bespeakDealBefore;
    }

    public void setBespeakDealBefore(String bespeakDealBefore) {
        this.bespeakDealBefore = bespeakDealBefore;
    }

    public String getBespeakDealCharge() {
        return bespeakDealCharge;
    }

    public void setBespeakDealCharge(String bespeakDealCharge) {
        this.bespeakDealCharge = bespeakDealCharge;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Charger getChargerDetail() {
        return chargerDetail;
    }

    public void setChargerDetail(Charger chargerDetail) {
        this.chargerDetail = chargerDetail;
    }

    public ChargerStationDetail getStationDetail() {
        return stationDetail;
    }

    public void setStationDetail(ChargerStationDetail stationDetail) {
        this.stationDetail = stationDetail;
    }
}
