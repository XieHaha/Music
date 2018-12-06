package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.OrderStatus;
import com.yht.yihuantong.ui.activity.PatientsActivity;
import com.yht.yihuantong.ui.activity.RegistrationDetailActivity;
import com.yht.yihuantong.ui.activity.RegistrationListActivity;
import com.yht.yihuantong.ui.activity.TransferPatientActivity;
import com.yht.yihuantong.ui.activity.TransferPatientHistoryActivity;
import com.yht.yihuantong.ui.adapter.OrderInfoAdapter;
import com.yht.yihuantong.ui.adapter.TransferInfoAdapter;

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
public class MainFragment extends BaseFragment
        implements OrderStatus, SwipeRefreshLayout.OnRefreshListener
{
    private TextView tvTransferMore, tvOrderMore;
    private TextView tvPatientNum;
    private LinearLayout llTransferNoneLayout, llOrderNoneLayout;
    private TextView tvApplyPatientNum;
    private RelativeLayout rlApplyPatientNumLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView orderInfoListView, transferInfoListView;
    private TransferInfoAdapter transferInfoAdapter;
    private OrderInfoAdapter orderInfoAdapter;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 转诊
     */
    private ArrayList<TransPatientBean> transPatientBeans = new ArrayList<>();
    /**
     * 开单记录
     */
    private ArrayList<RegistrationBean> registrationBeans = new ArrayList<>();
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 转诊推送
     */
    private static final int TRANSFER_CODE = 1;
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case TRANSFER_CODE:
                    getPatientFromList();
                    break;
            }
        }
    };
    /**
     * 推送回调监听  转诊申请
     */
    private IChange<String> doctorTransferPatientListener = data ->
    {
        if ("from".equals(data))
        {
            handler.sendEmptyMessage(TRANSFER_CODE);
        }
    };
    /**
     * 推送回调监听  患者申请
     */
    private IChange<String> patientStatusChangeListener = data ->
    {
        if ("add".equals(data))
        {
            getPatientsData();
        }
        else
        {
            getApplyPatientList();
        }
    };

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
        swipeRefreshLayout = view.findViewById(R.id.fragment_main_swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
        orderInfoListView = view.findViewById(R.id.fragment_main_order_info_listview);
        transferInfoListView = view.findViewById(R.id.fragment_main_transfer_info_listview);
        rlApplyPatientNumLayout = view.findViewById(R.id.message_red_point_layout);
        tvApplyPatientNum = view.findViewById(R.id.item_msg_num);
        tvTransferMore = view.findViewById(R.id.fragment_main_transfer_info_more);
        tvOrderMore = view.findViewById(R.id.fragment_main_order_info_more);
        llTransferNoneLayout = view.findViewById(R.id.fragment_main_transfer_info_none_layout);
        llOrderNoneLayout = view.findViewById(R.id.fragment_main_order_info_none_layout);
        tvPatientNum = view.findViewById(R.id.fragment_main_my_health_num);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance()
                                                .getServer(INotifyChangeListenerServer.class);
        transferInfoAdapter = new TransferInfoAdapter(getContext());
        transferInfoAdapter.setList(transPatientBeans);
        transferInfoListView.setAdapter(transferInfoAdapter);
        orderInfoAdapter = new OrderInfoAdapter(getContext());
        orderInfoAdapter.setList(registrationBeans);
        orderInfoListView.setAdapter(orderInfoAdapter);
        getPatientFromList();
        getApplyPatientList();
        getOrderList();
        getPatientsData();
    }

    @Override
    public void initListener()
    {
        swipeRefreshLayout.setOnRefreshListener(this);
        tvTransferMore.setOnClickListener(this);
        tvOrderMore.setOnClickListener(this);
        //注册患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.REGISTER);
        //注册转诊申请监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(
                doctorTransferPatientListener, RegisterType.REGISTER);
        transferInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), TransferPatientActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, false);
                intent.putExtra("isFrom", true);
                intent.putExtra(CommonData.KEY_TRANSFER_BEAN, transPatientBeans.get(position));
                startActivity(intent);
            }
        });
        orderInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getContext(), RegistrationDetailActivity.class);
                intent.putExtra(CommonData.KEY_REGISTRATION_BEAN, registrationBeans.get(position));
                startActivity(intent);
            }
        });
    }

    /**
     * 转诊记录
     */
    private void initTransferData()
    {
        if (transPatientBeans != null && transPatientBeans.size() > 0)
        {
            transferInfoListView.setVisibility(View.VISIBLE);
            llTransferNoneLayout.setVisibility(View.GONE);
            tvTransferMore.setVisibility(View.VISIBLE);
            transferInfoAdapter.setList(transPatientBeans);
            transferInfoAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(transferInfoListView, transferInfoAdapter,
                                             transPatientBeans.size() >
                                             CommonData.DATA_LIST_BASE_NUM
                                             ? CommonData.DATA_LIST_BASE_NUM
                                             : transPatientBeans.size());
        }
        else
        {
            transferInfoListView.setVisibility(View.GONE);
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
            orderInfoListView.setVisibility(View.VISIBLE);
            llOrderNoneLayout.setVisibility(View.GONE);
            tvOrderMore.setVisibility(View.VISIBLE);
            orderInfoAdapter.setList(registrationBeans);
            orderInfoAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(orderInfoListView, orderInfoAdapter,
                                             registrationBeans.size() >
                                             CommonData.DATA_LIST_BASE_NUM
                                             ? CommonData.DATA_LIST_BASE_NUM
                                             : registrationBeans.size());
        }
        else
        {
            llOrderNoneLayout.setVisibility(View.VISIBLE);
            orderInfoListView.setVisibility(View.GONE);
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
                swipeRefreshLayout.setRefreshing(false);
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
            case R.id.fragment_main_order_info_more:
                intent = new Intent(getContext(), RegistrationListActivity.class);
                intent.putExtra(CommonData.KEY_REGISTRATION_LIST, registrationBeans);
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
                break;
            case GET_APPLY_PATIENT_LIST://患者申请
                ArrayList<PatientBean> patientBeans = response.getData();
                if (patientBeans != null && patientBeans.size() > 0)
                {
                    rlApplyPatientNumLayout.setVisibility(View.VISIBLE);
                    tvApplyPatientNum.setText(String.valueOf(patientBeans.size()));
                }
                else
                {
                    rlApplyPatientNumLayout.setVisibility(View.GONE);
                }
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

    @Override
    public void onRefresh()
    {
        getPatientFromList();
        getApplyPatientList();
        getOrderList();
        getPatientsData();
    }

    /**
     * 设置高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView, BaseAdapter baseAdapter,
            int count)
    {
        if (listView == null || baseAdapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < count; i++)
        {
            View listItem = baseAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (count - 1));
        listView.setLayoutParams(params);
        //        if (scrollView != null)
        //        {
        //            scrollView.scrollTo(0, 0);
        //        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.UNREGISTER);
        //注销患者状态监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(
                doctorTransferPatientListener, RegisterType.UNREGISTER);
    }
}
