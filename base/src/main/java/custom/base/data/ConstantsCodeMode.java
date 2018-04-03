package custom.base.data;

/**
 * Created by thl on 2016-07-01.
 */
public interface ConstantsCodeMode {

    /**
     * token校验失败
     */
    int REQUEST_CODE_TOKEN_ERROR = 1;


    /**
     * 预约充电失败 （有未支付订单）
     */
    int REQUEST_CODE_ORDER_ERROR = REQUEST_CODE_TOKEN_ERROR << 1;
}
