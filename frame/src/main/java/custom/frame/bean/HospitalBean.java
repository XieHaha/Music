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
}
