package com.zyc.doctor.ui.activity;

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
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.chat.ChatActivity;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.http.bean.BaseResponse;
import com.zyc.doctor.http.bean.CooperateDocBean;
import com.zyc.doctor.http.bean.CooperateHospitalBean;
import com.zyc.doctor.http.bean.HospitalBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.adapter.CooperationDocHAdapter;
import com.zyc.doctor.ui.adapter.CooperationHospitalHAdapter;
import com.zyc.doctor.ui.adapter.base.BaseRecyclerAdapter;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.GlideHelper;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.recyclerview.AutoLoadRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息界面
 *
 * @author DUNDUN
 */
public class DoctorInfoActivity extends BaseActivity
        implements BaseRecyclerAdapter.OnItemClickListener<CooperateDocBean> {
    private static final String TAG = "DoctorInfoActivity";
    @BindView(R.id.public_title_bar_more_two)
    ImageView ivMore;
    @BindView(R.id.act_user_info_headimg)
    CircleImageView ivHeadImg;
    @BindView(R.id.act_user_info_name)
    TextView tvName;
    @BindView(R.id.act_user_info_hospital)
    TextView tvHospital;
    @BindView(R.id.act_user_info_title)
    TextView tvTitle;
    @BindView(R.id.act_user_info_type)
    TextView tvType;
    @BindView(R.id.act_user_info_hospital_verify)
    TextView tvHospitalVerify;
    @BindView(R.id.act_user_info_platform_verify)
    TextView tvPlatformVerify;
    @BindView(R.id.act_user_info_introduce)
    TextView tvIntroduce;
    @BindView(R.id.act_user_info_recycler)
    AutoLoadRecyclerView recyclerView;
    @BindView(R.id.act_user_info_cooperate_doc_layout)
    LinearLayout llCoopDocLayout;
    @BindView(R.id.act_user_info_hospital_recycler)
    AutoLoadRecyclerView hospitalRecyclerView;
    @BindView(R.id.act_user_info_chat)
    TextView tvChat;
    private TextView tvChange, tvDelete;
    private PopupWindow mPopupwinow;
    private CooperateDocBean cooperateDocBean;
    private CooperationDocHAdapter cooperationDocHAdapter;
    private CooperationHospitalHAdapter cooperationHospitalHAdapter;
    private String doctorId, doctorNickName;
    private String headImgUrl;
    private View viewPop;
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
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_doctor_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("医生信息");
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        findViewById(R.id.public_title_bar_more_two).setOnClickListener(this);
        ivMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        if (getIntent() != null) {
            cooperateDocBean = (CooperateDocBean)getIntent().getSerializableExtra(CommonData.KEY_DOCTOR_BEAN);
            isDealDoc = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
            isForbidChat = getIntent().getBooleanExtra(CommonData.KEY_IS_FORBID_CHAT, false);
        }
        if (isDealDoc) {
            ivMore.setVisibility(View.VISIBLE);
        }
        else {
            ivMore.setVisibility(View.GONE);
        }
        if (isForbidChat) {
            tvChat.setVisibility(View.GONE);
        }
        else {
            tvChat.setVisibility(View.VISIBLE);
        }
        //合作医生
        cooperationDocHAdapter = new CooperationDocHAdapter(this, cooperateDocBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cooperationDocHAdapter);
        cooperationDocHAdapter.setOnItemClickListener(this);
        //合作医院
        cooperationHospitalHAdapter = new CooperationHospitalHAdapter(this, cooperateHospitalBeans);
        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hospitalRecyclerView.setItemAnimator(new DefaultItemAnimator());
        hospitalRecyclerView.setAdapter(cooperationHospitalHAdapter);
        cooperationHospitalHAdapter.setOnItemClickListener((v, position, item) -> {
            HospitalBean hospitalBean = new HospitalBean();
            hospitalBean.setHospitalName(item.getHospitalName());
            hospitalBean.setAddress(item.getAddress());
            hospitalBean.setCityName(item.getCityName());
            hospitalBean.setHospitalPhone(item.getPhone());
            hospitalBean.setHospitalLevel(item.getHospitalLevel());
            hospitalBean.setHospitalDescription(item.getHospitalDescription());
            Intent intent = new Intent(DoctorInfoActivity.this, HospitalInfoActivity.class);
            intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, hospitalBean);
            startActivity(intent);
        });
        initPageData();
        getCooperationDocList();
        getCooperateHospitalList();
    }

    @Override
    public void initListener() {
        super.initListener();
        tvChat.setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData() {
        if (cooperateDocBean != null) {
            doctorId = cooperateDocBean.getDoctorId();
            headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            }
            if (!TextUtils.isEmpty(doctorNickName) && doctorNickName.length() < 20) {
                tvName.setText(doctorNickName + "(" + cooperateDocBean.getName() + ")");
            }
            else {
                if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) &&
                    cooperateDocBean.getNickname().length() < 20) {
                    tvName.setText(cooperateDocBean.getNickname() + "(" + cooperateDocBean.getName() + ")");
                }
                else {
                    tvName.setText(cooperateDocBean.getName());
                }
            }
            tvHospital.setText(cooperateDocBean.getHospital());
            tvTitle.setText(cooperateDocBean.getTitle());
            tvType.setText(cooperateDocBean.getDepartment());
            if (cooperateDocBean.getHospitalAuthorityCode() == 1) {
                tvHospitalVerify.setSelected(true);
            }
            else {
                tvHospitalVerify.setSelected(false);
            }
            if (cooperateDocBean.getChecked() == 6) {
                tvPlatformVerify.setSelected(true);
            }
            else {
                tvPlatformVerify.setSelected(false);
            }
            if (!TextUtils.isEmpty(cooperateDocBean.getDoctorDescription())) {
                tvIntroduce.setText(cooperateDocBean.getDoctorDescription());
            }
            else {
                tvIntroduce.setText("暂无简介");
            }
        }
    }

    /**
     * 取消关注医生
     */
    private void deleteDoctor() {
        RequestUtils.cancelCooperateDoc(this, loginSuccessBean.getDoctorId(), cooperateDocBean.getDoctorId(), this);
    }

    /**
     * 获取合作医生列表
     */
    private void getCooperationDocList() {
        RequestUtils.getCooperateList(this, doctorId, 0, PAGE_SIZE, this);
    }

    /**
     * 获取合作医院
     */
    private void getCooperateHospitalList() {
        RequestUtils.getHospitalListByDoctorId(this, doctorId, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.change:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                new SimpleDialog(this, "确定删除?", (dialog, which) -> {
                    deleteDoctor();
                }, (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.remark:
                if (mPopupwinow != null) {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(this, EditRemarkActivity.class);
                if (!TextUtils.isEmpty(doctorNickName)) {
                    intent.putExtra(CommonData.KEY_PUBLIC, doctorNickName);
                }
                else {
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
                if (cooperateDocBean != null) {
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, cooperateDocBean.getDoctorId());
                    if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) &&
                        cooperateDocBean.getNickname().length() < 20) {
                        intent.putExtra(CommonData.KEY_CHAT_NAME, cooperateDocBean.getNickname());
                        YihtApplication.getInstance().setEaseName(cooperateDocBean.getNickname());
                    }
                    else {
                        intent.putExtra(CommonData.KEY_CHAT_NAME, cooperateDocBean.getName());
                        //存储临时数据
                        YihtApplication.getInstance().setEaseName(cooperateDocBean.getName());
                    }
                    YihtApplication.getInstance().setEaseHeadImgUrl(cooperateDocBean.getPortraitUrl());
                    startActivity(intent);
                }
                break;
            default:
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
    public void onItemClick(View v, int position, CooperateDocBean item) {
        Intent intent = new Intent(this, DoctorInfoActivity.class);
        intent.putExtra(CommonData.KEY_DOCTOR_BEAN, item);
        intent.putExtra(CommonData.KEY_IS_FORBID_CHAT, true);
        startActivity(intent);
    }

    /**
     * 显示pop
     */
    private void showPop() {
        viewPop = LayoutInflater.from(this).inflate(R.layout.main_pop_menu_p_dianjiu, null);
        tvChange = (TextView)viewPop.findViewById(R.id.remark);
        tvChange.setOnClickListener(this);
        tvDelete = (TextView)viewPop.findViewById(R.id.change);
        tvDelete.setOnClickListener(this);
        if (mPopupwinow == null) {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(viewPop, LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT, true);
        }
        mPopupwinow.setFocusable(true);
        mPopupwinow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupwinow.setOutsideTouchable(true);
        mPopupwinow.showAtLocation(viewPop, Gravity.TOP | Gravity.RIGHT, 0, (int)AllUtils.dipToPx(this, 55));
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                ToastUtil.toast(this, response.getMsg());
                headImgUrl = (String)response.getData();
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            case GET_COOPERATE_DOC_LIST:
                ArrayList<CooperateDocBean> list = (ArrayList<CooperateDocBean>)response.getData();
                if (list != null && list.size() > 0) {
                    llCoopDocLayout.setVisibility(View.VISIBLE);
                    cooperationDocHAdapter.setList(list);
                }
                else {
                    llCoopDocLayout.setVisibility(View.GONE);
                }
                break;
            case GET_HOSPITAL_LIST_BY_DOCTORID:
                ArrayList<CooperateHospitalBean> list1 = (ArrayList<CooperateHospitalBean>)response.getData();
                cooperationHospitalHAdapter.setList(list1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case MODIFY_NICKNAME:
                setResult(RESULT_OK);
                if (data != null) {
                    String remark = data.getStringExtra(CommonData.KEY_PUBLIC);
                    if (!TextUtils.isEmpty(remark)) {
                        tvName.setText(remark + "(" + cooperateDocBean.getName() + ")");
                        cooperateDocBean.setNickname(remark);
                    }
                    else {
                        tvName.setText(cooperateDocBean.getName());
                        cooperateDocBean.setNickname(remark);
                    }
                }
                break;
            default:
                break;
        }
    }
}
