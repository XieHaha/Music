package com.yht.yihuantong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.CooperateDocListAdapter;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.HospitalBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

/**
 * 合作医院医生列表
 *
 * @author DUNDUN
 */
public class CooperateDocActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private TextView tvHintTxt, tvTitle;
    private EditText editText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View footerView;
    private CooperateDocListAdapter cooperateDocListAdapter;
    private List<CooperateDocBean> cooperateDocBeanList = new ArrayList<>();
    private HospitalBean hospitalBean;
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
        return R.layout.act_cooperate_doc;
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
        tvTitle = ((TextView)findViewById(R.id.public_title_bar_title));
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.act_cooperate_swipe_layout);
        autoLoadRecyclerView = (AutoLoadRecyclerView)findViewById(R.id.act_cooperate_recycler_view);
        editText = (EditText)findViewById(R.id.act_cooperate_doc_search);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
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
        cooperateDocListAdapter = new CooperateDocListAdapter(this, cooperateDocBeanList);
        cooperateDocListAdapter.addFooterView(footerView);
        page = 0;
        if (getIntent() != null)
        {
            hospitalBean = (HospitalBean)getIntent().getSerializableExtra(
                    CommonData.KEY_HOSPITAL_BEAN);
        }
        if (hospitalBean != null)
        {
            tvTitle.setText(hospitalBean.getHospitalName());
        }
        getCooperateHospitalDoctorList();
    }

    @Override
    public void initListener()
    {
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
        editText.setOnEditorActionListener((v, actionId, event) ->
                                           {
                                               if (actionId == EditorInfo.IME_ACTION_SEARCH)
                                               {
                                                   hideSoftInputFromWindow();
                                               }
                                               return false;
                                           });
    }

    /**
     * 获取合作医院下所有医生
     */
    private void getCooperateHospitalDoctorList()
    {
        if (hospitalBean != null)
        {
            mIRequest.getCooperateHospitalDoctorList(hospitalBean.getHospitalId(), page, PAGE_SIZE,
                                                     this);
        }
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        getCooperateHospitalDoctorList();
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getCooperateHospitalDoctorList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_COOPERATE_HOSPITAL_DOCTOR_LIST:
                if (response.getData() != null)
                {
                    cooperateDocBeanList = response.getData();
                    if (page == 0)
                    {
                        cooperateDocListAdapter.setList(cooperateDocBeanList);
                    }
                    else
                    {
                        cooperateDocListAdapter.addList(cooperateDocBeanList);
                    }
                    cooperateDocListAdapter.notifyDataSetChanged();
                    if (cooperateDocBeanList.size() < PAGE_SIZE)
                    {
                        tvHintTxt.setText("暂无更多数据");
                        autoLoadRecyclerView.loadFinish(false);
                    }
                    else
                    {
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
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        if (page > 0)
        {
            page--;
        }
        tvHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
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

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow()
    {
        InputMethodManager inputmanger = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
