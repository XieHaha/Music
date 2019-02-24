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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.DocAuthStatu;
import com.yht.yihuantong.utils.AllUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import custom.frame.bean.BaseResponse;
import custom.frame.http.Tasks;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.SharePreferenceUtil;
import custom.frame.utils.ToastUtil;

/**
 * 登录注册
 *
 * @author DUNDUN
 */
public class LoginActivity extends BaseActivity implements DocAuthStatu
{
    private TextView tvGetVerify;
    private LinearLayout llProtolLayout;
    private ImageView ivProtolImg;
    private EditText etPhone, etVerifyCode;
    private SharePreferenceUtil sharePreferenceUtil;
    private String phone, verifyCode;
    private int time = 0;
    /**
     * 是否获取过验证码
     */
    private boolean IS_SEND_VERIFY_CODE = false;
    private static final int MAX_RESEND_TIME = 60;
    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message message)
        {
            if (time <= 0)
            {
                tvGetVerify.setClickable(true);
                tvGetVerify.setSelected(true);
                tvGetVerify.setText(R.string.txt_get_verify_code_again);
            }
            else
            {
                tvGetVerify.setClickable(false);
                tvGetVerify.setSelected(false);
                tvGetVerify.setText(time + "秒后重试");
            }
            return true;
        }
    });

    @Override
    public int getLayoutID()
    {
        return R.layout.act_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        findViewById(R.id.act_login_btn).setOnClickListener(this);
        tvGetVerify = (TextView)findViewById(R.id.act_login_verify);
        ivProtolImg = (ImageView)findViewById(R.id.act_login_protocol_img);
        llProtolLayout = (LinearLayout)findViewById(R.id.act_login_protocol_layout);
        etPhone = (EditText)findViewById(R.id.act_login_phone);
        etVerifyCode = (EditText)findViewById(R.id.act_login_verifycode);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        sharePreferenceUtil = new SharePreferenceUtil(this);
        phone = sharePreferenceUtil.getString(CommonData.KEY_USER_PHONE);
        if (!TextUtils.isEmpty(phone))
        {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
            tvGetVerify.setSelected(true);
        }
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_login_protocol).setOnClickListener(this);
        tvGetVerify.setOnClickListener(this);
        ivProtolImg.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String string = s.toString();
                if (TextUtils.isEmpty(string))
                {
                    return;
                }
                if (string.length() == 11)
                {
                    tvGetVerify.setSelected(true);
                }
                else
                {
                    tvGetVerify.setSelected(false);
                }
            }
        });
        etVerifyCode.setOnEditorActionListener((v, actionId, event) ->
                                               {
                                                   if (actionId == EditorInfo.IME_ACTION_DONE)
                                                   {
                                                       loginAndRegister();
                                                   }
                                                   return false;
                                               });
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_login_verify:
                if (!tvGetVerify.isSelected())
                {
                    return;
                }
                //获取验证码
                phone = etPhone.getText().toString().trim();
                if (!AllUtils.isMobileNumber(phone))
                {
                    ToastUtil.toast(this, R.string.toast_txt_phone_error);
                    return;
                }
                //获取验证码
                getVerifyCode();
                break;
            case R.id.act_login_btn:
                loginAndRegister();
                break;
            case R.id.act_login_protocol:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", HttpConstants.BASE_BASIC_USER_PROTOCOL_URL);
                startActivity(intent);
                break;
            case R.id.act_login_protocol_img:
                if (ivProtolImg.isSelected())
                {
                    ivProtolImg.setSelected(false);
                }
                else
                {
                    ivProtolImg.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode()
    {
        mIRequest.getVerifyCode(phone, this);
    }

    /**
     * 登录 注册
     */
    private void loginAndRegister()
    {
        //                if (!IS_SEND_VERIFY_CODE) {
        //                    ToastUtil.toast(this, R.string.toast_txt_get_verifycoder_error);
        //                    return;
        //                }
        phone = etPhone.getText().toString().trim();
        verifyCode = etVerifyCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone))
        {
            ToastUtil.toast(this, R.string.txt_login_hint);
            return;
        }
        if (TextUtils.isEmpty(verifyCode))
        {
            ToastUtil.toast(this, R.string.toast_txt_verify_hint);
            return;
        }
        if (!AllUtils.isMobileNumber(phone))
        {
            ToastUtil.toast(this, R.string.toast_txt_phone_error);
            return;
        }
        if (StringUtils.isEmpty(verifyCode))
        {
            ToastUtil.toast(this, R.string.toast_txt_verify_hint);
            return;
        }
        if (!ivProtolImg.isSelected())
        {
            ToastUtil.toast(this, "请先阅读并勾选使用协议");
            return;
        }
        showProgressDialog("登录中", false);
        mIRequest.loginAndRegister(phone, verifyCode, "d", this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_VERIFY_CODE:
                IS_SEND_VERIFY_CODE = true;
                ToastUtil.toast(this, R.string.toast_txt_verifycode_success);
                time = MAX_RESEND_TIME;
                //org.apache.commons.lang3.concurrent.BasicThreadFactory
                final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                                                                                                 new BasicThreadFactory.Builder()
                                                                                                         .namingPattern(
                                                                                                                 "yht-thread-pool-%d")
                                                                                                         .daemon(true)
                                                                                                         .build());
                executorService.scheduleAtFixedRate(() ->
                                                    {
                                                        time--;
                                                        if (time < 0)
                                                        {
                                                            time = 0;
                                                            executorService.shutdownNow();
                                                        }
                                                        else
                                                        {
                                                            handler.sendEmptyMessage(0);
                                                        }
                                                    }, 0, 1, TimeUnit.SECONDS);
                break;
            case LOGIN_AND_REGISTER:
                //保存登录成功数据
                loginSuccessBean = response.getData();
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                //保存登录账号
                sharePreferenceUtil.putString(CommonData.KEY_USER_PHONE, phone);
                EMClient.getInstance()
                        .login(loginSuccessBean.getDoctorId(), "111111", new EMCallBack()
                        {//回调
                            @Override
                            public void onSuccess()
                            {
                                runOnUiThread(() ->
                                              {
                                                  EMClient.getInstance()
                                                          .chatManager()
                                                          .loadAllConversations();
                                                  EMClient.getInstance()
                                                          .groupManager()
                                                          .loadAllGroups();
                                                  Log.d("main", "登录聊天服务器成功！");
                                                  jumpTopage();
                                              });
                            }

                            @Override
                            public void onProgress(int progress, String status)
                            {
                            }

                            @Override
                            public void onError(int code, String message)
                            {
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
        int checked = loginSuccessBean.getChecked();
        switch (checked)
        {
            case NONE:
            case VERIFYING:
            case VERIFY_FAILD:
                startActivity(new Intent(this, AuthDocStatuActivity.class));
                break;
            case VERIFY_SUCCESS:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                startActivity(new Intent(this, MainActivity.class));
                finish();
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
            case GET_VERIFY_CODE:
                ToastUtil.toast(this, response.getMsg());
                break;
            case LOGIN_AND_REGISTER:
                Toast.makeText(this, response.getMsg(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
