package com.zyc.doctor.data.bean;

import java.io.Serializable;

/**
 * @author dundun
 * @date 18/7/19
 */
public class TransPatientBean implements Serializable {
    private static final long serialVersionUID = -4230007039183752036L;
    private int transferId;
    private int acceptState;
    private long transferDate;
    private String fromDoctorId;
    private String fromDoctorName;
    private String fromDoctorHospitalName;
    private String fromDoctorTitle;
    private String fromDoctorDepartment;
    private String fromDoctorImage;
    private String toDoctorId;
    private String toDoctorName;
    private String toDoctorHospitalName;
    private String toDoctorTitle;
    private String toDoctorDepartment;
    private String toDoctorImage;
    private String patientId;
    private String patientName;
    private String patientSex;
    private long patientBirthDate;
    private String patientImage;
    private String patientIdentityNumber;
    private String fromDoctorDiagnosisInfo;
    private int hospitalId;
    private String hospitalName;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(int acceptState) {
        this.acceptState = acceptState;
    }

    public long getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(long transferDate) {
        this.transferDate = transferDate;
    }

    public String getFromDoctorId() {
        return fromDoctorId;
    }

    public void setFromDoctorId(String fromDoctorId) {
        this.fromDoctorId = fromDoctorId;
    }

    public String getFromDoctorName() {
        return fromDoctorName;
    }

    public void setFromDoctorName(String fromDoctorName) {
        this.fromDoctorName = fromDoctorName;
    }

    public String getFromDoctorHospitalName() {
        return fromDoctorHospitalName;
    }

    public void setFromDoctorHospitalName(String fromDoctorHospitalName) {
        this.fromDoctorHospitalName = fromDoctorHospitalName;
    }

    public String getFromDoctorTitle() {
        return fromDoctorTitle;
    }

    public void setFromDoctorTitle(String fromDoctorTitle) {
        this.fromDoctorTitle = fromDoctorTitle;
    }

    public String getFromDoctorDepartment() {
        return fromDoctorDepartment;
    }

    public void setFromDoctorDepartment(String fromDoctorDepartment) {
        this.fromDoctorDepartment = fromDoctorDepartment;
    }

    public String getFromDoctorImage() {
        return fromDoctorImage;
    }

    public void setFromDoctorImage(String fromDoctorImage) {
        this.fromDoctorImage = fromDoctorImage;
    }

    public String getToDoctorId() {
        return toDoctorId;
    }

    public void setToDoctorId(String toDoctorId) {
        this.toDoctorId = toDoctorId;
    }

    public String getToDoctorName() {
        return toDoctorName;
    }

    public void setToDoctorName(String toDoctorName) {
        this.toDoctorName = toDoctorName;
    }

    public String getToDoctorHospitalName() {
        return toDoctorHospitalName;
    }

    public void setToDoctorHospitalName(String toDoctorHospitalName) {
        this.toDoctorHospitalName = toDoctorHospitalName;
    }

    public String getToDoctorTitle() {
        return toDoctorTitle;
    }

    public void setToDoctorTitle(String toDoctorTitle) {
        this.toDoctorTitle = toDoctorTitle;
    }

    public String getToDoctorDepartment() {
        return toDoctorDepartment;
    }

    public void setToDoctorDepartment(String toDoctorDepartment) {
        this.toDoctorDepartment = toDoctorDepartment;
    }

    public String getToDoctorImage() {
        return toDoctorImage;
    }

    public void setToDoctorImage(String toDoctorImage) {
        this.toDoctorImage = toDoctorImage;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public long getPatientBirthDate() {
        return patientBirthDate;
    }

    public void setPatientBirthDate(long patientBirthDate) {
        this.patientBirthDate = patientBirthDate;
    }

    public String getPatientImage() {
        return patientImage;
    }

    public void setPatientImage(String patientImage) {
        this.patientImage = patientImage;
    }

    public String getPatientIdentityNumber() {
        return patientIdentityNumber;
    }

    public void setPatientIdentityNumber(String patientIdentityNumber) {
        this.patientIdentityNumber = patientIdentityNumber;
    }

    public String getFromDoctorDiagnosisInfo() {
        return fromDoctorDiagnosisInfo;
    }

    public void setFromDoctorDiagnosisInfo(String fromDoctorDiagnosisInfo) {
        this.fromDoctorDiagnosisInfo = fromDoctorDiagnosisInfo;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
}
