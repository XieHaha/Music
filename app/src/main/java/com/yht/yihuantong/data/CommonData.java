package com.yht.yihuantong.data;

public interface CommonData
{
    /**
     * 公用数据key
     */
    String KEY_PUBLIC = "key_public";
    /**
     * 公用数据key
     */
    String KEY_PUBLIC_STRING = "key_public_string";
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
     * transfer bean
     */
    String KEY_TRANSFER_BEAN = "key_transfer_bean";
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
     * 挂号类型
     */
    String KEY_REGISTRATION_TYPE = "key_registration_type";
    /**
     * 我的患者数量
     */
    String KEY_PATIENT_NUM = "key_patient_num";
    /**
     * 我的合作医生数量
     */
    String KEY_DOCTOR_NUM = "key_doctor_num";
    /**
     * 我的患者申请数量
     */
    String KEY_PATIENT_APPLY_NUM = "key_patient_apply_num";
    /**
     * 我的转诊申请数量
     */
    String KEY_CHANGE_PATIENT_NUM = "key_change_patient_num";
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
     * 收到转诊通知 合作医生接受了我的转诊
     */
    int JIGUANG_CODE_TRANS_PATIENT_SUCCESS = 113;
    /**
     * 收到转诊通知 合作医生接受我转诊的患者已经就诊
     */
    int JIGUANG_CODE_TRANS_PATIENT_VISIT_SUCCESS = 123;
    /**
     * 收到转诊通知 合作医生转给我的
     */
    int JIGUANG_CODE_TRANS_PATIENT_APPLY = 133;
    /**
     * 患者病例 诊断
     */
    int CODE_CASE_DIA = 200;
    /**
     * 患者病例 医院
     */
    int CODE_CASE_HOSPITAL = 201;
    /**
     * 患者病例 科室
     */
    int CODE_CASE_TYPE = 202;
    /**
     * 患者病例 主诉
     */
    int CODE_CASE_INFO = 203;
    /**
     * 患者病例 病史
     */
    int CODE_CASE_NOW = 204;
    /**
     * 患者病例 体格检查
     */
    int CODE_CASE_CHECK = 205;
    /**
     * 患者病例 治疗
     */
    int CODE_CASE_DEAL_WAY = 206;
}
