package custom.frame.bean;

import java.io.Serializable;

/**
 * 登录成功
 *
 * @author DUNDUN
 */
public class LoginSuccessBean implements Serializable {
    private static final long serialVersionUID = 1574016439324324068L;

    private String doctorId;
    private String phone;
    private int focusStatus;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }

    @Override
    public String toString() {
        return "[LoginSuccessBean]->doctorId:" + doctorId
                + ",phone:" + phone
                + ",focusStatus:" + focusStatus;
    }
}
