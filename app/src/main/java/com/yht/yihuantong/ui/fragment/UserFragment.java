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

import custom.frame.bean.LoginSuccessBean;
import custom.frame.ui.activity.AppManager;
import custom.frame.ui.fragment.BaseFragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 */
public class UserFragment extends BaseFragment {
    private CircleImageView headImg;
    private TextView tvName, tvHospital, tvType, tvTitle, tvIntroduce;

    private LoginSuccessBean loginSuccessBean;

    private BarCodeImageView barCodeImageView;

    private String headImgUrl;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPageData();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
//        //状态栏透明
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        view.findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_exit).setOnClickListener(this);

        headImg = view.findViewById(R.id.fragmrnt_user_info_headimg);
        tvName = view.findViewById(R.id.fragmrnt_user_info_name);
        tvHospital = view.findViewById(R.id.fragmrnt_user_info_hospital);
        tvType = view.findViewById(R.id.fragmrnt_user_info_type);
        tvTitle = view.findViewById(R.id.fragmrnt_user_info_title);
        tvIntroduce = view.findViewById(R.id.fragmrnt_user_info_introduce);
        view.findViewById(R.id.fragmrnt_user_info_qrcode_layout).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData() {
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        if (loginSuccessBean != null) {
            barCodeImageView = new BarCodeImageView(getContext(),loginSuccessBean.getDoctorId());

            if (!TextUtils.isEmpty(loginSuccessBean.getPortraitUrl())) {
                headImgUrl = loginSuccessBean.getPortraitUrl();
            } else if (!TextUtils.isEmpty(YihtApplication.getInstance().getHeadImgUrl())) {
                headImgUrl = YihtApplication.getInstance().getHeadImgUrl();
            }

            if (!TextUtils.isEmpty(headImgUrl)) {
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                Intent intent = new Intent(getContext(), EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_info_exit:
                new SimpleDialog(getActivity(), "确定退出?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //清除登录信息
                        YihtApplication.getInstance().clearLoginSuccessBean();
                        //退出环信
                        EMClient.getInstance().logout(true);
                        dialog.dismiss();
                        AppManager.getInstance().finishAllActivity();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        System.exit(0);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.fragmrnt_user_info_qrcode_layout:
                DialogPersonalBarCode dialogPersonalBarCode = new DialogPersonalBarCode(getActivity());
                dialogPersonalBarCode.setQRImageViewSrc(barCodeImageView);
                dialogPersonalBarCode.show();
                break;
            default:
                break;
        }
    }
}