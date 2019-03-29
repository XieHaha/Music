package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.zyc.doctor.R;

import custom.frame.ui.activity.BaseActivity;

/**
 * @author dundun
 * @date 18/9/2
 */
public class AboutOurActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_about_our;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("关于我们");
    }
}