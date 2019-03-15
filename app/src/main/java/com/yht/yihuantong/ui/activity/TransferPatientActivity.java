package com.yht.yihuantong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.yht.yihuantong.api.ApiManager;
import com.yht.yihuantong.api.IChange;
import com.yht.yihuantong.api.RegisterType;
import com.yht.yihuantong.api.notify.INotifyChangeListenerServer;
import com.yht.yihuantong.api.notify.NotifyChangeListenerManager;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.data.TransferStatu;
import com.yht.yihuantong.ui.dialog.HintDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.LogUtils;
import com.yht.yihuantong.utils.RecentContactUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import custom.frame.bean.BaseResponse;
import custom.frame.bean.CooperateDocBean;
import custom.frame.bean.CooperateHospitalBean;
import custom.frame.bean.PatientBean;
import custom.frame.bean.TransPatientBean;
import custom.frame.http.data.BaseNetCode;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.GlideHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.FilterEmojiEditText;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author dundun
 */
public class TransferPatientActivity extends BaseActivity implements TransferStatu {
    private static final String TAG = "TransferPatientActivity";
    @BindView(R.id.act_transfer_patient_img)
    CircleImageView ivHeadImg;
    @BindView(R.id.act_transfer_patient_name)
    TextView tvName;
    @BindView(R.id.act_transfer_patient_sex)
    TextView tvSex;
    @BindView(R.id.act_transfer_patient_age)
    TextView tvAge;
    @BindView(R.id.act_transfer_patient_intro)
    FilterEmojiEditText filterEmojiEditText;
    @BindView(R.id.act_transfer_patient_transfer_txt)
    TextView tvTransferTxt;
    @BindView(R.id.act_transfer_patient_transfer_time)
    TextView tvTime;
    @BindView(R.id.act_transfer_patient_doc_layout)
    LinearLayout llTransferDocLayout;
    @BindView(R.id.act_transfer_patient_doc_img)
    CircleImageView ivDocHeadImg;
    @BindView(R.id.act_transfer_patient_doc_name)
    TextView tvDocName;
    @BindView(R.id.act_transfer_patient_doc_hospital)
    TextView tvDocHospital;
    @BindView(R.id.arrow)
    ImageView ivArrow;
    @BindView(R.id.act_transfer_patient_doc_layout1)
    LinearLayout llTransferDocLayout1;
    @BindView(R.id.act_transfer_patient_status_icon)
    ImageView ivTransferStatu;
    @BindView(R.id.act_transfer_patient_status)
    TextView tvTransferStatus;
    @BindView(R.id.act_transfer_patient_status_layout)
    LinearLayout llTransferStatusLayout;
    @BindView(R.id.act_transfer_patient_select_hospital_name)
    TextView tvSelectHospitalName;
    @BindView(R.id.img)
    ImageView imageView;
    @BindView(R.id.act_transfer_patient_select_hospital)
    LinearLayout llHospitalLayout;
    @BindView(R.id.act_transfer_patient_select_hospital_layout)
    LinearLayout llSelectHospitalLayout;
    @BindView(R.id.act_transfer_patient_next)
    TextView tvNext;
    @BindView(R.id.act_transfer_patient_transfer_next)
    TextView tvTransferNext;
    @BindView(R.id.act_transfer_patient_transfer_refuse)
    TextView tvTransferRefuse;
    @BindView(R.id.act_transfer_patient_transfer_cancel)
    TextView tvTransferCancel;

    private PatientBean patientBean;
    private CooperateDocBean cooperateDocBean;
    private CooperateHospitalBean cooperateHospitalBean;
    private TransPatientBean transPatientBean;
    private int transferId;
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
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getTransferDetailById();
                    break;
            }
        }
    };
    /**
     * 推送回调监听  转诊申请
     */
    private IChange<String> doctorTransferPatientListener = data ->
    {
        try {
            transferId = Integer.valueOf(data);
            handler.sendEmptyMessage(0);
        } catch (NumberFormatException e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    };

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_patient;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("转诊信息");
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            patientBean = (PatientBean) getIntent().getSerializableExtra(
                    CommonData.KEY_PATIENT_BEAN);
            transPatientBean = (TransPatientBean) getIntent().getSerializableExtra(
                    CommonData.KEY_TRANSFER_BEAN);
            transferId = getIntent().getIntExtra(CommonData.KEY_TRANSFER_ID, 0);
            isAddTransferMode = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            limit = getIntent().getBooleanExtra("limit", false);
        }
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        if (transPatientBean == null && patientBean == null) {
            getTransferDetailById();
        } else {
            initPageData();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        tvNext.setOnClickListener(this);
        tvTransferNext.setOnClickListener(this);
        tvTransferRefuse.setOnClickListener(this);
        tvTransferCancel.setOnClickListener(this);
        filterEmojiEditText.setOnEditorActionListener((v, actionId, event) ->
        {
            //屏蔽换行符
            if (event.getKeyCode() ==
                    KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
        //注册转诊状态监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(
                doctorTransferPatientListener, RegisterType.REGISTER);
    }

    /**
     * 页面处理
     */
    private void initPageData() {
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
            tvAge.setText(AllUtils.getAge(patientBean.getBirthDate()));
            tvTransferNext.setVisibility(View.GONE);
        } else//已有转诊单
        {
            filterEmojiEditText.setFocusable(isAddTransferMode);
            filterEmojiEditText.setFocusableInTouchMode(isAddTransferMode);
            llTransferDocLayout1.setVisibility(View.VISIBLE);
            llTransferStatusLayout.setVisibility(View.VISIBLE);
            llTransferDocLayout.setVisibility(View.GONE);
            ivArrow.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
            if (transPatientBean != null) {
                //患者信息
                Glide.with(this)
                        .load(transPatientBean.getPatientImage())
                        .apply(GlideHelper.getOptions())
                        .into(ivHeadImg);
                tvTime.setText(AllUtils.formatDate(transPatientBean.getTransferDate(),
                        AllUtils.YYYY_MM_DD_HH_MM));
                tvName.setText(transPatientBean.getPatientName());
                tvSex.setText(transPatientBean.getPatientSex());
                tvAge.setText(AllUtils.getAge(transPatientBean.getPatientBirthDate()));
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
                    switch (transPatientBean.getAcceptState()) {
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
                        default:
                            break;
                    }
                } else {
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
                    switch (transPatientBean.getAcceptState()) {
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
                        default:
                            break;
                    }
                }
            }
        }
        if (limit) {
            tvTransferNext.setVisibility(View.GONE);
            tvNext.setVisibility(View.GONE);
        }
    }

    /**
     * 获取转诊详情  新接口2019年2月22日11:55:40
     */
    private void getTransferDetailById() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/single/detail", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transferId);
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, TransPatientBean.class);
                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
                            transPatientBean = baseResponse.getData();
                            initPageData();
                        }
                        ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
                    }
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    /**
     * 医生转诊患者  新接口2018年10月11日10:45:48
     */
    private void addTransferPatient(String diagnosisInfo) {
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
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
                            //保存最近联系人
                            RecentContactUtils.save(patientBean.getPatientId());
                            NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                            HintDialog hintDialog = new HintDialog(TransferPatientActivity.this);
                            hintDialog.isShowCancelBtn(false);
                            hintDialog.setContentString(
                                    String.format(getString(R.string.txt_transfer_patient_to_doc),
                                            cooperateDocBean.getName()));
                            hintDialog.setOnEnterClickListener(() -> finish());
                            hintDialog.show();
                        } else {
                            ToastUtil.toast(TransferPatientActivity.this, baseResponse.getMsg());
                        }
                    }
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    /**
     * 取消转诊  新接口2019年2月21日11:12:40
     */
    private void cancelTransferPatient() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/cancel/notes", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);

                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
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
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    /**
     * 拒绝转诊  新接口2019年2月21日15:32:30
     */
    private void refuseTransferPatient() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/refuse", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);

                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
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
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    /**
     * 接受转诊  新接口2019年2月21日15:32:30
     */
    private void recvTransferPatient() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(
                HttpConstants.BASE_BASIC_URL + "/trans/doctor/receive", RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("transferId", transPatientBean.getTransferId());
        params.put("hospitalId", cooperateHospitalBean.getHospitalId());
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, String.class);

                    if (baseResponse != null) {
                        if (baseResponse.getCode() == BaseNetCode.REQUEST_SUCCESS) {
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
                } catch (JSONException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(TransferPatientActivity.this, response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.act_transfer_patient_doc_layout:
            case R.id.act_transfer_patient_doc_layout1:
                intent = new Intent(this, SelectTransferDocActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_DOC);
                break;
            case R.id.act_transfer_patient_next:
                if (cooperateDocBean == null) {
                    ToastUtil.toast(this, R.string.txt_transfer_patient_to_select_doc);
                    return;
                }
                String diagnosisInfo = filterEmojiEditText.getText().toString().trim();
                if (TextUtils.isEmpty(diagnosisInfo)) {
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
                switch (orderState) {
                    //接受转诊
                    case 1:
                        if (cooperateHospitalBean == null) {
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
                            NotifyChangeListenerManager.getInstance()
                                    .notifyRecentContactChange(
                                            "");
                        }, (dialog, which) -> dialog.dismiss()).show();
                        break;
                    //确认患者就诊
                    case 2:
                        new SimpleDialog(TransferPatientActivity.this, String.format(
                                getString(R.string.txt_transfer_patient_to_isvisit),
                                transPatientBean.getPatientName()),
                                //                                         (dialog, which) -> updateTransferPatient(),
                                (dialog, which) -> dialog.dismiss()).show();
                        break;
                    default:
                        break;
                }
                break;
            //选择接诊医院
            case R.id.act_transfer_patient_select_hospital:
                intent = new Intent(this, SelectTransferHospitalActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_HOSPITAL);
                break;
            //取消转诊
            case R.id.act_transfer_patient_transfer_cancel:
                cancelTransferPatient();
                break;
            //拒绝转诊
            case R.id.act_transfer_patient_transfer_refuse:
                refuseTransferPatient();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_TRANSFER_DOC:
                cooperateDocBean = (CooperateDocBean) data.getSerializableExtra(
                        CommonData.KEY_DOCTOR_BEAN);
                if (cooperateDocBean != null) {
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
                cooperateHospitalBean = (CooperateHospitalBean) data.getSerializableExtra(
                        CommonData.KEY_HOSPITAL_BEAN);
                if (cooperateHospitalBean != null) {
                    tvSelectHospitalName.setText(cooperateHospitalBean.getHospitalName());
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销转诊状态监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(
                doctorTransferPatientListener, RegisterType.UNREGISTER);
    }

}
