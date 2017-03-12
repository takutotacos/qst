package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter.NotificationAdapter;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Notification;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Notifications;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NotificationActivity extends AppCompatActivity {
    private final String TAG = NotificationActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private String authToken;
    private ListView notificationListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        sharedPreferences = getApplicationContext().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("auth_token", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notificationListView = (ListView) findViewById(R.id.notification_list_layout).findViewById(R.id.notification_list);

        final List<Notification> notificationList = new ArrayList<Notification>();
        ApiService apiService = ApiManager.getApiService();
        Observable<Notifications> notifications = apiService.getNotifications(authToken);
        notifications.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Notifications>() {
                    @Override
                    public void onCompleted() {
                        NotificationAdapter adapter = new NotificationAdapter(
                                getBaseContext(), R.layout.notification_list_item, notificationList, authToken);
                        notificationListView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR :" + e.toString());
                    }

                    @Override
                    public void onNext(Notifications notifications) {
                        for(Notification notification : notifications.getNotifications()) {
                            notificationList.add(notification);
                        }
                    }
                });
    }
}
