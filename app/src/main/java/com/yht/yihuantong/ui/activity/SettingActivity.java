package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.version.presenter.VersionPresenter;
import com.yht.yihuantong.version.view.VersionUpdateDialog;

import org.litepal.crud.DataSupport;

import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.Version;
import custom.frame.ui.activity.AppManager;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/9/2.
 */
public class SettingActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener
{
    private TextView tvVersion, tvVersionRemind;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private VersionUpdateDialog versionUpdateDialog;
    /**
     * 是否通过广播检查版本更新
     */
    private boolean versionUpdateChecked = false;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_setting;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("设置");
        tvVersion = (TextView)findViewById(R.id.act_setting_version);
        tvVersionRemind = (TextView)findViewById(R.id.act_setting_version_remind);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this, mIRequest);
        mVersionPresenter.setVersionViewListener(this);
        if (YihtApplication.getInstance().isVersionRemind())
        {
            tvVersionRemind.setVisibility(View.VISIBLE);
        }
        else
        {
            tvVersionRemind.setVisibility(View.GONE);
        }
        getAppVersionCode();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        findViewById(R.id.act_setting_vesion_layout).setOnClickListener(this);
        findViewById(R.id.act_setting_about_layout).setOnClickListener(this);
        findViewById(R.id.act_setting_exit_layout).setOnClickListener(this);
    }

    private void getAppVersionCode()
    {
        try
        {
            String name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";
            if (!TextUtils.isEmpty(name)) { tvVersion.setText("V" + name); }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.act_setting_vesion_layout:
                mVersionPresenter.init();
                break;
            case R.id.act_setting_exit_layout:
                new SimpleDialog(this, "确定退出?", (dialog, which) ->
                {
                    //清除登录信息
                    YihtApplication.getInstance().clearLoginSuccessBean();
                    //清除数据库数据
                    DataSupport.deleteAll(PatientBean.class);
                    DataSupport.deleteAll(CooperateDocBean.class);
                    //删除环信会话列表
                    //TODO
                    //退出环信
                    EMClient.getInstance().logout(true);
                    dialog.dismiss();
                    AppManager.getInstance().finishAllActivity();
                    startActivity(new Intent(this, LoginActivity.class));
                    System.exit(0);
                }, (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.act_setting_about_layout:
                startActivity(new Intent(this, AboutOurActivity.class));
                break;
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(Version version, int mode, boolean isDownLoading)
    {
        if (mode == -1)
        {
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
    public void updateLoading(long total, long current)
    {
        if (versionUpdateDialog != null && versionUpdateDialog.isShowing())
        {
            versionUpdateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError()
    {
        versionUpdateChecked = true;
    }

    @Override
    public void onEnter(boolean isMustUpdate)
    {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, "开始下载");
    }
}
