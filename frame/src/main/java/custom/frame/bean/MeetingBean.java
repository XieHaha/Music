package custom.frame.bean;

import java.io.Serializable;

/**
 * Created by dundun on 19/1/29.
 */
public class MeetingBean implements Serializable
{
    private static final long serialVersionUID = -3130120450821161028L;
    private String creatorId;
    private String password;
    private String controlPassword;
    private String meetingRoomNumber;
    private String meetingRoomName;
    private long meetingStartTime;
    private long meetingEndTime;

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getControlPassword()
    {
        return controlPassword;
    }

    public void setControlPassword(String controlPassword)
    {
        this.controlPassword = controlPassword;
    }

    public String getMeetingRoomNumber()
    {
        return meetingRoomNumber;
    }

    public void setMeetingRoomNumber(String meetingRoomNumber)
    {
        this.meetingRoomNumber = meetingRoomNumber;
    }

    public String getCreatorId()
    {
        return creatorId;
    }

    public void setCreatorId(String creatorId)
    {
        this.creatorId = creatorId;
    }

    public String getMeetingRoomName()
    {
        return meetingRoomName;
    }

    public void setMeetingRoomName(String meetingRoomName)
    {
        this.meetingRoomName = meetingRoomName;
    }

    public long getMeetingStartTime()
    {
        return meetingStartTime;
    }

    public void setMeetingStartTime(long meetingStartTime)
    {
        this.meetingStartTime = meetingStartTime;
    }

    public long getMeetingEndTime()
    {
        return meetingEndTime;
    }

    public void setMeetingEndTime(long meetingEndTime)
    {
        this.meetingEndTime = meetingEndTime;
    }
}
