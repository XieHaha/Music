package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.activity.ApplyCooperateDocActivity;
import com.yht.yihuantong.ui.activity.UserInfoActivity;
import com.yht.yihuantong.ui.adapter.CooperateDocListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 合作医生
 *
 * @author DUNDUN
 */
public class CooperateDocFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private RelativeLayout rlApplyCooperateLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View headerView;
    private CooperateDocListAdapter cooperateDocListAdapter;
    private List<String> msgList = new ArrayList<>();

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_cooperate_doc;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("合作医生");
        swipeRefreshLayout = view.findViewById(R.id.fragment_cooperate_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_cooperate_recycler_view);
        rlApplyCooperateLayout = view.findViewById(R.id.fragment_cooperate_apply_layout);
        headerView = LayoutInflater.from(getContext())
                                   .inflate(R.layout.view_cooperate_doc_header, null);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        for (int i = 0; i < 10; i++)
        {
            msgList.add("消息--" + i);
        }
        cooperateDocListAdapter = new CooperateDocListAdapter(this, msgList);
        cooperateDocListAdapter.addHeaderView(headerView);
    }

    @Override
    public void initListener()
    {
        headerView.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateDocListAdapter);
        cooperateDocListAdapter.setOnItemClickListener(
                new BaseRecyclerAdapter.OnItemClickListener<String>()
                {
                    @Override
                    public void onItemClick(View v, int position, String item)
                    {
                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), ApplyCooperateDocActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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
