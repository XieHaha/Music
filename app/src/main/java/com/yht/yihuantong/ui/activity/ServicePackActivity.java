package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.yht.yihuantong.ui.adapter.RegistrationAdapter;
import com.yht.yihuantong.ui.adapter.RegistrationProductAdapter;
import com.yht.yihuantong.ui.adapter.RegistrationProductTypeAdapter;
import com.yht.yihuantong.ui.dialog.HintDialog;
import com.yht.yihuantong.ui.dialog.listener.OnEnterClickListener;
import com.yht.yihuantong.utils.AllUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.HospitalBean;
import custom.frame.bean.HospitalProductBean;
import custom.frame.bean.HospitalProductTypeBean;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * Created by dundun on 18/9/13.
 */
public class ServicePackActivity<T> extends BaseActivity
{
    private AutoLoadRecyclerView autoLoadRecyclerView, hospitalProductTypeRecycler, hospitalProductRecycler;
    private TextView tvHintTxt, tvTitle;
    private TextView tvGoodsName, tvGoodsPrice, tvGoodsType, tvGoodsInfo;
    private TextView tvPatientName, tvPatientAge;
    private TextView tvTitle1, tvTitle2, tvTitle3, tvTitle4;
    private TextView tvNext;
    private EditText etDes;
    private ImageView ivHosspitalImg;
    private TextView tvHospitalName, tvHospitalAddress, tvHospitalGrade;
    private RelativeLayout rlHospitalLayout;
    private TextView tvContact, tvContactPhone, tvUseful, tvAttention;
    private View line;
    private ScrollView llProductDetaillayout;
    private View footerView, productTypeFooterView, productFooterView;
    private RegistrationAdapter registrationAdapter;
    private RegistrationProductTypeAdapter registrationProductTypeAdapter;
    private RegistrationProductAdapter registrationProductAdapter;
    private List<HospitalBean> hospitalList = new ArrayList<>();
    private List<HospitalProductTypeBean> productTypeBeans = new ArrayList<>();
    private List<HospitalProductBean> productBeans = new ArrayList<>();
    private HospitalBean curHospital;
    private HospitalProductTypeBean curProductType;
    private HospitalProductBean curProduct;
    private PatientBean patientBean;
    private int productTypeId;
    private String patientId;
    private String typeName;
    /**
     * 显示商品列表
     * 1 医院列表   2 商品列表  3 商品详情
     */
    private int curPage;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_service_pack;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        autoLoadRecyclerView = (AutoLoadRecyclerView)findViewById(
                R.id.act_service_pack_hospital_list);
        hospitalProductTypeRecycler = (AutoLoadRecyclerView)findViewById(
                R.id.act_service_pack_hospital_product_type_list);
        hospitalProductRecycler = (AutoLoadRecyclerView)findViewById(
                R.id.act_service_pack_hospital_product_list);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        productFooterView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        productTypeFooterView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        llProductDetaillayout = (ScrollView)findViewById(R.id.act_service_pack_goods_detail_layout);
        tvNext = (TextView)findViewById(R.id.act_service_pack_next);
        tvHintTxt = (TextView)findViewById(R.id.act_service_pack_hint_txt);
        tvGoodsName = (TextView)findViewById(R.id.act_service_pack_goods_name);
        tvGoodsType = (TextView)findViewById(R.id.act_service_pack_goods_type);
        tvGoodsInfo = (TextView)findViewById(R.id.act_service_pack_goods_info);
        tvGoodsPrice = (TextView)findViewById(R.id.act_service_pack_goods_price);
        tvPatientName = (TextView)findViewById(R.id.act_service_pack_patient_name);
        tvPatientAge = (TextView)findViewById(R.id.act_service_pack_patient_age);
        tvTitle1 = (TextView)findViewById(R.id.act_service_pack_goods_name_txt);
        tvTitle2 = (TextView)findViewById(R.id.act_service_pack_goods_price_txt);
        tvTitle4 = (TextView)findViewById(R.id.act_service_pack_goods_info_txt);
        etDes = (EditText)findViewById(R.id.act_service_pack_des);
        line = findViewById(R.id.title_line);
        rlHospitalLayout = (RelativeLayout)findViewById(R.id.act_service_pack_hint_hospital_layout);
        ivHosspitalImg = (ImageView)findViewById(R.id.act_service_pack_hint_hospital_img);
        tvHospitalName = (TextView)findViewById(R.id.act_service_pack_hint_hospital_name);
        tvHospitalAddress = (TextView)findViewById(R.id.act_service_pack_hint_hospital_address);
        tvHospitalGrade = (TextView)findViewById(R.id.act_service_pack_hint_hospital_grade);
        tvContact = (TextView)findViewById(R.id.act_service_pack_contact);
        tvContactPhone = (TextView)findViewById(R.id.act_service_pack_contact_phone);
        tvUseful = (TextView)findViewById(R.id.act_service_pack_useful);
        tvAttention = (TextView)findViewById(R.id.act_service_pack_attention);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        if (getIntent() != null)
        {
            productTypeId = getIntent().getIntExtra(CommonData.KEY_PUBLIC, -1);
            typeName = getIntent().getStringExtra(CommonData.KEY_REGISTRATION_TYPE);
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
        curPage = 1;
        registrationAdapter = new RegistrationAdapter(this, hospitalList);
        registrationAdapter.addFooterView(footerView);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(registrationAdapter);
        registrationProductTypeAdapter = new RegistrationProductTypeAdapter(this, productTypeBeans);
        registrationProductTypeAdapter.addFooterView(productTypeFooterView);
        hospitalProductTypeRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        hospitalProductTypeRecycler.setItemAnimator(new DefaultItemAnimator());
        hospitalProductTypeRecycler.setAdapter(registrationProductTypeAdapter);
        registrationProductAdapter = new RegistrationProductAdapter(this, productBeans);
        registrationProductAdapter.addFooterView(productFooterView);
        hospitalProductRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        hospitalProductRecycler.setItemAnimator(new DefaultItemAnimator());
        hospitalProductRecycler.setAdapter(registrationProductAdapter);
        ininPageData();
        getHospitalListByDoctorId();
    }

    @Override
    public void initListener()
    {
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        tvNext.setOnClickListener(this);
        registrationAdapter.setOnItemClickListener((v, position, item) ->
                                                   {
                                                       curPage = 2;
                                                       curHospital = item;
                                                       tvHintTxt.setVisibility(View.GONE);
                                                       line.setVisibility(View.GONE);
                                                       rlHospitalLayout.setVisibility(View.VISIBLE);
                                                       tvHospitalName.setText(
                                                               curHospital.getHospitalName());
                                                       tvHospitalAddress.setText(
                                                               curHospital.getCityName());
                                                       tvHospitalGrade.setText(
                                                               curHospital.getHospitalLevel());
                                                       Glide.with(this)
                                                            .load(curHospital.getHospitalLogo())
                                                            .apply(GlideHelper.getOptionsHospitalPic())
                                                            .into(ivHosspitalImg);
                                                       autoLoadRecyclerView.setVisibility(
                                                               View.GONE);
                                                       hospitalProductTypeRecycler.setVisibility(
                                                               View.VISIBLE);
                                                       getHospitalProductListByHospitalId();
                                                   });
        registrationProductTypeAdapter.setOnItemClickListener((v, position, item) ->
                                                              {
                                                                  curPage = 3;
                                                                  curProductType = item;
                                                                  tvHintTxt.setVisibility(
                                                                          View.VISIBLE);
                                                                  line.setVisibility(View.VISIBLE);
                                                                  rlHospitalLayout.setVisibility(
                                                                          View.GONE);
                                                                  tvHintTxt.setText(
                                                                          curHospital.getHospitalName() +
                                                                          " > " +
                                                                          curProductType.getProductTypeName());
                                                                  hospitalProductTypeRecycler.setVisibility(
                                                                          View.GONE);
                                                                  hospitalProductRecycler.setVisibility(
                                                                          View.VISIBLE);
                                                                  registrationProductAdapter.setList(
                                                                          item.getProductInfoByHospitalIdResList());
                                                              });
        registrationProductAdapter.setOnItemClickListener((v, position, item) ->
                                                          {
                                                              curPage = 4;
                                                              curProduct = item;
                                                              initProductDetailPage();
                                                          });
    }

    private void ininPageData()
    {
        List<PatientBean> list = DataSupport.where("patientId = ?", patientId)
                                            .find(PatientBean.class);
        if (list != null && list.size() > 0)
        {
            patientBean = list.get(0);
        }
        else
        {
            getPatientInfo();
        }
        tvTitle.setText(typeName);
        tvHintTxt.setText(String.format(getString(R.string.txt_registration_type_hint), typeName));
        tvTitle1.setText(
                String.format(getString(R.string.txt_registration_project_hint), typeName));
        tvTitle2.setText(String.format(getString(R.string.txt_registration_price_hint), typeName));
        tvTitle4.setText(
                String.format(getString(R.string.txt_registration_content_hint), typeName));
    }

    /**
     * 产品详情页面
     */
    private void initProductDetailPage()
    {
        tvHintTxt.setVisibility(View.GONE);
        rlHospitalLayout.setVisibility(View.VISIBLE);
        //        tvHintTxt.setText(
        //                curHospital.getHospitalName() + " > " + curProductType.getProductTypeName() +
        //                " > " + curProduct.getProductName());
        tvGoodsName.setText(curProduct.getProductName());
        tvGoodsPrice.setText(curProduct.getProductPrice() + curProduct.getProductPriceUnit());
        tvGoodsType.setText(curProduct.getProductTypeName());
        tvGoodsInfo.setText(curProduct.getProductDescription());
        tvContact.setText(curProduct.getProductContactName());
        tvContactPhone.setText(curProduct.getProductContactPhone());
        tvUseful.setText(curProduct.getProductAccessChannel());
        tvAttention.setText(curProduct.getProductOtherInfo());
        hospitalProductRecycler.setVisibility(View.GONE);
        llProductDetaillayout.setVisibility(View.VISIBLE);
        if (patientBean != null)
        {
            if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                patientBean.getNickname().length() < 20)
            {
                tvPatientName.setText(patientBean.getNickname());
            }
            else
            {
                tvPatientName.setText(patientBean.getName());
            }
            tvPatientAge.setText(AllUtils.formatDateByAge(patientBean.getBirthDate()));
        }
        tvNext.setVisibility(View.VISIBLE);
    }

    /**
     * 获取患者个人信息
     */
    private void getPatientInfo()
    {
        mIRequest.getPatientInfo(patientId, this);
    }

    /**
     * 获取医院列表
     */
    private void getHospitalListByDoctorId()
    {
        mIRequest.getHospitalListByDoctorId(loginSuccessBean.getDoctorId(), this);
    }

    /**
     * 获取医院商品列表
     */
    private void getHospitalProductListByHospitalId()
    {
        mIRequest.getHospitalProductListByHospitalId(curHospital.getHospitalId(), this);
    }

    /**
     * 新增订单
     */
    private void addProductOrderNew()
    {
        String diagnosisInfo = etDes.getText().toString().trim();
        if (TextUtils.isEmpty(diagnosisInfo))
        {
            ToastUtil.toast(this, "请输入描述信息");
            return;
        }
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/product/info/doctor/operator/add",
                RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("diagnosisInfo", diagnosisInfo);
        params.put("productTypeId", curProductType.getProductTypeId());
        params.put("productTypeName", curProductType.getProductTypeName());
        params.put("doctorId", loginSuccessBean.getDoctorId());
        params.put("doctorName", loginSuccessBean.getName());
        params.put("hospitalName", curHospital.getHospitalName());
        params.put("patientSex", patientBean.getSex());
        params.put("hospitalId", curHospital.getHospitalId());
        params.put("patientId", patientBean.getPatientId());
        params.put("patientName", patientBean.getName());
        params.put("patientBirthDate", patientBean.getBirthDate());
        params.put("productDescription", curProduct.getProductDescription());
        params.put("productId", curProduct.getProductId());
        params.put("productName", curProduct.getProductName());
        params.put("productPrice", curProduct.getProductPrice());
        params.put("productPriceUnit", curProduct.getProductPriceUnit());
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>()
        {
            @Override
            public void onStart(int what)
            {
                showProgressDialog("请稍等...");
            }

            @Override
            public void onSucceed(int what, Response<String> response)
            {
                String s = response.get();
                try
                {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
                    if (baseResponse != null && baseResponse.getCode() == 200)
                    {
                        HintDialog hintDialog = new HintDialog(ServicePackActivity.this);
                        hintDialog.isShowCancelBtn(false);
                        hintDialog.setContentString("已发送给患者，请等待患者答复");
                        hintDialog.setOnEnterClickListener(() -> finish());
                        hintDialog.show();
                    }
                    else
                    {
                        ToastUtil.toast(ServicePackActivity.this, baseResponse.getMsg());
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
                ToastUtil.toast(ServicePackActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what)
            {
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.public_title_bar_back:
                if (onBack())
                {
                    finish();
                }
                break;
            case R.id.act_service_pack_next:
                HintDialog hintDialog = new HintDialog(this);
                hintDialog.setContentString("确定发送给患者？");
                hintDialog.setOnEnterClickListener(new OnEnterClickListener()
                {
                    @Override
                    public void onEnter()
                    {
                        addProductOrderNew();
                    }
                });
                hintDialog.show();
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case GET_HOSPITAL_LIST_BY_DOCTORID:
                hospitalList = response.getData();
                registrationAdapter.setList(hospitalList);
                break;
            case GET_HOSPITAL_PRODUCT_LIST_BY_HOSPITALID:
                productTypeBeans = response.getData();
                registrationProductTypeAdapter.setList(productTypeBeans);
                break;
            case ADD_PRODUCT_ORDER:
                HintDialog hintDialog = new HintDialog(this);
                hintDialog.isShowCancelBtn(false);
                hintDialog.setContentString("已发送给患者，请等待患者答复");
                hintDialog.setOnEnterClickListener(() -> finish());
                hintDialog.show();
                break;
            case GET_PATIENT_INFO:
                patientBean = response.getData();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (!onBack())
            {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean onBack()
    {
        if (curPage == 4)
        {
            curPage = 3;
            tvHintTxt.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            rlHospitalLayout.setVisibility(View.GONE);
            tvHintTxt.setText(
                    curHospital.getHospitalName() + " > " + curProductType.getProductTypeName());
            tvNext.setVisibility(View.GONE);
            hospitalProductRecycler.setVisibility(View.VISIBLE);
            llProductDetaillayout.setVisibility(View.GONE);
            return false;
        }
        else if (curPage == 3)
        {
            curPage = 2;
            //            tvHintTxt.setText(curHospital.getHospitalName());
            tvHintTxt.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            rlHospitalLayout.setVisibility(View.VISIBLE);
            tvHospitalName.setText(curHospital.getHospitalName());
            tvHospitalAddress.setText(curHospital.getCityName());
            tvHospitalGrade.setText(curHospital.getHospitalLevel());
            Glide.with(this)
                 .load(curHospital.getHospitalLogo())
                 .apply(GlideHelper.getOptionsHospitalPic())
                 .into(ivHosspitalImg);
            tvNext.setVisibility(View.GONE);
            hospitalProductTypeRecycler.setVisibility(View.VISIBLE);
            hospitalProductRecycler.setVisibility(View.GONE);
            return false;
        }
        else if (curPage == 2)
        {
            curPage = 1;
            tvHintTxt.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            rlHospitalLayout.setVisibility(View.GONE);
            tvHintTxt.setText(
                    String.format(getString(R.string.txt_registration_type_hint), typeName));
            hospitalProductTypeRecycler.setVisibility(View.GONE);
            autoLoadRecyclerView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
