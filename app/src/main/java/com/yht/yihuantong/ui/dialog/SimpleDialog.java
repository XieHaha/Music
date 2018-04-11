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
public class SimpleDialog extends MessageBuilder
{
    private Dialog dialog;

    public SimpleDialog(final Activity activity, int resMsg)
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

    public SimpleDialog(final Activity activity, int resMsg, final boolean isNeedFinish)
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

    public SimpleDialog(final Activity activity, int resMsg, DialogInterface.OnClickListener onClickListener)
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

    public SimpleDialog(final Activity activity, String Msg)
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

    public SimpleDialog(final Activity activity, String Msg, final boolean isNeedFinish)
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

    public SimpleDialog(final Activity activity, String msg, DialogInterface.OnClickListener onClickListener)
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

    public SimpleDialog(final Activity activity, String Msg, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onCancelListener)
    {
        super(activity);
        setMessage(Msg);
        setPositiveButton(R.string.dialog_txt_confirm, onClickListener);
        setNegativeButton(R.string.txt_cancel,onCancelListener);
//        setOnKeyListener(new DialogInterface.OnKeyListener()
//        {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
//            {
//                return true;
//            }
//        });
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public Dialog show()
    {
        dialog.show();
        return dialog;
    }
}
