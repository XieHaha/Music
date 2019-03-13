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

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.CooperateDocListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 转诊医生
 *
 * @author DUNDUN
 */
public class SelectTransferDocActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {
    @BindView(R.id.act_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private TextView tvHintTxt;
    private View footerView;
    private CooperateDocListAdapter cooperateDocListAdapter;
    private List<CooperateDocBean> cooperateDocBeanList = new ArrayList<>();
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
        return R.layout.act_select_transfer_doc;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("合作医生");
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        cooperateDocListAdapter = new CooperateDocListAdapter(this, cooperateDocBeanList);
        cooperateDocListAdapter.addFooterView(footerView);
        page = 0;
        getCooperateList();
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateDocListAdapter);
        cooperateDocListAdapter.setOnItemClickListener((v, position, item) ->
        {
            Intent intent = new Intent();
            intent.putExtra(
                    CommonData.KEY_DOCTOR_BEAN,
                    item);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    /**
     * 获取合作医生列表数据
     */
    private void getCooperateList() {
        mIRequest.getCooperateList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onRefresh() {
        page = 0;
        getCooperateList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getCooperateList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_COOPERATE_DOC_LIST:
                if (response.getData() != null) {
                    cooperateDocBeanList = response.getData();
                    if (page == 0) {
                        cooperateDocListAdapter.setList(cooperateDocBeanList);
                    } else {
                        cooperateDocListAdapter.addList(cooperateDocBeanList);
                    }
                    cooperateDocListAdapter.notifyDataSetChanged();
                    if (cooperateDocBeanList.size() < PAGE_SIZE) {
                        tvHintTxt.setText("暂无更多数据");
                        autoLoadRecyclerView.loadFinish(false);
                    } else {
                        tvHintTxt.setText("上拉加载更多");
                        autoLoadRecyclerView.loadFinish(true);
                    }
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

}
