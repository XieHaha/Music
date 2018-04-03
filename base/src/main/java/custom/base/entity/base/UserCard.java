package custom.base.entity.base;

import java.io.Serializable;

/**
 * Created by dundun on 16/4/20.
 */
public class UserCard implements Serializable {
    private static final long serialVersionUID = -4211885579465126548L;

    private String cardNo;
    private String cardType;//08线上卡，05 线下卡（身份认证卡）
    private String state;//0未激活，1激活，2冻结，3挂失，4  注销

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "[userCard]:  cardNo:" + cardNo + ",cardType:" + cardType + ",state:" + state;
    }
}
