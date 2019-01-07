package com.yht.yihuantong.chat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.permission.OnPermissionCallback;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.PatientInfoActivity;
import com.yht.yihuantong.ui.activity.ServicePackActivity;
import com.yht.yihuantong.ui.activity.UserInfoActivity;
import com.yht.yihuantong.utils.AllUtils;

import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 18/4/16.
 */
public class ChatActivity extends BaseActivity
        implements OnPermissionCallback, EaseChatFragment.OnRightTitleBarClickListener
{
    private String chatId, chatName;
    private EaseChatFragment easeChatFragment;
    private PopupWindow mPopupwinow;
    private View view_pop;
    private RelativeLayout rlInfoLayout, rlServiceLayout;

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
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.info_layout:
                if (!TextUtils.isEmpty(chatId))
                {
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
                        intent = new Intent(this, PatientInfoActivity.class);
                        intent.putExtra(CommonData.KEY_PATIENT_ID, chatId);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.service_layout:
                intent = new Intent(this, ServicePackActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_ID, chatId);
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "服务");
                startActivity(intent);
                break;
        }
        mPopupwinow.dismiss();
    }

    @Override
    public void onRightTitleBarClick()
    {
        showPop();
    }

    /**
     * 显示pop
     */
    private void showPop()
    {
        view_pop = LayoutInflater.from(this).inflate(R.layout.main_pop_msg, null);
        rlInfoLayout = (RelativeLayout)view_pop.findViewById(R.id.info_layout);
        rlServiceLayout = (RelativeLayout)view_pop.findViewById(R.id.service_layout);
        rlInfoLayout.setOnClickListener(this);
        if (chatId.contains("d"))
        {
            rlServiceLayout.setVisibility(View.GONE);
        }
        else
        {
            rlServiceLayout.setOnClickListener(this);
        }
        if (mPopupwinow == null)
        {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(view_pop, LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(view_pop, Gravity.TOP | Gravity.RIGHT, 0,
                                   (int)AllUtils.dipToPx(this, 65));
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
