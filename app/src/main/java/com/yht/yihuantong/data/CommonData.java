package com.yht.yihuantong.data;

public interface CommonData
{
    /**
     * 公用数据key
     */
    String KEY_PUBLIC = "key_public";
    /**
     * 用户登录账户
     */
    String KEY_USER_PHONE = "key_user_phone";
    /**
     * 新增病例(or查看病例详情)
     */
    String KEY_ADD_NEW_HEALTH = "key_add_new_health";
    /**
     * 登录成功返回数据
     */
    String KEY_LOGIN_SUCCESS_BEAN = "key_login_success_bean";
    /**
     * 用户id 不区分医生患者
     */
    String KEY_ID = "key_id";
    /**
     * doctorid
     */
    String KEY_DOCTOR_ID = "key_doctor_id";
    /**
     * patientId
     */
    String KEY_PATIENT_ID = "key_patient_id";
    /**
     * patient name
     */
    String KEY_PATIENT_NAME = "key_patient_name";
    /**
     * patient bean
     */
    String KEY_PATIENT_BEAN = "key_patient_bean";
    /**
     * doctor bean
     */
    String KEY_DOCTOR_BEAN = "key_doctor_bean";
    /**
     * patient case detail bean
     */
    String PATIENT_CASE_DETAIL_BEAN = "Patient_Case_Detail_Bean";
    /**
     * 聊天id
     */
    String KEY_CHAT_ID = "key_chat_id";
    /**
     * 聊天name
     */
    String KEY_CHAT_NAME = "key_chat_name";
    /**
     * 是否能对合作医生操作
     */
    String KEY_IS_DEAL_DOC = "key_is_deal_doc";
    /**
     * 是否禁止发起聊天
     */
    String KEY_IS_FORBID_CHAT = "key_is_forbid_chat";
    /**
     * 极光-合作医生申请码
     */
    int JIGUANG_CODE_COLLEBORATE_DOCTOR_REQUEST = 101;
    /**
     * 极光-合作医生添加成功
     */
    int JIGUANG_CODE_COLLEBORATE_ADD_SUCCESS = 102;
    /**
     * 医生认证成功
     */
    int JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS = 103;
    /**
     * 医生认证失败
     */
    int JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED = 104;
    /**
     * 添加患者成功
     */
    int JIGUANG_CODE_PATIENT_DP_ADD_SUCCESS = 105;
    /**
     * 添加医生成功(患者添加)
     */
    int JIGUANG_CODE_DOCTOR_DP_ADD_SUCCESS = 106;
    /**
     * 申请添加患者(医生申请)
     */
    int JIGUANG_CODE_PATIENT_DP_ADD_REQUEST = 107;
    /**
     * 申请添加医生(患者申请)
     */
    int JIGUANG_CODE_DOCTOR_DP_ADD_REQUEST = 108;
    /**
     * 新增处方
     */
    int JIGUANG_NEW_PRESCRIPTION = 109;
    /**
     * 转诊通知
     */
    int JIGUANG_TRANS_PATIENT= 110;
}
