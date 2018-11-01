package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.chat.ChatActivity;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息界面
 *
 * @author DUNDUN
 */
public class UserInfoActivity extends BaseActivity
{
    private CircleImageView ivHeadImg, imgAuth;
    private ImageView ivTitleMore;
    private TextView tvName, tvType, tvTitle, tvIntroduce, tvHospital;
    private TextView tvChat;
    private View view_pop;
    private PopupWindow mPopupwinow;
    private TextView tvDelete, tvChange;
    /**
     * 2018年10月9日10:59:42
     */
    private LinearLayout llCoopHopitalLayout, llHospitalLayout;
    private CooperateDocBean cooperateDocBean;
    private String doctorId;
    private String headImgUrl;
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

    @Override
    public int getLayoutID()
    {
        return R.layout.act_user_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_user_info_headimg);
        imgAuth = (CircleImageView)findViewById(R.id.act_user_info_auth);
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        ivTitleMore = (ImageView)findViewById(R.id.act_user_info_more);
        ivTitleMore.setOnClickListener(this);
        tvChat = (TextView)findViewById(R.id.act_user_info_chat);
        tvName = (TextView)findViewById(R.id.act_user_info_name);
        tvHospital = (TextView)findViewById(R.id.act_user_info_hospital);
        tvTitle = (TextView)findViewById(R.id.act_user_info_title);
        tvType = (TextView)findViewById(R.id.act_user_info_type);
        tvIntroduce = (TextView)findViewById(R.id.act_user_info_introduce);
        llCoopHopitalLayout = (LinearLayout)findViewById(R.id.act_user_info_coop_hospital_layout);
        llHospitalLayout = (LinearLayout)findViewById(R.id.act_user_info_hospital_layout);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        if (getIntent() != null)
        {
            cooperateDocBean = (CooperateDocBean)getIntent().getSerializableExtra(
                    CommonData.KEY_DOCTOR_BEAN);
            doctorId = getIntent().getStringExtra(CommonData.KEY_DOCTOR_ID);
            isDealDoc = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
            isForbidChat = getIntent().getBooleanExtra(CommonData.KEY_IS_FORBID_CHAT, false);
        }
        if (isDealDoc)
        {
            ivTitleMore.setVisibility(View.VISIBLE);
        }
        else
        {
            ivTitleMore.setVisibility(View.GONE);
        }
        if (isForbidChat)
        {
            tvChat.setVisibility(View.GONE);
        }
        else
        {
            tvChat.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(doctorId))
        {
            List<CooperateDocBean> list = DataSupport.where("doctorId = ?", doctorId)
                                                     .find(CooperateDocBean.class);
            if (list != null && list.size() > 0)
            {
                cooperateDocBean = list.get(0);
            }
        }
        if (cooperateDocBean == null)
        {
            getDocInfo();
        }
        else
        {
            initPageData();
        }
    }

    @Override
    public void initListener()
    {
        tvChat.setOnClickListener(this);
        llHospitalLayout.setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initPageData()
    {
        if (cooperateDocBean != null)
        {
            headImgUrl = cooperateDocBean.getPortraitUrl();
            if (!TextUtils.isEmpty(headImgUrl))
            {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(ivHeadImg);
            }
            if (6 == cooperateDocBean.getChecked())
            {
                Glide.with(this).load(R.mipmap.icon_certified).into(imgAuth);
            }
            else
            {
                Glide.with(this).load(R.mipmap.icon_uncertified).into(imgAuth);
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
            tvHospital.setText(cooperateDocBean.getHospital());
            tvTitle.setText(cooperateDocBean.getTitle());
            tvType.setText(cooperateDocBean.getDepartment());
            tvIntroduce.setText(cooperateDocBean.getDoctorDescription());
        }
    }

    /**
     * 获取个人信息
     */
    private void getDocInfo()
    {
        mIRequest.getDocInfo(cooperateDocBean.getDoctorId(), this);
    }

    /**
     * 取消关注 合作医生
     */
    private void cancelCooperateDoc()
    {
        mIRequest.cancelCooperateDoc(loginSuccessBean.getDoctorId(), cooperateDocBean.getDoctorId(),
                                     this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.public_title_bar_back:
                finish();
                break;
            case R.id.act_user_info_chat:
                if (cooperateDocBean != null)
                {
                    Intent intent = new Intent(this, ChatActivity.class);
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
            case R.id.act_user_info_more:
                showPop();
                break;
            case R.id.delete:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                Intent intent = new Intent(this, EditRemarkActivity.class);
                intent.putExtra(CommonData.KEY_IS_DEAL_DOC, true);
                intent.putExtra(CommonData.KEY_PUBLIC, cooperateDocBean.getNickname());
                intent.putExtra(CommonData.KEY_ID, cooperateDocBean.getDoctorId());
                startActivityForResult(intent, MODIFY_NICKNAME);
                break;
            case R.id.change:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                new SimpleDialog(this, "确定删除?", (dialog, which) -> cancelCooperateDoc(),
                                 (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.act_user_info_hospital_layout:
                startActivity(new Intent(this, HospitalInfoActivity.class));
                break;
            default:
                break;
        }
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
            case GET_DOC_INFO:
                cooperateDocBean = response.getData();
                initPageData();
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            default:
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

    /**
     * 显示pop
     */
    private void showPop()
    {
        view_pop = LayoutInflater.from(this).inflate(R.layout.main_pop_menu, null);
        tvDelete = (TextView)view_pop.findViewById(R.id.delete);
        tvChange = (TextView)view_pop.findViewById(R.id.change);
        tvChange.setText("删除");
        tvDelete.setText("设置备注");
        tvDelete.setOnClickListener(this);
        tvChange.setOnClickListener(this);
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
                                   (int)AllUtils.dipToPx(this, 65));
    }
}
