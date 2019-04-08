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
import com.zyc.doctor.data.bean.CooperateHospitalBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.SelectHospitalAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 选择接诊医院
 *
 * @author DUNDUN
 */
public class SelectTransferHospitalActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener,
                   BaseRecyclerAdapter.OnItemClickListener<CooperateHospitalBean> {
    private static final String TAG = "SelectTransferHospitalA";
    @BindView(R.id.act_apply_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_apply_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private View footerView;
    private SelectHospitalAdapter selectHospitalAdapter;
    private List<CooperateHospitalBean> hospitalBeans = new ArrayList<>();
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
        return R.layout.act_cooperate_hospital;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("合作医院");
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        selectHospitalAdapter = new SelectHospitalAdapter(this, hospitalBeans);
        selectHospitalAdapter.addFooterView(footerView);
        page = 0;
        getCooperateHospitalList();
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(selectHospitalAdapter);
        selectHospitalAdapter.setOnItemClickListener(this);
    }

    /**
     * 获取合作医院
     */
    private void getCooperateHospitalList() {
        RequestUtils.getCooperateHospitalList(this, loginSuccessBean.getDoctorId(), this);
    }

    @Override
    public void onItemClick(View v, int position, CooperateHospitalBean item) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, item);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_COOPERATE_HOSPITAL_LIST:
                ArrayList<CooperateHospitalBean> list = (ArrayList<CooperateHospitalBean>)response.getData();
                selectHospitalAdapter.setList(list);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        getCooperateHospitalList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getCooperateHospitalList();
    }
}
