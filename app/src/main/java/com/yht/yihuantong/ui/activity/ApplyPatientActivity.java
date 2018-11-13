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

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.ApplyPatientAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 患者申请
 *
 * @author DUNDUN
 */
public class ApplyPatientActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View footerView;
    private TextView tvHintTxt;
    private ApplyPatientAdapter applyPatientAdapter;
    private List<PatientBean> applyPatientList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 20;
    /**
     * 一页最大数
     */
    private static final int REQUEST_CODE_APPLY = 100;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_apply_patient;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("患者申请");
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.act_apply_patient_swipe_layout);
        autoLoadRecyclerView = (AutoLoadRecyclerView)findViewById(
                R.id.act_apply_patient_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        applyPatientAdapter = new ApplyPatientAdapter(this, applyPatientList);
        applyPatientAdapter.addFooterView(footerView);
        page = 0;
        getApplyPatientList();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(applyPatientAdapter);
        applyPatientAdapter.setOnItemClickListener((v, position, item) ->
                                                   {
                                                       Intent intent = new Intent(
                                                               ApplyPatientActivity.this,
                                                               AddFriendsPatientActivity.class);
                                                       intent.putExtra(CommonData.KEY_PATIENT_ID,
                                                                       item.getPatientId());
                                                       intent.putExtra(CommonData.KEY_PUBLIC,
                                                                       false);
                                                       startActivityForResult(intent,
                                                                              REQUEST_CODE_APPLY);
                                                   });
    }

    /**
     * 获取患者申请列表
     */
    private void getApplyPatientList()
    {
        mIRequest.getApplyPatientList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case GET_APPLY_PATIENT_LIST:
                applyPatientList = response.getData();
                if (page == 0)
                {
                    applyPatientAdapter.setList(applyPatientList);
                }
                else
                {
                    applyPatientAdapter.addList(applyPatientList);
                }
                applyPatientAdapter.notifyDataSetChanged();
                if (applyPatientList.size() < PAGE_SIZE)
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
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case GET_APPLY_PATIENT_LIST:
                if (page > 0)
                {
                    page--;
                }
                tvHintTxt.setText("暂无更多数据");
                autoLoadRecyclerView.loadFinish();
                break;
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e)
    {
        super.onResponseError(task, e);
        switch (task)
        {
            case GET_APPLY_PATIENT_LIST:
                if (page > 0)
                {
                    page--;
                }
                tvHintTxt.setText("暂无更多数据");
                autoLoadRecyclerView.loadFinish();
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task)
    {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case REQUEST_CODE_APPLY:
                getApplyPatientList();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        getApplyPatientList();
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getApplyPatientList();
    }
}
