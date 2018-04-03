package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 16/4/29.
 */
public class UserBalance implements Serializable {
    private static final long serialVersionUID = -7133483681340501203L;

    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserBalance:{ balance:" + balance + "}";
    }
}
