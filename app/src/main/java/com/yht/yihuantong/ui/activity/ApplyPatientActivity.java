package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.OnEventTriggerListener;
import com.yht.yihuantong.ui.adapter.ApplyPatientAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 患者申请
 *
 * @author DUNDUN
 */
public class ApplyPatientActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener, OnEventTriggerListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View footerView;
    private TextView tvHintTxt;

    private ApplyPatientAdapter applyPatientAdapter;
    private List<PatientBean> applyPatientList = new ArrayList<>();

    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 20;
    /**
     * 医生端区别字段
     */
    private static final int MODE = 9;

    @Override
    public int getLayoutID() {
        return R.layout.act_apply_patient;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("申请人");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(
                R.id.act_apply_patient_swipe_layout);
        autoLoadRecyclerView = (AutoLoadRecyclerView) findViewById(
                R.id.act_apply_patient_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        footerView = LayoutInflater.from(this)
                .inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);

    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        applyPatientAdapter = new ApplyPatientAdapter(this, applyPatientList);
        applyPatientAdapter.setOnEventTriggerListener(this);
        applyPatientAdapter.addFooterView(footerView);
        page = 0;
        getApplyPatientList();
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(applyPatientAdapter);
        applyPatientAdapter.setOnItemClickListener(
                new BaseRecyclerAdapter.OnItemClickListener<PatientBean>() {
                    @Override
                    public void onItemClick(View v, int position, PatientBean item) {
                        Intent intent = new Intent(ApplyPatientActivity.this,
                                UserInfoActivity.class);
                        startActivity(intent);
                    }
                });
    }

    /**
     * 获取患者申请列表
     */
    private void getApplyPatientList() {
        mIRequest.getApplyPatientList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 拒绝患者申请
     */
    private void refusePatientApply(String patientId) {
        mIRequest.refusePatientApply(loginSuccessBean.getDoctorId(), patientId, MODE, this);
    }

    /**
     * 同意患者申请
     */
    private void agreePatientApply(String patientId,int requestCode) {
        mIRequest.agreePatientApply(loginSuccessBean.getDoctorId(), patientId, requestCode, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_APPLY_PATIENT_LIST:
                applyPatientList = response.getData();
                if (page == 0) {
                    applyPatientAdapter.setList(applyPatientList);
                } else {
                    applyPatientAdapter.addList(applyPatientList);
                }
                applyPatientAdapter.notifyDataSetChanged();

                if (applyPatientList.size() < PAGE_SIZE) {
                    tvHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                } else {
                    tvHintTxt.setText("上拉加载更多");
                    autoLoadRecyclerView.loadFinish(true);
                }
                break;
            case AGREE_PATIENT_APPLY:
            case REFUSE_PATIENT_APPLY:
                ToastUtil.toast(this,"处理成功");
                getApplyPatientList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response) {
        super.onResponseCodeError(task, response);
        switch (task) {
            case GET_APPLY_PATIENT_LIST:
                if (page > 0) {
                    page--;
                }
                tvHintTxt.setText("暂无更多数据");
                autoLoadRecyclerView.loadFinish();
                break;
            case AGREE_PATIENT_APPLY:
                ToastUtil.toast(this,response.getMsg());
                break;
            case REFUSE_PATIENT_APPLY:
                ToastUtil.toast(this,response.getMsg());
                break;
            default:
                break;
        }

    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        switch (task) {
            case GET_APPLY_PATIENT_LIST:
                if (page > 0) {
                    page--;
                }
                tvHintTxt.setText("暂无更多数据");
                autoLoadRecyclerView.loadFinish();
                break;
            case AGREE_PATIENT_APPLY:
                break;
            case REFUSE_PATIENT_APPLY:
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
        getApplyPatientList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getApplyPatientList();
    }

    @Override
    public void onPositiveTrigger(String s,int requestCode) {
        agreePatientApply(s,requestCode);
    }

    @Override
    public void onNegativeTrigger(String s,int requestCode) {
        refusePatientApply(s);
    }
}
