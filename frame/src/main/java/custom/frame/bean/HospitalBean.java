package custom.frame.bean;

import java.io.Serializable;

/**
 * Created by dundun on 18/8/15.
 */
public class HospitalBean implements Serializable
{
    private static final long serialVersionUID = 6102212747958144450L;
    private String hospitalName;
    private String hospitalId;
    private String cityName;
    private String hospitalLevel;
    private String hospitalLogo;

    public String getHospitalName()
    {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName)
    {
        this.hospitalName = hospitalName;
    }

    public String getHospitalId()
    {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId)
    {
        this.hospitalId = hospitalId;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getHospitalLevel()
    {
        return hospitalLevel;
    }

    public void setHospitalLevel(String hospitalLevel)
    {
        this.hospitalLevel = hospitalLevel;
    }

    public String getHospitalLogo()
    {
        return hospitalLogo;
    }

    public void setHospitalLogo(String hospitalLogo)
    {
        this.hospitalLogo = hospitalLogo;
    }
}
