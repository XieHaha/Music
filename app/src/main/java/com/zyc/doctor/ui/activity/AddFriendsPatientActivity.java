package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.CombineBean;
import com.zyc.doctor.http.bean.CombineChildBean;
import com.zyc.doctor.http.bean.PatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.LabelsView;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author dundun
 * @date 18/11/12
 * 好友验证界面
 */
public class AddFriendsPatientActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView tvTitle;
    @BindView(R.id.act_add_friend_img)
    CircleImageView ivHeadImg;
    @BindView(R.id.act_add_friend_name)
    TextView tvName;
    @BindView(R.id.act_add_friend_sex)
    TextView tvSex;
    @BindView(R.id.act_add_friend_age)
    TextView tvAge;
    @BindView(R.id.act_add_friend_hint)
    TextView tvHint;
    @BindView(R.id.act_add_friend_labels)
    LabelsView labelsView;
    @BindView(R.id.act_add_friend_company)
    TextView tvCompany;
    @BindView(R.id.act_add_friend_address)
    TextView tvAddress;
    @BindView(R.id.act_add_friend_next)
    TextView tvAgree;
    @BindView(R.id.act_add_friend_refuse)
    TextView tvRefuse;
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
    private String patientId;
    /**
     * 是否是主动申请
     */
    private boolean isAdd;
    /**
     * 扫码添加患者
     */
    private static final int ADD_PATIENT = 1;
    /**
     * 医生端区别字段
     */
    private static final int MODE = 1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_friend_patient;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isAdd = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
        if (isAdd) {
            tvTitle.setText("添加好友");
            tvRefuse.setVisibility(View.GONE);
        }
        else {
            tvTitle.setText("好友申请");
            tvAgree.setText("通过验证");
            tvRefuse.setVisibility(View.VISIBLE);
        }
        getPatientInfo();
        getPatientCombine();
    }

    @Override
    public void initListener() {
        super.initListener();
        tvAgree.setOnClickListener(this);
        tvRefuse.setOnClickListener(this);
    }

    private void iniPageData(PatientBean patientBean) {
        if (patientBean != null) {
            Glide.with(this).load(patientBean.getPatientImgUrl()).apply(GlideHelper.getOptionsP()).into(ivHeadImg);
            if (!TextUtils.isEmpty(patientBean.getNickname()) && patientBean.getNickname().length() < 20) {
                tvName.setText(patientBean.getNickname() + "(" + patientBean.getName() + ")");
            }
            else {
                tvName.setText(patientBean.getName());
            }
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.getAge(patientBean.getBirthDate()) + "岁");
            if (!TextUtils.isEmpty(patientBean.getAddress())) {
                tvAddress.setText(patientBean.getAddress());
            }
            if (!TextUtils.isEmpty(patientBean.getUnitName())) {
                tvCompany.setText(patientBean.getUnitName());
            }
        }
    }

    /**
     * 健康标签
     */
    private void initHealthData() {
        ArrayList<String> values = new ArrayList<>();
        if (patientDiagnosisList != null && patientDiagnosisList.size() > 0) {
            for (int i = 0; i < patientDiagnosisList.size(); i++) {
                values.add(patientDiagnosisList.get(i).getDiagnosisInfo());
            }
        }
        if (patientAllergyList != null && patientAllergyList.size() > 0) {
            for (int i = 0; i < patientAllergyList.size(); i++) {
                values.add(patientAllergyList.get(i).getAllergyInfo());
            }
        }
        if (patientSurgeryList != null && patientSurgeryList.size() > 0) {
            for (int i = 0; i < patientSurgeryList.size(); i++) {
                values.add(patientSurgeryList.get(i).getSurgeryName());
            }
        }
        //缺省值
        if (values.size() == 0) {
            labelsView.setVisibility(View.GONE);
            tvHint.setVisibility(View.VISIBLE);
        }
        else {
            labelsView.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            labelsView.setLabels(values);
        }
    }

    /**
     * 获取患者个人信息
     */
    private void getPatientInfo() {
        RequestUtils.getPatientInfo(this, patientId, this);
    }

    /**
     * 获取患者基础信息
     */
    private void getPatientCombine() {
        RequestUtils.getPatientCombine(this, patientId, this);
    }

    /**
     * 医生扫码添加患者  转诊患者
     * mode {@link #ADD_PATIENT}
     */
    private void addPatientByScan() {
        RequestUtils.addPatientByScan(this, loginSuccessBean.getDoctorId(), patientId, ADD_PATIENT, this);
    }

    /**
     * 拒绝患者申请
     */
    private void refusePatientApply() {
        RequestUtils.refusePatientApply(this, loginSuccessBean.getDoctorId(), patientId, MODE, this);
    }

    /**
     * 同意患者申请
     */
    private void agreePatientApply() {
        RequestUtils.agreePatientApply(this, loginSuccessBean.getDoctorId(), patientId, MODE, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_add_friend_next:
                if (isAdd) {
                    addPatientByScan();
                }
                else {
                    agreePatientApply();
                }
                break;
            case R.id.act_add_friend_refuse:
                refusePatientApply();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
                finish();
                break;
            case GET_PATIENT_INFO:
                PatientBean bean = (PatientBean)response.getData();
                iniPageData(bean);
                break;
            case GET_PATIENT_COMBINE:
                combineBean = (CombineBean)response.getData();
                if (combineBean != null) {
                    patientDiagnosisList = combineBean.getDiagnosisInfo();
                    patientAllergyList = combineBean.getAllergyInfo();
                    patientSurgeryList = combineBean.getSurgeryInfo();
                    initHealthData();
                }
                break;
            case REFUSE_PATIENT_APPLY:
                ToastUtil.toast(this, response.getMsg());
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
                setResult(RESULT_OK);
                finish();
                break;
            case AGREE_PATIENT_APPLY:
                ToastUtil.toast(this, response.getMsg());
                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("add");
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        switch (task) {
            case AGREE_PATIENT_APPLY:
                ToastUtil.toast(this, response.getMsg());
                break;
            case REFUSE_PATIENT_APPLY:
                ToastUtil.toast(this, response.getMsg());
                break;
            default:
                break;
        }
    }
}
