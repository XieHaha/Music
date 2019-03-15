package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.RegistrationListAdapter;
import com.yht.yihuantong.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.RegistrationBean;
import custom.frame.http.data.BaseNetCode;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 已完成的报告
 *
 * @author DUNDUN
 */
public class RegistrationListActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener,
        BaseRecyclerAdapter.OnItemClickListener<RegistrationBean> {
    private static final String TAG = "RegistrationListActivit";
    @BindView(R.id.fragment_patients_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.fragment_patients_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RegistrationListAdapter registrationListAdapter;
    private View footerView;
    private TextView tvFooterHintTxt;
    private int typeId;
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
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_registration_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 0;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("开单记录");
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        registrationListAdapter = new RegistrationListAdapter(this, registrationBeans);
        autoLoadRecyclerView.setAdapter(registrationListAdapter);
        registrationListAdapter.addFooterView(footerView);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            registrationBeans = (List<RegistrationBean>) getIntent().getSerializableExtra(
                    CommonData.KEY_REGISTRATION_LIST);
        }
        if (registrationBeans != null && registrationBeans.size() > 0) {
            registrationListAdapter.setList(registrationBeans);
            registrationListAdapter.notifyDataSetChanged();
        } else {
            getOrderList();
        }
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        registrationListAdapter.setOnItemClickListener(this);
    }

    /**
     * 开单记录
     */
    private void getOrderList() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/order/doctor/orders/list", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("doctorId", loginSuccessBean.getDoctorId());
        params.put("pageNo", page);
        params.put("pageSize", PAGE_SIZE);
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponseList(object,
                            RegistrationBean.class);
                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
                            registrationBeans = baseResponse.getData();
                            if (page == 0) {
                                registrationListAdapter.setList(registrationBeans);
                            } else {
                                registrationListAdapter.addList(registrationBeans);
                            }
                            registrationListAdapter.notifyDataSetChanged();
                        } else {
                            ToastUtil.toast(RegistrationListActivity.this, baseResponse.getMsg());
                        }
                    }
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(RegistrationListActivity.this,
                        response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onItemClick(View v, int position, RegistrationBean item) {
        Intent intent = new Intent(this, RegistrationDetailActivity.class);
        intent.putExtra(CommonData.KEY_REGISTRATION_BEAN, item);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getOrderList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getOrderList();
    }
}
