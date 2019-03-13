package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.frame.bean.HospitalBean;
import custom.frame.ui.activity.BaseActivity;

/**
 * Created by dundun on 18/10/9.
 */
public class HospitalInfoActivity extends BaseActivity {
    @BindView(R.id.act_hospital_info_name)
    TextView tvHospitalName;
    @BindView(R.id.act_hospital_info_city)
    TextView tvHospitalCity;
    @BindView(R.id.act_hospital_info_grade)
    TextView tvHospitalGrade;
    @BindView(R.id.act_hospital_info_address)
    TextView tvHospitalAddress;
    @BindView(R.id.act_hospital_info_phone)
    TextView tvHospitalPhone;
    @BindView(R.id.act_hospital_info_detail)
    TextView tvHospitalInfo;
    private HospitalBean hospitalBean;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_hospital_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("医院信息");
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            hospitalBean = (HospitalBean) getIntent().getSerializableExtra(
                    CommonData.KEY_HOSPITAL_BEAN);
        }
        initPage();
    }

    private void initPage() {
        if (hospitalBean != null) {
            tvHospitalName.setText(hospitalBean.getHospitalName());
            tvHospitalCity.setText(hospitalBean.getCityName());
            tvHospitalGrade.setText(hospitalBean.getHospitalLevel());
            tvHospitalAddress.setText(hospitalBean.getAddress());
            tvHospitalPhone.setText(hospitalBean.getHospitalPhone());
            tvHospitalInfo.setText(hospitalBean.getHospitalDescription());
        }
    }
}
