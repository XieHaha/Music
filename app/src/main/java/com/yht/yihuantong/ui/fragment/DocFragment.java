package com.yht.yihuantong.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.DocListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

public class DocFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private DocListAdapter docListAdapter;
    private List<String> msgList = new ArrayList<>();

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_doc;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("我的患者");
        swipeRefreshLayout = view.findViewById(R.id.fragment_doc_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_doc_recycler_view);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        for (int i = 0; i < 10; i++)
        {
            msgList.add("消息--" + i);
        }
        docListAdapter = new DocListAdapter(this, msgList);
    }

    @Override
    public void initListener()
    {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(docListAdapter);
        docListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<String>()
        {
            @Override
            public void onItemClick(View v, int position, String item)
            {
            }
        });
    }

    @Override
    public void onRefresh()
    {
    }

    @Override
    public void loadMore()
    {
    }
}
