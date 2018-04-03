package custom.base.entity;

import java.io.Serializable;

/**
 * Created by thl on 2016-03-10.
 */
public class LabelInfo implements Serializable {

    private static final long serialVersionUID = -3662697607516405197L;

    //标签ID
    private String label_id;
    //标签类型
    private int label_type;
    //标签名字
    private String label_name;

    public String getLabel_id() {
        return label_id;
    }

    public LabelInfo setLabel_id(String label_id) {
        this.label_id = label_id;
        return this;
    }

    public String getLabel_name() {
        return label_name;
    }

    public LabelInfo setLabel_name(String label_name) {
        this.label_name = label_name;
        return this;
    }

    public int getLabel_type() {
        return label_type;
    }

    public LabelInfo setLabel_type(int label_type) {
        this.label_type = label_type;
        return this;
    }

    @Override
    public String toString() {
        return "LabelInfo [label_id=" + label_id + ", label_type=" + label_type + ", label_name="
                + label_name + "]";
    }
}
