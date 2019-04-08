package com.zyc.doctor.data.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author dundun
 * @date 18/8/14
 */
public class RegistrationTypeBean extends DataSupport implements Serializable {
    private static final long serialVersionUID = 6372508858667359237L;
    private int fieldId;
    private long gmtCreate;
    private long gmtModified;
    private String productTypeName;

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
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

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}
