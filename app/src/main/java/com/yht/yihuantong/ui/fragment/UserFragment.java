package com.yht.yihuantong.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.ui.activity.EditInfoActivity;
import com.yht.yihuantong.ui.activity.LoginActivity;
import com.yht.yihuantong.ui.dialog.SimpleDialog;

import custom.frame.bean.LoginSuccessBean;
import custom.frame.ui.activity.AppManager;
import custom.frame.ui.fragment.BaseFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends BaseFragment {
    private CircleImageView headImg;
    private TextView tvName, tvHospital, tvType, tvTitle, tvIntroduce;

    private LoginSuccessBean loginSuccessBean;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
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
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        initPageData();
    }

    /**
     * 初始化界面数据
     */
    private void initPageData() {
        if (loginSuccessBean != null) {
            Glide.with(getContext())
                    .load("http://39.107.249.194:8080/DPView/f/download/avatar/20180411/1523433761828870439.jpg")
                    .into(headImg);
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
            default:
                break;
        }
    }
}
