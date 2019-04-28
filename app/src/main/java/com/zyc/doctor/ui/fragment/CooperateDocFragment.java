package com.zyc.doctor.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.activity.AddFriendsDocActivity;
import com.zyc.doctor.ui.activity.AddFriendsPatientActivity;
import com.zyc.doctor.ui.activity.ApplyCooperateDocActivity;
import com.zyc.doctor.ui.activity.CooperateHospitalActivity;
import com.zyc.doctor.ui.activity.DoctorInfoActivity;
import com.zyc.doctor.ui.adapter.CooperateDocListAdapter;
import com.zyc.doctor.ui.base.fragment.BaseFragment;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;
import com.zyc.doctor.widgets.recyclerview.callback.LoadMoreListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * 合作医生  碎片
 *
 * @author DUNDUN
 */
public class CooperateDocFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {
    @BindView(R.id.public_title_bar_more_three)
    ImageView ivTitleBarMore;
    @BindView(R.id.fragment_cooperate_recycler_view)
    AutoLoadRecyclerView autoLoadRecyclerView;
    @BindView(R.id.fragment_cooperate_swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvHintTxt;
    private TextView tvNum;
    private RelativeLayout rlMsgHint;
    private View headerView, footerView;
    private View viewPop;
    private PopupWindow mPopupwinow;
    private TextView tvOne, tvTwo;
    private CooperateDocListAdapter cooperateDocListAdapter;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private List<CooperateDocBean> cooperateDocBeanList = new ArrayList<>();
    /**
     * 当前页码
     */
    private int page = 0;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;
    /**
     * 扫码结果
     */
    public static final int REQUEST_CODE = 0x0000c0de;
    /**
     * 取消关注回调
     */
    public static final int REQUEST_CODE_CANCEL_DOC = 100;
    /**
     * 推送回调监听
     */
    private IChange<String> doctorStatusChangeListener = data -> {
        getCooperateList();
        getApplyCooperateList();
    };

    @Override
    public int getLayoutID() {
        return R.layout.fragment_cooperate_doc;
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        //获取状态栏高度，填充 //填充状态栏
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("合作医生");
        ivTitleBarMore.setVisibility(View.VISIBLE);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_cooperate_doc_header, null);
        rlMsgHint = headerView.findViewById(R.id.message_red_point);
        tvNum = headerView.findViewById(R.id.item_msg_num);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light, android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        cooperateDocListAdapter = new CooperateDocListAdapter(getActivity(), cooperateDocBeanList);
        cooperateDocListAdapter.addHeaderView(headerView);
        cooperateDocListAdapter.addFooterView(footerView);
        page = 0;
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        //获取合作医生申请
        getApplyCooperateList();
        getCooperateList();
    }

    @Override
    public void initListener() {
        ivTitleBarMore.setOnClickListener(this);
        headerView.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateDocListAdapter);
        cooperateDocListAdapter.setOnItemClickListener((v, position, item) -> {
            Intent intent = new Intent(getActivity(), DoctorInfoActivity.class);
            intent.putExtra(CommonData.KEY_DOCTOR_BEAN, item);
            intent.putExtra(CommonData.KEY_IS_DEAL_DOC, true);
            startActivityForResult(intent, REQUEST_CODE_CANCEL_DOC);
        });
        cooperateDocListAdapter.setOnItemLongClickListener((v, position, item) -> {
            HintDialog hintDialog = new HintDialog(getActivity());
            hintDialog.setContentString("确定取消关注?");
            hintDialog.setOnEnterClickListener(() -> cancelCooperateDoc(item.getDoctorId()));
            hintDialog.setOnCancelClickListener(() -> hintDialog.dismiss());
            hintDialog.show();
        });
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorStatusChangeListener(doctorStatusChangeListener,
                                                                       RegisterType.REGISTER);
    }

    /**
     * 获取合作医生列表数据
     */
    private void getCooperateList() {
        RequestUtils.getCooperateList(getContext(), loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 合作医生申请 取消
     */
    private void cancelCooperateDoc(String doctorId) {
        RequestUtils.cancelCooperateDoc(getContext(), loginSuccessBean.getDoctorId(), doctorId, this);
    }

    /**
     * 获取申请合作医生列表数据
     */
    private void getApplyCooperateList() {
        RequestUtils.getApplyCooperateList(getContext(), loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 显示pop
     */
    private void showPop() {
        viewPop = LayoutInflater.from(getActivity()).inflate(R.layout.health_pop_menu, null);
        tvOne = viewPop.findViewById(R.id.txt_one);
        tvTwo = viewPop.findViewById(R.id.txt_two);
        tvOne.setText(R.string.txt_camera);
        tvTwo.setText(R.string.txt_menu_doc);
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
        mPopupwinow.showAtLocation(viewPop, Gravity.TOP | Gravity.RIGHT, 0, (int)AllUtils.dipToPx(getActivity(), 55));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fragment_cooperate_apply_layout:
                intent = new Intent(getActivity(), ApplyCooperateDocActivity.class);
                startActivity(intent);
                break;
            case R.id.public_title_bar_more_three:
                showPop();
                break;
            case R.id.txt_one:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                IntentIntegrator.forSupportFragment(this)
                                .setBarcodeImageEnabled(false)
                                .setPrompt(getString(R.string.txt_camera_hint))
                                .initiateScan();
                break;
            case R.id.txt_two:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(getActivity(), CooperateHospitalActivity.class);
                startActivity(intent);
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
            case REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
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
            case REQUEST_CODE_CANCEL_DOC:
                onRefresh();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 0;
        getCooperateList();
        getApplyCooperateList();
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
                cooperateDocBeanList = (List<CooperateDocBean>)response.getData();
                if (cooperateDocBeanList == null) {
                    cooperateDocBeanList = new ArrayList<>();
                }
                if (page == 0) {
                    cooperateDocListAdapter.setList(cooperateDocBeanList);
                }
                else {
                    cooperateDocListAdapter.addList(cooperateDocBeanList);
                }
                cooperateDocListAdapter.notifyDataSetChanged();
                if (cooperateDocBeanList.size() < PAGE_SIZE) {
                    tvHintTxt.setText(R.string.txt_list_none_data_hint);
                    autoLoadRecyclerView.loadFinish(false);
                }
                else {
                    tvHintTxt.setText(R.string.txt_list_push_hint);
                    autoLoadRecyclerView.loadFinish(true);
                }
                //数据存储
                DataSupport.deleteAll(CooperateDocBean.class);
                DataSupport.saveAll(cooperateDocBeanList);
                sharePreferenceUtil.putString(CommonData.KEY_DOCTOR_NUM, String.valueOf(cooperateDocBeanList.size()));
                break;
            case APPLY_COOPERATE_DOC:
                ToastUtil.toast(getActivity(), response.getMsg());
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(getActivity(), response.getMsg());
                getCooperateList();
                break;
            case GET_APPLY_COOPERATE_DOC_LIST:
                ArrayList<CooperateDocBean> list = (ArrayList<CooperateDocBean>)response.getData();
                if (list != null && list.size() > 0) {
                    rlMsgHint.setVisibility(View.VISIBLE);
                    tvNum.setText(String.valueOf(list.size()));
                    sharePreferenceUtil.putString(CommonData.KEY_DOCTOR_APPLY_NUM, String.valueOf(list.size()));
                }
                else {
                    rlMsgHint.setVisibility(View.GONE);
                    sharePreferenceUtil.putString(CommonData.KEY_DOCTOR_APPLY_NUM, "0");
                }
                if (onDocApplyCallbackListener != null) {
                    onDocApplyCallbackListener.onDocApplyCallback();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
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

    private OnDocApplyCallbackListener onDocApplyCallbackListener;

    public void setOnDocApplyCallbackListener(OnDocApplyCallbackListener onDocApplyCallbackListener) {
        this.onDocApplyCallbackListener = onDocApplyCallbackListener;
    }

    public interface OnDocApplyCallbackListener {
        /**
         * 合作医生申请
         */
        void onDocApplyCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorStatusChangeListener(doctorStatusChangeListener,
                                                                       RegisterType.UNREGISTER);
    }
}
