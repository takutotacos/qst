package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.HomeActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.MapsActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.NotificationActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.PostingActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Activity.TimeLineActivity;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;

public class FooterFragment extends Fragment {
    private static String TAG = FooterFragment.class.getSimpleName();
    private Button mNotificationButton;
    private Button mHomeButton;
    private Button mMakingAPostingButton;
    private Button mTimelineButton;
    private Button mMapButton;
    public FooterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_footer, null);
        mTimelineButton= (Button) view.findViewById(R.id.footer_timeline);
        mNotificationButton = (Button) view.findViewById(R.id.footer_notification);
        mHomeButton = (Button) view.findViewById(R.id.footer_home);
        mMapButton = (Button) view.findViewById(R.id.footer_map);
        mMakingAPostingButton = (Button) view.findViewById(R.id.footer_making_a_posting);

        mTimelineButton.setOnTouchListener(new TouchListener(CommonConst.ActivityName.TAG_TIME_LINE_ACTIVITY));
        mNotificationButton.setOnTouchListener(new TouchListener(CommonConst.ActivityName.TAG_NOTIFICATION_ACTIVITY));
        mHomeButton.setOnTouchListener(new TouchListener(CommonConst.ActivityName.TAG_HOME_ACTIVITY));
        mMapButton.setOnTouchListener(new TouchListener(CommonConst.ActivityName.TAG_MAPS_ACTIVITY));
        mMakingAPostingButton.setOnTouchListener(new TouchListener(CommonConst.ActivityName.TAG_POSTING_ACTIVITY));
        return view;
    }

    private static class TouchListener implements View.OnTouchListener {
        private String selectedActivity;

        public TouchListener(String selectedActivity) {
            this.selectedActivity = selectedActivity;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Class selectedClass = getActivityFromSelection(selectedActivity);
            Intent intent = new Intent(v.getContext(), selectedClass);
            v.getContext().startActivity(intent);
            return true;
        }

        private Class getActivityFromSelection(String selectedActivity) {
            switch (selectedActivity) {
                case CommonConst.ActivityName.TAG_HOME_ACTIVITY:
                    return HomeActivity.class;

                case CommonConst.ActivityName.TAG_TIME_LINE_ACTIVITY:
                    return TimeLineActivity.class;

                case CommonConst.ActivityName.TAG_NOTIFICATION_ACTIVITY:
                    return NotificationActivity.class;

                case CommonConst.ActivityName.TAG_MAPS_ACTIVITY:
                    return MapsActivity.class;

                case CommonConst.ActivityName.TAG_POSTING_ACTIVITY:
                    return PostingActivity.class;

                default:
                    Log.e(TAG, "Unexpected screen transition.");
                    return NotificationActivity.class;
            }
        }
    }
}