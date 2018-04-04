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
package com.yht.yihuantong.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.dialog.base.MessageBuilder;

/**
 * Created by yinhao on 16/4/28.
 */
public class DialogPermission extends MessageBuilder
{
    private Dialog dialog;

    public DialogPermission(final Activity activity, int resMsg)
    {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(resMsg);
        setPositiveButton(R.string.dialog_txt_confirm, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                activity.finish();
            }
        });
        setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                return true;
            }
        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public DialogPermission(final Activity activity, int resMsg, final boolean isNeedFinish)
    {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(resMsg);
        setPositiveButton(R.string.dialog_txt_confirm, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if (isNeedFinish)
                {
                    activity.finish();
                }
            }
        });
        setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                return true;
            }
        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public DialogPermission(final Activity activity, int resMsg, DialogInterface.OnClickListener onClickListener)
    {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(resMsg);
        setPositiveButton(R.string.dialog_txt_confirm, onClickListener);
        setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                return true;
            }
        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public DialogPermission(final Activity activity, String Msg)
    {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(Msg);
        setPositiveButton(R.string.dialog_txt_confirm, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                activity.finish();
            }
        });
        setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                return true;
            }
        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public DialogPermission(final Activity activity, String Msg, final boolean isNeedFinish)
    {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(Msg);
        setPositiveButton(R.string.dialog_txt_confirm, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                if (isNeedFinish)
                {
                    activity.finish();
                }
            }
        });
        setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                return true;
            }
        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public DialogPermission(final Activity activity, String msg, DialogInterface.OnClickListener onClickListener)
    {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(msg);
        setPositiveButton(R.string.dialog_txt_confirm, onClickListener);
        setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                return true;
            }
        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public Dialog show()
    {
        dialog.show();
        return dialog;
    }
}
