package com.yht.yihuantong.data;

public interface OnEventTriggerListener {
    void onPositiveTrigger(String s,int requestCode);
    void onNegativeTrigger(String s,int requestCode);
}
