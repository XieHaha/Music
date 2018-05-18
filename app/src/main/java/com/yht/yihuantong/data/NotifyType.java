package com.yht.yihuantong.data;

/**
 * Created by dundun on 18/5/18.
 */
public enum NotifyType
{
    /**
     * 申请添加医生
     */
    APPLY_ADD_DOCTOR("申请添加医生",1),
    /**
     * 添加医生成功
     */
    APPLY_ADD_DOCTOR_SUCCESS("添加医生成功",2),

    /**
     * 申请添加患者
     */
    APPLY_ADD_PATIENT("申请添加患者",3),
    /**
     * 添加患者成功
     */
    APPLY_ADD_PATIENT_SUCCESS("添加患者成功",4),
    /**
     * 医生认证成功
     */
    APPLY_AUTH_SUCCESS("医生认证成功",100),
    /**
     * 医生认证失败
     */
    APPLY_AUTH_FAILD("医生认证失败",101);

    private int value;

   private NotifyType(String s,int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
