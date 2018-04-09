package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.yht.yihuantong.R;

import custom.frame.ui.activity.BaseActivity;

/**
 * 启动界面
 */
public class SplashActivity extends BaseActivity
{
    private LinearLayout llSplashPage;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_splash;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        hideBottomUIMenu();
        findViewById(R.id.act_splash_btn).setOnClickListener(this);
        llSplashPage = (LinearLayout)findViewById(R.id.act_splash_layout);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                initPage();
            }
        }, 2000);
    }

    private void startMainPage()
    {
//        Intent intent = new Intent(this, MainActivity.class);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    /**
     * 页面初始化
     */
    private void initPage()
    {
        llSplashPage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_splash_btn:
                startMainPage();
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu()
    {
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB &&
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
        {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if (Build.VERSION.SDK_INT > 19)
        {
            View decorView = getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 启动页取消操作
     */
    @Override
    public void onBackPressed()
    {
        return;
    }
}
