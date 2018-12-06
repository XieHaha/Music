package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.OrderStatus;

import java.util.ArrayList;

import custom.frame.bean.TransPatientBean;

/**
 * Created by dundun on 18/7/14.
 */
public class TransferInfoAdapter extends BaseAdapter implements OrderStatus, CommonData
{
    private Context context;
    private ArrayList<TransPatientBean> list = new ArrayList<>();

    public TransferInfoAdapter(Context context)
    {
        this.context = context;
    }

    public void setList(ArrayList<TransPatientBean> list)
    {
        this.list = list;
    }

    @Override
    public int getCount()
    {
        //不显示全部 固定显示条数
        return ((list.size() > CommonData.DATA_LIST_BASE_NUM)
                ? CommonData.DATA_LIST_BASE_NUM
                : list.size());
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context)
                                        .inflate(R.layout.item_transfer, parent, false);
            holder.tvPatientName = convertView.findViewById(R.id.item_transfer_patient_name);
            holder.tvPatientCase = convertView.findViewById(R.id.item_transfer_patient_case);
            holder.tvDoctorName = convertView.findViewById(R.id.item_transfer_doc_name);
            holder.tvDoctorHospital = convertView.findViewById(R.id.item_transfer_hospital);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        initData(holder, position);
        return convertView;
    }

    private void initData(ViewHolder holder, int position)
    {
        TransPatientBean curTransferPatient = list.get(position);
        holder.tvPatientName.setText(curTransferPatient.getPatientName());
        holder.tvPatientCase.setText(curTransferPatient.getFromDoctorDiagnosisInfo());
        holder.tvDoctorName.setText(curTransferPatient.getFromDoctorName());
        holder.tvDoctorHospital.setText(curTransferPatient.getFromDoctorHospitalName());
    }

    class ViewHolder
    {
        private TextView tvPatientName, tvPatientCase, tvDoctorName, tvDoctorHospital;
    }
}
