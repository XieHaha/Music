package com.zyc.doctor.ui.dialog.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * dialog构造器基类
 */
public class BaseBuilder
{
    protected AlertDialog.Builder mBuilder;

    public BaseBuilder(Activity activity)
    {
        mBuilder = new AlertDialog.Builder(activity);
    }

    public BaseBuilder setIcon(@DrawableRes int iconId)
    {
        mBuilder.setIcon(iconId);
        return this;
    }

    public BaseBuilder setIcon(Drawable icon)
    {
        mBuilder.setIcon(icon);
        return this;
    }

    public BaseBuilder setIconAttribute(int attrId)
    {
        mBuilder.setIconAttribute(attrId);
        return this;
    }

    public BaseBuilder setTitle(@StringRes int titleId)
    {
        mBuilder.setTitle(titleId);
        return this;
    }

    public BaseBuilder setTitle(CharSequence title)
    {
        mBuilder.setTitle(title);
        return this;
    }

    public BaseBuilder setTitle(View customTitle)
    {
        mBuilder.setCustomTitle(customTitle);
        return this;
    }

    public BaseBuilder cancelable(boolean cancelable)
    {
        mBuilder.setCancelable(cancelable);
        return this;
    }

    public BaseBuilder setInverseBackgroundForced(boolean useInverseBackground)
    {
        mBuilder.setInverseBackgroundForced(useInverseBackground);
        return this;
    }

    public BaseBuilder setPositiveButton(int textId, final DialogInterface.OnClickListener listener)
    {
        mBuilder.setPositiveButton(textId, listener);
        return this;
    }

    public BaseBuilder setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener)
    {
        mBuilder.setPositiveButton(text, listener);
        return this;
    }

    public BaseBuilder setNegativeButton(int textId, final DialogInterface.OnClickListener listener)
    {
        mBuilder.setNegativeButton(textId, listener);
        return this;
    }

    public BaseBuilder setNegativeButton(CharSequence text, final DialogInterface.OnClickListener listener)
    {
        mBuilder.setNegativeButton(text, listener);
        return this;
    }

    public BaseBuilder setNeutralButton(int textId, final DialogInterface.OnClickListener listener)
    {
        mBuilder.setNeutralButton(textId, listener);
        return this;
    }

    public BaseBuilder setNeutralButton(CharSequence text, final DialogInterface.OnClickListener listener)
    {
        mBuilder.setNeutralButton(text, listener);
        return this;
    }

    public BaseBuilder setOnKeyListener(final DialogInterface.OnKeyListener listener)
    {
        mBuilder.setOnKeyListener(listener);
        return this;
    }

    public BaseBuilder setOnCancelListener(final DialogInterface.OnCancelListener listener)
    {
        mBuilder.setOnCancelListener(listener);
        return this;
    }

    public BaseBuilder setOnDismissListener(final DialogInterface.OnDismissListener listener)
    {
        mBuilder.setOnDismissListener(listener);
        return this;
    }

    public AlertDialog buildAlertDialog()
    {
        return mBuilder.create();
    }

//    public AppCompatDialogFragment buildDialogFragment(final int style)
//    {
//        AppCompatDialogFragment dialogFragment = new AppCompatDialogFragment()
//        {
//            @Override
//            public Dialog onCreateDialog(Bundle savedInstanceState)
//            {
//                setStyle(style, 0);
//                return mBuilder.create();
//            }
//        };
//        return dialogFragment;
//    }
}
