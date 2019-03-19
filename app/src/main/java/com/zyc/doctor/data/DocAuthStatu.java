package com.zyc.doctor.data;

/**
 * Created by dundun on 19/2/19.
 * 医生认证状态
 */
public interface DocAuthStatu
{
    /**
     * 医生认证
     * <0-未上传资料（可修改）
     */
    int NONE = 0;
    /**
     * 医生认证
     * ；1-已上传资料等待审核（可修改）
     */
    int VERIFYING = 1;
    /**
     * 医生认证
     * ；2-审核未通过（可修改）；
     */
    int VERIFY_FAILD = 2;
    /**
     * 医生认证
     * 6-审核已通过（可修改）>
     */
    int VERIFY_SUCCESS = 6;
}
