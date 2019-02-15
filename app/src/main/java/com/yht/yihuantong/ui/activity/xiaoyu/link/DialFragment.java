package com.yht.yihuantong.ui.activity.xiaoyu.link;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ainemo.sdk.otf.NemoSDK;
import com.yht.xylink.utils.BackHandledFragment;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.AllUtils;

import custom.frame.bean.MeetingBean;

/**
 * 拨号界面
 */
public class DialFragment extends BackHandledFragment
{
    private static final String TAG = "DialFragment";
    private String myNumber;
    private String mCallNumber;
    private TextView tvMeetTitle, tvTime, tvNext, tvTitle;
    private EditText etRoomNumber;
    private LinearLayout llTitleLayout;
    private CallNumberInterface callBack;
    private boolean hadIntercept;
    private String mDisplayName;
    private boolean isAdd;
    private MeetingBean meetingBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        tvTitle = (TextView)view.findViewById(R.id.public_title_bar_title);
        tvMeetTitle = (TextView)view.findViewById(R.id.act_meet_detail_title);
        tvTime = (TextView)view.findViewById(R.id.act_meet_detail_time);
        llTitleLayout = (LinearLayout)view.findViewById(R.id.act_meet_detail_title_layout);
        if (isAdd)
        {
            tvTitle.setText("加入会议");
            llTitleLayout.setVisibility(View.GONE);
        }
        else
        {
            tvTitle.setText("会议详情");
            llTitleLayout.setVisibility(View.VISIBLE);
        }
        etRoomNumber = (EditText)view.findViewById(R.id.act_meet_detail_room_number);
        final EditText password = (EditText)view.findViewById(R.id.act_meet_detail_room_pwd);
        initpageData();
        view.findViewById(R.id.act_meet_detail_next).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (etRoomNumber.getText().toString().trim().length() == 0)
                {
                    Toast.makeText(getActivity(), "请输入呼叫号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkPermission();
                mCallNumber = etRoomNumber.getText().toString();
                callBack.getResult(mCallNumber);
                callBack.getDisplayName(mDisplayName);
                NemoSDK.getInstance().makeCall(mCallNumber, password.getText().toString());
                NemoSDK.getInstance().getRecordingUri(mCallNumber);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void initpageData()
    {
        if (meetingBean != null)
        {
            tvMeetTitle.setText(meetingBean.getMeetingRoomName());
            tvTime.setText(AllUtils.formatDate(meetingBean.getMeetingStartTime(),
                                               AllUtils.YYYY_MM_DD_HH_MM) + "-" +
                           AllUtils.formatDate(meetingBean.getMeetingEndTime(),
                                               AllUtils.YYYY_MM_DD_HH_MM));
            etRoomNumber.setText(meetingBean.getMeetingRoomNumber());
        }
    }

    public void setMyNumber(String myNumber)
    {
        this.myNumber = myNumber;
    }

    public void setDisplayName(String displayName)
    {
        this.mDisplayName = displayName;
    }

    public void setAdd(boolean add)
    {
        isAdd = add;
    }

    public void setMeetingBean(MeetingBean meetingBean)
    {
        this.meetingBean = meetingBean;
    }

    private void checkPermission()
    {
        if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
              PackageManager.PERMISSION_GRANTED) &&
            !(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) ==
              PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(), new String[] {
                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO }, 0);
        }
        else if (!(ContextCompat.checkSelfPermission(getActivity(),
                                                     Manifest.permission.RECORD_AUDIO) ==
                   PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(),
                                              new String[] { Manifest.permission.RECORD_AUDIO }, 0);
        }
        else if (!(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                   PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(getActivity(),
                                              new String[] { Manifest.permission.CAMERA }, 0);
        }
    }

    @Override
    public boolean onBackPressed()
    {
        if (hadIntercept)
        {
            return false;
        }
        else
        {
            hadIntercept = true;
            return true;
        }
    }

    /*接口*/
    public interface CallNumberInterface
    {
        /*定义一个获取信息的方法*/
        public void getResult(String callNumber);

        public void getDisplayName(String displayName);
    }

    /*设置监听器*/
    public void setCallBack(CallNumberInterface callBack)
    {
    /*获取文本框的信息,当然你也可以传其他类型的参数,看需求咯*/
        this.callBack = callBack;
    }
}
