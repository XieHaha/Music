package com.zyc.doctor.ui.adapter;

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
 * 合作医院列表适配器
 *
 * @author DUNDUN
 */
public class CooperateHospitalAdapter extends BaseRecyclerAdapter<HospitalBean> {
    public CooperateHospitalAdapter(List<HospitalBean> list) {
        super(list);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_hospital, parent, false);
        return new ApplyCooperateHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, HospitalBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyCooperateHolder extends BaseViewHolder<HospitalBean> {
        private TextView tvHospitalName, tvHospitalType;

        private ApplyCooperateHolder(View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.item_registration_name);
            tvHospitalType = itemView.findViewById(R.id.item_registration_type);
        }

        @Override
        public void showView(final int position, final HospitalBean item) {
            tvHospitalName.setText(item.getHospitalName());
            switch (item.getRelationshipId()) {
                case 1:
                    tvHospitalType.setText("执业医院");
                    break;
                case 2:
                    tvHospitalType.setText("合作医院");
                    break;
                default:
                    tvHospitalType.setText("执业医院");
                    break;
            }
        }
    }
}
