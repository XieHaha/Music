package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;

import custom.frame.ui.activity.BaseActivity;

/**
 * 启动界面
 *
 * @author DUNDUN
 */
public class SplashActivity extends BaseActivity
{
    private TextView tvStart;
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
        tvStart = (TextView)findViewById(R.id.act_splash_btn);
        tvStart.setOnClickListener(this);
        llSplashPage = (LinearLayout)findViewById(R.id.act_splash_layout);
        new Handler().postDelayed(() -> initPage(), 2000);
    }

    private void startMainPage()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        //        Intent intent = new Intent(this, CompleteInfoActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    /**
     * 页面初始化
     */
    private void initPage()
    {
        if (loginSuccessBean != null)
        {
            if (TextUtils.isEmpty(loginSuccessBean.getName()))
            {
                startActivity(new Intent(this, CompleteInfoActivity.class));
                finish();
            }
            else
            {
                startActivity(new Intent(this, MainActivity.class));
                finish();
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
            }
        }
        else
        {
            tvStart.setVisibility(View.VISIBLE);
        }
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //        //隐藏虚拟按键，并且全屏
        //        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB &&
        //                Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
        //            View v = this.getWindow().getDecorView();
        //            v.setSystemUiVisibility(View.GONE);
        //        } else if (Build.VERSION.SDK_INT > 19) {
        //            View decorView = getWindow().getDecorView();
        //            int uiOptions =
        //                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        //            decorView.setSystemUiVisibility(uiOptions);
        //        }
    }
}
