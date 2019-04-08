package com.zyc.doctor.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyc.doctor.R;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.ToastUtil;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author dundun
 * @date 18/11/12
 * 好友验证界面
 */
public class AddFriendsDocActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.act_add_friend_img)
    CircleImageView actAddFriendImg;
    @BindView(R.id.act_add_friend_name)
    TextView tvName;
    @BindView(R.id.act_add_friend_type)
    TextView tvType;
    @BindView(R.id.act_add_friend_depart)
    TextView tvDepart;
    @BindView(R.id.act_add_friend_hospital)
    TextView tvHospital;
    @BindView(R.id.act_add_friend_des)
    TextView tvIntroduce;
    @BindView(R.id.act_add_friend_next)
    TextView tvAgree;
    @BindView(R.id.act_add_friend_refuse)
    TextView tvRefuse;
    private String doctorId;
    private int requestSource;
    /**
     * 是否是主动申请
     */
    private boolean isAdd;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_friend_doc;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isAdd = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            doctorId = getIntent().getStringExtra(CommonData.KEY_DOCTOR_ID);
            requestSource = getIntent().getIntExtra("requestSource", -1);
        }
        if (isAdd) {
            publicTitleBarTitle.setText("添加好友");
            tvAgree.setText("加为好友");
            tvRefuse.setVisibility(View.GONE);
        }
        else {
            publicTitleBarTitle.setText("好友申请");
            tvAgree.setText("通过验证");
            tvRefuse.setVisibility(View.VISIBLE);
        }
        if (loginSuccessBean.getDoctorId().equals(doctorId)) {
            tvAgree.setVisibility(View.GONE);
        }
        else {
            tvAgree.setVisibility(View.VISIBLE);
        }
        getDocInfo();
    }

    @Override
    public void initListener() {
        super.initListener();
        tvAgree.setOnClickListener(this);
        tvRefuse.setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData(CooperateDocBean cooperateDocBean) {
        if (cooperateDocBean != null) {
            String headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(actAddFriendImg);
            }
            if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) && cooperateDocBean.getNickname().length() < 20) {
                tvName.setText(cooperateDocBean.getNickname() + "(" + cooperateDocBean.getName() + ")");
            }
            else {
                tvName.setText(cooperateDocBean.getName());
            }
            tvDepart.setText(cooperateDocBean.getDepartment());
            tvHospital.setText(cooperateDocBean.getHospital());
            tvType.setText(cooperateDocBean.getTitle());
            if (!TextUtils.isEmpty(cooperateDocBean.getDoctorDescription())) {
                tvIntroduce.setText(cooperateDocBean.getDoctorDescription());
            }
            else {
                tvIntroduce.setText("暂无个人简介");
            }
        }
    }

    /**
     * 获取个人信息
     */
    private void getDocInfo() {
        RequestUtils.getDocInfo(this, doctorId, this);
    }

    /**
     * 合作医生申请
     */
    private void applyCooperateDoc() {
        RequestUtils.applyCooperateDoc(this, loginSuccessBean.getDoctorId(), doctorId, 1, this);
    }

    /**
     * 处理医生合作申请
     */
    private void dealDocApply(int way) {
        RequestUtils.dealDocApply(this, loginSuccessBean.getDoctorId(), doctorId, way, requestSource, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_add_friend_next:
                if (isAdd) {
                    applyCooperateDoc();
                }
                else {
                    dealDocApply(1);
                }
                break;
            case R.id.act_add_friend_refuse:
                dealDocApply(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case APPLY_COOPERATE_DOC:
                ToastUtil.toast(this, response.getMsg());
                finish();
                break;
            case GET_DOC_INFO:
                CooperateDocBean cooperateDocBean = (CooperateDocBean)response.getData();
                initPageData(cooperateDocBean);
                break;
            case DEAL_DOC_APPLY:
                ToastUtil.toast(this, response.getMsg());
                //通知合作医生列表
                NotifyChangeListenerManager.getInstance().notifyDoctorStatusChange("");
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }
}
