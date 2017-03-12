package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter.UserAdapter;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.User;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Users;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FollowersListActivity extends AppCompatActivity {
    private String TAG = FollowersListActivity.class.getSimpleName();
    private EditText editTextUserId;
    private ImageButton searchButton;
    private SharedPreferences sharedPreferences;
    private String authToken;
    private ListView userList;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);
        sharedPreferences = getApplicationContext().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("auth_token", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userList = (ListView) findViewById(R.id.user_list_layout).findViewById(R.id.user_list);
        editTextUserId = (EditText) findViewById(R.id.user_list_layout).findViewById(R.id.edit_text_user_id);

        final List<User> users = new ArrayList<User>();
        final ApiService apiService = ApiManager.getApiService();
        Observable<Users> followerList = apiService.getFollowers(authToken, "");
        followerList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Users>() {
                    @Override
                    public void onCompleted() {
                        UserAdapter adapter = new UserAdapter(
                                getBaseContext(), R.layout.user_list_item, users, authToken,
                                CommonConst.ActivityName.TAG_LIST_FOLLOWERS_ACTIVITY);
                        userList.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR :" + e.toString());
                    }

                    @Override
                    public void onNext(Users followerList) {
                        for (User user : followerList.getUsers()) {
                            users.add(user);
                        }
                    }
                });

        searchButton = (ImageButton) findViewById(R.id.user_list_layout).findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users.clear();
                String userId = editTextUserId.getText().toString();
                Observable<Users> followerList = apiService.getFollowers(authToken, userId);
                followerList.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Users>() {
                            @Override
                            public void onCompleted() {
                                UserAdapter adapter = new UserAdapter(
                                        getBaseContext(), R.layout.user_list_item, users, authToken,
                                        CommonConst.ActivityName.TAG_LIST_FOLLOWERS_ACTIVITY);
                                userList.setAdapter(adapter);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "ERROR :" + e.toString());
                            }

                            @Override
                            public void onNext(Users followerList) {
                                for (User user : followerList.getUsers()) {
                                    users.add(user);
                                }
                            }
                        });
            }
        });
    }

    // @TODO: 2017/02/24 以下の記述が必要か？
//    @Override
//    protected void onPause() {
//        super.onPause();
//        TAG = null;
//        editTextUserId = null;
//        searchButton = null;
//        sharedPreferences = null;
//        authToken = null;
//        userList = null;
//
//    }
}