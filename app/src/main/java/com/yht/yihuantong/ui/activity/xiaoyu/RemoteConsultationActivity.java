package com.yht.yihuantong.ui.activity.xiaoyu;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ainemo.sdk.otf.ConnectNemoCallback;
import com.ainemo.sdk.otf.LoginResponseData;
import com.ainemo.sdk.otf.NemoSDK;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.yihuantong.R;
import com.yht.yihuantong.data.CommonData;
import com.yht.yihuantong.ui.activity.xiaoyu.link.CallActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import custom.frame.bean.BaseResponse;
import custom.frame.bean.MeetingBean;
import custom.frame.http.data.HttpConstants;
import custom.frame.ui.activity.BaseActivity;
import custom.frame.utils.ToastUtil;

/**
 * Created by dundun on 18/9/2.
 */
public class RemoteConsultationActivity extends BaseActivity {
    private NemoSDK nemoSDK;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_remote_consultation;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ((TextView) findViewById(R.id.public_title_bar_title)).setText("远程会诊");
        findViewById(R.id.act_remote_consultation_create_layout).setOnClickListener(this);
        findViewById(R.id.act_remote_consultation_add_layout).setOnClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        nemoSDK = NemoSDK.getInstance();
        checkPermission();
        loginExternalAccount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_remote_consultation_create_layout:
                findVideoInfo();
                break;
            case R.id.act_remote_consultation_add_layout:
                Intent intent = new Intent(RemoteConsultationActivity.this, CallActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 查询会议是否存在
     */
    private void findVideoInfo() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request =
                NoHttp.createStringRequest(HttpConstants.BASE_BASIC_URL + "/xiaoyu/video/info",
                        RequestMethod.POST);
        Map<String, Object> params = new HashMap<>();
        params.put("meetingCreatorId", loginSuccessBean.getDoctorId());
        JSONObject jsonObject = new JSONObject(params);
        request.setDefineRequestBodyForJson(jsonObject.toString());
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                showProgressDialog("查询中...");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                try {
                    JSONObject object = new JSONObject(s);
                    BaseResponse baseResponse = praseBaseResponse(object, MeetingBean.class);
                    if (baseResponse != null) {
                        Intent intent;
                        MeetingBean bean = baseResponse.getData();
                        if (TextUtils.isEmpty(bean.getCreatorId())) {
                            intent = new Intent(RemoteConsultationActivity.this,
                                    CreateMeetingActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(RemoteConsultationActivity.this,
                                    CallActivity.class);
                            intent.putExtra(CommonData.KEY_MEETING_BEAN, bean);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.toast(RemoteConsultationActivity.this, e.getMessage());
                }
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                ToastUtil.toast(RemoteConsultationActivity.this,
                        response.getException().getMessage());
            }

            @Override
            public void onFinish(int what) {
                closeProgressDialog();
            }
        });
    }

    /**
     * 小鱼匿名登录
     */
    private void loginExternalAccount() {
        try {
            nemoSDK.loginExternalAccount("111", loginSuccessBean.getDoctorId(),
                    new ConnectNemoCallback() {
                @Override
                public void onFailed(final int errorCode) {
                    Log.e("test", "匿名登录失败，错误码：" + errorCode);
                }

                @Override
                public void onSuccess(LoginResponseData data, boolean isDetectingNetworkTopology) {
                    Log.e("test", "匿名登录成功，号码为：" + data);
                    ToastUtil.toast(RemoteConsultationActivity.this, "登陆成功");
                }

                @Override
                public void onNetworkTopologyDetectionFinished(LoginResponseData resp) {
                    Log.e("test", "net detect onNetworkTopologyDetectionFinished 1");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && !(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else if (!(ContextCompat.checkSelfPermission(this,
                Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 0);
        }
    }
}
