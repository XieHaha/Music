package com.zyc.doctor.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.DirHelper;
import com.zyc.doctor.api.ThreadPoolHelper;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CheckUrl;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.base.activity.BaseActivity;
import com.zyc.doctor.ui.dialog.ActionSheetDialog;
import com.zyc.doctor.ui.dialog.HintDialog;
import com.zyc.doctor.utils.AllUtils;
import com.zyc.doctor.utils.FileUtils;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.ScalingUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.utils.glide.MatisseUtils;
import com.zyc.doctor.utils.permission.Permission;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

/**
 * @author dundun
 * @date 18/5/7
 * 医生认证
 */
public class AuthDocActivity extends BaseActivity {
    private static final String TAG = "AuthDocActivity";
    @BindView(R.id.public_title_bar_more_txt)
    TextView tvTitleMore;
    @BindView(R.id.act_auth_doc_name)
    EditText etName;
    @BindView(R.id.act_auth_doc_card_num)
    EditText etCardNumber;
    @BindView(R.id.act_auth_doc_idcard_front)
    ImageView ivIdCardFront;
    @BindView(R.id.act_auth_doc_idcard_front_hint)
    TextView tvIdCardFrontHint;
    @BindView(R.id.act_auth_doc_idcard_back)
    ImageView ivIdCardBack;
    @BindView(R.id.act_auth_doc_idcard_back_hint)
    TextView tvIdCardBackHint;
    @BindView(R.id.act_auth_doc_hospital)
    EditText etHospital;
    @BindView(R.id.act_auth_doc_title)
    EditText etTitle;
    @BindView(R.id.act_auth_doc_depart)
    TextView tvDepart;
    @BindView(R.id.act_auth_doc_doccard_front)
    ImageView ivDocCardFront;
    @BindView(R.id.act_auth_doc_doccard_front_hint)
    TextView tvDocCardFrontHint;
    @BindView(R.id.act_auth_doc_doccard_back)
    ImageView ivDocCardBack;
    @BindView(R.id.act_auth_doc_doccard_back_hint)
    TextView tvDocCardBackHint;
    @BindView(R.id.act_auth_doc_apply_layout)
    RelativeLayout rlApplyLayout;
    private File tempFile, idCardFrontTempFile, idCardBackTempFile, docCardFrontTempFile, docCardBackTempFile;
    private String txtName, txtCardNum, txtHospital, txtTitle, txtDepart;
    private CooperateDocBean cooperateDocBean;
    /**
     * 照片路径
     */
    private Uri uri;
    /**
     * 是否为重新提交资料
     */
    private boolean again;
    /**
     * 请求修改头像 相册
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * 请求修改头像 相机
     */
    private static final int RC_PICK_CAMERA_IMG = RC_PICK_IMG + 1;
    private int type = -1;
    /**
     * 身份证正面
     */
    private final int ID_CARD_FRONT = 1;
    /**
     * 身份证背面
     */
    private final int ID_CARD_BACK = 2;
    /**
     * 资质证明正面
     */
    private final int DOC_CARD_FRONT = 3;
    /**
     * 资质证明背面
     */
    private final int DOC_CARD_BACK = 4;
    /**
     * 医生认证状态
     */
    private int authStatus = -1;
    /**
     * 医生科室
     */
    public static final int REQUEST_DOC_DEPART = 100;

    @Override
    public int getLayoutID() {
        return R.layout.act_auth_doc;
    }

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable(CommonData.KEY_SAVE_DATA);
        }
        tvTitleMore.setText(R.string.txt_doc_auth_again);
        if (getIntent() != null) {
            again = getIntent().getBooleanExtra(CommonData.KEY_DOC_AUTH_AGAIN, false);
        }
        authStatus = loginSuccessBean.getChecked();
        if (authStatus == 0 || again) {
            updateMode(true);
            tvTitleMore.setVisibility(View.GONE);
        }
        else {
            updateMode(false);
            tvTitleMore.setVisibility(View.VISIBLE);
        }
        getDocInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CommonData.KEY_SAVE_DATA, uri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initListener() {
        tvIdCardFrontHint.setOnClickListener(this);
        tvIdCardBackHint.setOnClickListener(this);
        tvDocCardFrontHint.setOnClickListener(this);
        tvDocCardBackHint.setOnClickListener(this);
        //0未认证
        if (authStatus == 0) {
            findViewById(R.id.act_auth_doc_idcard_front_layout).setOnClickListener(this);
            findViewById(R.id.act_auth_doc_idcard_back_layout).setOnClickListener(this);
            findViewById(R.id.act_auth_doc_doccard_front_layout).setOnClickListener(this);
            findViewById(R.id.act_auth_doc_doccard_back_layout).setOnClickListener(this);
        }
        findViewById(R.id.act_auth_doc_apply).setOnClickListener(this);
        tvDepart.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                qualifyDoc();
            }
            return false;
        });
    }

    /**
     * 初始化界面数据
     */
    private void initPageData() {
        if (cooperateDocBean != null) {
            etName.setText(cooperateDocBean.getName());
            etCardNumber.setText(cooperateDocBean.getIdentityNumber());
            etHospital.setText(cooperateDocBean.getHospital());
            etTitle.setText(cooperateDocBean.getTitle());
            tvDepart.setText(cooperateDocBean.getDepartment());
            CheckUrl checkUrl = new Gson().fromJson(cooperateDocBean.getCheckUrl(), CheckUrl.class);
            if (checkUrl != null) {
                Glide.with(this).load(checkUrl.getIdFront()).into(ivIdCardFront);
                Glide.with(this).load(checkUrl.getIdEnd()).into(ivIdCardBack);
                Glide.with(this).load(checkUrl.getQualifiedFront()).into(ivDocCardFront);
                Glide.with(this).load(checkUrl.getQualifiedEnd()).into(ivDocCardBack);
            }
            ThreadPoolHelper.getInstance().execInSingle(() -> {
                if (checkUrl != null) {
                    FutureTarget<File> target = Glide.with(AuthDocActivity.this)
                                                     .asFile()
                                                     .load(checkUrl.getIdFront())
                                                     .submit();
                    FutureTarget<File> target1 = Glide.with(AuthDocActivity.this)
                                                      .asFile()
                                                      .load(checkUrl.getIdEnd())
                                                      .submit();
                    FutureTarget<File> target2 = Glide.with(AuthDocActivity.this)
                                                      .asFile()
                                                      .load(checkUrl.getQualifiedFront())
                                                      .submit();
                    FutureTarget<File> target3 = Glide.with(AuthDocActivity.this)
                                                      .asFile()
                                                      .load(checkUrl.getQualifiedEnd())
                                                      .submit();
                    try {
                        idCardFrontTempFile = target.get();
                        idCardBackTempFile = target1.get();
                        docCardFrontTempFile = target2.get();
                        docCardBackTempFile = target3.get();
                    }
                    catch (InterruptedException e) {
                        /**在代码中永远不应该忽略InterruptedExceptions，并且在这种情况下简单地记录异常计数为“忽略”。
                         *  抛出InterruptedException会清除Thread的中断状态，因此如果未正确处理异常，
                         *  则线程被中断的事实将会丢失。 相反，InterruptedExceptions应该被重新抛出
                         *  - 立即或在清理方法的状态之后 - 或者应该通过调用Thread.interrupt（）重新中断线程，
                         *  即使这应该是单线程应用程序。 任何其他行动方案都有可能延迟线程关闭并丢失线程被中断的信息 -
                         *  可能没有完成任务。**/
                        Thread.currentThread().interrupt();
                        LogUtils.w(TAG, "Exception error!", e);
                    }
                    catch (ExecutionException e) {
                        LogUtils.w(TAG, "Exception error!", e);
                    }
                }
            });
        }
    }

    /**
     * 编辑模式
     *
     * @param isEdit
     */
    private void updateMode(boolean isEdit) {
        etName.setFocusable(isEdit);
        etName.setFocusableInTouchMode(isEdit);
        etCardNumber.setFocusable(isEdit);
        etCardNumber.setFocusableInTouchMode(isEdit);
        etHospital.setFocusable(isEdit);
        etHospital.setFocusableInTouchMode(isEdit);
        etTitle.setFocusable(isEdit);
        etTitle.setFocusableInTouchMode(isEdit);
        if (!isEdit) {
            rlApplyLayout.setVisibility(View.GONE);
            tvDepart.setOnClickListener(null);
        }
        else {
            rlApplyLayout.setVisibility(View.VISIBLE);
            tvDepart.setOnClickListener(this);
        }
        if (authStatus != 0) {
            if (!isEdit) {
                tvIdCardFrontHint.setVisibility(View.GONE);
                tvIdCardBackHint.setVisibility(View.GONE);
                tvDocCardFrontHint.setVisibility(View.GONE);
                tvDocCardBackHint.setVisibility(View.GONE);
            }
            else {
                tvIdCardFrontHint.setVisibility(View.VISIBLE);
                tvIdCardBackHint.setVisibility(View.VISIBLE);
                tvDocCardFrontHint.setVisibility(View.VISIBLE);
                tvDocCardBackHint.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 获取个人信息
     */
    private void getDocInfo() {
        showLoadingView();
        RequestUtils.getDocInfo(this, loginSuccessBean.getDoctorId(), this);
    }

    /**
     * 提交审核 医生资质认证
     */
    private void qualifyDoc() {
        txtName = etName.getText().toString().trim();
        txtCardNum = etCardNumber.getText().toString().trim();
        txtHospital = etHospital.getText().toString().trim();
        txtTitle = etTitle.getText().toString().trim();
        txtDepart = tvDepart.getText().toString().trim();
        if (!AllUtils.isCardNum(txtCardNum)) {
            ToastUtil.toast(this, R.string.toast_upload_card_hint);
            return;
        }
        if (TextUtils.isEmpty(txtName) || TextUtils.isEmpty(txtCardNum) || idCardFrontTempFile == null ||
            idCardBackTempFile == null) {
            ToastUtil.toast(this, R.string.txt_doc_auth_improve_identity);
            return;
        }
        if (TextUtils.isEmpty(txtHospital) || TextUtils.isEmpty(txtTitle) || TextUtils.isEmpty(txtDepart) ||
            docCardFrontTempFile == null || docCardBackTempFile == null) {
            ToastUtil.toast(this, R.string.txt_doc_auth_improve_qualification);
            return;
        }
        showLoadingView();
        RequestUtils.qualifiyDoc(this, loginSuccessBean.getDoctorId(), txtName, txtCardNum, txtTitle, txtDepart,
                                 txtHospital, idCardFrontTempFile, idCardBackTempFile, docCardFrontTempFile,
                                 docCardBackTempFile, this);
    }

    private void uploadImg(int type) {
        this.type = type;
        new ActionSheetDialog(this).builder()
                                   .setCancelable(true)
                                   .setCanceledOnTouchOutside(true)
                                   .addSheetItem(getString(R.string.txt_capture_img),
                                                 ActionSheetDialog.SheetItemColor.Blue, which -> {
                                               //动态申请权限
                                               permissionHelper.request(new String[] { Permission.STORAGE_WRITE });
                                           })
                                   .addSheetItem(getString(R.string.txt_camera_img),
                                                 ActionSheetDialog.SheetItemColor.Blue, which -> {
                                               //动态申请权限
                                               permissionHelper.request(
                                                       new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                                           })
                                   .show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_auth_doc_idcard_front_hint:
            case R.id.act_auth_doc_idcard_front_layout:
                uploadImg(ID_CARD_FRONT);
                break;
            case R.id.act_auth_doc_idcard_back_hint:
            case R.id.act_auth_doc_idcard_back_layout:
                uploadImg(ID_CARD_BACK);
                break;
            case R.id.act_auth_doc_doccard_front_hint:
            case R.id.act_auth_doc_doccard_front_layout:
                uploadImg(DOC_CARD_FRONT);
                break;
            case R.id.act_auth_doc_doccard_back_hint:
            case R.id.act_auth_doc_doccard_back_layout:
                uploadImg(DOC_CARD_BACK);
                break;
            case R.id.act_auth_doc_apply:
                qualifyDoc();
                break;
            case R.id.act_auth_doc_depart:
                Intent intent = new Intent(this, SelectDocTypeActivity.class);
                startActivityForResult(intent, REQUEST_DOC_DEPART);
                break;
            default:
                break;
        }
    }

    /**
     * @param view
     */
    public void onTitleMoreClick(View view) {
        tvTitleMore.setVisibility(View.GONE);
        updateMode(true);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_DOC_INFO:
                closeLoadingView();
                cooperateDocBean = (CooperateDocBean)response.getData();
                initPageData();
                break;
            case QUALIFIY_DOC:
                closeLoadingView();
                ToastUtil.toast(this, response.getMsg());
                //改变认证状态，当前为审核中
                loginSuccessBean.setChecked(1);
                loginSuccessBean.setName(txtName);
                loginSuccessBean.setTitle(txtTitle);
                loginSuccessBean.setDepartment(txtDepart);
                loginSuccessBean.setHospital(txtHospital);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
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
            case GET_DOC_INFO:
                break;
            case QUALIFIY_DOC:
                closeLoadingView();
                ToastUtil.toast(this, response.getMsg());
                break;
            default:
                break;
        }
    }

    /**
     * 打开图片库
     */
    private void openPhoto() {
        MatisseUtils.open(this);
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        tempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        //选择拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, YihtApplication.getInstance().getPackageName() + ".fileprovider",
                                             tempFile);
        }
        else {
            uri = Uri.fromFile(tempFile);
        }
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                                                                                  PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri,
                               Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // 指定调用相机拍照后照片的储存路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA_IMG);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                if (null != paths && 0 != paths.size()) {
                    Uri imgUri = paths.get(0);
                    switch (type) {
                        case ID_CARD_FRONT:
                            tvIdCardFrontHint.setVisibility(View.GONE);
                            idCardFrontTempFile = new File(FileUtils.getFileByUri(imgUri, this));
                            ScalingUtils.resizePic(this, idCardFrontTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivIdCardFront);
                            break;
                        case ID_CARD_BACK:
                            tvIdCardBackHint.setVisibility(View.GONE);
                            idCardBackTempFile = new File(FileUtils.getFileByUri(imgUri, this));
                            ScalingUtils.resizePic(this, idCardBackTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivIdCardBack);
                            break;
                        case DOC_CARD_FRONT:
                            tvDocCardFrontHint.setVisibility(View.GONE);
                            docCardFrontTempFile = new File(FileUtils.getFileByUri(imgUri, this));
                            ScalingUtils.resizePic(this, docCardFrontTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivDocCardFront);
                            break;
                        case DOC_CARD_BACK:
                            tvDocCardBackHint.setVisibility(View.GONE);
                            docCardBackTempFile = new File(FileUtils.getFileByUri(imgUri, this));
                            ScalingUtils.resizePic(this, docCardBackTempFile.getAbsolutePath());
                            Glide.with(this).load(imgUri).into(ivDocCardBack);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case RC_PICK_CAMERA_IMG:
                if (tempFile == null) {
                    tempFile = new File(FileUtils.getFileByUri(uri, this));
                }
                ScalingUtils.resizePic(this, tempFile.getAbsolutePath());
                switch (type) {
                    case ID_CARD_FRONT:
                        tvIdCardFrontHint.setVisibility(View.GONE);
                        idCardFrontTempFile = tempFile;
                        Glide.with(this).load(uri).into(ivIdCardFront);
                        break;
                    case ID_CARD_BACK:
                        tvIdCardBackHint.setVisibility(View.GONE);
                        idCardBackTempFile = tempFile;
                        Glide.with(this).load(uri).into(ivIdCardBack);
                        break;
                    case DOC_CARD_FRONT:
                        tvDocCardFrontHint.setVisibility(View.GONE);
                        docCardFrontTempFile = tempFile;
                        Glide.with(this).load(uri).into(ivDocCardFront);
                        break;
                    case DOC_CARD_BACK:
                        tvDocCardBackHint.setVisibility(View.GONE);
                        docCardBackTempFile = tempFile;
                        Glide.with(this).load(uri).into(ivDocCardBack);
                        break;
                    default:
                        break;
                }
                break;
            case REQUEST_DOC_DEPART:
                txtDepart = data.getStringExtra(CommonData.KEY_PUBLIC_STRING);
                tvDepart.setText(txtDepart);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        HintDialog hintDialog = new HintDialog(this);
        hintDialog.setContentString(getString(R.string.dialog_no_camera_permission_tip));
        hintDialog.show();
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
