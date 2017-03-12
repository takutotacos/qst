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

public class AddFollowingActivity extends AppCompatActivity {
    private String TAG = AddFollowingActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences = null;
    private String authToken;
    private EditText editTextUserId;
    private ImageButton searchButton;
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_following);
        sharedPreferences = getApplicationContext().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("auth_token", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userListView = (ListView) findViewById(R.id.user_list_layout).findViewById(R.id.user_list);
        editTextUserId = (EditText) findViewById(R.id.user_list_layout).findViewById(R.id.edit_text_user_id);
        searchButton = (ImageButton) findViewById(R.id.user_list_layout).findViewById(R.id.search_button);

        final List<User> userList = new ArrayList<User>();
        final ApiService apiService = ApiManager.getApiService();
        Observable<Users> users = apiService.searchUsersWithUserId(authToken, "");
        users.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Users>() {
                    @Override
                    public void onCompleted() {
                        UserAdapter adapter = new UserAdapter(
                                getBaseContext(), R.layout.user_list_item, userList, authToken, CommonConst.ActivityName.TAG_ADD_FOLLOWING_ACTIVITY);
                        userListView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR :" + e.toString());
                    }

                    @Override
                    public void onNext(Users users) {
                        for(User user : users.getUsers()) {
                            userList.add(user);
                        }
                    }
                });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.clear();
                String userId = editTextUserId.getText().toString();
                Observable<Users> users = apiService.searchUsersWithUserId(authToken, userId);
                users.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Users>() {
                            @Override
                            public void onCompleted() {
                                UserAdapter adapter = new UserAdapter(
                                        getBaseContext(), R.layout.user_list_item, userList, authToken, CommonConst.ActivityName.TAG_ADD_FOLLOWING_ACTIVITY);
                                userListView.setAdapter(adapter);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "ERROR :" + e.toString());
                            }

                            @Override
                            public void onNext(Users users) {
                                for(User user : users.getUsers()) {
                                    userList.add(user);
                                }
                            }
                        });
            }
        });
    }
}
