package com.zyc.doctor.http.bean;

import java.io.Serializable;

/**
 * Created by dundun on 18/8/24.
 */
public class FileBean implements Serializable
{
    private static final long serialVersionUID = 8287011146509281223L;
    private String fileName;
    private String fileNameNoSuffix;
    private String fileType;
    private String fileUrl;

    public String getFileNameNoSuffix()
    {
        return fileNameNoSuffix;
    }

    public void setFileNameNoSuffix(String fileNameNoSuffix)
    {
        this.fileNameNoSuffix = fileNameNoSuffix;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileUrl()
    {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }
}
