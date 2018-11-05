package com.yht.yihuantong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/4/24.
 */
public class AddCaseInfoActivity extends BaseActivity implements CommonData
{
    private TextView tvTitle;
    private EditText editText;
    private int type = -1;
    private String value;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_add_case_info;
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
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        editText = (EditText)findViewById(R.id.act_add_health_info_edit);
        findViewById(R.id.act_add_health_info_sure).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        if (getIntent() != null)
        {
            type = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
            value = getIntent().getStringExtra(KEY_PUBLIC_STRING);
        }
        if (!TextUtils.isEmpty(value))
        {
            editText.setText(value);
            editText.setSelection(value.length());
        }
        switch (type)
        {
            case CODE_CASE_DIA:
                tvTitle.setText("诊断");
                break;
            case CODE_CASE_HOSPITAL:
                tvTitle.setText("医院");
                break;
            case CODE_CASE_TYPE:
                tvTitle.setText("科室");
                break;
            case CODE_CASE_INFO:
                tvTitle.setText("主诉");
                break;
            case CODE_CASE_NOW:
                tvTitle.setText("病史");
                break;
            case CODE_CASE_CHECK:
                tvTitle.setText("体格检查");
                break;
            case CODE_CASE_DEAL_WAY:
                tvTitle.setText("治疗");
                break;
        }
    }

    @Override
    public void initListener()
    {
        editText.setOnEditorActionListener(
                (v, actionId, event) -> (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
        backBtn.setOnClickListener(v ->
                                   {
                                       hideSoftInputFromWindow();
                                       finish();
                                   });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.act_add_health_info_sure:
                hideSoftInputFromWindow();
                String value = editText.getText().toString().trim();
                if (TextUtils.isEmpty(value))
                {
                    ToastUtil.toast(this, "内容不能为空哦");
                }
                Intent intent = new Intent(this, HealthDetailActivity.class);
                intent.putExtra(KEY_PUBLIC, value);
                setResult(RESULT_OK, intent);
                finish();
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
        inputmanger.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
