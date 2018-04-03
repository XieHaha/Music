package custom.base.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dundun on 16/4/29.
 */
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 7225673316843214520L;

    private String caculatedTotalPrice;//应付总价
    private String payType;//支付类型  1 第三实时支付 2 统付 3 企业支付 4 电子钱包支付 5 代付 6 线下卡
    private String thdPayChannel;//支付渠道  0 有贝先付 1 支付宝 2 微信支付
    private String status;//状态
    private String createTime;//创建时间

    private ArrayList<OrderDetails> orderDetails;
    private ChargerStationDetail stationDetail;
    private Charger chargerDetail;
    private ReservationDetail reservationDetail;
    private ChargeDetail chargeDetail;

    public String getCaculatedTotalPrice() {
        return caculatedTotalPrice;
    }

    public void setCaculatedTotalPrice(String caculatedTotalPrice) {
        this.caculatedTotalPrice = caculatedTotalPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getThdPayChannel() {
        return thdPayChannel;
    }

    public void setThdPayChannel(String thdPayChannel) {
        this.thdPayChannel = thdPayChannel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public ArrayList<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public ChargerStationDetail getStationDetail() {
        return stationDetail;
    }

    public void setStationDetail(ChargerStationDetail stationDetail) {
        this.stationDetail = stationDetail;
    }

    public Charger getChargerDetail() {
        return chargerDetail;
    }

    public void setChargerDetail(Charger chargerDetail) {
        this.chargerDetail = chargerDetail;
    }

    public ReservationDetail getReservationDetail() {
        return reservationDetail;
    }

    public void setReservationDetail(ReservationDetail reservationDetail) {
        this.reservationDetail = reservationDetail;
    }

    public ChargeDetail getChargeDetail() {
        return chargeDetail;
    }

    public void setChargeDetail(ChargeDetail chargeDetail) {
        this.chargeDetail = chargeDetail;
    }
}
