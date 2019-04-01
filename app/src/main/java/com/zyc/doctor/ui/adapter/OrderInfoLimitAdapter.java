package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.OrderStatus;
import com.zyc.doctor.http.data.RegistrationBean;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.SharePreferenceUtil;

import java.util.ArrayList;

/**
 * @author dundun
 * @date 18/7/14
 */
public class OrderInfoLimitAdapter extends BaseAdapter implements OrderStatus, CommonData {
    private Context context;
    private ArrayList<RegistrationBean> list = new ArrayList<>();
    private SharePreferenceUtil sharePreferenceUtil;
    private boolean showAll = false;

    public OrderInfoLimitAdapter(Context context) {
        this.context = context;
        sharePreferenceUtil = new SharePreferenceUtil(context);
    }

    public void setList(ArrayList<RegistrationBean> list) {
        this.list = list;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public boolean isShowAll() {
        return showAll;
    }

    @Override
    public int getCount() {
        if (showAll) {
            return list.size();
        }
        //不显示全部 只显示两条
        return ((list.size() > CommonData.DATA_LIST_BASE_NUM) ? CommonData.DATA_LIST_BASE_NUM : list.size());
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_limit_info, parent, false);
            holder.tvOrderType = convertView.findViewById(R.id.item_order_type);
            holder.tvOrderStatus = convertView.findViewById(R.id.item_order_status);
            holder.tvOrderPatientName = convertView.findViewById(R.id.item_order_patient_name);
            holder.tvOrderPatientSex = convertView.findViewById(R.id.item_order_patient_sex);
            holder.tvOrderPatientAge = convertView.findViewById(R.id.item_order_patient_age);
            holder.tvOrderDetail = convertView.findViewById(R.id.item_order_detail);
            holder.tvOrderHospital = convertView.findViewById(R.id.item_order_hospital);
            holder.tvTime = convertView.findViewById(R.id.item_order_time);
            holder.rlReadHint = convertView.findViewById(R.id.message_red_point_read);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        initData(holder, position);
        return convertView;
    }

    private void initData(ViewHolder holder, int position) {
        RegistrationBean curRegistrationBean = list.get(position);
        String ids = sharePreferenceUtil.getString(CommonData.KEY_NEW_MESSAGE_REMIND);
        if (!TextUtils.isEmpty(ids) && ids.contains(String.valueOf(curRegistrationBean.getProductOrderId()))) {
            holder.rlReadHint.setVisibility(View.VISIBLE);
        }
        else {
            holder.rlReadHint.setVisibility(View.GONE);
        }
        holder.tvOrderType.setText(curRegistrationBean.getProductName());
        holder.tvTime.setText(AllUtils.formatDate(curRegistrationBean.getOrderDate(), AllUtils.YYYY_MM_DD_HH_MM));
        switch (curRegistrationBean.getOrderState()) {
            case STATUS_SUBSCRIBE_NONE:
                holder.tvOrderStatus.setText("未确认");
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.app_main_txt_color));
                break;
            case STATUS_SUBSCRIBE:
                holder.tvOrderStatus.setText("待检查");
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.app_main_color));
                break;
            case STATUS_COMPLETE:
                holder.tvOrderStatus.setText("已检查");
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.app_main_txt_color));
                break;
            case STATUS_SEND_REPORT:
                holder.tvOrderStatus.setText("报告已发送");
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color._1F6BAC));
                break;
            case STATUS_REFUSE:
                holder.tvOrderStatus.setText("已拒绝");
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.app_red_color));
                break;
        }
        holder.tvOrderPatientName.setText(curRegistrationBean.getPatientName());
        holder.tvOrderPatientSex.setText(curRegistrationBean.getPatientSex());
        holder.tvOrderPatientAge.setText(AllUtils.getAge(curRegistrationBean.getPatientBirthDate()) + "岁");
        holder.tvOrderDetail.setText(curRegistrationBean.getDiagnosisInfo());
        holder.tvOrderHospital.setText(curRegistrationBean.getHospitalName());
    }

    class ViewHolder {
        private TextView tvOrderType, tvOrderStatus, tvOrderPatientName, tvOrderPatientSex, tvOrderPatientAge, tvOrderDetail, tvOrderHospital, tvTime;
        private RelativeLayout rlReadHint;
    }
}
