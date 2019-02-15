//package com.yht.yihuantong.ui.activity.xiaoyu;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.ainemo.sdk.NemoSDK;
//import com.ainemo.sdk.callback.MakeCallMeetingCallback;
//import com.ainemo.sdk.model.Meeting;
//import com.ainemo.sdk.model.Result;
//import com.ainemo.sdk.model.User;
//import com.yht.yihuantong.R;
//import com.yht.yihuantong.data.CommonData;
//import com.yht.yihuantong.utils.AllUtils;
//
//import custom.frame.bean.MeetingBean;
//import custom.frame.ui.activity.BaseActivity;
//import custom.frame.utils.ToastUtil;
//
///**
// * 创建会议
// *
// * @author DUNDUN
// */
//public class MeetingDetailActivity extends BaseActivity
//{
//    private EditText etRoomNumber, etRoomPwd;
//    private TextView tvMeetTitle, tvTime, tvNext, tvTitle;
//    private LinearLayout llTitleLayout;
//    private String roomNumber, roomPwd;
//    private boolean isAdd;
//    private MeetingBean meetingBean;
//    private String callNumber;
//
//    @Override
//    public int getLayoutID()
//    {
//        return R.layout.act_meeting_detail;
//    }
//
//    @Override
//    protected boolean isInitBackBtn()
//    {
//        return true;
//    }
//
//    @Override
//    public void initView(@NonNull Bundle savedInstanceState)
//    {
//        super.initView(savedInstanceState);
//        tvTitle = (TextView)findViewById(R.id.public_title_bar_title);
//        tvMeetTitle = (TextView)findViewById(R.id.act_meet_detail_title);
//        tvTime = (TextView)findViewById(R.id.act_meet_detail_time);
//        tvNext = (TextView)findViewById(R.id.act_meet_detail_next);
//        llTitleLayout = (LinearLayout)findViewById(R.id.act_meet_detail_title_layout);
//        etRoomNumber = (EditText)findViewById(R.id.act_meet_detail_room_number);
//        etRoomPwd = (EditText)findViewById(R.id.act_meet_detail_room_pwd);
//    }
//
//    @Override
//    public void initData(@NonNull Bundle savedInstanceState)
//    {
//        super.initData(savedInstanceState);
//        if (getIntent() != null)
//        {
//            isAdd = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
//            meetingBean = (MeetingBean)getIntent().getSerializableExtra(
//                    CommonData.KEY_MEETING_BEAN);
//        }
//        if (isAdd)
//        {
//            tvTitle.setText("加入会议");
//            llTitleLayout.setVisibility(View.GONE);
//        }
//        else
//        {
//            tvTitle.setText("会议详情");
//            llTitleLayout.setVisibility(View.VISIBLE);
//        }
//        initpageData();
//    }
//
//    private void initpageData()
//    {
//        if (meetingBean != null)
//        {
//            tvMeetTitle.setText(meetingBean.getMeetingRoomName());
//            tvTime.setText(AllUtils.formatDate(meetingBean.getMeetingStartTime(),
//                                               AllUtils.YYYY_MM_DD_HH_MM) + "——" +
//                           AllUtils.formatDate(meetingBean.getMeetingEndTime(),
//                                               AllUtils.YYYY_MM_DD_HH_MM));
//            etRoomNumber.setText(meetingBean.getMeetingRoomNumber());
//        }
//    }
//
//    @Override
//    public void initListener()
//    {
//        tvNext.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            case R.id.act_meet_detail_next:
//                roomNumber = etRoomNumber.getText().toString().trim();
//                roomPwd = etRoomPwd.getText().toString().trim();
//                if (TextUtils.isEmpty(roomNumber) || TextUtils.isEmpty(roomPwd))
//                {
//                    ToastUtil.toast(this, "房间号或密码不能为空");
//                    return;
//                }
//                joinMeeting();
//                break;
//        }
//    }
//
//    /**
//     * 加入会议
//     */
//    private void joinMeeting()
//    {
//        User me = new User();
//        me.setDisplayName(loginSuccessBean.getName());
//        me.setExternalUserId(loginSuccessBean.getDoctorId());
//        Meeting meeting = new Meeting(roomNumber, roomPwd);
//        // 如果不需要回调，callback设置null即可
//        NemoSDK.getInstance().makeCallMeeting(meeting, me, new MakeCallMeetingCallback()
//        {
//            @Override
//            public void onDone(Meeting meeting, Result result)
//            {
//                Log.i("test",
//                      "makeCallMeeting onDone, meeting: " + meeting + " , result: " + result);
//            }
//        });
//    }
//}
