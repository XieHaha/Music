package com.zyc.doctor.data;

/**
 * @author 顿顿
 * @date 19/4/11 16:45
 * @des 魔法值存储
 */
public interface BaseData {
    /**
     * 昵称的最大长度
     */
    int BASE_NICK_NAME_LENGTH = 20;
    /**
     * S手机号默认长度
     */
    int BASE_PHONE_DEFAULT_LENGTH = 11;
    /**
     * 验证码二次获取默认时间
     */
    int BASE_MAX_RESEND_TIME = 60;
    /**
     * 屏幕适配  默认宽度
     */
    int BASE_DEVICE_DEFAULT_WIDTH = 667;
    /**
     * requestCode
     */
    int BASE_PENDING_COUNT = 10000;
    /**
     * 消息最大显示数字
     */
    int BASE_MEAASGE_DISPLAY_NUM = 99;
    /**
     * 环信默认登录密码
     */
    String BASE_EASE_DEFAULT_PWD = "111111";
    /**
     * 适配华为  裁剪
     */
    String BASE_HONOR_NAME = "HONOR";
    /**
     * 适配华为  裁剪
     */
    String BASE_HUAWEI_NAME = "HUAWEI";
    /**
     * 图片类型
     */
    String BASE_IMAGE_TYPE = "image/bmp&&image/gif&&image/jpeg&&image/png";
}
