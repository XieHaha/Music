package com.zyc.doctor.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.MessageNotifyHelper;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.ui.activity.ApplyCooperateDocActivity;
import com.zyc.doctor.ui.activity.ApplyPatientActivity;
import com.zyc.doctor.ui.activity.AuthDocStatusActivity;
import com.zyc.doctor.ui.activity.MainActivity;
import com.zyc.doctor.ui.activity.PatientsActivity;
import com.zyc.doctor.ui.activity.RegistrationDetailActivity;
import com.zyc.doctor.ui.activity.TransferPatientActivity;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.SharePreferenceUtil;

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
 *
 * @author dundun
 */
public class JiPushReceiver extends BroadcastReceiver implements CommonData {
    private static final String TAG = "JIGUANG";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            LogUtils.i(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                int type = json.optInt("newsid");
                int msgId = json.optInt("msg");
                //新通知本地存储
                MessageNotifyHelper.save(new SharePreferenceUtil(context), type, msgId);
                notifyStatusChange(type, String.valueOf(msgId));
            }
            else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                int type = json.optInt("newsid");
                int msgId = json.optInt("msg");
                jumpPageByType(context, type, msgId);
            }
            else {
                LogUtils.i(TAG, "[JiPushReceiver] Unhandled intent - " + intent.getAction());
            }
        }
        catch (Exception e) {
            LogUtils.w(TAG, "Exception error", e);
        }
    }

    /**
     * 状态通知
     *
     * @param type
     */
    private void notifyStatusChange(int type, String msgId) {
        switch (type) {
            case JIGUANG_CODE_COLLEBORATE_DOCTOR_REQUEST:
            case JIGUANG_CODE_COLLEBORATE_ADD_SUCCESS:
                NotifyChangeListenerManager.getInstance().notifyDoctorStatusChange("");
                break;
            case JIGUANG_CODE_DOCTOR_DP_ADD_SUCCESS:
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("add");
                break;
            case JIGUANG_CODE_DOCTOR_DP_ADD_REQUEST:
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
                break;
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS:
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED:
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(type);
                break;
            case JIGUANG_CODE_TRANS_PATIENT_SUCCESS:
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient("to");
                break;
            case JIGUANG_CODE_TRANS_PATIENT_APPLY:
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient("from");
                break;
            case JIGUANG_CODE_DOCTOR_TRANS_REFUSE:
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISHED:
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISHED:
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISH_SUCCESS:
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISH_SUCCESS:
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient(msgId);
                break;
            case JIGUANG_CODE_DOCTOR_PRODUCT_ACCEPTED:
            case JIGUANG_CODE_DOCTOR_PRODUCT_REFUSED:
            case JIGUANG_CODE_DOCTOR_PRODUCT_FINISH:
            case JIGUANG_CODE_DOCTOR_PRODUCT_REPORT:
                NotifyChangeListenerManager.getInstance().notifyOrderStatusChange(msgId);
                break;
            default:
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
    private void jumpPageByType(Context context, int type, int id) {
        if (YihtApplication.getInstance().getLoginSuccessBean() == null) {
            return;
        }
        Intent mainIntent, baseIntent;
        Intent[] intents;
        switch (type) {
            case JIGUANG_CODE_DOCTOR_DP_ADD_REQUEST:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, ApplyPatientActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_DOCTOR_DP_ADD_SUCCESS:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, PatientsActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_COLLEBORATE_DOCTOR_REQUEST:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 2);
                baseIntent = new Intent(context, ApplyCooperateDocActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_COLLEBORATE_ADD_SUCCESS:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 2);
                context.startActivity(mainIntent);
                break;
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS:
            case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED:
                mainIntent = new Intent(context, AuthDocStatusActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(type);
                break;
            case JIGUANG_CODE_TRANS_PATIENT_APPLY:
            case JIGUANG_CODE_TRANS_PATIENT_SUCCESS:
            case JIGUANG_CODE_DOCTOR_TRANS_REFUSE:
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISHED:
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISHED:
            case JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISH_SUCCESS:
            case JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISH_SUCCESS:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, TransferPatientActivity.class);
                baseIntent.putExtra(CommonData.KEY_TRANSFER_ID, id);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case JIGUANG_CODE_DOCTOR_PRODUCT_ACCEPTED:
            case JIGUANG_CODE_DOCTOR_PRODUCT_REFUSED:
            case JIGUANG_CODE_DOCTOR_PRODUCT_FINISH:
            case JIGUANG_CODE_DOCTOR_PRODUCT_REPORT:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, RegistrationDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_REGISTRATION_ID, String.valueOf(id));
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            default:
                break;
        }
    }

    /**
     * 打印所有的 intent extra 数据
     *
     * @param bundle a
     * @return a
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                }
                catch (JSONException e) {
                    LogUtils.e(TAG, "Get message extra JSON error!");
                }
            }
            else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
