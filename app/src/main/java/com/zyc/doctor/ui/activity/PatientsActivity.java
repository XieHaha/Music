package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zyc.doctor.R;
import com.zyc.doctor.api.ApiManager;
import com.zyc.doctor.api.notify.IChange;
import com.zyc.doctor.api.notify.INotifyChangeListenerServer;
import com.zyc.doctor.api.notify.RegisterType;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.PatientsListAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 患者列表
 *
 * @author DUNDUN
 */
public class PatientsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {
    @BindView(R.id.public_title_bar_more_three)
    ImageView ivTitleBarMore;
    @BindView(R.id.fragment_patients_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.fragment_patients_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private PatientsListAdapter patientsListAdapter;
    private RelativeLayout rlMsgHint, rlMsgHint2;
    private TextView tvNum, tvExNum;
    private View headerView, exHeaderView, footerView;
    private TextView tvFooterHintTxt, tvHeanderHintTxt;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private List<PatientBean> patientBeanList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 扫码添加患者
     */
    private static final int ADD_PATIENT = 1;
    /**
     * 转诊患者
     */
    private static final int CHANGE_PATIENT = 2;
    /**
     * 删除患者
     */
    private static final int REQUEST_CODE_DELETE = 100;
    /**
     * 患者申请处理结果
     */
    private static final int REQUEST_CODE_PATIENT_APPLY = 101;
    /**
     * 患者转诊处理结果
     */
    private static final int REQUEST_CODE_PATIENT_EXCHANGE = 102;
    /**
     * 扫码结果
     */
    public static final int REQUEST_CODE = 0x0000c0de;
    /**
     * 推送回调监听  患者申请
     */
    private IChange<String> patientStatusChangeListener = data -> {
        if ("add".equals(data)) {
            getPatientsData();
        }
        getApplyPatientList();
    };

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_patients;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("我的患者");
        ivTitleBarMore.setVisibility(View.VISIBLE);
        headerView = LayoutInflater.from(this).inflate(R.layout.view_cooperate_doc_header, null);
        exHeaderView = LayoutInflater.from(this).inflate(R.layout.view_change_patient_header, null);
        footerView = LayoutInflater.from(this).inflate(R.layout.view_list_footerr, null);
        tvHeanderHintTxt = headerView.findViewById(R.id.view_header_hint_txt);
        rlMsgHint = headerView.findViewById(R.id.message_red_point);
        tvNum = headerView.findViewById(R.id.item_msg_num);
        tvExNum = exHeaderView.findViewById(R.id.item_msg_num);
        rlMsgHint2 = exHeaderView.findViewById(R.id.message_red_point2);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        tvHeanderHintTxt.setText("我的患者申请");
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        patientsListAdapter = new PatientsListAdapter(this, patientBeanList);
        patientsListAdapter.addFooterView(footerView);
        patientsListAdapter.addHeaderView(headerView);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        getPatientsData();
        getApplyPatientList();
    }

    @Override
    public void initListener() {
        ivTitleBarMore.setOnClickListener(this);
        headerView.setOnClickListener(this);
        exHeaderView.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(patientsListAdapter);
        patientsListAdapter.setOnItemClickListener((v, position, item) -> {
            Intent intent = new Intent(this, PatientInfoActivity.class);
            intent.putExtra(CommonData.KEY_PATIENT_BEAN, item);
            startActivityForResult(intent, REQUEST_CODE_DELETE);
        });
        //注册患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.REGISTER);
    }

    /**
     * 获取患者列表数据
     */
    private void getPatientsData() {
        RequestUtils.getPatientList(this, loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 获取患者申请列表
     */
    private void getApplyPatientList() {
        RequestUtils.getApplyPatientList(this, loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fragment_cooperate_apply_layout:
                intent = new Intent(this, ApplyPatientActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PATIENT_APPLY);
                break;
            case R.id.public_title_bar_more_three:
                new IntentIntegrator(this).setBarcodeImageEnabled(false).setPrompt("将二维码放入框内，即可自动识别").initiateScan();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            //删除患者
            case REQUEST_CODE_DELETE:
                getPatientsData();
                break;
            //患者申请操作
            case REQUEST_CODE_PATIENT_APPLY:
                getApplyPatientList();
                break;
            case REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
                if (result != null && !TextUtils.isEmpty(result.getContents())) {
                    String url = result.getContents();
                    String doctorId = Uri.parse(url).getQueryParameter("doctorId");
                    String patientId = Uri.parse(url).getQueryParameter("patientId");
                    if (!TextUtils.isEmpty(doctorId)) {
                        Intent intent = new Intent(PatientsActivity.this, AddFriendsDocActivity.class);
                        intent.putExtra(CommonData.KEY_DOCTOR_ID, doctorId);
                        intent.putExtra(CommonData.KEY_PUBLIC, true);
                        startActivity(intent);
                    }
                    else if (!TextUtils.isEmpty(patientId)) {
                        Intent intent = new Intent(PatientsActivity.this, AddFriendsPatientActivity.class);
                        intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                        intent.putExtra(CommonData.KEY_PUBLIC, true);
                        startActivity(intent);
                    }
                    else {
                        ToastUtil.toast(PatientsActivity.this, R.string.txt_camera_error);
                    }
                }
                else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        getPatientsData();
    }

    @Override
    public void loadMore() {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getPatientsData();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENTS_LIST:
                if (response.getData() != null) {
                    patientBeanList = (List<PatientBean>)response.getData();
                    if (page == 0) {
                        patientsListAdapter.setList(patientBeanList);
                    }
                    else {
                        patientsListAdapter.addList(patientBeanList);
                    }
                    patientsListAdapter.notifyDataSetChanged();
                    if (patientBeanList.size() < PAGE_SIZE) {
                        tvFooterHintTxt.setText(R.string.txt_list_none_data_hint);
                        autoLoadRecyclerView.loadFinish(false);
                    }
                    else {
                        tvFooterHintTxt.setText(R.string.txt_list_push_hint);
                        autoLoadRecyclerView.loadFinish(true);
                    }
                    //数据存储
                    DataSupport.deleteAll(PatientBean.class);
                    DataSupport.saveAll(patientBeanList);
                }
                sharePreferenceUtil.putString(CommonData.KEY_PATIENT_NUM, String.valueOf(patientBeanList.size()));
                break;
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
                break;
            //患者申请
            case GET_APPLY_PATIENT_LIST:
                ArrayList<PatientBean> list = (ArrayList<PatientBean>)response.getData();
                if (list != null && list.size() > 0) {
                    rlMsgHint.setVisibility(View.VISIBLE);
                    tvNum.setText(String.valueOf(list.size()));
                    sharePreferenceUtil.putString(CommonData.KEY_PATIENT_APPLY_NUM, String.valueOf(list.size()));
                }
                else {
                    rlMsgHint.setVisibility(View.GONE);
                    sharePreferenceUtil.putString(CommonData.KEY_PATIENT_APPLY_NUM, "0");
                }
                if (onApplyCallbackListener != null) {
                    onApplyCallbackListener.onApplyCallback();
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
        tvFooterHintTxt.setText(R.string.txt_list_none_data_hint);
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        if (page > 0) {
            page--;
        }
        tvFooterHintTxt.setText(R.string.txt_list_none_data_hint);
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    private OnApplyCallbackListener onApplyCallbackListener;

    public void setOnApplyCallbackListener(OnApplyCallbackListener onApplyCallbackListener) {
        this.onApplyCallbackListener = onApplyCallbackListener;
    }

    public interface OnApplyCallbackListener {
        void onApplyCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.UNREGISTER);
    }
}
