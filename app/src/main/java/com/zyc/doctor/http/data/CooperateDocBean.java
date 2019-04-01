package com.zyc.doctor.http.data;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 合作医生
 */
public class CooperateDocBean extends DataSupport implements Serializable
{
    private static final long serialVersionUID = -5825956356939935298L;
    private int fieldId;
    private String doctorId;
    private String phone;
    private String portraitUrl;
    private String name;
    private String hospital;
    private String title;
    private String department;
    private String doctorDescription;
    private int requestSource;
    private int checked;
    private int hospitalAuthorityCode;//0,未认证    1，已认证
    private String checkUrl;
    private String identityNumber;
    private String nickname;
    private int isFriends;//1为好友
    private int isAuthorityOpen;//0 关闭   1打开  默认为0；

    public int getFieldId()
    {
        return fieldId;
    }

    public void setFieldId(int fieldId)
    {
        this.fieldId = fieldId;
    }

    public String getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPortraitUrl()
    {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl)
    {
        this.portraitUrl = portraitUrl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getHospital()
    {
        return hospital;
    }

    public void setHospital(String hospital)
    {
        this.hospital = hospital;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    public String getDoctorDescription()
    {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription)
    {
        this.doctorDescription = doctorDescription;
    }

    public int getRequestSource()
    {
        return requestSource;
    }

    public void setRequestSource(int requestSource)
    {
        this.requestSource = requestSource;
    }

    public int getChecked()
    {
        return checked;
    }

    public void setChecked(int checked)
    {
        this.checked = checked;
    }

    public String getCheckUrl()
    {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl)
    {
        this.checkUrl = checkUrl;
    }

    public String getIdentityNumber()
    {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber)
    {
        this.identityNumber = identityNumber;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public int getIsFriends()
    {
        return isFriends;
    }

    public void setIsFriends(int isFriends)
    {
        this.isFriends = isFriends;
    }

    public int getIsAuthorityOpen()
    {
        return isAuthorityOpen;
    }

    public void setIsAuthorityOpen(int isAuthorityOpen)
    {
        this.isAuthorityOpen = isAuthorityOpen;
    }

    public int getHospitalAuthorityCode()
    {
        return hospitalAuthorityCode;
    }

    public void setHospitalAuthorityCode(int hospitalAuthorityCode)
    {
        this.hospitalAuthorityCode = hospitalAuthorityCode;
    }
}
