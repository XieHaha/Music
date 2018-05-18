package com.yht.yihuantong.ease;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.permission.OnPermissionCallback;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.HealthCardActivity;
import com.yht.yihuantong.ui.activity.UserInfoActivity;

import custom.frame.bean.PatientBean;
import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 18/4/16.
 */
public class ChatActivity extends BaseActivity
        implements OnPermissionCallback, EaseChatFragment.OnRightTitleBarClickListener
{
    private String chatId, chatName;
    EaseChatFragment easeChatFragment;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_chat;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        if (getIntent() != null)
        {
            chatId = getIntent().getStringExtra(CommonData.KEY_CHAT_ID);
            chatName = getIntent().getStringExtra(CommonData.KEY_CHAT_NAME);
        }
        //        ((TextView)findViewById(R.id.public_title_bar_title)).setText(chatName);
        easeChatFragment = new EaseChatFragment();  //环信聊天界面
        easeChatFragment.setChatFragmentHelper(new ChatHelper());
        easeChatFragment.setOnRightTitleBarClickListener(this);
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, chatId);
        easeChatFragment.hideTitleBar();
        easeChatFragment.setArguments(args);
        replaceFragment(R.id.act_chat_root, easeChatFragment, ChatActivity.class.getName());
    }

    /**
     * 替换fragment
     *
     * @param containerResId 需要替换的资源布局
     * @param fragment       目标Fragment
     * @param tag            替换后Tag
     */
    public void replaceFragment(final int containerResId, final EaseChatFragment fragment,
            final String tag)
    {
        runOnUiThread(() ->
                      {
                          FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                          if (TextUtils.isEmpty(tag))
                          {
                              fragmentTransaction.replace(containerResId, fragment);
                          }
                          else
                          {
                              fragmentTransaction.replace(containerResId, fragment, tag);
                          }
                          fragmentTransaction.commitAllowingStateLoss();
                      });
    }

    @Override
    public void onRightTitleBarClick()
    {
        if (!TextUtils.isEmpty(chatId))
        {
            Intent intent;
            if (chatId.contains("d"))
            {
                intent = new Intent(this, UserInfoActivity.class);
                intent.putExtra(CommonData.KEY_DOCTOR_ID, chatId);
                intent.putExtra(CommonData.KEY_IS_DEAL_DOC, false);
                intent.putExtra(CommonData.KEY_IS_FORBID_CHAT, true);
                startActivity(intent);
            }
            else
            {
                intent = new Intent(this, HealthCardActivity.class);
                PatientBean patientBean = new PatientBean();
                patientBean.setPatientId(chatId);
                intent.putExtra(CommonData.KEY_PATIENT_BEAN, patientBean);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onPermissionGranted(permissionName);
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onPermissionDeclined(permissionName);
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onPermissionPreGranted(permissionsName);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onPermissionReallyDeclined(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName)
    {
        if (easeChatFragment != null)
        {
            easeChatFragment.onNoPermissionNeeded(permissionName);
        }
    }
}
