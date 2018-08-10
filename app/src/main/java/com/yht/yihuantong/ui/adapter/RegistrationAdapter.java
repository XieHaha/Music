package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yht.yihuantong.R;

import java.util.List;

import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class RegistrationAdapter extends BaseRecyclerAdapter<String>
{
    private Context context;

    public RegistrationAdapter(Context context, List<String> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_registration, parent, false);
        return new ApplyPatientHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, String item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<String>
    {
        private TextView tvHospitalName;

        public ApplyPatientHolder(View itemView)
        {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.item_registration_name);
        }

        @Override
        public void showView(final int position, final String item)
        {
            tvHospitalName.setText(item);
        }
    }
}
