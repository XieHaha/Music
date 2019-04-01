package com.zyc.doctor.http.data;

import java.io.Serializable;

/**
 * Created by dundun on 18/4/19.
 */
public class CombineChildBean implements Serializable
{
    private static final long serialVersionUID = 3811432371020171544L;
    private int id;
    private long gmtCreate;
    private long gmtModified;
    private String patientId;
    private String allergyInfo;
    private String diagnosisInfo;
    private String surgeryName;
    private long surgeryDate;
    private String type;
    private boolean tempDate;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getAllergyInfo()
    {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo)
    {
        this.allergyInfo = allergyInfo;
    }

    public String getDiagnosisInfo()
    {
        return diagnosisInfo;
    }

    public void setDiagnosisInfo(String diagnosisInfo)
    {
        this.diagnosisInfo = diagnosisInfo;
    }

    public String getSurgeryName()
    {
        return surgeryName;
    }

    public void setSurgeryName(String surgeryName)
    {
        this.surgeryName = surgeryName;
    }

    public long getSurgeryDate()
    {
        return surgeryDate;
    }

    public void setSurgeryDate(long surgeryDate)
    {
        this.surgeryDate = surgeryDate;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isTempDate()
    {
        return tempDate;
    }

    public void setTempDate(boolean tempDate)
    {
        this.tempDate = tempDate;
    }
}
