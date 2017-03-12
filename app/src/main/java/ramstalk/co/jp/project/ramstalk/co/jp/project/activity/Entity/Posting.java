package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity;

import com.google.gson.annotations.SerializedName;


import java.util.List;

/**
 * Created by sugitatakuto on 2017/02/09.
 */
public class Posting {
    private String id;
    private String content;
    private String latitude;
    private String longitude;
    private String address;

    @SerializedName("place_name")
    private String placeName;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;
    private String image;

    @SerializedName("like_counts")
    private String likeCounts;

    @SerializedName("can_like")
    private boolean canLike;

    @SerializedName("comment_counts")
    private String commentCounts;

    @SerializedName("like_id")
    private String likeId;

    private User user;
    private Category category;
    private List<Comment> comments;
    private List<Like> likes;

    public Posting() {}

    // when making a posting
    public Posting(String userId, String categoryId, String content, String latitude,
                   String longitude, String address, String placeName, String image) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.placeName = placeName;
        this.image = image;
    }

    public Posting(String id, String userId, String categoryId, String content, String latitude,
                   String longitude, String address, String placeName, String image) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.placeName = placeName;
        this.image = image;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(String likeCounts) {
        this.likeCounts = likeCounts;
    }

    public boolean isCanLike() {
        return canLike;
    }

    public void setCanLike(boolean canLike) {
        this.canLike = canLike;
    }

    public String getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(String commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
