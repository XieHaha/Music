package com.zyc.doctor.data.bean;

import java.io.Serializable;

/**
 *
 * @author dundun
 * @date 18/10/19
 * 合作医院
 */
public class CooperateHospitalBean implements Serializable
{
    private static final long serialVersionUID = 3307408864297194730L;
    private String hospitalId;
    private String hospitalName;
    private String provinceName;
    private String cityName;
    private String hospitalLevel;
    private String userName;
    private String password;
    private String phone;
    private String address;
    private String hospitalDescription;
    private String image;
    private int relationshipId;//1，执业医院   2，合作医院

    public String getHospitalId()
    {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId)
    {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName()
    {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName)
    {
        this.hospitalName = hospitalName;
    }

    public String getProvinceName()
    {
        return provinceName;
    }

    public void setProvinceName(String provinceName)
    {
        this.provinceName = provinceName;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getHospitalLevel()
    {
        return hospitalLevel;
    }

    public void setHospitalLevel(String hospitalLevel)
    {
        this.hospitalLevel = hospitalLevel;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getHospitalDescription()
    {
        return hospitalDescription;
    }

    public void setHospitalDescription(String hospitalDescription)
    {
        this.hospitalDescription = hospitalDescription;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public int getRelationshipId()
    {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId)
    {
        this.relationshipId = relationshipId;
    }
}
