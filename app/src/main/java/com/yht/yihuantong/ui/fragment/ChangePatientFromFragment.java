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
import com.yht.yihuantong.data.OnTransPatientListener;
import com.yht.yihuantong.ui.activity.PatientApplyActivity;
import com.yht.yihuantong.ui.adapter.TransPatientsListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 病人转诊
 * 其他医生转给我
 *
 * @author DUNDUN
 */
public class ChangePatientFromFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener, OnTransPatientListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private TransPatientsListAdapter patientsListAdapter;
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
        getPatientFromList();
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
        patientsListAdapter = new TransPatientsListAdapter(getContext(), new ArrayList<>());
        patientsListAdapter.setOnTransPatientListener(this);
        patientsListAdapter.setShow(true);
        patientsListAdapter.addFooterView(footerView);
        autoLoadRecyclerView.setAdapter(patientsListAdapter);
    }

    private void getPatientFromList()
    {
        mIRequest.getPatientFromList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 拒绝转诊申请
     */
    private void refuseTransPatientApply(String fromDoctorId, String patientId, int requestSource)
    {
        mIRequest.refuseTransPatientApply(loginSuccessBean.getDoctorId(), fromDoctorId, patientId,
                                          requestSource, this);
    }

    /**
     * 同意转诊申请
     */
    private void agreeTransPatientApply(String fromDoctorId, String patientId, int requestSource)
    {
        mIRequest.agreeTransPatientApply(loginSuccessBean.getDoctorId(), fromDoctorId, patientId,
                                         requestSource, this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), PatientApplyActivity.class);
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
        getPatientFromList();
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getPatientFromList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case GET_PATIENTS_FROM_LIST:
                patientBeanList = response.getData();
                if (page == 0)
                {
                    patientsListAdapter.setList(patientBeanList);
                }
                else
                {
                    patientsListAdapter.addList(patientBeanList);
                }
                patientsListAdapter.notifyDataSetChanged();
                if (patientBeanList.size() < PAGE_SIZE)
                {
                    tvFooterHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                }
                else
                {
                    tvFooterHintTxt.setText("上拉加载更多");
                    autoLoadRecyclerView.loadFinish(true);
                }
                break;
            case REFUSE_TARNS_PATIENT_APPLY:
                ToastUtil.toast(getContext(), response.getMsg());
                getPatientFromList();
                break;
            case AGREE_TARNS_PATIENT_APPLY:
                ToastUtil.toast(getContext(), response.getMsg());
                getPatientFromList();
                break;
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

    @Override
    public void onPositiveTrigger(String fromDoctorId, String patientId, int requestCode)
    {
        agreeTransPatientApply(fromDoctorId, patientId, requestCode);
    }

    @Override
    public void onNegativeTrigger(String fromDoctorId, String patientId, int requestCode)
    {
        refuseTransPatientApply(fromDoctorId, patientId, requestCode);
    }
}
