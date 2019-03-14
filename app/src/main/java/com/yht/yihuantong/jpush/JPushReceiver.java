package com.yht.yihuantong.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.api.notify.NotifyChangeListenerManager;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.ApplyCooperateDocActivity;
import com.yht.yihuantong.ui.activity.ApplyPatientActivity;
import com.yht.yihuantong.ui.activity.AuthDocStatuActivity;
import com.yht.yihuantong.ui.activity.MainActivity;
import com.yht.yihuantong.ui.activity.PatientsActivity;
import com.yht.yihuantong.ui.activity.RegistrationDetailActivity;
import com.yht.yihuantong.ui.activity.TransferPatientActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import custom.frame.utils.SharePreferenceUtil;

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
                int msgId = json.optInt("msg");
                SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(context);
                String ids = sharePreferenceUtil.getString(CommonData.KEY_NEW_MESSAGE_REMIND);
                if (TextUtils.isEmpty(ids))
                {
                    ids = String.valueOf(msgId);
                }
                else
                {
                    StringBuilder stringBuilder = new StringBuilder(ids);
                    stringBuilder.append(",");
                    stringBuilder.append(msgId);
                    ids = stringBuilder.toString();
                }
                sharePreferenceUtil.putString(CommonData.KEY_NEW_MESSAGE_REMIND, ids);
                notifyStatusChange(type, String.valueOf(msgId));
            }
            else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
            {
                Log.d(TAG, "[JPushReceiver] 用户点击打开了通知");
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                int type = json.optInt("newsid");
                int msgId = json.optInt("msg");
                jumpPageByType(context, type, msgId);
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
     * 状态通知
     *
     * @param type
     */
    private void notifyStatusChange(int type, String msgId)
    {
        switch (type)
        {
            case JIGUANG_CODE_COLLEBORATE_DOCTOR_REQUEST://合作医生申请
            case JIGUANG_CODE_COLLEBORATE_ADD_SUCCESS://合作医生添加成功
                NotifyChangeListenerManager.getInstance().notifyDoctorStatusChange("");
                break;
            case JIGUANG_CODE_DOCTOR_DP_ADD_SUCCESS://添加好友成功
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("add");
                break;
            case JIGUANG_CODE_DOCTOR_DP_ADD_REQUEST://患者申请添加好友
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
                break;
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS://医生认证成功
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED://医生认证失败
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(type);
                break;
            case JIGUANG_CODE_TRANS_PATIENT_SUCCESS://我的转诊成功
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient("to");
                break;
            case JIGUANG_CODE_TRANS_PATIENT_APPLY://合作医生的转诊申请
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient("from");
                break;
            case JIGUANG_CODE_DOCTOR_TRANS_REFUSE://拒绝接受转诊
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISHED://医院取消转诊（发送给发起医生）
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISHED://医院取消转诊（发送给接受医生）
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISH_SUCCESS://极光-医院确认患者就诊（发送给发起医生）
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISH_SUCCESS://极光-医院确认患者就诊（发送给接受医生）
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient(msgId);
                break;
            case JIGUANG_CODE_DOCTOR_PRODUCT_ACCEPTED://极光-患者确认服务包订单（发送给医生）
            case JIGUANG_CODE_DOCTOR_PRODUCT_REFUSED://极光-患者拒绝服务包订单（发送给医生）
            case JIGUANG_CODE_DOCTOR_PRODUCT_FINISH://极光-后台确认完成检查（发送给医生）
            case JIGUANG_CODE_DOCTOR_PRODUCT_REPORT://极光-后台确认发送报告（发送给医生）
                NotifyChangeListenerManager.getInstance().notifyOrderStatusChange(msgId);
                break;
        }
    }

    /**
     * 通知栏业务处理
     * 将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
     * 如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
     * 如果Task栈不存在MainActivity实例，则在栈顶创建
     * 如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
     * 但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
     * DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
     * DetailActivity前，要先启动MainActivity。
     * 如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
     * SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
     * 参数跳转到DetailActivity中去了
     * Log.i("NotificationReceiver", "the app process is dead");
     * Intent launchIntent = context.getPackageManager().
     * getLaunchIntentForPackage("com.liangzili.notificationlaunch");
     * launchIntent.setFlags(
     * Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
     * context.startActivity(launchIntent);
     *
     * @param type
     */
    private void jumpPageByType(Context context, int type, int id)
    {
        if (YihtApplication.getInstance().getLoginSuccessBean() == null)
        {
            return;
        }
        Intent mainIntent, baseIntent;
        Intent intents[];
        switch (type)
        {
            case JIGUANG_CODE_DOCTOR_DP_ADD_REQUEST://患者申请
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, ApplyPatientActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_DOCTOR_DP_ADD_SUCCESS://患者添加成功
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, PatientsActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_COLLEBORATE_DOCTOR_REQUEST://合作医生申请
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 2);
                baseIntent = new Intent(context, ApplyCooperateDocActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_COLLEBORATE_ADD_SUCCESS://合作医生添加成功
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 2);
                context.startActivity(mainIntent);
                break;
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS://医生认证成功
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED://医生认证失败
                mainIntent = new Intent(context, AuthDocStatuActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(type);
                break;
            case JIGUANG_CODE_TRANS_PATIENT_APPLY://收到转诊
            case JIGUANG_CODE_TRANS_PATIENT_SUCCESS://合作医生接受转诊
            case JIGUANG_CODE_DOCTOR_TRANS_REFUSE://合作医生拒绝接受转诊
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISHED://医院取消转诊（发送给发起医生）
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISHED://医院取消转诊（发送给接受医生）
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISH_SUCCESS://极光-医院确认患者就诊（发送给发起医生）
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISH_SUCCESS://极光-医院确认患者就诊（发送给接受医生）
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, TransferPatientActivity.class);
                baseIntent.putExtra(CommonData.KEY_TRANSFER_ID, id);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_DOCTOR_PRODUCT_ACCEPTED://极光-患者确认服务包订单（发送给医生）
            case JIGUANG_CODE_DOCTOR_PRODUCT_REFUSED://极光-患者拒绝服务包订单（发送给医生）
            case JIGUANG_CODE_DOCTOR_PRODUCT_FINISH://极光-后台确认完成检查（发送给医生）
            case JIGUANG_CODE_DOCTOR_PRODUCT_REPORT://极光-后台确认发送报告（发送给医生）
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, RegistrationDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_REGISTRATION_ID, String.valueOf(id));
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
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
