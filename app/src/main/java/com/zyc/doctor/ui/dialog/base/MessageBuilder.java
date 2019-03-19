/*
 * 版权信息：嘉赛信息技术有限公司
 * Copyright (C) Justsy Information Technology Co., Ltd. All Rights Reserved
 *
 * FileName: .java
 * Description:
 *   <author> - <version> - <date> - <desc>
 *       jake - v1.1 - 2016.4.27 - 创建类
 *
 */

package com.zyc.doctor.ui.dialog.base;

import android.app.Activity;
import android.support.annotation.StringRes;

/**
 * 内容提示Dialog构造器
 */
public class MessageBuilder extends BaseBuilder
{
    public MessageBuilder(Activity activity)
    {
        super(activity);
    }

    public BaseBuilder setMessage(CharSequence message)
    {
        mBuilder.setMessage(message);
        return this;
    }

    public BaseBuilder setMessage(@StringRes int messageId)
    {
        mBuilder.setMessage(messageId);
        return this;
    }
}
