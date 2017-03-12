package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter.CommentAdapter;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Comment;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Comments;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Posting;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShowImageActivity extends AppCompatActivity {
    private String TAG = ShowImageActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private String authToken;
    private String postingId;
    private TextView textViewUserId;
    private TextView textViewContent;
    private TextView likeNumber;
    private TextView commentNumber;
    private ImageView imageView;
    private ImageView commentImage;
    private LinearLayout commentInputComponent;
    private EditText commentInput;
    private TextView commentInputButton;
    private ListView commentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getApplicationContext().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("auth_token", "");
        imageView = (ImageView) findViewById(R.id.content_show_image).findViewById(R.id.show_image_image);
        textViewUserId = (TextView) findViewById(R.id.content_show_image).findViewById(R.id.show_image_user_id);
        textViewContent = (TextView) findViewById(R.id.content_show_image).findViewById(R.id.show_image_content);
        likeNumber = (TextView) findViewById(R.id.content_show_image).findViewById(R.id.like_number);
        commentNumber = (TextView) findViewById(R.id.content_show_image).findViewById(R.id.comment_number);
        commentInputComponent = (LinearLayout) findViewById(R.id.content_show_image).findViewById(R.id.comment_input_component);
        commentImage = (ImageView) findViewById(R.id.content_show_image).findViewById(R.id.comment_button);
        commentInput = (EditText) findViewById(R.id.content_show_image).findViewById(R.id.comment_input);
        commentInputButton = (TextView) findViewById(R.id.content_show_image).findViewById(R.id.comment_input_button);
        commentListView = (ListView) findViewById(R.id.content_show_image).findViewById(R.id.comment_list);
        final List<Comment> commentList = new ArrayList<>();
        final CommentAdapter adapter = new CommentAdapter(
                getApplicationContext(), R.layout.comment_list_item, commentList);

        commentInputComponent.setVisibility(View.GONE);
        Intent incomingIntent = getIntent();
        postingId = incomingIntent.getStringExtra("postingId");

        final ApiService apiService = ApiManager.getApiService();
        Observable<Posting> posting = apiService.getPosting(authToken, postingId);
        posting.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Posting>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR : " + e.toString());
                    }

                    @Override
                    public void onNext(final Posting posting) {
                        String content = posting.getContent();
                        String userId = posting.getUser().getUserId();
                        if (posting.getImage() != null) {
                            byte[] decodedString = Base64.decode(posting.getImage(), Base64.DEFAULT);
                            Glide.with(getApplicationContext())
                                    .load(decodedString)
                                    .fitCenter()
                                    .into(imageView);
                        } else {
                            imageView.setVisibility(View.GONE);
                        }
                        textViewUserId.setText(userId);
                        textViewUserId.setTextColor(Color.RED);
                        textViewContent.setText(content);
                        textViewContent.setTextColor(Color.RED);
                        likeNumber.setText(posting.getLikeCounts());
                        commentNumber.setText(posting.getCommentCounts());
                    }
                });

        Observable<Comments> comments = apiService.getComments(authToken, postingId);
        comments.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Comments>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                        commentListView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR :" + e.toString());
                    }

                    @Override
                    public void onNext(Comments comments) {
                        for (Comment comment : comments.getComments()) {
                            commentList.add(comment);
                        }
                    }
                });

        commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentInputComponent.setVisibility(View.VISIBLE);
            }
        });

        commentInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInput.getText().toString();
                if (!"".equals(comment)) {
                    Comment commentToAdd = new Comment(comment);
                    HashMap<String, Comment> commentHashMap = new HashMap<>();
                    commentHashMap.put("comment", commentToAdd);
                    Observable<Posting> postingTarget = apiService.addComment(
                            sharedPreferences.getString("auth_token", ""), postingId, commentHashMap);
                    postingTarget.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Posting>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "ERROR : " + e.toString());
                                }

                                @Override
                                public void onNext(Posting postingTarget) {
                                    for(int i = 0; i < Integer.parseInt(postingTarget.getCommentCounts()); i++) {
                                        if(i + 1 == Integer.parseInt(postingTarget.getCommentCounts())) {
                                            commentList.add(postingTarget.getComments().get(i));
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    commentNumber.setText(postingTarget.getCommentCounts());
                                    commentInputComponent.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }
}
