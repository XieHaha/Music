package com.zyc.doctor.ui.fragment;

import android.app.Activity;
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
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.data.bean.TransPatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.activity.ApplyPatientActivity;
import com.zyc.doctor.ui.activity.TransferPatientActivity;
import com.zyc.doctor.ui.adapter.TransPatientsListAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.fragment.BaseFragment;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author dundun
 * @date 18/10/11
 * 合作医生转给我的
 */
public class TransferPatientFromFragment extends BaseFragment
        implements LoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseRecyclerAdapter.OnItemClickListener<TransPatientBean> {
    @BindView(R.id.act_patients_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_patients_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
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
    private static final int PAGE_SIZE = 500;
    /**
     * 转诊状态发生改变回调
     */
    public static final int REQUEST_CODE_STATUS_CHANGE = 100;

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_patient_list_notitle;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 0;
        getPatientFromList();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_footerr, null);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        patientsListAdapter = new TransPatientsListAdapter(getContext(), new ArrayList<>());
        patientsListAdapter.addFooterView(footerView);
        autoLoadRecyclerView.setAdapter(patientsListAdapter);
        patientsListAdapter.setOnItemClickListener(this);
    }

    /**
     * 收到转诊申请
     */
    private void getPatientFromList() {
        RequestUtils.getTransferPatientFromList(getContext(), loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), ApplyPatientActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position, TransPatientBean item) {
        Intent intent = new Intent(getContext(), TransferPatientActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, false);
        intent.putExtra(CommonData.KEY_TRANSFER_BEAN, item);
        startActivityForResult(intent, REQUEST_CODE_STATUS_CHANGE);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getPatientFromList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getPatientFromList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_PATIENTS_FROM_LIST:
                patientBeanList = (List<PatientBean>)response.getData();
                if (patientBeanList == null) {
                    patientBeanList = new ArrayList<>();
                }
                if (page == 0) {
                    patientsListAdapter.setList(patientBeanList);
                }
                else {
                    patientsListAdapter.addList(patientBeanList);
                }
                patientsListAdapter.notifyDataSetChanged();
                if (patientBeanList.size() < PAGE_SIZE) {
                    tvFooterHintTxt.setText(R.string.txt_list_none_data_hint);
                    autoLoadRecyclerView.loadFinish(false);
                }
                else {
                    tvFooterHintTxt.setText(R.string.txt_list_push_hint);
                    autoLoadRecyclerView.loadFinish(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (page > 0) {
            page--;
        }
        tvFooterHintTxt.setText(R.string.txt_list_none_data_hint);
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        if (page > 0) {
            page--;
        }
        tvFooterHintTxt.setText(R.string.txt_list_none_data_hint);
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_STATUS_CHANGE:
                getPatientFromList();
                break;
            default:
                break;
        }
    }
}
