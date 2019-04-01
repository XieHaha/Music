//package com.zyc.doctor.ui.activity.xiaoyu;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.bigkoo.pickerview.builder.TimePickerBuilder;
//import com.bigkoo.pickerview.view.TimePickerView;
//import com.yanzhenjie.nohttp.NoHttp;
//import com.yanzhenjie.nohttp.RequestMethod;
//import com.yanzhenjie.nohttp.rest.OnResponseListener;
//import com.yanzhenjie.nohttp.rest.Request;
//import com.yanzhenjie.nohttp.rest.RequestQueue;
//import com.yanzhenjie.nohttp.rest.Response;
//import com.zyc.doctor.R;
//import com.zyc.doctor.ui.activity.xiaoyu.link.CallActivity;
//import com.zyc.doctor.ui.dialog.SimpleDialog;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import butterknife.BindView;
//import com.zyc.doctor.http.data.BaseResponse;
//import com.zyc.doctor.http.data.HttpConstants;
//import com.zyc.doctor.ui.base.activity.BaseActivity;
//import com.zyc.doctor.utils.ToastUtil;
//
///**
// * 创建会议
// *
// * @author DUNDUN
// */
//public class CreateMeetingActivity extends BaseActivity {
//    @BindView(R.id.act_create_theme)
//    EditText etTheme;
//    @BindView(R.id.act_create_time)
//    TextView tvTime;
//    @BindView(R.id.act_create_time_end)
//    TextView tvEndTime;
//    @BindView(R.id.act_create_number)
//    EditText etNumber;
//    @BindView(R.id.act_create_pwd)
//    EditText etPwd;
//    @BindView(R.id.act_create_meet_pwd)
//    EditText etMeetPwd;
//    @BindView(R.id.act_create_next)
//    TextView tvNext;
//
//    private String theme, number, pwd, meetPwd;
//    private long startTime, endTime;
//    /**
//     * 时间选择控件
//     */
//    private TimePickerView timePicker;
//    private boolean isModifyData;
//
//    @Override
//    public int getLayoutID() {
//        return R.layout.act_create_meeting;
//    }
//
//    @Override
//    protected boolean isInitBackBtn() {
//        return true;
//    }
//
//    @Override
//    public void initView(@NonNull Bundle savedInstanceState) {
//        super.initView(savedInstanceState);
//        ((TextView) findViewById(R.id.public_title_bar_title)).setText("创建会议");
//    }
//
//    @Override
//    public void initData(@NonNull Bundle savedInstanceState) {
//        super.initData(savedInstanceState);
//    }
//
//    @Override
//    public void initListener() {
//        tvTime.setOnClickListener(this);
//        tvEndTime.setOnClickListener(this);
//        tvNext.setOnClickListener(this);
//        backBtn.setOnClickListener(v -> finishPage());
//    }
//
//    /**
//     * 创建会议
//     */
//    private void createMeeting() {
//        RequestQueue queue = NoHttp.getRequestQueueInstance();
//        final Request<String> request =
//                NoHttp.createStringRequest(HttpConstants.BASE_BASIC_URL + "/xiaoyu/video/create",
//                        RequestMethod.POST);
//        Map<String, Object> params = new HashMap<>();
//        params.put("meetingCreatorId", loginSuccessBean.getDoctorId());
//        params.put("controlPassword", meetPwd);
//        params.put("meetingStartTime", startTime);
//        params.put("meetingEndTime", endTime);
//        params.put("meetingName", theme);
//        params.put("participant", number);
//        params.put("password", pwd);
//        JSONObject jsonObject = new JSONObject(params);
//        request.setDefineRequestBodyForJson(jsonObject.toString());
//        queue.add(1, request, new OnResponseListener<String>() {
//            @Override
//            public void onStart(int what) {
//                showProgressDialog("创建中...");
//            }
//
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//                String s = response.get();
//                try {
//                    JSONObject object = new JSONObject(s);
//                    BaseResponse baseResponse = praseBaseResponse(object, String.class);
//                    if (baseResponse != null) {
//                        switch (baseResponse.getCode()) {
//                            //重复创建
//                            case 104:
//                                ToastUtil.toast(CreateMeetingActivity.this, baseResponse.getMsg());
//                            case 200:
//                                Intent intent = new Intent(CreateMeetingActivity.this,
//                                        CallActivity.class);
//                                startActivity(intent);
//                                break;
//                            //小鱼sdk错误
//                            case 102:
//                                //参数错误
//                            case 101:
//                            default:
//                                ToastUtil.toast(CreateMeetingActivity.this, baseResponse.getMsg());
//                                break;
//                        }
//                    }
//                } catch (JSONException e) {
//                    ToastUtil.toast(CreateMeetingActivity.this, e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailed(int what, Response<String> response) {
//                ToastUtil.toast(CreateMeetingActivity.this, response.getException().getMessage());
//            }
//
//            @Override
//            public void onFinish(int what) {
//                closeProgressDialog();
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.act_create_time:
//                hideSoftInputFromWindow();
//                selectDate(1);
//                break;
//            case R.id.act_create_time_end:
//                hideSoftInputFromWindow();
//                selectDate(2);
//                break;
//            case R.id.act_create_next:
//                theme = etTheme.getText().toString();
//                number = etNumber.getText().toString();
//                pwd = etPwd.getText().toString();
//                meetPwd = etMeetPwd.getText().toString();
//                if (startTime == 0 || TextUtils.isEmpty(theme) || TextUtils.isEmpty(number) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(meetPwd)) {
//                    ToastUtil.toast(this, "请完善信息");
//                    return;
//                }
//                createMeeting();
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 时间选择器
//     */
//    private void selectDate(int type) {
//        Calendar selectedDate = Calendar.getInstance();
//        Calendar startDate = Calendar.getInstance();
//        Calendar endDate = Calendar.getInstance();
//        //正确设置方式 原因：注意事项有说明
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
//        String time = simpleDateFormat.format(new Date());
//        String[] strings = time.split("-");
//        startDate.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1,
//                Integer.parseInt(strings[2]), Integer.parseInt(strings[3]),
//                Integer.parseInt(strings[4]));
//        endDate.set(2099, 12, 31, 0, 0);
//        selectedDate.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1,
//                Integer.parseInt(strings[2]), Integer.parseInt(strings[3]),
//                Integer.parseInt(strings[4]));
//        timePicker = new TimePickerBuilder(this, (date, v) -> {
//            //选中事件回调
//            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//            switch (type) {
//                //开始时间
//                case 1:
//                    tvTime.setText(simpleDateFormat1.format(date));
//                    startTime = date.getTime();
//                    break;
//                //结束时间
//                case 2:
//                    endTime = date.getTime();
//                    if (endTime <= startTime) {
//                        ToastUtil.toast(CreateMeetingActivity.this, "结束时间必须大于开始时间");
//                        endTime = 0;
//                        return;
//                    }
//                    tvEndTime.setText(simpleDateFormat1.format(date));
//                    break;
//                default:
//                    break;
//            }
//            // 默认全部显示
//        }).setType(new boolean[]{true, true, true, true, true, false})
//                //取消按钮文字
//                .setCancelText("取消")
//                //确认按钮文字
//                .setSubmitText("确定")
//                //标题文字大小
//                .setTitleSize(20)
//                //标题文字
//                .setTitleText("")
//                //点击屏幕，点在控件外部范围时，是否取消显示
//                .setOutSideCancelable(true)
//                //是否循环滚动
//                .isCyclic(false)
//                //确定按钮文字颜色
//                .setSubmitColor(R.color.app_main_color)
//                //取消按钮文字颜色
//                .setCancelColor(R.color.app_main_color)
//                //标题背景颜色 Night mode
//                .setTitleBgColor(0xffffffff)
//                //滚轮背景颜色 Night mode
//                .setBgColor(0xffffffff)
//                // 如果不设置的话，默认是系统时间*/
//                .setDate(selectedDate)
//                //起始终止年月日设定
//                .setRangDate(startDate, endDate)
//                //默认设置为年月日时分秒
//                .setLabel("年", "月", "日", "时", "分", "秒")
//                //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isCenterLabel(true)
//                //是否显示为对话框样式
//                .isDialog(false).build();
//        timePicker.show();
//    }
//
//    /**
//     * 隐藏软键盘
//     */
//    private void hideSoftInputFromWindow() {
//        InputMethodManager inputmanger =
//                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputmanger.hideSoftInputFromWindow(etTheme.getWindowToken(), 0);
//    }
//
//    /**
//     * 判断是否提示用户保存
//     */
//    private void finishPage() {
//        if (isModifyData) {
//            new SimpleDialog(this, "编辑内容还未保存，确定退出?", (dialog, which) -> finish(),
//                    (dialog, which) -> dialog.dismiss()).show();
//        } else {
//            finish();
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            finishPage();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
