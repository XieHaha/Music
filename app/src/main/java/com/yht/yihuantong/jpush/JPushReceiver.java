package com.yht.yihuantong.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yht.yihuantong.api.notify.NotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver implements CommonData
{
    private static final String TAG = "JIGUANG";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " +
                       printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
            {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[JPushReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...
            }
            else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
            {
                Log.d(TAG, "[JPushReceiver] 接收到推送下来的自定义消息: " +
                           bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);
            }
            else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
            {
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                int type = json.optInt("newsid");
                notifyStatusChange(type);
            }
            else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
            {
                Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                int type = json.optInt("newsid");
                jumpPageByType(type);
            }
            else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
            {
                Log.d(TAG, "[JPushReceiver] 用户收到到RICH PUSH CALLBACK: " +
                           bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            }
            else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
            {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE,
                                                           false);
                Log.w(TAG, "[JPushReceiver]" + intent.getAction() + " connected state change to " +
                           connected);
            }
            else
            {
                Log.d(TAG, "[JPushReceiver] Unhandled intent - " + intent.getAction());
            }
        }
        catch (Exception e)
        {
        }
    }

    /**
     * 通知栏业务处理
     *
     * @param type
     */
    private void jumpPageByType(int type)
    {
        switch (type)
        {
            case APPLY_ADD_DOCTOR:
                break;
            case APPLY_ADD_DOCTOR_SUCCESS:
                break;
            case APPLY_ADD_PATIENT:
                break;
            case APPLY_ADD_PATIENT_SUCCESS:
                break;
            case APPLY_AUTH_FAILD:
                break;
            case APPLY_AUTH_SUCCESS:
                break;
        }
    }

    /**
     * 状态通知
     *
     * @param type
     */
    private void notifyStatusChange(int type)
    {
        switch (type)
        {
            case APPLY_ADD_DOCTOR:
            case APPLY_ADD_DOCTOR_SUCCESS:
                NotifyChangeListenerServer.getInstance().notifyDoctorMessageChange("");
                break;
            case APPLY_ADD_PATIENT:
            case APPLY_ADD_PATIENT_SUCCESS:
                NotifyChangeListenerServer.getInstance().notifyPatientStatusChange("");
                break;
            case APPLY_AUTH_FAILD:
            case APPLY_AUTH_SUCCESS:
                NotifyChangeListenerServer.getInstance().notifyDoctorAuthStatusListeners(type);
                break;
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle)
    {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet())
        {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA))
            {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA)))
                {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
                try
                {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext())
                    {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " +
                                  json.optString(myKey) + "]");
                    }
                }
                catch (JSONException e)
                {
                    Log.e(TAG, "Get message extra JSON error!");
                }
            }
            else
            {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle)
    {
        //		ToastUtil.toast(context, "通知消息处理");
    }
}
