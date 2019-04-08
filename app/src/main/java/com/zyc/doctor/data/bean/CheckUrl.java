package com.zyc.doctor.data.bean;

import java.io.Serializable;

/**
 * @author dundun
 * @date 18/5/17
 */
public class CheckUrl implements Serializable {
    private static final long serialVersionUID = -2913848993219022550L;
    private String idFront;
    private String idEnd;
    private String qualifiedFront;
    private String qualifiedEnd;

    public String getIdFront() {
        return idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public String getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(String idEnd) {
        this.idEnd = idEnd;
    }

    public String getQualifiedFront() {
        return qualifiedFront;
    }

    public void setQualifiedFront(String qualifiedFront) {
        this.qualifiedFront = qualifiedFront;
    }

    public String getQualifiedEnd() {
        return qualifiedEnd;
    }

    public void setQualifiedEnd(String qualifiedEnd) {
        this.qualifiedEnd = qualifiedEnd;
    }
}
