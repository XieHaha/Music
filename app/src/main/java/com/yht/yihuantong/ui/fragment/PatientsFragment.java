package com.yht.yihuantong.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.ApplyPatientActivity;
import com.yht.yihuantong.ui.activity.HealthCardActivity;
import com.yht.yihuantong.ui.adapter.PatientsListAdapter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.fragment.BaseFragment;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import custom.frame.widgets.recyclerview.callback.LoadMoreListener;

import static android.app.Activity.RESULT_OK;

/**
 * 患者列表
 *
 * @author DUNDUN
 */
public class PatientsFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private PatientsListAdapter patientsListAdapter;
    private RelativeLayout rlMsgHint;
    private TextView tvNum;
    private ImageView ivTitleBarMore;
    private View headerView, footerView;
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
    private static final int PAGE_SIZE = 20;
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
     * 扫码结果
     */
    public static final int REQUEST_CODE = 0x0000c0de;
    /**
     * 推送回调监听
     */
    private IChange<String> patientStatusChangeListener = data ->
    {
        onResume();
    };

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_patients;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        page = 0;
        getPatientsData();
        getApplyPatientList();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState)
    {
        super.initView(view, savedInstanceState);
        //获取状态栏高度，填充
        View mStateBarFixer = view.findViewById( R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));//填充状态栏

        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("我的患者");
        swipeRefreshLayout = view.findViewById(R.id.fragment_patients_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_patients_recycler_view);
        ivTitleBarMore = view.findViewById(R.id.public_title_bar_more_three);
        ivTitleBarMore.setVisibility(View.VISIBLE);
        headerView = LayoutInflater.from(getContext())
                                   .inflate(R.layout.view_cooperate_doc_header, null);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_footerr, null);
        tvHeanderHintTxt = headerView.findViewById(R.id.view_header_hint_txt);
        rlMsgHint = headerView.findViewById(R.id.message_red_point);
        tvNum = headerView.findViewById(R.id.item_msg_num);
        tvFooterHintTxt = footerView.findViewById(R.id.footer_hint_txt);
        tvHeanderHintTxt.setText("我的患者申请");
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                                                   android.R.color.holo_red_light,
                                                   android.R.color.holo_orange_light,
                                                   android.R.color.holo_green_light);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        patientsListAdapter = new PatientsListAdapter(this, patientBeanList);
        //        patientsListAdapter.addHeaderView(headerView);
        patientsListAdapter.addFooterView(footerView);
        iNotifyChangeListenerServer = ApiManager.getInstance()
                                                .getServer(INotifyChangeListenerServer.class);
    }

    @Override
    public void initListener()
    {
        ivTitleBarMore.setOnClickListener(this);
        headerView.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        autoLoadRecyclerView.setLoadMoreListener(this);
        autoLoadRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        autoLoadRecyclerView.setAdapter(patientsListAdapter);
        patientsListAdapter.setOnItemClickListener((v, position, item) ->
                                                   {
                                                       Intent intent = new Intent(getContext(),
                                                                                  HealthCardActivity.class);
                                                       intent.putExtra(CommonData.KEY_PATIENT_BEAN,
                                                                       item);
                                                       startActivityForResult(intent,
                                                                              REQUEST_CODE_DELETE);
                                                   });
        //注册患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.REGISTER);
    }

    /**
     * 获取患者列表数据
     */
    private void getPatientsData()
    {
        mIRequest.getPatientList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 获取患者申请列表
     */
    private void getApplyPatientList()
    {
        mIRequest.getApplyPatientList(loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    /**
     * 医生扫码添加患者  转诊患者
     * mode {@link #ADD_PATIENT}  {@link #CHANGE_PATIENT}
     */
    private void addPatientByScan(String patientId, int mode)
    {
        mIRequest.addPatientByScan(loginSuccessBean.getDoctorId(), patientId, mode, this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), ApplyPatientActivity.class);
                startActivity(intent);
                break;
            case R.id.public_title_bar_more_three:
                IntentIntegrator.forSupportFragment(this)
                                .setBarcodeImageEnabled(false)
                                .setPrompt("将二维码放入框内，即可自动识别")
                                .initiateScan();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case REQUEST_CODE_DELETE:
                getPatientsData();
                break;
            case REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode,
                                                                           data);
                if (result != null)
                {
                    if (result.getContents() == null)
                    {
                    }
                    else
                    {
                        addPatientByScan(result.getContents(), ADD_PATIENT);
                    }
                }
                else
                {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                getPatientsData();
                break;
        }
    }

    @Override
    public void onRefresh()
    {
        page = 0;
        getPatientsData();
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getPatientsData();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_PATIENTS_LIST:
                if (response.getData() != null)
                {
                    patientBeanList = response.getData();
                    if (page == 0)
                    {
                        patientsListAdapter.setList(patientBeanList);
                    }
                    else
                    {
                        patientsListAdapter.addList(patientBeanList);
                    }
                    patientsListAdapter.notifyDataSetChanged();
                    if (patientBeanList.size() < PAGE_SIZE)
                    {
                        tvFooterHintTxt.setText("暂无更多数据");
                        autoLoadRecyclerView.loadFinish(false);
                    }
                    else
                    {
                        tvFooterHintTxt.setText("上拉加载更多");
                        autoLoadRecyclerView.loadFinish(true);
                    }

                    //数据存储
                    DataSupport.deleteAll(PatientBean.class);
                    DataSupport.saveAll(patientBeanList);
                }
                break;
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(getContext(), "处理成功");
                break;
            case GET_APPLY_PATIENT_LIST:
                ArrayList<PatientBean> list = response.getData();
                if (list != null && list.size() > 0)
                {
                    if (patientsListAdapter.getHeadersCount() == 0)
                    {
                        tvNum.setText(String.valueOf(list.size()));
                        patientsListAdapter.addHeaderView(headerView);
                        patientsListAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if (patientsListAdapter.getHeadersCount() > 0)
                    {
                        patientsListAdapter.removeHeaderView(headerView);
                        patientsListAdapter.notifyDataSetChanged();
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
        tvFooterHintTxt.setText("暂无更多数据");
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
        tvFooterHintTxt.setText("暂无更多数据");
        autoLoadRecyclerView.loadFinish();
    }

    @Override
    public void onResponseEnd(Tasks task)
    {
        super.onResponseEnd(task);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注册患者状态监听
        iNotifyChangeListenerServer.registerPatientStatusChangeListener(patientStatusChangeListener,
                                                                        RegisterType.UNREGISTER);
    }
}
