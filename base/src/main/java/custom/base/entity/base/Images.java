package custom.base.entity.base;

import java.io.Serializable;

/**
 * Created by luozi on 2016/3/25.
 */
public class Images implements Serializable {
    private static final long serialVersionUID = 1180325687120447823L;
    private String smallImage;//小图
    private String middleImage;//中图
    private String bigImage;//大图

    public String getSmallImage() {
        return smallImage;
    }

    public Images setSmallImage(String smallImage) {
        this.smallImage = smallImage;
        return this;
    }

    public String getMiddleImage() {
        return middleImage;
    }

    public Images setMiddleImage(String middleImage) {
        this.middleImage = middleImage;
        return this;
    }

    public String getBigImage() {
        return bigImage;
    }

    public Images setBigImage(String bigImage) {
        this.bigImage = bigImage;
        return this;
    }

    @Override
    public String toString() {
        return "Images{" +
                "smallImage='" + smallImage + '\'' +
                ", middleImage='" + middleImage + '\'' +
                ", bigImage='" + bigImage + '\'' +
                '}';
    }
}
