package custom.frame.http;

/**
 * Created by luozi on 2015/12/30.
 * 此类为任务队列编号，根据任务队列编号确定是否取消的任务
 */
public enum Tasks {
    /**
     * 测试
     */
    TEST,
    /**
     * TOKEN校验
     */
    CHECK_OUT_TOKEN,
    /**
     * 手机号验证
     */
    CHECK_PHONE_EXISTS,
    /**
     * 获取验证码
     */
    GET_VERIFY_CODE,
    /**
     * 验证验证码
     */
    CHECK_VERIFY_CODE,
    /**
     * 登录
     */
    USER_LOGIN,
    /**
     * 第三方登录
     */
    FOREIGN_LOGIN,
    /**
     * 未登录绑定
     */
    NOT_LOGIN_BIND_FOREIGN,
    /**
     * 登录后绑定
     */
    LOGIN_BIND_FOREIGN,
    /**
     * 手机号快捷登录
     */
    QUICK_LOGIN,
    /**
     * 后台登录
     */
    BACKROUND_LOGIN,
    /**
     * 注册
     */
    REGISTER_USER,
    /**
     * 找回密码
     */
    RESET_PASSWORD,
    /**
     * 修改密码
     */
    UPDATE_PASSWORD,
    /**
     * 编辑个人资料
     */
    UPDATE_USER_INFO,
    /**
     * 推送列表
     */
    ZHUANGYOU_LIST,
    /**
     * 下载文件
     */
    DOWNLOAD_FILE,
    /**
     * 下载文件
     */
    UPLOAD_FILE,
    /**
     * 获取热题标签列表
     */
    HOT_LABEL_LIST,
    /**
     * 获取资讯标签列表
     */
    NEWS_LABEL_LIST,
    /**
     * 获取服务banner地址
     */
    SERVICE_BANNER_URL,
    /**
     * 获取桩友圈数据
     */
    CHARGER_FRIENDS_CIRCLE_LIST,
    /**
     * 获取标签分类后桩友圈数据
     */
    HOT_TOPIC_GROUP_LIST,
    /**
     * 获取资讯数据
     */
    CHARGER_NEWS_LIST,
    /**
     * 获取资讯分类后数据
     */
    CHARGER_NEWS_GROUP_LIST,
    /**
     * 桩友圈点赞
     */
    CHARGER_FRIENDS_CIRCLE_LIKE,
    /**
     * 单条桩友动态
     */
    SINGLE_DYNAMIC_INFO,
    /**
     * 删除单条桩友动态
     */
    DELETE_DYNAMIC,
    /**
     * 删除评论
     */
    DELETE_COMMENT,
    /**
     * 单条动态详情
     */
    SINGLE_DYNAMIC_DETAIL,
    /**
     * 删除单条动态
     */
    DELETE_SINGLE_DYNAMIC,
    /**
     * 桩友圈评论
     */
    CHARGER_FRIENDS_CIRCLE_COMMENT,
    /**
     * 发布动态
     */
    PUBLISH_DYNAMIC,
    /**
     * 电桩列表
     */
    CHARGER_STATION_LIST,
    /**
     * 电桩详情
     */
    CHARGER_STATION_DETAIL,
    /**
     * 用户卡号列表
     */
    GET_USER_CARD,
    /**
     * 用户余额
     */
    GET_USER_BALANCE,
    /**
     * 第三方绑定列表
     */
    FOREIGN_BIND_LIST,
    /**
     * 开启预约
     */
    DO_RESERVATION,
    /**
     * 取消预约
     */
    DO_CANCEL_RESERVATION,
    /**
     * 开启充电
     */
    OPEN_CHARGE,
    /**
     * 扫二维码码开启充电
     */
    OPEN_CHARGE_BY_QRCODE,
    /**
     * 关闭充电
     */
    CLOSE_CHARGING,
    /**
     * 获取枪头状态
     */
    GET_PLUG_STATUS,
    /**
     * 通过二维码获取桩详情
     */
    GET_CHARGER_DETAIL_BY_QR_CODE,
    /**
     * 站点评论列表
     */
    STATION_COMMENT_LIST,
    /**
     * 站点评论点赞
     */
    CHAGER_COMMENT_PRAISE,
    /**
     * 站点评论
     */
    STATION_COMMENT,
    /**
     * 站点评论回复
     */
    STATION_REPLY_COMMENT,
    /**
     * 账单列表
     */
    FUND_LIST,
    /**
     * 账单详情
     */
    FUND_DETAIL,
    /**
     * 商户列表
     */
    STORE_LIST,
    /**
     * 查询站下空闲桩的数量
     */
    FREE_CHARGER_NUM,
    /**
     * 查询桩位列表
     */
    CHARGER_LIST,
    /**
     * 获取正在进行的业务操作（预约，充电）
     */
    ONGOING_OPERATION,
    /**
     * 订单支付接口
     */
    PAY_ORDER,

    /**
     * 根据预约流水号获取预约详细信息
     */
    GET_RESERVATION_DETAIL_BY_RESERVATIONSN,
    /**
     * 根据充电流水号获取充电详细信息
     */
    GET_CHARGE_DETAIL_BY_CHARGESN,
    /**
     * 查询未支付订单
     */
    GET_NON_PAYMENT_ORDER,
//----------------车辆控制相关----------------
    /**
     * 车位置获取
     */
    CAR_LOCATION,
    /**
     * 用户车列表
     */
    CAR_LIST,
    /**
     * 寻车
     */
    FIND_CAR,
    /**
     * 车锁控制
     */
    LOCK_CAR,
    /**
     * 七牛云
     */
    QINIU_KEY,
    /**
     * 版本更新
     */
    VERSION,
    /**
     * 单桩的费用信息
     */
    CHARGER_RATE;

}

