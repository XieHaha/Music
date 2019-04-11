package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;
import com.zyc.doctor.utils.glide.GlideHelper;

import java.util.List;

/**
 * @author dundun
 * @date 2018-4-8
 * 合作医生  水平
 */
public class CooperationHospitalHAdapter extends BaseRecyclerAdapter<HospitalBean> {
    private Context context;

    public CooperationHospitalHAdapter(Context context, List<HospitalBean> list) {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cooperation_hospital, parent, false);
        return new PatientsHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, HospitalBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<HospitalBean> {
        private TextView tvName, tvHospital;
        private ImageView ivHeadImg;

        public PatientsHolder(View convertView) {
            super(convertView);
            ivHeadImg = convertView.findViewById(R.id.item_cooperation_doc_headimg);
            tvName = convertView.findViewById(R.id.item_cooperation_doc_name);
            tvHospital = convertView.findViewById(R.id.item_cooperation_doc_hospital);
        }

        @Override
        public void showView(final int position, final HospitalBean item) {
            HospitalBean cooperateHospitalBean = list.get(position);
            String headImgUrl = item.getImage();
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(context).load(headImgUrl).apply(GlideHelper.getOptionsHospitalPic()).into(ivHeadImg);
            }
            tvName.setText(cooperateHospitalBean.getHospitalName());
        }
    }
}
