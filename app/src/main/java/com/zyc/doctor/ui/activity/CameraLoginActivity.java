package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.ui.base.activity.BaseActivity;

import butterknife.BindView;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * 登录注册
 *
 * @author DUNDUN
 */
public class CameraLoginActivity extends BaseActivity implements CustomAdapt {
    @BindView(R.id.act_camera_login_next)
    TextView actCameraLoginNext;
    @BindView(R.id.act_camera_login_cancel)
    TextView actCameraLoginCancel;
    @BindView(R.id.act_camera_login_know)
    TextView actCameraLoginKnow;

    @Override
    public int getLayoutID() {
        return R.layout.act_camera_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    public void initListener() {
        super.initListener();
        actCameraLoginNext.setOnClickListener(this);
        actCameraLoginCancel.setOnClickListener(this);
        actCameraLoginKnow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v, int clickID) {
        switch (clickID) {
            case R.id.act_camera_login_next:
                break;
            case R.id.act_camera_login_cancel:
            case R.id.act_camera_login_know:
                finish();
                overridePendingTransition(R.anim.keep, R.anim.push_top_out);
                break;
            default:
                break;
        }
    }

    /*************************屏幕适配*/
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }
}
