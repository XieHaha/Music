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
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.ui.adapter.ApplyCooperateAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 申请合作的医生
 *
 * @author DUNDUN
 */
public class ApplyCooperateDocActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {
    @BindView(R.id.act_apply_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_apply_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private View footerView;
    private TextView tvHintTxt;
    private ApplyCooperateAdapter applyCooperateAdapter;
    private List<CooperateDocBean> applyCooperateList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 操作返回码
     */
    private static final int REQUEST_CODE_APPLY = 100;

    @Override
    public int getLayoutID() {
        return R.layout.act_apply_cooperate;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("医生申请");
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
        applyCooperateAdapter = new ApplyCooperateAdapter(this, applyCooperateList);
        applyCooperateAdapter.addFooterView(footerView);
        page = 0;
        getApplyCooperateList();
    }

    @Override
    public void initListener() {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(applyCooperateAdapter);
        applyCooperateAdapter.setOnItemClickListener((v, position, item) ->
        {
            Intent intent = new Intent(
                    ApplyCooperateDocActivity.this,
                    AddFriendsDocActivity.class);
            intent.putExtra(CommonData.KEY_DOCTOR_ID,
                    item.getDoctorId());
            intent.putExtra(CommonData.KEY_PUBLIC,
                    false);
            intent.putExtra("requestSource",
                    item.getRequestSource());
            startActivityForResult(intent,
                    REQUEST_CODE_APPLY);
        });
    }

    /**
     * 获取申请合作医生列表数据
     */
    private void getApplyCooperateList() {
        mIRequest.getApplyCooperateList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_APPLY_COOPERATE_DOC_LIST:
                applyCooperateList = response.getData();
                if (page == 0) {
                    applyCooperateAdapter.setList(applyCooperateList);
                } else {
                    applyCooperateAdapter.addList(applyCooperateList);
                }
                applyCooperateAdapter.notifyDataSetChanged();
                if (applyCooperateList.size() < PAGE_SIZE) {
                    tvHintTxt.setText("暂无更多数据");
                    autoLoadRecyclerView.loadFinish(false);
                } else {
                    tvHintTxt.setText("上拉加载更多");
                    autoLoadRecyclerView.loadFinish(true);
                }
                break;
            case DEAL_DOC_APPLY:
                ToastUtil.toast(this, response.getMsg());
                getApplyCooperateList();
                //通知合作医生列表
                NotifyChangeListenerManager.getInstance().notifyDoctorStatusChange("");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_APPLY:
                getApplyCooperateList();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getApplyCooperateList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getApplyCooperateList();
    }
}
