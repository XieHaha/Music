package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.qrcode.BarCodeImageView;
import com.yht.yihuantong.qrcode.DialogPersonalBarCode;
import com.yht.yihuantong.tools.GlideHelper;
import com.yht.yihuantong.ui.activity.AuthDocActivity;
import com.yht.yihuantong.ui.activity.EditInfoActivity;
import com.yht.yihuantong.ui.activity.LoginActivity;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.version.presenter.VersionPresenter;
import com.yht.yihuantong.version.view.VersionUpdateDialog;

import org.litepal.crud.DataSupport;

import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.LoginSuccessBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.Version;
import custom.frame.ui.activity.AppManager;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.scrollview.CustomListenScrollView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment
        implements VersionPresenter.VersionViewListener, VersionUpdateDialog.OnEnterClickListener,
                   CustomListenScrollView.OnScrollChangeListener
{
    private CircleImageView headImg, authImg;
    private LinearLayout llTitleLayout;
    private RelativeLayout rlAuthLayout;
    private CustomListenScrollView scrollView;
    private TextView tvName, tvHospital, tvType, tvTitle, tvIntroduce;
    private TextView tvAuth, tvAuthStatus;
    private ImageView ivEditInfo;
    private LoginSuccessBean loginSuccessBean;
    private boolean isAuth;
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
        //获取状态栏高度，填充
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                              getStateBarHeight(getActivity())));//填充状态栏
        //        //状态栏透明
        //        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ivEditInfo = view.findViewById(R.id.public_title_bar_back);
        ivEditInfo.setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_exit).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_version).setOnClickListener(this);
        rlAuthLayout = view.findViewById(R.id.fragment_my_auth_layout);
        scrollView = view.findViewById(R.id.fragment_my_scrollview);
        llTitleLayout = view.findViewById(R.id.fragment_my_title_layout);
        headImg = view.findViewById(R.id.fragmrnt_user_info_headimg);
        authImg = view.findViewById(R.id.fragmrnt_user_info_auth);
        tvName = view.findViewById(R.id.fragmrnt_user_info_name);
        tvHospital = view.findViewById(R.id.fragmrnt_user_info_hospital);
        tvType = view.findViewById(R.id.fragmrnt_user_info_type);
        tvTitle = view.findViewById(R.id.fragmrnt_user_info_title);
        tvIntroduce = view.findViewById(R.id.fragmrnt_user_info_introduce);
        tvAuth = view.findViewById(R.id.fragment_my_auth);
        tvAuthStatus = view.findViewById(R.id.fragment_my_auth_status);
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

    @Override
    public void initListener()
    {
        super.initListener();
        scrollView.setOnScrollChangeListener(this);
        rlAuthLayout.setOnClickListener(this);
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
            int status = loginSuccessBean.getChecked();
            switch (status)
            {
                case 0://未认证
                    isAuth = false;
                    tvAuth.setText("去认证");
                    tvAuthStatus.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.app_auth_faild));
                    Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                    ivEditInfo.setVisibility(View.VISIBLE);
                    break;
                case 1://审核中
                    isAuth = false;
                    tvAuthStatus.setText("审核中");
                    tvAuth.setText("查看");
                    tvAuthStatus.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.app_auth_faild));
                    Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                    ivEditInfo.setVisibility(View.VISIBLE);
                    break;
                case 2://审核未通过
                    isAuth = false;
                    tvAuthStatus.setText("审核未通过");
                    tvAuth.setText("查看");
                    tvAuthStatus.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.app_auth_faild));
                    Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                    ivEditInfo.setVisibility(View.VISIBLE);
                    break;
                case 6://审核已通过
                    isAuth = false;
                    tvAuthStatus.setText("已认证");
                    tvAuth.setText("查看");
                    tvAuthStatus.setTextColor(
                            ContextCompat.getColor(getContext(), R.color.app_auth_success));
                    Glide.with(this).load(R.mipmap.icon_certified).into(authImg);
                    ivEditInfo.setVisibility(View.GONE);
                    break;
            }
            tvName.setText(loginSuccessBean.getName());
            tvHospital.setText(loginSuccessBean.getHospital());
            tvTitle.setText(loginSuccessBean.getTitle());
            tvType.setText(loginSuccessBean.getDepartment());
            tvIntroduce.setText(loginSuccessBean.getDoctorDescription());
        }
    }

    /**
     * 监听scrollview滑动情况
     *
     * @param scrollView
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    public void onScrollChanged(CustomListenScrollView scrollView, int l, int t, int oldl, int oldt)
    {
        int scrollHeight = (scrollView.getChildAt(0).getHeight() - scrollView.getMeasuredHeight());
        int alpha;
        if (t < scrollHeight && t > 0)
        {
            alpha = t * 255 / scrollHeight;
            llTitleLayout.setBackgroundColor(Color.argb(alpha, 231, 50, 120));
        }
        if (t >= scrollHeight)
        {
            llTitleLayout.setBackgroundColor(Color.argb(255, 231, 50, 120));
        }
        if (t <= 0)
        {
            llTitleLayout.setBackgroundColor(Color.argb(0, 231, 50, 120));
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
                new SimpleDialog(getActivity(), "确定退出?", (dialog, which) ->
                {
                    //清除登录信息
                    YihtApplication.getInstance().clearLoginSuccessBean();
                    //清除数据库数据
                    DataSupport.deleteAll(PatientBean.class);
                    DataSupport.deleteAll(CooperateDocBean.class);
                    //退出环信
                    EMClient.getInstance().logout(true);
                    dialog.dismiss();
                    AppManager.getInstance().finishAllActivity();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    System.exit(0);
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
            case R.id.fragment_my_auth_layout:
                if (isAuth) { return; }
                Intent intent1 = new Intent(getContext(), AuthDocActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(Version version, int mode, boolean isDownLoading)
    {
        if (mode == -1)
        {
            ToastUtil.toast(getContext(), "当前已是最新版本");
            return;
        }
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
