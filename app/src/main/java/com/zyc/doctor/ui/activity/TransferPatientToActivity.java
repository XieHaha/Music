package com.zyc.doctor.ui.activity;

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

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.ui.adapter.TransPatientsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.TransPatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * Created by dundun on 18/10/11.
 * 我转给合作医生的
 */
public class TransferPatientToActivity extends BaseActivity
        implements LoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
        BaseRecyclerAdapter.OnItemClickListener<TransPatientBean> {
    @BindView(R.id.act_patients_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_patients_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TransPatientsListAdapter transPatientsListAdapter;
    private ImageView ivTitleBarMore;
    private View footerView;
    private TextView tvFooterHintTxt;
    private List<TransPatientBean> patientBeanList = new ArrayList<>();
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
        return R.layout.act_transfer_patient_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 0;
        getPatientToList();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("我转出的患者");
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        transPatientsListAdapter = new TransPatientsListAdapter(this, new ArrayList<>());
        transPatientsListAdapter.addFooterView(footerView);
        autoLoadRecyclerView.setAdapter(transPatientsListAdapter);
        transPatientsListAdapter.setOnItemClickListener(this);
    }

    private void getPatientToList() {
        mIRequest.getTransferPatientToList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(this, ApplyPatientActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position, TransPatientBean item) {
        Intent intent = new Intent(this, TransferPatientActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, false);
        intent.putExtra(CommonData.KEY_TRANSFER_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getPatientToList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getPatientToList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_PATIENTS_TO_LIST:
                patientBeanList = response.getData();
                if (page == 0) {
                    transPatientsListAdapter.setList(patientBeanList);
                } else {
                    transPatientsListAdapter.addList(patientBeanList);
                }
                transPatientsListAdapter.notifyDataSetChanged();
                if (patientBeanList.size() < PAGE_SIZE) {
                    tvFooterHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                } else {
                    tvFooterHintTxt.setText("上拉加载更多");
                    autoLoadRecyclerView.loadFinish(true);
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
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        if (page > 0) {
            page--;
        }
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

}
