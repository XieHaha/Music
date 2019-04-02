package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.http.bean.HospitalProductTypeBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 商品类型列表
 *
 * @author DUNDUN
 */
public class RegistrationProductTypeAdapter extends BaseRecyclerAdapter<HospitalProductTypeBean> {
    private Context context;

    public RegistrationProductTypeAdapter(Context context, List<HospitalProductTypeBean> list) {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration, parent, false);
        return new ApplyPatientHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, HospitalProductTypeBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<HospitalProductTypeBean> {
        private TextView tvHospitalName;

        public ApplyPatientHolder(View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.item_registration_name);
        }

        @Override
        public void showView(final int position, final HospitalProductTypeBean item) {
            tvHospitalName.setText(item.getProductTypeName());
        }
    }
}
