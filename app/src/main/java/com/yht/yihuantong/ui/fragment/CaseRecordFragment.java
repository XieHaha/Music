package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.HealthDetailActivity;
import com.yht.yihuantong.ui.adapter.CaseRecordListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.http.Tasks;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 患者病例
 *
 * @author DUNDUN
 */
public class CaseRecordFragment extends BaseFragment implements LoadMoreListener {
    private LinearLayout llAddNewHealth;
    private AutoLoadRecyclerView autoLoadRecyclerView;

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

    private CaseRecordListAdapter caseRecordListAdapter;

    private List<PatientCaseDetailBean> caseRecordList = new ArrayList<>();

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
        return R.layout.fragment_health_record;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getPatientCaseList();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_health_record_recycler);
        llAddNewHealth = view.findViewById(R.id.fragment_health_record_add);

        footerView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        caseRecordListAdapter = new CaseRecordListAdapter(getContext(), caseRecordList);
        caseRecordListAdapter.addFooterView(footerView);

        if (patientBean != null) {
            patientId = patientBean.getPatientId();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        llAddNewHealth.setOnClickListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(caseRecordListAdapter);
        caseRecordListAdapter.setOnItemClickListener((v, position, item) ->
                                                     {
                                                         Intent intent = new Intent(getContext(), HealthDetailActivity.class);
                                                         intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
                                                         intent.putExtra(CommonData.KEY_PATIENT_ID,item.getPatientId());
                                                         intent.putExtra(CommonData.PATIENT_CASE_DETAIL_BEAN,item);
                                                         startActivity(intent);
                                                     });
    }

    public void setPatientBean(PatientBean patientBean) {
        this.patientBean = patientBean;
    }

    /**
     * 获取患者病例列表
     */
    private void getPatientCaseList() {
        mIRequest.getPatientCaseList(patientId, page, PAGE_SIZE, this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fragment_health_record_add:
                Intent intent = new Intent(getContext(), HealthDetailActivity.class);
                intent.putExtra(CommonData.KEY_ADD_NEW_HEALTH, true);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENT_CASE_LIST:
                if (page == 0) {
                    caseRecordList.clear();
                }
                ArrayList<PatientCaseDetailBean> list = response.getData();
                if (list != null && list.size() > 0) {

                    caseRecordList.addAll(list);
                }
                caseRecordListAdapter.notifyDataSetChanged();

                if (caseRecordList.size() < PAGE_SIZE) {
                    tvHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                } else {
                    tvHintTxt.setText("上拉加载更多");
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
    public void loadMore() {
        page++;
        getPatientCaseList();
    }
}
