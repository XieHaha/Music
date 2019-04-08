package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.RegistrationBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.RegistrationListAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 已完成的报告
 *
 * @author DUNDUN
 */
public class RegistrationListActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener,
                   BaseRecyclerAdapter.OnItemClickListener<RegistrationBean> {
    private static final String TAG = "RegistrationListActivit";
    @BindView(R.id.fragment_patients_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.fragment_patients_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private RegistrationListAdapter registrationListAdapter;
    private View footerView;
    private List<RegistrationBean> registrationBeans = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_registration_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 0;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("开单记录");
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        registrationListAdapter = new RegistrationListAdapter(this, registrationBeans);
        autoLoadRecyclerView.setAdapter(registrationListAdapter);
        registrationListAdapter.addFooterView(footerView);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            registrationBeans = (List<RegistrationBean>)getIntent().getSerializableExtra(
                    CommonData.KEY_REGISTRATION_LIST);
        }
        if (registrationBeans != null && registrationBeans.size() > 0) {
            registrationListAdapter.setList(registrationBeans);
            registrationListAdapter.notifyDataSetChanged();
        }
        else {
            getOrderList();
        }
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        registrationListAdapter.setOnItemClickListener(this);
    }

    /**
     * 开单记录
     */
    private void getOrderList() {
        RequestUtils.getOrderList(this, loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onItemClick(View v, int position, RegistrationBean item) {
        Intent intent = new Intent(this, RegistrationDetailActivity.class);
        intent.putExtra(CommonData.KEY_REGISTRATION_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_ORDER_LIST:
                registrationBeans = (List<RegistrationBean>)response.getData();
                if (page == 0) {
                    registrationListAdapter.setList(registrationBeans);
                }
                else {
                    registrationListAdapter.addList(registrationBeans);
                }
                registrationListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getOrderList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getOrderList();
    }
}
