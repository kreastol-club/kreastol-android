package club.kreastol.community.User.DashboardFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import club.kreastol.community.R;
import club.kreastol.community.User.UserDashboard;
import club.kreastol.community.Volley.LinkHolder;
import club.kreastol.community.util.AppRunSession;
import club.kreastol.community.util.UserProfilSession;


public class MyProfileFragment extends Fragment {

    public MyProfileFragment(){

    }

    UserDashboard dashboard;
    LinkHolder links = new LinkHolder();

    TextView hello, usernameHello, userPoints;
    EditText emailModify, fNameModify, lNameModify;
    Button saveBtn, cancelBtn;

    //Sessions
    private UserProfilSession userProfile;
    private AppRunSession appRun;

    SwipeRefreshLayout swipeRefreshLayout;

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
         dashboard = new UserDashboard();

        if (userProfile.isLoggedIn()) {
            username = userProfile.getUsername();

            String lang = appRun.getLanguage();


            usernameHello = view.findViewById(R.id.profile_hello_username);
            userPoints = view.findViewById(R.id.user_points);
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
        userProfile = new UserProfilSession(getContext());
        appRun = new AppRunSession(getContext());

        if (!userProfile.isLoggedIn()) {
            v = inflater.inflate(R.layout.activity_login_from_profile, container, false);
        } else {
            v = inflater.inflate(R.layout.fragment_dashboard_profile, container, false);
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

    void getUserDetails(View view, String lang) {

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


    StringRequest request = new StringRequest(Request.Method.POST, links.LOGGED_IN_USER_DATA, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response Arrived");
                Log.d(TAG, response);
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("Username", username);
                    dashboard.firstName = jsonObject.getString("first_name");
                    dashboard.lastName = jsonObject.getString("last_name");
                    dashboard.email = jsonObject.getString("email");
                    dashboard.birthday = jsonObject.getString("birthday");
                    dashboard.gender = jsonObject.getInt("gender");
                    dashboard.points = jsonObject.optInt("points", 0);
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
                params.put("username", userProfile.getUsername());
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

        lNameModify.setText(dashboard.lastName);
        fNameModify.setText(dashboard.firstName);
        emailModify.setText(dashboard.email);
        userPoints.setText(getString(R.string.profile_user_information_points_sum, dashboard.points));


        Typeface typeface = getResources().getFont(R.font.custom_bold);
        Typeface typeface2 = getResources().getFont(R.font.custom_regular);
        if (lang.equals("ja")) {
            usernameHello.setText(getString(R.string.profile_username, username, getString(R.string.honorific_male)));

        } else {
            if (gender == 1) {
                usernameHello.setText(getString(R.string.profile_username, getString(R.string.honorific_female), username));
            } else {
                usernameHello.setText(getString(R.string.profile_username, getString(R.string.honorific_male), username));
            }
//            RelativeLayout.LayoutParams name2Params = (RelativeLayout.LayoutParams) name2.getLayoutParams();
//            RelativeLayout.LayoutParams name1Params = (RelativeLayout.LayoutParams) name1.getLayoutParams();
//
//            name1Params.addRule(RelativeLayout.RIGHT_OF, R.id.profile_hello_name2);
//            name1.setLayoutParams(name1Params);
//
//            name2Params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
//            name2.setLayoutParams(name2Params);
        }
    }

    public String sendName(){
        return this.firstName;
    }


}