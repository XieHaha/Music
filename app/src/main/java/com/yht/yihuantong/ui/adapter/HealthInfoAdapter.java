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
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * 患者病例列表适配器
 *
 * @author DUNDUN
 */
public class HealthInfoAdapter extends BaseRecyclerAdapter<PatientCaseDetailBean>
{
    private Context context;
    private String patientId;

    public HealthInfoAdapter(Context context, List<PatientCaseDetailBean> list)
    {
        super(list);
        this.context = context;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_health_info, parent, false);
        return new PatientCaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, PatientCaseDetailBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientCaseHolder extends BaseViewHolder<PatientCaseDetailBean>
    {
        private TextView tvContent, tvDoctorName, tvTime;
        private LinearLayout llCreatorLayout;

        public PatientCaseHolder(View itemView)
        {
            super(itemView);
            tvContent = itemView.findViewById(R.id.item_health_info_title);
            tvDoctorName = itemView.findViewById(R.id.item_health_info_doctor_name);
            tvTime = itemView.findViewById(R.id.item_health_info_time);
            llCreatorLayout = itemView.findViewById(R.id.item_health_info_creator_layout);
        }

        @Override
        public void showView(final int position, final PatientCaseDetailBean item)
        {
            tvContent.setText(item.getDiagnosisInfo());
            //            if (item.getCaseCreatorId().equals(patientId))
            //            {
            //                llCreatorLayout.setVisibility(View.INVISIBLE);
            //            }
            //            else
            //            {
            //                llCreatorLayout.setVisibility(View.VISIBLE);
            //            }
            tvDoctorName.setText(item.getCreatorName());
            tvTime.setText(AllUtils.formatDate(item.getGmtCreate(), AllUtils.YYYY_MM_DD_HH_MM));
        }
    }
}
