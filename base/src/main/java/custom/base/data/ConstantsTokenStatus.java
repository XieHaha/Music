package custom.base.data;

/**
 * Created by dundun on 2016/5/3.
 */
public interface ConstantsTokenStatus {
    String TOKEN_STATUS = "dz.tt.token_status";//token验证

    /**
     * 错误码  key
     */
    String TOKEN_STATUS_CODE_KEY = "token_status_code_key";

    /**
     * 404
     */
    String REQUEST_TOKEN_NOT_FOUND = "10410001";
    /**
     * 调用主服务接口失败
     */
    String REQUEST_TOKEN_CONNECTION_MS_FAILED = "10401001";
    /**
     * token 校验失败码 (token 失敗)
     */
    String REQUEST_TOKEN_FAILD = "10401003";

    /**
     * token 校验失败码 (token 无效)
     */
    String REQUEST_TOKEN_INVALID = "10401004";

    /**
     * token 校验失败码 (token 过期)
     */
    String REQUEST_TOKEN_EXPIRED = "10401005";

    /**
     * 登录连续失败次数超规定次数，并且冷却时间还未到
     */
    String REQUEST_TOKEN_RETRY_LOGIN = "10401006";

    /**
     * 必须参数为空
     */
    String REQUEST_TOKEN_MISSING_PARAMETERS = "10402001";
}
