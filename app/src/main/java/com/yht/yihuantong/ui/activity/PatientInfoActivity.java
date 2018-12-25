package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.FragmentVpAdapter;
import com.yht.yihuantong.ui.fragment.HealthInfoFragment;
import com.yht.yihuantong.ui.fragment.OrderInfoFragment;
import com.yht.yihuantong.ui.fragment.TransferInfoFragment;
import com.yht.yihuantong.utils.AllUtils;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CombineBean;
import custom.frame.bean.CombineChildBean;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.widgets.view.SearchLabelLayout;
import custom.frame.widgets.view.ViewPrepared;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 */
public class PatientInfoActivity extends BaseActivity
{
    private Button btnHealthInfo, btnOrderInfo, btnTransferInfo;
    private ImageView ivTitleMore;
    private CircleImageView ivHeadImg;
    private TextView tvName, tvSex, tvAge, tvCompany, tvAddress;
    private SearchLabelLayout searchLabelLayout;
    private View viewIndicator;
    private ViewPager viewPager;
    private FragmentVpAdapter fragmentVpAdapter;
    /**
     * 健康档案
     */
    private HealthInfoFragment healthInfoFragment;
    /**
     * 开单
     */
    private OrderInfoFragment orderInfoFragment;
    /**
     * 转诊
     */
    private TransferInfoFragment transferInfoFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private PatientBean patientBean;
    /**
     * 综合病史信息
     */
    private CombineBean combineBean;
    /**
     * 手术信息
     */
    private ArrayList<CombineChildBean> patientSurgeryList = new ArrayList<>();
    /**
     * 诊断信息
     */
    private ArrayList<CombineChildBean> patientDiagnosisList = new ArrayList<>();
    /**
     * 过敏信息
     */
    private ArrayList<CombineChildBean> patientAllergyList = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_patient_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("患者信息");
        ivTitleMore = (ImageView)findViewById(R.id.public_title_bar_more_two);
        ivTitleMore.setVisibility(View.VISIBLE);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_patient_info_headImg);
        tvName = (TextView)findViewById(R.id.act_patient_info_name);
        tvSex = (TextView)findViewById(R.id.act_patient_info_sex);
        tvAge = (TextView)findViewById(R.id.act_patient_info_age);
        tvCompany = (TextView)findViewById(R.id.act_patient_info_company);
        tvAddress = (TextView)findViewById(R.id.act_patient_info_address);
        searchLabelLayout = (SearchLabelLayout)findViewById(
                R.id.act_patient_info_searchLabelLayout);
        btnHealthInfo = (Button)findViewById(R.id.act_patient_info_health_info);
        btnOrderInfo = (Button)findViewById(R.id.act_patient_info_order_info);
        btnTransferInfo = (Button)findViewById(R.id.act_patient_info_transfer_info);
        viewPager = (ViewPager)findViewById(R.id.act_patient_info_viewpager);
        viewIndicator = findViewById(R.id.act_patient_info_indicator);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            patientBean = (PatientBean)getIntent().getSerializableExtra(
                    CommonData.KEY_PATIENT_BEAN);
        }
        new ViewPrepared().asyncPrepare(btnHealthInfo, (w, h) ->
        {
            ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
            params.width = w;
            viewIndicator.setLayoutParams(params);
        });
        healthInfoFragment = new HealthInfoFragment();
        healthInfoFragment.setPatientBean(patientBean);
        orderInfoFragment = new OrderInfoFragment();
        orderInfoFragment.setPatientBean(patientBean);
        transferInfoFragment = new TransferInfoFragment();
        transferInfoFragment.setPatientBean(patientBean);
        fragmentList.add(healthInfoFragment);
        fragmentList.add(orderInfoFragment);
        fragmentList.add(transferInfoFragment);
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentVpAdapter);
        viewPager.setCurrentItem(0);
        initPageData();
        getPatientCombine();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        ivTitleMore.setOnClickListener(this);
        btnHealthInfo.setOnClickListener(this);
        btnOrderInfo.setOnClickListener(this);
        btnTransferInfo.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int tabWidth = btnHealthInfo.getWidth();
                viewIndicator.setTranslationX((position * tabWidth) + (positionOffset * tabWidth));
            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        btnHealthInfo.setSelected(true);
                        btnOrderInfo.setSelected(false);
                        btnTransferInfo.setSelected(false);
                        break;
                    case 1:
                        btnHealthInfo.setSelected(false);
                        btnOrderInfo.setSelected(true);
                        btnTransferInfo.setSelected(false);
                        break;
                    case 2:
                        btnHealthInfo.setSelected(false);
                        btnOrderInfo.setSelected(false);
                        btnTransferInfo.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        if (patientBean != null)
        {
            tvName.setText(patientBean.getName());
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.formatDateByAge(patientBean.getBirthDate()) + "岁");
            if (!TextUtils.isEmpty(patientBean.getUnitName()))
            {
                tvCompany.setVisibility(View.VISIBLE);
                tvCompany.setText(patientBean.getUnitName());
            }
            else
            {
                tvCompany.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(patientBean.getAddress()))
            {
                tvAddress.setVisibility(View.VISIBLE);
                tvAddress.setText(patientBean.getAddress());
            }
            else
            {
                tvAddress.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(patientBean.getPatientImgUrl()))
            {
                Glide.with(this)
                     .load(patientBean.getPatientImgUrl())
                     .apply(GlideHelper.getOptionsP())
                     .into(ivHeadImg);
            }
        }
    }

    /**
     * 健康标签
     */
    private void initHealthData()
    {
        searchLabelLayout.removeAllViews();
        ArrayList<String> values = new ArrayList<>();
        values.add("健康标签:");
        if (patientDiagnosisList != null && patientDiagnosisList.size() > 0)
        {
            for (int i = 0; i < patientDiagnosisList.size(); i++)
            {
                values.add(patientDiagnosisList.get(i).getDiagnosisInfo());
            }
        }
        if (patientAllergyList != null && patientAllergyList.size() > 0)
        {
            for (int i = 0; i < patientAllergyList.size(); i++)
            {
                values.add(patientAllergyList.get(i).getAllergyInfo());
            }
        }
        if (patientSurgeryList != null && patientSurgeryList.size() > 0)
        {
            for (int i = 0; i < patientSurgeryList.size(); i++)
            {
                values.add(patientSurgeryList.get(i).getSurgeryName());
            }
        }
        if (values.size() == 1)//缺省值
        {
            values.set(0, "健康标签：未填写健康标签！");
        }
        for (int i = 0; i < values.size(); i++)
        {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_product_label, null);
            final TextView textView = view.findViewById(R.id.textView);
            if (i == 0)
            {
                textView.setBackground(null);
            }
            textView.setText(values.get(i));
            searchLabelLayout.addView(view);
        }
    }

    /**
     * 获取患者基础信息
     */
    private void getPatientCombine()
    {
        mIRequest.getPatientCombine(patientBean.getPatientId(), this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_patient_info_health_info:
                viewPager.setCurrentItem(0);
                btnHealthInfo.setSelected(true);
                btnOrderInfo.setSelected(false);
                btnTransferInfo.setSelected(false);
                break;
            case R.id.act_patient_info_order_info:
                viewPager.setCurrentItem(1);
                btnHealthInfo.setSelected(false);
                btnOrderInfo.setSelected(true);
                btnTransferInfo.setSelected(false);
                break;
            case R.id.act_patient_info_transfer_info:
                viewPager.setCurrentItem(2);
                btnHealthInfo.setSelected(false);
                btnOrderInfo.setSelected(false);
                btnTransferInfo.setSelected(true);
                break;
            case R.id.public_title_bar_more_two:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENT_COMBINE:
                combineBean = response.getData();
                if (combineBean != null)
                {
                    patientDiagnosisList = combineBean.getDiagnosisInfo();
                    patientAllergyList = combineBean.getAllergyInfo();
                    patientSurgeryList = combineBean.getSurgeryInfo();
                    initHealthData();
                }
                break;
        }
    }
}
