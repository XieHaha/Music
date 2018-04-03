package custom.base.entity;

import java.io.Serializable;

/**
 * Created by luozi on 2016/11/16.
 */

public class OfficeMsg implements Serializable {
    private static final long serialVersionUID = 3703050236149090427L;
    private int id;
    private String time;//时间
    private String title;//标题
    private String content;//内容
    private String isRead;//是否已读

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OfficeMsg{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
