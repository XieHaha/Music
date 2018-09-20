package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.RegistrationAdapter;
import com.yht.yihuantong.ui.adapter.RegistrationProductAdapter;
import com.yht.yihuantong.ui.adapter.RegistrationProductTypeAdapter;
import com.yht.yihuantong.ui.dialog.HintDialog;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.HospitalBean;
import custom.frame.bean.HospitalProductBean;
import custom.frame.bean.HospitalProductTypeBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;

/**
 * Created by dundun on 18/9/13.
 */
public class ServicePackActivity extends BaseActivity
{
    private AutoLoadRecyclerView autoLoadRecyclerView, hospitalProductTypeRecycler, hospitalProductRecycler;
    private TextView tvHintTxt, tvTitle;
    private TextView tvGoodsName, tvGoodsPrice, tvGoodsType, tvGoodsInfo;
    private TextView tvTitle1, tvTitle2, tvTitle3, tvTitle4;
    private TextView tvNext;
    private LinearLayout llProductDetaillayout;
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
        llProductDetaillayout = (LinearLayout)findViewById(
                R.id.act_service_pack_goods_detail_layout);
        tvNext = (TextView)findViewById(R.id.act_service_pack_next);
        tvHintTxt = (TextView)findViewById(R.id.act_service_pack_hint_txt);
        tvGoodsName = (TextView)findViewById(R.id.act_service_pack_goods_name);
        tvGoodsType = (TextView)findViewById(R.id.act_service_pack_goods_type);
        tvGoodsInfo = (TextView)findViewById(R.id.act_service_pack_goods_info);
        tvGoodsPrice = (TextView)findViewById(R.id.act_service_pack_goods_price);
        tvTitle1 = (TextView)findViewById(R.id.act_service_pack_goods_name_txt);
        tvTitle2 = (TextView)findViewById(R.id.act_service_pack_goods_price_txt);
        tvTitle4 = (TextView)findViewById(R.id.act_service_pack_goods_info_txt);
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
                                                       tvHintTxt.setText(
                                                               curHospital.getHospitalName());
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
        tvHintTxt.setText(
                curHospital.getHospitalName() + " > " + curProductType.getProductTypeName() +
                " > " + curProduct.getProductName());
        tvGoodsName.setText(curProduct.getProductName());
        tvGoodsPrice.setText(curProduct.getProductPrice() + curProduct.getProductPriceUnit());
        tvGoodsType.setText(curProduct.getProductTypeName());
        tvGoodsInfo.setText(curProduct.getProductDescription());
        hospitalProductRecycler.setVisibility(View.GONE);
        llProductDetaillayout.setVisibility(View.VISIBLE);
        tvNext.setVisibility(View.VISIBLE);
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

    private void addProductOrder()
    {
        mIRequest.addProductOrder(loginSuccessBean.getDoctorId(), patientId,
                                  curHospital.getHospitalId(), curProduct.getProductId(),
                                  productTypeId, this);
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
            case R.id.act_registration_next:
                addProductOrder();
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
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        switch (task)
        {
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
            tvHintTxt.setText(curHospital.getHospitalName());
            tvNext.setVisibility(View.GONE);
            hospitalProductTypeRecycler.setVisibility(View.VISIBLE);
            hospitalProductRecycler.setVisibility(View.GONE);
            return false;
        }
        else if (curPage == 2)
        {
            curPage = 1;
            tvHintTxt.setText(
                    String.format(getString(R.string.txt_registration_type_hint), typeName));
            hospitalProductTypeRecycler.setVisibility(View.GONE);
            autoLoadRecyclerView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }
}
