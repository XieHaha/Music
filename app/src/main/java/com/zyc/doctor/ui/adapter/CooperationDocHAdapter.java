package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;

import java.util.List;

import custom.frame.bean.CooperateDocBean;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;
import custom.frame.utils.GlideHelper;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dundun on 2018-4-8.
 * 合作医生  水平
 *
 * @author DUNDUN
 */
public class CooperationDocHAdapter extends BaseRecyclerAdapter<CooperateDocBean>
{
    private Context context;

    public CooperationDocHAdapter(Context context, List<CooperateDocBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_cooperation_doc, parent, false);
        return new PatientsHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, CooperateDocBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class PatientsHolder extends BaseViewHolder<CooperateDocBean>
    {
        private TextView tvName, tvHospital;
        private CircleImageView ivHeadImg;

        public PatientsHolder(View convertView)
        {
            super(convertView);
            ivHeadImg = convertView.findViewById(R.id.item_cooperation_doc_headimg);
            tvName = convertView.findViewById(R.id.item_cooperation_doc_name);
            tvHospital = convertView.findViewById(R.id.item_cooperation_doc_hospital);
        }

        @Override
        public void showView(final int position, final CooperateDocBean item)
        {
            CooperateDocBean cooperateDocBean = list.get(position);
            String headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl))
            {
                Glide.with(context)
                     .load(headImgUrl)
                     .apply(GlideHelper.getOptions())
                     .into(ivHeadImg);
            }
            if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) &&
                cooperateDocBean.getNickname().length() < 20)
            {
                tvName.setText(cooperateDocBean.getNickname());
            }
            else
            {
                tvName.setText(cooperateDocBean.getName());
            }
            tvHospital.setText(cooperateDocBean.getHospital());
        }
    }
}
