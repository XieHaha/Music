package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.zhihu.matisse.Matisse;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.DirHelper;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.NormImage;
import com.zyc.doctor.data.bean.PatientCaseDetailBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.ActionSheetDialog;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.ui.dialog.listener.OnEnterClickListener;
import com.zyc.doctor.utils.BaseUtils;
import com.zyc.doctor.utils.FileUtils;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.RecentContactUtils;
import com.zyc.doctor.utils.ScalingUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.utils.glide.MatisseUtils;
import com.zyc.doctor.utils.permission.Permission;
import com.zyc.doctor.utils.qiniu.QiniuUtils;
import com.zyc.doctor.widgets.expandable.ExpandTextView;
import com.zyc.doctor.widgets.gridview.AutoGridView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * ????????????????????????
 *
 * @author DUNDUN
 */
public class HealthDetailActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, ExpandTextView.OnContentClickLinsener, CommonData {
    private static final String TAG = "HealthDetailActivity";
    @BindView(R.id.public_title_bar_more)
    ImageView ivTitlebBarMore;
    @BindView(R.id.public_title_bar_more_txt)
    TextView tvTitleBarMore;
    @BindView(R.id.act_health_detail_hint)
    TextView tvHint;
    @BindView(R.id.act_health_detail_diagnosis)
    TextView tvDiagnosis;
    @BindView(R.id.act_health_detail_diagnosis_time)
    TextView tvDiagnosisTime;
    @BindView(R.id.act_health_detail_hopital)
    TextView tvHospital;
    @BindView(R.id.act_health_detail_department)
    TextView tvDepartment;
    @BindView(R.id.act_health_detail_case_info)
    ExpandTextView tvCaseInfo;
    @BindView(R.id.act_health_detail_case_now)
    ExpandTextView tvCaseNow;
    @BindView(R.id.act_health_detail_check)
    ExpandTextView tvCaseCheck;
    @BindView(R.id.act_health_detail_auto_gridview)
    AutoGridView autoGridView;
    @BindView(R.id.act_health_detail_deal_type)
    TextView tvCaseDealType;
    @BindView(R.id.act_health_detail_create_time)
    TextView tvCreateTime;
    private PatientCaseDetailBean patientCaseDetailBean;
    private File cameraTempFile;
    private String patientId;
    private long diagnosisTimeMil;
    /**
     * ??????????????????
     */
    private TimePickerView timePicker;
    private String diagnosis = "", department = "", hospital = "", caseInfo = "", caseNow = "", caseImportment = "", caseCheck = "", caseDealType = "";
    private String fieldId = "";
    /**
     * ????????????
     */
    private Uri uri;
    /**
     * ??????????????????
     */
    private boolean isAddNewHealth = false;
    /**
     * ??????????????????
     */
    private boolean isSelectTime = false;
    /**
     * ?????????????????????
     */
    private boolean isModifyData = false;
    /**
     * ??????????????????
     */
    private int maxPicNum = 9;
    /**
     * ?????????????????????????????????
     */
    private int currentUploadImgIndex = 0;
    /**
     * ?????????????????????
     */
    private StringBuilder allImgUrl;
    /**
     * ????????????????????????????????????
     */
    private int currentMaxPicNum = 0;
    private ArrayList<NormImage> imageList = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();
    private ArrayList<Uri> mSelectPath = new ArrayList<>();
    /**
     * ??????
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * ??????
     */
    private static final int RC_PICK_CAMERA_IMG = RC_PICK_IMG + 1;

    @Override
    public int getLayoutID() {
        return R.layout.act_health_detail;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    private Handler dealImgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    autoGridView.updateImg(imageList, true);
                    //?????????????????????????????????
                    currentUploadImgIndex = 0;
                    showLoadingView();
                    uploadHeadImg(mSelectPath.get(currentUploadImgIndex));
                    break;
                case 1:
                    autoGridView.updateImg(imageList, true);
                    //?????????????????????????????????
                    currentUploadImgIndex = 0;
                    showLoadingView();
                    uploadHeadImg(cameraTempFile);
                    return;
            }
        }
    };

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable(CommonData.KEY_SAVE_DATA);
        }
        diagnosisTimeMil = System.currentTimeMillis();
        tvDiagnosisTime.setText(BaseUtils.formatDate(diagnosisTimeMil, BaseUtils.YYYY_MM_DD));
        allImgUrl = new StringBuilder();
        if (getIntent() != null) {
            isAddNewHealth = getIntent().getBooleanExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
            patientCaseDetailBean = (PatientCaseDetailBean)getIntent().getSerializableExtra(
                    CommonData.PATIENT_CASE_DETAIL_BEAN);
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
        if (isAddNewHealth) {
            tvTitleBarMore.setVisibility(View.VISIBLE);
            tvTitleBarMore.setText(R.string.txt_complete);
            initWidght(true);
            isSelectTime = true;
        }
        else {
            ivTitlebBarMore.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            initWidght(false);
            isSelectTime = false;
        }
        initPageData();
    }

    @Override
    public void initListener() {
        tvDiagnosisTime.setOnClickListener(this);
        autoGridView.setOnItemClickListener(this);
        ivTitlebBarMore.setOnClickListener(this);
        backBtn.setOnClickListener(v -> finishPage());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CommonData.KEY_SAVE_DATA, uri);
        super.onSaveInstanceState(outState);
    }

    /**
     * editview  ????????????
     *
     * @param mode
     */
    private void initWidght(boolean mode) {
        tvDiagnosisTime.setClickable(mode);
        if (mode) {
            tvDiagnosis.setOnClickListener(this);
            tvDepartment.setOnClickListener(this);
            tvHospital.setOnClickListener(this);
            tvCaseDealType.setOnClickListener(this);
            tvCaseInfo.setOnContentClickLinsener(this);
            tvCaseNow.setOnContentClickLinsener(this);
            tvCaseCheck.setOnContentClickLinsener(this);
        }
        else {
            tvDiagnosis.setOnClickListener(null);
            tvDepartment.setOnClickListener(null);
            tvHospital.setOnClickListener(null);
            tvCaseDealType.setOnClickListener(null);
            tvCaseInfo.setOnContentClickLinsener(null);
            tvCaseNow.setOnContentClickLinsener(null);
            tvCaseCheck.setOnContentClickLinsener(null);
        }
    }

    /**
     * ???????????????
     */
    private void initPageData() {
        if (patientCaseDetailBean != null) {
            if (!patientCaseDetailBean.getPatientId().equals(patientCaseDetailBean.getCaseCreatorId()) &&
                !loginSuccessBean.getDoctorId().equals(patientCaseDetailBean.getCaseCreatorId())) {
                tvTitleBarMore.setVisibility(View.GONE);
                ivTitlebBarMore.setVisibility(View.GONE);
            }
            tvCreateTime.setText("????????????" + patientCaseDetailBean.getCreatorName() + "\n???????????????" +
                                 BaseUtils.formatDate(patientCaseDetailBean.getGmtCreate(),
                                                      BaseUtils.YYYY_MM_DD_HH_MM));
            diagnosis = patientCaseDetailBean.getDiagnosisInfo();
            tvDiagnosis.setText(diagnosis);
            diagnosisTimeMil = patientCaseDetailBean.getVisDate();
            tvDiagnosisTime.setText(BaseUtils.formatDate(diagnosisTimeMil, BaseUtils.YYYY_MM_DD));
            department = patientCaseDetailBean.getDoctorDep();
            tvDepartment.setText(department);
            hospital = patientCaseDetailBean.getHospital();
            tvHospital.setText(hospital);
            caseInfo = patientCaseDetailBean.getPatientWords();
            tvCaseInfo.setText(caseInfo);
            caseNow = patientCaseDetailBean.getImportantHistory();
            tvCaseNow.setText(caseNow);
            caseCheck = patientCaseDetailBean.getCheckReport();
            tvCaseCheck.setText(caseCheck);
            caseDealType = patientCaseDetailBean.getTreat();
            tvCaseDealType.setText(caseDealType);
            String imgUrls = patientCaseDetailBean.getReportImgUrl();
            if (!TextUtils.isEmpty(imgUrls)) {
                String[] str = patientCaseDetailBean.getReportImgUrl().split(",");
                for (int i = 0; i < str.length; i++) {
                    String url = str[i];
                    NormImage normImage = new NormImage();
                    normImage.setBigImageUrl(url);
                    normImage.setMiddleImageUrl(url);
                    normImage.setSmallImageUrl(url);
                    imageList.add(normImage);
                    imageUrl.add(url);
                }
            }
            autoGridView.updateImg(imageList, false);
        }
    }

    /**
     * ????????????
     */
    private void uploadHeadImg(Uri uri) {
        File file = new File(FileUtils.getFilePathFromURI(uri, this));
        ScalingUtils.resizePic(this, file.getAbsolutePath());
        RequestUtils.uploadImg(this, file, "jpg", this);
    }

    /**
     * ????????????
     */
    private void uploadHeadImg(File file) {
        ScalingUtils.resizePic(this, file.getAbsolutePath());
        RequestUtils.uploadImg(this, file, "jpg", this);
    }

    /**
     * ??????????????????
     */
    private void addPatientCase() {
        Map<String, Object> params = new HashMap<>();
        params.put("patientId", patientId);
        params.put("caseCreatorId", loginSuccessBean.getDoctorId());
        params.put("caseCreatorName", loginSuccessBean.getName());
        params.put("caseLastUpdateId", patientId);
        params.put("checkReport", caseCheck);
        params.put("currentInfo", caseNow);
        params.put("diagnosisInfo", diagnosis);
        params.put("doctorDep", department);
        params.put("hospital", hospital);
        params.put("importantHistory", caseNow);
        params.put("patientWords", caseInfo);
        params.put("reportImgUrl", allImgUrl.toString());
        params.put("treat", caseDealType);
        params.put("visDate", diagnosisTimeMil + "");
        RequestUtils.addPatientCase(this, params, this);
    }

    /**
     * ??????????????????
     */
    private void updatePatientCase() {
        Map<String, Object> params = new HashMap<>();
        params.put("patientId", patientId);
        params.put("caseCreatorId", patientCaseDetailBean.getCaseCreatorId());
        params.put("caseCreatorName", patientCaseDetailBean.getCreatorName());
        params.put("caseLastUpdateId", patientCaseDetailBean.getCaseLastUpdateId());
        params.put("caseOperatorId", patientId);
        params.put("checkReport", caseCheck);
        params.put("currentInfo", caseNow);
        params.put("diagnosisInfo", diagnosis);
        params.put("doctorDep", department);
        params.put("fieldId", patientCaseDetailBean.getFieldId());
        params.put("hospital", hospital);
        params.put("importantHistory", caseNow);
        params.put("patientWords", caseInfo);
        params.put("reportImgUrl", allImgUrl.toString());
        params.put("treat", caseDealType);
        params.put("visDate", diagnosisTimeMil + "");
        RequestUtils.updatePatientCase(this, params, this);
    }

    /**
     * @param view
     */
    public void onTitleMoreClick(View view) {
        if (TextUtils.isEmpty(diagnosis)) {
            ToastUtil.toast(this, R.string.toast_add_health_detail);
            return;
        }
        for (int i = 0; i < imageUrl.size(); i++) {
            allImgUrl.append(imageUrl.get(i));
            if (imageUrl.size() - 1 != i) {
                allImgUrl.append(",");
            }
        }
        if (isAddNewHealth) {
            addPatientCase();
        }
        else {
            updatePatientCase();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddCaseInfoActivity.class);
        switch (v.getId()) {
            case R.id.public_title_bar_more:
                ivTitlebBarMore.setVisibility(View.GONE);
                tvHint.setVisibility(View.VISIBLE);
                tvTitleBarMore.setVisibility(View.VISIBLE);
                tvTitleBarMore.setText(R.string.txt_complete);
                autoGridView.updateImg(imageList, true);
                initWidght(true);
                isSelectTime = true;
                break;
            case R.id.act_health_detail_diagnosis_time:
                if (isSelectTime) {
                    selectDate();
                }
                break;
            case R.id.act_health_detail_diagnosis:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_DIA);
                intent.putExtra(KEY_PUBLIC_STRING, diagnosis);
                startActivityForResult(intent, CODE_CASE_DIA);
                break;
            case R.id.act_health_detail_hopital:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_HOSPITAL);
                intent.putExtra(KEY_PUBLIC_STRING, hospital);
                startActivityForResult(intent, CODE_CASE_HOSPITAL);
                break;
            case R.id.act_health_detail_department:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_TYPE);
                intent.putExtra(KEY_PUBLIC_STRING, department);
                startActivityForResult(intent, CODE_CASE_TYPE);
                break;
            case R.id.act_health_detail_deal_type:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_DEAL_WAY);
                intent.putExtra(KEY_PUBLIC_STRING, caseDealType);
                startActivityForResult(intent, CODE_CASE_DEAL_WAY);
                break;
            default:
                break;
        }
    }

    @Override
    public void onContentClick(View v) {
        Intent intent = new Intent(this, AddCaseInfoActivity.class);
        switch (v.getId()) {
            case R.id.act_health_detail_case_info:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_INFO);
                intent.putExtra(KEY_PUBLIC_STRING, caseInfo);
                startActivityForResult(intent, CODE_CASE_INFO);
                break;
            case R.id.act_health_detail_case_now:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_NOW);
                intent.putExtra(KEY_PUBLIC_STRING, caseNow);
                startActivityForResult(intent, CODE_CASE_NOW);
                break;
            case R.id.act_health_detail_check:
                intent.putExtra(CommonData.KEY_PUBLIC, CODE_CASE_CHECK);
                intent.putExtra(KEY_PUBLIC_STRING, caseCheck);
                startActivityForResult(intent, CODE_CASE_CHECK);
                break;
            default:
                break;
        }
    }

    /**
     * ???????????????
     */
    private void selectDate() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //?????????????????? ??????????????????????????????
        startDate.set(1900, 1, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        String[] strings = time.split("-");
        endDate.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1, Integer.parseInt(strings[2]));
        //??????????????????
        if (patientCaseDetailBean != null) {
            String curTime = simpleDateFormat.format(patientCaseDetailBean.getVisDate());
            String[] strings1 = curTime.split("-");
            selectedDate.set(Integer.parseInt(strings1[0]), Integer.parseInt(strings1[1]) - 1,
                             Integer.parseInt(strings1[2]));
        }
        timePicker = new TimePickerBuilder(this, (date, v) -> {
            isModifyData = true;
            //??????????????????
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            tvDiagnosisTime.setText(simpleDateFormat1.format(date));
            diagnosisTimeMil = date.getTime();
            // ??????????????????
        }).setType(new boolean[] { true, true, true, false, false, false })
          //??????????????????
          .setCancelText("??????")
          //??????????????????
          .setSubmitText("??????")
          //??????????????????
          .setTitleSize(20)
          //????????????
          .setTitleText("")
          //???????????????????????????????????????????????????????????????
          .setOutSideCancelable(true)
          //??????????????????
          .isCyclic(false)
          //????????????????????????
          .setSubmitColor(R.color.app_main_color)
          //????????????????????????
          .setCancelColor(R.color.app_main_color)
          //?????????????????? Night mode
          .setTitleBgColor(0xffffffff)
          //?????????????????? Night mode
          .setBgColor(0xffffffff)
          // ?????????????????????????????????????????????*/
          .setDate(selectedDate)
          //???????????????????????????
          .setRangDate(startDate, endDate)
          //?????????????????????????????????
          .setLabel("???", "???", "???", "???", "???", "???")
          //?????????????????????????????????label?????????false?????????item???????????????label???
          .isCenterLabel(true)
          //??????????????????????????????
          .isDialog(false).build();
        timePicker.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < imageList.size() && imageList.size() <= maxPicNum) {
            if (isAddNewHealth || isSelectTime) {
                showDeletePhotoDialog(position);
            }
            else {
                Intent intent = new Intent(this, ImagePreviewActivity.class);
                intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
                intent.putExtra(ImagePreviewActivity.INTENT_URLS, imageList);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.keep);
            }
        }
        else {
            currentMaxPicNum = maxPicNum - imageList.size();
            selectImg();
        }
    }

    /**
     * ??????????????????dialog
     *
     * @param position
     */
    private void showDeletePhotoDialog(final int position) {
        HintDialog hintDialog = new HintDialog(this);
        hintDialog.setContentString(getString(R.string.txt_delete_hint));
        hintDialog.setOnEnterClickListener(new OnEnterClickListener() {
            @Override
            public void onEnter() {
                imageUrl.remove(position);
                imageList.remove(position);
                autoGridView.updateImg(imageList, true);
            }
        });
        hintDialog.show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                if (mSelectPath.size() - 1 > currentUploadImgIndex) {
                    currentUploadImgIndex++;
                    showLoadingView();
                    uploadHeadImg(mSelectPath.get(currentUploadImgIndex));
                }
                else {
                    closeLoadingView();
                }
                imageUrl.add(response.getData().toString());
                break;
            case ADD_PATIENT_CASE:
                //?????????????????????
                RecentContactUtils.save(patientId);
                NotifyChangeListenerManager.getInstance().notifyRecentContactChange("");
                ToastUtil.toast(HealthDetailActivity.this, response.getMsg());
                setResult(RESULT_OK);
                finish();
                break;
            case UPDATE_PATIENT_CASE:
                ToastUtil.toast(HealthDetailActivity.this, response.getMsg());
                ivTitlebBarMore.setVisibility(View.VISIBLE);
                tvTitleBarMore.setVisibility(View.GONE);
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        switch (task) {
            case UPLOAD_FILE:
                closeLoadingView();
                ToastUtil.toast(this, response.getMsg());
                break;
            default:
                break;
        }
    }

    /**
     * ????????????
     */
    class DealImgThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (Uri path : mSelectPath) {
                try {
                    imageList.add(QiniuUtils.initQuanBitmaps(path, HealthDetailActivity.this));
                }
                catch (IOException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }
            dealImgHandler.sendEmptyMessage(0);
        }
    }

    /**
     * ????????????
     */
    class DealCameraThread extends Thread {
        @Override
        public void run() {
            super.run();
            for (Uri path : mSelectPath) {
                try {
                    imageList.add(QiniuUtils.initQuanBitmaps(path, HealthDetailActivity.this));
                }
                catch (IOException e) {
                    LogUtils.w(TAG, "Exception error!", e);
                }
            }
            dealImgHandler.sendEmptyMessage(1);
        }
    }

    private void selectImg() {
        new ActionSheetDialog(this).builder()
                                   .setCancelable(true)
                                   .setCanceledOnTouchOutside(true)
                                   .addSheetItem(getString(R.string.txt_capture_img),
                                                 ActionSheetDialog.SheetItemColor.Blue, which -> {
                                               //??????????????????
                                               permissionHelper.request(new String[] {
                                                       Permission.STORAGE_WRITE });
                                           })
                                   //                                   .addSheetItem(getString(R.string.txt_camera_img),
                                   //                                                 ActionSheetDialog.SheetItemColor.Blue, which -> {
                                   //                                               //??????????????????
                                   //                                               permissionHelper.request(new String[] {
                                   //                                                       Permission.CAMERA, Permission.STORAGE_WRITE });
                                   //                                           })
                                   .show();
    }

    /**
     * ???????????????
     */
    private void openPhoto() {
        MatisseUtils.open(this);
    }

    /**
     * ????????????
     */
    private void openCamera() {
        String pathRecv = DirHelper.getPathImage();
        cameraTempFile = new File(pathRecv, System.currentTimeMillis() + ".jpg");
        //????????????
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ????????????????????????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, YihtApplication.getInstance().getPackageName() + ".fileprovider",
                                             cameraTempFile);
        }
        else {
            uri = Uri.fromFile(cameraTempFile);
        }
        // ????????????????????????????????????????????????
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        cameraintent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(cameraintent, RC_PICK_CAMERA_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        isModifyData = true;
        switch (requestCode) {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                mSelectPath.clear();
                mSelectPath.addAll(paths);
                new DealImgThread().start();
                break;
            case RC_PICK_CAMERA_IMG:
                if (cameraTempFile == null) {
                    cameraTempFile = new File(FileUtils.getFilePathFromURI(uri, this));
                }
                if (cameraTempFile.exists()) {
                    mSelectPath.clear();
                    mSelectPath.add(uri);
                }
                new DealCameraThread().start();
                break;
            case CODE_CASE_DIA:
                diagnosis = data.getStringExtra(KEY_PUBLIC);
                tvDiagnosis.setText(diagnosis);
                break;
            case CODE_CASE_HOSPITAL:
                hospital = data.getStringExtra(KEY_PUBLIC);
                tvHospital.setText(hospital);
                break;
            case CODE_CASE_TYPE:
                department = data.getStringExtra(KEY_PUBLIC);
                tvDepartment.setText(department);
                break;
            case CODE_CASE_INFO:
                caseInfo = data.getStringExtra(KEY_PUBLIC);
                tvCaseInfo.setText(caseInfo);
                break;
            case CODE_CASE_NOW:
                caseNow = data.getStringExtra(KEY_PUBLIC);
                tvCaseNow.setText(caseNow);
                break;
            case CODE_CASE_CHECK:
                caseCheck = data.getStringExtra(KEY_PUBLIC);
                tvCaseCheck.setText(caseCheck);
                break;
            case CODE_CASE_DEAL_WAY:
                caseDealType = data.getStringExtra(KEY_PUBLIC);
                tvCaseDealType.setText(caseDealType);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ??????????????????????????????
     */
    private void finishPage() {
        if (isModifyData) {
            HintDialog hintDialog = new HintDialog(this);
            hintDialog.setContentString(getString(R.string.dialog_txt_exit_hint));
            hintDialog.setOnEnterClickListener(new OnEnterClickListener() {
                @Override
                public void onEnter() {
                    finish();
                }
            });
            hintDialog.show();
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /***************************************????????????**********************/
    private boolean isSamePermission(String o, String n) {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n)) {
            return false;
        }
        if (o.equals(n)) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (permissions == null) {
            return;
        }
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        if (permissionName == null || permissionName.length == 0) {
            return;
        }
        if (isSamePermission(Permission.CAMERA, permissionName[0])) {
            openCamera();
        }
        else if (isSamePermission(Permission.STORAGE_WRITE, permissionName[0])) {
            openPhoto();
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        super.onPermissionDeclined(permissionName);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        LogUtils.d("test", "onPermissionPreGranted:" + permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.STORAGE_WRITE, ((String[])permissionName)[0])) {
                openPhoto();
            }
            else if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openCamera();
            }
        }
    }
}
