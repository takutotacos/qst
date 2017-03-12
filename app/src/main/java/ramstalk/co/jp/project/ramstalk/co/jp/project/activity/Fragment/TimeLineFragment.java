package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ramstalk.co.jp.project.R;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter.PostingAdapter;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Cons.CommonConst;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Posting;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Entity.Postings;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Interface.ApiService;
import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Manager.ApiManager;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

;

public class TimeLineFragment extends Fragment {
    private String TAG = TimeLineFragment.class.getSimpleName();
    private final static String CATEGORY_ID = "cateogry";
    private SharedPreferences sharedPreferences;
    private ListView listView;

    public TimeLineFragment() {
    }

    public static TimeLineFragment newInstance(String categoryId) {
        TimeLineFragment timeLineFragment = new TimeLineFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_ID, categoryId);
        timeLineFragment.setArguments(bundle);
        return timeLineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listView = (ListView) inflater.inflate(R.layout.fragment_timeline_list, null);
        String categoryId = getArguments().getString(CATEGORY_ID);
        sharedPreferences = getActivity().getSharedPreferences(CommonConst.FileName.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("auth_token", "");

        final List<Posting> postingList = new ArrayList<Posting>();
        ApiService apiService = ApiManager.getApiService();
        Observable<Postings> postings = apiService.getPostingsByCategories(authToken, categoryId);
        postings.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Postings>() {
                    @Override
                    public void onCompleted() {
                        PostingAdapter adapter = new PostingAdapter(
                                getContext(), R.layout.fragment_timeline, postingList);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "ERROR : " + e.toString());
                    }

                    @Override
                    public void onNext(Postings postings) {
                        if(postings.getPostings() != null) {
                            for (Posting posting : postings.getPostings()) {
                                postingList.add(posting);
                            }
                        }
                    }
                });
        return listView;
    }
}
