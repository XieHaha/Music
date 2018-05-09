package com.yht.yihuantong.ui.activity;

import android.content.Context;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.dialog.ActionSheetDialog;
import com.yht.yihuantong.ui.dialog.SimpleDialog;
import com.yht.yihuantong.utils.AllUtils;
import com.yht.yihuantong.utils.FileUtils;
import com.yht.yihuantong.utils.LogUtils;
import com.yht.yihuantong.utils.ScalingUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.NormImage;
import custom.frame.bean.PatientCaseDetailBean;
import custom.frame.http.Tasks;
import custom.frame.permission.Permission;
import custom.frame.qiniu.QiniuUtils;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.DirHelper;
import custom.frame.utils.ToastUtil;
import custom.frame.widgets.gridview.AutoGridView;

/**
 * 病例详情（编辑）
 *
 * @author DUNDUN
 */
public class HealthDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
    private TextView tvTitleBarMore, tvCreateTime, tvHint;
    private ImageView ivTitlebBarMore;
    private EditText etDiagnosis, etDepartment, etHospital, etCaseInfo, etCaseNow, etCaseImportment, etCaseCheck, etCaseDealType;
    private TextView tvDiagnosisTime;
    private AutoGridView autoGridView;
    private ScrollView scrollView;
    private PatientCaseDetailBean patientCaseDetailBean;
    private Uri originUri;
    private Uri cutFileUri;
    private File cameraTempFile;
    private String patientId;
    /**
     * 时间选择控件
     */
    private TimePickerView timePicker;
    private String diagnosis, diagnosisTime, department, hospital, caseInfo, caseNow, caseImportment, caseCheck, caseDealType;
    /**
     * 是否新增病例
     */
    private boolean isAddNewHealth = false;
    /**
     * 是否选择时间
     */
    private boolean isSelectTime = false;
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
    public int getLayoutID()
    {
        return R.layout.act_health_detail;
    }

    @Override
    protected boolean isInitBackBtn()
    {
        return true;
    }

    private Handler dealImgHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            autoGridView.updateImg(imageList, true);
            //图片显示完开始上传图片
            currentUploadImgIndex = 0;
            showProgressDialog("正在上传第1张图片");
            uploadHeadImg(mSelectPath.get(currentUploadImgIndex));
        }
    };

    @Override
    public void initView(@NonNull Bundle savedInstanceState)
    {
        super.initView(savedInstanceState);
        ((TextView)findViewById(R.id.public_title_bar_title)).setText("健康详情");
        scrollView = (ScrollView)findViewById(R.id.act_health_detail_scroll);
        tvTitleBarMore = (TextView)findViewById(R.id.public_title_bar_more_txt);
        ivTitlebBarMore = (ImageView)findViewById(R.id.public_title_bar_more);
        tvCreateTime = (TextView)findViewById(R.id.act_health_detail_create_time);
        tvHint = (TextView)findViewById(R.id.act_health_detail_hint);
        etDiagnosis = (EditText)findViewById(R.id.act_health_detail_diagnosis);
        tvDiagnosisTime = (TextView)findViewById(R.id.act_health_detail_diagnosis_time);
        tvDiagnosisTime.setText(
                AllUtils.formatDate(System.currentTimeMillis(), AllUtils.DATE_FORMAT_NO_HOUR));
        etDepartment = (EditText)findViewById(R.id.act_health_detail_department);
        etHospital = (EditText)findViewById(R.id.act_health_detail_hopital);
        etCaseInfo = (EditText)findViewById(R.id.act_health_detail_case_info);
        etCaseNow = (EditText)findViewById(R.id.act_health_detail_case_now);
        //        etCaseImportment = (EditText)findViewById(R.id.act_health_detail_case_importent);
        etCaseCheck = (EditText)findViewById(R.id.act_health_detail_check);
        etCaseDealType = (EditText)findViewById(R.id.act_health_detail_deal_type);
        autoGridView = (AutoGridView)findViewById(R.id.act_health_detail_auto_gridview);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState)
    {
        super.initData(savedInstanceState);
        allImgUrl = new StringBuilder();
        if (getIntent() != null)
        {
            isAddNewHealth = getIntent().getBooleanExtra(CommonData.KEY_ADD_NEW_HEALTH, false);
            patientCaseDetailBean = (PatientCaseDetailBean)getIntent().getSerializableExtra(
                    CommonData.PATIENT_CASE_DETAIL_BEAN);
            patientId = getIntent().getStringExtra(CommonData.KEY_PATIENT_ID);
        }
        if (isAddNewHealth)
        {
            tvTitleBarMore.setVisibility(View.VISIBLE);
            tvTitleBarMore.setText("完成");
            initWidght(true);
            isSelectTime = true;
        }
        else
        {
            ivTitlebBarMore.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.GONE);
            initWidght(false);
            isSelectTime = false;
        }
        initPageData();
    }

    @Override
    public void initListener()
    {
        super.initListener();
        tvDiagnosisTime.setOnClickListener(this);
        autoGridView.setOnItemClickListener(this);
        tvTitleBarMore.setOnClickListener(this);
        ivTitlebBarMore.setOnClickListener(this);
        //        scrollView.setOnTouchListener(new View.OnTouchListener()
        //        {
        //            @Override
        //            public boolean onTouch(View v, MotionEvent event)
        //            {
        //                hideSoftInputFromWindow();
        //                return false;
        //            }
        //        });
        scrollView.setOnTouchListener((v, event) ->
                                      {
                                          hideSoftInputFromWindow();
                                          return false;
                                      });
        backBtn.setOnClickListener(view ->
                                   {
                                       hideSoftInputFromWindow();
                                       finish();
                                   });
    }

    /**
     * editview  焦点处理
     *
     * @param mode
     */
    private void initWidght(boolean mode)
    {
        tvDiagnosisTime.setClickable(mode);
        etDiagnosis.setFocusable(mode);
        etDiagnosis.setFocusableInTouchMode(mode);
        //        etDiagnosisTime.setFocusable(mode);
        //        etDiagnosisTime.setFocusableInTouchMode(mode);
        etDepartment.setFocusable(mode);
        etDepartment.setFocusableInTouchMode(mode);
        etHospital.setFocusable(mode);
        etHospital.setFocusableInTouchMode(mode);
        etCaseInfo.setFocusable(mode);
        etCaseInfo.setFocusableInTouchMode(mode);
        etCaseNow.setFocusable(mode);
        etCaseNow.setFocusableInTouchMode(mode);
        //        etCaseImportment.setFocusable(mode);
        //        etCaseImportment.setFocusableInTouchMode(mode);
        etCaseCheck.setFocusable(mode);
        etCaseCheck.setFocusableInTouchMode(mode);
        etCaseDealType.setFocusable(mode);
        etCaseDealType.setFocusableInTouchMode(mode);
    }

    /**
     * 页面初始化
     */
    private void initPageData()
    {
        if (patientCaseDetailBean != null)
        {
            tvCreateTime.setText("创建时间 " + AllUtils.formatDate(patientCaseDetailBean.getGmtCreate(),
                                                               AllUtils.DATE_FORMAT));
            etDiagnosis.setText(patientCaseDetailBean.getDiagnosisInfo());
            tvDiagnosisTime.setText(AllUtils.formatDate(patientCaseDetailBean.getVisDate(),
                                                        AllUtils.DATE_FORMAT_NO_HOUR));
            etDepartment.setText(patientCaseDetailBean.getDoctorDep());
            etHospital.setText(patientCaseDetailBean.getHospital());
            etCaseInfo.setText(patientCaseDetailBean.getPatientWords());
            etCaseNow.setText(patientCaseDetailBean.getCurrentInfo());
            //            etCaseImportment.setText(patientCaseDetailBean.getImportantHistory());
            etCaseCheck.setText(patientCaseDetailBean.getCheckReport());
            etCaseDealType.setText(patientCaseDetailBean.getTreat());
            allImgUrl.append(patientCaseDetailBean.getReportImgUrl());
            String[] str = patientCaseDetailBean.getReportImgUrl().split(",");
            for (int i = 0; i < str.length; i++)
            {
                String url = str[i];
                NormImage normImage = new NormImage();
                normImage.setBigImageUrl(url);
                normImage.setMiddleImageUrl(url);
                normImage.setSmallImageUrl(url);
                imageList.add(normImage);
            }
            autoGridView.updateImg(imageList, false);
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImg(Uri uri)
    {
        File file = FileUtils.getFileByUri(uri,this);
        ScalingUtils.resizePic(this,file.getAbsolutePath());
        mIRequest.uploadHeadImg(file, "jpg", this);
    }

    /**
     * 新增患者病例
     */
    private void addPatientCase()
    {
        mIRequest.addPatientCase(patientId, caseCheck, caseInfo, caseNow, diagnosis, department,
                                 hospital, caseNow, allImgUrl.toString(), caseDealType,
                                 diagnosisTime, this);
    }

    /**
     * 更新患者病例
     */
    private void updatePatientCase()
    {
        mIRequest.updatePatientCase(patientId, patientCaseDetailBean.getId(), caseCheck, caseInfo,
                                    caseNow, diagnosis, department, hospital, caseNow,
                                    allImgUrl.toString(), caseDealType, diagnosisTime, this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.public_title_bar_more:
                ivTitlebBarMore.setVisibility(View.GONE);
                tvHint.setVisibility(View.VISIBLE);
                tvTitleBarMore.setVisibility(View.VISIBLE);
                tvTitleBarMore.setText("完成");
                autoGridView.updateImg(imageList, true);
                initWidght(true);
                isSelectTime = true;
                break;
            case R.id.public_title_bar_more_txt:
                diagnosis = etDiagnosis.getText().toString();
                diagnosisTime = tvDiagnosisTime.getText().toString();
                department = etDepartment.getText().toString();
                hospital = etHospital.getText().toString();
                caseInfo = etCaseInfo.getText().toString();
                caseNow = etCaseNow.getText().toString();
                //                caseImportment = etCaseImportment.getText().toString();
                caseCheck = etCaseCheck.getText().toString();
                caseDealType = etCaseDealType.getText().toString();
                if (TextUtils.isEmpty(diagnosis) || TextUtils.isEmpty(diagnosisTime))
                {
                    ToastUtil.toast(this, "带星号为必填项");
                    return;
                }
                if (isAddNewHealth)
                {
                    addPatientCase();
                }
                else
                {
                    updatePatientCase();
                }
                break;
            case R.id.act_health_detail_diagnosis_time:
                if (isSelectTime)
                {
                    hideSoftInputFromWindow();
                    selectDate();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 时间选择器
     */
    private void selectDate()
    {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);
        //正确设置方式 原因：注意事项有说明
        startDate.set(1900, 1, 1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        String[] strings = time.split("-");
        endDate.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1,
                    Integer.parseInt(strings[2]));
        timePicker = new TimePickerBuilder(this, new OnTimeSelectListener()
        {
            @Override
            public void onTimeSelect(Date date, View v)
            {//选中事件回调
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tvDiagnosisTime.setText(simpleDateFormat.format(date));
            }
        }).setType(new boolean[] { true, true, true, false, false, false })// 默认全部显示
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确定")//确认按钮文字
          .setTitleSize(20)//标题文字大小
          .setTitleText("")//标题文字
          .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
          .isCyclic(false)//是否循环滚动
          .setSubmitColor(R.color.app_main_color)//确定按钮文字颜色
          .setCancelColor(R.color.app_main_color)//取消按钮文字颜色
          .setTitleBgColor(0xffffffff)//标题背景颜色 Night mode
          .setBgColor(0xffffffff)//滚轮背景颜色 Night mode
          .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
          .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
          .isDialog(false)//是否显示为对话框样式
          .build();
        timePicker.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (position < imageList.size() && imageList.size() <= maxPicNum)
        {
            if (isAddNewHealth || isSelectTime) { return; }
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            intent.putExtra(ImagePreviewActivity.INTENT_POSITION, position);
            intent.putExtra(ImagePreviewActivity.INTENT_URLS, imageList);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.keep);
        }
        else
        {
            currentMaxPicNum = maxPicNum - imageList.size();
            selectImg();
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response)
    {
        super.onResponseSuccess(task, response);
        switch (task)
        {
            case UPLOAD_FILE:
                if (mSelectPath.size() - 1 > currentUploadImgIndex)
                {
                    currentUploadImgIndex++;
                    showProgressDialog("正在上传第" + (currentUploadImgIndex + 1) + "张图片");
                    uploadHeadImg(mSelectPath.get(currentUploadImgIndex));
                }
                else
                {
                    closeProgressDialog();
                }
                if (!TextUtils.isEmpty(allImgUrl.toString()))
                {
                    allImgUrl.append(",");
                }
                allImgUrl.append(response.getData().toString());
                break;
            case UPDATE_PATIENT_CASE:
                ToastUtil.toast(this, "保存成功");
                ivTitlebBarMore.setVisibility(View.VISIBLE);
                tvTitleBarMore.setVisibility(View.GONE);
                initWidght(false);
                //                finish();
                break;
            case ADD_PATIENT_CASE:
                ToastUtil.toast(this, "保存成功");
                ivTitlebBarMore.setVisibility(View.VISIBLE);
                tvTitleBarMore.setVisibility(View.GONE);
                initWidght(false);
                //                finish();
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
            case UPLOAD_FILE:
                closeProgressDialog();
                ToastUtil.toast(this, response.getMsg());
                break;
        }
    }

    /**
     * 处理图片
     */
    class DealImgThread extends Thread
    {
        @Override
        public void run()
        {
            super.run();
            for (Uri path : mSelectPath)
            {
                imageList.add(QiniuUtils.initQuanBitmaps(path, HealthDetailActivity.this));
            }
            dealImgHandler.sendEmptyMessage(0);
        }
    }

    private void selectImg()
    {
        new ActionSheetDialog(this).builder()
                                   .setCancelable(false)
                                   .setCanceledOnTouchOutside(false)
                                   .addSheetItem("相册", ActionSheetDialog.SheetItemColor.Blue,
                                                 new ActionSheetDialog.OnSheetItemClickListener()
                                                 {
                                                     @Override
                                                     public void onClick(int which)
                                                     {
                                                         //动态申请权限
                                                         permissionHelper.request(new String[] {
                                                                 Permission.STORAGE_WRITE });
                                                     }
                                                 })
                                   .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                                                 new ActionSheetDialog.OnSheetItemClickListener()
                                                 {
                                                     @Override
                                                     public void onClick(int which)
                                                     {
                                                         //动态申请权限
                                                         permissionHelper.request(new String[] {
                                                                 Permission.CAMERA,
                                                                 Permission.STORAGE_WRITE });
                                                     }
                                                 })
                                   .show();
    }

    /**
     * 打开图片库
     */
    private void openPhoto()
    {
        Matisse.from(this)
               // 选择 mime 的类型
               .choose(MimeType.allOf())
               // 显示选择的数量
               .countable(true)
               //                //相机
               //                .capture(true)
               //                .captureStrategy(new CaptureStrategy(true, "com.yht.yihuantong.fileprovider"))
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
               .imageEngine(new PicassoEngine())
               // 设置作为标记的请求码，返回图片时使用
               .forResult(RC_PICK_IMG);
    }

    /**
     * 打开相机
     */
    private void openCamera()
    {
        String pathRecv = DirHelper.getPathImage();
        cameraTempFile = new File(pathRecv, System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            cameraintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, "com.yht.yihuantong.fileprovider",
                                             cameraTempFile);
        }
        else
        {
            uri = Uri.fromFile(cameraTempFile);
        }
        // 指定调用相机拍照后照片的储存路径
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        cameraintent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(cameraintent, RC_PICK_CAMERA_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                mSelectPath.clear();
                mSelectPath.addAll(paths);
                break;
            case RC_PICK_CAMERA_IMG:
                if (cameraTempFile.exists())
                {
                    Uri originalUri = Uri.fromFile(cameraTempFile);
                    mSelectPath.clear();
                    mSelectPath.add(originalUri);
                }
                break;
        }
        new DealImgThread().start();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInputFromWindow()
    {
        InputMethodManager inputmanger = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(etDiagnosis.getWindowToken(), 0);
    }

    /***************************************权限处理**********************/
    private boolean isSamePermission(String o, String n)
    {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n))
        {
            return false;
        }
        if (o.equals(n))
        {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        if (permissions == null)
        {
            return;
        }
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName)
    {
        if (permissionName == null || permissionName.length == 0)
        {
            return;
        }
        if (isSamePermission(Permission.CAMERA, permissionName[0]))
        {
            openCamera();
        }
        else if (isSamePermission(Permission.STORAGE_WRITE, permissionName[0]))
        {
            openPhoto();
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName)
    {
        super.onPermissionDeclined(permissionName);
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName)
    {
        LogUtils.d("test", "onPermissionPreGranted:" + permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName)
    {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName)
    {
        new SimpleDialog(this, R.string.dialog_no_camera_permission_tip, false).show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName)
    {
        if (permissionName instanceof String[])
        {
            if (isSamePermission(Permission.STORAGE_WRITE, ((String[])permissionName)[0]))
            {
                openPhoto();
            }
            else if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0]))
            {
                openCamera();
            }
        }
    }
}
