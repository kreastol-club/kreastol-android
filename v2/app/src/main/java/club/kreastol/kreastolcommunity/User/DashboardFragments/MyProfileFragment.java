package club.kreastol.kreastolcommunity.User.DashboardFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import club.kreastol.kreastolcommunity.Database.DatabaseConnect;
import club.kreastol.kreastolcommunity.Database.VolleyCallBack;
import club.kreastol.kreastolcommunity.HelperClasses.util.SessionManager;
import club.kreastol.kreastolcommunity.R;
import club.kreastol.kreastolcommunity.User.UserDashboard;


public class MyProfileFragment extends Fragment {

    public MyProfileFragment(){

    }

    UserDashboard dashboard;

    private static final String URL_USER_DATA = "https://community.kreastol.club/android/getUserData.php";

    TextView hello, name1, name2;
    EditText emailModify, fNameModify, lNameModify;
    Button saveBtn, cancelBtn;
    SessionManager sessionManager;

    SwipeRefreshLayout swipeRefreshLayout;

    private List<Object> dataList = new ArrayList<>();

    public String username, firstName, lastName, email, birthday;

    private Context mContext;
    int gender;
    final String TAG = this.getClass().getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Getting the seession for login
        sessionManager = new SessionManager(mContext);
         dashboard = new UserDashboard();

        if (sessionManager.isLoggedIn()) {
            username = sessionManager.getUsername();

            String lang = Locale.getDefault().getLanguage();

           DatabaseConnect connect = new DatabaseConnect(mContext);

            name1 = view.findViewById(R.id.profile_hello_name1);
            name2 = view.findViewById(R.id.profile_hello_name2);
            hello = view.findViewById(R.id.profile_current_hello);
            fNameModify = view.findViewById(R.id.profile_first_name);
            lNameModify = view.findViewById(R.id.profile_last_name);
            emailModify = view.findViewById(R.id.profile_email);
            cancelBtn = view.findViewById(R.id.profile_cancel_button);
            saveBtn = view.findViewById(R.id.profile_save_button);
            cancelBtn.setEnabled(true);
            saveBtn.setEnabled(false);

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.time_refresh);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    tellHello();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 300);
                }
            });
            getUserDetails(view, lang);
            //time specific hello
            tellHello();
        } else {

        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        sessionManager = new SessionManager(getContext());

        if (!sessionManager.isLoggedIn()) {
            v = inflater.inflate(R.layout.profile_please_login, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        }
        return v;
    }



    public void tellHello() {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int i = 3;

        if (hour > 2 && hour <= 10) {
            i = -1;  //morning
        }
        if (hour > 10 && hour <= 17) {
            i = 0; // day
        }
        if ((hour > 17 && hour <= 23) ||
                (hour >= 0 && hour <= 2)) {
            i = 1; // evening
        }

        switch (i) {
            case -1:
                hello.setText(getResources().getString(R.string.profile_morning));
                break;
            case 0:
                hello.setText(getResources().getString(R.string.profile_day));
                break;
            case 1:
                hello.setText(getResources().getString(R.string.profile_evening));
                break;
            default:
                hello.setText("Hello,");
                break;
        }
    }


    /*public void onResume(){
        super.onResume();
        getString(new VolleyCallback(){
            @Override
            public void onSuccess(String result){
                System.out.println(result); // returns the value of lastInsertId
            }
        });
    }*/

    void getUserDetails(View view, String lang) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


    StringRequest request = new StringRequest(Request.Method.POST, URL_USER_DATA, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Arrived");
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("Username", username);
                    Iterator<String> iter = jsonObject.keys();

                    while (iter.hasNext()) {
                        String key = iter.next();
                        Log.d(TAG, "Json key: " + key);
                        Object value = jsonObject.get(key);
                        dataList.add(value);
                    }

                    dashboard.firstName = dataList.get(0).toString();
                    dashboard.lastName = dataList.get(1).toString();
                    dashboard.email = dataList.get(2).toString();
                    dashboard.birthday = dataList.get(3).toString();
                    dashboard.gender = Integer.valueOf(dataList.get(4).toString());
                    viewSetup(lang, view);

                } catch (JSONException e) {

                    e.printStackTrace();
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
                params.put("username", username);
                return params;
            }
        };

        Volley.newRequestQueue(mContext).add(request);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void viewSetup(String lang, View view) {

        TextInputLayout fNameTil = (TextInputLayout)view.findViewById(R.id.profile_first_name_til);
        TextInputLayout lNameTil = (TextInputLayout)view.findViewById(R.id.profile_last_name_til);
        TextInputLayout emailTil = (TextInputLayout)view.findViewById(R.id.profile_email_til);

        fNameTil.setHelperTextEnabled(false);
        lNameTil.setHelperTextEnabled(false);
        emailTil.setHelperTextEnabled(false);

        /*if (dataList.size() > 0) {
            firstName = dataList.get(0).toString();
            lastName = dataList.get(1).toString();
            email = dataList.get(2).toString();
            birthday = dataList.get(3).toString();
            gender = Integer.valueOf(dataList.get(4).toString());
        } else {
            Log.d(TAG, "No datalis for you Profile");
        }*/

        lNameModify.setText(dashboard.lastName);
        fNameModify.setText(dashboard.firstName);
        emailModify.setText(dashboard.email);

        Typeface typeface = getResources().getFont(R.font.custom_bold);
        Typeface typeface2 = getResources().getFont(R.font.custom_regular);
        if (lang.equals("ja")) {
            name1.setTypeface(typeface2);
            name2.setTypeface(typeface);
            name2.setText(sessionManager.getUsername());
            name1.setText(getResources().getString(R.string.honorific_male));

        } else {

            name1.setTypeface(typeface);
            name2.setTypeface(typeface2);
            name1.setText(sessionManager.getUsername());
            if (gender == 1) {
                name2.setText(getResources().getString(R.string.honorific_female));
            } else {
                name2.setText(getResources().getString(R.string.honorific_male));
            }
            RelativeLayout.LayoutParams name2Params = (RelativeLayout.LayoutParams) name2.getLayoutParams();
            RelativeLayout.LayoutParams name1Params = (RelativeLayout.LayoutParams) name1.getLayoutParams();

            name1Params.addRule(RelativeLayout.RIGHT_OF, R.id.profile_hello_name2);
            name1.setLayoutParams(name1Params);

            name2Params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            name2.setLayoutParams(name2Params);
        }

    }

    public String sendName(){
        return this.firstName;
    }


}