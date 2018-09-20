package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.yihuantong.R;
import com.yht.yihuantong.YihtApplication;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.chat.ChatActivity;
import com.yht.yihuantong.ui.adapter.FragmentVpAdapter;
import com.yht.yihuantong.ui.fragment.BaseInfoFragment;
import com.yht.yihuantong.ui.fragment.CaseRecordFragment;
import com.yht.yihuantong.utils.AllUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.PatientBean;
import custom.frame.http.Tasks;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.view.ViewPrepared;

/**
 * 患者健康卡
 *
 * @author DUNDUN
 */
public class HealthCardActivity extends BaseActivity
{
    private Button btnBaseInfo, btnHealthRecord;
    private TextView tvChat, tvTitle;
    private ImageView ivTitleBarMore;
    private ViewPager viewPager;
    private View viewIndicator;
    private FragmentVpAdapter fragmentVpAdapter;
    private View view_pop, view_line;
    private PopupWindow mPopupwinow;
    private RelativeLayout rlRemarkLayout;
    private TextView tvDelete, tvChange, tvRemark;
    /**
     * 患者 bean
     */
    private PatientBean patientBean;
    /**
     * 患者基础信息
     */
    private BaseInfoFragment baseInfoFragment;
    /**
     * 患者病例
     */
    private CaseRecordFragment caseRecordFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    /**
     * 转诊患者
     */
    private static final int CHANGE_PATIENT = 2;
    /**
     * 选择转诊医生回调
     */
    private static final int CHANGE_PATIENT_REQUEST_CODE = 2;
    /**
     * 服务包回调
     */
    public static final int SERVICE_REQUEST_CODE = 100;

    @Override
    public int getLayoutID()
    {
        return R.layout.act_health_card;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        viewPager = (ViewPager)findViewById(R.id.act_health_card_viewpager);
        viewIndicator = findViewById(R.id.act_health_card_indicator);
        btnBaseInfo = (Button)findViewById(R.id.act_health_card_base_info);
        tvChat = (TextView)findViewById(R.id.act_health_card_chat);
        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
        btnHealthRecord = (Button)findViewById(R.id.act_health_card_health_record);
        ivTitleBarMore = (ImageView)findViewById(R.id.public_title_bar_more_two);
        ivTitleBarMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            patientBean = (PatientBean)getIntent().getSerializableExtra(
                    CommonData.KEY_PATIENT_BEAN);
        }
        if (patientBean != null)
        {
            List<PatientBean> list = DataSupport.where("patientId = ?", patientBean.getPatientId())
                                                .find(PatientBean.class);
            if (list != null && list.size() > 0)
            {
                patientBean = list.get(0);
            }
            if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                patientBean.getNickname().length() < 20)
            {
                tvTitle.setText(patientBean.getNickname() + "的健康卡");
            }
            else
            {
                tvTitle.setText(patientBean.getName() + "的健康卡");
            }
        }
        new ViewPrepared().asyncPrepare(btnBaseInfo, (w, h) ->
        {
            ViewGroup.LayoutParams params = viewIndicator.getLayoutParams();
            params.width = w;
            viewIndicator.setLayoutParams(params);
        });
        baseInfoFragment = new BaseInfoFragment();
        baseInfoFragment.setPatientBean(patientBean);
        caseRecordFragment = new CaseRecordFragment();
        caseRecordFragment.setPatientBean(patientBean);
        fragmentList.add(baseInfoFragment);
        fragmentList.add(caseRecordFragment);
        fragmentVpAdapter = new FragmentVpAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentVpAdapter);
        viewPager.setCurrentItem(0);
        //        getPatientInfo();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        ivTitleBarMore.setOnClickListener(this);
        btnBaseInfo.setOnClickListener(this);
        btnHealthRecord.setOnClickListener(this);
        tvChat.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                int tabWidth = btnBaseInfo.getWidth();
                viewIndicator.setTranslationX((position * tabWidth) + (positionOffset * tabWidth));
            }

            @Override
            public void onPageSelected(int position)
            {
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });
    }

    /**
     * 获取患者个人信息
     */
    private void getPatientInfo()
    {
        mIRequest.getPatientInfo(patientBean.getPatientId(), this);
    }

    /**
     * 删除病人(取消关注)
     */
    private void deletePatient()
    {
        mIRequest.deletePatient(loginSuccessBean.getDoctorId(), patientBean.getPatientId(), this);
    }

    /**
     * 医生扫码添加患者  转诊患者
     * {@link #CHANGE_PATIENT}
     */
    private void addPatientByScanOrChangePatient(String doctorId)
    {
        mIRequest.addPatientByScanOrChangePatient(doctorId, loginSuccessBean.getDoctorId(),
                                                  patientBean.getPatientId(), CHANGE_PATIENT, this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        Intent intent;
        switch (v.getId())
        {
            case R.id.act_health_card_base_info:
                viewPager.setCurrentItem(0);
                break;
            case R.id.act_health_card_health_record:
                viewPager.setCurrentItem(1);
                break;
            case R.id.public_title_bar_more_two:
                showPop();
                break;
            case R.id.delete:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                deletePatient();
                break;
            case R.id.change:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(this, ServicePackageActivity.class);
                //                intent = new Intent(this, ServicePackActivity.class);
                intent.putExtra(CommonData.KEY_PATIENT_ID, patientBean.getPatientId());
                intent.putExtra(CommonData.KEY_REGISTRATION_TYPE, "服务");
                startActivityForResult(intent, SERVICE_REQUEST_CODE);
                //                if (mPopupwinow != null)
                //                {
                //                    mPopupwinow.dismiss();
                //                }
                //                intent = new Intent(this, CooperateDocActivity.class);
                //                startActivityForResult(intent, CHANGE_PATIENT_REQUEST_CODE);
                break;
            case R.id.remark:
                if (mPopupwinow != null)
                {
                    mPopupwinow.dismiss();
                }
                intent = new Intent(this, EditRemarkActivity.class);
                intent.putExtra(CommonData.KEY_IS_DEAL_DOC, false);
                intent.putExtra(CommonData.KEY_PUBLIC, patientBean.getNickname());
                intent.putExtra(CommonData.KEY_ID, patientBean.getPatientId());
                startActivity(intent);
                break;
            case R.id.act_health_card_chat:
                if (patientBean != null)
                {
                    intent = new Intent(this, ChatActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.getPatientId());
                    //存储临时数据
                    YihtApplication.getInstance().setEaseName(patientBean.getName());
                    YihtApplication.getInstance().setEaseHeadImgUrl(patientBean.getPatientImgUrl());
                    startActivity(intent);
                }
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
            case GET_PATIENT_INFO:
                patientBean = response.getData();
                if (patientBean != null)
                {
                    if (!TextUtils.isEmpty(patientBean.getNickname()) &&
                        patientBean.getNickname().length() < 20)
                    {
                        tvTitle.setText(patientBean.getNickname() + "的健康卡");
                    }
                    else
                    {
                        tvTitle.setText(patientBean.getName() + "的健康卡");
                    }
                }
                if (baseInfoFragment != null)
                {
                    baseInfoFragment.setPatientBean(patientBean);
                }
                break;
            case DELETE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCodeError(Tasks task, BaseResponse response)
    {
        super.onResponseCodeError(task, response);
        switch (task)
        {
            case ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT:
                ToastUtil.toast(this, response.getMsg());
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
            case CHANGE_PATIENT_REQUEST_CODE:
                if (data != null)
                {
                    String doctorId = data.getStringExtra(CommonData.KEY_DOCTOR_ID);
                    addPatientByScanOrChangePatient(doctorId);
                }
                break;
            case SERVICE_REQUEST_CODE:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    /**
     * 显示pop
     */
    private void showPop()
    {
        view_pop = LayoutInflater.from(this).inflate(R.layout.health_pop_menu, null);
        //        tvDelete = (TextView)view_pop.findViewById(R.id.delete);
        tvChange = (TextView)view_pop.findViewById(R.id.change);
        tvRemark = view_pop.findViewById(R.id.remark);
        rlRemarkLayout = view_pop.findViewById(R.id.btn_remark_layout);
        view_line = view_pop.findViewById(R.id.main_pop_menu_line_1);
        view_line.setVisibility(View.VISIBLE);
        rlRemarkLayout.setVisibility(View.VISIBLE);
        //        tvDelete.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        tvRemark.setOnClickListener(this);
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
