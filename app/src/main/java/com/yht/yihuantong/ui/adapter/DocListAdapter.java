package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.fragment.DocFragment;

import java.util.HashMap;
import java.util.List;

import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.adapter.BaseViewHolder;

/**
 * Created by dundun on 2018-4-8.
 */
public class DocListAdapter extends BaseRecyclerAdapter<String>
{
    private DocFragment fragment;
    private Context context;
    private HashMap<String, Boolean> mMemoryCache;

    public DocListAdapter(DocFragment fragment, List<String> list)
    {
        super(list);
        this.fragment = fragment;
        context = fragment.getContext();
        mMemoryCache = new HashMap<>();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.item_doc_list, parent, false);
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
        public DynamicHolder(View itemView)
        {
            super(itemView);
        }

        @Override
        public void showView(final int position, final String item)
        {
        }
    }
}
