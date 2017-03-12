package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by sugitatakuto on 2017/02/24.
 */
public class Notification {

    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("notified_by_id")
    private String notifiedById;
    @SerializedName("posting_id")
    private String postingId;
    @SerializedName("comment_id")
    private String commentId;
    @SerializedName("notice_type")
    private String noticeType;
    private boolean read;
    private User user;
    @SerializedName("notified_by")
    private User notifiedBy;
    private Comment comment;
    private Posting posting;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotifiedById() {
        return notifiedById;
    }

    public void setNotifiedById(String notifiedById) {
        this.notifiedById = notifiedById;
    }

    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getNotifiedBy() {
        return notifiedBy;
    }

    public void setNotifiedBy(User notifiedBy) {
        this.notifiedBy = notifiedBy;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
