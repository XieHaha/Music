package com.zyc.doctor.http.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dundun on 18/8/15.
 * 商品类型
 */
public class HospitalProductTypeBean implements Serializable
{
    private static final long serialVersionUID = 8253586452889907001L;
    private String productTypeId;
    private String productTypeName;
    private ArrayList<HospitalProductBean> productInfoByHospitalIdResList;

    public String getProductTypeId()
    {
        return productTypeId;
    }

    public void setProductTypeId(String productTypeId)
    {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName()
    {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName)
    {
        this.productTypeName = productTypeName;
    }

    public ArrayList<HospitalProductBean> getProductInfoByHospitalIdResList()
    {
        return productInfoByHospitalIdResList;
    }

    public void setProductInfoByHospitalIdResList(
            ArrayList<HospitalProductBean> productInfoByHospitalIdResList)
    {
        this.productInfoByHospitalIdResList = productInfoByHospitalIdResList;
    }
}
