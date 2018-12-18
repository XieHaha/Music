package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.OrderStatus;
import com.yht.yihuantong.utils.AllUtils;

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
            holder.tvTransferType = convertView.findViewById(R.id.item_transfer_status);
            holder.tvFromType = convertView.findViewById(R.id.item_transfer_status_txt);
            holder.tvPatientName = convertView.findViewById(R.id.item_transfer_patient_name);
            holder.tvTime = convertView.findViewById(R.id.item_transfer_time);
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
        holder.tvDoctorHospital.setText(curTransferPatient.getFromDoctorHospitalName());
        holder.tvTime.setText(AllUtils.formatDate(curTransferPatient.getTransferDate(),
                                                  AllUtils.YYYY_MM_DD_HH_MM));
        if (curTransferPatient.getFromDoctorId()
                              .equals(YihtApplication.getInstance()
                                                     .getLoginSuccessBean()
                                                     .getDoctorId()))
        {
            holder.tvDoctorName.setText(curTransferPatient.getToDoctorName());
            holder.tvTransferType.setText("转出");
            holder.tvFromType.setText("转给");
        }
        else
        {
            holder.tvDoctorName.setText(curTransferPatient.getFromDoctorName());
            holder.tvTransferType.setText("转入");
            holder.tvFromType.setText("来自");
        }
    }

    class ViewHolder
    {
        private TextView tvTransferType, tvFromType, tvPatientName, tvPatientCase, tvDoctorName, tvDoctorHospital, tvTime;
    }
}
