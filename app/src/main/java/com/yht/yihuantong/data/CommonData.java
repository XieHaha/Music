package com.yht.yihuantong.data;

public interface CommonData
{
    /**
     * 图片选择  单选  多选
     */
    String KEY_SINGLE_CHECK = "key_single_check";
    /**
     * 选中图片后, 回传图片路径key
     */
    String KEY_CHECKED_PATH = "key_checked_path";
    /**
     * 新增病例(or查看病例详情)
     */
    String KEY_ADD_NEW_HEALTH = "key_add_new_health";
    /**
     * 登录成功返回数据
     */
    String KEY_LOGIN_SUCCESS_BEAN = "key_login_success_bean";
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
     * 申请添加医生
     */
    int APPLY_ADD_DOCTOR = 1;
    /**
     * 添加医生成功
     */
    int APPLY_ADD_DOCTOR_SUCCESS = 2;
    /**
     * 申请添加患者
     */
    int APPLY_ADD_PATIENT = 3;
    /**
     * 添加患者成功
     */
    int APPLY_ADD_PATIENT_SUCCESS = 4;
    /**
     * 医生认证成功
     */
    int APPLY_AUTH_SUCCESS = 100;
    /**
     * 医生认证失败
     */
    int APPLY_AUTH_FAILD = 101;
}
