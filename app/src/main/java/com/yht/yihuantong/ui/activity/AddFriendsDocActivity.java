package com.yht.yihuantong.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.notify.NotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dundun on 18/11/12.
 * 好友验证界面
 */
public class AddFriendsDocActivity extends BaseActivity
{
    private TextView tvTitle;
    private CircleImageView ivHeadImg;
    private TextView tvName, tvType, tvDepart, tvIntroduce, tvHospital;
    private TextView tvAgree, tvRefuse;
    private String doctorId;
    private int requestSource;
    /**
     * 是否是主动申请
     */
    private boolean isAdd;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_add_friend_doc;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_add_friend_img);
        tvName = (TextView)findViewById(R.id.act_add_friend_name);
        tvType = (TextView)findViewById(R.id.act_add_friend_type);
        tvDepart = (TextView)findViewById(R.id.act_add_friend_depart);
        tvHospital = (TextView)findViewById(R.id.act_add_friend_hospital);
        tvIntroduce = (TextView)findViewById(R.id.act_add_friend_des);
        tvAgree = (TextView)findViewById(R.id.act_add_friend_next);
        tvRefuse = (TextView)findViewById(R.id.act_add_friend_refuse);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            isAdd = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            doctorId = getIntent().getStringExtra(CommonData.KEY_DOCTOR_ID);
            requestSource = getIntent().getIntExtra("requestSource", -1);
        }
        if (isAdd)
        {
            tvTitle.setText("添加好友");
            tvAgree.setText("加为好友");
            tvRefuse.setVisibility(View.GONE);
        }
        else
        {
            tvTitle.setText("好友申请");
            tvAgree.setText("通过验证");
            tvRefuse.setVisibility(View.VISIBLE);
        }
        getDocInfo();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvAgree.setOnClickListener(this);
        tvRefuse.setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData(CooperateDocBean cooperateDocBean)
    {
        if (cooperateDocBean != null)
        {
            String headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl))
            {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            }
            if (!TextUtils.isEmpty(cooperateDocBean.getNickname()) &&
                cooperateDocBean.getNickname().length() < 20)
            {
                tvName.setText(
                        cooperateDocBean.getNickname() + "(" + cooperateDocBean.getName() + ")");
            }
            else
            {
                tvName.setText(cooperateDocBean.getName());
            }
            tvDepart.setText(cooperateDocBean.getDepartment());
            tvHospital.setText(cooperateDocBean.getHospital());
            tvType.setText(cooperateDocBean.getTitle());
            if (!TextUtils.isEmpty(cooperateDocBean.getDoctorDescription()))
            {
                tvIntroduce.setText(cooperateDocBean.getDoctorDescription());
            }
            else
            {
                tvIntroduce.setText("暂无个人简介");
            }
        }
    }

    /**
     * 获取个人信息
     */
    private void getDocInfo()
    {
        mIRequest.getDocInfo(doctorId, this);
    }

    /**
     * 合作医生申请
     */
    private void applyCooperateDoc()
    {
        mIRequest.applyCooperateDoc(loginSuccessBean.getDoctorId(), doctorId, 1, this);
    }

    /**
     * 处理医生合作申请
     */
    private void dealDocApply(int way)
    {
        mIRequest.dealDocApply(loginSuccessBean.getDoctorId(), doctorId, way, requestSource, this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.act_add_friend_next:
                if (isAdd)
                {
                    applyCooperateDoc();
                }
                else
                {
                    dealDocApply(1);
                }
                break;
            case R.id.act_add_friend_refuse:
                dealDocApply(3);
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case APPLY_COOPERATE_DOC:
                ToastUtil.toast(this, response.getMsg());
                finish();
                break;
            case GET_DOC_INFO:
                CooperateDocBean cooperateDocBean = response.getData();
                initPageData(cooperateDocBean);
                break;
            case DEAL_DOC_APPLY:
                ToastUtil.toast(this, response.getMsg());
                //通知合作医生列表
                NotifyChangeListenerServer.getInstance().notifyDoctorStatusChange("");
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
