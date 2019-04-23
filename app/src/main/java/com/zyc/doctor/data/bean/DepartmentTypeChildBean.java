package com.zyc.doctor.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/4/23 16:00
 * @des
 */
public class DepartmentTypeChildBean implements Serializable {
    private static final long serialVersionUID = 1383143907335602855L;
    private String value;
    private String label;
    private String fatherDepartmentTypeName;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFatherDepartmentTypeName() {
        return fatherDepartmentTypeName;
    }

    public void setFatherDepartmentTypeName(String fatherDepartmentTypeName) {
        this.fatherDepartmentTypeName = fatherDepartmentTypeName;
    }
}
