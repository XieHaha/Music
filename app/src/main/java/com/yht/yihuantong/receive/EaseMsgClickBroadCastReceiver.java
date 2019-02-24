package com.yht.yihuantong.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.chat.ChatActivity;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.MainActivity;

/**
 * Created by dundun on 19/2/23.
 */
public class EaseMsgClickBroadCastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if ("ease.msg.android.intent.CLICK".equals(intent.getAction()))
        {
            Log.i("test", "EaseMsgClickBroadCastReceiver....");
            if (YihtApplication.getInstance().getLoginSuccessBean() == null)
            {
                return;
            }
            String chatId = intent.getStringExtra(CommonData.KEY_CHAT_ID);
            Intent mainIntent, baseIntent;
            Intent intents[];
            mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.putExtra(CommonData.KEY_PUBLIC, 1);
            baseIntent = new Intent(context, ChatActivity.class);
            baseIntent.putExtra(CommonData.KEY_CHAT_ID, chatId);
            intents = new Intent[] { mainIntent, baseIntent };
            context.startActivities(intents);
        }
    }
}
