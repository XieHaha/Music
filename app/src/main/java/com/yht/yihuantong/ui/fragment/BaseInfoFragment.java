package com.yht.yihuantong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.AllUtils;

import java.util.ArrayList;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.bean.PatientCaseBasicBean;
import custom.frame.http.Tasks;
import custom.frame.ui.fragment.BaseFragment;

/**
 * 患者基本信息
 *
 * @author DUNDUN
 */
public class BaseInfoFragment extends BaseFragment {

    private TextView tvName, tvSex, tvAge, tvType, tvAllergy, tvDiagnosis, tvSurgery;
    private ListView lvSurgeryInfo;
    private SurgeryAdapter surgeryAdapter;

    /**
     * 患者 bean
     */
    private PatientBean patientBean;
    /**
     * 患者id
     */
    private String patientId;

    /**
     * 手术信息
     */
    private ArrayList<PatientCaseBasicBean> patientSurgeryList = new ArrayList<>();
    /**
     * 诊断信息
     */
    private ArrayList<PatientCaseBasicBean> patientDiagnosisList = new ArrayList<>();
    /**
     * 过敏信息
     */
    private ArrayList<PatientCaseBasicBean> patientAllergyList = new ArrayList<>();

    @Override
    public int getLayoutID() {
        return R.layout.fragment_base_info;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        tvSex = view.findViewById(R.id.fragment_base_info_sex);
        tvAge = view.findViewById(R.id.fragment_base_info_age);
        tvType = view.findViewById(R.id.fragment_base_info_type);
        tvAllergy = view.findViewById(R.id.fragment_base_info_allergy);
        tvDiagnosis = view.findViewById(R.id.fragment_base_info_type);
        tvSurgery = view.findViewById(R.id.fragment_base_info_surgery_txt);
        lvSurgeryInfo = view.findViewById(R.id.fragment_base_info_list);
        surgeryAdapter = new SurgeryAdapter();
        lvSurgeryInfo.setAdapter(surgeryAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (patientBean != null) {
            patientId = patientBean.getPatientId();
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.formatDateByAge(patientBean.getBirthDate()));
        }
        getPatientAllergy();
        getPatientDiagnosis();
        getPatientSurgery();
    }

    public void setPatientBean(PatientBean patientBean) {
        this.patientBean = patientBean;
    }

    /**
     * 诊断信息
     */
    private void initDiagnosisInfo() {
        if (patientDiagnosisList != null && patientDiagnosisList.size() > 0) {
            int size = patientDiagnosisList.size();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                PatientCaseBasicBean bean = patientDiagnosisList.get(i);
                stringBuilder.append(bean.getDiagnosisInfo());
                if (i != size - 1) {
                    stringBuilder.append("、");
                }
            }
            tvDiagnosis.setText(stringBuilder.toString());
        } else {
            tvDiagnosis.setText("暂无信息");
        }
    }

    /**
     * 过敏信息
     */
    private void initAllergyInfo() {
        if (patientAllergyList != null && patientAllergyList.size() > 0) {
            int size = patientAllergyList.size();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < size; i++) {
                PatientCaseBasicBean bean = patientAllergyList.get(i);
                stringBuilder.append(bean.getAllergyInfo());
                if (i != size - 1) {
                    stringBuilder.append("、");
                }
            }
            tvAllergy.setText(stringBuilder.toString());
        } else {
            tvAllergy.setText("暂无信息");
        }
    }

    /**
     * 获取患者手术信息
     */
    private void getPatientSurgery() {
        mIRequest.getPatientSurgery(patientId, this);
    }

    /**
     * 获取患者诊断信息
     */
    private void getPatientDiagnosis() {
        mIRequest.getPatientDiagnosis(patientId, this);
    }

    /**
     * 获取患者过敏信息
     */
    private void getPatientAllergy() {
        mIRequest.getPatientAllergy(patientId, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);

        switch (task) {
            case GET_PATIENT_SURGERY_INFO:
                ArrayList<PatientCaseBasicBean> list = response.getData();
                if (list != null && list.size() > 0) {
                    patientSurgeryList.clear();
                    patientSurgeryList.addAll(list);
                }
                if (patientSurgeryList.size() == 0) {
                    tvSurgery.setVisibility(View.VISIBLE);
                    tvSurgery.setText("暂无信息");
                }
                surgeryAdapter.notifyDataSetChanged();
                break;
            case GET_PATIENT_DIAGNOSIS_INFO:
                ArrayList<PatientCaseBasicBean> list1 = response.getData();
                if (list1 != null && list1.size() > 0) {
                    patientDiagnosisList.clear();
                    patientDiagnosisList.addAll(list1);
                }
                initDiagnosisInfo();
                break;
            case GET_PATIENT_ALLERGY_INFO:
                ArrayList<PatientCaseBasicBean> list2 = response.getData();
                if (list2 != null && list2.size() > 0) {
                    patientAllergyList.clear();
                    patientAllergyList.addAll(list2);
                }
                initAllergyInfo();
                break;
            default:
                break;
        }
    }

    class SurgeryAdapter extends BaseAdapter {
        ViewHolder holder;

        @Override
        public int getCount() {
            return patientSurgeryList.size();
        }

        @Override
        public Object getItem(int position) {
            return patientSurgeryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_patient_surgery_info, parent, false);
                holder.tvName = convertView.findViewById(R.id.item_patient_surgery_name);
                holder.tvTime = convertView.findViewById(R.id.item_patient_surgery_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            init(patientSurgeryList.get(position));
            return convertView;
        }

        private void init(PatientCaseBasicBean item) {
            holder.tvName.setText(item.getSurgeryInfo());
            if (item.getSurgeryDate() == 0)
            {
                holder.tvTime.setVisibility(View.GONE);
            }
            else
            {
                holder.tvTime.setVisibility(View.VISIBLE);
            }
            holder.tvTime.setText(AllUtils.formatDate(item.getGmtCreate(), AllUtils.DATE_FORMAT_AGE));
        }
    }

    class ViewHolder {
        public TextView tvName, tvTime;
    }
}
