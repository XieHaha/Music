package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.api.notify.NotifyChangeListenerServer;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.TransferStatu;
import com.yht.yihuantong.ui.dialog.HintDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.RecentContactUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.CooperateHospitalBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.FilterEmojiEditText;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dundun on 18/10/9.
 */
public class TransferPatientActivity extends BaseActivity implements TransferStatu
{
    private CircleImageView ivHeadImg, ivDocHeadImg;
    private ImageView ivArrow;
    private FilterEmojiEditText filterEmojiEditText;
    private TextView tvName, tvSex, tvAge, tvTransferDoc;
    private TextView tvDocName, tvDocHospital, tvNext, tvTransferNext, tvTransferRefuse, tvTransferCancel, tvTime;
    private TextView tvTransferStatus, tvTransferTxt, tvSelectHospitalName;
    private ImageView ivTransferStatu, imageView;
    private LinearLayout llTransferDocLayout, llTransferDocLayout1, llTransferStatusLayout, llSelectHospitalLayout, llHospitalLayout;
    private PatientBean patientBean;
    private CooperateDocBean cooperateDocBean;
    private CooperateHospitalBean cooperateHospitalBean;
    private TransPatientBean transPatientBean;
    /**
     * 选择合作医生
     */
    private static final int REQUEST_CODE_TRANSFER_DOC = 100;
    /**
     * 选择接诊医院
     */
    private static final int REQUEST_CODE_TRANSFER_HOSPITAL = 200;
    /**
     * 是否为新增转诊模式
     */
    private boolean isAddTransferMode;
    /**
     * 是否限制操作
     */
    private boolean limit;
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
        imageView = (ImageView)findViewById(R.id.img);
        ivHeadImg = (CircleImageView)findViewById(R.id.act_transfer_patient_img);
        tvName = (TextView)findViewById(R.id.act_transfer_patient_name);
        tvSex = (TextView)findViewById(R.id.act_transfer_patient_sex);
        tvAge = (TextView)findViewById(R.id.act_transfer_patient_age);
        tvTransferDoc = (TextView)findViewById(R.id.act_transfer_patient_doc);
        ivDocHeadImg = (CircleImageView)findViewById(R.id.act_transfer_patient_doc_img);
        tvDocName = (TextView)findViewById(R.id.act_transfer_patient_doc_name);
        tvDocHospital = (TextView)findViewById(R.id.act_transfer_patient_doc_hospital);
        tvTransferStatus = (TextView)findViewById(R.id.act_transfer_patient_status);
        tvTransferTxt = (TextView)findViewById(R.id.act_transfer_patient_transfer_txt);
        tvSelectHospitalName = (TextView)findViewById(
                R.id.act_transfer_patient_select_hospital_name);
        tvTime = (TextView)findViewById(R.id.act_transfer_patient_transfer_time);
        tvNext = (TextView)findViewById(R.id.act_transfer_patient_next);
        tvTransferNext = (TextView)findViewById(R.id.act_transfer_patient_transfer_next);
        tvTransferRefuse = (TextView)findViewById(R.id.act_transfer_patient_transfer_refuse);
        tvTransferCancel = (TextView)findViewById(R.id.act_transfer_patient_transfer_cancel);
        ivTransferStatu = (ImageView)findViewById(R.id.act_transfer_patient_status_icon);
        filterEmojiEditText = (FilterEmojiEditText)findViewById(R.id.act_transfer_patient_intro);
        llTransferDocLayout = (LinearLayout)findViewById(R.id.act_transfer_patient_doc_layout);
        llTransferDocLayout1 = (LinearLayout)findViewById(R.id.act_transfer_patient_doc_layout1);
        llTransferStatusLayout = (LinearLayout)findViewById(
                R.id.act_transfer_patient_status_layout);
        llSelectHospitalLayout = (LinearLayout)findViewById(
                R.id.act_transfer_patient_select_hospital_layout);
        llHospitalLayout = (LinearLayout)findViewById(R.id.act_transfer_patient_select_hospital);
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
            limit = getIntent().getBooleanExtra("limit", false);
        }
        initPageData();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvNext.setOnClickListener(this);
        tvTransferNext.setOnClickListener(this);
        tvTransferRefuse.setOnClickListener(this);
        tvTransferCancel.setOnClickListener(this);
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
            llSelectHospitalLayout.setVisibility(View.GONE);
            llTransferDocLayout.setVisibility(View.VISIBLE);
            llTransferDocLayout.setOnClickListener(this);
            llTransferDocLayout1.setOnClickListener(this);
            Glide.with(this)
                 .load(patientBean.getPatientImgUrl())
                 .apply(GlideHelper.getOptions())
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
                     .apply(GlideHelper.getOptions())
                     .into(ivHeadImg);
                tvTime.setText(AllUtils.formatDate(transPatientBean.getTransferDate(),
                                                   AllUtils.YYYY_MM_DD_HH_MM));
                tvName.setText(transPatientBean.getPatientName());
                tvSex.setText(transPatientBean.getPatientSex());
                tvAge.setText(AllUtils.formatDateByAge(transPatientBean.getPatientBirthDate()));
                filterEmojiEditText.setText(transPatientBean.getFromDoctorDiagnosisInfo());
                if (!loginSuccessBean.getDoctorId()
                                     .equals(transPatientBean.getFromDoctorId()))//接收转诊
                {
                    tvTransferTxt.setText(R.string.txt_transfer_patient_from);
                    //转诊医生信息
                    Glide.with(this)
                         .load(transPatientBean.getFromDoctorImage())
                         .apply(GlideHelper.getOptions())
                         .into(ivDocHeadImg);
                    tvDocName.setText(transPatientBean.getFromDoctorName());
                    tvDocHospital.setText(transPatientBean.getFromDoctorHospitalName());
                    switch (transPatientBean.getAcceptState())
                    {
                        case TRANSFER_NONE:
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_comfirm);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._FF6417));
                            llHospitalLayout.setOnClickListener(this);
                            tvTransferNext.setVisibility(View.VISIBLE);
                            tvTransferRefuse.setVisibility(View.VISIBLE);
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvTransferNext.setText(R.string.txt_transfer_patient_to_recved);
                            orderState = 1;
                            ivTransferStatu.setImageResource(R.mipmap.icon_wait);
                            break;
                        case TRANSFER_RECV:
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_wait_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._1F6BAC));
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvSelectHospitalName.setText(transPatientBean.getHospitalName());
                            imageView.setVisibility(View.GONE);
                            tvNext.setVisibility(View.GONE);
                            tvTransferNext.setVisibility(View.GONE);
                            tvTransferRefuse.setVisibility(View.GONE);
                            llHospitalLayout.setOnClickListener(null);
                            orderState = 2;
                            ivTransferStatu.setImageResource(R.mipmap.icon_wait_visit);
                            break;
                        case TRANSFER_VISIT:
                            tvTransferStatus.setText(
                                    R.string.txt_transfer_patient_to_complete_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color.app_main_color));
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvSelectHospitalName.setText(transPatientBean.getHospitalName());
                            imageView.setVisibility(View.GONE);
                            llHospitalLayout.setOnClickListener(null);
                            tvTransferNext.setVisibility(View.GONE);
                            tvNext.setVisibility(View.GONE);
                            tvTransferRefuse.setVisibility(View.GONE);
                            ivTransferStatu.setImageResource(R.mipmap.icon_complete);
                            break;
                        case TRANSFER_CANCEL:
                        case TRANSFER_HOSPITAL_CANCEL:
                            tvTransferNext.setVisibility(View.GONE);
                            tvTransferRefuse.setVisibility(View.GONE);
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_cancel_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                        case TRANSFER_REFUSE:
                            tvTransferNext.setVisibility(View.GONE);
                            tvTransferRefuse.setVisibility(View.GONE);
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                    }
                }
                else
                {
                    tvTransferTxt.setText(R.string.txt_transfer_patient_to);
                    tvTransferNext.setVisibility(View.GONE);
                    //转诊医生信息
                    Glide.with(this)
                         .load(transPatientBean.getToDoctorImage())
                         .apply(GlideHelper.getOptions())
                         .into(ivDocHeadImg);
                    tvDocName.setText(transPatientBean.getToDoctorName());
                    tvDocHospital.setText(transPatientBean.getToDoctorHospitalName() + "-" +
                                          transPatientBean.getToDoctorTitle());
                    switch (transPatientBean.getAcceptState())
                    {
                        case TRANSFER_NONE:
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferCancel.setVisibility(View.VISIBLE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_comfirm);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._FF6417));
                            ivTransferStatu.setImageResource(R.mipmap.icon_wait);
                            break;
                        case TRANSFER_RECV:
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_wait_visit);
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvSelectHospitalName.setText(transPatientBean.getHospitalName());
                            imageView.setVisibility(View.GONE);
                            llHospitalLayout.setOnClickListener(null);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._1F6BAC));
                            ivTransferStatu.setImageResource(R.mipmap.icon_wait_visit);
                            break;
                        case TRANSFER_VISIT:
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvSelectHospitalName.setText(transPatientBean.getHospitalName());
                            imageView.setVisibility(View.GONE);
                            llHospitalLayout.setOnClickListener(null);
                            tvTransferStatus.setText(
                                    R.string.txt_transfer_patient_to_complete_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color.app_main_color));
                            ivTransferStatu.setImageResource(R.mipmap.icon_complete);
                            break;
                        case TRANSFER_CANCEL:
                        case TRANSFER_HOSPITAL_CANCEL:
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_cancel_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                        case TRANSFER_REFUSE:
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                            tvTransferStatus.setTextColor(
                                    ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                    }
                }
            }
        }
        if (limit)
        {
            tvTransferNext.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
        }
    }

    /**
     * 医生转诊患者  新接口2018年10月11日10:45:48
     */
    private void addTransferPatient(String diagnosisInfo)
    {
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
                        //保存最近联系人
                        RecentContactUtils.save(patientBean.getPatientId());
                        NotifyChangeListenerServer.getInstance().notifyRecentContactChange("");
                        HintDialog hintDialog = new HintDialog(TransferPatientActivity.this);
                        hintDialog.isShowCancelBtn(false);
                        hintDialog.setContentString(
                                String.format(getString(R.string.txt_transfer_patient_to_doc),
                                              cooperateDocBean.getName()));
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
     * 2019年2月21日16:18:05 弃用
     */
    //    private void updateTransferPatient()
    //    {
    //        RequestQueue queue = NoHttp.getRequestQueueInstance();
    //        final Request<String> request = NoHttp.createStringRequest(
    //                HttpConstants.BASE_BASIC_URL + "/trans/doctor/update/notes/state",
    //                RequestMethod.POST);
    //        Map<String, Object> params = new HashMap<>();
    //        params.put("transferId", transPatientBean.getTransferId());
    //        params.put("toDoctorId", transPatientBean.getToDoctorId());
    //        params.put("fromDoctorId", transPatientBean.getFromDoctorId());
    //        params.put("state", orderState);
    //        params.put("patientId", transPatientBean.getPatientId());
    //        JSONObject jsonObject = new JSONObject(params);
    //        request.setDefineRequestBodyForJson(jsonObject.toString());
    //        queue.add(1, request, new OnResponseListener<String>()
    //        {
    //            @Override
    //            public void onStart(int what)
    //            {
    //            }
    //
    //            @Override
    //            public void onSucceed(int what, Response<String> response)
    //            {
    //                String s = response.get();
    //                try
    //                {
    //                    JSONObject object = new JSONObject(s);
    //                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
    //                    if (baseResponse != null && baseResponse.getCode() == 200)
    //                    {
    //                        ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
    //                        if (orderState == 1)
    //                        {
    //                            tvTransferNext.setText(
    //                                    getString(R.string.txt_transfer_patient_to_visit));
    //                            tvTransferStatus.setText(
    //                                    getString(R.string.txt_transfer_patient_to_wait_visit));
    //                            orderState = 2;
    //                        }
    //                        else
    //                        {
    //                            tvTransferNext.setVisibility(View.GONE);
    //                            tvTransferStatus.setText(
    //                                    getString(R.string.txt_transfer_patient_to_complete_visit));
    //                            ivTransferStatu.setSelected(true);
    //                        }
    //                    }
    //                    else
    //                    {
    //                        ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
    //                    }
    //                }
    //                catch (JSONException e)
    //                {
    //                    e.printStackTrace();
    //                }
    //            }
    //
    //            @Override
    //            public void onFailed(int what, Response<String> response)
    //            {
    //                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
    //            }
    //
    //            @Override
    //            public void onFinish(int what)
    //            {
    //                closeProgressDialog();
    //            }
    //        });
    //    }

    /**
     * 取消转诊  新接口2019年2月21日11:12:40
     */
    private void cancelTransferPatient()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/cancel/notes", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
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
                        setResult(RESULT_OK);
                        tvTransferCancel.setVisibility(View.GONE);
                        tvTransferStatus.setText(R.string.txt_transfer_patient_to_cancel_visit);
                        tvTransferStatus.setTextColor(
                                ContextCompat.getColor(TransferPatientActivity.this,
                                                       R.color._E40505));
                        ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                    }
                    ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
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
     * 拒绝转诊  新接口2019年2月21日15:32:30
     */
    private void refuseTransferPatient()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/refuse", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
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
                        setResult(RESULT_OK);
                        tvTransferCancel.setVisibility(View.GONE);
                        tvTransferNext.setVisibility(View.GONE);
                        tvTransferRefuse.setVisibility(View.GONE);
                        llSelectHospitalLayout.setVisibility(View.GONE);
                        tvTransferStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                        tvTransferStatus.setTextColor(
                                ContextCompat.getColor(TransferPatientActivity.this,
                                                       R.color._E40505));
                        ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                    }
                    ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
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
     * 接受转诊  新接口2019年2月21日15:32:30
     */
    private void recvTransferPatient()
    {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/receive", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
        params.put("hospitalId", cooperateHospitalBean.getHospitalId());
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
                        setResult(RESULT_OK);
                        orderState = 2;
                        tvTransferRefuse.setVisibility(View.GONE);
                        tvTransferNext.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        llHospitalLayout.setOnClickListener(null);
                        tvTransferStatus.setText(R.string.txt_transfer_patient_to_wait_visit);
                        tvTransferStatus.setTextColor(
                                ContextCompat.getColor(TransferPatientActivity.this,
                                                       R.color._1F6BAC));
                        ivTransferStatu.setImageResource(R.mipmap.icon_wait_visit);
                    }
                    ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
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
                intent = new Intent(this, SelectTransferDocActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_DOC);
                break;
            case R.id.act_transfer_patient_next:
                if (cooperateDocBean == null)
                {
                    ToastUtil.toast(this, R.string.txt_transfer_patient_to_select_doc);
                    return;
                }
                String diagnosisInfo = filterEmojiEditText.getText().toString().trim();
                if (TextUtils.isEmpty(diagnosisInfo))
                {
                    ToastUtil.toast(this, R.string.txt_transfer_patient_to_input_diagnosisinfo);
                    return;
                }
                new SimpleDialog(TransferPatientActivity.this, String.format(
                        getString(R.string.txt_transfer_patient_to_issure_doc),
                        cooperateDocBean.getName()),
                                 (dialog, which) -> addTransferPatient(diagnosisInfo),
                                 (dialog, which) -> dialog.dismiss()).show();
                break;
            case R.id.act_transfer_patient_transfer_next:
                switch (orderState)
                {
                    case 1://接受转诊
                        if (cooperateHospitalBean == null)
                        {
                            ToastUtil.toast(this, R.string.txt_transfer_patient_to_select_hospital);
                            return;
                        }
                        new SimpleDialog(TransferPatientActivity.this, String.format(
                                getString(R.string.txt_transfer_patient_to_isrecv_doc),
                                transPatientBean.getFromDoctorName()), (dialog, which) ->
                                         {
                                             //                                             updateTransferPatient();
                                             recvTransferPatient();
                                             //保存最近联系人
                                             RecentContactUtils.save(
                                                     transPatientBean.getPatientId());
                                             NotifyChangeListenerServer.getInstance()
                                                                       .notifyRecentContactChange(
                                                                               "");
                                         }, (dialog, which) -> dialog.dismiss()).show();
                        break;
                    case 2://确认患者就诊
                        new SimpleDialog(TransferPatientActivity.this, String.format(
                                getString(R.string.txt_transfer_patient_to_isvisit),
                                transPatientBean.getPatientName()),
                                         //                                         (dialog, which) -> updateTransferPatient(),
                                         (dialog, which) -> dialog.dismiss()).show();
                        break;
                }
                break;
            case R.id.act_transfer_patient_select_hospital://选择接诊医院
                intent = new Intent(this, SelectTransferHospitalActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_HOSPITAL);
                break;
            case R.id.act_transfer_patient_transfer_cancel://取消转诊
                cancelTransferPatient();
                break;
            case R.id.act_transfer_patient_transfer_refuse://拒绝转诊
                refuseTransferPatient();
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
                         .apply(GlideHelper.getOptions())
                         .into(ivDocHeadImg);
                    tvDocName.setText(cooperateDocBean.getName());
                    tvDocHospital.setText(
                            cooperateDocBean.getHospital() + "-" + cooperateDocBean.getTitle());
                }
                break;
            case REQUEST_CODE_TRANSFER_HOSPITAL:
                cooperateHospitalBean = (CooperateHospitalBean)data.getSerializableExtra(
                        CommonData.KEY_HOSPITAL_BEAN);
                if (cooperateHospitalBean != null)
                {
                    tvSelectHospitalName.setText(cooperateHospitalBean.getHospitalName());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
