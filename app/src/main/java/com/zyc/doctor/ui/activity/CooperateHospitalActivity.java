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
import com.zyc.doctor.ui.adapter.CooperateHospitalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.HospitalBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 合作医院
 *
 * @author DUNDUN
 */
public class CooperateHospitalActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener,
        BaseRecyclerAdapter.OnItemClickListener<HospitalBean> {

    @BindView(R.id.act_apply_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_apply_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private View footerView;
    private TextView tvHintTxt;
    private CooperateHospitalAdapter cooperateHospitalAdapter;
    private List<HospitalBean> hospitalBeans = new ArrayList<>();
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
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("合作医院");
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        cooperateHospitalAdapter = new CooperateHospitalAdapter(this, hospitalBeans);
        cooperateHospitalAdapter.addFooterView(footerView);
        page = 0;
        getHospitalListByDoctorId();
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateHospitalAdapter);
        cooperateHospitalAdapter.setOnItemClickListener(this);
    }

    /**
     * 获取医院列表
     */
    private void getHospitalListByDoctorId() {
        mIRequest.getHospitalListByDoctorId(loginSuccessBean.getDoctorId(), this);
    }

    @Override
    public void onItemClick(View v, int position, HospitalBean item) {
        Intent intent = new Intent(this, CooperateDocActivity.class);
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_HOSPITAL_LIST_BY_DOCTORID:
                hospitalBeans = response.getData();
                if (page == 0) {
                    cooperateHospitalAdapter.setList(hospitalBeans);
                } else {
                    cooperateHospitalAdapter.addList(hospitalBeans);
                }
                cooperateHospitalAdapter.notifyDataSetChanged();
                if (hospitalBeans.size() < PAGE_SIZE) {
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
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getHospitalListByDoctorId();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getHospitalListByDoctorId();
    }

}
