package com.yht.yihuantong;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class YiHTApplication extends Application
{
    private static YiHTApplication sApplication;
    private static Toast sToast;
    public static Handler sHandler;

    @Override
    public void onCreate()
    {
        super.onCreate();
        init();
    }

    private void init()
    {
        sApplication = this;
        sHandler = new Handler();
    }

    public static YiHTApplication getApp()
    {
        return sApplication;
    }

    public static void toast(Context context, int resId)
    {
        toast(context, context.getString(resId));
    }

    public static void toast(Context context, int resId, Object... formatArgs)
    {
        toast(context, context.getString(resId, formatArgs));
    }

    public static void toast(Context context, final String msg)
    {
        toast(context, msg, Gravity.BOTTOM);
    }

    public static void toast(final Context context, final String msg, final int gravity)
    {
        if (TextUtils.isEmpty(msg)) { return; }
        sHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (sToast == null)
                {
                    sToast = Toast.makeText(getApp().getApplicationContext(), msg,
                                            Toast.LENGTH_SHORT);
                }
                else
                {
                    sToast.setText(msg);
                }
                sToast.setDuration(Toast.LENGTH_SHORT);
                sToast.setGravity(gravity, 0, 100);
                sToast.show();
            }
        });
    }

    /**
     * app字体不随系统改变而改变
     *
     * @return
     */
    @Override
    public Resources getResources()
    {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        //        }
        return resources;
    }
}
