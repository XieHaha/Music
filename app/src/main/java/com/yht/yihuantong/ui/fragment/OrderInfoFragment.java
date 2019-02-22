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
import com.yht.yihuantong.ui.activity.RegistrationDetailActivity;
import com.yht.yihuantong.ui.adapter.OrderInfoAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.bean.RegistrationBean;
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
public class OrderInfoFragment extends BaseFragment
        implements LoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseRecyclerAdapter.OnItemClickListener<RegistrationBean>
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
    private OrderInfoAdapter orderInfoAdapter;
    private List<RegistrationBean> registrationBeans = new ArrayList<>();
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
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.fragment_swipe_layout);
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
        orderInfoAdapter = new OrderInfoAdapter(getContext(), registrationBeans);
        orderInfoAdapter.addFooterView(footerView);
        getPatientAllOrders();
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
        autoLoadRecyclerView.setAdapter(orderInfoAdapter);
        orderInfoAdapter.setOnItemClickListener(this);
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
     * 获取患者订单
     */
    private void getPatientAllOrders()
    {
        mIRequest.getPatientOrders(loginSuccessBean.getDoctorId(),patientId, page, PAGE_SIZE, this);
    }

    @Override
    public void onItemClick(View v, int position, RegistrationBean item)
    {
        Intent intent = new Intent(getContext(), RegistrationDetailActivity.class);
        intent.putExtra(CommonData.KEY_REGISTRATION_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENT_ORDER_LIST:
                if (page == 0)
                {
                    registrationBeans.clear();
                }
                ArrayList<RegistrationBean> list = response.getData();
                if (list != null && list.size() > 0)
                {
                    llNoneLayout.setVisibility(View.GONE);
                    registrationBeans.addAll(list);
                }
                else
                {
                    llNoneLayout.setVisibility(View.VISIBLE);
                    tvNoneTxt.setText("还没有开单记录哦~");
                }
                orderInfoAdapter.notifyDataSetChanged();
                if (registrationBeans.size() < PAGE_SIZE)
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
        getPatientAllOrders();
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        getPatientAllOrders();
    }
}
