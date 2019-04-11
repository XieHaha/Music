package com.zyc.doctor.ui.activity;

import com.zyc.doctor.R;
import com.zyc.doctor.ui.base.activity.BaseActivity;

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
}
