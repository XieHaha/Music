package custom.base.entity;

import java.io.Serializable;

import custom.base.entity.base.UserBase;

/**
 * Created by thl on 2016-06-06.
 */
public class SharesCommentInfos implements Serializable {
    private static final long serialVersionUID = 7129455990507641475L;

    private String commentAddr;//评论的地点
    private UserBase commentUserInfo;//评论用户
    private String content;//评论内容
    private String createTime;//创建时间
    private UserBase replyUserInfo;//被回复的用户

    public String getCommentAddr() {
        return commentAddr;
    }

    public void setCommentAddr(String commentAddr) {
        this.commentAddr = commentAddr;
    }

    public UserBase getCommentUserInfo() {
        return commentUserInfo;
    }

    public void setCommentUserInfo(UserBase commentUserInfo) {
        this.commentUserInfo = commentUserInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserBase getReplyUserInfo() {
        return replyUserInfo;
    }

    public void setReplyUserInfo(UserBase replyUserInfo) {
        this.replyUserInfo = replyUserInfo;
    }

    @Override
    public String toString() {
        return "SharesCommentInfos{ " +
                "commentAddr='" + commentAddr + '\'' +
                ", commentUserInfo='" + commentUserInfo + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", replyUserInfo=" + replyUserInfo +
                '}';
    }
}
