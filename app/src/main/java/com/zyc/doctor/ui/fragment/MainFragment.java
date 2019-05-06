package com.zyc.doctor.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.zyc.doctor.R;
import com.zyc.doctor.api.ApiManager;
import com.zyc.doctor.api.MessageNotifyHelper;
import com.zyc.doctor.api.notify.IChange;
import com.zyc.doctor.api.notify.INotifyChangeListenerServer;
import com.zyc.doctor.api.notify.RegisterType;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.OrderStatus;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseNetConfig;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CameraLoginBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.data.bean.RegistrationBean;
import com.zyc.doctor.data.bean.TransPatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.activity.AddFriendsDocActivity;
import com.zyc.doctor.ui.activity.AddFriendsPatientActivity;
import com.zyc.doctor.ui.activity.CameraLoginActivity;
import com.zyc.doctor.ui.activity.CooperateHospitalActivity;
import com.zyc.doctor.ui.activity.PatientInfoActivity;
import com.zyc.doctor.ui.activity.PatientsActivity;
import com.zyc.doctor.ui.activity.RegistrationDetailActivity;
import com.zyc.doctor.ui.activity.RegistrationListActivity;
import com.zyc.doctor.ui.activity.ServicePackActivity;
import com.zyc.doctor.ui.activity.TransferPatientActivity;
import com.zyc.doctor.ui.activity.TransferPatientHistoryActivity;
import com.zyc.doctor.ui.adapter.MainOptionsAdapter;
import com.zyc.doctor.ui.adapter.OrderInfoLimitAdapter;
import com.zyc.doctor.ui.adapter.RecentContactAdapter;
import com.zyc.doctor.ui.adapter.TransferInfoLimitAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.fragment.BaseFragment;
import com.zyc.doctor.utils.BaseUtils;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.RecentContactUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.gridview.CustomGridView;
import com.zyc.doctor.widgets.qrcode.BarCodeImageView;
import com.zyc.doctor.widgets.qrcode.CaptureQrCodeActivity;
import com.zyc.doctor.widgets.qrcode.DialogPersonalBarCode;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 我的页面
 *
 * @author dundun
 */
public class MainFragment extends BaseFragment implements OrderStatus, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainFragment";
    @BindView(R.id.public_title_bar_more_two)
    ImageView ivTitleBarMore;
    @BindView(R.id.fragment_main_my_recent_contacts_none)
    LinearLayout llNoneRecentContactLayout;
    @BindView(R.id.fragment_main_my_recent_contacts)
    AutoLoadRecyclerView recyclerView;
    @BindView(R.id.fragment_main_options)
    CustomGridView customGridView;
    @BindView(R.id.item_msg_num)
    TextView tvApplyPatientNum;
    @BindView(R.id.message_red_point_layout)
    RelativeLayout rlApplyPatientNumLayout;
    @BindView(R.id.fragment_main_my_health_num)
    TextView tvPatientNum;
    @BindView(R.id.fragment_main_transfer_info_listview)
    ListView transferInfoListView;
    @BindView(R.id.fragment_main_transfer_info_more)
    TextView tvTransferMore;
    @BindView(R.id.fragment_main_transfer_info_none_layout)
    LinearLayout llTransferNoneLayout;
    @BindView(R.id.fragment_main_order_info_listview)
    ListView orderInfoListView;
    @BindView(R.id.fragment_main_order_info_more)
    TextView tvOrderMore;
    @BindView(R.id.fragment_main_order_info_none_layout)
    LinearLayout llOrderNoneLayout;
    @BindView(R.id.fragment_main_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private MainOptionsAdapter mainOptionsAdapter;
    private View viewPop;
    private PopupWindow mPopupwinow;
    private TextView tvOne, tvTwo;
    /**
     * 二维码
     */
    private BarCodeImageView barCodeImageView;
    /**
     * 最近联系人
     */
    private RecentContactAdapter recentContactAdapter;
    /**
     * 转诊记录
     */
    private TransferInfoLimitAdapter transferInfoLimitAdapter;
    /**
     * 开单记录
     */
    private OrderInfoLimitAdapter orderInfoAdapter;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 转诊
     */
    private ArrayList<TransPatientBean> transPatientBeans = new ArrayList<>();
    /**
     * 开单记录
     */
    private ArrayList<RegistrationBean> registrationBeans = new ArrayList<>();
    private List<PatientBean> recentContacts = new ArrayList<>();
    private int[] optionsTxt = {
            R.string.fragment_main_service, R.string.fragment_main_telemedicine, R.string.fragment_main_train,
            R.string.fragment_main_doctor_group, R.string.fragment_main_hospital, R.string.fragment_main_integral };
    private int[] optionsIcon = {
            R.mipmap.icon_service, R.mipmap.icon_telemedicine, R.mipmap.icon_train, R.mipmap.icon_doctor_group,
            R.mipmap.icon_main_hospital, R.mipmap.icon_integral };
    public static final int SERVICE_PACKAGE = 0,//服务包
            REMOTE_CONSULTATION = 1,//远程会诊
            CULTIVATE = 2,//培训
            DOCTOR_GROUP = 3,//医生集团
            HOSPITAL_GROUP = 4,//合作医院
            INTEGRAL = 5;//积分
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 转诊推送
     */
    private static final int TRANSFER_CODE = 1;
    /**
     * 最近联系人
     */
    private static final int RECENT_PEOPLE_CODE = 2;
    /**
     * 最近联系人
     */
    private static final int ORDER_STATUS_CODE = 3;
    /**
     * 扫码结果  添加好友
     */
    public static final int REQUEST_CODE = 0x0000c0de;
    /**
     * 扫码结果  扫码登录
     */
    public static final int REQUEST_CODE_LOGIN = 200;
    /**
     * 转诊状态发生改变回调
     */
    public static final int REQUEST_CODE_STATUS_CHANGE = 100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TRANSFER_CODE:
                    getTransferList();
                    break;
                case RECENT_PEOPLE_CODE:
                    recentContacts = RecentContactUtils.getRecentContactList();
                    if (recentContacts.size() > 0) {
                        llNoneRecentContactLayout.setVisibility(View.GONE);
                    }
                    else {
                        llNoneRecentContactLayout.setVisibility(View.VISIBLE);
                    }
                    ArrayList<PatientBean> list = new ArrayList<>();
                    list.addAll(recentContacts);
                    recentContactAdapter.setList(list);
                    recentContactAdapter.notifyDataSetChanged();
                    break;
                case ORDER_STATUS_CODE:
                    getOrderList();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 推送回调监听  转诊申请
     */
    private IChange<String> doctorTransferPatientListener = data -> {
        handler.sendEmptyMessage(TRANSFER_CODE);
    };
    /**
     * 推送回调监听  患者申请
     */
    private IChange<String> patientStatusChangeListener = data -> {
        if ("add".equals(data)) {
            getPatientsData();
        }
        getApplyPatientList();
    };
    /**
     * 推送回调监听  最近联系人
     */
    private IChange<String> mRecentContactChangeListener = data -> {
        handler.sendEmptyMessage(RECENT_PEOPLE_CODE);
    };
    /**
     * 推送回调监听  订单状态
     */
    private IChange<String> orderStatusChangeListener = data -> {
        try {
            handler.sendEmptyMessage(ORDER_STATUS_CODE);
        }
        catch (NumberFormatException e) {
            LogUtils.w(TAG, "NumberFormatException error", e);
        }
    };

    @Override
    public int getLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        //获取状态栏高度，填充
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("首页");
        view.findViewById(R.id.fragment_main_my_patient_layout).setOnClickListener(this);
        ivTitleBarMore.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        transferInfoLimitAdapter = new TransferInfoLimitAdapter(getActivity());
        transferInfoLimitAdapter.setList(transPatientBeans);
        transferInfoListView.setAdapter(transferInfoLimitAdapter);
        orderInfoAdapter = new OrderInfoLimitAdapter(getActivity());
        orderInfoAdapter.setList(registrationBeans);
        orderInfoListView.setAdapter(orderInfoAdapter);
        barCodeImageView = new BarCodeImageView(getActivity(),
                                                BaseNetConfig.BASE_BASIC_DOWNLOAD_URL + loginSuccessBean.getDoctorId());
        mainOptionsAdapter = new MainOptionsAdapter(getActivity());
        mainOptionsAdapter.setOptionsIcon(optionsIcon);
        mainOptionsAdapter.setOptionsTxt(optionsTxt);
        customGridView.setAdapter(mainOptionsAdapter);
        //最近联系人
        recentContacts = RecentContactUtils.getRecentContactList();
        if (recentContacts.size() > 0) {
            llNoneRecentContactLayout.setVisibility(View.GONE);
        }
        else {
            llNoneRecentContactLayout.setVisibility(View.VISIBLE);
        }
        recentContactAdapter = new RecentContactAdapter(getActivity(), recentContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recentContactAdapter);
    }

    @Override
    public void fillData() {
        super.fillData();
        getTransferList();
        getApplyPatientList();
        getOrderList();
        getPatientsData();
    }

    @Override
    public void initListener() {
        ivTitleBarMore.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        tvTransferMore.setOnClickListener(this);
        tvOrderMore.setOnClickListener(this);
        //注册患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.REGISTER);
        //注册转诊申请监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(doctorTransferPatientListener,
                                                                          RegisterType.REGISTER);
        //最近联系人
        iNotifyChangeListenerServer.registerRecentContactChangeListener(mRecentContactChangeListener,
                                                                        RegisterType.REGISTER);
        //注册订单状态监听
        iNotifyChangeListenerServer.registerOrderStatusChangeListener(orderStatusChangeListener, RegisterType.REGISTER);
        transferInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransPatientBean transPatientBean = transPatientBeans.get(position);
                String transferId = String.valueOf(transPatientBean.getTransferId());
                //移除通知红点
                MessageNotifyHelper.remove(sharePreferenceUtil, transferId, CommonData.KEY_NEW_TRANSFER_MESSAGE_REMIND);
                Intent intent = new Intent(getActivity(), TransferPatientActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, false);
                intent.putExtra(CommonData.KEY_TRANSFER_BEAN, transPatientBean);
                startActivityForResult(intent, REQUEST_CODE_STATUS_CHANGE);
                transferInfoLimitAdapter.notifyDataSetChanged();
                //通知首页tab刷新
                if (mainFragmentCallbackListener != null) {
                    mainFragmentCallbackListener.onOrderStatusCallback();
                }
            }
        });
        orderInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RegistrationBean registrationBean = registrationBeans.get(position);
                String orderId = String.valueOf(registrationBean.getProductOrderId());
                //移除通知红点
                MessageNotifyHelper.remove(sharePreferenceUtil, orderId, CommonData.KEY_NEW_ORDER_MESSAGE_REMIND);
                Intent intent = new Intent(getActivity(), RegistrationDetailActivity.class);
                intent.putExtra(CommonData.KEY_REGISTRATION_BEAN, registrationBean);
                startActivity(intent);
                orderInfoAdapter.notifyDataSetChanged();
                //通知tab
                if (mainFragmentCallbackListener != null) {
                    mainFragmentCallbackListener.onOrderStatusCallback();
                }
            }
        });
        customGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case SERVICE_PACKAGE:
                        intent = new Intent(getActivity(), ServicePackActivity.class);
                        intent.putExtra("limit", true);
                        intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "服务");
                        startActivity(intent);
                        break;
                    case REMOTE_CONSULTATION:
                        new IntentIntegrator(getActivity()).setCaptureActivity(CaptureQrCodeActivity.class)
                                                           .setRequestCode(REQUEST_CODE_LOGIN)
                                                           .setPrompt(getString(R.string.txt_camera_hint))
                                                           .setBarcodeImageEnabled(false)
                                                           .initiateScan();
                        break;
                    case HOSPITAL_GROUP:
                        intent = new Intent(getActivity(), CooperateHospitalActivity.class);
                        startActivity(intent);
                        break;
                    case CULTIVATE:
                    case DOCTOR_GROUP:
                    case INTEGRAL:
                    default:
                        ToastUtil.toast(getActivity(), "敬请期待");
                        break;
                }
            }
        });
        recentContactAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener<PatientBean>() {
            @Override
            public void onItemClick(View v, int position, PatientBean item) {
                Intent intent = new Intent(getActivity(), PatientInfoActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_BEAN, item);
                startActivity(intent);
            }
        });
    }

    /**
     * 转诊记录
     */
    private void initTransferData() {
        if (transPatientBeans != null && transPatientBeans.size() > 0) {
            transferInfoListView.setVisibility(View.VISIBLE);
            llTransferNoneLayout.setVisibility(View.GONE);
            tvTransferMore.setVisibility(View.VISIBLE);
            transferInfoLimitAdapter.setList(transPatientBeans);
            transferInfoLimitAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(transferInfoListView, transferInfoLimitAdapter,
                                             transPatientBeans.size() > CommonData.DATA_LIST_BASE_NUM
                                             ? CommonData.DATA_LIST_BASE_NUM
                                             : transPatientBeans.size());
        }
        else {
            transferInfoListView.setVisibility(View.GONE);
            llTransferNoneLayout.setVisibility(View.VISIBLE);
            tvTransferMore.setVisibility(View.GONE);
        }
        if (mainFragmentCallbackListener != null) {
            mainFragmentCallbackListener.onOrderStatusCallback();
        }
    }

    /**
     * 开单记录
     */
    private void initOrderData() {
        if (registrationBeans != null && registrationBeans.size() > 0) {
            orderInfoListView.setVisibility(View.VISIBLE);
            llOrderNoneLayout.setVisibility(View.GONE);
            tvOrderMore.setVisibility(View.VISIBLE);
            orderInfoAdapter.setList(registrationBeans);
            orderInfoAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(orderInfoListView, orderInfoAdapter,
                                             registrationBeans.size() > CommonData.DATA_LIST_BASE_NUM
                                             ? CommonData.DATA_LIST_BASE_NUM
                                             : registrationBeans.size());
        }
        else {
            llOrderNoneLayout.setVisibility(View.VISIBLE);
            orderInfoListView.setVisibility(View.GONE);
            tvOrderMore.setVisibility(View.GONE);
        }
        if (mainFragmentCallbackListener != null) {
            mainFragmentCallbackListener.onOrderStatusCallback();
        }
    }

    /**
     * 获取患者申请列表
     */
    private void getApplyPatientList() {
        RequestUtils.getApplyPatientList(getContext(), loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 获取患者列表数据
     */
    private void getPatientsData() {
        RequestUtils.getPatientList(getContext(), loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 转诊记录   包括转入转出
     */
    private void getTransferList() {
        RequestUtils.getTransferList(getContext(), loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 开单记录
     */
    private void getOrderList() {
        RequestUtils.getOrderList(getContext(), loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fragment_main_my_patient_layout:
                intent = new Intent(getActivity(), PatientsActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_main_transfer_info_more:
                MessageNotifyHelper.clear(sharePreferenceUtil, CommonData.KEY_NEW_TRANSFER_MESSAGE_REMIND);
                transferInfoLimitAdapter.notifyDataSetChanged();
                //通知首页tab刷新
                if (mainFragmentCallbackListener != null) {
                    mainFragmentCallbackListener.onOrderStatusCallback();
                }
                intent = new Intent(getActivity(), TransferPatientHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_main_order_info_more:
                MessageNotifyHelper.clear(sharePreferenceUtil, CommonData.KEY_NEW_ORDER_MESSAGE_REMIND);
                orderInfoAdapter.notifyDataSetChanged();
                //通知tab
                if (mainFragmentCallbackListener != null) {
                    mainFragmentCallbackListener.onOrderStatusCallback();
                }
                intent = new Intent(getActivity(), RegistrationListActivity.class);
                intent.putExtra(CommonData.KEY_REGISTRATION_LIST, registrationBeans);
                startActivity(intent);
                break;
            case R.id.public_title_bar_more_two:
                showPop();
                break;
            case R.id.txt_one:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                new IntentIntegrator(getActivity()).setCaptureActivity(CaptureQrCodeActivity.class)
                                                   .setPrompt(getString(R.string.txt_camera_hint))
                                                   .setBarcodeImageEnabled(false)
                                                   .initiateScan();
                break;
            case R.id.txt_two:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                DialogPersonalBarCode dialogPersonalBarCode = new DialogPersonalBarCode(getActivity());
                dialogPersonalBarCode.setQRImageViewSrc(barCodeImageView);
                dialogPersonalBarCode.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_PATIENTS_LIST:
                List<PatientBean> list = (List<PatientBean>)response.getData();
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list.size() > 0) {
                    tvPatientNum.setText(list.size() + "人");
                }
                break;
            //患者申请
            case GET_APPLY_PATIENT_LIST:
                ArrayList<PatientBean> patientBeans = (ArrayList<PatientBean>)response.getData();
                if (patientBeans != null && patientBeans.size() > 0) {
                    rlApplyPatientNumLayout.setVisibility(View.VISIBLE);
                    tvApplyPatientNum.setText(String.valueOf(patientBeans.size()));
                    sharePreferenceUtil.putString(CommonData.KEY_PATIENT_APPLY_NUM,
                                                  String.valueOf(patientBeans.size()));
                }
                else {
                    rlApplyPatientNumLayout.setVisibility(View.GONE);
                }
                if (mainFragmentCallbackListener != null) {
                    mainFragmentCallbackListener.onPatientApplyCallback();
                }
                break;
            case GET_TRANSFER_LIST:
                transPatientBeans = (ArrayList<TransPatientBean>)response.getData();
                initTransferData();
                break;
            case GET_ORDER_LIST:
                registrationBeans = (ArrayList<RegistrationBean>)response.getData();
                initOrderData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        IntentResult result;
        switch (requestCode) {
            case REQUEST_CODE:
                result = IntentIntegrator.parseActivityResult(resultCode, data);
                if (result != null && !TextUtils.isEmpty(result.getContents())) {
                    String url = result.getContents();
                    String doctorId = Uri.parse(url).getQueryParameter("doctorId");
                    String patientId = Uri.parse(url).getQueryParameter("patientId");
                    if (!TextUtils.isEmpty(doctorId)) {
                        Intent intent = new Intent(getActivity(), AddFriendsDocActivity.class);
                        intent.putExtra(CommonData.KEY_DOCTOR_ID, doctorId);
                        intent.putExtra(CommonData.KEY_PUBLIC, true);
                        startActivity(intent);
                    }
                    else if (!TextUtils.isEmpty(patientId)) {
                        Intent intent = new Intent(getActivity(), AddFriendsPatientActivity.class);
                        intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                        intent.putExtra(CommonData.KEY_PUBLIC, true);
                        startActivity(intent);
                    }
                    else {
                        ToastUtil.toast(getContext(), R.string.txt_camera_error);
                    }
                }
                else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REQUEST_CODE_LOGIN:
                String contents = data.getStringExtra(Intents.Scan.RESULT);
                LogUtils.i(TAG, contents);
                try {
                    CameraLoginBean cameraLoginBean = new Gson().fromJson(contents, CameraLoginBean.class);
                    Intent intent = new Intent(getActivity(), CameraLoginActivity.class);
                    intent.putExtra(CommonData.KEY_CAMERA_LOGIN_BEAN, cameraLoginBean);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_bottom_in, R.anim.keep);
                }
                catch (JsonSyntaxException e) {
                    ToastUtil.toast(getContext(), R.string.txt_camera_error);
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_STATUS_CHANGE:
                getTransferList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        fillData();
    }

    /**
     * 显示pop
     */
    private void showPop() {
        viewPop = LayoutInflater.from(getActivity()).inflate(R.layout.health_pop_menu, null);
        tvOne = viewPop.findViewById(R.id.txt_one);
        tvTwo = viewPop.findViewById(R.id.txt_two);
        tvOne.setText(R.string.txt_camera);
        tvTwo.setText(R.string.txt_camera_qr);
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        if (mPopupwinow == null) {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(viewPop, LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(viewPop, Gravity.TOP | Gravity.RIGHT, 0, (int)BaseUtils.dipToPx(getActivity(), 55));
    }

    /**
     * 设置高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView, BaseAdapter baseAdapter, int count) {
        if (listView == null || baseAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = baseAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (count - 1));
        listView.setLayoutParams(params);
    }

    private OnMainFragmentCallbackListener mainFragmentCallbackListener;

    public void setOnMainFragmentCallbackListener(OnMainFragmentCallbackListener onMainFragmentCallbackListener) {
        this.mainFragmentCallbackListener = onMainFragmentCallbackListener;
    }

    public interface OnMainFragmentCallbackListener {
        /**
         * 患者申请回调
         */
        void onPatientApplyCallback();

        /**
         * 首页就诊信息  转诊信息更改
         */
        void onOrderStatusCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.UNREGISTER);
        //注销患者状态监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(doctorTransferPatientListener,
                                                                          RegisterType.UNREGISTER);
        //最近联系人
        iNotifyChangeListenerServer.registerRecentContactChangeListener(mRecentContactChangeListener,
                                                                        RegisterType.UNREGISTER);
        //注销订单状态监听
        iNotifyChangeListenerServer.registerOrderStatusChangeListener(orderStatusChangeListener,
                                                                      RegisterType.UNREGISTER);
    }
}
