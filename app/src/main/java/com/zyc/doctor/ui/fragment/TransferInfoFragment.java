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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseNetConfig;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.PatientBean;
import com.zyc.doctor.http.bean.TransPatientBean;
import com.zyc.doctor.ui.activity.TransferPatientActivity;
import com.zyc.doctor.ui.adapter.TransferInfoAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.fragment.BaseFragment;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 转诊
 *
 * @author DUNDUN
 */
public class TransferInfoFragment extends BaseFragment implements LoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseRecyclerAdapter.OnItemClickListener<TransPatientBean> {
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
    /**
     * 转诊状态发生改变回调
     */
    public static final int REQUEST_CODE_STATUS_CHANGE = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_info;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTransferInfoList();
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
        transferInfoAdapter = new TransferInfoAdapter(getActivity(), transferPatientBeanList);
        transferInfoAdapter.addFooterView(footerView);
        if (patientBean != null) {
            patientId = patientBean.getPatientId();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(transferInfoAdapter);
        transferInfoAdapter.setOnItemClickListener(this);
    }

    public void setPatientBean(PatientBean patientBean) {
        this.patientBean = patientBean;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * 获取我的转诊记录
     */
    private void getTransferInfoList() {
        mIRequest.getTransferByPatient(loginSuccessBean.getDoctorId(), patientId, page, PAGE_SIZE
                , DAYS_DATA, this);
    }

    @Override
    public void onItemClick(View v, int position, TransPatientBean item) {
        Intent intent = new Intent(getActivity(), TransferPatientActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, false);
        intent.putExtra(CommonData.KEY_TRANSFER_BEAN, item);
        startActivityForResult(intent, REQUEST_CODE_STATUS_CHANGE);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_TRANSFER_BY_PATIENT:
                transferPatientBeanList = (ArrayList<TransPatientBean>)response.getData();
                if (transferPatientBeanList != null && transferPatientBeanList.size() > 0) {
                    llNoneLayout.setVisibility(View.GONE);
                } else {
                    llNoneLayout.setVisibility(View.VISIBLE);
                    tvNoneTxt.setText("还没有转诊记录哦~");
                }
                transferInfoAdapter.setList(transferPatientBeanList);
                transferInfoAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        switch (task) {
            case DELETE_PATIENT_CASE:
                if (BaseNetConfig.CODE_MODIFY_CASE_RECORD == response.getCode()) {
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
        getTransferInfoList();
    }

    @Override
    public void onRefresh() {
        page = 0;
        getTransferInfoList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_STATUS_CHANGE:
                getTransferInfoList();
                break;
            default:
                break;
        }
    }

}
