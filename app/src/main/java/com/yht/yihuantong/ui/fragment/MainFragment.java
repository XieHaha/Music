package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.OrderStatus;
import com.yht.yihuantong.ui.activity.PatientsActivity;
import com.yht.yihuantong.ui.activity.RegistrationDetailActivity;
import com.yht.yihuantong.ui.activity.RegistrationListActivity;
import com.yht.yihuantong.ui.activity.TransferPatientActivity;
import com.yht.yihuantong.ui.activity.TransferPatientHistoryActivity;
import com.yht.yihuantong.utils.AllUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.bean.RegistrationBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.http.Tasks;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;

/**
 * 我的页面
 */
public class MainFragment extends BaseFragment implements OrderStatus
{
    private TextView tvTransferMore, tvOrderMore;
    private TextView tvPatientName, tvPatientCase, tvDoctorName, tvDoctorHospital;
    private TextView tvOrderType, tvOrderStatus, tvOrderPatientName, tvOrderPatientSex, tvOrderPatientAge, tvOrderDetail, tvOrderHospital;
    private TextView tvPatientNum;
    private LinearLayout llTransferLayout, llOrderLayout, llTransferNoneLayout, llOrderNoneLayout;
    private List<TransPatientBean> transPatientBeans = new ArrayList<>();
    private TransPatientBean curTransferPatient;
    private RegistrationBean curRegistrationBean;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 开单记录
     */
    private ArrayList<RegistrationBean> registrationBeans = new ArrayList<>();

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        //获取状态栏高度，填充
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                              getStateBarHeight(getActivity())));//填充状态栏
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("首页");
        view.findViewById(R.id.fragment_main_my_patient_layout).setOnClickListener(this);
        tvPatientName = view.findViewById(R.id.item_transfer_patient_name);
        tvPatientCase = view.findViewById(R.id.item_transfer_patient_case);
        tvDoctorName = view.findViewById(R.id.item_transfer_doc_name);
        tvDoctorHospital = view.findViewById(R.id.item_transfer_hospital);
        tvTransferMore = view.findViewById(R.id.fragment_main_transfer_info_more);
        tvOrderMore = view.findViewById(R.id.fragment_main_order_info_more);
        llTransferLayout = view.findViewById(R.id.fragment_main_transfer_layout);
        llTransferNoneLayout = view.findViewById(R.id.fragment_main_transfer_info_none_layout);
        llOrderLayout = view.findViewById(R.id.fragment_main_order_layout);
        llOrderNoneLayout = view.findViewById(R.id.fragment_main_order_info_none_layout);
        tvOrderType = view.findViewById(R.id.item_order_type);
        tvOrderStatus = view.findViewById(R.id.item_order_status);
        tvOrderPatientName = view.findViewById(R.id.item_order_patient_name);
        tvOrderPatientSex = view.findViewById(R.id.item_order_patient_sex);
        tvOrderPatientAge = view.findViewById(R.id.item_order_patient_age);
        tvOrderDetail = view.findViewById(R.id.item_order_detail);
        tvOrderHospital = view.findViewById(R.id.item_order_hospital);
        tvPatientNum = view.findViewById(R.id.fragment_main_my_health_num);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        getPatientFromList();
        getOrderList();
        getPatientsData();
    }

    @Override
    public void initListener()
    {
        tvTransferMore.setOnClickListener(this);
        tvOrderMore.setOnClickListener(this);
        llTransferLayout.setOnClickListener(this);
        llOrderLayout.setOnClickListener(this);
    }

    /**
     * 转诊记录
     */
    private void initTransferData()
    {
        if (transPatientBeans != null && transPatientBeans.size() > 0)
        {
            curTransferPatient = transPatientBeans.get(0);
            tvPatientName.setText(curTransferPatient.getPatientName());
            tvPatientCase.setText(curTransferPatient.getFromDoctorDiagnosisInfo());
            tvDoctorName.setText(curTransferPatient.getFromDoctorName());
            tvDoctorHospital.setText(curTransferPatient.getFromDoctorHospitalName());
            llTransferNoneLayout.setVisibility(View.GONE);
            tvTransferMore.setVisibility(View.VISIBLE);
        }
        else
        {
            llTransferNoneLayout.setVisibility(View.VISIBLE);
            tvTransferMore.setVisibility(View.GONE);
        }
    }

    /**
     * 开单记录
     */
    private void initOrderData()
    {
        if (registrationBeans != null && registrationBeans.size() > 0)
        {
            curRegistrationBean = registrationBeans.get(0);
            tvOrderType.setText(curRegistrationBean.getProductName());
            switch (curRegistrationBean.getOrderState())
            {
                case STATUS_SUBSCRIBE_NONE:
                    tvOrderStatus.setText("未预约");
                    break;
                case STATUS_SUBSCRIBE:
                    tvOrderStatus.setText("已预约");
                    break;
                case STATUS_COMPLETE:
                    tvOrderStatus.setText("完成检查");
                    break;
                case STATUS_SEND_REPORT:
                    tvOrderStatus.setText("报告已发送");
                    break;
                case STATUS_REFUSE:
                    tvOrderStatus.setText("拒绝");
                    break;
            }
            tvOrderPatientName.setText(curRegistrationBean.getPatientName());
            tvOrderPatientSex.setText(curRegistrationBean.getPatientSex());
            tvOrderPatientAge.setText(AllUtils.formatDate(curRegistrationBean.getOrderDate(),
                                                          AllUtils.YYYY_MM_DD_HH_MM));
            tvOrderDetail.setText(curRegistrationBean.getProductDescription());
            tvOrderHospital.setText(curRegistrationBean.getHospitalName());
            llOrderNoneLayout.setVisibility(View.GONE);
            tvOrderMore.setVisibility(View.VISIBLE);
        }
        else
        {
            llOrderNoneLayout.setVisibility(View.VISIBLE);
            tvOrderMore.setVisibility(View.GONE);
        }
    }

    /**
     * 获取患者申请列表
     */
    private void getApplyPatientList()
    {
        mIRequest.getApplyPatientList(loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 获取患者列表数据
     */
    private void getPatientsData()
    {
        mIRequest.getPatientList(loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 收到转诊申请
     */
    private void getPatientFromList()
    {
        mIRequest.getTransferPatientFromList(loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 开单记录
     */
    private void getOrderList()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/order/doctor/orders/list", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("doctorId", loginSuccessBean.getDoctorId());
        params.put("pageNo", 0);
        params.put("pageSize", PAGE_SIZE);
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>()
        {
            @Override
            public void onStart(int what)
            {
            }

            @Override
            public void onSucceed(int what, Response<String> response)
            {
                String s = response.get();
                try
                {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponseList(object,
                                                                      RegistrationBean.class);
                    if (baseResponse.getCode() == 200)
                    {
                        registrationBeans = baseResponse.getData();
                        initOrderData();
                    }
                    else
                    {
                        ToastUtil.toast(getContext(), baseResponse.getMsg());
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<String> response)
            {
                ToastUtil.toast(getContext(), response.getException().getMessage());
            }

            @Override
            public void onFinish(int what)
            {
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.fragment_main_my_patient_layout:
                intent = new Intent(getContext(), PatientsActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_main_transfer_info_more:
                intent = new Intent(getContext(), TransferPatientHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_main_transfer_layout:
                intent = new Intent(getContext(), TransferPatientActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, false);
                intent.putExtra("isFrom", true);
                intent.putExtra(CommonData.KEY_TRANSFER_BEAN, curTransferPatient);
                startActivity(intent);
                break;
            case R.id.fragment_main_order_info_more:
                intent = new Intent(getContext(), RegistrationListActivity.class);
                intent.putExtra(CommonData.KEY_REGISTRATION_LIST, registrationBeans);
                startActivity(intent);
                break;
            case R.id.fragment_main_order_layout:
                intent = new Intent(getContext(), RegistrationDetailActivity.class);
                intent.putExtra(CommonData.KEY_REGISTRATION_BEAN, curRegistrationBean);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        switch (task)
        {
            case GET_PATIENTS_FROM_LIST:
                transPatientBeans = response.getData();
                initTransferData();
                break;
            case GET_PATIENTS_LIST:
                List<PatientBean> list = response.getData();
                if (list != null && list.size() > 0)
                {
                    tvPatientNum.setText(list.size() + "人");
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != getActivity().RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
