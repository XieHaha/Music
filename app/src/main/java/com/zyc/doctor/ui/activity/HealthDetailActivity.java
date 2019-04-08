package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.zhihu.matisse.MimeType;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.notify.NotifyChangeListenerManager;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.http.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.NormImage;
import com.zyc.doctor.data.bean.PatientCaseDetailBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.utils.permission.Permission;
import com.zyc.doctor.utils.qiniu.QiniuUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.ActionSheetDialog;
import com.zyc.doctor.ui.dialog.SimpleDialog;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.api.DirHelper;
import com.zyc.doctor.utils.FileUtils;
import com.zyc.doctor.utils.glide.GlideEngine;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.RecentContactUtils;
import com.zyc.doctor.utils.ScalingUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.widgets.gridview.AutoGridView;
import com.zyc.doctor.widgets.expandable.ExpandTextView;

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
 * 病例详情（编辑）
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
    private Uri originUri;
    private Uri cutFileUri;
    private File cameraTempFile;
    private String patientId;
    private long diagnosisTimeMil;
    /**
     * 时间选择控件
     */
    private TimePickerView timePicker;
    private String diagnosis = "", department = "", hospital = "", caseInfo = "", caseNow = "", caseImportment = "", caseCheck = "", caseDealType = "";
    private String fieldId = "";
    /**
     * 是否新增病例
     */
    private boolean isAddNewHealth = false;
    /**
     * 是否选择时间
     */
    private boolean isSelectTime = false;
    /**
     * 是否有修改病例
     */
    private boolean isModifyData = false;
    /**
     * 最多图片限制
     */
    private int maxPicNum = 9;
    /**
     * 当前正在上传的图片下标
     */
    private int currentUploadImgIndex = 0;
    /**
     * 图片拼接字符串
     */
    private StringBuilder allImgUrl;
    /**
     * 当前还能选择的图片最大数
     */
    private int currentMaxPicNum = 0;
    private ArrayList<NormImage> imageList = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();
    private ArrayList<Uri> mSelectPath = new ArrayList<>();
    /**
     * 相册
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * 相机
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
            autoGridView.updateImg(imageList, true);
            //图片显示完开始上传图片
            currentUploadImgIndex = 0;
            showProgressDialog("正在上传第1张图片");
            uploadHeadImg(mSelectPath.get(currentUploadImgIndex));
        }
    };

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("健康详情");
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        diagnosisTimeMil = System.currentTimeMillis();
        tvDiagnosisTime.setText(AllUtils.formatDate(diagnosisTimeMil, AllUtils.YYYY_MM_DD));
        allImgUrl = new StringBuilder();
        if (getIntent() != null) {
            isAddNewHealth = getIntent().getBooleanExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
            patientCaseDetailBean = (PatientCaseDetailBean)getIntent().getSerializableExtra(
                    CommonData.PATIENT_CASE_DETAIL_BEAN);
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
        if (isAddNewHealth) {
            tvTitleBarMore.setVisibility(View.VISIBLE);
            tvTitleBarMore.setText("完成");
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

    /**
     * editview  焦点处理
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
     * 页面初始化
     */
    private void initPageData() {
        if (patientCaseDetailBean != null) {
            if (!patientCaseDetailBean.getPatientId().equals(patientCaseDetailBean.getCaseCreatorId()) &&
                !loginSuccessBean.getDoctorId().equals(patientCaseDetailBean.getCaseCreatorId())) {
                tvTitleBarMore.setVisibility(View.GONE);
                ivTitlebBarMore.setVisibility(View.GONE);
            }
            tvCreateTime.setText("创建者：" + patientCaseDetailBean.getCreatorName() + "\n创建时间：" +
                                 AllUtils.formatDate(patientCaseDetailBean.getGmtCreate(), AllUtils.YYYY_MM_DD_HH_MM));
            diagnosis = patientCaseDetailBean.getDiagnosisInfo();
            tvDiagnosis.setText(diagnosis);
            diagnosisTimeMil = patientCaseDetailBean.getVisDate();
            tvDiagnosisTime.setText(AllUtils.formatDate(diagnosisTimeMil, AllUtils.YYYY_MM_DD));
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
     * 上传图片
     */
    private void uploadHeadImg(Uri uri) {
        File file = FileUtils.getFileByUri(uri, this);
        ScalingUtils.resizePic(this, file.getAbsolutePath());
        RequestUtils.uploadImg(this, file, "jpg", this);
    }

    /**
     * 新增患者病例
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
     * 更新患者病例
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
            ToastUtil.toast(this, "带星号为必填项");
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
                tvTitleBarMore.setText("完成");
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
     * 时间选择器
     */
    private void selectDate() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(1900, 1, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        String[] strings = time.split("-");
        endDate.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1, Integer.parseInt(strings[2]));
        //当前时间设置
        if (patientCaseDetailBean != null) {
            String curTime = simpleDateFormat.format(patientCaseDetailBean.getVisDate());
            String[] strings1 = curTime.split("-");
            selectedDate.set(Integer.parseInt(strings1[0]), Integer.parseInt(strings1[1]) - 1,
                             Integer.parseInt(strings1[2]));
        }
        timePicker = new TimePickerBuilder(this, (date, v) -> {
            isModifyData = true;
            //选中事件回调
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            tvDiagnosisTime.setText(simpleDateFormat1.format(date));
            diagnosisTimeMil = date.getTime();
            // 默认全部显示
        }).setType(new boolean[] { true, true, true, false, false, false })
          //取消按钮文字
          .setCancelText("取消")
          //确认按钮文字
          .setSubmitText("确定")
          //标题文字大小
          .setTitleSize(20)
          //标题文字
          .setTitleText("")
          //点击屏幕，点在控件外部范围时，是否取消显示
          .setOutSideCancelable(true)
          //是否循环滚动
          .isCyclic(false)
          //确定按钮文字颜色
          .setSubmitColor(R.color.app_main_color)
          //取消按钮文字颜色
          .setCancelColor(R.color.app_main_color)
          //标题背景颜色 Night mode
          .setTitleBgColor(0xffffffff)
          //滚轮背景颜色 Night mode
          .setBgColor(0xffffffff)
          // 如果不设置的话，默认是系统时间*/
          .setDate(selectedDate)
          //起始终止年月日设定
          .setRangDate(startDate, endDate)
          //默认设置为年月日时分秒
          .setLabel("年", "月", "日", "时", "分", "秒")
          //是否只显示中间选中项的label文字，false则每项item全部都带有label。
          .isCenterLabel(true)
          //是否显示为对话框样式
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
     * 显示删除图片dialog
     *
     * @param position
     */
    private void showDeletePhotoDialog(final int position) {
        new SimpleDialog(this, getString(R.string.txt_delete_hint), (dialog, which) -> {
            imageUrl.remove(position);
            imageList.remove(position);
            autoGridView.updateImg(imageList, true);
        }, (dialog, which) -> dialog.dismiss()).show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                if (mSelectPath.size() - 1 > currentUploadImgIndex) {
                    currentUploadImgIndex++;
                    showProgressDialog(
                            String.format(getString(R.string.txt_upload_img_num), (currentUploadImgIndex + 1)));
                    uploadHeadImg(mSelectPath.get(currentUploadImgIndex));
                }
                else {
                    closeProgressDialog();
                }
                imageUrl.add(response.getData().toString());
                break;
            case ADD_PATIENT_CASE:
                //保存最近联系人
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
        super.onResponseCode(task, response);
        switch (task) {
            case UPLOAD_FILE:
                closeProgressDialog();
                ToastUtil.toast(this, response.getMsg());
                break;
            default:
                break;
        }
    }

    /**
     * 处理图片
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

    private void selectImg() {
        new ActionSheetDialog(this).builder()
                                   .setCancelable(true)
                                   .setCanceledOnTouchOutside(true)
                                   .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                       //动态申请权限
                                       permissionHelper.request(new String[] {
                                               Permission.STORAGE_WRITE });
                                   })
                                   .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                       //动态申请权限
                                       permissionHelper.request(new String[] {
                                               Permission.CAMERA, Permission.STORAGE_WRITE });
                                   })
                                   .show();
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        Matisse.from(this)
               // 选择 mime 的类型
               .choose(MimeType.ofImage())
               // 显示选择的数量
               .countable(true)
               //                //相机
               //                .capture(true)
               //                .captureStrategy(new CaptureStrategy(true, YihtApplication.getInstance().getPackageName() +".fileprovider"))
               // 黑色背景
               .theme(R.style.Matisse_Dracula)
               // 图片选择的最多数量
               .maxSelectable(currentMaxPicNum)
               // 列表中显示的图片大小
               .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.app_picture_size))
               .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
               // 缩略图的比例
               .thumbnailScale(0.85f)
               // 使用的图片加载引擎
               .imageEngine(new GlideEngine())
               // 设置作为标记的请求码，返回图片时使用
               .forResult(RC_PICK_IMG);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        String pathRecv = DirHelper.getPathImage();
        cameraTempFile = new File(pathRecv, System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, YihtApplication.getInstance().getPackageName() + ".fileprovider",
                                             cameraTempFile);
        }
        else {
            uri = Uri.fromFile(cameraTempFile);
        }
        // 指定调用相机拍照后照片的储存路径
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
                if (cameraTempFile.exists()) {
                    Uri originalUri = Uri.fromFile(cameraTempFile);
                    mSelectPath.clear();
                    mSelectPath.add(originalUri);
                }
                new DealImgThread().start();
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
     * 判断是否提示用户保存
     */
    private void finishPage() {
        if (isModifyData) {
            new SimpleDialog(this, "编辑内容还未保存，确定退出?", (dialog, which) -> finish(),
                             (dialog, which) -> dialog.dismiss()).show();
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

    /***************************************权限处理**********************/
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
        new SimpleDialog(this, R.string.dialog_no_camera_permission_tip, false).show();
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
