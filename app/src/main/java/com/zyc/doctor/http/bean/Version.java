package com.zyc.doctor.http.bean;

import java.io.Serializable;

/**
 * 版本model
 */
public class Version implements Serializable
{
    private static final long serialVersionUID = -8438526479985188981L;

    private String newVersion;
    private String minVersion;
    private String downloadUrl;
    private String updateDescription;
    private String deviceSystem;
    private int forceUpdate;
    private long publicTime;

    public String getNewVersion()
    {
        return newVersion;
    }

    public void setNewVersion(String newVersion)
    {
        this.newVersion = newVersion;
    }

    public String getMinVersion()
    {
        return minVersion;
    }

    public void setMinVersion(String minVersion)
    {
        this.minVersion = minVersion;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl)
    {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateDescription()
    {
        return updateDescription;
    }

    public void setUpdateDescription(String updateDescription)
    {
        this.updateDescription = updateDescription;
    }

    public String getDeviceSystem()
    {
        return deviceSystem;
    }

    public void setDeviceSystem(String deviceSystem)
    {
        this.deviceSystem = deviceSystem;
    }

    public int getForceUpdate()
    {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate)
    {
        this.forceUpdate = forceUpdate;
    }

    public long getPublicTime()
    {
        return publicTime;
    }

    public void setPublicTime(long publicTime)
    {
        this.publicTime = publicTime;
    }
}
