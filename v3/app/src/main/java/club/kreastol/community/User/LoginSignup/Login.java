package club.kreastol.community.User.LoginSignup;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import club.kreastol.community.R;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;
import club.kreastol.community.util.UserProfilSession;


public class Login extends AppCompatActivity {


    private UserProfilSession userProfile;
    private AppRunSession appRun;

    LinkHolder links = new LinkHolder();
    TextInputLayout username, password;
    Context mContext;


    private static final String TAG = "Login";

    protected void onPause() {
        super.onPause();
        password.setError(null);
        username.setError(null);
        password.clearFocus();
        username.clearFocus();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RetailerStartupScreen.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_btn), "transition_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
        userProfile = new UserProfilSession(this);
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
        setContentView(R.layout.activity_retailer_login);

        //Hooks for getting data
        password = findViewById(R.id.login_password);
        username = findViewById(R.id.login_username);

        mContext = this;


    }


    //Login Button
    public void callLogin(View view) {
        SessionManager session = new SessionManager(getApplicationContext());
        if (!validatePassword() | !validateUsername()) {
            return;
        }

        final String usernameS = username.getEditText().getText().toString().trim();
        final String passwordS = password.getEditText().getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
        Log.d(TAG, "Intent reached!");


        StringRequest request = new StringRequest(Request.Method.POST, links.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.contains("1")) {
                    Log.d(TAG, "Logged with normal");
                    userProfile.setLogin(true);
                    userProfile.setUsername(usernameS);
                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    finish();
                } else if (response.contains("2")) {
                    Log.d(TAG, "Logged with admin");
                    userProfile.setLogin(true);
                    userProfile.setAdmin(true);
                    userProfile.setUsername(usernameS);
                    Log.d(TAG, userProfile.getUsername());

                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    finish();
                } else if (response.contains("0")) {
                    Log.d(TAG, "Wrong password");
                    Toast.makeText(mContext, "Wrong Username or password!", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Other problem");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("username", usernameS);
                params.put("password", passwordS);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    //SignUp Button
    public void callToSignUp(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void callBackButtonClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), RetailerStartupScreen.class);

        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(findViewById(R.id.signup_btn), "transition_signup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
        finish();
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            username.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}

