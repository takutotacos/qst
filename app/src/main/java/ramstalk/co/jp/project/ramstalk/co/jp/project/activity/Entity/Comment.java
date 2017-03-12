package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Created by sugitatakuto on 2017/02/21.
 */
public class Comment {
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("posting_id")
    private String postingId;

    private String content;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    private User user;

    private Posting posting;

    public Comment() {}

    public Comment(String content) {
        this.content = content;
    }

    public Comment(String id, String userId, String content) {
        this.id = id;
        this.userId = userId;
        this.content = content;
    }
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

    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Posting getPosting() {
        return posting;
    }

    public void setPosting(Posting posting) {
        this.posting = posting;
    }
}
