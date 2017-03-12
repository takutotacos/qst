package ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ramstalk.co.jp.project.ramstalk.co.jp.project.activity.Fragment.TimeLineFragment;

/**
 * Created by sugitatakuto on 2017/02/08.
 */
public class TimeLineFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<TimeLineFragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();

    public TimeLineFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(String categoryId, String categoryName) {
        mFragmentList.add(TimeLineFragment.newInstance(categoryId));
        mFragmentTitleList.add(categoryName);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
