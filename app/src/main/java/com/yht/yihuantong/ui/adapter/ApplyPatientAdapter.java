package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;

import java.util.List;

import custom.frame.bean.PatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;
import custom.frame.utils.GlideHelper;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class ApplyPatientAdapter extends BaseRecyclerAdapter<PatientBean>
{
    private Context context;

    public ApplyPatientAdapter(Context context, List<PatientBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_apply_patient_list, parent, false);
        return new ApplyPatientHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, PatientBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<PatientBean>
    {
        private ImageView ivHeadImg;
        private TextView tvName, tvPhone;

        public ApplyPatientHolder(View itemView)
        {
            super(itemView);
            ivHeadImg = itemView.findViewById(R.id.item_patient_list_headimg);
            tvName = itemView.findViewById(R.id.item_patient_list_name);
            tvPhone = itemView.findViewById(R.id.item_patient_list_phone);
        }

        @Override
        public void showView(final int position, final PatientBean item)
        {
            Glide.with(context)
                 .load(item.getPatientImgUrl())
                 .apply(GlideHelper.getOptions())
                 .into(ivHeadImg);
            tvName.setText(item.getName());
            tvPhone.setText(item.getPhone());
        }
    }
}
