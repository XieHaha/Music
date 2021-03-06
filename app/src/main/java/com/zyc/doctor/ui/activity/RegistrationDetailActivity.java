package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.api.ApiManager;
import com.zyc.doctor.api.notify.IChange;
import com.zyc.doctor.api.notify.INotifyChangeListenerServer;
import com.zyc.doctor.api.notify.RegisterType;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.OrderStatus;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.data.bean.RegistrationBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.BaseUtils;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.ToastUtil;

import butterknife.BindView;

/**
 * @author dundun
 */
public class RegistrationDetailActivity extends BaseActivity implements OrderStatus {
    private static final String TAG = "RegistrationDetailActiv";
    @BindView(R.id.act_service_pack_hint_hospital_img)
    ImageView ivHospitalImg;
    @BindView(R.id.act_service_pack_hint_hospital_name)
    TextView tvHospitalName;
    @BindView(R.id.act_service_pack_hint_hospital_address)
    TextView tvHospitalAddress;
    @BindView(R.id.act_service_pack_hint_hospital_grade)
    TextView tvHospitalGrade;
    @BindView(R.id.act_service_pack_hint_hospital_layout)
    RelativeLayout rlHospitalLayout;
    @BindView(R.id.act_registration_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.act_registration_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.act_registration_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.act_registration_goods_info)
    TextView tvGoodsInfo;
    @BindView(R.id.act_registration_detail_patient_name)
    TextView tvPatientName;
    @BindView(R.id.act_registration_detail_patient_sex)
    TextView tvPatientSex;
    @BindView(R.id.act_registration_detail_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.act_registration_detail_des)
    TextView tvDes;
    @BindView(R.id.act_registration_detail_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.act_registration_detail_doctor_hospital)
    TextView tvDoctorHospital;
    @BindView(R.id.act_service_pack_contact)
    TextView tvContact;
    @BindView(R.id.act_service_pack_contact_phone)
    TextView tvContactPhone;
    @BindView(R.id.act_service_pack_useful)
    TextView tvUseful;
    @BindView(R.id.act_service_pack_attention)
    TextView tvAttention;
    @BindView(R.id.act_registration_detail_time_title)
    TextView tvReserveTimeTitle;
    @BindView(R.id.act_registration_detail_time)
    TextView tvReserveTime;
    @BindView(R.id.act_registration_detail_hint)
    TextView tvReserveTips;
    @BindView(R.id.act_registration_detail_time_layout)
    LinearLayout llReserveLayout;
    @BindView(R.id.act_registration_next)
    TextView tvNext;
    private RegistrationBean registrationBean;
    private String registrationId;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getDetailById();
                    break;
            }
        }
    };
    /**
     * ??????????????????  ????????????
     */
    private IChange<String> orderStatusChangeListener = data -> {
        try {
            registrationId = data;
            handler.sendEmptyMessage(0);
        }
        catch (NumberFormatException e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    };

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_registration_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        if (getIntent() != null) {
            registrationBean = (RegistrationBean)getIntent().getSerializableExtra(CommonData.KEY_REGISTRATION_BEAN);
            registrationId = getIntent().getStringExtra(CommonData.KEY_REGISTRATION_ID);
        }
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        if (registrationBean == null) {
            getDetailById();
        }
        else {
            initPageData();
        }
    }

    @Override
    public void initListener() {
        rlHospitalLayout.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        //????????????????????????
        iNotifyChangeListenerServer.registerOrderStatusChangeListener(orderStatusChangeListener, RegisterType.REGISTER);
    }

    private void initPageData() {
        //????????????
        tvHospitalName.setText(registrationBean.getHospitalName());
        tvHospitalAddress.setText(registrationBean.getCityName());
        tvHospitalGrade.setText(registrationBean.getHospitalLevel());
        Glide.with(this)
             .load(registrationBean.getHospitalLogo())
             .apply(GlideHelper.getOptionsHospitalPic())
             .into(ivHospitalImg);
        //????????????
        tvGoodsName.setText(registrationBean.getProductName());
        tvGoodsType.setText(registrationBean.getProductTypeName());
        tvGoodsInfo.setText(registrationBean.getProductDescription());
        tvGoodsPrice.setText(registrationBean.getProductPrice() + registrationBean.getProductPriceUnit());
        //????????????
        tvPatientName.setText(registrationBean.getPatientName());
        tvPatientSex.setText(registrationBean.getPatientSex());
        tvPatientAge.setText(BaseUtils.getAge(registrationBean.getPatientBirthDate()) + "???");
        tvDes.setText(registrationBean.getDiagnosisInfo());
        tvDoctorName.setText(registrationBean.getDoctorName());
        tvDoctorHospital.setText(registrationBean.getHospitalName());
        //????????????
        tvContact.setText(registrationBean.getProductContactName());
        tvContactPhone.setText(registrationBean.getProductContactPhone());
        tvUseful.setText(registrationBean.getProductAccessChannel());
        tvAttention.setText(registrationBean.getProductOtherInfo());
        //????????????
        if (TextUtils.isEmpty(registrationBean.getHospitalReserveTips())) {
            llReserveLayout.setVisibility(View.GONE);
        }
        else {
            llReserveLayout.setVisibility(View.VISIBLE);
            tvReserveTime.setText(
                    BaseUtils.formatDate(registrationBean.getHospitalReserveTime(), BaseUtils.YYYY_MM_DD_HH_MM));
            tvReserveTips.setText(registrationBean.getHospitalReserveTips());
        }
        switch (registrationBean.getOrderState()) {
            case STATUS_SUBSCRIBE_NONE:
                break;
            case STATUS_SUBSCRIBE:
                break;
            case STATUS_COMPLETE:
                tvReserveTimeTitle.setText(R.string.txt_transfer_patient_reverse_time_title);
                break;
            case STATUS_SEND_REPORT:
                tvReserveTimeTitle.setText(R.string.txt_transfer_patient_reverse_time_title);
                tvNext.setText("????????????");
                tvNext.setVisibility(View.VISIBLE);
                break;
            case STATUS_REFUSE:
                break;
            default:
                break;
        }
    }

    /**
     * ??????????????????
     */
    private void getDetailById() {
        RequestUtils.getDetailById(this, registrationId, this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.act_service_pack_hint_hospital_layout:
                HospitalBean hospitalBean = new HospitalBean();
                hospitalBean.setHospitalName(registrationBean.getHospitalName());
                hospitalBean.setCityName(registrationBean.getCityName());
                hospitalBean.setHospitalLevel(registrationBean.getHospitalLevel());
                hospitalBean.setAddress(registrationBean.getHospitalAddress());
                hospitalBean.setHospitalPhone(registrationBean.getHospitalPhone());
                hospitalBean.setHospitalDescription(registrationBean.getHospitalDescription());
                intent = new Intent(this, HospitalInfoActivity.class);
                intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, hospitalBean);
                startActivity(intent);
                break;
            case R.id.act_registration_next:
                intent = new Intent(this, FileListActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, registrationBean.getOrderReportAddress());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_CASE_DETAIL_BY_ID:
                if (response.getData() != null) {
                    registrationBean = (RegistrationBean)response.getData();
                    initPageData();
                }
                else {
                    ToastUtil.toast(RegistrationDetailActivity.this, response.getMsg());
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //????????????????????????
        iNotifyChangeListenerServer.registerOrderStatusChangeListener(orderStatusChangeListener,
                                                                      RegisterType.UNREGISTER);
    }
}
