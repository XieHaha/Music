package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/4/20.
 */
public class FreeChargerNum implements Serializable {

    private static final long serialVersionUID = -7258382456960313396L;
    private String freeDirectNum;
    private String freeAlternateNum;

    public String getFreeDirectNum() {
        return freeDirectNum;
    }

    public void setFreeDirectNum(String freeDirectNum) {
        this.freeDirectNum = freeDirectNum;
    }

    public String getFreeAlternateNum() {
        return freeAlternateNum;
    }

    public void setFreeAlternateNum(String freeAlternateNum) {
        this.freeAlternateNum = freeAlternateNum;
    }


    @Override
    public String toString() {
        return "FreeChargerNum{" +
                "freeDirectNum='" + freeDirectNum + '\'' +
                ", freeAlternateNum='" + freeAlternateNum + '\'' +
                '}';
    }
}
