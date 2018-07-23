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
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.ApplyCooperateDocActivity;
import com.yht.yihuantong.ui.activity.UserInfoActivity;
import com.yht.yihuantong.ui.adapter.CooperateDocListAdapter;
import com.yht.yihuantong.ui.dialog.SimpleDialog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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
public class CooperateDocFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, LoadMoreListener
{
    private TextView tvHintTxt;
    private ImageView ivTitleBarMore;
    private TextView tvNum;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView autoLoadRecyclerView;
    private View headerView, footerView;
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
    private static final int PAGE_SIZE = 20;
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
    private IChange<String> doctorStatusChangeListener = data ->
    {
        getCooperateList();
    };

    @Override
    public int getLayoutID()
    {
        return R.layout.fragment_cooperate_doc;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getCooperateList();
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
        ((TextView)view.findViewById(R.id.public_title_bar_title)).setText("合作医生");
        ivTitleBarMore = view.findViewById(R.id.public_title_bar_more_three);
        ivTitleBarMore.setVisibility(View.VISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.fragment_cooperate_swipe_layout);
        autoLoadRecyclerView = view.findViewById(R.id.fragment_cooperate_recycler_view);
        headerView = LayoutInflater.from(getContext())
                                   .inflate(R.layout.view_cooperate_doc_header, null);
        tvNum = headerView.findViewById(R.id.item_msg_num);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_list_footerr, null);
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
        cooperateDocListAdapter = new CooperateDocListAdapter(getContext(), cooperateDocBeanList);
        //        cooperateDocListAdapter.addHeaderView(headerView);
        cooperateDocListAdapter.addFooterView(footerView);
        page = 0;
        iNotifyChangeListenerServer = ApiManager.getInstance()
                                                .getServer(INotifyChangeListenerServer.class);
        //获取合作医生申请
        getApplyCooperateList();
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
        autoLoadRecyclerView.setAdapter(cooperateDocListAdapter);
        cooperateDocListAdapter.setOnItemClickListener((v, position, item) ->
                                                       {
                                                           Intent intent = new Intent(getContext(),
                                                                                      UserInfoActivity.class);
                                                           intent.putExtra(
                                                                   CommonData.KEY_DOCTOR_BEAN,
                                                                   item);
                                                           intent.putExtra(
                                                                   CommonData.KEY_IS_DEAL_DOC,
                                                                   true);
                                                           startActivityForResult(intent,
                                                                                  REQUEST_CODE_CANCEL_DOC);
                                                       });
        cooperateDocListAdapter.setOnItemLongClickListener(
                (v, position, item) -> new SimpleDialog(getActivity(), "确定取消关注?",
                                                        (dialog, which) -> cancelCooperateDoc(
                                                                item.getDoctorId()),
                                                        (dialog, which) -> dialog.dismiss()).show());
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorStatusChangeListener(doctorStatusChangeListener,
                                                                       RegisterType.REGISTER);
    }

    /**
     * 获取合作医生列表数据
     */
    private void getCooperateList()
    {
        mIRequest.getCooperateList(loginSuccessBean.getDoctorId(), page, PAGE_SIZE, this);
    }

    /**
     * 合作医生申请
     */
    private void applyCooperateDoc(String doctorId, int requestCode)
    {
        mIRequest.applyCooperateDoc(loginSuccessBean.getDoctorId(), doctorId, requestCode, this);
    }

    /**
     * 合作医生申请
     */
    private void cancelCooperateDoc(String doctorId)
    {
        mIRequest.cancelCooperateDoc(loginSuccessBean.getDoctorId(), doctorId, this);
    }

    /**
     * 获取申请合作医生列表数据
     */
    private void getApplyCooperateList()
    {
        mIRequest.getApplyCooperateList(loginSuccessBean.getDoctorId(), 0, PAGE_SIZE, this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.fragment_cooperate_apply_layout:
                Intent intent = new Intent(getContext(), ApplyCooperateDocActivity.class);
                startActivity(intent);
                break;
            case R.id.public_title_bar_more_three:
                IntentIntegrator.forSupportFragment(this)
                                .setBarcodeImageEnabled(false)
                                .setPrompt("将二维码放入框内，即可自动识别")
                                .initiateScan();
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
                        applyCooperateDoc(result.getContents(), 1);
                    }
                }
                else
                {
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
    public void onRefresh()
    {
        page = 0;
        getCooperateList();
        getApplyCooperateList();
    }

    @Override
    public void loadMore()
    {
        swipeRefreshLayout.setRefreshing(true);
        page++;
        getCooperateList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case GET_COOPERATE_DOC_LIST:
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
                    //数据存储
                    DataSupport.deleteAll(CooperateDocBean.class);
                    DataSupport.saveAll(cooperateDocBeanList);
                }
                break;
            case APPLY_COOPERATE_DOC:
                ToastUtil.toast(getContext(), "处理成功");
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(getContext(), "处理成功");
                getCooperateList();
                break;
            case GET_APPLY_COOPERATE_DOC_LIST:
                ArrayList<CooperateDocBean> list = response.getData();
                if (list != null && list.size() > 0)
                {
                    if (cooperateDocListAdapter.getHeadersCount() == 0)
                    {
                        tvNum.setText(String.valueOf(list.size()));
                        cooperateDocListAdapter.addHeaderView(headerView);
                        cooperateDocListAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if (cooperateDocListAdapter.getHeadersCount() > 0)
                    {
                        cooperateDocListAdapter.removeHeaderView(headerView);
                        cooperateDocListAdapter.notifyDataSetChanged();
                    }
                }
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //注册患者状态监听
        iNotifyChangeListenerServer.registerDoctorStatusChangeListener(doctorStatusChangeListener,
                                                                       RegisterType.UNREGISTER);
    }
}
