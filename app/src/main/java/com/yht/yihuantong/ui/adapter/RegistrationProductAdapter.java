package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yht.yihuantong.R;

import java.util.List;

import custom.frame.bean.HospitalProductBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * 商品列表
 *
 * @author DUNDUN
 */
public class RegistrationProductAdapter extends BaseRecyclerAdapter<HospitalProductBean>
{
    private Context context;

    public RegistrationProductAdapter(Context context, List<HospitalProductBean> list)
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
    public void onBindViewHolder(BaseViewHolder holder, int position, HospitalProductBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class ApplyPatientHolder extends BaseViewHolder<HospitalProductBean>
    {
        private TextView tvProductName, tvProductPrice, tvProductDes;

        public ApplyPatientHolder(View itemView)
        {
            super(itemView);
            //            tvProductName = itemView.findViewById(R.id.item_product_name);
            //            tvProductPrice = itemView.findViewById(R.id.item_product_price);
            //            tvProductDes = itemView.findViewById(R.id.item_product_des);
            tvProductName = itemView.findViewById(R.id.item_registration_name);
        }

        @Override
        public void showView(final int position, final HospitalProductBean item)
        {
            tvProductName.setText(item.getProductName());
            //            tvProductPrice.setText(item.getProductPrice());
            //            tvProductDes.setText(item.getProductDescription());
        }
    }
}
