package com.zyc.doctor.api.notify;

/**
 * 注册状态枚举
 * @author dundun
 */
public class RegisterType
{
    private int type;
    /**
     * 注册
     */
    public static final RegisterType REGISTER = new RegisterType(1);
    /**
     * 取消注册
     */
    public static final RegisterType UNREGISTER = new RegisterType(0);

    private RegisterType(int type)
    {
        this.type = type;
    }
}

