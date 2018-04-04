package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;

import com.yht.yihuantong.R;

import custom.frame.ui.activity.BaseActivity;

public class CompleteInfo2Activity extends BaseActivity
{
    @Override
    public int getLayoutID()
    {
        return R.layout.act_complete_info2;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_complete_info2_back).setOnClickListener(this);
        findViewById(R.id.act_complete_info2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_complete_info2:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.act_complete_info2_back:
                finish();
                break;
            default:
                break;
        }
    }
}
