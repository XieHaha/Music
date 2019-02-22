package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.data.TransferStatu;

import java.util.List;

import custom.frame.bean.TransPatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;
import custom.frame.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dundun on 2018-4-8.
 * 患者列表适配器
 *
 * @author DUNDUN
 */
public class TransPatientsListAdapter extends BaseRecyclerAdapter<TransPatientBean>
        implements TransferStatu
{
    private Context context;

    public TransPatientsListAdapter(Context context, List<TransPatientBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_trans_patient_list, parent, false);
        return new PatientsHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, TransPatientBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<TransPatientBean>
    {
        private CircleImageView ivPatHeadImg;
        private TextView tvDocName, tvDocHospital, tvPatName, tvStatus;

        public PatientsHolder(View itemView)
        {
            super(itemView);
            ivPatHeadImg = itemView.findViewById(R.id.item_trans_patient_list_patient_img);
            tvStatus = itemView.findViewById(R.id.item_trans_patient_list_status);
            tvPatName = itemView.findViewById(R.id.item_trans_patient_list_patient_name);
            tvDocName = itemView.findViewById(R.id.item_trans_patient_list_doctor_name);
            tvDocHospital = itemView.findViewById(R.id.item_trans_patient_list_doctor_hospital);
        }

        @Override
        public void showView(final int position, final TransPatientBean item)
        {
            Glide.with(context)
                 .load(item.getPatientImage())
                 .apply(GlideHelper.getOptions())
                 .into(ivPatHeadImg);
            tvPatName.setText(item.getPatientName());
            if (!YihtApplication.getInstance()
                                .getLoginSuccessBean()
                                .getDoctorId()
                                .equals(item.getFromDoctorId()))
            {
                tvDocHospital.setText(item.getFromDoctorHospitalName());
                tvDocName.setText("来自：" + item.getFromDoctorName());
            }
            else
            {
                tvDocName.setText("转给：" + item.getToDoctorName());
                tvDocHospital.setText(item.getToDoctorHospitalName());
            }
            switch (item.getAcceptState())
            {
                case TRANSFER_NONE:
                    tvStatus.setText("(待接受)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._F16798));
                    break;
                case TRANSFER_RECV:
                    tvStatus.setText("(待就诊)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._1F6BAC));
                    break;
                case TRANSFER_VISIT:
                    tvStatus.setText("(已就诊)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._000000));
                    break;
                case TRANSFER_CANCEL:
                case TRANSFER_HOSPITAL_CANCEL:
                    tvStatus.setText("(已取消)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._E40505));
                    break;
                case TRANSFER_REFUSE:
                    tvStatus.setText("(已拒绝)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._E40505));
                    break;
            }
        }
    }
}
