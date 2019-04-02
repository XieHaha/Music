package com.zyc.doctor.data;

/**
 * @author dundun
 */
public interface OnEventTriggerListener {
    void onPositiveTrigger(String s,int requestCode);
    void onNegativeTrigger(String s,int requestCode);
}
