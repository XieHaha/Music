package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;

import java.util.List;

import com.zyc.doctor.http.data.CooperateHospitalBean;
import com.zyc.doctor.ui.base.adapter.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.adapter.BaseViewHolder;

/**
 * 合作医院列表适配器
 *
 * @author DUNDUN
 */
public class SelectHospitalAdapter extends BaseRecyclerAdapter<CooperateHospitalBean>
{
    private Context context;

    public SelectHospitalAdapter(Context context, List<CooperateHospitalBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_select_hospital, parent, false);
        return new ApplyCooperateHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, CooperateHospitalBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyCooperateHolder extends BaseViewHolder<CooperateHospitalBean>
    {
        private TextView tvHospitalName, tvHospitalType;

        public ApplyCooperateHolder(View itemView)
        {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.item_registration_name);
            tvHospitalType = itemView.findViewById(R.id.item_registration_type);
        }

        @Override
        public void showView(final int position, final CooperateHospitalBean item)
        {
            tvHospitalName.setText(item.getHospitalName());
            switch (item.getRelationshipId())
            {
                case 1://执业医院
                    tvHospitalType.setText("执业医院");
                    break;
                case 2://合作医院
                    tvHospitalType.setText("合作医院");
                    break;
            }
        }
    }
}
