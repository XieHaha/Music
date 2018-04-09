package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.ApplyCooperateAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 申请合作的医生
 *
 * @author DUNDUN
 */
public class ApplyCooperateDocActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private ApplyCooperateAdapter applyCooperateAdapter;
    private List<String> msgList = new ArrayList<>();

    @Override
    public int getLayoutID()
    {
        return R.layout.act_apply_cooperate;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("申请人");
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(
                R.id.act_apply_cooperate_swipe_layout);
        autoLoadRecyclerView = (AutoLoadRecyclerView)findViewById(
                R.id.act_apply_cooperate_recycler_view);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        for (int i = 0; i < 10; i++)
        {
            msgList.add("消息--" + i);
        }
        applyCooperateAdapter = new ApplyCooperateAdapter(this, msgList);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(applyCooperateAdapter);
        applyCooperateAdapter.setOnItemClickListener(
                new BaseRecyclerAdapter.OnItemClickListener<String>()
                {
                    @Override
                    public void onItemClick(View v, int position, String item)
                    {
                        Intent intent = new Intent(ApplyCooperateDocActivity.this,
                                                   UserInfoActivity.class);
                        startActivity(intent);
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
