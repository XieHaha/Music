package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/6/3.
 */
public class QiNiuResponse implements Serializable {
    private static final long serialVersionUID = -8063155823309057591L;
    String hash;
    String key;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "QiNiuResponse{" +
                "hash='" + hash + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
