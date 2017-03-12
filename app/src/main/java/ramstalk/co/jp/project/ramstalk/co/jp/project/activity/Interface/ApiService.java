package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface;

import java.util.HashMap;

import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Categories;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Comment;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Comments;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Notifications;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Posting;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Postings;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.User;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Users;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sugitatakuto on 2017/02/20.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("authenticate")
    Observable<User> authenticate(@Field("email") String email,
                                  @Field("password") String password);
    @POST("users")
    Observable<User> registerUser(@Body HashMap<String, User> user);

    @GET("categories")
    Observable<Categories> getAllCategories(@Header("Authorization") String authToken);

    @POST("postings")
    Observable<Posting> createOnePosting(@Header("Authorization") String authToken,
                                         @Body HashMap<String, Posting> posting);
    @GET("postings_by_category")
    Observable<Postings> getPostingsByCategories(@Header("Authorization") String authToken,
                                                 @Query("category_id") String categoryId);
    @GET("find_within_certain_distance")
    Observable<Postings> getPostingsWithinCertainDistance(@Header("Authorization") String authToken,
                                                          @Query("category_id") String categoryId,
                                                          @Query("latitude") String latitude,
                                                          @Query("longitude") String longitude);
    @GET("postings/{posting_id}")
    Observable<Posting> getPosting(@Header("Authorization") String authToken,
                                   @Path("posting_id") String posting_id);
    @GET("followings")
    Observable<Users> getFollowings(@Header("Authorization") String authToken,
                                    @Query("user_id") String user_id);
    @FormUrlEncoded
    @POST("delete_followings")
    Observable<Void> deleteFromFollowing(@Header("Authorization") String authToken,
                                          @Field("id") String idToDelete);
    @GET("followers")
    Observable<Users> getFollowers(@Header("Authorization") String authToken,
                                   @Query("user_id") String user_id);
    // search users a user is not following yet
    @GET("like_user_id_query")
    Observable<Users> searchUsersWithUserId(@Header("Authorization") String authToken,
                                            @Query("user_id") String user_id);
    @FormUrlEncoded
    @POST("relationships")
    Observable<Void> addToFollowing(@Header("Authorization") String authToken,
                                    @Field("id") String idToAdd);
    @POST("postings/{posting_id}/comments")
    Observable<Posting> addComment(@Header("Authorization") String authToken,
                                   @Path("posting_id") String posting_id,
                                   @Body HashMap<String, Comment> commentHashMap);
    @POST("postings/{posting_id}/likes")
    Observable<Posting> addLike(@Header("Authorization") String authToken,
                                @Path("posting_id") String posting_id);
    @DELETE("postings/{posting_id}/likes/{like_id}")
    Observable<Posting> deleteLike(@Header("Authorization") String authToken,
                                   @Path("posting_id") String posting_id,
                                   @Path("like_id") String like_id);
    @GET("notifications")
    Observable<Notifications> getNotifications(@Header("Authorization") String authToken);

    @POST("notifications/{notification_id}/")
    Observable<Void> showNotification(@Header("Authorization") String authToken,
                                              @Path("notification_id") String notification_id);
    @GET("postings/{posting_id}/comments")
    Observable<Comments> getComments(@Header("Authorization") String authToken,
                                     @Path("posting_id") String posting_id);
}
