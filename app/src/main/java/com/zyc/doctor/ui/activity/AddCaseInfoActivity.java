package com.zyc.doctor.ui.activity;

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

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.utils.LogUtils;

import butterknife.BindView;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * @author dundun
 */
public class AddCaseInfoActivity extends BaseActivity implements CommonData {
    private static final String TAG = "AddCaseInfoActivity";
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.act_add_health_info_edit)
    EditText actAddHealthInfoEdit;
    private int type = -1;
    private String value;

    @Override
    public int getLayoutID() {
        return R.layout.act_add_case_info;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        findViewById(R.id.act_add_health_info_sure).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        if (getIntent() != null) {
            type = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
            value = getIntent().getStringExtra(KEY_PUBLIC_STRING);
        }
        if (!TextUtils.isEmpty(value)) {
            actAddHealthInfoEdit.setText(value);
            actAddHealthInfoEdit.setSelection(value.length());
        }
        switch (type) {
            case CODE_CASE_DIA:
                publicTitleBarTitle.setText("诊断");
                break;
            case CODE_CASE_HOSPITAL:
                publicTitleBarTitle.setText("医院");
                break;
            case CODE_CASE_TYPE:
                publicTitleBarTitle.setText("科室");
                break;
            case CODE_CASE_INFO:
                publicTitleBarTitle.setText("主诉");
                break;
            case CODE_CASE_NOW:
                publicTitleBarTitle.setText("病史");
                break;
            case CODE_CASE_CHECK:
                publicTitleBarTitle.setText("体格检查");
                break;
            case CODE_CASE_DEAL_WAY:
                publicTitleBarTitle.setText("治疗");
                break;
            default:
                break;
        }
    }

    @Override
    public void initListener() {
        actAddHealthInfoEdit.setOnEditorActionListener(
                (v, actionId, event) -> (event.getKeyCode() == KeyEvent.KEYCODE_ENTER));
        backBtn.setOnClickListener(v ->
        {
            hideSoftInputFromWindow();
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_add_health_info_sure:
                hideSoftInputFromWindow();
                String value = actAddHealthInfoEdit.getText().toString().trim();
                if (TextUtils.isEmpty(value)) {
                    ToastUtil.toast(this, "内容不能为空哦");
                }
                Intent intent = new Intent(this, HealthDetailActivity.class);
                intent.putExtra(KEY_PUBLIC, value);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow() {
        try {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(actAddHealthInfoEdit.getWindowToken(), 0);
        } catch (NullPointerException e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }
}
