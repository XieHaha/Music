package com.zyc.doctor.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zyc.doctor.R;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateHospitalDocBean;
import com.zyc.doctor.data.bean.HospitalBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.CooperateHospitalDocListAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 合作医院医生列表
 *
 * @author DUNDUN
 */
public class CooperateDocActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {
    @BindView(R.id.public_title_bar_title)
    TextView tvTitle;
    @BindView(R.id.act_cooperate_doc_search)
    EditText editText;
    @BindView(R.id.act_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.act_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvHintTxt;
    private View footerView;
    private CooperateHospitalDocListAdapter cooperateHospitalDocListAdapter;
    private List<CooperateHospitalDocBean> cooperateHospitalDocBeans = new ArrayList<>();
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
    public int getLayoutID() {
        return R.layout.act_cooperate_doc;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        cooperateHospitalDocListAdapter = new CooperateHospitalDocListAdapter(this, cooperateHospitalDocBeans);
        cooperateHospitalDocListAdapter.addFooterView(footerView);
        page = 0;
        if (getIntent() != null) {
            hospitalBean = (HospitalBean)getIntent().getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
        }
        if (hospitalBean != null) {
            tvTitle.setText(hospitalBean.getHospitalName());
        }
        getCooperateHospitalDoctorList();
    }

    @Override
    public void initListener() {
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateHospitalDocListAdapter);
        cooperateHospitalDocListAdapter.setOnItemClickListener((v, position, item) -> {
            Intent intent = new Intent(this, AddFriendsDocActivity.class);
            intent.putExtra(CommonData.KEY_DOCTOR_ID, item.getDoctorId());
            intent.putExtra(CommonData.KEY_PUBLIC, true);
            startActivity(intent);
        });
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftInputFromWindow();
                search(v.getText().toString());
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    search(s.toString());
                }
                else {
                    cooperateHospitalDocListAdapter.setList(cooperateHospitalDocBeans);
                    cooperateHospitalDocListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 获取合作医院下所有医生
     */
    private void getCooperateHospitalDoctorList() {
        if (hospitalBean != null) {
            RequestUtils.getCooperateHospitalDoctorList(this, hospitalBean.getHospitalId(), page, PAGE_SIZE, this);
        }
    }

    /**
     * 匹配搜索
     *
     * @param key
     */
    private void search(String key) {
        List<CooperateHospitalDocBean> datas = DataSupport.where("name like ?", "%" + key + "%")
                                                          .find(CooperateHospitalDocBean.class);
        cooperateHospitalDocListAdapter.setList(datas);
        cooperateHospitalDocListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        page = 0;
        getCooperateHospitalDoctorList();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getCooperateHospitalDoctorList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_COOPERATE_HOSPITAL_DOCTOR_LIST:
                if (response.getData() != null) {
                    cooperateHospitalDocBeans = (List<CooperateHospitalDocBean>)response.getData();
                    if (page == 0) {
                        cooperateHospitalDocListAdapter.setList(cooperateHospitalDocBeans);
                    }
                    else {
                        cooperateHospitalDocListAdapter.addList(cooperateHospitalDocBeans);
                    }
                    cooperateHospitalDocListAdapter.notifyDataSetChanged();
                    if (cooperateHospitalDocBeans.size() < PAGE_SIZE) {
                        tvHintTxt.setText(R.string.txt_list_none_data_hint);
                        autoLoadRecyclerView.loadFinish(false);
                    }
                    else {
                        tvHintTxt.setText(R.string.txt_list_push_hint);
                        autoLoadRecyclerView.loadFinish(true);
                    }
                    //数据存储
                    DataSupport.deleteAll(CooperateHospitalDocBean.class);
                    DataSupport.saveAll(cooperateHospitalDocBeans);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        if (page > 0) {
            page--;
        }
        tvHintTxt.setText(R.string.txt_list_none_data_hint);
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        if (page > 0) {
            page--;
        }
        tvHintTxt.setText(R.string.txt_list_none_data_hint);
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow() {
        InputMethodManager inputmanger = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
