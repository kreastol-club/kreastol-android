package club.kreastol.community.User.LoginSignup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import club.kreastol.community.R;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;


public class RetailerStartupScreen extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UserDashboard.class));
        finish();
    }
    private AppRunSession appRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
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
        setContentView(R.layout.activity_retailer_startup_screen);
    }

    public void callLoginScreen(View view){

        Intent intent = new Intent(getApplicationContext(), Login.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.login_btn), "transition_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerStartupScreen.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }

    public void callSignUpScreen(View view){

        Intent intent = new Intent(getApplicationContext(), SignUp.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_btn), "transition_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RetailerStartupScreen.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }
}