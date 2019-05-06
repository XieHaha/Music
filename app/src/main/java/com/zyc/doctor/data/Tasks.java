package com.zyc.doctor.data;

/**
 * 此类为任务队列编号，根据任务队列编号确定是否取消的任务
 *
 * @author dundun
 */
public enum Tasks {
    /**
     * 获取环信appkey
     */
    GET_EASE_APPKEY,
    /**
     * 获取广告业
     */
    GET_SPLASH,
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
     * 获取转诊患者申请列表
     */
    GET_PATIENTS_TO_LIST,
    /**
     * 获取收到转诊患者列表
     */
    GET_PATIENTS_FROM_LIST,
    /**
     * 转诊记录（转入转出）
     */
    GET_TRANSFER_LIST,
    /**
     * 开单记录
     */
    GET_ORDER_LIST,
    /**
     * 医生转诊患者
     */
    ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
    /**
     * 合作医生申请
     */
    APPLY_COOPERATE_DOC,
    /**
     * 合作医生备注设置
     */
    MODIFY_NICK_NAME,
    /**
     * 患者备注设置
     */
    MODIFY_NICK_NAME_BY_PATIENT,
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
    /**
     * 获取医生个人信息
     */
    GET_DOC_INFO,
    /**
     * 获取转诊详情
     */
    GET_TRANSFER_DETAIL_BY_ID,
    /**
     * 取消转诊
     */
    CANCEL_TRANSFER_PATIENT,
    /**
     * 拒绝转诊
     */
    REFUSE_TRANSFER_PATIENT,
    /**
     * 接受转诊
     */
    RECV_TRANSFER_PATIENT,
    /**
     * 医生转诊患者
     */
    ADD_TRANSFER_PATIENT,
    /**
     * 取消合作医生关系
     */
    CANCEL_COOPERATE_DOC,
    /**
     * 获取患者个人信息
     */
    GET_PATIENT_INFO,
    /**
     * 删除患者（取消关注）
     */
    DELETE_PATIENT,
    /**
     * 获取合作医院
     */
    GET_COOPERATE_HOSPITAL_LIST,
    /**
     * 获取病例详情
     */
    GET_CASE_DETAIL_BY_ID,
    /**
     * 拒绝患者申请
     */
    REFUSE_PATIENT_APPLY,
    /**
     * 同意患者申请
     */
    AGREE_PATIENT_APPLY,
    /**
     * 获取患者病例列表
     */
    GET_PATIENT_LIMIT_CASE_LIST,
    /**
     * 好友验证
     */
    FRIENDS_VERIFY,
    /**
     * 删除患者病例
     */
    DELETE_PATIENT_CASE,
    /**
     * 处理医生申请合作（proCode为字符1（表示同意）或字符3（表示拒绝））
     */
    DEAL_DOC_APPLY,
    /**
     * 版本更新
     */
    UPDATE_VERSION,
    /**
     * 文件下载
     */
    DOWNLOAD_FILE,
    /**
     * 医生资质认证
     */
    QUALIFIY_DOC,
    /**
     * 获取医院列表
     */
    GET_HOSPITAL_LIST,
    /**
     * 获取医院商品列表
     */
    GET_HOSPITAL_PRODUCT_LIST,
    /**
     * 根据医生id获取获取医院列表
     */
    GET_HOSPITAL_LIST_BY_DOCTORID,
    /**
     * 根据医生id获取获取合作医院
     */
    GET_COOPERATE_HOSPITAL_LIST_BY_DOCTORID,
    /**
     * 根据医生id获取获取合作医院下所有医生
     */
    GET_COOPERATE_HOSPITAL_DOCTOR_LIST,
    /**
     * 根据医院id获取商品类型和类型下的商品详情
     */
    GET_HOSPITAL_PRODUCT_LIST_BY_HOSPITALID,
    /**
     * 新增订单
     */
    ADD_PRODUCT_ORDER_NEW,
    /**
     * 获取所有商品
     */
    GET_ALL_PRODUCT,
    /**
     * 获取患者综合病史
     */
    GET_PATIENT_COMBINE,
    /**
     * 新增患者病例
     */
    ADD_PATIENT_CASE,
    /**
     * 新增患者病例
     */
    UPDATE_PATIENT_CASE,
    /**
     * 我的转诊记录
     */
    GET_TRANSFER_PATIENT_HISTORY_LIST,
    /**
     * 患者转诊记录
     */
    GET_TRANSFER_BY_PATIENT,
    /**
     * 患者订单记录
     */
    GET_PATIENT_ALL_ORDER_LIST,
    /**
     * 当前医生给患者开的订单记录
     */
    GET_PATIENT_ORDER_LIST,
    /**
     * 医生科室类别
     */
    GET_DEPARTMENT_TYPE,
    /**
     * 远程会诊登录
     */
    REMOTE_CONSULTATION_LOGIN,
    /**
     * 远程会诊 参数校验
     */
    REMOTE_CONSULTATION_VERIFY,
}

