package custom.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.List;

import custom.base.data.ConstantsBroadcast;

public class BroadcastUtil implements ConstantsBroadcast {
    private static BroadcastUtil broadcastUtil = null;

    public BroadcastUtil() {

    }

    public static BroadcastUtil getInstance() {
        if (broadcastUtil == null) {
            broadcastUtil = new BroadcastUtil();
        }
        return broadcastUtil;
    }

    /**
     * 发送广播，发送的广播为空广播
     *
     * @param context     context
     * @param ACTION_NAME action name
     * @author cubebox
     */
    public void sendEmptyBoradcast(Context context, String ACTION_NAME) {

        Intent intent = new Intent(ACTION_NAME);
        // 发送广播
        context.sendBroadcast(intent);
    }

    /**
     * 发送广播，发送带intent的广播
     *
     * @param context
     * @param ACTION_NAME
     * @param intent
     * @author cubebox
     */
    public void sendBoradcast(Context context, Intent intent, String ACTION_NAME) {

        intent.setAction(ACTION_NAME);
        intent.setPackage(context.getPackageName());
        // 发送广播
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播
     *
     * @param context
     * @param ACTION_NAME
     * @author cubebox
     */
    public boolean registerReceiver(Context context, String ACTION_NAME,
                                    BroadcastReceiver receiver) {
        try {
            IntentFilter myIntentFilter = new IntentFilter();
            myIntentFilter.addAction(ACTION_NAME);
            // 注册广播
            context.registerReceiver(receiver, myIntentFilter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 注册多事件广播
     *
     * @param context
     * @param ACTION_NAME_LIST
     * @param receiver
     * @return
     * @author cubebox
     */
    public boolean registerReceiverList(Context context, List<String> ACTION_NAME_LIST,
                                        BroadcastReceiver receiver) {
        try {
            IntentFilter myIntentFilter = new IntentFilter();
            for (int i = 0; i < ACTION_NAME_LIST.size(); i++) {
                myIntentFilter.addAction(ACTION_NAME_LIST.get(i));
            }
            myIntentFilter.setPriority(1000);
            // 注册广播
            context.registerReceiver(receiver, myIntentFilter);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
