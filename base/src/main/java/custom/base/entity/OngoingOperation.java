package custom.base.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by thl on 2016-05-01.
 */
public class OngoingOperation implements Serializable {

    private static final long serialVersionUID = -7387709407830623104L;

    //正在进行的充电
    private List<ChargeDetail> chargeList;
    //预约
    private List<ReservationDetail> reservationList;

    public List<ChargeDetail> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<ChargeDetail> chargeList) {
        this.chargeList = chargeList;
    }

    public List<ReservationDetail> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<ReservationDetail> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public String toString() {
        return "OngoingOperation {" +
                "chargeList='" + chargeList + '\'' +
                ", reservationList='" + reservationList + '\'' +
                '}';
    }
}
