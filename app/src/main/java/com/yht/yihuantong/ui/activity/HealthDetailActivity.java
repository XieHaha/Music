package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import custom.frame.ui.activity.BaseActivity;

/**
 * 病例详情（编辑）
 *
 * @author DUNDUN
 */
public class HealthDetailActivity extends BaseActivity
{
    private TextView tvTitleBarMore;
    /**
     * 是否新增病例
     */
    private boolean isAddNewHealth = false;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_health_detail;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("病例详情");
        tvTitleBarMore = (TextView)findViewById(R.id.public_title_bar_more_txt);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            isAddNewHealth = getIntent().getBooleanExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
        }
        if (isAddNewHealth)
        {
            tvTitleBarMore.setVisibility(View.VISIBLE);
            tvTitleBarMore.setText("编辑");
        }
    }
}
