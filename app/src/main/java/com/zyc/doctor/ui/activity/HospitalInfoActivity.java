package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.ui.base.activity.BaseActivity;

import butterknife.BindView;

/**
 * @author dundun
 * @date 18/10/9
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
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            hospitalBean = (HospitalBean)getIntent().getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
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
