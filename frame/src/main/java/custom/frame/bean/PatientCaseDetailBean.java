package custom.frame.bean;

import java.io.Serializable;

/**
 * 患者病例详情
 * @author DUNDUN
 */
public class PatientCaseDetailBean implements Serializable {
    private static final long serialVersionUID = -4790053578533223621L;

    private int id;
    private long gmtCreate;
    private long gmtModified;
    private long visDate;
    private String patientId;
    private String diagnosisInfo;
    private String doctorDep;
    private String hospital;
    private String patientWords;
    private String currentInfo;
    private String importantHistory;
    private String checkReport;
    private String treat;
    private String reportImgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getVisDate() {
        return visDate;
    }

    public void setVisDate(long visDate) {
        this.visDate = visDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDiagnosisInfo() {
        return diagnosisInfo;
    }

    public void setDiagnosisInfo(String diagnosisInfo) {
        this.diagnosisInfo = diagnosisInfo;
    }

    public String getDoctorDep() {
        return doctorDep;
    }

    public void setDoctorDep(String doctorDep) {
        this.doctorDep = doctorDep;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPatientWords() {
        return patientWords;
    }

    public void setPatientWords(String patientWords) {
        this.patientWords = patientWords;
    }

    public String getCurrentInfo() {
        return currentInfo;
    }

    public void setCurrentInfo(String currentInfo) {
        this.currentInfo = currentInfo;
    }

    public String getImportantHistory() {
        return importantHistory;
    }

    public void setImportantHistory(String importantHistory) {
        this.importantHistory = importantHistory;
    }

    public String getCheckReport() {
        return checkReport;
    }

    public void setCheckReport(String checkReport) {
        this.checkReport = checkReport;
    }

    public String getTreat() {
        return treat;
    }

    public void setTreat(String treat) {
        this.treat = treat;
    }

    public String getReportImgUrl() {
        return reportImgUrl;
    }

    public void setReportImgUrl(String reportImgUrl) {
        this.reportImgUrl = reportImgUrl;
    }
}
