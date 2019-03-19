package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;

import java.util.List;

import custom.frame.bean.CooperateDocBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;
import custom.frame.utils.GlideHelper;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class ApplyCooperateAdapter extends BaseRecyclerAdapter<CooperateDocBean> {
    private Context context;

    public ApplyCooperateAdapter(Context context, List<CooperateDocBean> list) {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_apply_cooperate_list, parent, false);
        return new ApplyCooperateHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, CooperateDocBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyCooperateHolder extends BaseViewHolder<CooperateDocBean> {
        private ImageView ivHeadImg;
        private TextView tvName, tvType, tvHopital;

        public ApplyCooperateHolder(View itemView) {
            super(itemView);
            ivHeadImg = itemView.findViewById(R.id.item_cooperate_list_headimg);
            tvName = itemView.findViewById(R.id.item_cooperate_list_name);
            tvType = itemView.findViewById(R.id.item_cooperate_list_type);
            tvHopital = itemView.findViewById(R.id.item_cooperate_list_hospital);
        }

        @Override
        public void showView(final int position, final CooperateDocBean item) {


            Glide.with(context).load(item.getPortraitUrl()).apply(GlideHelper.getOptions()).into(ivHeadImg);
            tvName.setText(item.getName());
            tvType.setText(item.getDepartment());
            tvHopital.setText(item.getHospital());
        }
    }
}
