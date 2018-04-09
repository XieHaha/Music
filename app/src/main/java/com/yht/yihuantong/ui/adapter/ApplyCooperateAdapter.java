package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yht.yihuantong.R;

import java.util.HashMap;
import java.util.List;

import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * 申请合作医生列表适配器
 *
 * @author DUNDUN
 */
public class ApplyCooperateAdapter extends BaseRecyclerAdapter<String>
{
    private Context context;
    private HashMap<String, Boolean> mMemoryCache;

    public ApplyCooperateAdapter(Context context, List<String> list)
    {
        super(list);
        this.context = context;
        mMemoryCache = new HashMap<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_cooperate_list, parent, false);
        return new DynamicHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, String item)
    {
        super.onBindViewHolder(holder, position, item);
        holder.showView(position, item);
    }

    public class DynamicHolder extends BaseViewHolder<String>
    {
        LinearLayout llLayout;

        public DynamicHolder(View itemView)
        {
            super(itemView);
            llLayout = itemView.findViewById(R.id.item_cooperate_list_layout);
            llLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void showView(final int position, final String item)
        {
        }
    }
}
