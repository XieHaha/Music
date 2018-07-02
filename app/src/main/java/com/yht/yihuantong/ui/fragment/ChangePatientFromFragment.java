package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.activity.ApplyPatientActivity;
import com.yht.yihuantong.ui.adapter.PatientsListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 病人转诊
 * 其他医生转给我
 *
 * @author DUNDUN
 */
public class ChangePatientFromFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private PatientsListAdapter patientsListAdapter;
    private ImageView ivTitleBarMore;
    private View footerView;
    private TextView tvFooterHintTxt;
    private List<PatientBean> patientBeanList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 20;

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_change;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        page = 0;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.fragment_patients_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_patients_recycler_view);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_footerr, null);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
    }

    @Override
    public void initListener()
    {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(patientsListAdapter);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), ApplyPatientActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh()
    {
        page = 0;
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        if (page > 0)
        {
            page--;
        }
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        super.onResponseError(task, e);
        if (page > 0)
        {
            page--;
        }
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task)
    {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }
}
