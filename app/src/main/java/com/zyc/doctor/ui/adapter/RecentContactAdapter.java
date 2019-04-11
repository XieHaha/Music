package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;
import com.zyc.doctor.utils.glide.GlideHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dundun on 2018-4-8.
 * 患者  水平
 *
 * @author DUNDUN
 */
public class RecentContactAdapter extends BaseRecyclerAdapter<PatientBean>
{
    private Context context;

    public RecentContactAdapter(Context context, List<PatientBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_recent_contact, parent, false);
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
        private TextView tvName;
        private CircleImageView ivHeadImg;

        public PatientsHolder(View convertView)
        {
            super(convertView);
            ivHeadImg = convertView.findViewById(R.id.item_cooperation_doc_headimg);
            tvName = convertView.findViewById(R.id.item_cooperation_doc_name);
        }

        @Override
        public void showView(final int position, final PatientBean item)
        {
            PatientBean patientBean = list.get(position);
            Glide.with(context)
                 .load(patientBean.getPatientImgUrl())
                 .apply(GlideHelper.getOptionsP())
                 .into(ivHeadImg);
            tvName.setText(patientBean.getName());
        }
    }
}
