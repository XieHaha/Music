package custom.base.data;

/**
 * Created by luozi on 2016/4/13.
 */
public interface ConstantsBroadcast {

    String base = "com.dz.tt.";
    String BRO_BOOK_SUCCESS = "com.dz.tt.booksuccess";

    String EXIT_USER = base + "exit_user";

    /**
     * 预约推送回调广播
     */
    String BOOK_CALLBACK = base + "book_callback";
    /**
     * 充电推送回调广播
     */
    String CHARGE_CALLBACK = base + "charge_callback";
    /**
     * 控制错误推送回调广播
     */
    String ERROR_CALLBACK = base + "error_callback";
    /**
     * 订单推送回调广播
     */
    String ORDER_CALLBACK = base + "order_callback";

    /**
     * 官方消息广播
     */
    String OFFICE_MSG = base + "office_msg";

    /**
     * 显示web对话框广播
     */
    String HINT_WEB = base + "hint_web";
}
