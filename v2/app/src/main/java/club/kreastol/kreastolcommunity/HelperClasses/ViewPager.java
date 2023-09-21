package club.kreastol.kreastolcommunity.HelperClasses;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import club.kreastol.kreastolcommunity.Common.FirstFragment;
import club.kreastol.kreastolcommunity.Common.SecondFragment;
import club.kreastol.kreastolcommunity.Common.ThirdFragment;

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
