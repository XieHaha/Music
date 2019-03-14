package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.DocAuthStatu;

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.StatusBarUtil;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * Created by dundun on 19/2/19.
 */
public class AuthDocStatuActivity extends BaseActivity implements DocAuthStatu, CommonData, CustomAdapt {
    @BindView(R.id.public_title_bar_back)
    ImageView ivBack;
    @BindView(R.id.act_auth_doc_statu_verifying)
    TextView tvVerifying;
    @BindView(R.id.act_auth_doc_statu_img)
    ImageView ivImg;
    @BindView(R.id.act_auth_doc_statu_name)
    TextView tvName;
    @BindView(R.id.act_auth_doc_statu_hint)
    TextView tvHint;
    @BindView(R.id.act_auth_doc_statu_next)
    TextView tvNext;
    @BindView(R.id.act_auth_doc_statu_again)
    TextView tvAgain;

    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 认证
     */
    public static final int REQUEST_CODE_AUTH = 100;
    /**
     * 重新认证
     */
    public static final int REQUEST_CODE_AUTH_AGAIN = 200;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED:
                    //认证失败  更新本地数据
                    loginSuccessBean.setChecked(2);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initPageData();
                    break;
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS:
                    //认证成功  更新本地数据
                    loginSuccessBean.setChecked(6);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initPageData();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 医生认证状态
     */
    private IChange<Integer> doctorAuthStatusChangeListener = data ->
    {
        handler.sendEmptyMessage(data);
    };

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_auth_doc_statu;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        StatusBarUtil.StatusBarLightMode(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        tvName.setText(
                String.format(getString(R.string.txt_doc_auth_name), loginSuccessBean.getName()));
        initPageData();
    }

    @Override
    public void initListener() {
        super.initListener();
        ivBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        tvAgain.setOnClickListener(this);
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(
                doctorAuthStatusChangeListener, RegisterType.REGISTER);
    }

    private void initPageData() {
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        int checked = loginSuccessBean.getChecked();
        switch (checked) {
            case NONE:
                tvVerifying.setVisibility(View.INVISIBLE);
                tvHint.setText(R.string.txt_doc_auth_hint);
                tvNext.setVisibility(View.VISIBLE);
                tvAgain.setVisibility(View.GONE);
                ivImg.setImageResource(R.mipmap.icon_doc_auth);
                break;
            case VERIFYING:
                tvVerifying.setVisibility(View.VISIBLE);
                tvVerifying.setText(R.string.txt_doc_auth_verifying);
                tvHint.setText(R.string.txt_doc_auth_hint1);
                tvNext.setVisibility(View.GONE);
                tvAgain.setVisibility(View.VISIBLE);
                ivImg.setImageResource(R.mipmap.icon_doc_auth_wait);
                break;
            case VERIFY_FAILD:
                tvVerifying.setText(R.string.txt_doc_auth_verify_faild);
                tvHint.setText(R.string.txt_doc_auth_hint2);
                ivImg.setImageResource(R.mipmap.icon_doc_auth_faild);
                tvNext.setVisibility(View.VISIBLE);
                tvNext.setText(R.string.txt_doc_auth_again);
                tvAgain.setVisibility(View.GONE);
                break;
            case VERIFY_SUCCESS:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.act_auth_doc_statu_next:
                intent = new Intent(this, AuthDocActivity.class);
                //认证失败
                if (VERIFY_FAILD == loginSuccessBean.getChecked()) {
                    intent.putExtra("again", true);
                }
                startActivityForResult(intent, REQUEST_CODE_AUTH);
                break;
            case R.id.act_auth_doc_statu_again:
                intent = new Intent(this, AuthDocActivity.class);
                intent.putExtra("again", true);
                startActivityForResult(intent, REQUEST_CODE_AUTH_AGAIN);
                break;
            case R.id.public_title_bar_back:
                finishPage();
                break;
            default:
                break;
        }
    }

    /**
     * 判断 登录
     */
    private void finishPage() {
        //清除登录信息
        YihtApplication.getInstance().clearLoginSuccessBean();
        //退出环信
        EMClient.getInstance().logout(true);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_AUTH:
                initPageData();
                break;
            case REQUEST_CODE_AUTH_AGAIN:
                initPageData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(
                doctorAuthStatusChangeListener, RegisterType.UNREGISTER);
    }


    //===================================屏幕适配
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 667;
    }

    //===================================屏幕适配
}
