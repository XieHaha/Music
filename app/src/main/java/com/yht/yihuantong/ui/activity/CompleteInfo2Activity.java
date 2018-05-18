package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * 职业信息完善
 *
 * @author DUNDUN
 */
public class CompleteInfo2Activity extends BaseActivity
{
    private EditText etHospital, etType, etJob, etIntroduce;
    private String hospital, type, job, introduce;

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
        etHospital = (EditText)findViewById(R.id.act_complete_info2_hospital);
        etType = (EditText)findViewById(R.id.act_complete_info2_type);
        etJob = (EditText)findViewById(R.id.act_complete_info2_job);
        etIntroduce = (EditText)findViewById(R.id.act_complete_info2_introduce);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_complete_info2_back).setOnClickListener(this);
        findViewById(R.id.act_complete_info2).setOnClickListener(this);
        etIntroduce.setOnEditorActionListener((v, actionId, event) ->
                                              {
                                                  if (actionId == EditorInfo.IME_ACTION_DONE)
                                                  {
                                                      updateJobInfo();
                                                  }
                                                  return false;
                                              });
    }

    /**
     * 上传职业信息
     */
    private void updateJobInfo()
    {
        hospital = etHospital.getText().toString().trim();
        type = etType.getText().toString().trim();
        job = etJob.getText().toString().trim();
        introduce = etIntroduce.getText().toString().trim();
        if (TextUtils.isEmpty(hospital) || TextUtils.isEmpty(type) || TextUtils.isEmpty(job) ||
            TextUtils.isEmpty(introduce))
        {
            ToastUtil.toast(this, R.string.toast_upload_job_info_hint);
            return;
        }
        showProgressDialog("");
        mIRequest.updateJobInfo(loginSuccessBean.getDoctorId(), loginSuccessBean.getChecked(),type, introduce, hospital, job,
                                this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_complete_info2:
                updateJobInfo();
                break;
            case R.id.act_complete_info2_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case UPDATE_JOB_INFO:
                closeProgressDialog();
                loginSuccessBean.setDepartment(type);
                loginSuccessBean.setHospital(hospital);
                loginSuccessBean.setDoctorDescription(introduce);
                loginSuccessBean.setTitle(job);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        closeProgressDialog();
        super.onResponseCodeError(task, response);
        switch (task)
        {
            case UPDATE_JOB_INFO:
                ToastUtil.toast(this, "code:" + response.getCode() + "  msg:" + response.getMsg());
                break;
            default:
                break;
        }
    }
}
