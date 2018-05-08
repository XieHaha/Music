package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;

import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 18/5/7.
 * 医生认证
 */
public class AuthDocActivity extends BaseActivity
{
    private TextView tvTitleMore;
    @Override
    public int getLayoutID()
    {
        return R.layout.act_auth_doc;
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
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("提交资料");
        tvTitleMore = (TextView)findViewById(R.id.public_title_bar_more_txt);
        tvTitleMore.setVisibility(View.VISIBLE);
        tvTitleMore.setText("信息");
    }
}
