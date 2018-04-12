package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.tools.GlideHelper;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息界面
 *
 * @author DUNDUN
 */
public class UserInfoActivity extends BaseActivity {
    private CircleImageView ivHeadImg;
    private TextView tvName, tvType, tvTitle, tvIntroduce, tvHospital;

    private CooperateDocBean cooperateDocBean;

    private String headImgUrl;
    private String doctorId;

    @Override
    public int getLayoutID() {
        return R.layout.act_user_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ivHeadImg = (CircleImageView) findViewById(R.id.act_user_info_headimg);
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.act_user_info_name);
        tvHospital = (TextView) findViewById(R.id.act_user_info_hospital);
        tvTitle = (TextView) findViewById(R.id.act_user_info_title);
        tvType = (TextView) findViewById(R.id.act_user_info_type);
        tvIntroduce = (TextView) findViewById(R.id.act_user_info_introduce);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            doctorId = getIntent().getStringExtra(CommonData.KEY_DOCTOR_ID);
        }
        getDocInfo();
    }

    /**
     * 初始化界面数据
     */
    private void initPageData() {
        if (cooperateDocBean != null) {
            headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            }
            tvName.setText(cooperateDocBean.getName());
            tvHospital.setText(cooperateDocBean.getHospital());
            tvTitle.setText(cooperateDocBean.getTitle());
            tvType.setText(cooperateDocBean.getDepartment());
            tvIntroduce.setText(cooperateDocBean.getDoctorDescription());
        }
    }

    /**
     * 获取个人信息
     */
    private void getDocInfo() {
        mIRequest.getDocInfo(doctorId, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                ToastUtil.toast(this, "上传成功!!");
                headImgUrl = response.getData();
                break;
            case GET_DOC_INFO:
                cooperateDocBean = response.getData();
                initPageData();
            default:
                break;
        }
    }
}
