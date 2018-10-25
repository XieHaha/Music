package com.yht.yihuantong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.AllUtils;

import java.util.ArrayList;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CombineBean;
import custom.frame.bean.CombineChildBean;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.fragment.BaseFragment;

/**
 * 患者基本信息
 *
 * @author DUNDUN
 */
public class BaseInfoFragment extends BaseFragment
{
    private ScrollView scrollView;
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
    public int getLayoutID()
    {
        return R.layout.fragment_base_info;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        scrollView = view.findViewById(R.id.scrollView);
        tvName = view.findViewById(R.id.fragment_base_info_name);
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
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (patientBean != null)
        {
            patientId = patientBean.getPatientId();
        }
        initPageData();
        getPatientCombine();
    }

    public void initPageData()
    {
        if (patientBean != null)
        {
            if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                patientBean.getNickname().length() < 20)
            {
                tvName.setText(patientBean.getNickname() + "(" + patientBean.getName() + ")");
            }
            else
            {
                tvName.setText(patientBean.getName());
            }
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.formatDateByAge(patientBean.getBirthDate()));
        }
    }

    public void setPatientBean(PatientBean patientBean)
    {
        this.patientBean = patientBean;
    }

    /**
     * 诊断信息
     */
    private void initCombineInfo()
    {
        if (combineBean != null)
        {
            patientDiagnosisList = combineBean.getDiagnosisInfo();
            patientAllergyList = combineBean.getAllergyInfo();
            patientSurgeryList = combineBean.getSurgeryInfo();
            //诊断信息
            if (patientDiagnosisList != null && patientDiagnosisList.size() > 0)
            {
                int size = patientDiagnosisList.size();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < size; i++)
                {
                    CombineChildBean bean = patientDiagnosisList.get(i);
                    stringBuilder.append(bean.getDiagnosisInfo());
                    if (i != size - 1)
                    {
                        stringBuilder.append("、");
                    }
                }
                tvDiagnosis.setText(stringBuilder.toString());
            }
            else
            {
                tvDiagnosis.setText("暂无信息");
            }
            //过敏信息
            if (patientAllergyList != null && patientAllergyList.size() > 0)
            {
                int size = patientAllergyList.size();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < size; i++)
                {
                    CombineChildBean bean = patientAllergyList.get(i);
                    stringBuilder.append(bean.getAllergyInfo());
                    if (i != size - 1)
                    {
                        stringBuilder.append("、");
                    }
                }
                tvAllergy.setText(stringBuilder.toString());
            }
            else
            {
                tvAllergy.setText("暂无信息");
            }
            //手术信息
            if (patientSurgeryList.size() == 0)
            {
                tvSurgery.setVisibility(View.VISIBLE);
                tvSurgery.setText("暂无信息");
            }
            surgeryAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvSurgeryInfo);
        }
        else
        {
            tvSurgery.setVisibility(View.VISIBLE);
            tvSurgery.setText("暂无信息");
        }
    }

    /**
     * 获取患者基础信息
     */
    private void getPatientCombine()
    {
        mIRequest.getPatientCombine(patientId, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENT_COMBINE:
                combineBean = response.getData();
                initCombineInfo();
                break;
        }
    }

    class SurgeryAdapter extends BaseAdapter
    {
        ViewHolder holder;

        @Override
        public int getCount()
        {
            return patientSurgeryList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return patientSurgeryList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext())
                                            .inflate(R.layout.item_patient_surgery_info, parent,
                                                     false);
                holder.tvName = convertView.findViewById(R.id.item_patient_surgery_name);
                holder.tvTime = convertView.findViewById(R.id.item_patient_surgery_time);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            init(patientSurgeryList.get(position));
            return convertView;
        }

        private void init(CombineChildBean item)
        {
            holder.tvName.setText(item.getSurgeryName());
            if (item.getSurgeryDate() == 0)
            {
                holder.tvTime.setVisibility(View.GONE);
            }
            else
            {
                holder.tvTime.setVisibility(View.VISIBLE);
            }
            holder.tvTime.setText(
                    AllUtils.formatDate(item.getSurgeryDate(), AllUtils.YYYY));
        }
    }

    class ViewHolder
    {
        public TextView tvName, tvTime;
    }

    /**
     * 设置高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView)
    {
        if (listView == null)
        {
            return;
        }
        if (surgeryAdapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < surgeryAdapter.getCount(); i++)
        {
            View listItem = surgeryAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height =
                totalHeight + (listView.getDividerHeight() * (surgeryAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
