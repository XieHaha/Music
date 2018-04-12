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
     * 登录 注册
     */
    LOGIN_AND_REGISTER,
    /**
     * 上传文件
     */
    UPLOAD_FILE,
    /**
     * 上传基本信息
     */
    UPDATE_BASIC_INFO,
    /**
     * 上传职业信息
     */
    UPDATE_JOB_INFO,
    /**
     * 修改个人信息
     */
    UPDATE_USER_INFO,
    /**
     * 获取患者列表
     */
    GET_PATIENTS_LIST,
    /**
     * 获取合作医生列表
     */
    GET_COOPERATE_DOC_LIST,
    /**
     * 获取申请合作医生列表
     */
    GET_APPLY_COOPERATE_DOC_LIST,
    /**
     * 获取患者申请列表
     */
    GET_APPLY_PATIENT_LIST,
}

