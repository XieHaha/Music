package custom.frame.http;

/**
 * 此类为任务队列编号，根据任务队列编号确定是否取消的任务
 */
public enum Tasks
{
    /**
     * 获取验证码
     */
    GET_VERIFY_CODE,
    /**
     * 上传文件
     */
    UPLOAD_FILE,
}

