package club.kreastol.kreastolcommunity.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import club.kreastol.kreastolcommunity.R;
import club.kreastol.kreastolcommunity.User.UserDashboard;

public class SpalshScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 5000;

    ImageView backgroundImage;
    TextView poweredbyLine;

    //Animations

    Animation sideAnim, bottomAnim;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

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
                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);

                boolean isFirsTime = onBoardingScreen.getBoolean("firtTime", true);

                if(isFirsTime){

                    SharedPreferences.Editor editor = onBoardingScreen.edit();
                    editor.putBoolean("firtTime", false);
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    /*
                    TODO Ne felejts el ezt az onboarding ot UserDasboardRa irni
                     */
                    Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_TIMER);

    }
}