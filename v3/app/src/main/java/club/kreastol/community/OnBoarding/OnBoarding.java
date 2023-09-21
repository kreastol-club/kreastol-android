package club.kreastol.community.OnBoarding;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.cuberto.liquid_swipe.LiquidPager;

import club.kreastol.community.Common.SpalshScreen;
import club.kreastol.community.OnBoarding.helpers.ViewPager;
import club.kreastol.community.R;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;


public class OnBoarding extends AppCompatActivity {

    LiquidPager pager;
    ViewPager viewPager;
    private AppRunSession appRun;
    SpalshScreen splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        splash = new SpalshScreen();
        appRun = new AppRunSession(this);

        splash.setLocaleStart(appRun.getLanguage(), getResources());

        switch(appRun.getTheme()){
            case 0:
                setTheme(R.style.Theme_Kreastol);
                break;
            case 1:
                setTheme(R.style.Theme_Kreastol_Red_Pulse);
                break;
            case 2:
                setTheme(R.style.Theme_Kreastol_Futuristic);
                break;
            case 3:
                setTheme(R.style.Theme_Kreastol_Miku);
                break;
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding);

        pager = findViewById(R.id.pager);
        viewPager = new ViewPager(getSupportFragmentManager(), 1);
        pager.setAdapter(viewPager);


    }
    public void skipCall(View view){
        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
        finish();
    }
}