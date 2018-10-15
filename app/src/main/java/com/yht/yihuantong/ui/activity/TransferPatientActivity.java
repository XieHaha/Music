package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.widget.EaseImageView;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.tools.GlideHelper;
import com.yht.yihuantong.ui.dialog.HintDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.FilterEmojiEditText;

/**
 * Created by dundun on 18/10/9.
 */
public class TransferPatientActivity extends BaseActivity
{
    private EaseImageView ivHeadImg, ivDocHeadImg;
    private ImageView ivArrow;
    private FilterEmojiEditText filterEmojiEditText;
    private TextView tvName, tvSex, tvAge, tvTransferDoc;
    private TextView tvDocName, tvDocHospital, tvNext, tvTransferNext;
    private TextView tvTransferStatus, tvTransferTxt;
    private LinearLayout llTransferDocLayout, llTransferDocLayout1, llTransferStatusLayout;
    private PatientBean patientBean;
    private CooperateDocBean cooperateDocBean;
    private TransPatientBean transPatientBean;
    /**
     * 选择合作医生
     */
    private static final int REQUEST_CODE_TRANSFER_DOC = 100;
    /**
     * 是否为新增转诊模式
     */
    private boolean isAddTransferMode;
    /**
     * 是否为接收转诊
     */
    private boolean isFrom;
    /**
     * 当前转诊单状态
     */
    private int orderState;

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    @Override
    public int getLayoutID()
    {
        return R.layout.act_transfer_patient;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("转诊信息");
        ivArrow = (ImageView)findViewById(R.id.arrow);
        ivHeadImg = (EaseImageView)findViewById(R.id.act_transfer_patient_img);
        tvName = (TextView)findViewById(R.id.act_transfer_patient_name);
        tvSex = (TextView)findViewById(R.id.act_transfer_patient_sex);
        tvAge = (TextView)findViewById(R.id.act_transfer_patient_age);
        tvTransferDoc = (TextView)findViewById(R.id.act_transfer_patient_doc);
        ivDocHeadImg = (EaseImageView)findViewById(R.id.act_transfer_patient_doc_img);
        tvDocName = (TextView)findViewById(R.id.act_transfer_patient_doc_name);
        tvDocHospital = (TextView)findViewById(R.id.act_transfer_patient_doc_hospital);
        tvTransferStatus = (TextView)findViewById(R.id.act_transfer_patient_status);
        tvTransferTxt = (TextView)findViewById(R.id.act_transfer_patient_transfer_txt);
        tvNext = (TextView)findViewById(R.id.act_transfer_patient_next);
        tvTransferNext = (TextView)findViewById(R.id.act_transfer_patient_transfer_next);
        filterEmojiEditText = (FilterEmojiEditText)findViewById(R.id.act_transfer_patient_intro);
        llTransferDocLayout = (LinearLayout)findViewById(R.id.act_transfer_patient_doc_layout);
        llTransferDocLayout1 = (LinearLayout)findViewById(R.id.act_transfer_patient_doc_layout1);
        llTransferStatusLayout = (LinearLayout)findViewById(
                R.id.act_transfer_patient_status_layout);
        //圆角设置
        ivHeadImg.setShapeType(2);
        ivHeadImg.setRadius(10);
        ivDocHeadImg.setShapeType(2);
        ivDocHeadImg.setRadius(10);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        if (getIntent() != null)
        {
            patientBean = (PatientBean)getIntent().getSerializableExtra(
                    CommonData.KEY_PATIENT_BEAN);
            transPatientBean = (TransPatientBean)getIntent().getSerializableExtra(
                    CommonData.KEY_TRANSFER_BEAN);
            isAddTransferMode = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            isFrom = getIntent().getBooleanExtra("isFrom", false);
        }
        initPageData();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvNext.setOnClickListener(this);
        tvTransferNext.setOnClickListener(this);
        filterEmojiEditText.setOnEditorActionListener((v, actionId, event) ->
                                                      {
                                                          //屏蔽换行符
                                                          if (event.getKeyCode() ==
                                                              KeyEvent.KEYCODE_ENTER)
                                                          {
                                                              return true;
                                                          }
                                                          return false;
                                                      });
    }

    /**
     * 页面处理
     */
    private void initPageData()
    {
        if (patientBean != null && isAddTransferMode)//新增转诊单
        {
            llTransferStatusLayout.setVisibility(View.GONE);
            llTransferDocLayout1.setVisibility(View.GONE);
            llTransferDocLayout.setVisibility(View.VISIBLE);
            llTransferDocLayout.setOnClickListener(this);
            llTransferDocLayout1.setOnClickListener(this);
            Glide.with(this)
                 .load(patientBean.getPatientImgUrl())
                 .apply(GlideHelper.getOptionsRect())
                 .into(ivHeadImg);
            tvName.setText(patientBean.getName());
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.formatDateByAge(patientBean.getBirthDate()));
            tvTransferNext.setVisibility(View.GONE);
        }
        else//已有转诊单
        {
            filterEmojiEditText.setFocusable(isAddTransferMode);
            filterEmojiEditText.setFocusableInTouchMode(isAddTransferMode);
            llTransferDocLayout1.setVisibility(View.VISIBLE);
            llTransferStatusLayout.setVisibility(View.VISIBLE);
            llTransferDocLayout.setVisibility(View.GONE);
            ivArrow.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
            if (transPatientBean != null)
            {
                //患者信息
                Glide.with(this)
                     .load(transPatientBean.getPatientImage())
                     .apply(GlideHelper.getOptionsRect())
                     .into(ivHeadImg);
                tvName.setText(transPatientBean.getPatientName());
                tvSex.setText(transPatientBean.getPatientSex());
                tvAge.setText(AllUtils.formatDateByAge(transPatientBean.getPatientBirthDate()));
                filterEmojiEditText.setText(transPatientBean.getFromDoctorDiagnosisInfo());
                if (isFrom)//接收转诊
                {
                    tvTransferTxt.setText("转诊来自");
                    //转诊医生信息
                    Glide.with(this)
                         .load(transPatientBean.getFromDoctorImage())
                         .apply(GlideHelper.getOptionsRect())
                         .into(ivDocHeadImg);
                    tvDocName.setText(transPatientBean.getFromDoctorName());
                    tvDocHospital.setText(transPatientBean.getFromDoctorHospitalName());
                    switch (transPatientBean.getAcceptState())
                    {
                        case 0:
                            tvTransferStatus.setText("未接受");
                            tvTransferNext.setVisibility(View.VISIBLE);
                            tvTransferNext.setText("接受转诊");
                            orderState = 1;
                            break;
                        case 1:
                            tvTransferStatus.setText("已接受-未就诊");
                            tvTransferNext.setVisibility(View.VISIBLE);
                            tvTransferNext.setText("确认就诊");
                            orderState = 2;
                            break;
                        case 2:
                            tvTransferStatus.setText("已接受-已就诊");
                            tvTransferNext.setVisibility(View.GONE);
                            break;
                    }
                }
                else
                {
                    tvTransferTxt.setText("转诊给");
                    tvTransferNext.setVisibility(View.GONE);
                    //转诊医生信息
                    Glide.with(this)
                         .load(transPatientBean.getToDoctorImage())
                         .apply(GlideHelper.getOptionsRect())
                         .into(ivDocHeadImg);
                    tvDocName.setText(transPatientBean.getToDoctorName());
                    tvDocHospital.setText(transPatientBean.getToDoctorHospitalName() + "-" +
                                          transPatientBean.getToDoctorTitle());
                    switch (transPatientBean.getAcceptState())
                    {
                        case 0:
                            tvTransferStatus.setText("待确认");
                            break;
                        case 1:
                            tvTransferStatus.setText("已确认-未就诊");
                            break;
                        case 2:
                            tvTransferStatus.setText("已接受-已就诊");
                            break;
                    }
                }
            }
        }
    }

    /**
     * 医生转诊患者  新接口2018年10月11日10:45:48
     */
    private void addTransferPatient()
    {
        String diagnosisInfo = filterEmojiEditText.getText().toString().trim();
        if (TextUtils.isEmpty(diagnosisInfo))
        {
            ToastUtil.toast(this, "请输入诊断内容");
            return;
        }
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/add/notes", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("fromDoctorDiagnosisInfo", diagnosisInfo);
        params.put("fromDoctorDepartment", loginSuccessBean.getDepartment());
        params.put("fromDoctorHospitalName", loginSuccessBean.getHospital());
        params.put("fromDoctorId", loginSuccessBean.getDoctorId());
        params.put("fromDoctorImage", loginSuccessBean.getPortraitUrl());
        params.put("fromDoctorName", loginSuccessBean.getName());
        params.put("fromDoctorTitle", loginSuccessBean.getTitle());
        params.put("patientBirthdate", patientBean.getBirthDate());
        params.put("patientId", patientBean.getPatientId());
        params.put("patientIdentityNumber", patientBean.getIdentityNum());
        params.put("patientImage", patientBean.getPatientImgUrl());
        params.put("patientName", patientBean.getName());
        params.put("patientSex", patientBean.getSex());
        params.put("toDoctorDepartment", cooperateDocBean.getDepartment());
        params.put("toDoctorHospitalName", cooperateDocBean.getHospital());
        params.put("toDoctorId", cooperateDocBean.getDoctorId());
        params.put("toDoctorImage", cooperateDocBean.getPortraitUrl());
        params.put("toDoctorName", cooperateDocBean.getName());
        params.put("toDoctorTitle", cooperateDocBean.getTitle());
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
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
                    if (baseResponse != null && baseResponse.getCode() == 200)
                    {
                        HintDialog hintDialog = new HintDialog(TransferPatientActivity.this);
                        hintDialog.isShowCancelBtn(false);
                        hintDialog.setContentString("已发送给患者，请等待患者答复");
                        hintDialog.setOnEnterClickListener(() -> finish());
                        hintDialog.show();
                    }
                    else
                    {
                        ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
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
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what)
            {
                closeProgressDialog();
            }
        });
    }

    /**
     * 医生更新转诊单状态  新接口2018年10月11日18:10:36
     */
    private void updateTransferPatient()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/update/notes/state",
                RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
        params.put("toDoctorId", transPatientBean.getToDoctorId());
        params.put("fromDoctorId", transPatientBean.getFromDoctorId());
        params.put("state", orderState);
        params.put("patientId", transPatientBean.getPatientId());
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
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
                    if (baseResponse != null && baseResponse.getCode() == 200)
                    {
                        ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
                        if (orderState == 1)
                        {
                            tvTransferNext.setText("确认就诊");
                            tvTransferStatus.setText("已接受-待就诊");
                            orderState = 2;
                        }
                        else
                        {
                            tvTransferNext.setVisibility(View.GONE);
                            tvTransferStatus.setText("已接受-已就诊");
                        }
                    }
                    else
                    {
                        ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
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
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what)
            {
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.act_transfer_patient_doc_layout:
            case R.id.act_transfer_patient_doc_layout1:
                intent = new Intent(this, CooperateDocActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_DOC);
                break;
            case R.id.act_transfer_patient_next:
                if (cooperateDocBean == null)
                {
                    ToastUtil.toast(this, "请选择合作医生");
                    return;
                }
                new SimpleDialog(TransferPatientActivity.this,
                                 "确定转诊给 " + cooperateDocBean.getName() + " 医生吗？",
                                 (dialog, which) -> addTransferPatient(),
                                 (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.act_transfer_patient_transfer_next:
                switch (orderState)
                {
                    case 1://接受转诊
                        new SimpleDialog(TransferPatientActivity.this,
                                         "确定接受 " + transPatientBean.getFromDoctorName() +
                                         " 医生的转诊吗？", (dialog, which) -> updateTransferPatient(),
                                         (dialog, which) -> dialog.dismiss()).show();
                        break;
                    case 2://确认患者就诊
                        new SimpleDialog(TransferPatientActivity.this,
                                         "确定患者 " + transPatientBean.getPatientName() + " 已就诊？",
                                         (dialog, which) -> updateTransferPatient(),
                                         (dialog, which) -> dialog.dismiss()).show();
                        break;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case REQUEST_CODE_TRANSFER_DOC:
                cooperateDocBean = (CooperateDocBean)data.getSerializableExtra(
                        CommonData.KEY_DOCTOR_BEAN);
                if (cooperateDocBean != null)
                {
                    llTransferDocLayout1.setVisibility(View.VISIBLE);
                    llTransferDocLayout.setVisibility(View.GONE);
                    Glide.with(this)
                         .load(cooperateDocBean.getPortraitUrl())
                         .apply(GlideHelper.getOptionsRect())
                         .into(ivDocHeadImg);
                    tvDocName.setText(cooperateDocBean.getName());
                    tvDocHospital.setText(
                            cooperateDocBean.getHospital() + "-" + cooperateDocBean.getTitle());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}