package custom.base.entity;

public class Comment {
    private String id;
    private String user_id;
    private String share_id;
    private String parentuserid;
    private String content;
    private String addtime;
    private String day;
    private String time;
    private String nickname;
    private String parentnickname;
    private String profile_image_url;
    private String from;

    public String getParentNickname() {
        return parentnickname;
    }

    public void setParentNickname(String parentnickname) {
        this.parentnickname = parentnickname;
    }

    public String getParentuserid() {
        return parentuserid;
    }

    public void setParentuserid(String parentuserid) {
        this.parentuserid = parentuserid;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShare_id() {
        return share_id;
    }

    public void setShare_id(String share_id) {
        this.share_id = share_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", share_id=" + share_id + ", content="
                + content + ", addtime=" + addtime + ", day=" + day + ", time="
                + time + ", nickname=" + nickname + ", profile_image_url="
                + profile_image_url + ", from=" + from + ", parentnickname="
                + parentnickname + ", parentuserid=" + parentuserid
                + ", user_id=" + user_id + "]";
    }

}
