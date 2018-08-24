package com.yht.yihuantong.ease;

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
import com.yht.yihuantong.ui.activity.HealthCardActivity;
import com.yht.yihuantong.ui.activity.RegistrationActivity;
import com.yht.yihuantong.ui.activity.UserInfoActivity;
import com.yht.yihuantong.utils.AllUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import custom.frame.bean.PatientBean;
import custom.frame.bean.RegistrationTypeBean;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/4/16.
 */
public class ChatActivity extends BaseActivity
        implements OnPermissionCallback, EaseChatFragment.OnRightTitleBarClickListener
{
    private String chatId, chatName;
    EaseChatFragment easeChatFragment;
    private PopupWindow mPopupwinow;
    private View view_pop;
    private RelativeLayout rlInfoLayout, rlPrescriptionLayout, rlCheckLayout, rlHealthCheckLayout, rlChemicalLayout;
    private List<RegistrationTypeBean> registrationTypeBeans;

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

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        registrationTypeBeans = DataSupport.findAll(RegistrationTypeBean.class, false);
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
                        intent = new Intent(this, HealthCardActivity.class);
                        PatientBean patientBean = new PatientBean();
                        patientBean.setPatientId(chatId);
                        intent.putExtra(CommonData.KEY_PATIENT_BEAN, patientBean);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.check_layout:
                intent = new Intent(this, RegistrationActivity.class);
                if (registrationTypeBeans != null && registrationTypeBeans.size() > 0)
                {
                    intent.putExtra(CommonData.KEY_PUBLIC,
                                    registrationTypeBeans.get(0).getFieldId());
                }
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "检查");
                intent.putExtra(CommonData.KEY_PATIENT_ID, chatId);
                startActivity(intent);
                break;
            case R.id.chemical_layout:
                intent = new Intent(this, RegistrationActivity.class);
                if (registrationTypeBeans != null && registrationTypeBeans.size() > 1)
                {
                    intent.putExtra(CommonData.KEY_PUBLIC,
                                    registrationTypeBeans.get(1).getFieldId());
                }
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "化验");
                intent.putExtra(CommonData.KEY_PATIENT_ID, chatId);
                startActivity(intent);
                break;
            case R.id.health_check_layout:
                intent = new Intent(this, RegistrationActivity.class);
                if (registrationTypeBeans != null && registrationTypeBeans.size() > 2)
                {
                    intent.putExtra(CommonData.KEY_PUBLIC,
                                    registrationTypeBeans.get(2).getFieldId());
                }
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "体检");
                intent.putExtra(CommonData.KEY_PATIENT_ID, chatId);
                startActivity(intent);
                break;
            case R.id.prescription_layout:
                ToastUtil.toast(this, "敬请期待");
                //                intent = new Intent(this, RegistrationActivity.class);
                //                if (registrationTypeBeans != null && registrationTypeBeans.size() > 3)
                //                {
                //                    intent.putExtra(CommonData.KEY_PUBLIC,
                //                                    registrationTypeBeans.get(3).getFieldId());
                //                }
                //                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "处方");
                //                intent.putExtra(CommonData.KEY_PATIENT_ID, chatId);
                //                startActivity(intent);
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
        rlPrescriptionLayout = (RelativeLayout)view_pop.findViewById(R.id.prescription_layout);
        rlCheckLayout = (RelativeLayout)view_pop.findViewById(R.id.check_layout);
        rlHealthCheckLayout = (RelativeLayout)view_pop.findViewById(R.id.health_check_layout);
        rlChemicalLayout = (RelativeLayout)view_pop.findViewById(R.id.chemical_layout);
        rlInfoLayout.setOnClickListener(this);
        if (chatId.contains("d"))
        {
            rlPrescriptionLayout.setVisibility(View.GONE);
            rlCheckLayout.setVisibility(View.GONE);
            rlHealthCheckLayout.setVisibility(View.GONE);
            rlChemicalLayout.setVisibility(View.GONE);
        }
        else
        {
            rlPrescriptionLayout.setOnClickListener(this);
            rlCheckLayout.setOnClickListener(this);
            rlHealthCheckLayout.setOnClickListener(this);
            rlChemicalLayout.setOnClickListener(this);
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
