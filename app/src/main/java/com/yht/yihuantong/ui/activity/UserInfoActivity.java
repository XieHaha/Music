package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ease.ChatActivity;
import com.yht.yihuantong.tools.GlideHelper;
import com.yht.yihuantong.utils.AllUtils;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
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
    private View view_pop;
    private PopupWindow mPopupwinow;
    private TextView tvDelete, tvChange;
    private CooperateDocBean cooperateDocBean;
    private String headImgUrl;
    private String doctorId;
    /**
     * 是否可以取消关注
     */
    private boolean isDealDoc;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_user_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_user_info_headimg);
        imgAuth = (CircleImageView)findViewById(R.id.act_user_info_auth);
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        ivTitleMore = (ImageView)findViewById(R.id.act_user_info_more);
        ivTitleMore.setOnClickListener(this);
        findViewById(R.id.act_user_info_chat).setOnClickListener(this);
        tvName = (TextView)findViewById(R.id.act_user_info_name);
        tvHospital = (TextView)findViewById(R.id.act_user_info_hospital);
        tvTitle = (TextView)findViewById(R.id.act_user_info_title);
        tvType = (TextView)findViewById(R.id.act_user_info_type);
        tvIntroduce = (TextView)findViewById(R.id.act_user_info_introduce);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            doctorId = getIntent().getStringExtra(CommonData.KEY_DOCTOR_ID);
            isDealDoc = getIntent().getBooleanExtra(CommonData.KEY_IS_DEAL_DOC, false);
        }
        if (isDealDoc)
        {
            ivTitleMore.setVisibility(View.VISIBLE);
        }
        else
        {
            ivTitleMore.setVisibility(View.GONE);
        }
        getDocInfo();
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
                imgAuth.setSelected(true);
            }
            else
            {
                imgAuth.setSelected(false);
            }
            tvName.setText(cooperateDocBean.getName());
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
        mIRequest.getDocInfo(doctorId, this);
    }

    /**
     * 取消关注 合作医生
     */
    private void cancelCooperateDoc()
    {
        mIRequest.cancelCooperateDoc(loginSuccessBean.getDoctorId(), doctorId, this);
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
                    intent.putExtra(CommonData.KEY_CHAT_NAME, cooperateDocBean.getName());
                    //存储临时数据
                    YihtApplication.getInstance().setEaseName(cooperateDocBean.getName());
                    YihtApplication.getInstance()
                                   .setEaseHeadImgUrl(cooperateDocBean.getPortraitUrl());
                    startActivity(intent);
                }
                break;
            case R.id.act_user_info_more:
                showPop();
                break;
            case R.id.change:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                cancelCooperateDoc();
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
                ToastUtil.toast(this, "上传成功!!");
                headImgUrl = response.getData();
                break;
            case GET_DOC_INFO:
                cooperateDocBean = response.getData();
                initPageData();
                break;
            case CANCEL_COOPERATE_DOC:
                ToastUtil.toast(this, "处理成功!");
                setResult(RESULT_OK);
                finish();
                break;
            default:
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
        tvChange.setText("取消关注");
        tvDelete.setVisibility(View.GONE);
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
