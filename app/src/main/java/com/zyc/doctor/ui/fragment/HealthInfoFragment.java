package com.zyc.doctor.ui.fragment;

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

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.data.BaseNetCode;
import com.zyc.doctor.http.data.BaseResponse;
import com.zyc.doctor.http.data.CooperateDocBean;
import com.zyc.doctor.http.data.PatientBean;
import com.zyc.doctor.http.data.PatientCaseDetailBean;
import com.zyc.doctor.ui.activity.HealthDetailActivity;
import com.zyc.doctor.ui.adapter.HealthInfoAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.fragment.BaseFragment;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 患者病例
 *
 * @author DUNDUN
 */
public class HealthInfoFragment extends BaseFragment implements LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseRecyclerAdapter.OnItemClickListener<PatientCaseDetailBean>,
        BaseRecyclerAdapter.OnItemLongClickListener<PatientCaseDetailBean> {
    @BindView(R.id.fragment_health_record_recycler)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.fragment_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fragment_info_none_txt)
    TextView tvNoneTxt;
    @BindView(R.id.fragment_info_none_layout)
    LinearLayout llNoneLayout;
    private View footerView;
    private TextView tvHintTxt;
    /**
     * 患者 bean
     */
    private PatientBean patientBean;
    private CooperateDocBean cooperateDocBean;
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
    public int getLayoutID() {
        return R.layout.fragment_info;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPatientLimitCaseList();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (patientBean != null) {
            patientId = patientBean.getPatientId();
        }
        healthInfoAdapter = new HealthInfoAdapter(getActivity(), caseRecordList);
        healthInfoAdapter.setPatientId(patientId);
        healthInfoAdapter.addFooterView(footerView);
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(healthInfoAdapter);
        healthInfoAdapter.setOnItemClickListener(this);
        healthInfoAdapter.setOnItemLongClickListener(this);
    }

    public void setPatientBean(PatientBean patientBean) {
        this.patientBean = patientBean;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * 获取患者病例列表  只能查看当前医生自己开的健康档案
     */
    private void getPatientLimitCaseList() {
        mIRequest.getPatientLimitCaseList(loginSuccessBean.getDoctorId(), patientId, this);
    }

    /**
     * 删除患者病例列表
     */
    private void deletePatientCaseList(PatientCaseDetailBean bean) {
        mIRequest.deletePatientCase(patientId, bean.getFieldId(), bean.getCaseCreatorId(),
                loginSuccessBean.getDoctorId(), this);
    }

    @Override
    public void onItemClick(View v, int position, PatientCaseDetailBean item) {
        Intent intent = new Intent(getActivity(), HealthDetailActivity.class);
        intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
        intent.putExtra(CommonData.KEY_PATIENT_ID, item.getPatientId());
        intent.putExtra(CommonData.PATIENT_CASE_DETAIL_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View v, int position, PatientCaseDetailBean item) {
        new SimpleDialog(getActivity(), "删除当前病例", (dialog, which) -> {
            deletePatientCaseList(caseRecordList.get(position));
        }, (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENT_LIMIT_CASE_LIST:
                if (page == 0) {
                    caseRecordList.clear();
                }
                ArrayList<PatientCaseDetailBean> list1 = response.getData();
                if (list1 != null && list1.size() > 0) {
                    llNoneLayout.setVisibility(View.GONE);
                    caseRecordList.addAll(list1);
                } else {
                    llNoneLayout.setVisibility(View.VISIBLE);
                    tvNoneTxt.setText("还没有健康档案哦~");
                }
                healthInfoAdapter.notifyDataSetChanged();
                if (caseRecordList.size() < PAGE_SIZE) {
                    tvHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                } else {
                    tvHintTxt.setText("上拉加载更多");
                    autoLoadRecyclerView.loadFinish(true);
                }
                break;
            case DELETE_PATIENT_CASE:
                ToastUtil.toast(getActivity(), response.getMsg());
                getPatientLimitCaseList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response) {
        super.onResponseCodeError(task, response);
        switch (task) {
            case GET_PATIENT_LIMIT_CASE_LIST:
                if (page > 0) {
                    page--;
                }
                tvHintTxt.setText("暂无更多数据");
                autoLoadRecyclerView.loadFinish();
                break;
            case DELETE_PATIENT_CASE:
                if (BaseNetCode.CODE_MODIFY_CASE_RECORD == response.getCode()) {
                    ToastUtil.toast(getActivity(), response.getMsg());
                }
                break;
            default:
                break;
        }
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

    @Override
    public void loadMore() {
        page++;
        getPatientLimitCaseList();
    }

    @Override
    public void onRefresh() {
        page = 0;
        getPatientLimitCaseList();
    }

}
