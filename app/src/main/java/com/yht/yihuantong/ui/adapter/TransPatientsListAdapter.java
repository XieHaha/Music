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
            if (!YihtApplication.getInstance().getLoginSuccessBean().getDoctorId().equals(item.getFromDoctorId()))
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
                case 0:
                    tvStatus.setText("(待确认)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._F16798));
                    break;
                case 1:
                    tvStatus.setText("(未就诊)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._1F6BAC));
                    break;
                case 2:
                    tvStatus.setText("(已就诊)");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._000000));
                    break;
            }
        }
    }
}
