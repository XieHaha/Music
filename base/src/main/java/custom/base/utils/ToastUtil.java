package custom.base.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import custom.base.data.Global;
import custom.base.data.Mode;

public class ToastUtil {
    private static Toast toast = null;

    private ToastUtil() {
    }

    /**
     * 测试输出，在开发模式下不会输出结果
     *
     * @param context
     * @param msg
     */
    public static void debugShow(Context context, String msg) {
        String tmp = "DEBUG->" + msg;
        if (Global.getInstance().launchMode == Mode.Launch.RELEASE) return;
        if (toast == null) {
            toast = Toast.makeText(context, tmp, Toast.LENGTH_SHORT);
        } else {
            toast.setText(tmp);
        }
        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 0, 0);
        toast.show();
    }

    /**
     * 正常输出
     *
     * @param context
     * @param msg
     */
    public static void releaseShow(Context context, String msg) {
        if (msg == null || msg.isEmpty()) return;
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 正常输出
     *
     * @param context
     * @param resId
     */
    public static void releaseShow(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 正常输出
     *
     * @param context
     * @param msg
     * @param time
     */
    public static void releaseShow(Context context, String msg, int time) {
        if (msg == null || msg.isEmpty()) return;
        if (toast == null) {
            toast = Toast.makeText(context, msg, time);
        } else {
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
