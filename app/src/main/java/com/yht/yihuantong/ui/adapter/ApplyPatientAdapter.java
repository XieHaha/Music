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
import com.yht.yihuantong.data.OnEventTriggerListener;
import com.yht.yihuantong.tools.GlideHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import custom.frame.bean.PatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class ApplyPatientAdapter extends BaseRecyclerAdapter<PatientBean> {
    private Context context;

    private OnEventTriggerListener onEventTriggerListener;

    public ApplyPatientAdapter(Context context, List<PatientBean> list) {
        super(list);
        this.context = context;
    }

    public void setOnEventTriggerListener(OnEventTriggerListener onEventTriggerListener) {
        this.onEventTriggerListener = onEventTriggerListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_list, parent, false);
        return new ApplyPatientHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, PatientBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<PatientBean> {
        private LinearLayout llLayout;
        private ImageView ivHeadImg;
        private TextView tvName, tvType, tvHopital, tvRefuse, tvAgree, tvPhone;

        public ApplyPatientHolder(View itemView) {
            super(itemView);
            llLayout = itemView.findViewById(R.id.item_patient_list_layout);
            llLayout.setVisibility(View.VISIBLE);
            ivHeadImg = itemView.findViewById(R.id.item_patient_list_headimg);
            tvName = itemView.findViewById(R.id.item_patient_list_name);
            tvRefuse = itemView.findViewById(R.id.item_patient_list_refuse);
            tvAgree = itemView.findViewById(R.id.item_patient_list_agree);
            tvPhone = itemView.findViewById(R.id.item_patient_list_phone);
        }

        @Override
        public void showView(final int position, final PatientBean item) {
            String newUrl = "";
            try {
                newUrl = URLEncoder.encode(item.getPatientImgUrl(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Glide.with(context).load(newUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            tvName.setText(item.getName());
            tvPhone.setText(item.getPhone());
            tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEventTriggerListener != null) {
                        onEventTriggerListener.onPositiveTrigger(item.getPatientId(),item.getRequestSource());
                    }
                }
            });

            tvRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEventTriggerListener != null) {
                        onEventTriggerListener.onNegativeTrigger(item.getPatientId(),0);
                    }
                }
            });
        }
    }
}