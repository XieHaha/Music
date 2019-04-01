package com.zyc.doctor.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.utils.GlideHelper;

import java.util.ArrayList;
import java.util.List;

import com.zyc.doctor.http.data.CooperateDocBean;
import com.zyc.doctor.ui.base.adapter.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.adapter.BaseViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 合作医生列表适配器
 *
 * @author DUNDUN
 */
public class CooperateDocListAdapter extends BaseRecyclerAdapter<CooperateDocBean>
{
    private Context context;
    private ArrayList<CooperateDocBean> cooperateDocBeans = new ArrayList<>();

    public CooperateDocListAdapter(Context context, List<CooperateDocBean> list)
    {
        super(list);
        this.context = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_cooperate_list, parent, false);
        return new CooperateDocHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, CooperateDocBean item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class CooperateDocHolder extends BaseViewHolder<CooperateDocBean>
    {
        private CircleImageView ivHeadImg;
        private TextView tvName, tvType, tvHopital;

        public CooperateDocHolder(View itemView)
        {
            super(itemView);
            ivHeadImg = itemView.findViewById(R.id.item_cooperate_list_headimg);
            tvName = itemView.findViewById(R.id.item_cooperate_list_name);
            tvType = itemView.findViewById(R.id.item_cooperate_list_type);
            tvHopital = itemView.findViewById(R.id.item_cooperate_list_hospital);
            tvName = itemView.findViewById(R.id.item_cooperate_list_name);
        }

        @Override
        public void showView(final int position, final CooperateDocBean item)
        {
            Glide.with(context)
                 .load(item.getPortraitUrl())
                 .apply(GlideHelper.getOptions())
                 .into(ivHeadImg);
            if (!TextUtils.isEmpty(item.getNickname()) && item.getNickname().length() < 20)
            {
                tvName.setText(item.getNickname());
            }
            else
            {
                tvName.setText(item.getName());
            }
            tvHopital.setText(item.getHospital());
            tvType.setText(item.getDepartment());
        }
    }
}
