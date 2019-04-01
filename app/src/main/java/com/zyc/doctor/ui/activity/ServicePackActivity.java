package com.zyc.doctor.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zyc.doctor.R;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.ui.adapter.RegistrationAdapter;
import com.zyc.doctor.ui.adapter.RegistrationProductAdapter;
import com.zyc.doctor.ui.adapter.RegistrationProductTypeAdapter;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.ui.dialog.listener.OnEnterClickListener;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.RecentContactUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import com.zyc.doctor.http.data.BaseResponse;
import com.zyc.doctor.http.data.HospitalBean;
import com.zyc.doctor.http.data.HospitalProductBean;
import com.zyc.doctor.http.data.HospitalProductTypeBean;
import com.zyc.doctor.http.data.PatientBean;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.data.BaseNetCode;
import com.zyc.doctor.http.data.HttpConstants;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.GlideHelper;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * @author dundun
 */
public class ServicePackActivity<T> extends BaseActivity {
    private static final String TAG = "ServicePackActivity";
    @BindView(R.id.public_title_bar_title)
    TextView tvTitle;
    @BindView(R.id.act_cooperate_doc_search)
    EditText searchEdit;
    @BindView(R.id.search_layout)
    LinearLayout llSearchLayout;
    @BindView(R.id.act_service_pack_hint_txt)
    TextView tvHintTxt;
    @BindView(R.id.act_service_pack_hint_hospital_img)
    ImageView ivHosspitalImg;
    @BindView(R.id.act_service_pack_hint_hospital_name)
    TextView tvHospitalName;
    @BindView(R.id.act_service_pack_hint_hospital_address)
    TextView tvHospitalAddress;
    @BindView(R.id.act_service_pack_hint_hospital_grade)
    TextView tvHospitalGrade;
    @BindView(R.id.act_service_pack_hint_hospital_layout)
    RelativeLayout rlHospitalLayout;
    @BindView(R.id.title_line)
    View line;
    @BindView(R.id.act_service_pack_hospital_list)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_service_pack_hospital_product_type_list)
    AutoLoadRecyclerView hospitalProductTypeRecycler;
    @BindView(R.id.act_service_pack_hospital_product_list)
    AutoLoadRecyclerView hospitalProductRecycler;
    @BindView(R.id.act_service_pack_goods_name_txt)
    TextView tvTitle1;
    @BindView(R.id.act_service_pack_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.act_service_pack_goods_price_txt)
    TextView tvTitle2;
    @BindView(R.id.act_service_pack_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.act_service_pack_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.act_service_pack_goods_info_txt)
    TextView tvTitle4;
    @BindView(R.id.act_service_pack_goods_info)
    TextView tvGoodsInfo;
    @BindView(R.id.act_service_pack_patient_name)
    TextView tvPatientName;
    @BindView(R.id.act_service_pack_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.act_service_pack_des)
    EditText etDes;
    @BindView(R.id.act_service_pack_contact)
    TextView tvContact;
    @BindView(R.id.act_service_pack_contact_phone)
    TextView tvContactPhone;
    @BindView(R.id.act_service_pack_useful)
    TextView tvUseful;
    @BindView(R.id.act_service_pack_attention)
    TextView tvAttention;
    @BindView(R.id.act_service_pack_patient_info_layout)
    LinearLayout llPatientInfoLayout;
    @BindView(R.id.act_service_pack_goods_detail_layout)
    ScrollView llProductDetaillayout;
    @BindView(R.id.act_service_pack_next)
    TextView tvNext;

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
    /**
     * 只是查看信息 不开单
     */
    private boolean limit;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_service_pack;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        productFooterView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        productTypeFooterView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        if (getIntent() != null) {
            limit = getIntent().getBooleanExtra("limit", false);
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
    public void initListener() {
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        tvNext.setOnClickListener(this);
        rlHospitalLayout.setOnClickListener(this);
        registrationAdapter.setOnItemClickListener((v, position, item) ->
        {
            curPage = 2;
            curHospital = item;
            tvHintTxt.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            llSearchLayout.setVisibility(View.GONE);
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
            llSearchLayout.setVisibility(
                    View.VISIBLE);
            hospitalProductTypeRecycler.setVisibility(
                    View.GONE);
            hospitalProductRecycler.setVisibility(
                    View.VISIBLE);
            productBeans = item.getProductInfoByHospitalIdResList();
            DataSupport.deleteAll(
                    HospitalProductBean.class);
            DataSupport.saveAll(productBeans);
            registrationProductAdapter.setList(
                    productBeans);
        });
        registrationProductAdapter.setOnItemClickListener((v, position, item) ->
        {
            curPage = 4;
            curProduct = item;
            initProductDetailPage();
        });
        searchEdit.setOnEditorActionListener((v, actionId, event) ->
        {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInputFromWindow();
                search(v.getText().toString());
            }
            return false;
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    search(s.toString());
                } else {
                    registrationProductAdapter.setList(productBeans);
                    registrationProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void ininPageData() {
        if (!limit) {
            List<PatientBean> list = DataSupport.where("patientId = ?", patientId)
                    .find(PatientBean.class);
            if (list != null && list.size() > 0) {
                patientBean = list.get(0);
            } else {
                getPatientInfo();
            }
            llPatientInfoLayout.setVisibility(View.VISIBLE);
        } else {
            llPatientInfoLayout.setVisibility(View.GONE);
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
    private void initProductDetailPage() {
        tvHintTxt.setVisibility(View.GONE);
        rlHospitalLayout.setVisibility(View.VISIBLE);
        llSearchLayout.setVisibility(View.GONE);
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
        if (patientBean != null) {
            if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                    patientBean.getNickname().length() < 20) {
                tvPatientName.setText(patientBean.getNickname());
            } else {
                tvPatientName.setText(patientBean.getName());
            }
            tvPatientAge.setText(AllUtils.getAge(patientBean.getBirthDate()));
        }
        if (!limit) {
            tvNext.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 匹配搜索
     *
     * @param key
     */
    private void search(String key) {
        List<HospitalProductBean> datas = DataSupport.where("productName like ?", "%" + key + "%")
                .find(HospitalProductBean.class);
        registrationProductAdapter.setList(datas);
        registrationProductAdapter.notifyDataSetChanged();
    }

    /**
     * 获取患者个人信息
     */
    private void getPatientInfo() {
        mIRequest.getPatientInfo(patientId, this);
    }

    /**
     * 获取医院列表
     */
    private void getHospitalListByDoctorId() {
        mIRequest.getHospitalListByDoctorId(loginSuccessBean.getDoctorId(), this);
    }

    /**
     * 获取医院商品列表
     */
    private void getHospitalProductListByHospitalId() {
        mIRequest.getHospitalProductListByHospitalId(curHospital.getHospitalId(), this);
    }

    /**
     * 新增订单
     */
    private void addProductOrderNew() {
        String diagnosisInfo = etDes.getText().toString().trim();
        if (TextUtils.isEmpty(diagnosisInfo)) {
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
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                showProgressDialog("请稍等...");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    //保存最近联系人
                    RecentContactUtils.save(patientBean.getPatientId());
                    NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
                            HintDialog hintDialog = new HintDialog(ServicePackActivity.this);
                            hintDialog.isShowCancelBtn(false);
                            hintDialog.setContentString("已发送给患者，请等待患者答复");
                            hintDialog.setOnEnterClickListener(() -> finish());
                            hintDialog.show();
                        } else {
                            ToastUtil.toast(ServicePackActivity.this, baseResponse.getMsg());
                        }
                    }
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(ServicePackActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                if (onBack()) {
                    finish();
                }
                break;
            case R.id.act_service_pack_next:
                HintDialog hintDialog = new HintDialog(this);
                hintDialog.setContentString("确定发送给患者？");
                hintDialog.setOnEnterClickListener(new OnEnterClickListener() {
                    @Override
                    public void onEnter() {
                        addProductOrderNew();
                    }
                });
                hintDialog.show();
                break;
            case R.id.act_service_pack_hint_hospital_layout:
                Intent intent = new Intent(this, HospitalInfoActivity.class);
                intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, curHospital);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!onBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean onBack() {
        if (curPage == 4) {
            curPage = 3;
            tvHintTxt.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            rlHospitalLayout.setVisibility(View.GONE);
            llSearchLayout.setVisibility(View.VISIBLE);
            tvHintTxt.setText(
                    curHospital.getHospitalName() + " > " + curProductType.getProductTypeName());
            tvNext.setVisibility(View.GONE);
            hospitalProductRecycler.setVisibility(View.VISIBLE);
            llProductDetaillayout.setVisibility(View.GONE);
            return false;
        } else if (curPage == 3) {
            curPage = 2;
            //            tvHintTxt.setText(curHospital.getHospitalName());
            tvHintTxt.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            llSearchLayout.setVisibility(View.GONE);
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
        } else if (curPage == 2) {
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

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow() {
        InputMethodManager inputmanger = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(searchEdit.getWindowToken(), 0);
    }

}
