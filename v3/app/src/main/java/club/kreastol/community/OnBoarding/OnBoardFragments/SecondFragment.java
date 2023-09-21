package club.kreastol.community.OnBoarding.OnBoardFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import club.kreastol.community.R;
import club.kreastol.community.User.UserDashboard;


public class SecondFragment extends Fragment {

    private Switch dayNightSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding_second, container, false);


        return view;
    }
}