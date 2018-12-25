package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.AllUtils;

import java.util.List;

import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 18/7/14.
 * 患者开单记录适配器
 */
public class TransferInfoAdapter extends BaseRecyclerAdapter<TransPatientBean>
{
    private Context context;

    public TransferInfoAdapter(Context context,List<TransPatientBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_transfer_info, parent, false);
        return new TransferInfoAdapter.Transferholder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, TransPatientBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class Transferholder extends BaseViewHolder<TransPatientBean>
    {
        private TextView tvPatientCase, tvFromDoctorName, tvToDoctorName, tvTime;

        public Transferholder(View itemView)
        {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_transfer_info_time);
            tvPatientCase = itemView.findViewById(R.id.item_transfer_info_patient_case);
            tvFromDoctorName = itemView.findViewById(
                    R.id.item_transfer_info_from_doc_name);
            tvToDoctorName = itemView.findViewById(R.id.item_transfer_info_to_doc_name);
        }

        @Override
        public void showView(final int position, final TransPatientBean curTransferPatient)
        {
            tvPatientCase.setText(curTransferPatient.getFromDoctorDiagnosisInfo());
            tvTime.setText(AllUtils.formatDate(curTransferPatient.getTransferDate(),
                                                      AllUtils.YYYY_MM_DD_HH_MM));
            tvToDoctorName.setText(curTransferPatient.getToDoctorName());
            tvFromDoctorName.setText(curTransferPatient.getFromDoctorName());
        }
    }
}
