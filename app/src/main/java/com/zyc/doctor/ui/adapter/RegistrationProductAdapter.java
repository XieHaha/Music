package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.HospitalProductBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * ๅๅๅ่กจ
 *
 * @author DUNDUN
 */
public class RegistrationProductAdapter extends BaseRecyclerAdapter<HospitalProductBean> {
    private Context context;

    public RegistrationProductAdapter(Context context, List<HospitalProductBean> list) {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration, parent, false);
        return new ApplyPatientHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, HospitalProductBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<HospitalProductBean> {
        private TextView tvProductName, tvProductPrice, tvProductDes;

        public ApplyPatientHolder(View itemView) {
            super(itemView);
            //            tvProductName = itemView.findViewById(R.id.item_product_name);
            //            tvProductPrice = itemView.findViewById(R.id.item_product_price);
            //            tvProductDes = itemView.findViewById(R.id.item_product_des);
            tvProductName = itemView.findViewById(R.id.item_registration_name);
        }

        @Override
        public void showView(final int position, final HospitalProductBean item) {
            tvProductName.setText(item.getProductName());
            //            tvProductPrice.setText(item.getProductPrice());
            //            tvProductDes.setText(item.getProductDescription());
        }
    }
}
