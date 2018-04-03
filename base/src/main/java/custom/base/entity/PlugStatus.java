package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 16/4/28.
 */
public class PlugStatus implements Serializable {
    private static final long serialVersionUID = -8704868168654904454L;
    private String portNo;//  端口序号
    private String chargeStatus;// 插头状态 0: 空闲;1: 预约;2 充电; 3 故障;4:占用中（针对双枪单充等情况）;5 刷卡中  8:离线
    private String userCard;//  当前占用的用户卡号
    private String onlineStatus;// 在线状态 1代表在线  0代表离线
    private String cableStatus;// 充电插头连接状态 0未连接 1 已连接
    private String chargerV;//实时电压
    private String chargerI;// 实时电流
    private String chargerP;//  实时功率
    private String chargerKwh;// 充电电量
    private String chargerFee;//  充电金额
    private String reservationSn;//预约流水号
    private String chargeSn;//充电流水号
    private String soc;//已充电量百分比

    public String getChargeSn() {
        return chargeSn;
    }

    public void setChargeSn(String chargeSn) {
        this.chargeSn = chargeSn;
    }

    public String getReservationSn() {
        return reservationSn;
    }

    public void setReservationSn(String reservationSn) {
        this.reservationSn = reservationSn;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getCableStatus() {
        return cableStatus;
    }

    public void setCableStatus(String cableStatus) {
        this.cableStatus = cableStatus;
    }

    public String getChargerV() {
        return chargerV;
    }

    public void setChargerV(String chargerV) {
        this.chargerV = chargerV;
    }

    public String getChargerI() {
        return chargerI;
    }

    public void setChargerI(String chargerI) {
        this.chargerI = chargerI;
    }

    public String getChargerP() {
        return chargerP;
    }

    public void setChargerP(String chargerP) {
        this.chargerP = chargerP;
    }

    public String getChargerKwh() {
        return chargerKwh;
    }

    public void setChargerKwh(String chargerKwh) {
        this.chargerKwh = chargerKwh;
    }

    public String getChargerFee() {
        return chargerFee;
    }

    public void setChargerFee(String chargerFee) {
        this.chargerFee = chargerFee;
    }

    public String getSoc() {
        return soc;
    }

    public void setSoc(String soc) {
        this.soc = soc;
    }

    @Override
    public String toString() {
        return "[PlugStatus:] portNo:" + portNo +
                ",chargeStatus:" + chargeStatus +
                ",userCard:" + userCard +
                ",onlineStatus:" + onlineStatus +
                ",cableStatus:" + cableStatus +
                ",chargerV:" + chargerV +
                ",chargerI:" + chargerI +
                ",chargerP:" + chargerP +
                ",chargerKwh:" + chargerKwh +
                ",reservationSn:" + reservationSn +
                ",chargerFee:" + chargerFee +
                ",soc:" + soc +
                "]";
    }
}
