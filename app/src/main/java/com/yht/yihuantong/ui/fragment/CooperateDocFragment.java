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
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.ApplyCooperateDocActivity;
import com.yht.yihuantong.ui.activity.UserInfoActivity;
import com.yht.yihuantong.ui.adapter.CooperateDocListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 合作医生  碎片
 *
 * @author DUNDUN
 */
public class CooperateDocFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {

    private TextView tvHintTxt;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View headerView, footerView;
    private CooperateDocListAdapter cooperateDocListAdapter;
    private List<CooperateDocBean> cooperateDocBeanList = new ArrayList<>();

    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 20;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_cooperate_doc;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.public_title_bar_title)).setText("合作医生");
        swipeRefreshLayout = view.findViewById(R.id.fragment_cooperate_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_cooperate_recycler_view);
        headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_cooperate_doc_header, null);
        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        cooperateDocListAdapter = new CooperateDocListAdapter(getContext(), cooperateDocBeanList);
        cooperateDocListAdapter.addHeaderView(headerView);
        cooperateDocListAdapter.addFooterView(footerView);
        page = 0;
        getCooperateList();
    }

    @Override
    public void initListener() {
        headerView.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateDocListAdapter);
        cooperateDocListAdapter.setOnItemClickListener(
                new BaseRecyclerAdapter.OnItemClickListener<CooperateDocBean>() {
                    @Override
                    public void onItemClick(View v, int position, CooperateDocBean item) {
                        Intent intent = new Intent(getContext(), UserInfoActivity.class);
                        intent.putExtra(CommonData.KEY_DOCTOR_ID,item.getDoctorId());
                        startActivity(intent);
                    }
                });
    }

    /**
     * 获取合作医生列表数据
     */
    private void getCooperateList() {
        mIRequest.getCooperateList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), ApplyCooperateDocActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        getCooperateList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getCooperateList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_COOPERATE_DOC_LIST:
                if (response.getData() != null) {
                    cooperateDocBeanList = response.getData();
                    if (page == 0) {
                        cooperateDocListAdapter.setList(cooperateDocBeanList);
                    } else {
                        cooperateDocListAdapter.addList(cooperateDocBeanList);
                    }
                    cooperateDocListAdapter.notifyDataSetChanged();

                    if (cooperateDocBeanList.size() < PAGE_SIZE) {
                        tvHintTxt.setText("暂无更多数据");
                        autoLoadRecyclerView.loadFinish(false);
                    } else {
                        tvHintTxt.setText("上拉加载更多");
                        autoLoadRecyclerView.loadFinish(true);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response) {
        super.onResponseCodeError(task, response);
        if (page > 0) {
            page--;
        }
        tvHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        if (page > 0) {
            page--;
        }
        tvHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }
}
