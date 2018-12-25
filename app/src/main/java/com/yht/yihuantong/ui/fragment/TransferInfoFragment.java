package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.TransferPatientActivity;
import com.yht.yihuantong.ui.adapter.TransferInfoAdapter;

import java.util.ArrayList;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.http.Tasks;
import custom.frame.http.data.BaseNetCode;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 转诊
 *
 * @author DUNDUN
 */
public class TransferInfoFragment extends BaseFragment
        implements LoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseRecyclerAdapter.OnItemClickListener<TransPatientBean>
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private LinearLayout llNoneLayout;
    private TextView tvNoneTxt;
    private View footerView;
    private TextView tvHintTxt;
    /**
     * 患者 bean
     */
    private PatientBean patientBean;
    /**
     * 患者id
     */
    private String patientId;
    private TransferInfoAdapter transferInfoAdapter;
    /**
     * 转诊信息列表
     */
    private ArrayList<TransPatientBean> transferPatientBeanList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 0  表示不限制
     */
    private static final int DAYS_DATA = 0;

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_info;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getTransferInfoList();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.fragment_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_health_record_recycler);
        llNoneLayout = view.findViewById(R.id.fragment_info_none_layout);
        tvNoneTxt = view.findViewById(R.id.fragment_info_none_txt);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        transferInfoAdapter = new TransferInfoAdapter(getContext(), transferPatientBeanList);
        transferInfoAdapter.addFooterView(footerView);
        if (patientBean != null)
        {
            patientId = patientBean.getPatientId();
        }
    }

    @Override
    public void initListener()
    {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(transferInfoAdapter);
        transferInfoAdapter.setOnItemClickListener(this);
    }

    public void setPatientBean(PatientBean patientBean)
    {
        this.patientBean = patientBean;
    }

    /**
     * 获取我的转诊记录
     */
    private void getTransferInfoList()
    {
        mIRequest.getTransferPatientHistoryList(patientId, page, PAGE_SIZE, DAYS_DATA, this);
    }

    @Override
    public void onItemClick(View v, int position, TransPatientBean item)
    {
        Intent intent = new Intent(getContext(), TransferPatientActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, false);
        intent.putExtra(CommonData.KEY_TRANSFER_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_TRANSFER_PATIENT_HISTORY_LIST:
                transferPatientBeanList = response.getData();
                if (transferPatientBeanList != null && transferPatientBeanList.size() > 0)
                {
                    llNoneLayout.setVisibility(View.GONE);
                }
                else
                {
                    llNoneLayout.setVisibility(View.VISIBLE);
                    tvNoneTxt.setText("还没有健康档案哦~");
                }
                transferInfoAdapter.setList(transferPatientBeanList);
                transferInfoAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        switch (task)
        {
            case GET_PATIENT_CASE_LIST:
                if (page > 0)
                {
                    page--;
                }
                tvHintTxt.setText("暂无更多数据");
                autoLoadRecyclerView.loadFinish();
                break;
            case DELETE_PATIENT_CASE:
                if (BaseNetCode.CODE_MODIFY_CASE_RECORD == response.getCode())
                {
                    ToastUtil.toast(getContext(), response.getMsg());
                }
                break;
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        super.onResponseError(task, e);
        if (page > 0)
        {
            page--;
        }
        tvHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task)
    {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMore()
    {
        page++;
        getTransferInfoList();
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        getTransferInfoList();
    }
}