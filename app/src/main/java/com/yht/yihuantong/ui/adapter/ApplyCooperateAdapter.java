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

import custom.frame.bean.CooperateDocBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class ApplyCooperateAdapter extends BaseRecyclerAdapter<CooperateDocBean> {
    private Context context;
    private OnEventTriggerListener onEventTriggerListener;

    public ApplyCooperateAdapter(Context context, List<CooperateDocBean> list) {
        super(list);
        this.context = context;
    }

    public void setOnEventTriggerListener(OnEventTriggerListener onEventTriggerListener) {
        this.onEventTriggerListener = onEventTriggerListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cooperate_list, parent, false);
        return new ApplyCooperateHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, CooperateDocBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyCooperateHolder extends BaseViewHolder<CooperateDocBean> {
        LinearLayout llLayout;
        private ImageView ivHeadImg;
        private TextView tvName, tvType, tvHopital, tvRefuse, tvAgree, tvPhone;

        public ApplyCooperateHolder(View itemView) {
            super(itemView);
            llLayout = itemView.findViewById(R.id.item_cooperate_list_layout);
            llLayout.setVisibility(View.VISIBLE);
            ivHeadImg = itemView.findViewById(R.id.item_cooperate_list_headimg);
            tvName = itemView.findViewById(R.id.item_cooperate_list_name);
            tvRefuse = itemView.findViewById(R.id.item_cooperate_list_refuse);
            tvAgree = itemView.findViewById(R.id.item_cooperate_list_agree);
            tvType = itemView.findViewById(R.id.item_cooperate_list_type);
            tvHopital = itemView.findViewById(R.id.item_cooperate_list_hospital);
        }

        @Override
        public void showView(final int position, final CooperateDocBean item) {

            String newUrl = "";
            try {
                newUrl = URLEncoder.encode(item.getPortraitUrl(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Glide.with(context).load(newUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            tvName.setText(item.getName());
            tvType.setText(item.getDepartment());
            tvHopital.setText(item.getHospital());
            tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEventTriggerListener != null) {
                        onEventTriggerListener.onPositiveTrigger(item.getDoctorId(),item.getRequestSource());
                    }
                }
            });

            tvRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEventTriggerListener != null) {
                        onEventTriggerListener.onNegativeTrigger(item.getDoctorId(),item.getRequestSource());
                    }
                }
            });
        }
    }
}
