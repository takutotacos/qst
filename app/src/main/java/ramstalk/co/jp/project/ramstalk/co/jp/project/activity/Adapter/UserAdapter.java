package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.User;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sugitatakuto on 2017/02/12.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private final String TAG = UserAdapter.class.getSimpleName();
    private int resourceId;
    private LayoutInflater inflater = null;
    private List<User> users;
    private String authToken = null;
    private String activityName;


    public UserAdapter(Context context, int resourceId, List<User> users, String authToken, String activityName) {
        super(context, resourceId, users);
        this.resourceId = resourceId;
        this.users = users;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.authToken = authToken;
        this.activityName = activityName;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }
        User user = this.users.get(position);
//        ImageView imageImageView = (ImageView) view.findViewById(R.id.image_image_view);

        TextView userIdTextView = (TextView)view.findViewById(R.id.user_id_text_view);
        userIdTextView.setText(user.getUserId());

        ImageButton userListButton = (ImageButton) view.findViewById(R.id.user_list_button);
        setUpButton(activityName, userListButton, position);
        return view;
    }

    private void setUpButton(final String activityName, ImageButton userListButton, final int position) {
        if(CommonConst.ActivityName.TAG_LIST_FOLLOWINGS_ACTIVITY.equals(activityName)) {
            userListButton.setBackgroundResource(android.R.drawable.ic_menu_close_clear_cancel);
        }
        final ApiService apiService = ApiManager.getApiService();
        final String userId = users.get(position).getId();
        userListButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Observable<Void> relationships = (CommonConst.ActivityName.TAG_LIST_FOLLOWINGS_ACTIVITY.equals(activityName))?
                        apiService.deleteFromFollowing(authToken, userId) : apiService.addToFollowing(authToken, userId);
                    relationships.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Void>() {
                                @Override
                                public void onCompleted() {
                                    users.remove(position);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "ERROR :" + e.toString());
                                }

                                @Override
                                public void onNext(Void params) {
                                }
                            });
                }
            });
    }
}
