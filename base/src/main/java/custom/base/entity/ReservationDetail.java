package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-05-01.
 */
public class ReservationDetail implements Serializable {

    private static final long serialVersionUID = 4898879906064337896L;

    //2016年11月25日14:57:56 新增
    private Charger chargerDetail;//桩 详情

    private String userCard;//用户卡号
    private String reservationSn;//预约流水号
    private String plugNo;//插头编号（充电枪头）
    private String startTime;//开始时间
    private String estimateEndTime;//理论结束时间（超期时间）
    private String actualEndTime;//实际结束时间
    private String duration;//预约时长
    private String bespeakDealBefore;//预约结算前费用
    private String bespeakDealCharge;//最终预约结算费用
    private String stationId;//站点ID
    private String chargerId;//电桩地址
    private String orderNo;//订单号
    private String isDone;//是否完成
    /**
     * 1： 预约中    2： 已预约    3： 取消预约中    4 ：已取消预约    5： 充电
     */
    private String status;

    public Charger getChargerDetail() {
        return chargerDetail;
    }

    public void setChargerDetail(Charger chargerDetail) {
        this.chargerDetail = chargerDetail;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
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

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEstimateEndTime() {
        return estimateEndTime;
    }

    public void setEstimateEndTime(String estimateEndTime) {
        this.estimateEndTime = estimateEndTime;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlugNo() {
        return plugNo;
    }

    public void setPlugNo(String plugNo) {
        this.plugNo = plugNo;
    }

    public String getReservationSn() {
        return reservationSn;
    }

    public void setReservationSn(String reservationSn) {
        this.reservationSn = reservationSn;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    @Override
    public String toString() {
        return "ReservationDetail {" +
                "chargerDetail='" + chargerDetail + '\'' +
                "userCard='" + userCard + '\'' +
                ", reservationSn='" + reservationSn + '\'' +
                ", plugNo='" + plugNo + '\'' +
                ", startTime='" + startTime + '\'' +
                ", estimateEndTime='" + estimateEndTime + '\'' +
                ", actualEndTime='" + actualEndTime + '\'' +
                ", duration='" + duration + '\'' +
                ", bespeakDealBefore='" + bespeakDealBefore + '\'' +
                ", bespeakDealCharge='" + bespeakDealCharge + '\'' +
                ", stationId='" + stationId + '\'' +
                ", chargerId='" + chargerId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", isDone='" + isDone + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
