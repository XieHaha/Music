package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.yihuantong.R;

import custom.frame.ui.activity.BaseActivity;

public class HealthCardActivity extends BaseActivity
{
    @Override
    public int getLayoutID()
    {
        return R.layout.act_health_card;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("健康卡");
    }
}
