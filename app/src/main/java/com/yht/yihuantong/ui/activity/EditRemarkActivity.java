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

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.FilterEmojiEditText;

/**
 * Created by dundun on 18/7/2.
 */
public class EditRemarkActivity extends BaseActivity {
    @BindView(R.id.act_edit_remark_nickname)
    FilterEmojiEditText actEditRemarkNickname;
    private String userId;
    private String nickName, remark;
    private int maxCount;
    /**
     * 为true设置合作医生的备注，false设置患者的备注
     */
    private boolean isDealDoctor = false;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_edit_remark;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("设置备注");
        if (getIntent() != null) {
            userId = getIntent().getStringExtra(CommonData.KEY_ID);
            nickName = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            isDealDoctor = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
        }
        if (!TextUtils.isEmpty(nickName) && nickName.length() < 20) {
            actEditRemarkNickname.setText(nickName);
            actEditRemarkNickname.setSelection(nickName.length());
        }
        maxCount = 10;
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.act_edit_remark_save).setOnClickListener(this);
        actEditRemarkNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int mTextMaxlenght = 0;
                Editable editable = actEditRemarkNickname.getText();
                String str = editable.toString().trim();
                //得到最初字段的长度大小,用于光标位置的判断
                int selEndIndex = Selection.getSelectionEnd(editable);
                // 取出每个字符进行判断,如果是字母数字和标点符号则为一个字符加1,
                //如果是汉字则为两个字符
                for (int i = 0; i < str.length(); i++) {
                    char charAt = str.charAt(i);
                    //32-122包含了空格,大小写字母,数字和一些常用的符号,
                    //如果在这个范围内则算一个字符,
                    //如果不在这个范围比如是汉字的话就是两个字符
                    if (charAt >= 32 && charAt <= 122) {
                        mTextMaxlenght++;
                    } else {
                        mTextMaxlenght += 2;
                    }
                    // 当最大字符大于10时,进行字段的截取,并进行提示字段的大小
                    if (mTextMaxlenght > 20) {
                        // 截取最大的字段
                        String newStr = str.substring(0, i);
                        actEditRemarkNickname.setText(newStr);
                        // 得到新字段的长度值
                        editable = actEditRemarkNickname.getText();
                        int newLen = editable.length();
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
                        // 设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        actEditRemarkNickname.setOnEditorActionListener((v, actionId, event) ->
        {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
    }

    /**
     * 设置备注  合作医生
     */
    private void modifyNickName() {
        mIRequest.modifyNickName(loginSuccessBean.getDoctorId(), userId, remark, this);
    }

    /**
     * 设置备注  患者
     */
    private void modifyNickNameByPatient() {
        mIRequest.modifyNickNameByPatient(loginSuccessBean.getDoctorId(), userId, remark, "d",
                this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_edit_remark_save:
                hideSoftInputFromWindow();
                remark = actEditRemarkNickname.getText().toString().trim();
                //                if (TextUtils.isEmpty(remark))
                //                {
                //                    ToastUtil.toast(this, "备注不能为空");
                //                    return;
                //                }
                if (isDealDoctor) {
                    modifyNickName();
                } else {
                    modifyNickNameByPatient();
                }
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
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
    public void onResponseCodeError(Tasks task, BaseResponse response) {
        super.onResponseCodeError(task, response);
        switch (task) {
            case MODIFY_NICK_NAME:
                ToastUtil.toast(this, response.getMsg());
                break;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow() {
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(actEditRemarkNickname.getWindowToken(), 0);
    }
}
