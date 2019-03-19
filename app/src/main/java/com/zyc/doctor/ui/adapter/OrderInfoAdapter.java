package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.OrderStatus;
import com.zyc.doctor.utils.AllUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import custom.frame.bean.RegistrationBean;
import custom.frame.bean.RegistrationTypeBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 18/7/14.
 * 患者开单记录适配器
 */
public class OrderInfoAdapter extends BaseRecyclerAdapter<RegistrationBean> implements OrderStatus
{
    private Context context;

    public OrderInfoAdapter(Context context, List<RegistrationBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_order_info, parent, false);
        return new OrderInfoAdapter.Transferholder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, RegistrationBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class Transferholder extends BaseViewHolder<RegistrationBean>
    {
        private TextView tvPatientCase, tvCaseType, tvStatus, tvTime;

        public Transferholder(View itemView)
        {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_order_info_time);
            tvPatientCase = itemView.findViewById(R.id.item_order_info_patient_case);
            tvCaseType = itemView.findViewById(R.id.item_order_info_case_type);
            tvStatus = itemView.findViewById(R.id.item_order_info_status);
        }

        @Override
        public void showView(final int position, final RegistrationBean curTransferPatient)
        {
            tvPatientCase.setText(curTransferPatient.getProductName());
            tvTime.setText(AllUtils.formatDate(curTransferPatient.getOrderDate(),
                                               AllUtils.YYYY_MM_DD_HH_MM));
            List<RegistrationTypeBean> list = DataSupport.where("fieldId = ?",
                                                                curTransferPatient.getProductTypeId())
                                                         .find(RegistrationTypeBean.class);
            if (list != null && list.size() > 0)
            {
                RegistrationTypeBean bean = list.get(0);
                tvCaseType.setText("#" + bean.getProductTypeName());
            }
            switch (curTransferPatient.getOrderState())
            {
                case STATUS_SUBSCRIBE_NONE:
                    tvStatus.setText("未确认");
                    tvStatus.setTextColor(
                            ContextCompat.getColor(context, R.color.app_main_txt_color));
                    break;
                case STATUS_SUBSCRIBE:
                    tvStatus.setText("待检查");
                    tvStatus.setTextColor(
                            ContextCompat.getColor(context, R.color.app_main_color));
                    break;
                case STATUS_COMPLETE:
                    tvStatus.setText("已检查");
                    tvStatus.setTextColor(
                            ContextCompat.getColor(context, R.color.app_main_txt_color));
                    break;
                case STATUS_SEND_REPORT:
                    tvStatus.setText("报告已发送");
                    tvStatus.setTextColor(ContextCompat.getColor(context, R.color._1F6BAC));
                    break;
                case STATUS_REFUSE:
                    tvStatus.setText("已拒绝");
                    tvStatus.setTextColor(
                            ContextCompat.getColor(context, R.color.app_red_color));
                    break;
            }
        }
    }
}
