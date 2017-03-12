package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.ShowImageActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Notification;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sugitatakuto on 2017/02/24.
 */
public class NotificationAdapter extends ArrayAdapter<Notification> {

    private final String TAG = NotificationAdapter.class.getSimpleName();
    private int resourceId;
    private LayoutInflater inflater;
    private List<Notification> notifications;
    private String authToken;


    public NotificationAdapter(Context context, int resourceId, List<Notification> notifications, String authToken) {
        super(context, resourceId, notifications);
        this.resourceId = resourceId;
        this.notifications = notifications;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.authToken = authToken;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null)? convertView : this.inflater.inflate(this.resourceId, null);
        final Notification notification = this.notifications.get(position);
        LinearLayout notificationItem = (LinearLayout) view.findViewById(R.id.notification_item);
        final ApiService apiService = ApiManager.getApiService();

        // make each item clickable
        notificationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable<Void> result = apiService.showNotification(authToken, notification.getId());
                result.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Void>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "Success : Notification has been read." );
                                Intent intent = new Intent(getContext(), ShowImageActivity.class);
                                intent.putExtra("postingId", notification.getPostingId());
                                getContext().startActivity(intent);
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

        //change the background color depending on the read flg!
        if(notification.isRead()) {
            notificationItem.setBackgroundColor(Color.WHITE);
        }

        TextView notifiedByView = (TextView)view.findViewById(R.id.notification_notified_by);
        notifiedByView.setText(notification.getNotifiedBy().getUserId() + "さんが");

        TextView noticeTypeView = (TextView) view.findViewById(R.id.notification_notice_type);
        int noticeType = ("comment".equals(notification.getNoticeType()))?
                R.string.notification_notice_type_comment : R.string.notification_notice_type_like;
        noticeTypeView.setText(noticeType);

        TextView createdAtView = (TextView) view.findViewById(R.id.notification_created_at);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        String createdAtString = sdf.format(notification.getCreatedAt());
        createdAtView.setText(createdAtString);

        return view;
    }
}
