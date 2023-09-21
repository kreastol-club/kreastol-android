package club.kreastol.community.Common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import club.kreastol.community.OnBoarding.OnBoarding;
import club.kreastol.community.R;
import club.kreastol.community.User.DashboardFragments.SettingsFragment;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.Volley.VolleyResponse;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class SpalshScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 2000;
    ImageView backgroundImage;
    TextView poweredbyLine;

    //Animations
    private AppRunSession appRun;

    Animation sideAnim, bottomAnim;
    Configuration configuration;
    boolean isFirsTime = false;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
        isFirsTime = appRun.getFirstTime();
        if(!isFirsTime)
            setLocaleStart(appRun.getLanguage(), getResources());
        if(appRun.getNightBySystem()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        else{
            if(appRun.getUserNightMode().equals("dark")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else if(appRun.getUserNightMode().equals("light")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(appRun.getIsAuto() ? AppCompatDelegate.MODE_NIGHT_AUTO : MODE_NIGHT_UNSPECIFIED);
                configuration = getResources().getConfiguration();
                int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (currentNightMode) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        appRun.setToNightMode(false);
                        break;
                    case Configuration.UI_MODE_NIGHT_YES:
                        appRun.setToNightMode(true);
                        break;
                }
            }
        }

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
        getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION   |
                SYSTEM_UI_FLAG_LAYOUT_STABLE | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        //Hooks
        backgroundImage = findViewById(R.id.splash_title_logo);
        poweredbyLine = findViewById(R.id.poweredBy_line);


        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        //SetAnimations On ELements
        backgroundImage.setAnimation(sideAnim);
        poweredbyLine.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirsTime = appRun.getFirstTime();

                if(isFirsTime){
                    setLocale(Locale.getDefault().getLanguage());
                    appRun.setLanguage(Locale.getDefault().getLanguage());
                    appRun.setFirstTime(false);
                    Log.d("Lang-splash-first", Locale.getDefault().getLanguage());

                    Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Log.d("Lang-avarageRun: ", appRun.getLanguage());
                    setLocaleStart(appRun.getLanguage(), getResources());
                    Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIMER);

    }

    public void setLocaleStart(String lang, Resources res) {
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }


        public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SpalshScreen.class);
        finish();
        startActivity(refresh);
    }
}