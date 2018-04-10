package custom.frame.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil
{
    private static Toast toast = null;

    private ToastUtil()
    {
    }

    /**
     * @param context
     * @param msg
     */
    public static void toast(Context context, String msg)
    {
        if (msg == null || msg.isEmpty()) { return; }
        if (toast == null)
        {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setText(msg);
        }
//        toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        toast.show();
    }

    /**
     * @param context
     * @param resId
     */
    public static void toast(Context context, int resId)
    {
        if (toast == null)
        {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        }
        else
        {
            toast.setText(resId);
        }
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
