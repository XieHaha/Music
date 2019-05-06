package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.BaseData;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CameraLoginBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.ToastUtil;

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
    @BindView(R.id.act_camera_hint)
    TextView actCameraHint;
    private CameraLoginBean cameraLoginBean;
    private String value;

    @Override
    public int getLayoutID() {
        return R.layout.act_camera_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (getIntent() != null) {
            cameraLoginBean = (CameraLoginBean)getIntent().getSerializableExtra(CommonData.KEY_CAMERA_LOGIN_BEAN);
        }
        remoteConsultationVerify();
    }

    private void initPageData(boolean tag) {
        if (!tag) {
            actCameraLoginNext.setVisibility(View.GONE);
            actCameraLoginCancel.setVisibility(View.GONE);
            actCameraLoginKnow.setVisibility(View.VISIBLE);
            actCameraHint.setText(
                    String.format(getString(R.string.txt_camera_login_error_hint), cameraLoginBean.getHospitalName(),
                                  cameraLoginBean.getDepartmentTypeName()));
            actCameraHint.setTextColor(ContextCompat.getColor(this, R.color._878787));
        }
        else {
            actCameraLoginNext.setVisibility(View.VISIBLE);
            actCameraLoginCancel.setVisibility(View.VISIBLE);
        }
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
                remoteConsultationLogin();
                break;
            case R.id.act_camera_login_cancel:
            case R.id.act_camera_login_know:
                finish();
                overridePendingTransition(R.anim.keep, R.anim.push_bottom_out);
                break;
            default:
                break;
        }
    }

    private void remoteConsultationVerify() {
        RequestUtils.remoteConsultationVerify(this, loginSuccessBean.getDoctorId(), cameraLoginBean.getPageUnicode(),
                                              this);
    }

    private void remoteConsultationLogin() {
        RequestUtils.remoteConsultationLogin(this, loginSuccessBean.getDoctorId(), cameraLoginBean.getPageUnicode(),
                                             this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case REMOTE_CONSULTATION_LOGIN:
                ToastUtil.toast(this, response.getMsg());
                finish();
                break;
            case REMOTE_CONSULTATION_VERIFY:
                initPageData(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        switch (task) {
            case REMOTE_CONSULTATION_VERIFY:
                initPageData(false);
                break;
            case REMOTE_CONSULTATION_LOGIN:
                ToastUtil.toast(this, response.getMsg());
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
        return BaseData.BASE_DEVICE_DEFAULT_WIDTH;
    }
}
