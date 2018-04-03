package custom.base.entity;

import java.io.Serializable;

/**
 * Created by dundun on 16/4/29.
 *
 * 费用详细数组  包括 服务费、预约费、充电费
 *
 */
public class OrderDetails implements Serializable {
    private static final long serialVersionUID = -1734104627911019839L;

    private String orderNo;//订单号
    private String realTotalPrice;//实际总价
    private String serviceProvider;//提供服务的运营商编号
    private String totalPrice;//原始总价
    private String serviceNo;//服务编号
    private String unitPrice;//原始单价
    private String realUnitPrice;//实际单价

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getRealUnitPrice() {
        return realUnitPrice;
    }

    public void setRealUnitPrice(String realUnitPrice) {
        this.realUnitPrice = realUnitPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRealTotalPrice() {
        return realTotalPrice;
    }

    public void setRealTotalPrice(String realTotalPrice) {
        this.realTotalPrice = realTotalPrice;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderNo='" + orderNo + '\'' +
                ", realTotalPrice='" + realTotalPrice + '\'' +
                ", serviceProvider='" + serviceProvider + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", serviceNo='" + serviceNo + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", realUnitPrice='" + realUnitPrice + '\'' +
                '}';
    }

}
