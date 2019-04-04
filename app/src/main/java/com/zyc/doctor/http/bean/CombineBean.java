package com.zyc.doctor.http.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author dundun
 * 综合病史
 */
public class CombineBean implements Serializable
{
    private static final long serialVersionUID = -2579063235461831828L;

    private long gmtCreate;
    private long gmtModified;
    private String patientId;

    /**
     * 过敏
     */
    private ArrayList<CombineChildBean> allergyInfo;
    /**
     * 诊断
     */
    private ArrayList<CombineChildBean> diagnosisInfo;
    /**
     * 手术
     */
    private ArrayList<CombineChildBean> surgeryInfo;

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public long getGmtCreate()
    {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified()
    {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified)
    {
        this.gmtModified = gmtModified;
    }

    public ArrayList<CombineChildBean> getAllergyInfo()
    {
        return allergyInfo;
    }

    public void setAllergyInfo(ArrayList<CombineChildBean> allergyInfo)
    {
        this.allergyInfo = allergyInfo;
    }

    public ArrayList<CombineChildBean> getDiagnosisInfo()
    {
        return diagnosisInfo;
    }

    public void setDiagnosisInfo(ArrayList<CombineChildBean> diagnosisInfo)
    {
        this.diagnosisInfo = diagnosisInfo;
    }

    public ArrayList<CombineChildBean> getSurgeryInfo()
    {
        return surgeryInfo;
    }

    public void setSurgeryInfo(ArrayList<CombineChildBean> surgeryInfo)
    {
        this.surgeryInfo = surgeryInfo;
    }
}
