package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.yihuantong.R;

import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 18/10/9.
 */
public class HospitalInfoActivity extends BaseActivity
{
    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_hospital_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("医院信息");
    }
}
