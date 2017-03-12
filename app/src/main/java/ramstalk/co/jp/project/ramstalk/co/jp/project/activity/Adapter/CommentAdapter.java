package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Comment;

/**
 * Created by sugitatakuto on 2017/02/27.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {
    private int resourceId;
    private LayoutInflater inflater;
    private List<Comment> comments;

    public CommentAdapter(Context context, int resourceId, List<Comment> comments) {
        super(context, resourceId, comments);
        this.resourceId = resourceId;
        this.comments = comments;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null)? convertView : this.inflater.inflate(this.resourceId, null);
        final Comment comment = this.comments.get(position);

        TextView userIdTextView = (TextView)view.findViewById(R.id.comment_list_user_id);
        userIdTextView.setText(comment.getUser().getUserId());

        TextView contentTextView = (TextView) view.findViewById(R.id.comment_list_content);
        contentTextView.setText(comment.getContent());

        TextView createdAtView = (TextView) view.findViewById(R.id.comment_list_created_at);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        createdAtView.setText(sdf.format(comment.getCreatedAt()));
        return view;
    }


}
