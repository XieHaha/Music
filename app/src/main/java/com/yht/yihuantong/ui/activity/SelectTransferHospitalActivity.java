package com.yht.yihuantong.ui.activity;

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

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.SelectHospitalAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateHospitalBean;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 选择接诊医院
 *
 * @author DUNDUN
 */
public class SelectTransferHospitalActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener,
        BaseRecyclerAdapter.OnItemClickListener<CooperateHospitalBean> {
    @BindView(R.id.act_apply_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_apply_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private View footerView;
    private TextView tvHintTxt;
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
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(selectHospitalAdapter);
        selectHospitalAdapter.setOnItemClickListener(this);
    }

    /**
     * 获取合作医院
     */
    private void getCooperateHospitalList() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/hospital/doctor/relation/list",
                RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("doctorId", loginSuccessBean.getDoctorId());
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
                            CooperateHospitalBean.class);
                    if (baseResponse != null && baseResponse.getCode() == 200) {
                        ArrayList<CooperateHospitalBean> list = baseResponse.getData();
                        selectHospitalAdapter.setList(list);
                    } else {
                        ToastUtil.toast(SelectTransferHospitalActivity.this, baseResponse.getMsg());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(SelectTransferHospitalActivity.this,
                        response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    @Override
    public void onItemClick(View v, int position, CooperateHospitalBean item) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, item);
        setResult(RESULT_OK, intent);
        finish();
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
