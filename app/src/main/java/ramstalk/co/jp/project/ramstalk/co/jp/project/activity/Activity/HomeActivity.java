package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Users;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Listener.ViewClickListener;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    private String TAG = HomeActivity.class.getSimpleName();
    private ImageButton addFollowingImgButton;
    private TextView followingNumber, followerNumber;
    private LinearLayout followings, followers;
    private SharedPreferences sharedPreferences;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getApplicationContext().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("auth_token", "");
        addFollowingImgButton = (ImageButton) findViewById(R.id.home_add_following_button);
        followingNumber = (TextView) findViewById(R.id.home_following_number);
        followerNumber = (TextView) findViewById(R.id.home_follower_number);
        followings = (LinearLayout) findViewById(R.id.home_following_list);
        followers = (LinearLayout) findViewById(R.id.home_follower_list);

        ApiService apiService = ApiManager.getApiService();
        Observable<Users> followerList = apiService.getFollowers(authToken, "");
        followerList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Users>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR :" + e.toString());
                    }

                    @Override
                    public void onNext(Users followerList) {
                        String number = String.valueOf(followerList.getUsers().size());
                        followerNumber.setText(number);
                    }
                });

        Observable<Users> followingList = apiService.getFollowings(authToken, "");
        followingList.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Users>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Users followingList) {
                        String number = String.valueOf(followingList.getUsers().size());
                        followingNumber.setText(number);
                    }
                });
        addFollowingImgButton.setOnClickListener(new ViewClickListener(HomeActivity.this, AddFollowingActivity.class));
        followings.setOnClickListener(new ViewClickListener(HomeActivity.this, FollowingsListActivity.class));
        followers.setOnClickListener(new ViewClickListener(HomeActivity.this, FollowersListActivity.class));
    }
}
