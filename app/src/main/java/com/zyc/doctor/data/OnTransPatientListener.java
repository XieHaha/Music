package com.zyc.doctor.data;

public interface OnTransPatientListener
{
    void onPositiveTrigger(String fromDoctorId, String patientId, int requestCode);

    void onNegativeTrigger(String fromDoctorId, String patientId, int requestCode);
}
