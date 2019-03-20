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
import com.zyc.doctor.api.IChange;
import com.zyc.doctor.api.RegisterType;
import com.zyc.doctor.api.notify.INotifyChangeListenerServer;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.ui.activity.AddFriendsDocActivity;
import com.zyc.doctor.ui.activity.AddFriendsPatientActivity;
import com.zyc.doctor.ui.activity.ApplyCooperateDocActivity;
import com.zyc.doctor.ui.activity.CooperateHospitalActivity;
import com.zyc.doctor.ui.activity.DoctorInfoActivity;
import com.zyc.doctor.ui.adapter.CooperateDocListAdapter;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.utils.AllUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

import static android.app.Activity.RESULT_OK;

/**
 * 合作医生  碎片
 *
 * @author DUNDUN
 */
public class CooperateDocFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener {
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
    private View view_pop;
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
        mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        ((TextView) view.findViewById(R.id.public_title_bar_title)).setText("合作医生");
        ivTitleBarMore.setVisibility(View.VISIBLE);
        headerView =
                LayoutInflater.from(getActivity()).inflate(R.layout.view_cooperate_doc_header,
                        null);
        rlMsgHint = headerView.findViewById(R.id.message_red_point);
        tvNum = headerView.findViewById(R.id.item_msg_num);
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.view_list_footerr, null);
        tvHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
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
        autoLoadRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(cooperateDocListAdapter);
        cooperateDocListAdapter.setOnItemClickListener((v, position, item) -> {
            Intent intent = new Intent(getActivity(), DoctorInfoActivity.class);
            intent.putExtra(CommonData.KEY_DOCTOR_BEAN, item);
            intent.putExtra(CommonData.KEY_IS_DEAL_DOC, true);
            startActivityForResult(intent, REQUEST_CODE_CANCEL_DOC);
        });
        cooperateDocListAdapter.setOnItemLongClickListener((v, position, item) -> new SimpleDialog(getActivity(), "确定取消关注?", (dialog, which) -> cancelCooperateDoc(item.getDoctorId()), (dialog, which) -> dialog.dismiss()).show());
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorStatusChangeListener(doctorStatusChangeListener
                , RegisterType.REGISTER);
    }

    /**
     * 获取合作医生列表数据
     */
    private void getCooperateList() {
        mIRequest.getCooperateList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 合作医生申请
     */
    private void applyCooperateDoc(String doctorId, int requestCode) {
        mIRequest.applyCooperateDoc(loginSuccessBean.getDoctorId(), doctorId, requestCode, this);
    }

    /**
     * 合作医生申请 取消
     */
    private void cancelCooperateDoc(String doctorId) {
        mIRequest.cancelCooperateDoc(loginSuccessBean.getDoctorId(), doctorId, this);
    }

    /**
     * 获取申请合作医生列表数据
     */
    private void getApplyCooperateList() {
        mIRequest.getApplyCooperateList(loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 显示pop
     */
    private void showPop() {
        view_pop = LayoutInflater.from(getActivity()).inflate(R.layout.health_pop_menu, null);
        tvOne = view_pop.findViewById(R.id.txt_one);
        tvTwo = view_pop.findViewById(R.id.txt_two);
        tvOne.setText("扫一扫");
        tvTwo.setText("合作医院医生");
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        if (mPopupwinow == null) {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(view_pop, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(view_pop, Gravity.TOP | Gravity.RIGHT, 0,
                (int) AllUtils.dipToPx(getActivity(), 55));
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
                IntentIntegrator.forSupportFragment(this).setBarcodeImageEnabled(false).setPrompt(getString(R.string.txt_camera_hint)).initiateScan();
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
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                    } else {
                        String url = result.getContents();
                        String doctorId = Uri.parse(url).getQueryParameter("doctorId");
                        String patientId = Uri.parse(url).getQueryParameter("patientId");
                        if (!TextUtils.isEmpty(doctorId)) {
                            Intent intent = new Intent(getActivity(), AddFriendsDocActivity.class);
                            intent.putExtra(CommonData.KEY_DOCTOR_ID, doctorId);
                            intent.putExtra(CommonData.KEY_PUBLIC, true);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    AddFriendsPatientActivity.class);
                            intent.putExtra(CommonData.KEY_PATIENT_ID, patientId);
                            intent.putExtra(CommonData.KEY_PUBLIC, true);
                            startActivity(intent);
                        }
                    }
                } else {
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
                    //数据存储
                    DataSupport.deleteAll(CooperateDocBean.class);
                    DataSupport.saveAll(cooperateDocBeanList);
                }
                sharePreferenceUtil.putString(CommonData.KEY_DOCTOR_NUM,
                        String.valueOf(cooperateDocBeanList.size()));
                break;
            case APPLY_COOPERATE_DOC:
                ToastUtil.toast(getActivity(), response.getMsg());
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(getActivity(), response.getMsg());
                getCooperateList();
                break;
            case GET_APPLY_COOPERATE_DOC_LIST:
                ArrayList<CooperateDocBean> list = response.getData();
                if (list.size() > 0) {
                    rlMsgHint.setVisibility(View.VISIBLE);
                    tvNum.setText(String.valueOf(list.size()));
                } else {
                    rlMsgHint.setVisibility(View.GONE);
                }
                sharePreferenceUtil.putString(CommonData.KEY_DOCTOR_APPLY_NUM,
                        String.valueOf(list.size()));
                if (onDocApplyCallbackListener != null) {
                    onDocApplyCallbackListener.onDocApplyCallback();
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

    private OnDocApplyCallbackListener onDocApplyCallbackListener;

    public void setOnDocApplyCallbackListener(OnDocApplyCallbackListener onDocApplyCallbackListener) {
        this.onDocApplyCallbackListener = onDocApplyCallbackListener;
    }

    public interface OnDocApplyCallbackListener {
        void onDocApplyCallback();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorStatusChangeListener(doctorStatusChangeListener
                , RegisterType.UNREGISTER);
    }
}
