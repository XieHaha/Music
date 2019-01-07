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
import com.yht.yihuantong.ui.activity.HealthDetailActivity;
import com.yht.yihuantong.ui.adapter.HealthInfoAdapter;
import com.yht.yihuantong.ui.dialog.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.http.Tasks;
import custom.frame.http.data.BaseNetCode;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 患者病例
 *
 * @author DUNDUN
 */
public class HealthInfoFragment extends BaseFragment
        implements LoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseRecyclerAdapter.OnItemClickListener<PatientCaseDetailBean>,
                   BaseRecyclerAdapter.OnItemLongClickListener<PatientCaseDetailBean>
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
    private HealthInfoAdapter healthInfoAdapter;
    private List<PatientCaseDetailBean> caseRecordList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_info;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPatientCaseList();
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
        if (patientBean != null)
        {
            patientId = patientBean.getPatientId();
        }
        healthInfoAdapter = new HealthInfoAdapter(getContext(), caseRecordList);
        healthInfoAdapter.setPatientId(patientId);
        healthInfoAdapter.addFooterView(footerView);
    }

    @Override
    public void initListener()
    {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(healthInfoAdapter);
        healthInfoAdapter.setOnItemClickListener(this);
        healthInfoAdapter.setOnItemLongClickListener(this);
    }

    public void setPatientBean(PatientBean patientBean)
    {
        this.patientBean = patientBean;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    /**
     * 获取患者病例列表
     */
    private void getPatientCaseList()
    {
        mIRequest.getPatientCaseList(patientId, this);
    }

    /**
     * 删除患者病例列表
     */
    private void deletePatientCaseList(PatientCaseDetailBean bean)
    {
        mIRequest.deletePatientCase(patientId, bean.getFieldId(), bean.getCaseCreatorId(),
                                    loginSuccessBean.getDoctorId(), this);
    }

    @Override
    public void onItemClick(View v, int position, PatientCaseDetailBean item)
    {
        Intent intent = new Intent(getContext(), HealthDetailActivity.class);
        intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
        intent.putExtra(CommonData.KEY_PATIENT_ID, item.getPatientId());
        intent.putExtra(CommonData.PATIENT_CASE_DETAIL_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View v, int position, PatientCaseDetailBean item)
    {
        new SimpleDialog(getActivity(), "删除当前病例", (dialog, which) ->
        {
            deletePatientCaseList(caseRecordList.get(position));
        }, (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENT_CASE_LIST:
                if (page == 0)
                {
                    caseRecordList.clear();
                }
                ArrayList<PatientCaseDetailBean> list = response.getData();
                if (list != null && list.size() > 0)
                {
                    llNoneLayout.setVisibility(View.GONE);
                    caseRecordList.addAll(list);
                }
                else
                {
                    llNoneLayout.setVisibility(View.VISIBLE);
                    tvNoneTxt.setText("还没有健康档案哦~");
                }
                healthInfoAdapter.notifyDataSetChanged();
                if (caseRecordList.size() < PAGE_SIZE)
                {
                    tvHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                }
                else
                {
                    tvHintTxt.setText("上拉加载更多");
                    autoLoadRecyclerView.loadFinish(true);
                }
                break;
            case DELETE_PATIENT_CASE:
                ToastUtil.toast(getContext(), response.getMsg());
                getPatientCaseList();
                break;
            default:
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
        getPatientCaseList();
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        getPatientCaseList();
    }
}
