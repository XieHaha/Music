package com.zyc.doctor.http.data;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by dundun on 18/8/15.
 */
public class HospitalProductBean extends DataSupport implements Serializable
{
    private static final long serialVersionUID = 144695042019835544L;

    private String productId;
    private String productTypeId;
    private String productTypeName;
    private String productName;
    private String productPrice;
    private String productPriceUnit;
    private String productDescription;
    private String productContactName;
    private String productContactPhone;
    private String productAccessChannel;
    private String productOtherInfo;
    private long gmtCreate;
    private long gmtModified;

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

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

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductPrice()
    {
        return productPrice;
    }

    public void setProductPrice(String productPrice)
    {
        this.productPrice = productPrice;
    }

    public String getProductPriceUnit()
    {
        return productPriceUnit;
    }

    public void setProductPriceUnit(String productPriceUnit)
    {
        this.productPriceUnit = productPriceUnit;
    }

    public String getProductDescription()
    {
        return productDescription;
    }

    public void setProductDescription(String productDescription)
    {
        this.productDescription = productDescription;
    }

    public String getProductContactName()
    {
        return productContactName;
    }

    public void setProductContactName(String productContactName)
    {
        this.productContactName = productContactName;
    }

    public String getProductContactPhone()
    {
        return productContactPhone;
    }

    public void setProductContactPhone(String productContactPhone)
    {
        this.productContactPhone = productContactPhone;
    }

    public String getProductAccessChannel()
    {
        return productAccessChannel;
    }

    public void setProductAccessChannel(String productAccessChannel)
    {
        this.productAccessChannel = productAccessChannel;
    }

    public String getProductOtherInfo()
    {
        return productOtherInfo;
    }

    public void setProductOtherInfo(String productOtherInfo)
    {
        this.productOtherInfo = productOtherInfo;
    }

    public long getGmtCreate()
    {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate)
    {
        this.gmtCreate = gmtCreate;
    }

    public long getGmtModified()
    {
        return gmtModified;
    }

    public void setGmtModified(long gmtModified)
    {
        this.gmtModified = gmtModified;
    }
}
