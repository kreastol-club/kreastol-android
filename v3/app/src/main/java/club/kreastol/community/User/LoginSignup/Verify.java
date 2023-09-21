package club.kreastol.community.User.LoginSignup;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import club.kreastol.community.R;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.UserProfilSession;

public class Verify extends AppCompatActivity {
    boolean delete;
    EditText editText;
    String text;
    Context context;
    UserProfilSession userProfilSession;
    AppRunSession appRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appRun = new AppRunSession(this);
        setLocaleStart(appRun.getLanguage());

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
        setContentView(R.layout.activity_verify);
        context = this;
        userProfilSession = new UserProfilSession(context);
        editText = findViewById(R.id.editText);
        delete = false;

        text = editText.getText().toString();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                text = editText.getText().toString();
                if (count > after)
                    delete = true;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                StringBuilder sb = new StringBuilder(s.toString());
                int replacePosition = editText.getSelectionEnd();

                if (s.length() != 6) { //where 6 is the character underline per text
                    if (!delete) {
                        if (replacePosition < s.length())
                            sb.deleteCharAt(replacePosition);
                    } else {
                        sb.insert(replacePosition, '_');
                    }

                    if (replacePosition < s.length() || delete) {
                        editText.setText(sb.toString());
                        editText.setSelection(replacePosition);
                    } else {
                        editText.setText(text);
                        editText.setSelection(replacePosition - 1);
                    }
                }
                delete = false;
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
    });
    }

    public void callVerify(View view) {
        StringRequest request = new StringRequest(Request.Method.POST, new LinkHolder().VERIFY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Verify-Response", response);
                if(response.equals("yes")){
                    Toast.makeText(context, "Verified",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(context, "Something went wrong!",Toast.LENGTH_LONG).show();
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
                params.put("vkey", editText.getText().toString());
                params.put("username", userProfilSession.getUsername());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public void setLocaleStart(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}