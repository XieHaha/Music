package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.OrderStatus;
import com.yht.yihuantong.utils.AllUtils;

import java.util.ArrayList;

import custom.frame.bean.RegistrationBean;

/**
 * Created by dundun on 18/7/14.
 */
public class OrderInfoAdapter extends BaseAdapter implements OrderStatus, CommonData
{
    private Context context;
    private ArrayList<RegistrationBean> list = new ArrayList<>();
    private boolean showAll = false;

    public OrderInfoAdapter(Context context)
    {
        this.context = context;
    }

    public void setList(ArrayList<RegistrationBean> list)
    {
        this.list = list;
    }

    public void setShowAll(boolean showAll)
    {
        this.showAll = showAll;
    }

    public boolean isShowAll()
    {
        return showAll;
    }

    @Override
    public int getCount()
    {
        if (showAll)
        {
            return list.size();
        }
        //不显示全部 只显示两条
        return ((list.size() > CommonData.DATA_LIST_BASE_NUM)
                ? CommonData.DATA_LIST_BASE_NUM
                : list.size());
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context)
                                        .inflate(R.layout.item_order_info, parent, false);
            holder.tvOrderType = convertView.findViewById(R.id.item_order_type);
            holder.tvOrderStatus = convertView.findViewById(R.id.item_order_status);
            holder.tvOrderPatientName = convertView.findViewById(R.id.item_order_patient_name);
            holder.tvOrderPatientSex = convertView.findViewById(R.id.item_order_patient_sex);
            holder.tvOrderPatientAge = convertView.findViewById(R.id.item_order_patient_age);
            holder.tvOrderDetail = convertView.findViewById(R.id.item_order_detail);
            holder.tvOrderHospital = convertView.findViewById(R.id.item_order_hospital);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        initData(holder, position);
        return convertView;
    }

    private void initData(ViewHolder holder, int position)
    {
        RegistrationBean curRegistrationBean = list.get(position);
        holder.tvOrderType.setText(curRegistrationBean.getProductName());
        switch (curRegistrationBean.getOrderState())
        {
            case STATUS_SUBSCRIBE_NONE:
                holder.tvOrderStatus.setText("未预约");
                break;
            case STATUS_SUBSCRIBE:
                holder.tvOrderStatus.setText("已预约");
                break;
            case STATUS_COMPLETE:
                holder.tvOrderStatus.setText("完成检查");
                break;
            case STATUS_SEND_REPORT:
                holder.tvOrderStatus.setText("报告已发送");
                break;
            case STATUS_REFUSE:
                holder.tvOrderStatus.setText("拒绝");
                break;
        }
        holder.tvOrderPatientName.setText(curRegistrationBean.getPatientName());
        holder.tvOrderPatientSex.setText(curRegistrationBean.getPatientSex());
        holder.tvOrderPatientAge.setText(
                AllUtils.formatDateByAge(curRegistrationBean.getPatientBirthDate()));
        holder.tvOrderDetail.setText(curRegistrationBean.getProductDescription());
        holder.tvOrderHospital.setText(curRegistrationBean.getHospitalName());
    }

    class ViewHolder
    {
        private TextView tvOrderType, tvOrderStatus, tvOrderPatientName, tvOrderPatientSex, tvOrderPatientAge, tvOrderDetail, tvOrderHospital;
    }
}
