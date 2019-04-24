package com.zyc.doctor.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.zyc.doctor.R;
import com.zyc.doctor.ui.dialog.base.MessageBuilder;

/**
 * @author yinhao
 * @date 16/4/28
 */
public class SimpleDialog extends MessageBuilder {
    private Dialog dialog;

    public SimpleDialog(final Activity activity, int resMsg) {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(resMsg);
        setPositiveButton(R.string.dialog_txt_confirm, (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        });
        setOnKeyListener((dialog, keyCode, event) -> true);
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public SimpleDialog(final Activity activity, int resMsg, final boolean isNeedFinish) {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(resMsg);
        setPositiveButton(R.string.dialog_txt_confirm, (dialog, which) -> {
            dialog.dismiss();
            if (isNeedFinish) {
                activity.finish();
            }
        });
        setOnKeyListener((dialog, keyCode, event) -> true);
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public SimpleDialog(final Activity activity, int resMsg, DialogInterface.OnClickListener onClickListener) {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(resMsg);
        setPositiveButton(R.string.dialog_txt_confirm, onClickListener);
        setOnKeyListener((dialog, keyCode, event) -> true);
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public SimpleDialog(final Activity activity, String msg) {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(msg);
        setPositiveButton(R.string.dialog_txt_confirm, (dialog, which) -> {
            dialog.dismiss();
            activity.finish();
        });
        setOnKeyListener((dialog, keyCode, event) -> true);
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public SimpleDialog(final Activity activity, String msg, final boolean isNeedFinish) {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(msg);
        setPositiveButton(R.string.dialog_txt_confirm, (dialog, which) -> {
            dialog.dismiss();
            if (isNeedFinish) {
                activity.finish();
            }
        });
        setOnKeyListener((dialog, keyCode, event) -> true);
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public SimpleDialog(final Activity activity, String msg, DialogInterface.OnClickListener onClickListener) {
        super(activity);
        setTitle(R.string.dialog_public_title);
        setMessage(msg);
        setPositiveButton(R.string.dialog_txt_confirm, onClickListener);
        setOnKeyListener((dialog, keyCode, event) -> true);
        dialog = buildAlertDialog();
        dialog.setCanceledOnTouchOutside(false);
    }

    public SimpleDialog(final Activity activity, String msg, DialogInterface.OnClickListener onClickListener,
            DialogInterface.OnClickListener onCancelListener) {
        super(activity);
        setMessage(msg);
        setPositiveButton(R.string.dialog_txt_confirm, onClickListener);
        setNegativeButton(R.string.txt_cancel, onCancelListener);
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

    public Dialog show() {
        dialog.show();
        return dialog;
    }
}
