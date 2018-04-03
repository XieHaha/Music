package custom.frame.log;


import custom.base.data.Global;
import custom.base.data.Mode;

public final class MLog {
    private static final String tag = "dianzhuang";
    public static final String ErrorTag = "电桩错误";

    /**
     * 测试tag调试
     */
    public static void test(Object msg) {
        if (Global.getInstance().getLaunchMode() == Mode.Launch.DEBUG) {
            android.util.Log.d(tag, msg == null ? "msg为空" : msg.toString());
        }
    }

    /**
     * 测试tag调试
     */
    public static void testE(Object msg) {
        if (Global.getInstance().getLaunchMode() == Mode.Launch.DEBUG) {
            android.util.Log.e(tag, msg == null ? "msg为空" : msg.toString());
        }
    }

    /**
     * 传入tag  调试
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, Object msg) {
        if (Global.getInstance().getLaunchMode() == Mode.Launch.DEBUG) {
            android.util.Log.d(tag, msg == null ? "msg为空" : msg.toString());
        }
    }


    /**
     * 传入tag  错误日志输出
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, Object msg) {
        if (Global.getInstance().getLaunchMode() == Mode.Launch.DEBUG) {
            android.util.Log.e(tag, msg==null?"msg为空":msg.toString());
        }
    }

    /**
     * 传入tag  强调日志输出
     *
     * @param tag
     * @param msg
     */
    public static void stressD(String tag, Object msg) {
        if (Global.getInstance().getLaunchMode() == Mode.Launch.DEBUG) {
            android.util.Log.e(tag, msg==null?"msg为空":msg.toString());
        }
    }

}
