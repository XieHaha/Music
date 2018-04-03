package custom.base.entity.base;

import java.io.Serializable;
import java.util.List;

import custom.base.entity.Charger;
import custom.base.entity.OrderDetails;

/**
 * Created by thl on 2016-03-28.
 */
public class OrderBase implements Serializable {
    private static final long serialVersionUID = 7087718092370024505L;

    private String orderNo;//订单号
    private String caculatedTotalPrice;//实际应付的总价
    private String payType;//支付类型 1 第三实时支付 2 统付 3 企业支付 4 电子钱包支付 5 代付 6 线下卡
    private String thdPayChannel;//第三方支付渠道名称 0 有贝先付 1 支付宝 2 微信支付
    private String status;//订单状态(0, "已下单，未支付"),(1, "正在支付"),(2, "未审核"),(3, "正在审核"),(4, "已确认"),(5, "备货中"),(6, "已发货"),(7, "已支付，已完成"),(8, "已取消，已完成"),(9, "已超期，已完成")
    private String createTime;//创建时间

    //2016年11月25日14:57:56 新增
    private Charger charger;//桩 详情

    public Charger getCharger() {
        return charger;
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }

    private List<OrderDetails> orderDetails;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCaculatedTotalPrice() {
        return caculatedTotalPrice;
    }

    public void setCaculatedTotalPrice(String caculatedTotalPrice) {
        this.caculatedTotalPrice = caculatedTotalPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getThdPayChannel() {
        return thdPayChannel;
    }

    public void setThdPayChannel(String thdPayChannel) {
        this.thdPayChannel = thdPayChannel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderBase{" +
                "orderNo='" + orderNo + '\'' +
                ", caculatedTotalPrice='" + caculatedTotalPrice + '\'' +
                ", payType='" + payType + '\'' +
                ", thdPayChannel='" + thdPayChannel + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
