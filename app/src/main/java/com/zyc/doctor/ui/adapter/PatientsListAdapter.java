package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.data.BaseData;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.glide.GlideHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dundun on 2018-4-8.
 * 患者列表适配器
 *
 * @author DUNDUN
 */
public class PatientsListAdapter extends BaseRecyclerAdapter<PatientBean>
{
    private Context context;

    public PatientsListAdapter(Context context, List<PatientBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_patients_list, parent, false);
        return new PatientsHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, PatientBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<PatientBean>
    {
        private TextView tvAge, tvSex, tvName;
        private CircleImageView ivHeadImg;
        private ImageView ivSex;
        private LinearLayout lllayout;

        public PatientsHolder(View itemView)
        {
            super(itemView);
            lllayout = itemView.findViewById(R.id.item_patient_list_layout);
            ivHeadImg = itemView.findViewById(R.id.item_patient_list_headimg);
            ivSex = itemView.findViewById(R.id.item_patient_list_sex);
            tvName = itemView.findViewById(R.id.item_patient_list_name);
            tvAge = itemView.findViewById(R.id.item_patient_list_age);
        }

        @Override
        public void showView(final int position, final PatientBean item)
        {
            Glide.with(context)
                 .load(item.getPatientImgUrl())
                 .apply(GlideHelper.getOptionsP())
                 .into(ivHeadImg);
            if (!TextUtils.isEmpty(item.getNickName()) && item.getNickName().length() < BaseData.BASE_NICK_NAME_LENGTH)
            {
                tvName.setText(item.getNickName());
            }
            else
            {
                tvName.setText(item.getName());
            }
            tvAge.setText(AllUtils.getAge(item.getBirthDate()));
            if ("男".equals(item.getSex()))
            {
                lllayout.setSelected(true);
            }
            else
            {
                lllayout.setSelected(false);
            }
        }
    }
}
