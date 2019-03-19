package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.TransferStatu;
import com.zyc.doctor.utils.AllUtils;

import java.util.List;

import custom.frame.bean.TransPatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 18/7/14.
 * 患者开单记录适配器
 */
public class TransferInfoAdapter extends BaseRecyclerAdapter<TransPatientBean>
        implements TransferStatu
{
    private Context context;

    public TransferInfoAdapter(Context context, List<TransPatientBean> list)
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
        private TextView tvPatientCase, tvFromDoctorName, tvToDoctorName, tvTime, tvStatus;

        public Transferholder(View itemView)
        {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_transfer_info_time);
            tvPatientCase = itemView.findViewById(R.id.item_transfer_info_patient_case);
            tvFromDoctorName = itemView.findViewById(R.id.item_transfer_info_from_doc_name);
            tvToDoctorName = itemView.findViewById(R.id.item_transfer_info_to_doc_name);
            tvStatus = itemView.findViewById(R.id.item_transfer_info_status);
        }

        @Override
        public void showView(final int position, final TransPatientBean curTransferPatient)
        {
            tvPatientCase.setText(curTransferPatient.getFromDoctorDiagnosisInfo());
            tvTime.setText(AllUtils.formatDate(curTransferPatient.getTransferDate(),
                                               AllUtils.YYYY_MM_DD_HH_MM));
            tvToDoctorName.setText(curTransferPatient.getToDoctorName());
            tvFromDoctorName.setText(curTransferPatient.getFromDoctorName());
            switch (curTransferPatient.getAcceptState())
            {
                case TRANSFER_NONE:
                    tvStatus.setText(R.string.txt_transfer_patient_to_comfirm);
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._FF6417));
                    break;
                case TRANSFER_RECV:
                    tvStatus.setText(R.string.txt_transfer_patient_to_wait_visit);
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._1F6BAC));
                    break;
                case TRANSFER_VISIT:
                    tvStatus.setText(R.string.txt_transfer_patient_to_complete_visit);
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color.app_main_color));
                    break;
                case TRANSFER_CANCEL:
                case TRANSFER_HOSPITAL_CANCEL:
                    tvStatus.setText(R.string.txt_transfer_patient_to_cancel_visit);
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._E40505));
                    break;
                case TRANSFER_REFUSE:
                    tvStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._E40505));
                    break;
            }
        }
    }
}
