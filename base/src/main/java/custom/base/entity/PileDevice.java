package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 16/5/1.
 */
public class PileDevice implements Serializable {
    private static final long serialVersionUID = -8867839782011720567L;

    private String chargeP;//功率
    private String chargePortType;//充电接口类型：1.国标；2特斯拉；3.ABB，4其他
    private String connectType;//链接类型  0-在线桩（GPRS桩）,1-离线桩（蓝牙桩）
    private String chargerType;// 电桩类型 :1--直流  2--交流  3--混合


    public String getChargeP() {
        return chargeP;
    }

    public void setChargeP(String chargeP) {
        this.chargeP = chargeP;
    }

    public String getChargePortType() {
        return chargePortType;
    }

    public void setChargePortType(String chargePortType) {
        this.chargePortType = chargePortType;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getChargerType() {
        return chargerType;
    }

    public void setChargerType(String chargerType) {
        this.chargerType = chargerType;
    }

    @Override
    public String toString() {
        return "PileDevice [ chargeP : " + chargeP +
                ",chargePortType : " + chargePortType +
                ",connectType : " + connectType +
                ",chargerType : " + chargerType +
                "]";
    }
}
