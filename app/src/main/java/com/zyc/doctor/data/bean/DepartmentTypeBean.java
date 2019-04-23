package com.zyc.doctor.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/4/23 15:58
 * @des 医生科室类型
 */
public class DepartmentTypeBean implements Serializable {
    private static final long serialVersionUID = 6542023090744918438L;
    private String value;
    private String label;
    private ArrayList<DepartmentTypeChildBean> children;

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

    public ArrayList<DepartmentTypeChildBean> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<DepartmentTypeChildBean> children) {
        this.children = children;
    }
}
