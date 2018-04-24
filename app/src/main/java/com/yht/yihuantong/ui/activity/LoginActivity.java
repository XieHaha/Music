package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.utils.AllUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * 登录注册
 *
 * @author DUNDUN
 */
public class LoginActivity extends BaseActivity {
    private TextView tvGetVerify;
    private EditText etPhone, etVerifyCode;
    private Timer timer;
    private String phone, verifyCode;
    private int time = 0;
    /**
     * 是否获取过验证码
     */
    private boolean IS_SEND_VERIFY_CODE = false;
    private static final int MAX_RESEND_TIME = 60;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (time <= 0) {
                tvGetVerify.setClickable(true);
                tvGetVerify.setSelected(true);
                tvGetVerify.setText(R.string.txt_get_verify_code_again);
            } else {
                tvGetVerify.setClickable(false);
                tvGetVerify.setSelected(false);
                tvGetVerify.setText(time + "秒后重试");
            }
            return true;
        }
    });

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        findViewById(R.id.act_login_btn).setOnClickListener(this);
        tvGetVerify = (TextView) findViewById(R.id.act_login_verify);
        etPhone = (EditText) findViewById(R.id.act_login_phone);
        etVerifyCode = (EditText) findViewById(R.id.act_login_verifycode);
    }

    @Override
    public void initListener() {
        super.initListener();
        tvGetVerify.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                if (string.length() == 11) {
                    tvGetVerify.setSelected(true);
                } else {
                    tvGetVerify.setSelected(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_login_verify:
                if (!tvGetVerify.isSelected()) {
                    return;
                }
                //获取验证码
                phone = etPhone.getText().toString().trim();
                if (!AllUtils.isMobileNumber(phone)) {
                    ToastUtil.toast(this, R.string.toast_txt_phone_error);
                    return;
                }
                //获取验证码
                getVerifyCode();
                break;
            case R.id.act_login_btn:
//                if (!IS_SEND_VERIFY_CODE) {
//                    ToastUtil.toast(this, R.string.toast_txt_get_verifycoder_error);
//                    return;
//                }
                phone = etPhone.getText().toString().trim();
                verifyCode = etVerifyCode.getText().toString().trim();
                if (StringUtils.isEmpty(verifyCode)) {
                    ToastUtil.toast(this, R.string.toast_txt_verify_hint);
                    return;
                }
                loginAndRegister();
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        mIRequest.getVerifyCode(phone, this);
    }

    /**
     * 登录 注册
     */
    private void loginAndRegister() {
        showProgressDialog("",false);
        mIRequest.loginAndRegister(phone, verifyCode, "d", this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_VERIFY_CODE:
                IS_SEND_VERIFY_CODE = true;
                ToastUtil.toast(this, R.string.toast_txt_verifycode_success);

                time = MAX_RESEND_TIME;
                //org.apache.commons.lang3.concurrent.BasicThreadFactory
                final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                        new BasicThreadFactory.Builder().namingPattern("yht-thread-pool-%d").daemon(true).build());
                executorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        time--;
                        if (time < 0) {
                            time = 0;
                            executorService.shutdownNow();
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }, 0, 1, TimeUnit.SECONDS);

                break;
            case LOGIN_AND_REGISTER:
                //保存登录成功数据
                loginSuccessBean = response.getData();
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                EMClient.getInstance().login(loginSuccessBean.getDoctorId(), "111111", new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                                Log.d("main", "登录聊天服务器成功！");
                                jumpTopage();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("main", "登录聊天服务器失败！");
                        closeProgressDialog();
                    }
                });
            default:
                break;
        }
    }

    private void jumpTopage()
    {
        closeProgressDialog();
        //暂时只判断用户名是否为空  为空进入信息完善流程
        if (TextUtils.isEmpty(loginSuccessBean.getName())) {
            startActivity(new Intent(this, CompleteInfoActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response) {
        closeProgressDialog();
        super.onResponseCodeError(task, response);
        switch (task) {
            case GET_VERIFY_CODE:
                ToastUtil.toast(this, "code:" + response.getCode() + "  msg:" + response.getMsg());
                break;
            case LOGIN_AND_REGISTER:
                break;
            default:
                break;
        }
    }
}
