package com.yht.yihuantong.ui.activity;

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
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.OrderStatus;
import com.yht.yihuantong.utils.AllUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.HospitalBean;
import custom.frame.bean.RegistrationBean;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/8/10.
 */
public class RegistrationDetailActivity extends BaseActivity implements OrderStatus
{
    private TextView tvTitle, tvNext;
    private TextView tvGoodsName, tvGoodsPrice, tvGoodsType, tvGoodsInfo;
    private TextView tvContact, tvContactPhone, tvUseful, tvAttention;
    private ImageView ivHospitalImg;
    private TextView tvHospitalName, tvHospitalAddress, tvHospitalGrade;
    private TextView tvPatientName, tvPatientSex, tvPatientAge, tvDes, tvDoctorName, tvDoctorHospital;
    private TextView tvReserveTime,tvReserveTimeTitle, tvReserveTips;
    private LinearLayout llReserveLayout;
    private RelativeLayout rlHospitalLayout;
    private RegistrationBean registrationBean;
    private String registrationId;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    getDetailById();
                    break;
            }
        }
    };
    /**
     * 推送回调监听  转诊申请
     */
    private IChange<String> orderStatusChangeListener = data ->
    {
        try
        {
            registrationId = data;
            handler.sendEmptyMessage(0);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    };

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_registration_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        tvTitle = findViewById(R.id.public_title_bar_title);
        tvNext = findViewById(R.id.act_registration_next);
        tvGoodsName = findViewById(R.id.act_registration_goods_name);
        tvGoodsType = findViewById(R.id.act_registration_goods_type);
        tvGoodsInfo = findViewById(R.id.act_registration_goods_info);
        tvGoodsPrice = findViewById(R.id.act_registration_goods_price);
        tvContact = findViewById(R.id.act_service_pack_contact);
        tvContactPhone = findViewById(R.id.act_service_pack_contact_phone);
        tvUseful = findViewById(R.id.act_service_pack_useful);
        tvAttention = findViewById(R.id.act_service_pack_attention);
        tvPatientName = findViewById(R.id.act_registration_detail_patient_name);
        tvPatientAge = findViewById(R.id.act_registration_detail_patient_age);
        tvPatientSex = findViewById(R.id.act_registration_detail_patient_sex);
        tvDes = findViewById(R.id.act_registration_detail_des);
        tvDoctorName = findViewById(R.id.act_registration_detail_doctor_name);
        tvDoctorHospital = findViewById(R.id.act_registration_detail_doctor_hospital);
        ivHospitalImg = findViewById(R.id.act_service_pack_hint_hospital_img);
        tvHospitalName = findViewById(R.id.act_service_pack_hint_hospital_name);
        tvHospitalAddress = findViewById(R.id.act_service_pack_hint_hospital_address);
        tvHospitalGrade = findViewById(R.id.act_service_pack_hint_hospital_grade);
        tvReserveTime = findViewById(R.id.act_registration_detail_time);
        tvReserveTimeTitle = findViewById(R.id.act_registration_detail_time_title);
        tvReserveTips = findViewById(R.id.act_registration_detail_hint);
        rlHospitalLayout = findViewById(R.id.act_service_pack_hint_hospital_layout);
        llReserveLayout = findViewById(R.id.act_registration_detail_time_layout);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        if (getIntent() != null)
        {
            registrationBean = (RegistrationBean)getIntent().getSerializableExtra(
                    CommonData.KEY_REGISTRATION_BEAN);
            registrationId = getIntent().getStringExtra(CommonData.KEY_REGISTRATION_ID);
        }
        tvTitle.setText("订单详情");
        iNotifyChangeListenerServer = ApiManager.getInstance()
                                                .getServer(INotifyChangeListenerServer.class);
        if (registrationBean == null)
        {
            getDetailById();
        }
        else
        {
            initPageData();
        }
    }

    @Override
    public void initListener()
    {
        rlHospitalLayout.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        //注册订单状态监听
        iNotifyChangeListenerServer.registerOrderStatusChangeListener(orderStatusChangeListener,
                                                                          RegisterType.REGISTER);
    }

    private void initPageData()
    {
        //医院信息
        tvHospitalName.setText(registrationBean.getHospitalName());
        tvHospitalAddress.setText(registrationBean.getCityName());
        tvHospitalGrade.setText(registrationBean.getHospitalLevel());
        Glide.with(this)
             .load(registrationBean.getHospitalLogo())
             .apply(GlideHelper.getOptionsHospitalPic())
             .into(ivHospitalImg);
        //商品信息
        tvGoodsName.setText(registrationBean.getProductName());
        tvGoodsType.setText(registrationBean.getProductTypeName());
        tvGoodsInfo.setText(registrationBean.getProductDescription());
        tvGoodsPrice.setText(
                registrationBean.getProductPrice() + registrationBean.getProductPriceUnit());
        //患者信息
        tvPatientName.setText(registrationBean.getPatientName());
        tvPatientSex.setText(registrationBean.getPatientSex());
        tvPatientAge.setText(
                AllUtils.formatDateByAge(registrationBean.getPatientBirthDate()) + "岁");
        tvDes.setText(registrationBean.getDiagnosisInfo());
        tvDoctorName.setText(registrationBean.getDoctorName());
        tvDoctorHospital.setText(registrationBean.getHospitalName());
        //注意事项
        tvContact.setText(registrationBean.getProductContactName());
        tvContactPhone.setText(registrationBean.getProductContactPhone());
        tvUseful.setText(registrationBean.getProductAccessChannel());
        tvAttention.setText(registrationBean.getProductOtherInfo());
        //预约信息
        if (TextUtils.isEmpty(registrationBean.getHospitalReserveTips()))
        {
            llReserveLayout.setVisibility(View.GONE);
        }
        else
        {
            llReserveLayout.setVisibility(View.VISIBLE);
            tvReserveTime.setText(AllUtils.formatDate(registrationBean.getHospitalReserveTime(),
                                                      AllUtils.YYYY_MM_DD_HH_MM));
            tvReserveTips.setText(registrationBean.getHospitalReserveTips());
        }
        switch (registrationBean.getOrderState())
        {
            case STATUS_SUBSCRIBE_NONE:
                break;
            case STATUS_SUBSCRIBE:
                break;
            case STATUS_COMPLETE:
                tvReserveTimeTitle.setText(R.string.txt_transfer_patient_reverse_time_title);
                break;
            case STATUS_SEND_REPORT:
                tvReserveTimeTitle.setText(R.string.txt_transfer_patient_reverse_time_title);
                tvNext.setText("查看报告");
                tvNext.setVisibility(View.VISIBLE);
                break;
            case STATUS_REFUSE:
                break;
        }
    }

    /**
     * 获取病例详情
     */
    private void getDetailById()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/order/single", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("fieldId", registrationId);
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>()
        {
            @Override
            public void onStart(int what)
            {
            }

            @Override
            public void onSucceed(int what, Response<String> response)
            {
                String s = response.get();
                try
                {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, RegistrationBean.class);
                    if (baseResponse != null && baseResponse.getCode() == 200)
                    {
                        registrationBean = baseResponse.getData();
                        initPageData();
                    }
                    else
                    {
                        ToastUtil.toast(RegistrationDetailActivity.this, baseResponse.getMsg());
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response)
            {
                ToastUtil.toast(RegistrationDetailActivity.this,
                                response.getException().getMessage());
            }

            @Override
            public void onFinish(int what)
            {
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
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
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //注销订单状态监听
        iNotifyChangeListenerServer.registerOrderStatusChangeListener(orderStatusChangeListener,
                                                                          RegisterType.UNREGISTER);
    }
}
