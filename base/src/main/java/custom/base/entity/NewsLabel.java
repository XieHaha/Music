package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-03-10.
 */
public class NewsLabel implements Serializable {

    private static final long serialVersionUID = 5104498909637983131L;
    //信息id
    private int info_id;
    //标签id
    private String label_id;
    //标签名称值
    private String label_name;

    public int getInfo_id() {
        return info_id;
    }

    public void setInfo_id(int info_id) {
        this.info_id = info_id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    @Override
    public String toString() {
        return "NewsLabel [info_id=" + info_id + ", label_id=" + label_id + ", label_name="
                + label_name + "]";
    }

}
