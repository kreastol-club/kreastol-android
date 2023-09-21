package club.kreastol.community.User.LoginSignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import club.kreastol.community.R;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.SessionManager;
import club.kreastol.community.util.UserProfilSession;


public class SignUp3rdClass extends AppCompatActivity {


    //Get data variables
    TextInputLayout password, passwordConfirm;

    String lNameS;
    String fNameS;
    String usernameS;
    String emailS;
    String genderS;
    String dateS;
    private Context mContext;
    private AppRunSession appRun;
    private UserProfilSession userProfiSession;
    LinkHolder links;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
        userProfiSession = new UserProfilSession(this);
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
        setContentView(R.layout.activity_retailer_signup3);
        mContext = this;
        links = new LinkHolder();
        //Hooks for getting data
        password = findViewById(R.id.signup_password);
        passwordConfirm = findViewById(R.id.signup_password_confirm);

        //Getting the data from previos activiy
        Intent intentS = getIntent();
        lNameS = intentS.getStringExtra("lName");
        fNameS = intentS.getStringExtra("fName");
        usernameS = intentS.getStringExtra("username");
        emailS = intentS.getStringExtra("email");
        genderS = intentS.getStringExtra("gender");
        dateS = intentS.getStringExtra("date");


    }

    public void callBackButtonClicked(View view) {
        super.onBackPressed();
    }

    public void callToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void callSignUpNClicked(View view) {
        if (!validatePasswords()) {
            return;
        }

//        Intent intent = new Intent(getApplicationContext(), RetailerStartupScreen.class);
//        startActivity(intent);
        Log.d("Signup",lNameS);
        Log.d("Signup",fNameS);
        Log.d("Signup",usernameS);
        Log.d("Signup",emailS);
        Log.d("Signup",genderS);
        Log.d("Signup",password.getEditText().getText().toString());
        Log.d("Signup",passwordConfirm.getEditText().getText().toString());
        Log.d("Signup-lang", appRun.getLanguage());


        StringRequest request = new StringRequest(Request.Method.POST, links.REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Register", response);

                if(response.contains("username"))
                    Toast.makeText(mContext, getString(R.string.username_taken),Toast.LENGTH_LONG).show();

                if(response.contains("email"))
                    Toast.makeText(mContext, getString(R.string.email_taken),Toast.LENGTH_LONG).show();

                Log.d("Signup-Response", response);
                if(response.equals("yes")){
                    userProfiSession.setUsername(usernameS);
                    Toast.makeText(mContext, "Verification Sent",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Verify.class);
                    startActivity(intent);
                    finish();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("first_name", fNameS);
                params.put("last_name", lNameS);
                params.put("username", usernameS);
                params.put("email", emailS);
                params.put("gender", genderS);
                params.put("birthday", dateS);
                params.put("password", password.getEditText().getText().toString());
                params.put("lang", appRun.getLanguage());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private boolean validatePasswords() {
        String val = password.getEditText().getText().toString().trim();
        String val2 = passwordConfirm.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" + //at least 1 digit
                //"(?=.*[a-z])" + //at least lowercase char
                //"(?=.*[A-Z])" + //at least 1 uppercase char
                "(?=.*[a-zA-Z])" + // any letter
                //"(?=.*[@#$%^&+=])" + //at least 1 special char
                "(?=\\S+$)" + // no white spaces
                ".{6,}" + //at least 6 length
                "$";
        String passwordLenght = "^" + ".{6,}" + "$";
        if (val.isEmpty()) {
            password.setError(this.getString(R.string.retailer_signup_error_cannot_be_empty));
            return false;
        } else if (!val.matches(passwordLenght)) {
            password.setError(this.getString(R.string.retailer_signup_error_password_at_least_size));
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError(this.getString(R.string.retailer_signup_error_password_at_least_chars));
            return false;
        } else {
            if (!val.equals(val2)) {
                password.setError(this.getString(R.string.retailer_signup_error_passwords_match));
                passwordConfirm.setError(this.getString(R.string.retailer_signup_error_passwords_match));
                return false;
            }else{
                password.setError(null);
                passwordConfirm.setError(null);
                password.setErrorEnabled(false);
                return true;
            }
        }
    }


}