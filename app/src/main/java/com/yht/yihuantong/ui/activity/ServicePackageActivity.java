package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.dialog.SimpleDialog;

import org.litepal.crud.DataSupport;

import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.RegistrationTypeBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/9/2.
 */
public class ServicePackageActivity extends BaseActivity
{
    private List<RegistrationTypeBean> registrationTypeBeans;
    private String patientId;
    /**
     * 转诊患者
     */
    private static final int CHANGE_PATIENT = 2;
    /**
     * 选择转诊医生回调
     */
    private static final int CHANGE_PATIENT_REQUEST_CODE = 200;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_service_package;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("服务信息");
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        registrationTypeBeans = DataSupport.findAll(RegistrationTypeBean.class, false);
        if (getIntent() != null)
        {
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_service_package_change_layout).setOnClickListener(this);
        findViewById(R.id.act_service_package_prescription_layout).setOnClickListener(this);
        findViewById(R.id.act_service_package_check_layout).setOnClickListener(this);
        findViewById(R.id.act_service_package_chemical_layout).setOnClickListener(this);
        findViewById(R.id.act_service_package_health_check_layout).setOnClickListener(this);
        findViewById(R.id.act_service_package_delete_layout).setOnClickListener(this);
        findViewById(R.id.act_service_package_service_layout).setOnClickListener(this);
    }

    /**
     * 医生扫码添加患者  转诊患者
     * {@link #CHANGE_PATIENT}
     */
    private void addPatientByScanOrChangePatient(String doctorId)
    {
        mIRequest.addPatientByScanOrChangePatient(doctorId, loginSuccessBean.getDoctorId(),
                                                  patientId, CHANGE_PATIENT, this);
    }

    /**
     * 删除病人(取消关注)
     */
    private void deletePatient()
    {
        mIRequest.deletePatient(loginSuccessBean.getDoctorId(), patientId, this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.act_service_package_change_layout:
                intent = new Intent(this, CooperateDocActivity.class);
                startActivityForResult(intent, CHANGE_PATIENT_REQUEST_CODE);
                break;
            case R.id.act_service_package_prescription_layout:
                ToastUtil.toast(this, "敬请期待");
                break;
            case R.id.act_service_package_check_layout:
                intent = new Intent(this, RegistrationActivity.class);
                if (registrationTypeBeans != null && registrationTypeBeans.size() > 0)
                {
                    intent.putExtra(CommonData.KEY_PUBLIC,
                                    registrationTypeBeans.get(0).getFieldId());
                }
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "检查");
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                startActivity(intent);
                break;
            case R.id.act_service_package_chemical_layout:
                intent = new Intent(this, RegistrationActivity.class);
                if (registrationTypeBeans != null && registrationTypeBeans.size() > 1)
                {
                    intent.putExtra(CommonData.KEY_PUBLIC,
                                    registrationTypeBeans.get(1).getFieldId());
                }
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "化验");
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                startActivity(intent);
                break;
            case R.id.act_service_package_health_check_layout:
                intent = new Intent(this, RegistrationActivity.class);
                if (registrationTypeBeans != null && registrationTypeBeans.size() > 2)
                {
                    intent.putExtra(CommonData.KEY_PUBLIC,
                                    registrationTypeBeans.get(2).getFieldId());
                }
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "体检");
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                startActivity(intent);
                break;
            case R.id.act_service_package_delete_layout:
                new SimpleDialog(this, "确定删除当前患者?", (dialog, which) -> deletePatient(),
                                 (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.act_service_package_service_layout:
                intent = new Intent(this, ServicePackActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "服务");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case DELETE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case CHANGE_PATIENT_REQUEST_CODE:
                if (data != null)
                {
                    String doctorId = data.getStringExtra(CommonData.KEY_DOCTOR_ID);
                    addPatientByScanOrChangePatient(doctorId);
                }
                break;
        }
    }
}
