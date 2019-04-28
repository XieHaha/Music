package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.data.BaseData;
import com.zyc.doctor.data.bean.VersionBean;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.ui.dialog.listener.OnEnterClickListener;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.version.presenter.VersionPresenter;
import com.zyc.doctor.version.view.VersionUpdateDialog;

import butterknife.BindView;

/**
 * @author dundun
 */
public class SettingActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener {
    private static final String TAG = "SettingActivity";
    @BindView(R.id.act_setting_version)
    TextView tvVersion;
    @BindView(R.id.act_setting_version_remind)
    TextView tvVersionRemind;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private VersionUpdateDialog versionUpdateDialog;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this);
        mVersionPresenter.setVersionViewListener(this);
        if (YihtApplication.getInstance().isVersionRemind()) {
            tvVersionRemind.setVisibility(View.VISIBLE);
        }
        else {
            tvVersionRemind.setVisibility(View.GONE);
        }
        getAppVersionCode();
    }

    @Override
    public void initListener() {
        super.initListener();
        findViewById(R.id.act_setting_vesion_layout).setOnClickListener(this);
        findViewById(R.id.act_setting_about_layout).setOnClickListener(this);
        findViewById(R.id.act_setting_exit_layout).setOnClickListener(this);
    }

    private void getAppVersionCode() {
        try {
            String name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";
            if (!TextUtils.isEmpty(name)) {
                tvVersion.setText("V" + name);
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_setting_vesion_layout:
                mVersionPresenter.init();
                break;
            case R.id.act_setting_exit_layout:
                HintDialog hintDialog = new HintDialog(this);
                hintDialog.setContentString(getString(R.string.txt_exit_hint));
                hintDialog.setOnEnterClickListener(new OnEnterClickListener() {
                    @Override
                    public void onEnter() {
                        Intent intent = new Intent(BaseData.BASE_LOGINOUT_ACTION);
                        intent.setPackage(getPackageName());
                        sendBroadcast(intent);
                    }
                });
                hintDialog.show();
                break;
            case R.id.act_setting_about_layout:
                startActivity(new Intent(this, AboutOurActivity.class));
                break;
            default:
                break;
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(VersionBean version, int mode, boolean isDownLoading) {
        if (mode == -1) {
            ToastUtil.toast(this, "当前已是最新版本");
            return;
        }
        versionUpdateDialog = new VersionUpdateDialog(this);
        versionUpdateDialog.setCancelable(false);
        versionUpdateDialog.setUpdateMode(mode).
                setIsDownNewAPK(isDownLoading).setContent(version.getUpdateDescription());
        versionUpdateDialog.setOnEnterClickListener(this);
        versionUpdateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (versionUpdateDialog != null && versionUpdateDialog.isShowing()) {
            versionUpdateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, R.string.txt_download_hint);
    }
}
