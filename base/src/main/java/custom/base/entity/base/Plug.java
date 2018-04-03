package custom.base.entity.base;

import java.io.Serializable;

/**
 * Created by luozi on 2016/4/21.
 */
public class Plug implements Serializable {
    private static final long serialVersionUID = 1833825452303610817L;
    String chargeStatus;//充电插头状态  0: 空闲;1: 预约;2 充电; 3 故障;4:占用中（针对双枪单充等情况）;5 刷卡中  8:离线
    String portNo;//插头号

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    @Override
    public String toString() {
        return "Plug{" +
                "chargeStatus='" + chargeStatus + '\'' +
                ", portNo='" + portNo + '\'' +
                '}';
    }
}
