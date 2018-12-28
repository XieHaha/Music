package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.chat.ChatActivity;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.adapter.CooperationDocHAdapter;
import com.yht.yihuantong.ui.adapter.CooperationHospitalHAdapter;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.CooperateHospitalBean;
import custom.frame.bean.HospitalBean;
import custom.frame.http.Tasks;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.ui.adapter.BaseRecyclerAdapter;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.recyclerview.AutoLoadRecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息界面
 *
 * @author DUNDUN
 */
public class DoctorInfoActivity extends BaseActivity
        implements BaseRecyclerAdapter.OnItemClickListener<CooperateDocBean>
{
    private CircleImageView ivHeadImg;
    private ImageView ivMore;
    private TextView tvName, tvType, tvTitle, tvIntroduce, tvHospital, tvChat, tvAuthStatus;
    private TextView tvChange, tvDelete;
    private AutoLoadRecyclerView recyclerView, hospitalRecyclerView;
    private PopupWindow mPopupwinow;
    /**
     * 2018年10月9日10:59:42
     */
    private LinearLayout llCoopHopitalLayout, llCoopDocLayout;
    private CooperateDocBean cooperateDocBean;
    private CooperationDocHAdapter cooperationDocHAdapter;
    private CooperationHospitalHAdapter cooperationHospitalHAdapter;
    private String doctorId, doctorNickName;
    private String headImgUrl;
    private View view_pop;
    /**
     * 合作医生列表
     */
    private ArrayList<CooperateDocBean> cooperateDocBeans = new ArrayList<>();
    /**
     * 合作医院列表
     */
    private ArrayList<CooperateHospitalBean> cooperateHospitalBeans = new ArrayList<>();
    /**
     * 是否可以取消关注
     */
    private boolean isDealDoc;
    /**
     * 是否禁止聊天
     */
    private boolean isForbidChat;
    /**
     * 添加备注名
     */
    private static final int MODIFY_NICKNAME = 100;
    /**
     * 一页最大数
     */
    private static final int PAGE_SIZE = 500;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_doctor_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("医生信息");
        ivHeadImg = (CircleImageView)findViewById(R.id.act_user_info_headimg);
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        ivMore = (ImageView)findViewById(R.id.public_title_bar_more_two);
        ivMore.setVisibility(View.VISIBLE);
        findViewById(R.id.public_title_bar_more_two).setOnClickListener(this);
        tvName = (TextView)findViewById(R.id.act_user_info_name);
        tvHospital = (TextView)findViewById(R.id.act_user_info_hospital);
        tvTitle = (TextView)findViewById(R.id.act_user_info_title);
        tvType = (TextView)findViewById(R.id.act_user_info_type);
        tvChat = (TextView)findViewById(R.id.act_user_info_chat);
        tvAuthStatus = (TextView)findViewById(R.id.act_user_info_auth_status);
        tvIntroduce = (TextView)findViewById(R.id.act_user_info_introduce);
        recyclerView = (AutoLoadRecyclerView)findViewById(R.id.act_user_info_recycler);
        hospitalRecyclerView = (AutoLoadRecyclerView)findViewById(
                R.id.act_user_info_hospital_recycler);
        llCoopDocLayout = (LinearLayout)findViewById(R.id.act_user_info_cooperate_doc_layout);
        llCoopHopitalLayout = (LinearLayout)findViewById(R.id.act_user_info_coop_hospital_layout);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        if (getIntent() != null)
        {
            cooperateDocBean = (CooperateDocBean)getIntent().getSerializableExtra(
                    CommonData.KEY_DOCTOR_BEAN);
            isDealDoc = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
            isForbidChat = getIntent().getBooleanExtra(CommonData.KEY_IS_FORBID_CHAT, false);
        }
        if (isDealDoc)
        {
            ivMore.setVisibility(View.VISIBLE);
        }
        else
        {
            ivMore.setVisibility(View.GONE);
        }
        if (isForbidChat)
        {
            tvChat.setVisibility(View.GONE);
        }
        else
        {
            tvChat.setVisibility(View.VISIBLE);
        }
        //合作医生
        cooperationDocHAdapter = new CooperationDocHAdapter(this, cooperateDocBeans);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cooperationDocHAdapter);
        cooperationDocHAdapter.setOnItemClickListener(this);
        //合作医院
        cooperationHospitalHAdapter = new CooperationHospitalHAdapter(this, cooperateHospitalBeans);
        hospitalRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hospitalRecyclerView.setAdapter(cooperationHospitalHAdapter);
        cooperationHospitalHAdapter.setOnItemClickListener((v, position, item) ->
                                                           {
                                                               HospitalBean hospitalBean = new HospitalBean();
                                                               hospitalBean.setHospitalName(
                                                                       item.getHospitalName());
                                                               hospitalBean.setAddress(
                                                                       item.getAddress());
                                                               hospitalBean.setCityName(
                                                                       item.getCityName());
                                                               hospitalBean.setHospitalPhone(
                                                                       item.getPhone());
                                                               hospitalBean.setHospitalLevel(
                                                                       item.getHospitalLevel());
                                                               hospitalBean.setHospitalDescription(
                                                                       item.getHospitalDescription());
                                                               Intent intent = new Intent(
                                                                       DoctorInfoActivity.this,
                                                                       HospitalInfoActivity.class);
                                                               intent.putExtra(
                                                                       CommonData.KEY_HOSPITAL_BEAN,
                                                                       hospitalBean);
                                                               startActivity(intent);
                                                           });
        initPageData();
        getCooperationDocList();
        getCooperateHospitalList();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvChat.setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        if (cooperateDocBean != null)
        {
            doctorId = cooperateDocBean.getDoctorId();
            headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl))
            {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            }
            if (!TextUtils.isEmpty(doctorNickName) && doctorNickName.length() < 20)
            {
                tvName.setText(doctorNickName + "(" + cooperateDocBean.getName() + ")");
            }
            else
            {
                if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) &&
                    cooperateDocBean.getNickname().length() < 20)
                {
                    tvName.setText(
                            cooperateDocBean.getNickname() + "(" + cooperateDocBean.getName() +
                            ")");
                }
                else
                {
                    tvName.setText(cooperateDocBean.getName());
                }
            }
            tvHospital.setText(cooperateDocBean.getHospital());
            tvTitle.setText(cooperateDocBean.getTitle());
            tvType.setText(cooperateDocBean.getDepartment());
            if (!TextUtils.isEmpty(cooperateDocBean.getDoctorDescription()))
            {
                tvIntroduce.setText(cooperateDocBean.getDoctorDescription());
            }
            else
            {
                tvIntroduce.setText("暂无简介");
            }
            if (6 == cooperateDocBean.getChecked())
            {
                tvAuthStatus.setText("已认证");
                tvAuthStatus.setSelected(true);
            }
            else
            {
                tvAuthStatus.setText("待认证");
                tvAuthStatus.setSelected(false);
            }
        }
    }

    /**
     * 取消关注医生
     */
    private void deleteDoctor()
    {
        mIRequest.cancelCooperateDoc(loginSuccessBean.getDoctorId(), cooperateDocBean.getDoctorId(),
                                     this);
    }

    /**
     * 获取合作医生列表
     */
    private void getCooperationDocList()
    {
        mIRequest.getCooperateList(doctorId, 0, PAGE_SIZE, this);
    }

    /**
     * 获取合作医院
     */
    private void getCooperateHospitalList()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/hospital/doctor/relation/list",
                RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("doctorId", doctorId);
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
                                                                      CooperateHospitalBean.class);
                    if (baseResponse != null && baseResponse.getCode() == 200)
                    {
                        ArrayList<CooperateHospitalBean> list = baseResponse.getData();
                        cooperationHospitalHAdapter.setList(list);
                    }
                    else
                    {
                        ToastUtil.toast(DoctorInfoActivity.this, baseResponse.getMsg());
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
                ToastUtil.toast(DoctorInfoActivity.this, response.getException().getMessage());
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
        super.onClick(v);
        Intent intent;
        switch (v.getId())
        {
            case R.id.change:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                new SimpleDialog(this, "确定删除?", (dialog, which) ->
                {
                    deleteDoctor();
                }, (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.remark:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(this, EditRemarkActivity.class);
                if (!TextUtils.isEmpty(doctorNickName))
                {
                    intent.putExtra(CommonData.KEY_PUBLIC, doctorNickName);
                }
                else
                {
                    intent.putExtra(CommonData.KEY_PUBLIC, cooperateDocBean.getNickname());
                }
                intent.putExtra(CommonData.KEY_DOCTOR_ID, cooperateDocBean.getDoctorId());
                intent.putExtra(CommonData.KEY_IS_DEAL_DOC, true);
                intent.putExtra(CommonData.KEY_ID, doctorId);
                startActivityForResult(intent, MODIFY_NICKNAME);
                break;
            case R.id.public_title_bar_back:
                finish();
                break;
            case R.id.public_title_bar_more_two:
                showPop();
                break;
            case R.id.act_user_info_chat:
                if (cooperateDocBean != null)
                {
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, cooperateDocBean.getDoctorId());
                    if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) &&
                        cooperateDocBean.getNickname().length() < 20)
                    {
                        intent.putExtra(CommonData.KEY_CHAT_NAME, cooperateDocBean.getNickname());
                        YihtApplication.getInstance().setEaseName(cooperateDocBean.getNickname());
                    }
                    else
                    {
                        intent.putExtra(CommonData.KEY_CHAT_NAME, cooperateDocBean.getName());
                        //存储临时数据
                        YihtApplication.getInstance().setEaseName(cooperateDocBean.getName());
                    }
                    YihtApplication.getInstance()
                                   .setEaseHeadImgUrl(cooperateDocBean.getPortraitUrl());
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 合作医生列表
     *
     * @param v
     * @param position
     * @param item
     */
    @Override
    public void onItemClick(View v, int position, CooperateDocBean item)
    {
        Intent intent = new Intent(this, DoctorInfoActivity.class);
        intent.putExtra(CommonData.KEY_DOCTOR_BEAN, item);
        intent.putExtra(CommonData.KEY_IS_FORBID_CHAT, true);
        startActivity(intent);
    }

    /**
     * 显示pop
     */
    private void showPop()
    {
        view_pop = LayoutInflater.from(this).inflate(R.layout.main_pop_menu_p_dianjiu, null);
        tvChange = (TextView)view_pop.findViewById(R.id.remark);
        tvChange.setOnClickListener(this);
        tvDelete = (TextView)view_pop.findViewById(R.id.change);
        tvDelete.setOnClickListener(this);
        if (mPopupwinow == null)
        {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(view_pop, LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(view_pop, Gravity.TOP | Gravity.RIGHT, 0,
                                   (int)AllUtils.dipToPx(this, 55));
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case UPLOAD_FILE:
                ToastUtil.toast(this, response.getMsg());
                headImgUrl = response.getData();
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            case GET_COOPERATE_DOC_LIST:
                ArrayList<CooperateDocBean> list = response.getData();
                if (list != null && list.size() > 0)
                {
                    llCoopDocLayout.setVisibility(View.VISIBLE);
                    cooperationDocHAdapter.setList(list);
                }
                else
                {
                    llCoopDocLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case MODIFY_NICKNAME:
                setResult(RESULT_OK);
                if (data != null)
                {
                    String remark = data.getStringExtra(CommonData.KEY_PUBLIC);
                    if (!TextUtils.isEmpty(remark))
                    {
                        tvName.setText(remark + "(" + cooperateDocBean.getName() + ")");
                        cooperateDocBean.setNickname(remark);
                    }
                    else
                    {
                        tvName.setText(cooperateDocBean.getName());
                        cooperateDocBean.setNickname(remark);
                    }
                }
                break;
        }
    }
}
