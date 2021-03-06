package com.zyc.doctor.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.zhihu.matisse.Matisse;
import com.zyc.doctor.R;
import com.zyc.doctor.YihtApplication;
import com.zyc.doctor.api.ApiManager;
import com.zyc.doctor.api.DirHelper;
import com.zyc.doctor.api.notify.IChange;
import com.zyc.doctor.api.notify.INotifyChangeListenerServer;
import com.zyc.doctor.api.notify.RegisterType;
import com.zyc.doctor.data.CommonData;
import com.zyc.doctor.data.Tasks;
import com.zyc.doctor.data.bean.BaseNetConfig;
import com.zyc.doctor.data.bean.BaseResponse;
import com.zyc.doctor.data.bean.CooperateDocBean;
import com.zyc.doctor.data.bean.LoginSuccessBean;
import com.zyc.doctor.data.bean.PatientBean;
import com.zyc.doctor.http.retrofit.RequestUtils;
import com.zyc.doctor.ui.activity.AuthDocActivity;
import com.zyc.doctor.ui.activity.AuthDocStatusActivity;
import com.zyc.doctor.ui.activity.EditInfoActivity;
import com.zyc.doctor.ui.activity.SettingActivity;
import com.zyc.doctor.ui.activity.TransferPatientFromActivity;
import com.zyc.doctor.ui.activity.TransferPatientToActivity;
import com.zyc.doctor.ui.base.fragment.BaseFragment;
import com.zyc.doctor.ui.dialog.ActionSheetDialog;
import com.zyc.doctor.utils.BaseUtils;
import com.zyc.doctor.utils.FileUtils;
import com.zyc.doctor.utils.LogUtils;
import com.zyc.doctor.utils.ToastUtil;
import com.zyc.doctor.utils.glide.GlideHelper;
import com.zyc.doctor.utils.glide.MatisseUtils;
import com.zyc.doctor.utils.permission.OnPermissionCallback;
import com.zyc.doctor.utils.permission.Permission;
import com.zyc.doctor.utils.permission.PermissionHelper;
import com.zyc.doctor.widgets.qrcode.BarCodeImageView;
import com.zyc.doctor.widgets.qrcode.DialogPersonalBarCode;
import com.zyc.doctor.widgets.scrollview.CustomListenScrollView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * ????????????
 *
 * @author dundun
 */
public class UserFragment extends BaseFragment implements CustomListenScrollView.OnScrollChangeListener, CommonData {
    @BindView(R.id.fragmrnt_user_info_headimg)
    CircleImageView headImg;
    @BindView(R.id.fragmrnt_user_info_auth)
    CircleImageView authImg;
    @BindView(R.id.fragmrnt_user_info_name)
    TextView tvName;
    @BindView(R.id.public_title_bar_back)
    ImageView ivEditInfo;
    @BindView(R.id.fragment_my_auth_status)
    TextView tvAuthStatus;
    @BindView(R.id.fragment_my_auth)
    TextView tvAuth;
    @BindView(R.id.fragment_my_auth_layout)
    RelativeLayout rlAuthLayout;
    @BindView(R.id.fragmrnt_user_info_title)
    TextView tvTitle;
    @BindView(R.id.fragmrnt_user_info_hospital)
    TextView tvHospital;
    @BindView(R.id.fragmrnt_user_info_type)
    TextView tvType;
    @BindView(R.id.fragmrnt_user_info_introduce)
    TextView tvIntroduce;
    @BindView(R.id.message_red_point)
    RelativeLayout rlTransferMsg;
    @BindView(R.id.fragmrnt_user_doctors_num)
    TextView tvDocNum;
    @BindView(R.id.fragmrnt_user_patients_num)
    TextView tvPatientNum;
    @BindView(R.id.fragment_user_remind)
    TextView tvRemind;
    @BindView(R.id.fragment_my_scrollview)
    CustomListenScrollView scrollView;
    private LoginSuccessBean loginSuccessBean;
    private INotifyChangeListenerServer iNotifyChangeListenerServer;
    private Uri originUri, cutFileUri;
    private File cameraTempFile;
    /**
     * ????????????
     */
    private Uri uri;
    /**
     * ????????????
     */
    private int page = 0;
    /**
     * ???????????????
     */
    private static final int PAGE_SIZE = 500;
    /**
     * ???????????????
     */
    protected PermissionHelper permissionHelper;
    /**
     * ?????????
     */
    private BarCodeImageView barCodeImageView;
    /**
     * ?????????????????? ??????
     */
    private static final int RC_PICK_IMG = 0x0001;
    /**
     * ?????????????????? ??????
     */
    private static final int RC_PICK_CAMERA_IMG = RC_PICK_IMG + 1;
    /**
     * ??????  ??????
     */
    public static final int RC_CROP_IMG = RC_PICK_CAMERA_IMG + 1;
    /**
     * ??????  ??????
     */
    public static final int REQUEST_CODE_DOC_AUTH = RC_CROP_IMG + 1;
    private String headImgUrl;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED:
                    //????????????  ??????????????????
                    loginSuccessBean.setChecked(2);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initAuthStatus(2);
                    break;
                case JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS:
                    //????????????  ??????????????????
                    loginSuccessBean.setChecked(6);
                    YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                    initAuthStatus(6);
                    break;
                case 10:
                    //TODO ?????????????????????????????????
                    sharePreferenceUtil.putBoolean(CommonData.KEY_CHANGE_PATIENT_NUM, true);
                    if (onTransferCallbackListener != null) {
                        onTransferCallbackListener.onTransferCallback();
                    }
                    rlTransferMsg.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    /**
     * ??????????????????  ????????????
     */
    private IChange<String> doctorTransferPatientListener = data -> {
    };
    /**
     * ??????????????????
     */
    private IChange<Integer> doctorAuthStatusChangeListener = data -> {
        handler.sendEmptyMessage(data);
    };

    @Override
    public int getLayoutID() {
        return R.layout.fragment_my;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPageData();
    }

    @Override
    public void initView(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        //??????????????????????????????
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        view.findViewById(R.id.fragmrnt_user_info_setting_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_train_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_service_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_transfer_to_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_transfer_from_layout).setOnClickListener(this);
        view.findViewById(R.id.fragmrnt_user_info_qrcode_layout).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            uri = savedInstanceState.getParcelable(CommonData.KEY_SAVE_DATA);
        }
        /**
         * ???????????????
         */
        permissionHelper = PermissionHelper.getInstance(getActivity(), onPermissionCallback);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
    }

    @Override
    public void initListener() {
        super.initListener();
        ivEditInfo.setOnClickListener(this);
        headImg.setOnClickListener(this);
        scrollView.setOnScrollChangeListener(this);
        rlAuthLayout.setOnClickListener(this);
        //????????????????????????
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(doctorAuthStatusChangeListener,
                                                                           RegisterType.REGISTER);
        //????????????????????????
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(doctorTransferPatientListener,
                                                                          RegisterType.REGISTER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CommonData.KEY_SAVE_DATA, uri);
        super.onSaveInstanceState(outState);
    }

    /**
     * ????????????
     */
    private void uploadHeadImg(Uri uri) {
        File file = new File(FileUtils.getFilePathFromURI(uri, getActivity()));
        RequestUtils.uploadImg(getContext(), file, "jpg", this);
    }

    /**
     * ??????????????????
     */
    private void updateBasicInfo() {
        if (TextUtils.isEmpty(headImgUrl)) {
            return;
        }
        JsonObject json = new JsonObject();
        json.addProperty("portraitUrl", headImgUrl);
        RequestUtils.updateUserInfo(getContext(), loginSuccessBean.getDoctorId(), loginSuccessBean.getFieldId(), json,
                                    this);
    }

    /**
     * ?????????????????????
     */
    private void initPageData() {
        tvPatientNum.setText(String.format(getString(R.string.txt_user_info_num),
                                           sharePreferenceUtil.getString(CommonData.KEY_PATIENT_NUM)));
        tvDocNum.setText(String.format(getString(R.string.txt_user_info_num),
                                       sharePreferenceUtil.getString(CommonData.KEY_DOCTOR_NUM)));
        loginSuccessBean = YihtApplication.getInstance().getLoginSuccessBean();
        if (loginSuccessBean != null) {
            barCodeImageView = new BarCodeImageView(getActivity(), BaseNetConfig.BASE_BASIC_DOWNLOAD_URL +
                                                                   loginSuccessBean.getDoctorId());
            if (!TextUtils.isEmpty(loginSuccessBean.getPortraitUrl())) {
                headImgUrl = loginSuccessBean.getPortraitUrl();
            }
            else if (!TextUtils.isEmpty(YihtApplication.getInstance().getHeadImgUrl())) {
                headImgUrl = YihtApplication.getInstance().getHeadImgUrl();
            }
            if (!TextUtils.isEmpty(headImgUrl)) {
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(headImg);
            }
            //????????????
            initAuthStatus(loginSuccessBean.getChecked());
            tvName.setText(loginSuccessBean.getName());
            tvHospital.setText(loginSuccessBean.getHospital());
            tvTitle.setText(loginSuccessBean.getTitle());
            tvType.setText(loginSuccessBean.getDepartment());
            tvIntroduce.setText(loginSuccessBean.getDoctorDescription());
        }
        if (YihtApplication.getInstance().isVersionRemind()) {
            tvRemind.setVisibility(View.VISIBLE);
        }
        else {
            tvRemind.setVisibility(View.GONE);
        }
    }

    /**
     * ????????????????????????
     *
     * @param status
     */
    private void initAuthStatus(int status) {
        switch (status) {
            //?????????
            case 0:
                tvAuth.setText("?????????");
                tvAuthStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_auth_faild));
                Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                ivEditInfo.setVisibility(View.VISIBLE);
                break;
            //?????????
            case 1:
                tvAuthStatus.setText("?????????");
                tvAuth.setText("??????");
                tvAuthStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_auth_faild));
                Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                break;
            //???????????????
            case 2:
                tvAuthStatus.setText("???????????????");
                tvAuth.setText("??????");
                tvAuthStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_auth_faild));
                Glide.with(this).load(R.mipmap.icon_uncertified).into(authImg);
                ivEditInfo.setVisibility(View.VISIBLE);
                break;
            //???????????????
            case 6:
                tvAuthStatus.setText("?????????");
                tvAuth.setText("??????");
                tvAuthStatus.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_auth_success));
                Glide.with(this).load(R.mipmap.icon_certified).into(authImg);
                ivEditInfo.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_info_qrcode_layout:
                DialogPersonalBarCode dialogPersonalBarCode = new DialogPersonalBarCode(getActivity());
                dialogPersonalBarCode.setQRImageViewSrc(barCodeImageView);
                dialogPersonalBarCode.show();
                break;
            case R.id.fragment_my_auth_layout:
                intent = new Intent(getActivity(), AuthDocActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DOC_AUTH);
                break;
            case R.id.fragmrnt_user_info_headimg:
                editHeadImg();
                break;
            case R.id.fragmrnt_user_info_setting_layout:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.fragmrnt_user_transfer_to_layout:
                intent = new Intent(getActivity(), TransferPatientToActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_transfer_from_layout:
                rlTransferMsg.setVisibility(View.GONE);
                sharePreferenceUtil.putBoolean(CommonData.KEY_CHANGE_PATIENT_NUM, false);
                if (onTransferCallbackListener != null) {
                    onTransferCallbackListener.onTransferCallback();
                }
                intent = new Intent(getActivity(), TransferPatientFromActivity.class);
                startActivity(intent);
                break;
            case R.id.fragmrnt_user_info_train_layout:
            case R.id.fragmrnt_user_info_service_layout:
                ToastUtil.toast(getActivity(), "????????????");
                break;
            default:
                break;
        }
    }

    /**
     * ????????????
     */
    private void editHeadImg() {
        new ActionSheetDialog(getActivity()).builder()
                                            .setCancelable(true)
                                            .setCanceledOnTouchOutside(true)
                                            .addSheetItem("??????", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                                //??????????????????
                                                permissionHelper.request(new String[] { Permission.STORAGE_WRITE });
                                            })
                                            .addSheetItem("??????", ActionSheetDialog.SheetItemColor.Blue, which -> {
                                                //??????????????????
                                                permissionHelper.request(
                                                        new String[] { Permission.CAMERA, Permission.STORAGE_WRITE });
                                            })
                                            .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case UPLOAD_FILE:
                headImgUrl = (String)response.getData();
                Log.i("test", "headImgUrl:" + headImgUrl);
                updateBasicInfo();
                break;
            case UPDATE_USER_INFO:
                ToastUtil.toast(getActivity(), "????????????");
                Glide.with(this).load(headImgUrl).apply(GlideHelper.getOptions()).into(headImg);
                loginSuccessBean.setPortraitUrl(headImgUrl);
                YihtApplication.getInstance().setLoginSuccessBean(loginSuccessBean);
                break;
            default:
                break;
        }
    }

    /**
     * ??????scrollview????????????
     *
     * @param scrollView
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    public void onScrollChanged(CustomListenScrollView scrollView, int l, int t, int oldl, int oldt) {
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
        cameraTempFile = new File(DirHelper.getPathImage(), System.currentTimeMillis() + ".jpg");
        //????????????
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ????????????????????????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            uri = FileProvider.getUriForFile(getActivity(),
                                             YihtApplication.getInstance().getPackageName() + ".fileprovider",
                                             cameraTempFile);
        }
        else {
            uri = Uri.fromFile(cameraTempFile);
        }
        List<ResolveInfo> resInfoList = getActivity().getPackageManager()
                                                     .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                                               Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // ????????????????????????????????????????????????
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(intent, RC_PICK_CAMERA_IMG);
    }

    /**
     * ????????????
     */
    private void startCutImg(Uri uri, Uri cutUri) {
        originUri = uri;
        cutFileUri = cutUri;
        //????????????
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //???????????????????????????????????????????????????Uri??????????????????
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        // ???Android N????????????????????????????????????????????????????????????Uri???????????????????????????????????????????????????????????????uri????????????????????? ???????????????????????????
        intent.setDataAndType(originUri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        if (Build.BRAND.toUpperCase().contains("HONOR") || Build.BRAND.toUpperCase().contains("HUAWEI")) {
            //?????????????????? ??????????????????
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        }
        else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutFileUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, RC_CROP_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case RC_PICK_IMG:
                List<Uri> paths = Matisse.obtainResult(data);
                if (null != paths && 0 != paths.size()) {
                    cameraTempFile = new File(FileUtils.getFilePathFromURI(paths.get(0), getActivity()));
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    startCutImg(paths.get(0), Uri.fromFile(file));
                }
                break;
            case RC_PICK_CAMERA_IMG:
                if (cameraTempFile == null) {
                    cameraTempFile = new File(FileUtils.getFilePathFromURI(uri, getContext()));
                }
                if (cameraTempFile.exists()) {
                    String fileName = "corp" + System.currentTimeMillis() + ".jpg";
                    File file = new File(DirHelper.getPathCache(), fileName);
                    Uri cropUri;
                    cropUri = Uri.fromFile(file);
                    startCutImg(uri, cropUri);
                }
                break;
            case RC_CROP_IMG:
                //???????????????????????????
                if (BaseUtils.isNetworkAvaliable(getActivity())) {
                    uploadHeadImg(cutFileUri);
                }
                else {
                    ToastUtil.toast(getActivity(), R.string.toast_public_current_no_network);
                }
                //?????????????????????????????????
                Glide.with(this).load(cutFileUri).into(headImg);
                break;
            case REQUEST_CODE_DOC_AUTH:
                //?????????????????????
                DataSupport.deleteAll(PatientBean.class);
                DataSupport.deleteAll(CooperateDocBean.class);
                startActivity(new Intent(getActivity(), AuthDocStatusActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private OnPermissionCallback onPermissionCallback = new OnPermissionCallback() {
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
            if (permissionName == null) {
                return;
            }
            for (String permission : permissionName) {
                if (Permission.STORAGE_WRITE.equals(permission)) {
                    ToastUtil.toast(getActivity(), R.string.dialog_no_storage_permission_tip);
                    break;
                }
                if (Permission.CAMERA.equals(permission)) {
                    ToastUtil.toast(getActivity(), R.string.dialog_no_camera_permission_tip);
                    break;
                }
            }
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
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //????????????????????????
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(doctorAuthStatusChangeListener,
                                                                           RegisterType.UNREGISTER);
        //????????????????????????
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(doctorTransferPatientListener,
                                                                          RegisterType.UNREGISTER);
    }

    private OnTransferCallbackListener onTransferCallbackListener;

    public void setOnTransferCallbackListener(OnTransferCallbackListener onTransferCallbackListener) {
        this.onTransferCallbackListener = onTransferCallbackListener;
    }

    public interface OnTransferCallbackListener {
        void onTransferCallback();
    }
}
