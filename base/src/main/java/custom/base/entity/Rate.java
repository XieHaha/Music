package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 16/7/1.
 */
public class Rate implements Serializable {
    private static final long serialVersionUID = -4719560988291354269L;

    private String serialNo;
    private String rateName;//费率名称
    private String chargeMode;//收费模式： 0、按时间收费，1、按次数收费
    private String feeType; //费率类型： 1、预约费用，2、充电费用，3、充电服务费
    private String startTime;
    private String endTime;
    private String rate;


    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getRateName() {
        return rateName;
    }

    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "[Rate:] serialNo:" + serialNo +
                ",rateName:" + rateName +
                ",chargeMode:" + chargeMode +
                ",feeType:" + feeType +
                ",startTime:" + startTime +
                ",endTime:" + endTime +
                ",rate:" + rate +
                "]";
    }
}
