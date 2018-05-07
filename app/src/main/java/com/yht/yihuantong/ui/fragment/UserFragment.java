package com.yht.yihuantong.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.qrcode.BarCodeImageView;
import com.yht.yihuantong.qrcode.DialogPersonalBarCode;
import com.yht.yihuantong.tools.GlideHelper;
import com.yht.yihuantong.ui.activity.EditInfoActivity;
import com.yht.yihuantong.ui.activity.LoginActivity;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.version.presenter.VersionPresenter;
import com.yht.yihuantong.version.view.VersionUpdateDialog;

import custom.frame.bean.LoginSuccessBean;
import custom.frame.bean.Version;
import custom.frame.ui.activity.AppManager;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment
        implements VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener
{
    private CircleImageView headImg;
    private TextView tvName, tvHospital, tvType, tvTitle, tvIntroduce;
    private LoginSuccessBean loginSuccessBean;
    /**
     * 二维码
     */
    private BarCodeImageView barCodeImageView;
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
    private String headImgUrl;

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_my;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        initPageData();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        //        //状态栏透明
        //        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        view.findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_exit).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_version).setOnClickListener(this);
        headImg = view.findViewById(R.id.fragmrnt_user_info_headimg);
        tvName = view.findViewById(R.id.fragmrnt_user_info_name);
        tvHospital = view.findViewById(R.id.fragmrnt_user_info_hospital);
        tvType = view.findViewById(R.id.fragmrnt_user_info_type);
        tvTitle = view.findViewById(R.id.fragmrnt_user_info_title);
        tvIntroduce = view.findViewById(R.id.fragmrnt_user_info_introduce);
        view.findViewById(R.id.fragmrnt_user_info_qrcode_layout).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(getContext(), mIRequest);
        mVersionPresenter.setVersionViewListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        if (loginSuccessBean != null)
        {
            barCodeImageView = new BarCodeImageView(getContext(), loginSuccessBean.getDoctorId());
            if (!TextUtils.isEmpty(loginSuccessBean.getPortraitUrl()))
            {
                headImgUrl = loginSuccessBean.getPortraitUrl();
            }
            else if (!TextUtils.isEmpty(YihtApplication.getInstance().getHeadImgUrl()))
            {
                headImgUrl = YihtApplication.getInstance().getHeadImgUrl();
            }
            if (!TextUtils.isEmpty(headImgUrl))
            {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(headImg);
            }
            tvName.setText(loginSuccessBean.getName());
            tvHospital.setText(loginSuccessBean.getHospital());
            tvTitle.setText(loginSuccessBean.getTitle());
            tvType.setText(loginSuccessBean.getDepartment());
            tvIntroduce.setText(loginSuccessBean.getDoctorDescription());
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.public_title_bar_back:
                Intent intent = new Intent(getContext(), EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_info_exit:
                new SimpleDialog(getActivity(), "确定退出?", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //清除登录信息
                        YihtApplication.getInstance().clearLoginSuccessBean();
                        //退出环信
                        EMClient.getInstance().logout(true);
                        dialog.dismiss();
                        AppManager.getInstance().finishAllActivity();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        System.exit(0);
                    }
                }, (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.fragmrnt_user_info_qrcode_layout:
                DialogPersonalBarCode dialogPersonalBarCode = new DialogPersonalBarCode(
                        getActivity());
                dialogPersonalBarCode.setQRImageViewSrc(barCodeImageView);
                dialogPersonalBarCode.show();
                break;
            case R.id.fragmrnt_user_info_version://版本更新
                mVersionPresenter.init();
                break;
            default:
                break;
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(Version version, int mode, boolean isDownLoading)
    {
        versionUpdateDialog = new VersionUpdateDialog(getContext());
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
        ToastUtil.toast(getContext(), "开始下载");
    }
}
