package custom.frame.bean;

import java.io.Serializable;

/**
 * 患者基本病例信息
 * @author DUNDUN
 */
public class PatientCaseBasicBean implements Serializable {
    private static final long serialVersionUID = -1134905694309937454L;

    private String patientId;
    //过敏信息
    private String allergyInfo;
    //诊断信息
    private String diagnosisInfo;
    //手术信息
    private String surgeryInfo;
    private long gmtCreate;
    private long gmtModified;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public String getDiagnosisInfo() {
        return diagnosisInfo;
    }

    public void setDiagnosisInfo(String diagnosisInfo) {
        this.diagnosisInfo = diagnosisInfo;
    }

    public String getSurgeryInfo() {
        return surgeryInfo;
    }

    public void setSurgeryInfo(String surgeryInfo) {
        this.surgeryInfo = surgeryInfo;
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
}
