package com.zyc.doctor.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/4/24 15:41
 * @des 远程会诊登录
 */
public class CameraLoginBean implements Serializable {
    private static final long serialVersionUID = -3828143199532110082L;
    private String hospitalName;
    private String departmentTypeName;
    private String pageUnicode;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentTypeName() {
        return departmentTypeName;
    }

    public void setDepartmentTypeName(String departmentTypeName) {
        this.departmentTypeName = departmentTypeName;
    }

    public String getPageUnicode() {
        return pageUnicode;
    }

    public void setPageUnicode(String pageUnicode) {
        this.pageUnicode = pageUnicode;
    }
}
