package club.kreastol.community.OnBoarding.helpers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import club.kreastol.community.OnBoarding.OnBoardFragments.FirstFragment;
import club.kreastol.community.OnBoarding.OnBoardFragments.SecondFragment;
import club.kreastol.community.OnBoarding.OnBoardFragments.ThirdFragment;


public class ViewPager extends FragmentPagerAdapter {
    public ViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return new FirstFragment();
            case 1: return new SecondFragment();
            case 2: return new ThirdFragment();
        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;
    }
}
