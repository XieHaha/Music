package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.data.bean.CooperateHospitalDocBean;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.adapter.base.BaseViewHolder;
import com.zyc.doctor.utils.glide.GlideHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 合作医院下属医生列表适配器
 *
 * @author DUNDUN
 */
public class CooperateHospitalDocListAdapter extends BaseRecyclerAdapter<CooperateHospitalDocBean>
{
    private Context context;
    private ArrayList<CooperateHospitalDocBean> cooperateDocBeans = new ArrayList<>();

    public CooperateHospitalDocListAdapter(Context context, List<CooperateHospitalDocBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_cooperate_hospital_doc, parent, false);
        return new CooperateDocHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, CooperateHospitalDocBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class CooperateDocHolder extends BaseViewHolder<CooperateHospitalDocBean>
    {
        private CircleImageView ivHeadImg;
        private TextView tvName, tvType, tvTitle, tvHopital, tvDocType;

        public CooperateDocHolder(View itemView)
        {
            super(itemView);
            ivHeadImg = itemView.findViewById(R.id.item_cooperate_list_headimg);
            tvName = itemView.findViewById(R.id.item_cooperate_list_name);
            tvType = itemView.findViewById(R.id.item_cooperate_list_type);
            tvTitle = itemView.findViewById(R.id.item_cooperate_list_title);
            tvDocType = itemView.findViewById(R.id.item_cooperate_list_doc_type);
            tvHopital = itemView.findViewById(R.id.item_cooperate_list_hospital);
            tvName = itemView.findViewById(R.id.item_cooperate_list_name);
        }

        @Override
        public void showView(final int position, final CooperateHospitalDocBean item)
        {
            Glide.with(context)
                 .load(item.getPortraitUrl())
                 .apply(GlideHelper.getOptions())
                 .into(ivHeadImg);
            tvName.setText(item.getName());
            tvHopital.setText(item.getPlatformHospitalName());
            tvType.setText(item.getDepartment());
            tvTitle.setText(item.getTitle());
            if (1 == item.getRelationId())
            {
                tvDocType.setText("执业");
            }
            else
            {
                tvDocType.setText("合作");
            }
        }
    }
}
