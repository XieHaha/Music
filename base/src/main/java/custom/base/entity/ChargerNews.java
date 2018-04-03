package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-03-24.
 */
public class ChargerNews implements Serializable {

    private static final long serialVersionUID = 1226750724686419228L;

    private String createTime;//添加时间
    private String imageUrl;//资讯图片地址
    private String infoId;//资讯列表id
    private String intro;//简介
    private String remark;//备注
    private String title;//资讯的标题
    private String url;//地址
    private String validFlag;

    //private ArrayList<NewsLabel> info_label;//标签列表


    public String getValidFlag() {
        return validFlag;
    }

    public void setValidFlag(String validFlag) {
        this.validFlag = validFlag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public ArrayList<NewsLabel> getInfo_label() {
//        return info_label;
//    }
//
//    public void setInfo_label(ArrayList<NewsLabel> info_label) {
//        this.info_label = info_label;
//    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ChargerNews{" + "createTime='" + createTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", infoId='" + infoId + '\'' +
                ", intro='" + intro + '\'' +
                ", remark='" + remark + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", validFlag=" + validFlag +
                '}';
    }
}
