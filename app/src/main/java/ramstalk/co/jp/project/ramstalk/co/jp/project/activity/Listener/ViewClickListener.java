package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

/**
 * Created by sugitatakuto on 2017/02/11.
 */
public class ViewClickListener implements View.OnClickListener {
    private String TAG = getClass().getName();
    private Activity current;
    private Class selectedClass;

    public ViewClickListener(Activity current, Class selectedActivity) {
        this.current = current;
        this.selectedClass = selectedActivity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(current, selectedClass);
        current.startActivity(intent);
    }
}
