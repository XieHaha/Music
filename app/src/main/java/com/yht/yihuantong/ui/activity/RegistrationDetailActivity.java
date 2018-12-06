package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView ivTitleMore;
    private TextView tvTitle, tvNext;
    private TextView tvGoodsName, tvGoodsPrice, tvGoodsType, tvGoodsInfo;
    private TextView tvContact, tvContactPhone, tvUseful, tvAttention;
    private ImageView ivHospitalImg;
    private TextView tvHospitalName, tvHospitalAddress, tvHospitalGrade;
    private TextView tvPatientName, tvPatientSex, tvPatientAge, tvDes, tvDoctorName, tvDoctorHospital;
    private RelativeLayout rlHospitalLayout;
    private RegistrationBean registrationBean;
    private String registrationId;

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
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        ivTitleMore = (ImageView)findViewById(R.id.public_title_bar_more_two);
        tvNext = (TextView)findViewById(R.id.act_registration_next);
        tvGoodsName = (TextView)findViewById(R.id.act_registration_goods_name);
        tvGoodsType = (TextView)findViewById(R.id.act_registration_goods_type);
        tvGoodsInfo = (TextView)findViewById(R.id.act_registration_goods_info);
        tvGoodsPrice = (TextView)findViewById(R.id.act_registration_goods_price);
        tvContact = (TextView)findViewById(R.id.act_service_pack_contact);
        tvContactPhone = (TextView)findViewById(R.id.act_service_pack_contact_phone);
        tvUseful = (TextView)findViewById(R.id.act_service_pack_useful);
        tvAttention = (TextView)findViewById(R.id.act_service_pack_attention);
        tvPatientName = (TextView)findViewById(R.id.act_registration_detail_patient_name);
        tvPatientAge = (TextView)findViewById(R.id.act_registration_detail_patient_age);
        tvPatientSex = (TextView)findViewById(R.id.act_registration_detail_patient_sex);
        tvDes = (TextView)findViewById(R.id.act_registration_detail_des);
        tvDoctorName = (TextView)findViewById(R.id.act_registration_detail_doctor_name);
        tvDoctorHospital = (TextView)findViewById(R.id.act_registration_detail_doctor_hospital);
        ivHospitalImg = (ImageView)findViewById(R.id.act_service_pack_hint_hospital_img);
        tvHospitalName = (TextView)findViewById(R.id.act_service_pack_hint_hospital_name);
        tvHospitalAddress = (TextView)findViewById(R.id.act_service_pack_hint_hospital_address);
        tvHospitalGrade = (TextView)findViewById(R.id.act_service_pack_hint_hospital_grade);
        rlHospitalLayout = (RelativeLayout)findViewById(R.id.act_service_pack_hint_hospital_layout);
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
        ivTitleMore.setOnClickListener(this);
        rlHospitalLayout.setOnClickListener(this);
        tvNext.setOnClickListener(this);
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
        switch (registrationBean.getOrderState())
        {
            case STATUS_SUBSCRIBE_NONE:
                tvNext.setVisibility(View.VISIBLE);
                ivTitleMore.setVisibility(View.VISIBLE);
                break;
            case STATUS_SUBSCRIBE:
                tvNext.setVisibility(View.GONE);
                ivTitleMore.setVisibility(View.GONE);
                break;
            case STATUS_COMPLETE:
                break;
            case STATUS_SEND_REPORT:
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
                Intent intent = new Intent(this, HospitalInfoActivity.class);
                intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, hospitalBean);
                startActivity(intent);
                break;
        }
    }
}
