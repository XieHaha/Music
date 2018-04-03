package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-03-10.
 */
public class ShareLabel implements Serializable {

    private static final long serialVersionUID = -1991427046087529948L;
    //信息id
    private int share_id;
    //标签id
    private String label_id;
    //标签名称值
    private String label_name;

    public String getLabel_id() {
        return label_id;
    }

    public void setLabel_id(String label_id) {
        this.label_id = label_id;
    }

    public int getShare_id() {
        return share_id;
    }

    public void setShare_id(int share_id) {
        this.share_id = share_id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    @Override
    public String toString() {
        return "ShareLabel [share_id=" + share_id + ", label_id=" + label_id + ", label_name="
                + label_name + "]";
    }
}
