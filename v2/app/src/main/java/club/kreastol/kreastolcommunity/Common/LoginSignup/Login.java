package club.kreastol.kreastolcommunity.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import club.kreastol.kreastolcommunity.HelperClasses.util.SessionManager;
import club.kreastol.kreastolcommunity.R;
import club.kreastol.kreastolcommunity.User.UserDashboard;

public class Login extends AppCompatActivity {

    final static String URL = "https://community.kreastol.club/android/login.php";
    TextInputLayout username, password;


    private static final String TAG = "Login";

    protected void onPause() {
        super.onPause();
        password.setError(null);
        username.setError(null);
        password.clearFocus();
        username.clearFocus();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_login);

        //Hooks for getting data
        password = findViewById(R.id.login_password);
        username = findViewById(R.id.login_username);


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


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if (response.contains("1")) {
                    Log.d(TAG, "Logged with normal");
                    session.setLogin(true);
                    session.setUsername(usernameS);
                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    finish();
                } else if (response.contains("2")) {
                    Log.d(TAG, "Logged with admin");
                    session.setLogin(true);
                    session.setAdmin(true);
                    session.setUsername(usernameS);
                    Log.d(TAG, session.getUsername());

                    startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                    finish();
                } else if (response.contains("0")) {
                    Log.d(TAG, "Wrong password");
                    Toast.makeText(getApplicationContext(), "Wrong Username or password!", Toast.LENGTH_LONG).show();
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
        super.onBackPressed();
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

