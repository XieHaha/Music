package com.zyc.doctor.ui.activity;

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
import com.zyc.doctor.R;
import com.zyc.doctor.api.ApiManager;
import com.zyc.doctor.api.notify.IChange;
import com.zyc.doctor.api.notify.INotifyChangeListenerServer;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.api.notify.RegisterType;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.TransferStatu;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.data.bean.CooperateHospitalBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.data.bean.TransPatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.RecentContactUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.FilterEmojiEditText;

import butterknife.BindView;
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
    private IChange<String> doctorTransferPatientListener = data -> {
        try {
            transferId = Integer.valueOf(data);
            handler.sendEmptyMessage(0);
        }
        catch (NumberFormatException e) {
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
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("转诊信息");
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            patientBean = (PatientBean)getIntent().getSerializableExtra(CommonData.KEY_PATIENT_BEAN);
            transPatientBean = (TransPatientBean)getIntent().getSerializableExtra(CommonData.KEY_TRANSFER_BEAN);
            transferId = getIntent().getIntExtra(CommonData.KEY_TRANSFER_ID, 0);
            isAddTransferMode = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            limit = getIntent().getBooleanExtra("limit", false);
        }
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        if (transPatientBean == null && patientBean == null) {
            getTransferDetailById();
        }
        else {
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
        filterEmojiEditText.setOnEditorActionListener((v, actionId, event) -> {
            //屏蔽换行符
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                return true;
            }
            return false;
        });
        //注册转诊状态监听
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(doctorTransferPatientListener,
                                                                          RegisterType.REGISTER);
    }

    /**
     * 页面处理
     */
    private void initPageData() {
        //新增转诊单
        if (patientBean != null && isAddTransferMode) {
            llTransferStatusLayout.setVisibility(View.GONE);
            llTransferDocLayout1.setVisibility(View.GONE);
            llSelectHospitalLayout.setVisibility(View.GONE);
            llTransferDocLayout.setVisibility(View.VISIBLE);
            llTransferDocLayout.setOnClickListener(this);
            llTransferDocLayout1.setOnClickListener(this);
            Glide.with(this).load(patientBean.getPatientImgUrl()).apply(GlideHelper.getOptions()).into(ivHeadImg);
            tvName.setText(patientBean.getName());
            tvSex.setText(patientBean.getSex());
            tvAge.setText(AllUtils.getAge(patientBean.getBirthDate()));
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
            if (transPatientBean != null) {
                //患者信息
                Glide.with(this)
                     .load(transPatientBean.getPatientImage())
                     .apply(GlideHelper.getOptions())
                     .into(ivHeadImg);
                tvTime.setText(AllUtils.formatDate(transPatientBean.getTransferDate(), AllUtils.YYYY_MM_DD_HH_MM));
                tvName.setText(transPatientBean.getPatientName());
                tvSex.setText(transPatientBean.getPatientSex());
                tvAge.setText(AllUtils.getAge(transPatientBean.getPatientBirthDate()));
                filterEmojiEditText.setText(transPatientBean.getFromDoctorDiagnosisInfo());
                //接收转诊
                if (!loginSuccessBean.getDoctorId().equals(transPatientBean.getFromDoctorId())) {
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
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._FF6417));
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
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._1F6BAC));
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
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_complete_visit);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color.app_main_color));
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
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                        case TRANSFER_REFUSE:
                            tvTransferNext.setVisibility(View.GONE);
                            tvTransferRefuse.setVisibility(View.GONE);
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                        default:
                            break;
                    }
                }
                else {
                    tvTransferTxt.setText(R.string.txt_transfer_patient_to);
                    tvTransferNext.setVisibility(View.GONE);
                    //转诊医生信息
                    Glide.with(this)
                         .load(transPatientBean.getToDoctorImage())
                         .apply(GlideHelper.getOptions())
                         .into(ivDocHeadImg);
                    tvDocName.setText(transPatientBean.getToDoctorName());
                    tvDocHospital.setText(
                            transPatientBean.getToDoctorHospitalName() + "-" + transPatientBean.getToDoctorTitle());
                    switch (transPatientBean.getAcceptState()) {
                        case TRANSFER_NONE:
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferCancel.setVisibility(View.VISIBLE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_comfirm);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._FF6417));
                            ivTransferStatu.setImageResource(R.mipmap.icon_wait);
                            break;
                        case TRANSFER_RECV:
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_wait_visit);
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvSelectHospitalName.setText(transPatientBean.getHospitalName());
                            imageView.setVisibility(View.GONE);
                            llHospitalLayout.setOnClickListener(null);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._1F6BAC));
                            ivTransferStatu.setImageResource(R.mipmap.icon_wait_visit);
                            break;
                        case TRANSFER_VISIT:
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.VISIBLE);
                            tvSelectHospitalName.setText(transPatientBean.getHospitalName());
                            imageView.setVisibility(View.GONE);
                            llHospitalLayout.setOnClickListener(null);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_complete_visit);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color.app_main_color));
                            ivTransferStatu.setImageResource(R.mipmap.icon_complete);
                            break;
                        case TRANSFER_CANCEL:
                        case TRANSFER_HOSPITAL_CANCEL:
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_cancel_visit);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._E40505));
                            ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                            break;
                        case TRANSFER_REFUSE:
                            tvTransferCancel.setVisibility(View.GONE);
                            llSelectHospitalLayout.setVisibility(View.GONE);
                            tvTransferStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                            tvTransferStatus.setTextColor(ContextCompat.getColor(this, R.color._E40505));
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
        RequestUtils.getTransferDetailById(this, transferId, this);
    }

    /**
     * 医生转诊患者  新接口2018年10月11日10:45:48
     */
    private void addTransferPatient(String diagnosisInfo) {
        RequestUtils.addTransferPatient(this, diagnosisInfo, patientBean, cooperateDocBean, loginSuccessBean, this);
    }

    /**
     * 取消转诊  新接口2019年2月21日11:12:40
     */
    private void cancelTransferPatient() {
        RequestUtils.cancelTransferPatient(this, transPatientBean.getTransferId(), this);
    }

    /**
     * 拒绝转诊  新接口2019年2月21日15:32:30
     */
    private void refuseTransferPatient() {
        RequestUtils.refuseTransferPatient(this, transPatientBean.getTransferId(), this);
    }

    /**
     * 接受转诊  新接口2019年2月21日15:32:30
     */
    private void recvTransferPatient() {
        RequestUtils.recvTransferPatient(this, transPatientBean.getTransferId(), cooperateHospitalBean.getHospitalId(),
                                         this);
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
                new SimpleDialog(TransferPatientActivity.this,
                                 String.format(getString(R.string.txt_transfer_patient_to_issure_doc),
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
                        new SimpleDialog(TransferPatientActivity.this,
                                         String.format(getString(R.string.txt_transfer_patient_to_isrecv_doc),
                                                       transPatientBean.getFromDoctorName()), (dialog, which) -> {
                            //                                             updateTransferPatient();
                            recvTransferPatient();
                            //保存最近联系人
                            RecentContactUtils.save(transPatientBean.getPatientId());
                            NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                        }, (dialog, which) -> dialog.dismiss()).show();
                        break;
                    //确认患者就诊
                    case 2:
                        new SimpleDialog(TransferPatientActivity.this,
                                         String.format(getString(R.string.txt_transfer_patient_to_isvisit),
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
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_TRANSFER_DETAIL_BY_ID:
                transPatientBean = (TransPatientBean)response.getData();
                initPageData();
                break;
            case ADD_TRANSFER_PATIENT:
                //保存最近联系人
                RecentContactUtils.save(patientBean.getPatientId());
                NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                HintDialog hintDialog = new HintDialog(TransferPatientActivity.this);
                hintDialog.isShowCancelBtn(false);
                hintDialog.setContentString(
                        String.format(getString(R.string.txt_transfer_patient_to_doc), cooperateDocBean.getName()));
                hintDialog.setOnEnterClickListener(() -> finish());
                hintDialog.show();
                break;
            case CANCEL_TRANSFER_PATIENT:
                setResult(RESULT_OK);
                tvTransferCancel.setVisibility(View.GONE);
                tvTransferStatus.setText(R.string.txt_transfer_patient_to_cancel_visit);
                tvTransferStatus.setTextColor(ContextCompat.getColor(TransferPatientActivity.this, R.color._E40505));
                ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                ToastUtil.toast(TransferPatientActivity.this, response.getMsg());
                break;
            case REFUSE_TRANSFER_PATIENT:
                setResult(RESULT_OK);
                tvTransferCancel.setVisibility(View.GONE);
                tvTransferNext.setVisibility(View.GONE);
                tvTransferRefuse.setVisibility(View.GONE);
                llSelectHospitalLayout.setVisibility(View.GONE);
                tvTransferStatus.setText(R.string.txt_transfer_patient_to_refuse_visit);
                tvTransferStatus.setTextColor(ContextCompat.getColor(TransferPatientActivity.this, R.color._E40505));
                ivTransferStatu.setImageResource(R.mipmap.icon_refuse);
                ToastUtil.toast(TransferPatientActivity.this, response.getMsg());
                break;
            case RECV_TRANSFER_PATIENT:
                setResult(RESULT_OK);
                orderState = 2;
                tvTransferRefuse.setVisibility(View.GONE);
                tvTransferNext.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                llHospitalLayout.setOnClickListener(null);
                tvTransferStatus.setText(R.string.txt_transfer_patient_to_wait_visit);
                tvTransferStatus.setTextColor(ContextCompat.getColor(TransferPatientActivity.this, R.color._1F6BAC));
                ivTransferStatu.setImageResource(R.mipmap.icon_wait_visit);
                ToastUtil.toast(TransferPatientActivity.this, response.getMsg());
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
                cooperateDocBean = (CooperateDocBean)data.getSerializableExtra(CommonData.KEY_DOCTOR_BEAN);
                if (cooperateDocBean != null) {
                    llTransferDocLayout1.setVisibility(View.VISIBLE);
                    llTransferDocLayout.setVisibility(View.GONE);
                    Glide.with(this)
                         .load(cooperateDocBean.getPortraitUrl())
                         .apply(GlideHelper.getOptions())
                         .into(ivDocHeadImg);
                    tvDocName.setText(cooperateDocBean.getName());
                    tvDocHospital.setText(cooperateDocBean.getHospital() + "-" + cooperateDocBean.getTitle());
                }
                break;
            case REQUEST_CODE_TRANSFER_HOSPITAL:
                cooperateHospitalBean = (CooperateHospitalBean)data.getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
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
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(doctorTransferPatientListener,
                                                                          RegisterType.UNREGISTER);
    }
}
