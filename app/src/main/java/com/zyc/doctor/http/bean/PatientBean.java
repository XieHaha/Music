package com.zyc.doctor.http.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 患者
 */
public class PatientBean extends DataSupport implements Serializable {
    private static final long serialVersionUID = -8620697034094662215L;

    private int focusStatus;
    private String patientId;
    private String sex;
    private String name;
    private int age;
    private String patientImgUrl;
    private String phone;
    private String birth;
    private String identityNum;
    private String disease;
    private String past_disease;
    private String allergy;
    private String im_username;
    private String address;
    private String nickname;
    private String unitName;
    private long date;
    private long birthDate;
    private long gmtCreate;
    private long gmtModified;
    private int requestSource;

    public int getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(int requestSource) {
        this.requestSource = requestSource;
    }

    public String getPatientImgUrl() {
        return patientImgUrl;
    }

    public void setPatientImgUrl(String patientImgUrl) {
        this.patientImgUrl = patientImgUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPast_disease() {
        return past_disease;
    }

    public void setPast_disease(String past_disease) {
        this.past_disease = past_disease;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getIm_username() {
        return im_username;
    }

    public void setIm_username(String im_username) {
        this.im_username = im_username;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getUnitName()
    {
        return unitName;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified) {
        this.gmtModified = gmtModified;
    }

    public long getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(long birthDate)
    {
        this.birthDate = birthDate;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }
}
