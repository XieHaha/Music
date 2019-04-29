package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.PatientCaseDetailBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;
import com.zyc.doctor.utils.BaseUtils;

import java.util.List;

/**
 * 患者病例列表适配器
 *
 * @author DUNDUN
 */
public class CaseRecordListAdapter extends BaseRecyclerAdapter<PatientCaseDetailBean> {
    private Context context;

    public CaseRecordListAdapter(Context context, List<PatientCaseDetailBean> list) {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_case_record, parent, false);
        return new PatientCaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, PatientCaseDetailBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientCaseHolder extends BaseViewHolder<PatientCaseDetailBean> {
        private TextView tvContent, tvTime;

        public PatientCaseHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.item_case_record_content);
            tvTime = itemView.findViewById(R.id.item_case_record_time);
        }

        @Override
        public void showView(final int position, final PatientCaseDetailBean item) {
            tvContent.setText(item.getDiagnosisInfo());
            tvTime.setText(BaseUtils.formatDate(item.getGmtCreate(), BaseUtils.YYYY_MM_DD_HH_MM));
        }
    }
}
