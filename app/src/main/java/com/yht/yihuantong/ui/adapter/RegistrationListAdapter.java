package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.OrderStatus;
import com.yht.yihuantong.utils.AllUtils;

import java.util.List;

import custom.frame.bean.RegistrationBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 2018-4-8.
 * 患者列表适配器
 *
 * @author DUNDUN
 */
public class RegistrationListAdapter extends BaseRecyclerAdapter<RegistrationBean>
        implements OrderStatus
{
    private Context context;

    public RegistrationListAdapter(Context context, List<RegistrationBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_order, parent, false);
        return new PatientsHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, RegistrationBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<RegistrationBean>
    {
        private TextView tvOrderType, tvOrderPrice, tvOrderStatus, tvOrderPatientName, tvOrderPatientSex, tvOrderPatientAge, tvOrderDetail, tvOrderHospital;

        public PatientsHolder(View view)
        {
            super(view);
            tvOrderType = view.findViewById(R.id.item_order_type);
            tvOrderPrice = view.findViewById(R.id.item_order_price);
            tvOrderStatus = view.findViewById(R.id.item_order_status);
            tvOrderPatientName = view.findViewById(R.id.item_order_patient_name);
            tvOrderPatientSex = view.findViewById(R.id.item_order_patient_sex);
            tvOrderPatientAge = view.findViewById(R.id.item_order_patient_age);
            tvOrderDetail = view.findViewById(R.id.item_order_detail);
            tvOrderHospital = view.findViewById(R.id.item_order_hospital);
        }

        @Override
        public void showView(final int position, final RegistrationBean item)
        {
            tvOrderType.setText(item.getProductName());
            tvOrderPrice.setText(item.getProductPrice() + item.getProductPriceUnit());
            switch (item.getOrderState())
            {
                case STATUS_SUBSCRIBE_NONE:
                    tvOrderStatus.setText("未预约");
                    break;
                case STATUS_SUBSCRIBE:
                    tvOrderStatus.setText("已预约");
                    break;
                case STATUS_COMPLETE:
                    tvOrderStatus.setText("完成检查");
                    break;
                case STATUS_SEND_REPORT:
                    tvOrderStatus.setText("报告已发送");
                    break;
                case STATUS_REFUSE:
                    tvOrderStatus.setText("拒绝");
                    break;
            }
            tvOrderPatientName.setText(item.getPatientName());
            tvOrderPatientSex.setText(item.getPatientSex());
            tvOrderPatientAge.setText(
                    AllUtils.formatDate(item.getOrderDate(), AllUtils.YYYY_MM_DD_HH_MM));
            tvOrderDetail.setText(item.getProductDescription());
            tvOrderHospital.setText(item.getHospitalName());
        }
    }
}
