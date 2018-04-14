package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.tools.GlideHelper;
import com.yht.yihuantong.ui.fragment.PatientsFragment;

import java.util.List;

import custom.frame.bean.PatientBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 2018-4-8.
 * 患者列表适配器
 *
 * @author DUNDUN
 */
public class PatientsListAdapter extends BaseRecyclerAdapter<PatientBean> {
    private PatientsFragment fragment;
    private Context context;

    public PatientsListAdapter(PatientsFragment fragment, List<PatientBean> list) {
        super(list);
        this.fragment = fragment;
        context = fragment.getContext();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patients_list, parent, false);
        return new PatientsHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, PatientBean item) {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<PatientBean> {
        private TextView tvAge, tvSex, tvName;
        private ImageView ivHeadImg, ivSex;
        private LinearLayout lllayout;

        public PatientsHolder(View itemView) {
            super(itemView);
            lllayout = itemView.findViewById(R.id.item_patient_list_layout);
            ivHeadImg = itemView.findViewById(R.id.item_patient_list_headimg);
            ivSex = itemView.findViewById(R.id.item_patient_list_sex);
            tvName = itemView.findViewById(R.id.item_patient_list_name);
            tvAge = itemView.findViewById(R.id.item_patient_list_age);
        }

        @Override
        public void showView(final int position, final PatientBean item) {
            Glide.with(context).load(item.getPatientImgUrl()).apply(GlideHelper.getOptions()).into(ivHeadImg);

            tvName.setText(item.getName());
            tvAge.setText(item.getAge() + "");
            if ("男".equals(item.getSex())) {
                lllayout.setSelected(true);
            } else {
                lllayout.setSelected(false);
            }
        }
    }
}
