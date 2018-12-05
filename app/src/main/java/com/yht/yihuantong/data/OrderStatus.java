package com.yht.yihuantong.data;

/**
 * Created by dundun on 18/8/14.
 */
public interface OrderStatus
{
    /**
     * 未预约
     */
    String STATUS_SUBSCRIBE_NONE = "0";
    /**
     * 已预约
     */
    String STATUS_SUBSCRIBE = "1";
    /**
     * 完成检查
     */
    String STATUS_COMPLETE = "2";
    /**
     * 已发送报告
     */
    String STATUS_SEND_REPORT = "3";
    /**
     * 已拒绝
     */
    String STATUS_REFUSE = "4";
}
