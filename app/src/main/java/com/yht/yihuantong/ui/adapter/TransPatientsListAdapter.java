package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.OnTransPatientListener;
import com.yht.yihuantong.tools.GlideHelper;

import java.util.List;

import custom.frame.bean.TransPatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 2018-4-8.
 * 患者列表适配器
 *
 * @author DUNDUN
 */
public class TransPatientsListAdapter extends BaseRecyclerAdapter<TransPatientBean>
{
    private Context context;
    private OnTransPatientListener onTransPatientListener;
    private boolean isShow;

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

    public void setOnTransPatientListener(OnTransPatientListener onTransPatientListener)
    {
        this.onTransPatientListener = onTransPatientListener;
    }

    public void setShow(boolean show)
    {
        isShow = show;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, TransPatientBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<TransPatientBean>
    {
        private LinearLayout llLayout;
        private ImageView ivDocHeadImg, ivPatHeadImg;
        private TextView tvDocName, tvDocHospital, tvPatName, tvRefuse, tvAgree;

        public PatientsHolder(View itemView)
        {
            super(itemView);
            llLayout = itemView.findViewById(R.id.item_patient_list_layout);
            llLayout.setVisibility(View.VISIBLE);
            ivDocHeadImg = itemView.findViewById(R.id.item_trans_patient_list_doctor_img);
            ivPatHeadImg = itemView.findViewById(R.id.item_trans_patient_list_patient_img);
            tvDocName = itemView.findViewById(R.id.item_trans_patient_list_doctor_name);
            tvDocHospital = itemView.findViewById(R.id.item_trans_patient_list_doctor_hospital);
            tvPatName = itemView.findViewById(R.id.item_trans_patient_list_patient_name);
            tvRefuse = itemView.findViewById(R.id.item_patient_list_refuse);
            tvAgree = itemView.findViewById(R.id.item_patient_list_agree);
        }

        @Override
        public void showView(final int position, final TransPatientBean item)
        {
            if (isShow)
            {
                llLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                llLayout.setVisibility(View.GONE);
            }
            Glide.with(context)
                 .load(item.getPatientImgUrl())
                 .apply(GlideHelper.getOptionsRect())
                 .into(ivPatHeadImg);
            tvPatName.setText(item.getPatientName());
            Glide.with(context)
                 .load(item.getFromDoctorImgUrl())
                 .apply(GlideHelper.getOptionsRect())
                 .into(ivDocHeadImg);
            tvDocName.setText(item.getFromDoctorName());
            tvDocHospital.setText(item.getFromDoctorHospital());
            tvAgree.setOnClickListener(v ->
                                       {
                                           if (onTransPatientListener != null)
                                           {
                                               onTransPatientListener.onPositiveTrigger(
                                                       item.getFromDoctorId(), item.getPatientId(),
                                                       2);
                                           }
                                       });
            tvRefuse.setOnClickListener(v ->
                                        {
                                            if (onTransPatientListener != null)
                                            {
                                                onTransPatientListener.onNegativeTrigger(
                                                        item.getFromDoctorId(), item.getPatientId(),
                                                        2);
                                            }
                                        });
        }
    }
}
