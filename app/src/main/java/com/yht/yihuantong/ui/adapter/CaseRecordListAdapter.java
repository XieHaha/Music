package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.AllUtils;

import java.util.List;

import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_case_record, parent, false);
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
            tvTime.setText(AllUtils.formatDate(item.getGmtCreate(), AllUtils.YYYY_MM_DD_HH_MM));
        }
    }
}
