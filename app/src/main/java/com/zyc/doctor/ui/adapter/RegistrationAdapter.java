package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 医院列表
 *
 * @author DUNDUN
 */
public class RegistrationAdapter extends BaseRecyclerAdapter<HospitalBean> {
    private Context context;

    public RegistrationAdapter(Context context, List<HospitalBean> list) {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration, parent, false);
        return new ApplyPatientHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, HospitalBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<HospitalBean> {
        private TextView tvHospitalName;

        public ApplyPatientHolder(View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.item_registration_name);
        }

        @Override
        public void showView(final int position, final HospitalBean item) {
            tvHospitalName.setText(item.getHospitalName());
        }
    }
}
