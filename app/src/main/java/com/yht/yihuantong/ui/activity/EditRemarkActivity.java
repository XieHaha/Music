package com.yht.yihuantong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.FilterEmojiEditText;

/**
 * Created by dundun on 18/7/2.
 */
public class EditRemarkActivity extends BaseActivity
{
    private FilterEmojiEditText etNickName;
    private String userId;
    private String nickName, remark;
    private int maxCount;
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
        etNickName = (FilterEmojiEditText)findViewById(R.id.act_edit_remark_nickname);
        if (getIntent() != null)
        {
            userId = getIntent().getStringExtra(CommonData.KEY_ID);
            nickName = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            isDealDoctor = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
        }
        if (!TextUtils.isEmpty(nickName) && nickName.length() < 20)
        {
            etNickName.setText(nickName);
            etNickName.setSelection(nickName.length());
        }
        maxCount = 10;
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_edit_remark_save).setOnClickListener(this);
        etNickName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Editable editable = etNickName.getText();
                int len = editable.length();
                if (len > maxCount)
                {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxCount);
                    etNickName.setText(newStr);
                    editable = etNickName.getText();
                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen)
                    {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        etNickName.setOnEditorActionListener((v, actionId, event) ->
                                             {
                                                 //屏蔽换行符
                                                 if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                                                 {
                                                     return true;
                                                 }
                                                 return false;
                                             });
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
                hideSoftInputFromWindow();
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
            case MODIFY_NICK_NAME_BY_PATIENT:
                Intent intent = new Intent();
                intent.putExtra(CommonData.KEY_PUBLIC, remark);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        switch (task)
        {
            case MODIFY_NICK_NAME:
                ToastUtil.toast(this, response.getMsg());
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow()
    {
        InputMethodManager inputmanger = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(etNickName.getWindowToken(), 0);
    }
}
