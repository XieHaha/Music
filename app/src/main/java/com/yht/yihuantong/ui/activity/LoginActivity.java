package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yht.yihuantong.R;

import java.util.Timer;

import custom.frame.ui.activity.BaseActivity;

public class LoginActivity extends BaseActivity
{
    private TextView tvGetVerify;
    private Timer timer;
    private int time = 0;
    //发送验证码可用性
    private boolean sendCodeAble = true;
    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            if (time <= 0)
            {
                sendCodeAble = true;
                tvGetVerify.setText(R.string.txt_get_verify_code_again);
            }
            else
            {
                tvGetVerify.setText(time + "秒后重试");
            }
            return true;
        }
    });

    @Override
    public int getLayoutID()
    {
        return R.layout.act_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        findViewById(R.id.act_splash_btn).setOnClickListener(this);
        tvGetVerify = (TextView)findViewById(R.id.act_login_verify);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvGetVerify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_login_verify:
                //获取验证码
                break;
            case R.id.act_splash_btn:
                startActivity(new Intent(this, CompleteInfoActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
