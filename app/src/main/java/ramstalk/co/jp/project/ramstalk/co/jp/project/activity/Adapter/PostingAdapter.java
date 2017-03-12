package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.ShowImageActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Comment;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Posting;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sugitatakuto on 2017/02/10.
 */
public class PostingAdapter extends ArrayAdapter<Posting> {
    private final String TAG = PostingAdapter.class.getSimpleName();
    private int resourceId;
    private List<Posting> postings;
    private LayoutInflater inflater;
    private SharedPreferences sharedPreferences;
    private TextView likeNumber;

    public PostingAdapter(Context context,int resourceId, List<Posting> postings) {
        super(context, resourceId, postings);
        this.resourceId = resourceId;
        this.postings = postings;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sharedPreferences = context.getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = (convertView != null)? convertView : this.inflater.inflate(this.resourceId, null);
        final Posting posting = this.postings.get(position);

        // setting comment
        TextView onePostingLineComment = (TextView)view.findViewById(R.id.time_line_posting_comment);
        onePostingLineComment.setText(posting.getContent());

        // setting userId
        TextView onePostingLineUserId = (TextView) view.findViewById(R.id.time_line_posting_user_id);
        onePostingLineUserId.setText(posting.getUser().getUserId());

        // setting posting image
        ImageView onePostingLineImage = (ImageView) view.findViewById(R.id.time_line_posting_image);
        if(posting.getImage() != null) {
            byte[] decodedString = Base64.decode(posting.getImage(), Base64.DEFAULT);
            Glide.with(getContext())
                    .load(decodedString)
                    .fitCenter()
                    .into(onePostingLineImage);
        } else {
            onePostingLineImage.setVisibility(View.GONE);
        }

        // setting number of likes
        likeNumber = (TextView) view.findViewById(R.id.like_number);
        likeNumber.setText(posting.getLikeCounts());
        final ApiService apiService = ApiManager.getApiService();
        final ImageView likeImage = (ImageView) view.findViewById(R.id.like_button);
        if(!posting.isCanLike()) {
            likeImage.setBackgroundResource(android.R.drawable.ic_delete);
        }
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<Posting> postingTarget = posting.isCanLike()?
                        apiService.addLike(sharedPreferences.getString("auth_token", ""), posting.getId()) :
                        apiService.deleteLike(sharedPreferences.getString("auth_token", ""), posting.getId(), posting.getLikeId());
                postingTarget.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Posting>() {
                            @Override
                            public void onCompleted() {
                                int drawableId =
                                        posting.isCanLike()? android.R.drawable.ic_input_add : android.R.drawable.ic_input_delete;
                                likeImage.setBackgroundResource(drawableId);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "ERROR : " + e.toString());
                            }

                            @Override
                            public void onNext(Posting postingTarget) {
                                postings.get(position).setLikeCounts(postingTarget.getLikeCounts());
                                postings.get(position).setCanLike(postingTarget.isCanLike());
                                postings.get(position).setLikeId(postingTarget.getLikeId());
                            }
                        });
                }
        });

        // setting number of comments
        TextView commentNumber = (TextView) view.findViewById(R.id.comment_number);
        commentNumber.setText(posting.getCommentCounts());

        // setting comment input layout visibility
        final LinearLayout commentInputComponent = (LinearLayout) view.findViewById(R.id.comment_input_component);
        commentInputComponent.setVisibility(View.GONE);
        ImageView commentImage = (ImageView) view.findViewById(R.id.comment_button);
        commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentInputComponent.setVisibility(View.VISIBLE);
            }
        });
        EditText commentInput = (EditText) view.findViewById(R.id.comment_input);
        final String comment = commentInput.getText().toString();
        TextView commentInputButton = (TextView) view.findViewById(R.id.comment_input_button);
        commentInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(comment)) {
                    Comment commentToAdd = new Comment(comment);
                    HashMap<String, Comment> commentHashMap = new HashMap<String, Comment>();
                    commentHashMap.put("comment", commentToAdd);
                    Observable<Posting> postingTarget = apiService.addComment(
                            sharedPreferences.getString("auth_token", ""), posting.getId(), commentHashMap);
                    postingTarget.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Posting>() {
                                @Override
                                public void onCompleted() {
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "ERROR : " + e.toString());
                                }

                                @Override
                                public void onNext(Posting postingTarget) {
                                    postings.get(position).setCommentCounts(postingTarget.getCommentCounts());
                                }
                            });
                }
            }
        });

        LinearLayout commentDisplaySwitcher = (LinearLayout) view.findViewById(R.id.comment_display_switch);
        commentDisplaySwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowImageActivity.class);
                intent.putExtra("postingId", posting.getId());
                getContext().startActivity(intent);
            }
        });
        return view;
    }
}
