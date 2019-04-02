package com.zyc.doctor.data;

/**
 * @author dundun
 * @date 19/2/19
 * 转诊状态
 */
public interface TransferStatu {
    /**
     * 转诊待接受
     */
    int TRANSFER_NONE = 0;
    /**
     * 已接受转诊
     */
    int TRANSFER_RECV = 1;
    /**
     * 已确诊
     */
    int TRANSFER_VISIT = 2;
    /**
     * 已取消
     */
    int TRANSFER_CANCEL = 40;
    /**
     * 已拒绝
     */
    int TRANSFER_REFUSE = 41;
    /**
     * 医院后台 取消
     */
    int TRANSFER_HOSPITAL_CANCEL = 42;
}
