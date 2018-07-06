package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/7/2.
 */
public class EditRemarkActivity extends BaseActivity
{
    private EditText etNickName;
    private String userId;
    private String nickName, remark;
    /**
     * 为true设置合作医生的备注，false设置患者的备注
     */
    private boolean isDealDoctor = false;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_edit_remark;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("设置备注");
        etNickName = (EditText)findViewById(R.id.act_edit_remark_nickname);
        if (getIntent() != null)
        {
            userId = getIntent().getStringExtra(CommonData.KEY_ID);
            nickName = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            isDealDoctor = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
        }
        if (!TextUtils.isEmpty(nickName) && nickName.length() < 20)
        {
            etNickName.setText(nickName);
            etNickName.setSelection(nickName.length() - 1);
        }
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_edit_remark_save).setOnClickListener(this);
    }

    /**
     * 设置备注  合作医生
     */
    private void modifyNickName()
    {
        mIRequest.modifyNickName(loginSuccessBean.getDoctorId(), userId, remark, this);
    }

    /**
     * 设置备注  患者
     */
    private void modifyNickNameByPatient()
    {
        mIRequest.modifyNickNameByPatient(loginSuccessBean.getDoctorId(), userId, remark, "d",
                                          this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_edit_remark_save:
                remark = etNickName.getText().toString().trim();
                if (TextUtils.isEmpty(remark))
                {
                    ToastUtil.toast(this, "备注不能为空");
                    return;
                }
                if (isDealDoctor)
                {
                    modifyNickName();
                }
                else
                {
                    modifyNickNameByPatient();
                }
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case MODIFY_NICK_NAME:
                Intent intent = new Intent();
                intent.putExtra(CommonData.KEY_PUBLIC, remark);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
