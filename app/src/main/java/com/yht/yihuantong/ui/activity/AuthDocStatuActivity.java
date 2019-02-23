package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.DocAuthStatu;

import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 19/2/19.
 */
public class AuthDocStatuActivity extends BaseActivity implements DocAuthStatu, CommonData
{
    private TextView tvName, tvVerifying, tvHint, tvNext, tvAgain;
    private ImageView ivBack, ivImg;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 认证
     */
    public static final int REQUEST_CODE_AUTH = 100;
    /**
     * 重新认证
     */
    public static final int REQUEST_CODE_AUTH_AGAIN = 200;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED:
                    //认证失败  更新本地数据
                    loginSuccessBean.setChecked(2);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initPageData();
                    break;
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS:
                    //认证成功  更新本地数据
                    loginSuccessBean.setChecked(6);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initPageData();
                    break;
            }
        }
    };
    /**
     * 医生认证状态
     */
    private IChange<Integer> doctorAuthStatusChangeListener = data ->
    {
        handler.sendEmptyMessage(data);
    };

    @Override
    public int getLayoutID()
    {
        return R.layout.act_auth_doc_statu;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ivBack = (ImageView)findViewById(R.id.public_title_bar_back);
        ivImg = (ImageView)findViewById(R.id.act_auth_doc_statu_img);
        tvName = (TextView)findViewById(R.id.act_auth_doc_statu_name);
        tvVerifying = (TextView)findViewById(R.id.act_auth_doc_statu_verifying);
        tvHint = (TextView)findViewById(R.id.act_auth_doc_statu_hint);
        tvNext = (TextView)findViewById(R.id.act_auth_doc_statu_next);
        tvAgain = (TextView)findViewById(R.id.act_auth_doc_statu_again);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance()
                                                .getServer(INotifyChangeListenerServer.class);
        tvName.setText(
                String.format(getString(R.string.txt_doc_auth_name), loginSuccessBean.getName()));
        initPageData();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        ivBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvAgain.setOnClickListener(this);
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(
                doctorAuthStatusChangeListener, RegisterType.REGISTER);
    }

    private void initPageData()
    {
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        int checked = loginSuccessBean.getChecked();
        switch (checked)
        {
            case NONE:
                tvVerifying.setVisibility(View.INVISIBLE);
                tvHint.setText(R.string.txt_doc_auth_hint);
                tvNext.setVisibility(View.VISIBLE);
                tvAgain.setVisibility(View.GONE);
                ivImg.setSelected(false);
                break;
            case VERIFYING:
                tvVerifying.setVisibility(View.VISIBLE);
                tvVerifying.setText(R.string.txt_doc_auth_verifying);
                tvHint.setText(R.string.txt_doc_auth_hint1);
                tvNext.setVisibility(View.GONE);
                tvAgain.setVisibility(View.VISIBLE);
                ivImg.setSelected(false);
                break;
            case VERIFY_FAILD:
                tvVerifying.setText(R.string.txt_doc_auth_verify_faild);
                tvHint.setText(R.string.txt_doc_auth_hint2);
                ivImg.setSelected(true);
                tvNext.setVisibility(View.VISIBLE);
                tvNext.setText(R.string.txt_doc_auth_again);
                tvAgain.setVisibility(View.GONE);
                break;
            case VERIFY_SUCCESS:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.act_auth_doc_statu_next:
                intent = new Intent(this, AuthDocActivity.class);
                //认证失败
                if (VERIFY_FAILD == loginSuccessBean.getChecked())
                {
                    intent.putExtra("again", true);
                }
                startActivityForResult(intent, REQUEST_CODE_AUTH);
                break;
            case R.id.act_auth_doc_statu_again:
                intent = new Intent(this, AuthDocActivity.class);
                intent.putExtra("again", true);
                startActivityForResult(intent, REQUEST_CODE_AUTH_AGAIN);
                break;
            case R.id.public_title_bar_back:
                finishPage();
                break;
        }
    }

    /**
     * 判断 登录
     */
    private void finishPage()
    {
        //清除登录信息
        YihtApplication.getInstance().clearLoginSuccessBean();
        //退出环信
        EMClient.getInstance().logout(true);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            finishPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case REQUEST_CODE_AUTH:
                initPageData();
                break;
            case REQUEST_CODE_AUTH_AGAIN:
                initPageData();
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(
                doctorAuthStatusChangeListener, RegisterType.UNREGISTER);
    }
}
